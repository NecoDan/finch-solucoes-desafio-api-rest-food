package br.com.finch.api.food.repository;

import br.com.finch.api.food.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findAll(Pageable pageable);

    List<Pedido> findAllByNomeClienteContainingOrderById(String nomeCliente);

    Page<Pedido> findAllByNomeClienteContaining(String nomeCliente, Pageable pageable);

    List<Pedido> findAllByValorTotalIsNotNullAndValorTotalLessThanEqual(BigDecimal menorValor);

    List<Pedido> findAllByValorTotalIsNotNullAndValorTotalGreaterThanEqual(BigDecimal maiorValor);

    @Query(value = "select * from food_service.fd04_lanche_pedido where date(dt_cadastro) = :#{#data} order by id", nativeQuery = true)
    List<Pedido> recuperarTodosPorDataInsercao(@Param("data") LocalDate data);

    @Query(value = "select * from food_service.fd04_lanche_pedido where date(dt_cadastro) between :#{#dataInicio} and :#{#dataFim} order by id", nativeQuery = true)
    List<Pedido> recuperarTodosPorPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
