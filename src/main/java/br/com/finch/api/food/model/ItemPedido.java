package br.com.finch.api.food.model;

import br.com.finch.api.food.model.domain.CalculadoraDescontoQtdePorcoes;
import br.com.finch.api.food.service.negocio.Promocao;
import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@Data
@ToString
@JacksonXmlRootElement(localName = "ItemPedido")
@Entity
@Table(name = "fd05_item_lanche_pedido", schema = "food_service")
@Inheritance(strategy = InheritanceType.JOINED)
public class ItemPedido extends AbstractEntity {

    @Tolerate
    public ItemPedido() {
        super();
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lanche_pedido")
    private Pedido pedido;

    @JacksonXmlProperty
    @OneToOne
    @JoinColumn(name = "id_lanche")
    private Lanche lanche;

    @JacksonXmlProperty
    @Column(name = "item")
    private Integer item;

    @JacksonXmlProperty
    @Min(1)
    @PositiveOrZero
    @DecimalMin(value = "1.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "qtde")
    private BigDecimal quantidade;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @JacksonXmlProperty
    @Column(name = "valor_item")
    private BigDecimal valorItem;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @JacksonXmlProperty
    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @JacksonXmlProperty
    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;

    @JacksonXmlProperty(localName = "AdicionalItemPedido")
    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "itemPedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AdicionalItemPedido> adicionais = new ArrayList<>();

    @Transient
    @Getter
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private CalculadoraDescontoQtdePorcoes calculadoraDescontoQtdePorcoes = new CalculadoraDescontoQtdePorcoes();

    public void add(AdicionalItemPedido adicionalItemPedido) {
        if (isNotContainsAdicionais())
            this.adicionais = new ArrayList<>();
        this.adicionais.add(adicionalItemPedido);
    }

    public void addAll(Collection<AdicionalItemPedido> adicionalItemPedidoCollection) {
        this.adicionais.addAll(adicionalItemPedidoCollection);
    }

    @JsonIgnore
    public BigDecimal getValorTotalCalculado() {
        calculaValorTotal();
        return this.valorTotal;
    }

    @JsonIgnore
    public BigDecimal getQuantidadeGarantida() {
        if (Objects.isNull(this.quantidade))
            this.quantidade = BigDecimal.ONE;
        return this.quantidade;
    }

    public void aplicarItemValor() {
        if (Objects.isNull(this.lanche) || Objects.isNull(this.lanche.getValorTotal()))
            this.valorItem = BigDecimal.ZERO;
        this.valorItem = this.lanche.getValorTotal();
    }

    public void calculaValorTotal() {
        this.inicializarParamsEfetuarCalculo();

        if (Objects.isNull(this.valorItem))
            aplicarItemValor();

        this.valorTotal = calculaValorTotalItem();
        this.valorTotal = this.valorTotal.add(getValorTotalAdicionais()).setScale(2, BigDecimal.ROUND_UP);
    }

    public BigDecimal calculaValorTotalItem() {
        return this.valorItem.multiply(getQuantidadeGarantida());
    }

    @JsonIgnore
    public BigDecimal getValorTotalAdicionais() {
        if (isNotContainsAdicionais())
            return BigDecimal.ZERO;

        return BigDecimal.valueOf(this.adicionais.stream()
                .filter(Objects::nonNull)
                .filter(AdicionalItemPedido::isParamValidosFilterCalculoCustoTotal)
                .mapToDouble(AdicionalItemPedido::getCalculoValorTotalNumeric)
                .sum())
                .setScale(2, BigDecimal.ROUND_UP);
    }

    public void aplicarPromocao(Promocao promocao) {
        if (Objects.nonNull(promocao)) {
            this.valorDesconto = promocao.getRegraCalculoPromocaoDesconto().calculaDescontoNaPromocao(this);
            this.valorTotal = this.valorTotal.subtract(this.valorDesconto);
        }
        this.valorTotal = this.valorTotal.setScale(2, BigDecimal.ROUND_UP);
    }

    @JsonIgnore
    public boolean isValorTotalItemInFilterValido() {
        return Objects.nonNull(this.valorTotal);
    }

    @JsonIgnore
    public boolean isValorItemInFilterValido() {
        return Objects.nonNull(this.valorItem);
    }

    @JsonIgnore
    public boolean isValorDescontoItemInFilterValido() {
        return Objects.nonNull(this.valorDesconto);
    }

    @JsonIgnore
    public double getValorTotalItemNumeric() {
        if (isValorTotalItemInFilterValido())
            return this.valorTotal.doubleValue();
        return BigDecimal.ZERO.doubleValue();
    }

    @JsonIgnore
    public double getValorItemNumeric() {
        if (isValorItemInFilterValido())
            return calculaValorTotalItem().add(getValorTotalAdicionais()).doubleValue();
        return BigDecimal.ZERO.doubleValue();
    }

    @JsonIgnore
    public double getValorDescontoItemNumeric() {
        if (isValorDescontoItemInFilterValido())
            return this.valorDesconto.doubleValue();
        return BigDecimal.ZERO.doubleValue();
    }

    @JsonIgnore
    public boolean isNotContainsAdicionais() {
        return Objects.isNull(this.adicionais) || this.adicionais.isEmpty();
    }

    @JsonIgnore
    public boolean isParamsValidosFilterAdicionais() {
        return !(Objects.isNull(this.getValorTotal()) || !isNotContainsAdicionais());
    }

    @JsonIgnore
    public boolean isParamsValidosFilterCalculoQtdePorcoes() {
        return (Objects.nonNull(this.valorTotal) && Objects.nonNull(this.adicionais) && !this.adicionais.isEmpty());
    }

    @JsonIgnore
    public boolean isContainsAdicionais() {
        return !isNotContainsAdicionais();
    }

    private void inicializarParamsEfetuarCalculo() {
        if (Objects.isNull(this.valorTotal))
            this.valorTotal = BigDecimal.ZERO;

        if (Objects.isNull(this.valorDesconto))
            this.valorDesconto = BigDecimal.ZERO;
    }
}
