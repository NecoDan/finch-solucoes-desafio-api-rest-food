package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.LanchePedido;

import java.math.BigDecimal;
import java.util.Objects;

public class PromocaoLight implements IRegraCalculoPromocao {

    private final static double VALOR_DESCONTO_CINCO = 10.00;

    @Override
    public BigDecimal calculaDesconto(LanchePedido pedido) {
        if (Objects.isNull(pedido) || Objects.isNull(pedido.getValorTotal()))
            return BigDecimal.ZERO;
        return pedido.getValorTotal().multiply(BigDecimal.valueOf(VALOR_DESCONTO_CINCO / 100));
    }

    @Override
    public double calcularDesconto(LanchePedido pedido) {
        return this.calculaDesconto(pedido).doubleValue();
    }
}
