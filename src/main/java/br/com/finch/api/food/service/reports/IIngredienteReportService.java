package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.reports.IngredientesWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IIngredienteReportService {

    IngredientesWrapper listarPorId(Long id) throws ValidadorException;

    IngredientesWrapper listarTodos();

    IngredientesWrapper listarAtivos();

    IngredientesWrapper listarInativos();

    IngredientesWrapper listarPorDescricaoNome(String descricao) throws ValidadorException;

    IngredientesWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException;

    IngredientesWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) throws ValidadorException;

    IngredientesWrapper listarPorDataInsercao(LocalDate data) throws ValidadorException;

    IngredientesWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException;
}
