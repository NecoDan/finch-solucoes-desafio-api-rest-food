package br.com.finch.api.food.model;

import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
import java.util.Objects;

@SuperBuilder
@Data
@ToString
@JacksonXmlRootElement(localName = "AdicionalItemPedido")
@Entity
@Table(name = "fd06_adicional_item_lanche_pedido", schema = "food_service")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AdicionalItemPedido extends AbstractEntity {

    @Tolerate
    public AdicionalItemPedido() {
        super();
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_item_lanche_pedido")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemPedido itemPedido;

    @JacksonXmlProperty
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ingrediente")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ingrediente ingrediente;

    @JacksonXmlProperty
    @DecimalMin(value = "1.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "qtde")
    private BigDecimal quantidade;

    @JsonIgnore
    public BigDecimal getCustoAdicional() {
        if (Objects.nonNull(this.getIngrediente()) && Objects.nonNull(this.getIngrediente().getCusto()) && this.getIngrediente().getCusto().compareTo(BigDecimal.ZERO) != 0)
            return this.getIngrediente().getCusto();
        return BigDecimal.ZERO;
    }

    @JsonIgnore
    public BigDecimal getCalculoValorTotal() {
        return getCustoAdicional().multiply(getQuantidade());
    }

    @JsonIgnore
    public double getCalculoValorTotalNumeric() {
        return getCalculoValorTotal().doubleValue();
    }

    @JsonIgnore
    public double getQuantidadeNumeric() {
        return this.quantidade.doubleValue();
    }

    @JsonIgnore
    public boolean isParamValidosFilterCalculoCustoTotal() {
        return Objects.nonNull(getCustoAdicional()) && Objects.nonNull(getQuantidade());
    }

    @JsonIgnore
    public boolean isParamValidosFilterIngredientes() {
        return (Objects.nonNull(this.ingrediente) || Objects.nonNull(this.getIngrediente()) && !this.ingrediente.getId().equals(0L));
    }

    @JsonIgnore
    public boolean isParamValidosFilterCalculoQtdeAdicionada() {
        return Objects.nonNull(this.ingrediente) && this.ingrediente.isTipoValido();
    }

    @JsonIgnore
    public boolean isContemIngredienteHamburguer() {
        return isParamValidosFilterCalculoQtdeAdicionada() && this.ingrediente.isCarne();
    }

    @JsonIgnore
    public boolean isContemIngredienteQueijo() {
        return isParamValidosFilterCalculoQtdeAdicionada() && this.ingrediente.isQueijo();
    }
}
