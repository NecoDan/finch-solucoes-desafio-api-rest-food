package br.com.finch.api.food.model;

import br.com.finch.api.food.util.domain.AbstractEntity;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@ToString
@Data
@JacksonXmlRootElement(localName = "LanchePedido")
@Entity(name = "fd04_lanche_pedido")
public class LanchePedido extends AbstractEntity {

    @Tolerate
    public LanchePedido() {
        super();
    }

    @Size(max = 60)
    private String nome;

    @NotBlank
    @Size(max = 20)
    private String telefone;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 19, fraction = 2)
    @JacksonXmlProperty
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @JacksonXmlProperty
    private LocalDate dtLancamento;

    @OneToMany
    @JacksonXmlProperty
    private List<Lanche> lanches;

    public void gerarDataCorrente() {
        if (Objects.isNull(this.getDataCadastro()))
            this.setDataCadastro(LocalDateTime.now());
    }

//    public void calcularValorTotal(Desconto desconto) {
//        if (Objects.isNull(this.valor) || this.valor.compareTo(BigDecimal.ZERO) == 0)
//            return;
//
//        this.valorTotal = this.valor.multiply(this.quantidade);
//
//        if (Objects.nonNull(desconto)) {
//            BigDecimal valorDesconto = desconto.getRegraCalculoDesconto().calculaDesconto(this);
//            this.valorTotal = this.valorTotal.subtract(valorDesconto);
//        }
//
//        this.valorTotal = this.valorTotal.setScale(2, BigDecimal.ROUND_UP);
//    }
}
