package br.com.finch.api.food.service;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.util.List;

/**
 * @author Daniel Santos
 */
public interface IPedidoService {

    Pedido salvar(Pedido pedido) throws ValidadorException;

    ItemPedido salvarItem(ItemPedido itemPedido) throws ValidadorException;

    void salvarAllAdicionalItemLanche(List<AdicionalItemPedido> adicionalItemPedidos) throws ValidadorException;
}
