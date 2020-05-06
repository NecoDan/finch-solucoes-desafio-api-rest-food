package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.service.negocio.Promocao;

import java.math.BigDecimal;

public interface IBuilderDescontoService {
    Promocao obterPromocaoAPartir(BigDecimal quantidade);
}
