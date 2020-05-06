package br.com.finch.api.food.model.domain;

import br.com.finch.api.food.model.Lanche;

public abstract class IngredienteDecorator extends Lanche {

    public IngredienteDecorator(){ }

    public abstract String getDescricaoAdicional();
}
