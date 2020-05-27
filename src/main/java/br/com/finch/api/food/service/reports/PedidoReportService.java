package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.model.dtos.PedidosWrapper;
import br.com.finch.api.food.repository.PedidoRepository;
import br.com.finch.api.food.service.IPedidoService;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoReportService implements IPedidoReportService {

    private final PedidoRepository pedidoRepository;

    @Override
    public PedidosWrapper listarPorId(Long id) throws ValidadorException {
        if (Objects.isNull(id))
            throw new ValidadorException("Parametro com a referência [ID] ao Pedido encontra-se inválida e/ou inexistente [NULA].");
        return PedidosWrapper.builder().build().adicionar(pedidoRepository.findById(id).orElse(null));
    }

    @Override
    public Optional<Pedido> listarPorBuscaId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public PedidosWrapper listarTodos() {
        return PedidosWrapper.builder().pedidos(pedidoRepository.findAll()).build();
    }

    @Override
    public PedidosWrapper listarTodos(Pageable pageable) {
        return PedidosWrapper.builder().pedidos(pedidoRepository.findAll(pageable).getContent()).build();
    }

    @Override
    public PedidosWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return PedidosWrapper.builder()
                .pedidos(pedidoRepository.findAllByValorTotalIsNotNullAndValorTotalGreaterThanEqual(valor))
                .build();
    }

    @Override
    public PedidosWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) {
        return PedidosWrapper.builder()
                .pedidos(pedidoRepository.findAllByValorTotalIsNotNullAndValorTotalLessThanEqual(valor))
                .build();
    }

    @Override
    public PedidosWrapper listarPorDataInsercao(LocalDate data) {
        return PedidosWrapper.builder().pedidos(pedidoRepository.recuperarTodosPorDataInsercao(data)).build();
    }

    @Override
    public PedidosWrapper listarPorNomeCliente(String nomeCliente) throws ValidadorException {
        if (Objects.isNull(nomeCliente) || nomeCliente.isEmpty())
            throw new ValidadorException("Parametro com o nome do cliente para efetuar a busca, encontra-se vazio (em branco) ou inválido e/ou inexistente[NULO].");
        return PedidosWrapper.builder().pedidos(pedidoRepository.findAllByNomeClienteContainingOrderById(nomeCliente)).build();
    }

    @Override
    public PedidosWrapper listarPorNomeCliente(String nomeCliente, Pageable pageable) throws ValidadorException {
        if (Objects.isNull(nomeCliente) || nomeCliente.isEmpty())
            throw new ValidadorException("Parametro com o nome do cliente para efetuar a busca, encontra-se vazio (em branco) ou inválido e/ou inexistente[NULO].");
        return PedidosWrapper.builder().pedidos(pedidoRepository.findAllByNomeClienteContaining(nomeCliente, pageable).getContent()).build();
    }

    @Override
    public PedidosWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException {
        return PedidosWrapper.builder().pedidos(pedidoRepository.recuperarTodosPorPeriodo(dataInicio, dataFim)).build();
    }
}
