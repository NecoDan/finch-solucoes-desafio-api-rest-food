package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.service.negocio.Promocao;

public interface IFactoryPromocaoService {

    boolean isAppliable(ItemPedido itemPedido);

    Promocao obterPromocao(ItemPedido itemPedido);
}
