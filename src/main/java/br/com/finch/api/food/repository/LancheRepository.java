package br.com.finch.api.food.repository;

import br.com.finch.api.food.model.ItemLancheIngrediente;
import br.com.finch.api.food.model.Lanche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = "select * from food_service.fd02_lanche where date(dt_cadastro) = :#{#data} order by descricao", nativeQuery = true)
    List<Lanche> recuperarTodosPorDataInsercao(@Param("data") LocalDate data);

    @Query(value = "select * from food_service.fd02_lanche where date(dt_cadastro) between :#{#dataInicio} and :#{#dataFim} order by descricao", nativeQuery = true)
    List<Lanche> recuperarTodosPorPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Modifying
    @Query(value = "insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde, dt_cadastro, ativo) values (:#{#itemLancheIngrediente.lanche.id}, :#{#itemLancheIngrediente.ingrediente.id}, :#{#itemLancheIngrediente.quantidade}, :#{#itemLancheIngrediente.dataCadastro}, :#{#itemLancheIngrediente.ativo}) ", nativeQuery = true)
    void inserirItemIngredienteLanche(@Param("itemLancheIngrediente") ItemLancheIngrediente itemLancheIngrediente);

    @Modifying
    @Query(value = "update food_service.fd03_ingrediente_lanche set qtde = :#{#itemLancheIngrediente.quantidade}, dt_cadastro = :#{#itemLancheIngrediente.dataCadastro}, ativo = :#{#itemLancheIngrediente.ativo} where id_lanche = :#{#itemLancheIngrediente.lanche.id} and id_ingrediente = :#{#itemLancheIngrediente.ingrediente.id}", nativeQuery = true)
    void atualizarItemIngredienteLanche(@Param("itemLancheIngrediente") ItemLancheIngrediente itemLancheIngrediente);

    @Modifying
    @Query(value = "delete from food_service.fd03_ingrediente_lanche where id_lanche = :#{#itemLancheIngrediente.lanche.id} and id_ingrediente = :#{#itemLancheIngrediente.ingrediente.id}", nativeQuery = true)
    void removerItemIngredienteLanche(@Param("itemLancheIngrediente") ItemLancheIngrediente itemLancheIngrediente);

    @Query(value = "select (count(id_lanche) > 0 and count(id_ingrediente) > 0) as exists from food_service.fd03_ingrediente_lanche where id_lanche = :#{#itemLancheIngrediente.lanche.id} and id_ingrediente = :#{#itemLancheIngrediente.ingrediente.id}", nativeQuery = true)
    boolean existsItemIngredienteLanche(@Param("itemLancheIngrediente") ItemLancheIngrediente itemLancheIngrediente);

    @Query(value = "select sum(qtde*ingrediente.custo) as valor_total from food_service.fd03_ingrediente_lanche as item_ingrediente inner join food_service.fd01_ingrediente as ingrediente on (ingrediente.id = item_ingrediente.id_ingrediente) where item_ingrediente.id_lanche = :#{#lanche.id}", nativeQuery = true)
    BigDecimal recuperarValorTotalLancheRecalculado(@Param("lanche") Lanche lanche);

}
