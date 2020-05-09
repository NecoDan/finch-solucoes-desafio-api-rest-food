package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.ItemPedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface IRegraCalculoPromocao {
    BigDecimal calculaDescontoNaPromocao(ItemPedido itemPedido);

    double calcularDescontoNaPromocao(ItemPedido itemPedido);
}
