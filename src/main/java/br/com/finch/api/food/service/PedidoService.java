package br.com.finch.api.food.service;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.repository.AdicionaltemPedidoRepository;
import br.com.finch.api.food.repository.ItemPedidoRepository;
import br.com.finch.api.food.repository.PedidoRepository;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import br.com.finch.api.food.validation.ILancheValidation;
import br.com.finch.api.food.validation.IPedidoValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Daniel Santos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final AdicionaltemPedidoRepository adicionaltemPedidoRepository;
    private final IPedidoValidation pedidoValidation;
    private final ILancheValidation lancheValidation;

    @Override
    public Pedido recuperarPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Pedido> recuperar(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    @Transactional
    public Pedido salvar(Pedido pedido) throws ValidadorException {
        this.pedidoValidation.validarSomentePedido(pedido);
        return this.pedidoRepository.save(pedido);
    }

    @Override
    public Integer recuperarUltimoValorItemAdicionado(Long idPedido) {
        return this.itemPedidoRepository.recuperarUltimoValorItemAdicionado(idPedido);
    }

    @Transactional
    public ItemPedido salvarItem(ItemPedido itemPedido) throws ValidadorException {
        this.pedidoValidation.validarSomenteItemPedidoLanche(itemPedido);
        return this.itemPedidoRepository.saveAndFlush(itemPedido);
    }

    @Override
    public ItemPedido recuperarItemPedidoPor(Pedido pedido, Lanche lanche) throws ValidadorException {
        this.pedidoValidation.validarSomentePedido(pedido);
        this.lancheValidation.validaLanche(lanche);
        return itemPedidoRepository.findByPedidoAndLanche(pedido, lanche);
    }

    @Override
    @Transactional
    public void salvarAllAdicionalItemLanche(List<AdicionalItemPedido> adicionalItemPedidos) {
        this.adicionaltemPedidoRepository.saveAll(adicionalItemPedidos);
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

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido atualizarPedido(Pedido pedido) throws ValidadorException {
        Pedido pedidoAtualizar = this.recalcularValorTotalPedido(pedido);
        if (Objects.nonNull(pedidoAtualizar))
            this.pedidoRepository.save(pedidoAtualizar);
        return pedidoAtualizar;
    }
}
