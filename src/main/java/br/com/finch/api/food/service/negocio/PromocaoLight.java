package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.ItemPedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class PromocaoLight implements IRegraCalculoPromocao {

    private final static double VALOR_DESCONTO_DEZ_POR_CENTO = 10.00;

    @Override
    public BigDecimal calculaDescontoNaPromocao(ItemPedido itemPedido) {
        if (Objects.isNull(itemPedido) || Objects.isNull(itemPedido.getValorTotal()))
            return BigDecimal.ZERO;
        return itemPedido.getValorTotal().multiply(BigDecimal.valueOf(VALOR_DESCONTO_DEZ_POR_CENTO / 100)).setScale(2, BigDecimal.ROUND_UP);
    }

    @Override
    public double calcularDescontoNaPromocao(ItemPedido itemPedido) {
        return this.calculaDescontoNaPromocao(itemPedido).doubleValue();
    }
}
