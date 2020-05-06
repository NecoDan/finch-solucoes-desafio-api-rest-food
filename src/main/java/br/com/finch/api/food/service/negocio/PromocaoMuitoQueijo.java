package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.LanchePedido;

import java.math.BigDecimal;

public class PromocaoMuitoQueijo implements IRegraCalculoPromocao{

    @Override
    public BigDecimal calculaDesconto(LanchePedido pedido) {
        return null;
    }

    @Override
    public double calcularDesconto(LanchePedido pedido) {
        return 0;
    }
}
