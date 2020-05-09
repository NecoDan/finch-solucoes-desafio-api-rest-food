package br.com.finch.api.food.service;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.dtos.IngredientesWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.time.LocalDate;
import java.util.List;

public interface IIngredienteService {

    Ingrediente recuperarPorId(Long id);

    List<Ingrediente> recuperarTodos();

    List<Ingrediente> recuperarAtivos();

    List<Ingrediente> recuperarInativos();

    List<Ingrediente> recuperarPorDescricao(String descricao) throws ValidadorException;

    List<Ingrediente> recuperarTodosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException;

    List<Ingrediente> recuperarPorDataInsercao(LocalDate data) throws ValidadorException;

    IngredientesWrapper gerar(List<Ingrediente> ingredientes) throws ValidadorException;

    Ingrediente salvar(Ingrediente ingrediente) throws ValidadorException;

    boolean atualizarPor(Long ingredienteId, Ingrediente ingrediente) throws ValidadorException;

    boolean excluir(Ingrediente ingrediente) throws ValidadorException;
}
