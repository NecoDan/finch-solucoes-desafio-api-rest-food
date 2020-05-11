package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.dtos.PedidosWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IPedidoReportService {

    PedidosWrapper listarPorId(Long id) throws ValidadorException;

    PedidosWrapper listarTodos();

    PedidosWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException;

    PedidosWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) throws ValidadorException;

    PedidosWrapper listarPorDataInsercao(LocalDate data) throws ValidadorException;

    PedidosWrapper listarPorNomeCliente(String nomeCliente) throws ValidadorException;

    PedidosWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException;
}
