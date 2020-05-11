package br.com.finch.api.food.model.dtos;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.Pedido;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class FilterPedido {
    private Long idPedido = 0L;
    private Long idLanche = 0L;
    private Long idIngrediente = 0L;
    private BigDecimal qtde = BigDecimal.ZERO;
    private Pedido pedido;
    private Lanche lanche;
    private Ingrediente ingrediente;
}
