package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.dtos.PedidosWrapper;
import br.com.finch.api.food.repository.PedidoRepository;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LanchePedidoReportService implements ILanchePedidoReportService {

    private final PedidoRepository pedidoRepository;

    @Override
    public PedidosWrapper listarPorId(Long id) throws ValidadorException {
        if (Objects.isNull(id))
            throw new ValidadorException("Parametro com a referência [ID] ao Pedido encontra-se inválida e/ou inexistente [NULA].");
        return PedidosWrapper.builder().build().adicionar(pedidoRepository.findById(id).orElse(null));
    }

    @Override
    public PedidosWrapper listarTodos() {
        return PedidosWrapper.builder().pedidos(pedidoRepository.findAll()).build();
    }

    @Override
    public PedidosWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return PedidosWrapper.builder()
                .pedidos(pedidoRepository.findAllByValorTotalIsNotNullAndValorTotalGreaterThanEqual(valor))
                .build();
    }

    @Override
    public PedidosWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return PedidosWrapper.builder()
                .pedidos(pedidoRepository.findAllByValorTotalIsNotNullAndValorTotalLessThanEqual(valor))
                .build();
    }

    @Override
    public PedidosWrapper listarPorDataInsercao(LocalDate data) throws ValidadorException {
        return PedidosWrapper.builder().pedidos(pedidoRepository.recuperarTodosPorDataInsercao(data)).build();
    }

    @Override
    public PedidosWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException {
        return PedidosWrapper.builder().pedidos(pedidoRepository.recuperarTodosPorPeriodo(dataInicio, dataFim)).build();
    }
}
