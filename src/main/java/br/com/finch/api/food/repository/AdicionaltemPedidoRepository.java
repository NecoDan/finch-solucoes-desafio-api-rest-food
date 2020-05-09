package br.com.finch.api.food.repository;

import br.com.finch.api.food.model.AdicionalItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdicionaltemPedidoRepository extends JpaRepository<AdicionalItemPedido, Long> {
}
