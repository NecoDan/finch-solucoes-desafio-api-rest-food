package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.service.negocio.Promocao;

import java.math.BigDecimal;

public interface IFactoryDescontoService {

    boolean isAppliable(BigDecimal quantidade);

    Promocao obterPromocao(BigDecimal quantidade);
}
