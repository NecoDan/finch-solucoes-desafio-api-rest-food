package br.com.finch.api.food.model;

import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@SuperBuilder
@Data
@ToString
@JacksonXmlRootElement(localName = "Ingrediente")
@Entity
@Table(name = "fd01_ingrediente", schema = "food_service")
public class Ingrediente extends AbstractEntity {

    @Tolerate
    public Ingrediente() {
        super();
    }

    @JacksonXmlProperty
    @Column(name = "descricao")
    private String descricao;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 19, fraction = 2)
    @JacksonXmlProperty
    @Column(name = "custo")
    private BigDecimal custo;
}
