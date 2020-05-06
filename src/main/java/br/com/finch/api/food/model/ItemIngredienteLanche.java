package br.com.finch.api.food.model;

import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@SuperBuilder
@Data
@ToString
@Entity
@Table(name = "fd03_item_ingrediente_lanche", schema = "food_service")
public class ItemIngredienteLanche extends AbstractEntity {

    @Tolerate
    public ItemIngredienteLanche() {
        super();
    }

    @ManyToOne(targetEntity = Lanche.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lanche", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Lanche lanche;

    @ManyToOne(targetEntity = Ingrediente.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ingrediente", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Ingrediente ingrediente;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 19, fraction = 2)
    @Column(name = "qtde")
    private BigDecimal quantidade;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 19, fraction = 2)
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
}
