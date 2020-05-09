package br.com.finch.api.food.service;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.dtos.LanchesWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ILancheService {

    boolean atualizarPor(Long lancheId, Lanche lanche) throws ValidadorException;

    LanchesWrapper adicionarIngrediente(Long lancheId, BigDecimal qtde, Ingrediente ingrediente) throws ValidadorException;

    boolean excluir(Lanche lanche) throws ValidadorException;

    LanchesWrapper gerar(List<Lanche> lanches) throws ValidadorException;

    List<Lanche> recuperarAtivos();

    List<Lanche> recuperarInativos();

    Lanche recuperarPorId(Long id) throws ValidadorException;

    List<Lanche> recuperarTodos();

    List<Lanche> recuperarPorDescricao(String descricao) throws ValidadorException;

    List<Lanche> recuperarTodosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException;

    List<Lanche> recuperarPorDataInsercao(LocalDate data) throws ValidadorException;

    boolean removerIngrediente(Long lancheId, Ingrediente ingrediente) throws ValidadorException;

    Lanche recalcularValorTotalLanche(Lanche lanche) throws ValidadorException;

    Lanche salvar(Lanche lanche) throws ValidadorException;
}
