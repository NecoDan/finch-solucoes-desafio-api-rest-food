package br.com.finch.api.food.model;

import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Data
@ToString
@JacksonXmlRootElement(localName = "Ingrediente")
@Entity
@Table(name = "fd01_ingrediente", schema = "food_service")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(value = {"lanches"})
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

    @ManyToMany(fetch = FetchType.EAGER)
    @Transient
    @JoinTable(name = "fd03_ingrediente_lanche", schema = "food_service", joinColumns =
            {@JoinColumn(name = "id_ingrediente")}, inverseJoinColumns =
            {@JoinColumn(name = "id_lanche")})
    private List<Lanche> lanches = new ArrayList<>();
}
