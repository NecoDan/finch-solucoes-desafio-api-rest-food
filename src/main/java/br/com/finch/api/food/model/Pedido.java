package br.com.finch.api.food.model;

import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@ToString
@Data
@JacksonXmlRootElement(localName = "Pedido")
@Entity
@Table(name = "fd04_lanche_pedido", schema = "food_service")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pedido extends AbstractEntity {

    @Tolerate
    public Pedido() {
        super();
    }

    @JacksonXmlProperty
    @Size(max = 200)
    @NotBlank(message = "Insira um nome de cliente")
    @NotNull
    @Column(name = "nome_cliente")
    private String nomeCliente;

    @JacksonXmlProperty
    @NotBlank(message = "Insira um telefone do cliente para manter contato")
    @NotNull
    @Size(max = 20)
    @Column(name = "telefone")
    private String telefone;

    @JacksonXmlProperty
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valorItens")
    private BigDecimal valorItens = BigDecimal.ZERO;

    @JacksonXmlProperty
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_desconto_total")
    private BigDecimal valorTotalDesconto = BigDecimal.ZERO;

    @JacksonXmlProperty
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @JacksonXmlProperty
    @Fetch(FetchMode.SELECT)
    // @Transient
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotEmpty
    private List<ItemPedido> itens = new ArrayList<>();

    public void gerarDataCorrente() {
        if (Objects.isNull(this.getDataCadastro()))
            this.setDataCadastro(LocalDateTime.now());
    }

    public void add(ItemPedido itemPedido) {
        if (isNotContainsItens())
            this.itens = new ArrayList<>();
        this.itens.add(itemPedido);
    }

    public void addAll(Collection<ItemPedido> itens) {
        this.itens.addAll(itens);
    }

    public void calculaValorTotal() {
        if (isNotContainsItens())
            return;

        this.valorTotal = BigDecimal.valueOf(this.itens
                .stream()
                .filter(Objects::nonNull)
                .filter(ItemPedido::isValorTotalItemInFilterValido)
                .mapToDouble(ItemPedido::getValorTotalItemNumeric)
                .sum())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    public void calculaValorItens() {
        inicializarParamsEfetuarCalculo();
        if (isNotContainsItens())
            return;

        this.valorItens = BigDecimal.valueOf(this.itens
                .stream()
                .filter(Objects::nonNull)
                .filter(ItemPedido::isValorItemInFilterValido)
                .mapToDouble(ItemPedido::getValorItemNumeric)
                .sum())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    public void calcularValorTotalDesconto() {
        inicializarParamsEfetuarCalculo();
        if (isNotContainsItens())
            return;

        this.valorTotalDesconto = BigDecimal.valueOf(this.itens.stream()
                .filter(Objects::nonNull)
                .filter(ItemPedido::isValorDescontoItemInFilterValido)
                .mapToDouble(ItemPedido::getValorDescontoItemNumeric)
                .sum())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    @JsonIgnore
    public boolean isNotContainsItens() {
        return Objects.isNull(this.itens) || this.itens.isEmpty();
    }

    private void inicializarParamsEfetuarCalculo() {
        if (Objects.isNull(this.valorTotal))
            this.valorTotal = BigDecimal.ZERO;

        if (Objects.isNull(this.valorTotalDesconto))
            this.valorTotalDesconto = BigDecimal.ZERO;
    }
}
