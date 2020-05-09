package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.dtos.LanchesWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ILancheReportsService {

    LanchesWrapper listarPorId(Long id) throws ValidadorException;

    LanchesWrapper listarTodos();

    LanchesWrapper listarAtivos();

    LanchesWrapper listarInativos();

    LanchesWrapper listarPorDescricaoNome(String descricao) throws ValidadorException;

    LanchesWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException;

    LanchesWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) throws ValidadorException;

    LanchesWrapper listarPorDataInsercao(LocalDate data) throws ValidadorException;

    LanchesWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException;
}
