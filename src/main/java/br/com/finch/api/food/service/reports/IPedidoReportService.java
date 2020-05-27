package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.model.dtos.PedidosWrapper;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface IPedidoReportService {

    PedidosWrapper listarPorId(Long id) throws ValidadorException;

    Optional<Pedido> listarPorBuscaId(Long id);

    PedidosWrapper listarTodos();

    PedidosWrapper listarTodos(Pageable pageable);

    PedidosWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException;

    PedidosWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) throws ValidadorException;

    PedidosWrapper listarPorDataInsercao(LocalDate data) throws ValidadorException;

    PedidosWrapper listarPorNomeCliente(String nomeCliente) throws ValidadorException;

    PedidosWrapper listarPorNomeCliente(String nomeCliente, Pageable pageable) throws ValidadorException;

    PedidosWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException;
}
