package br.com.finch.api.food.validation;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class LancheValidation {

    public void validarApenasObjLanche(Lanche lanche) throws ValidadorException {
        if (Objects.isNull(lanche))
            throw new ValidadorException("Lanche encontra-se inválido e/ou inexistente [NULO].");
    }

    public void validarIdReferenteLanche(Lanche lanche) throws ValidadorException {
        if (Objects.isNull(lanche.getId()))
            throw new ValidadorException("Lanche encontra-se com a referência do [ID] inválida e/ou inexistente [NULA].");
    }

    public void validarLanche(Lanche lanche) throws ValidadorException {
        validarApenasObjLanche(lanche);

        if (Objects.isNull(lanche.getDescricao()) || lanche.getDescricao().isEmpty())
            throw new ValidadorException("Descrição referente ao Lanche encontra-se inválido e/ou inexistente [NULA].");

        if (Objects.isNull(lanche.getValorTotal()) || lanche.getValorTotal().compareTo(BigDecimal.ZERO) != 0)
            throw new ValidadorException("O valor total referente ao Lanche encontra-se inexistente [NULO] e/ou igual à zero (0).");
    }

    public void validarParametrosActionReferenteIngredienteLanche(Long lancheId, Ingrediente ingrediente) throws ValidadorException {
        if (Objects.isNull(lancheId))
            throw new ValidadorException("Parametro com a referência [ID] ao Lanche, encontra-se inválida e/ou inexistente [NULA].");

        if (Objects.isNull(ingrediente) || Objects.isNull(ingrediente.getId()))
            throw new ValidadorException("Não existem ingrediente(s) ou ingredientes(s) encontram-se inexistente(s) a serem processados pela API.");
    }
}
