package br.com.finch.api.food.repository;

import br.com.finch.api.food.model.LanchePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanchePedidoRepository extends JpaRepository<LanchePedido, Long> {

    LanchePedido findLanchePedidoByNomeAfterAndTelefone(String nome, String telefone);

    List<LanchePedido> findAllLanchePedidoByNomeAfterAndTelefone(String nome, String telefone);

}
