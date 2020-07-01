package br.com.finch.api.food.model.domain;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.TipoIngrediente;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@ToString
public class CalculadoraDescontoQtdePorcoes {

    public BigDecimal efetuarCalculoDescontoNaPromocaoPor(ItemPedido itemPedido, TipoIngrediente tipoIngrediente) {
        return obterValorDesconto(itemPedido, tipoIngrediente).setScale(2, BigDecimal.ROUND_UP);
    }

    public boolean isParamInvalidos(ItemPedido itemPedido) {
        return (Objects.isNull(itemPedido) || Objects.equals(itemPedido.isParamsValidosFilterCalculoQtdePorcoes(), false));
    }

    private BigDecimal obterValorDesconto(ItemPedido itemPedido, TipoIngrediente tipoIngrediente) {
        List<AdicionalItemPedido> adicionais = getListaAdicionaisItemPedido(itemPedido, tipoIngrediente);
        BigDecimal qtdeAdicionada = getQtdeIngredientePorTipoAdicionada(itemPedido, tipoIngrediente);
        BigDecimal valorQtdeDesconto = getQtdeDesconto(qtdeAdicionada);

        if (!isParamsEfetuarCalculoValidos(adicionais, qtdeAdicionada, valorQtdeDesconto))
            return BigDecimal.ZERO;

        return (BigDecimal.valueOf(adicionais.stream()
                .filter(Objects::nonNull)
                .mapToDouble(a -> calculaValorADescontar(a, valorQtdeDesconto))
                .sum()));
    }

    private boolean isParamsEfetuarCalculoValidos(List<AdicionalItemPedido> adicionalItemPedidoList, BigDecimal qtdeAdicionada, BigDecimal valorQtdeDesconto) {
        return (!adicionalItemPedidoList.isEmpty() && qtdeAdicionada.compareTo(BigDecimal.ZERO) > 0 && isMultipoTres(qtdeAdicionada) && valorQtdeDesconto.compareTo(BigDecimal.ZERO) > 0);
    }

    private double calculaValorADescontar(AdicionalItemPedido a, BigDecimal valorQtdeDesconto) {
        BigDecimal valorTotal = a.getCalculoValorTotal();
        BigDecimal valorDesconto = a.getCustoAdicional().multiply(valorQtdeDesconto);

        valorQtdeDesconto = valorTotal.subtract(valorDesconto);
        valorDesconto = valorTotal.subtract(valorQtdeDesconto);
        return valorDesconto.doubleValue();
    }

    private BigDecimal getQtdeDesconto(BigDecimal qtdeCarneAdicionada) {
        if (Objects.isNull(qtdeCarneAdicionada))
            return BigDecimal.ZERO;

        BigDecimal valor = qtdeCarneAdicionada.multiply(BigDecimal.valueOf(2));
        valor = valor.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_EVEN);
        return valor;
    }

    private boolean isMultipoTres(BigDecimal qtde) {
        return (Objects.nonNull(qtde) && qtde.compareTo(BigDecimal.ZERO) != 0 && BigDecimal.valueOf(qtde.doubleValue() % 3).compareTo(BigDecimal.ZERO) == 0);
    }

    private List<AdicionalItemPedido> getListaAdicionaisItemPedido(ItemPedido itemPedido, TipoIngrediente tipoIngrediente) {
        return itemPedido.getAdicionais().stream()
                .filter(Objects::nonNull)
                .filter(AdicionalItemPedido::isParamValidosFilterCalculoQtdeAdicionada)
                .filter(a -> isContainsTipoIngrediente(tipoIngrediente, a))
                .collect(Collectors.toList());
    }

    private BigDecimal getQtdeIngredientePorTipoAdicionada(ItemPedido itemPedido, TipoIngrediente tipoIngrediente) {
        return BigDecimal.valueOf(itemPedido.getAdicionais()
                .stream()
                .filter(Objects::nonNull)
                .filter(AdicionalItemPedido::isParamValidosFilterCalculoQtdeAdicionada)
                .filter(a -> isContainsTipoIngrediente(tipoIngrediente, a))
                .mapToDouble(AdicionalItemPedido::getQuantidadeNumeric)
                .sum())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private boolean isValidaFilterAdicionalWithIngrediente(AdicionalItemPedido adicionalItemPedido, TipoIngrediente tipoIngrediente) {
        return (Objects.nonNull(adicionalItemPedido) && Objects.nonNull(adicionalItemPedido.getIngrediente())
                && Objects.nonNull(adicionalItemPedido.getIngrediente().getTipoIngrediente()));
    }

    private boolean isContainsTipoIngrediente(TipoIngrediente tipoIngrediente, AdicionalItemPedido adicionalItemPedido) {
        return (isValidaFilterAdicionalWithIngrediente(adicionalItemPedido, tipoIngrediente)
                && adicionalItemPedido.getIngrediente().isTipoEquals(tipoIngrediente));
    }
}
