package br.com.finch.api.food.repository;

import br.com.finch.api.food.model.Lanche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancheRepository extends JpaRepository<Lanche, Long> {

    List<Lanche> findAllByAtivo(boolean ativo);

    List<Lanche> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);

    List<Lanche> findAllByValorTotalIsNotNullAndValorTotalLessThanEqual(BigDecimal menorValor);

    List<Lanche> findAllByValorTotalIsNotNullAndValorTotalGreaterThanEqual(BigDecimal maiorValor);

    @Query(value="select * from food_service.fd02_lanche where date(dt_cadastro) = :#{#data} order by descricao", nativeQuery = true)
    List<Lanche> recuperarTodosPorDataInsercao(@Param("data") LocalDate data);

    @Query(value="select * from food_service.fd02_lanche where date(dt_cadastro) between :#{#dataInicio} and :#{#dataFim} order by descricao", nativeQuery = true)
    List<Lanche> recuperarTodosPorPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
