package br.com.finch.api.food.service;

import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.reports.LanchesWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.time.LocalDate;
import java.util.List;

public interface ILancheService {

    Lanche recuperarPorId(Long id) throws ValidadorException;

    List<Lanche> recuperarTodos();

    List<Lanche> recuperarAtivos();

    List<Lanche> recuperarInativos();

    List<Lanche> recuperarPorDescricao(String descricao) throws ValidadorException;

    List<Lanche> recuperarTodosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException;

    List<Lanche> recuperarPorDataInsercao(LocalDate data) throws ValidadorException;

    LanchesWrapper gerar(List<Lanche> lanches) throws ValidadorException;

    Lanche salvar(Lanche lanche) throws ValidadorException;

    boolean atualizarPor(Long lancheId, Lanche lanche) throws ValidadorException;

    boolean excluir(Lanche lanche) throws ValidadorException;
}
