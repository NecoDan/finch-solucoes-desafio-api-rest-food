package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.ItemPedido;

import java.math.BigDecimal;

public interface ICalculadoraDescontoQtdePorcoesService {
    BigDecimal efetuarCalculoDescontoNaPromocaoPor(ItemPedido itemPedido, BigDecimal qtde);

    boolean isParamInvalidos(ItemPedido itemPedido);
}
