package br.com.finch.api.food.model;

import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Objects;

@SuperBuilder
@Data
@ToString
@JacksonXmlRootElement(localName = "Ingrediente")
@Entity
@Table(name = "fd01_ingrediente", schema = "food_service")
@Inheritance(strategy = InheritanceType.JOINED)
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
    private BigDecimal custo = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "id_tipo")
    private TipoIngrediente tipoIngrediente;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "fd03_ingrediente_lanche", schema = "food_service", joinColumns =
            {@JoinColumn(name = "id_ingrediente")}, inverseJoinColumns =
            {@JoinColumn(name = "id_lanche")})
    @JsonIgnore
    private List<Lanche> lanches = new ArrayList<>();

    @JsonIgnore
    public boolean isTipoValido() {
        return Objects.nonNull(this.tipoIngrediente);
    }

    @JsonIgnore
    public boolean isCarne() {
        return (isTipoValido() && this.tipoIngrediente.isCarne());
    }

    @JsonIgnore
    public boolean isQueijo() {
        return (isTipoValido() && this.tipoIngrediente.isQueijo());
    }

    @JsonIgnore
    public boolean isBacon() {
        return (isTipoValido() && this.tipoIngrediente.isBacon());
    }

    @JsonIgnore
    public boolean isTipoEquals(TipoIngrediente otherTipoIngrediente) {
        return (isTipoValido() && this.tipoIngrediente.isEquals(otherTipoIngrediente));
    }
}
