package br.com.finch.api.food.service.generate;

import br.com.finch.api.food.model.*;
import br.com.finch.api.food.model.dtos.FilterPedido;
import br.com.finch.api.food.model.dtos.PedidosWrapper;
import br.com.finch.api.food.service.IIngredienteService;
import br.com.finch.api.food.service.ILancheService;
import br.com.finch.api.food.service.IPedidoService;
import br.com.finch.api.food.service.negocio.Promocao;
import br.com.finch.api.food.service.strategy.IBuilderPromocaoService;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import br.com.finch.api.food.validation.IIngredienteValidation;
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
@Slf4j
@RequiredArgsConstructor
public class GeraPedidoLancheService implements IGeraPedidoLancheService {

    private final IPedidoService pedidoService;
    private final ILancheService lancheService;
    private final IIngredienteService ingredienteService;
    private final IBuilderPromocaoService builderPromocaoService;
    private final ILancheValidation lancheValidation;
    private final IPedidoValidation pedidoValidation;
    private final IIngredienteValidation ingredienteValidation;

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
    public Pedido gerar(Pedido pedido) throws ValidadorException {
        Pedido pedidoNovo = null;

        if (Objects.nonNull(pedido) && Objects.nonNull(pedido.getId()))
            pedidoNovo = this.pedidoService.recuperarPorId(pedido.getId());

        if (Objects.nonNull(pedidoNovo))
            return pedidoNovo;

        pedidoNovo = pedido;
        pedidoValidation.validarParamsEDependenciasPedido(pedidoNovo);
        assert pedidoNovo != null;
        pedidoNovo.gerarDataCorrente();
        pedidoNovo.ativado();
        pedidoNovo = this.pedidoService.salvar(pedidoNovo);

        int item = 1;
        for (ItemPedido itemPedido : pedidoNovo.getItens()) {
            itemPedido.setPedido(pedidoNovo);
            gerarItemPedido(item, itemPedido);
            item++;
        }
        return this.pedidoService.atualizarPedido(pedidoNovo);
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

        Promocao promocao = this.builderPromocaoService.obterPromocaoAPartir(itemPedido);
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
                .valorCusto(ingrediente.getCusto())
                .dataCadastro(LocalDateTime.now())
                .ativo(Boolean.TRUE)
                .build();
    }

    @Override
    public PedidosWrapper adicionarItemLanchePedido(FilterPedido filterPedido) throws ValidadorException {
        PedidosWrapper pedidosWrapper = PedidosWrapper.builder().build();

        FilterPedido filterPedidoNovo = extrairFilterPedidoContendoDependenciaValidas(filterPedido, false);
        ItemPedido itemPedido = extrairItemPedido(filterPedidoNovo);
        Pedido pedido = itemPedido.getPedido();

        processarItem(itemPedido);
        pedidosWrapper.add(pedidoService.atualizarPedido(pedido));
        return pedidosWrapper;
    }

    private ItemPedido extrairItemPedido(FilterPedido filterPedido) throws ValidadorException {
        Pedido pedido = filterPedido.getPedido();
        Lanche lanche = filterPedido.getLanche();

        ItemPedido itemPedido = this.pedidoService.recuperarItemPedidoPor(pedido, lanche);
        itemPedido = Objects.isNull(itemPedido) ? createNovoItemPedido(pedido, lanche, filterPedido.getQtde()) : updateItemPedidoAPartir(itemPedido, filterPedido.getQtde());

        return itemPedido;
    }

    private FilterPedido extrairFilterPedidoContendoDependenciaValidas(FilterPedido filterPedido, boolean retornaIngrediente) throws ValidadorException {
        pedidoValidation.validarParamsFilterPedidoAddItemPedido(filterPedido);

        Pedido pedido = pedidoService.recuperarPorId(filterPedido.getIdPedido());
        pedidoValidation.validarSomentePedido(pedido);

        Lanche lanche = recuperarLanche(Lanche.builder().id(filterPedido.getIdLanche()).build());
        lancheValidation.validarSomenteLanche(lanche);

        FilterPedido filterPedidoNovo = FilterPedido.builder()
                .qtde(filterPedido.getQtde())
                .pedido(pedido)
                .lanche(lanche)
                .build();

        if (retornaIngrediente && Objects.nonNull(filterPedido.getIdIngrediente()))
            filterPedido.setIngrediente(ingredienteService.recuperarPorId(filterPedido.getIdIngrediente()));

        return filterPedidoNovo;
    }

    @Override
    public PedidosWrapper adicionarIngredienteAdicionaAItemPedido(FilterPedido filterPedido) throws ValidadorException {
        FilterPedido filterPedidoNovo = extrairFilterPedidoContendoDependenciaValidas(filterPedido, false);

        ItemPedido itemPedido = extrairItemPedido(filterPedidoNovo);
        itemPedido.add(mountAdicionalItem(filterPedido.getIdIngrediente(), filterPedido.getQtde(), itemPedido));

        Pedido pedido = itemPedido.getPedido();

        processarItem(itemPedido);
        return PedidosWrapper.builder().build().adicionar(pedidoService.atualizarPedido(pedido));
    }

    private ItemPedido updateItemPedidoAPartir(ItemPedido itemPedido, BigDecimal qtde) {
        itemPedido.setQuantidade(qtde);
        itemPedido.gerarDataCorrente();
        itemPedido.ativado();
        return itemPedido;
    }

    private ItemPedido createNovoItemPedido(Pedido pedido, Lanche lanche, BigDecimal qtde) throws ValidadorException {
        this.pedidoValidation.validarParamsFilterPedidoAddItemPedidoWithInstancias(FilterPedido.builder()
                .pedido(pedido)
                .lanche(lanche)
                .qtde(qtde).build());

        Integer valorUltimoItemAdicionado = this.pedidoService.recuperarUltimoValorItemAdicionado(pedido.getId());
        ItemPedido itemPedido = ItemPedido.builder()
                .lanche(lanche)
                .pedido(pedido)
                .item(valorUltimoItemAdicionado + 1)
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
            return lancheService.recuperarPorId(lanche.getId());
        return lanche;
    }

    private boolean isDeveRecuperarLancheCompleto(Lanche lanche) {
        return (Objects.isNull(lanche.getValorTotal()) || lanche.getValorTotal().compareTo(BigDecimal.ZERO) == 0);
    }
}
