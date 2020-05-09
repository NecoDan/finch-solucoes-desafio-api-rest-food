package br.com.finch.api.food.validation;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class IngredienteValidation implements IIngredienteValidation {

    @Override
    public void validarApenasObjIngrediente(Ingrediente ingrediente) throws ValidadorException {
        if (Objects.isNull(ingrediente))
            throw new ValidadorException("Ingrediente encontra-se inválido e/ou inexistente [NULO].");
    }

    @Override
    public void validarIdReferenteIngrediente(Ingrediente ingrediente) throws ValidadorException {
        if (Objects.isNull(ingrediente.getId()))
            throw new ValidadorException("Ingrediente encontra-se com a referência do [ID] inválida e/ou inexistente [NULA].");
    }

    @Override
    public void validarIngrediente(Ingrediente ingrediente) throws ValidadorException {
        validarApenasObjIngrediente(ingrediente);

        if (Objects.isNull(ingrediente.getDescricao()) || ingrediente.getDescricao().isEmpty())
            throw new ValidadorException("Descrição referente ao Ingrediente encontra-se inválido e/ou inexistente [NULA].");

        if (Objects.isNull(ingrediente.getCusto()) || ingrediente.getCusto().compareTo(BigDecimal.ZERO) == 0)
            throw new ValidadorException("O valor de custo referente ao Ingrediente encontra-se inexistente [NULO] e/ou igual à zero (0).");
    }
}
