package br.com.finch.api.food.service;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.util.List;
import java.util.Optional;

/**
 * @author Daniel Santos
 */
public interface IPedidoService {

    Pedido atualizarPedido(Pedido pedido) throws ValidadorException;

    Pedido recuperarPorId(Long id);

    Optional<Pedido> recuperar(Long id);

    Pedido salvar(Pedido pedido) throws ValidadorException;

    Integer recuperarUltimoValorItemAdicionado(Long idPedido);

    ItemPedido salvarItem(ItemPedido itemPedido) throws ValidadorException;

    ItemPedido recuperarItemPedidoPor(Pedido pedido, Lanche lanche) throws ValidadorException;

    void salvarAllAdicionalItemLanche(List<AdicionalItemPedido> adicionalItemPedidos) throws ValidadorException;

    Pedido recalcularValorTotalPedido(Pedido pedido) throws ValidadorException;

    Pedido recalcularValorTotalPedido(Long idPedido);
}
