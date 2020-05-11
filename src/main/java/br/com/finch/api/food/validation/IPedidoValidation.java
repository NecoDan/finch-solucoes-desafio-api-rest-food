package br.com.finch.api.food.validation;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.model.dtos.FilterPedido;
import br.com.finch.api.food.util.exceptions.ValidadorException;

public interface IPedidoValidation {

    void validarSomentePedido(Pedido pedido) throws ValidadorException;

    void validarParamsFilterPedidoAddItemPedido(FilterPedido filterPedido) throws ValidadorException;

    void validarParamsFilterPedidoAddItemPedidoWithInstancias(FilterPedido filterPedido) throws ValidadorException;

    void validarIdReferentePedido(Pedido pedido) throws ValidadorException;

    void validarSomenteItemPedidoLanche(ItemPedido itemPedido) throws ValidadorException;

    void validarIdReferenteItemPedido(ItemPedido itemPedido) throws ValidadorException;

    void validarSomenteAdicionalItem(AdicionalItemPedido adicionalItemPedido) throws ValidadorException;

    void validarIdReferenteAdicionalItem(AdicionalItemPedido adicionalItemPedido) throws ValidadorException;

    void validarParamsEDependenciasPedido(Pedido pedido) throws ValidadorException;
}
