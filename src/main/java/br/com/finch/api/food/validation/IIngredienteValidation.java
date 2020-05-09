package br.com.finch.api.food.validation;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.util.exceptions.ValidadorException;

public interface IIngredienteValidation {

    void validarApenasObjIngrediente(Ingrediente ingrediente) throws ValidadorException;

    void validarIdReferenteIngrediente(Ingrediente ingrediente) throws ValidadorException;

    void validarIngrediente(Ingrediente ingrediente) throws ValidadorException;
}
