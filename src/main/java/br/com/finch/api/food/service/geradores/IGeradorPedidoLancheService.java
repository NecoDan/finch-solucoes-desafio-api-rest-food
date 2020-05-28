package br.com.finch.api.food.service.geradores;

import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.model.dtos.FilterPedido;
import br.com.finch.api.food.model.dtos.PedidosWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.util.List;

public interface IGeradorPedidoLancheService {

    PedidosWrapper adicionarItemLanchePedido(FilterPedido filterPedido) throws ValidadorException;

    PedidosWrapper adicionarIngredienteAdicionaAItemPedido(FilterPedido filterPedido) throws ValidadorException;

    PedidosWrapper gerar(List<Pedido> pedidos) throws ValidadorException;

    Pedido gerar(Pedido pedido) throws ValidadorException;

    ItemPedido gerarItemPedido(Integer item, ItemPedido itemPedido) throws ValidadorException;

    ItemPedido gerarAdicionalItemPedido(ItemPedido itemPedido) throws ValidadorException;
}
