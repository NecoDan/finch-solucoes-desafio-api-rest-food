package br.com.finch.api.food.validation;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.util.exceptions.ValidadorException;

public interface ILancheValidation {

    void validarSomenteLanche(Lanche lanche) throws ValidadorException;

    void validarIdReferenteLanche(Lanche lanche) throws ValidadorException;

    void validarLanche(Lanche lanche) throws ValidadorException;

    boolean validaLanche(Lanche lanche);

    void validarParametrosActionReferenteIngredienteLanche(Long lancheId, Ingrediente ingrediente) throws ValidadorException;
}
