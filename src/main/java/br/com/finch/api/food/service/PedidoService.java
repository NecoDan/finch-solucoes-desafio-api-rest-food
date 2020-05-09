package br.com.finch.api.food.service;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.repository.AdicionaltemPedidoRepository;
import br.com.finch.api.food.repository.ItemPedidoRepository;
import br.com.finch.api.food.repository.PedidoRepository;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import br.com.finch.api.food.validation.PedidoValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    private final PedidoValidation pedidoValidation;

    @Override
    @Transactional
    public Pedido salvar(Pedido pedido) throws ValidadorException {
        this.pedidoValidation.validarSomentePedido(pedido);
        return this.pedidoRepository.save(pedido);
    }

    @Transactional
    public ItemPedido salvarItem(ItemPedido itemPedido) throws ValidadorException {
        this.pedidoValidation.validarSomenteItemPedidoLanche(itemPedido);
        return this.itemPedidoRepository.saveAndFlush(itemPedido);
    }

    @Override
    @Transactional
    public void salvarAllAdicionalItemLanche(List<AdicionalItemPedido> adicionalItemPedidos) {
        this.adicionaltemPedidoRepository.saveAll(adicionalItemPedidos);
    }
}
