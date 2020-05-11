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
import java.util.Objects;

@SuperBuilder
@Data
@ToString
@JacksonXmlRootElement(localName = "TipoIngrediente")
@Entity
@Table(name = "fd07_tipo_ingrediente", schema = "food_service")
@Inheritance(strategy = InheritanceType.JOINED)
public class TipoIngrediente extends AbstractEntity {

    public final static Long TIPO_CARNE = 1L;
    public final static Long TIPO_QUEIJO = 2L;
    public final static Long TIPO_BACON = 4L;

    @Tolerate
    public TipoIngrediente() {
        super();
    }

    @JacksonXmlProperty
    @Column(name = "descricao")
    private String descricao;

    @JsonIgnore
    public boolean isIdValido() {
        return Objects.nonNull(this.getId());
    }

    @JsonIgnore
    public boolean isCarne() {
        return (isIdValido() && this.getId().equals(TIPO_CARNE));
    }

    @JsonIgnore
    public boolean isQueijo() {
        return (isIdValido() && this.getId().equals(TIPO_QUEIJO));
    }

    @JsonIgnore
    public boolean isBacon() {
        return (isIdValido() && this.getId().equals(TIPO_BACON));
    }

    @JsonIgnore
    public boolean isEquals(TipoIngrediente otherTipoIngrediente) {
        return (Objects.nonNull(otherTipoIngrediente) && this.getId().equals(otherTipoIngrediente.getId()));
    }
}
