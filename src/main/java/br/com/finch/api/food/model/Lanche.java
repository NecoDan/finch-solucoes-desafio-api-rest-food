package br.com.finch.api.food.model;

import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Data
@ToString
@JacksonXmlRootElement(localName = "Lanche")
@Entity
@Table(name = "fd02_lanche", schema = "food_service")
public class Lanche extends AbstractEntity {

    @Tolerate
    public Lanche() {
        super();
    }

    @JacksonXmlProperty
    @Column(name = "descricao")
    private String descricao;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 19, fraction = 2)
    @Column(name = "valor_total")
    @JacksonXmlProperty
    private BigDecimal valorTotal;

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "lanche", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemIngredienteLanche> itemIngredienteLanches = new ArrayList<>();
}

