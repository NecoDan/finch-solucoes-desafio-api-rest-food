package br.com.finch.api.food.repository;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.Lanche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    List<Ingrediente> findAllByAtivo(boolean ativo);

    List<Ingrediente> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);

    @Query(value = "select * from food_service.fd01_ingrediente where date(dt_cadastro) = :#{#data} order by descricao", nativeQuery = true)
    List<Ingrediente> recuperarTodosPorDataInsercao(@Param("data") LocalDate data);

    @Query(value = "select * from food_service.fd01_ingrediente where date(dt_cadastro) between :#{#dataInicio} and :#{#dataFim} order by descricao", nativeQuery = true)
    List<Ingrediente> recuperarTodosPorPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    List<Ingrediente> findAllByCustoIsNotNullAndCustoLessThanEqual(BigDecimal menorValor);

    List<Ingrediente> findAllByCustoIsNotNullAndCustoGreaterThanEqual(BigDecimal maiorValor);

    @Query(value = "select ingrediente.* from food_service.fd03_ingrediente_lanche as item_ingrediente inner join food_service.fd02_lanche as lanche on (lanche.id = item_ingrediente.id_lanche) inner join food_service.fd01_ingrediente as ingrediente on (ingrediente.id = item_ingrediente.id_ingrediente) where item_ingrediente.id_lanche = :#{#lanche.id} ", nativeQuery = true)
    List<Ingrediente> recuperarIngredientesContidoAoLanche(@Param("lanche") Lanche lanche);

}
