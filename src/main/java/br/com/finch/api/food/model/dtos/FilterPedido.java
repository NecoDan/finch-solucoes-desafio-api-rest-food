package br.com.finch.api.food.model.dtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class FilterPedido {
    private Long idPedido;
    private Long idLanche;
    private Long idIngrediente;
    private BigDecimal qtde = BigDecimal.ZERO;
}
