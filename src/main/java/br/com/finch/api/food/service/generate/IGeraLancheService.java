package br.com.finch.api.food.service.generate;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.dtos.LanchesWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.math.BigDecimal;
import java.util.List;

public interface IGeraLancheService {

    LanchesWrapper adicionarIngrediente(Long lancheId, BigDecimal qtde, Ingrediente ingrediente) throws ValidadorException;

    LanchesWrapper gerar(List<Lanche> lanches) throws ValidadorException;

    boolean removerIngrediente(Long lancheId, Ingrediente ingrediente) throws ValidadorException;

    Lanche recalcularValorTotalLanche(Lanche lanche) throws ValidadorException;

    boolean atualizarLanchePor(Long lancheId, Lanche lanche) throws ValidadorException;

    boolean excluirLanche(Lanche lanche) throws ValidadorException;
}
