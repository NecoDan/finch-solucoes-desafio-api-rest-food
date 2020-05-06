package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.LanchePedido;

import java.math.BigDecimal;

public interface IRegraCalculoPromocao {
    BigDecimal calculaDesconto(LanchePedido pedido);

    double calcularDesconto(LanchePedido pedido);
}
