package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.LanchePedido;

import java.math.BigDecimal;

public class PromocaoZerada implements IRegraCalculoPromocao {

    @Override
    public BigDecimal calculaDesconto(LanchePedido pedido) {
        return BigDecimal.ZERO;
    }

    @Override
    public double calcularDesconto(LanchePedido pedido) {
        return calculaDesconto(pedido).doubleValue();
    }
}
