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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@Data
@ToString
@JacksonXmlRootElement(localName = "Lanche")
@Entity
@Table(name = "fd02_lanche", schema = "food_service")
@Inheritance(strategy = InheritanceType.JOINED)
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "fd03_ingrediente_lanche", schema = "food_service", joinColumns =
            {@JoinColumn(name = "id_lanche")}, inverseJoinColumns =
            {@JoinColumn(name = "id_ingrediente")})
    @JacksonXmlProperty
    private List<Ingrediente> ingredientes = new ArrayList<>();

    public void recalculaValorTotal(BigDecimal valor) {
        if (Objects.isNull(this.ingredientes))
            valor = BigDecimal.ZERO;
        this.setValorTotal(valor.setScale(2, BigDecimal.ROUND_UP));
    }

    public void recalculaValorTotal() {
        if (Objects.isNull(this.ingredientes) || this.ingredientes.isEmpty())
            return;

        this.setValorTotal(BigDecimal.valueOf(this.ingredientes.stream()
                .filter(Objects::nonNull)
                .filter(i -> i.getCusto().compareTo(BigDecimal.ZERO) > 0)
                .mapToDouble(i -> i.getCusto().doubleValue())
                .sum())
                .setScale(2, BigDecimal.ROUND_UP));
    }
}

