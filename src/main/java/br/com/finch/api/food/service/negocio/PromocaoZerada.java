package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.ItemPedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromocaoZerada implements IRegraCalculoPromocao {

    @Override
    public BigDecimal calculaDescontoNaPromocao(ItemPedido itemPedido) {
        return BigDecimal.ZERO;
    }

    @Override
    public double calcularDescontoNaPromocao(ItemPedido itemPedido) {
        return calculaDescontoNaPromocao(itemPedido).doubleValue();
    }
}
