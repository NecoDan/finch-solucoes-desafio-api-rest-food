package br.com.finch.api.food.service;

import br.com.finch.api.food.model.*;
import br.com.finch.api.food.model.dtos.FilterPedido;
import br.com.finch.api.food.model.dtos.PedidosWrapper;
import br.com.finch.api.food.repository.ItemPedidoRepository;
import br.com.finch.api.food.repository.PedidoRepository;
import br.com.finch.api.food.service.negocio.Promocao;
import br.com.finch.api.food.service.strategy.IBuilderPromocaoService;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import br.com.finch.api.food.validation.ILancheValidation;
import br.com.finch.api.food.validation.IPedidoValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeradorPedidoLancheService implements IGeradorPedidoLancheService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final IPedidoService pedidoService;
    private final ILancheService lancheService;
    private final IIngredienteService ingredienteService;
    private final IBuilderPromocaoService builderPromocaoService;
    private final ILancheValidation lancheValidation;
    private final IPedidoValidation pedidoValidation;

    @Override
    public PedidosWrapper gerar(List<Pedido> pedidos) throws ValidadorException {
        if (Objects.isNull(pedidos) || pedidos.isEmpty())
            throw new ValidadorException("Não existem pedido(s) ou pedido(s) encontram-se inexistente(s) a serem processados pela API.");

        PedidosWrapper lanchesWrapper = PedidosWrapper.builder().build();
        for (Pedido pedido : pedidos)
            lanchesWrapper.add(gerar(pedido));
        return lanchesWrapper;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido gerar(Pedido pedidoNovo) throws ValidadorException {
        pedidoValidation.validarParamsEDependenciasPedido(pedidoNovo);
        pedidoNovo.gerarDataCorrente();
        pedidoNovo.ativado();
        pedidoNovo = this.pedidoService.salvar(pedidoNovo);

        int item = 0;
        for (ItemPedido itemPedido : pedidoNovo.getItens()) {
            item++;
            itemPedido.setPedido(pedidoNovo);
            gerarItemPedido(item, itemPedido);
        }
        return atualizarPedido(pedidoNovo);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido atualizarPedido(Pedido pedido) throws ValidadorException {
        Pedido pedidoAtualizar = recalcularValorTotalPedido(pedido);
        if (Objects.nonNull(pedidoAtualizar))
            this.pedidoRepository.save(pedidoAtualizar);
        return pedidoAtualizar;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public ItemPedido gerarItemPedido(Integer item, ItemPedido itemPedido) throws ValidadorException {
        Lanche lanche = recuperarLanche(itemPedido.getLanche());
        this.lancheValidation.validaLanche(lanche);

        itemPedido.setItem(item);
        itemPedido.setLanche(lanche);
        itemPedido.getQuantidadeGarantida();
        itemPedido.gerarDataCorrente();
        itemPedido.ativado();
        return processarItem(itemPedido);
    }

    @Transactional
    protected ItemPedido processarItem(ItemPedido itemPedido) throws ValidadorException {
        itemPedido = this.pedidoService.salvarItem(itemPedido);
        gerarAdicionalItemPedido(itemPedido);
        itemPedido.calculaValorTotal();

        Promocao promocao = builderPromocaoService.obterPromocaoAPartir(itemPedido);
        itemPedido.aplicarPromocao(promocao);
        itemPedido = this.pedidoService.salvarItem(itemPedido);

        return itemPedido;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public ItemPedido gerarAdicionalItemPedido(ItemPedido itemPedido) throws ValidadorException {
        if (Objects.isNull(itemPedido.getAdicionais()) || itemPedido.getAdicionais().isEmpty())
            return itemPedido;

        List<AdicionalItemPedido> adicionalItemPedidoList = itemPedido.getAdicionais().stream()
                .filter(AdicionalItemPedido::isParamValidosFilterIngredientes)
                .map(adicionalItemPedido -> mountAdicionalItem(adicionalItemPedido.getIngrediente().getId(), adicionalItemPedido.getQuantidade(), itemPedido))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        this.pedidoService.salvarAllAdicionalItemLanche(adicionalItemPedidoList);
        return itemPedido;
    }

    private AdicionalItemPedido mountAdicionalItem(Long idIngrediente, BigDecimal qtde, ItemPedido itemPedido) {
        Ingrediente ingrediente = ingredienteService.recuperarPorId(idIngrediente);

        if (Objects.isNull(ingrediente))
            return null;

        return AdicionalItemPedido.builder()
                .ingrediente(ingrediente)
                .itemPedido(itemPedido)
                .quantidade(qtde)
                .dataCadastro(LocalDateTime.now())
                .ativo(Boolean.TRUE)
                .build();
    }

    @Override
    public PedidosWrapper adicionarItemLanchePedido(FilterPedido filterPedido) throws ValidadorException {
        PedidosWrapper pedidosWrapper = PedidosWrapper.builder().build();
        pedidoValidation.validarParamsFilterPedidoAddItemPedido(filterPedido);

        Pedido pedido = pedidoRepository.findById(filterPedido.getIdPedido()).get();
        pedidoValidation.validarSomentePedido(pedido);

        Lanche lanche = recuperarLanche(Lanche.builder().id(filterPedido.getIdLanche()).build());
        lancheValidation.validarSomenteLanche(lanche);

        ItemPedido itemPedido = itemPedidoRepository.findByPedidoAndLanche(pedido, lanche);

        itemPedido = Objects.isNull(itemPedido) ? createNovoItemPedido(pedido, lanche, filterPedido.getQtde())
                : updateItemPedidoAPartir(itemPedido, filterPedido.getQtde());

        processarItem(itemPedido);
        pedidosWrapper.add(atualizarPedido(pedido));
        return pedidosWrapper;
    }

    private ItemPedido updateItemPedidoAPartir(ItemPedido itemPedido, BigDecimal qtde) {
        itemPedido.setQuantidade(qtde);
        itemPedido.gerarDataCorrente();
        itemPedido.ativado();
        return itemPedido;
    }

    private ItemPedido createNovoItemPedido(Pedido pedido, Lanche lanche, BigDecimal qtde) {
        Integer valorUltimoItemAdicionado = itemPedidoRepository.recuperarUltimoValorItemAdicionado(pedido.getId());
        ItemPedido itemPedido = ItemPedido.builder()
                .lanche(lanche)
                .pedido(pedido)
                .item(valorUltimoItemAdicionado)
                .quantidade(qtde)
                .build();
        itemPedido.gerarDataCorrente();
        itemPedido.ativado();

        return itemPedido;
    }

    private Lanche recuperarLanche(Lanche lanche) throws ValidadorException {
        if (Objects.isNull(lanche) || Objects.isNull(lanche.getId()))
            return null;

        if (isDeveRecuperarLancheCompleto(lanche))
            lanche = lancheService.recuperarPorId(lanche.getId());

        return lanche;
    }

    private boolean isDeveRecuperarLancheCompleto(Lanche lanche) {
        return (Objects.isNull(lanche.getValorTotal()) || lanche.getValorTotal().compareTo(BigDecimal.ZERO) == 0);
    }

    @Override
    public Pedido recalcularValorTotalPedido(Pedido pedido) throws ValidadorException {
        this.pedidoValidation.validarIdReferentePedido(pedido);
        return recalcularValorTotalPedido(pedido.getId());
    }

    @Override
    public Pedido recalcularValorTotalPedido(Long idPedido) {
        return this.pedidoRepository.findById(idPedido)
                .map(p -> {
                    p.calculaValorItens();
                    p.calcularValorTotalDesconto();
                    p.calculaValorTotal();
                    p.gerarDataCorrente();
                    return p;
                }).orElse(null);
    }
}
