package br.com.finch.api.food.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fd03_ingrediente_lanche", schema = "food_service")
public class ItemLancheIngrediente implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "id_lanche")
    private Lanche lanche;

    @ManyToOne
    @JoinColumn(name = "id_ingrediente")
    private Ingrediente ingrediente;

    @DecimalMin(value = "1.0", inclusive = false)
    @Digits(integer = 19, fraction = 2)
    @Column(name = "qtde")
    private BigDecimal quantidade;

    @Column(name = "dt_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "ativo", columnDefinition = "tinyint(1) default 1", nullable = false)
    private boolean ativo = true;

    public ItemLancheIngrediente ativado() {
        this.setAtivo(true);
        return this;
    }

    public ItemLancheIngrediente gerarDataCorrente() {
        if (Objects.isNull(this.getDataCadastro()))
            this.setDataCadastro(LocalDateTime.now());
        return this;
    }
}
