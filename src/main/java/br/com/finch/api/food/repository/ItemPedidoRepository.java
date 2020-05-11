package br.com.finch.api.food.repository;

import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    ItemPedido findByPedidoAndLanche(Pedido pedido, Lanche lanche);

    @Query(value = "select max(item) from food_service.fd05_item_lanche_pedido where id_lanche_pedido = :#{#idPedido}", nativeQuery = true)
    Integer recuperarUltimoValorItemAdicionado(@Param("idPedido") Long idPedido);
}
