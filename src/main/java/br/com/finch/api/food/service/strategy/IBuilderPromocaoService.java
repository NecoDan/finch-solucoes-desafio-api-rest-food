package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.service.negocio.Promocao;

import java.math.BigDecimal;

public interface IBuilderPromocaoService {
    Promocao obterPromocaoAPartir(ItemPedido itemPedido);
}
