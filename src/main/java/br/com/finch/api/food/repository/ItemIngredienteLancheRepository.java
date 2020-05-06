package br.com.finch.api.food.repository;

import br.com.finch.api.food.model.ItemIngredienteLanche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemIngredienteLancheRepository extends JpaRepository<ItemIngredienteLanche, Long> {
}
