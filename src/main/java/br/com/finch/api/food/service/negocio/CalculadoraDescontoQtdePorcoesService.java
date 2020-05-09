package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculadoraDescontoQtdePorcoesService implements ICalculadoraDescontoQtdePorcoesService {

    @Override
    public BigDecimal efetuarCalculoDescontoNaPromocaoPor(ItemPedido itemPedido, BigDecimal qtde) {
        if (!isMultipoTres(qtde))
            return BigDecimal.ZERO;
        return obterValorDesconto(itemPedido, qtde).setScale(2, BigDecimal.ROUND_UP);
    }

    @Override
    public boolean isParamInvalidos(ItemPedido itemPedido) {
        return (Objects.isNull(itemPedido) || Objects.equals(itemPedido.isParamsValidosFilterAdicionais(), false));
    }

    private BigDecimal getQtdeDesconto(BigDecimal qtdeCarneAdicionada) {
        BigDecimal valor = qtdeCarneAdicionada.multiply(BigDecimal.valueOf(2));
        valor = valor.divide(BigDecimal.valueOf(3));
        return valor;
    }

    private boolean isMultipoTres(BigDecimal qtde) {
        return (Objects.nonNull(qtde) && qtde.compareTo(BigDecimal.ZERO) != 0 && BigDecimal.valueOf(qtde.doubleValue() % 3).compareTo(BigDecimal.ZERO) == 0);
    }

    private BigDecimal obterValorDesconto(ItemPedido itemPedido, BigDecimal qtdeCarneAdicionada) {
        List<AdicionalItemPedido> collect = itemPedido.getAdicionais().stream()
                .filter(Objects::nonNull)
                .filter(AdicionalItemPedido::isParamValidosFilterCalculoQtdeAdicionada)
                .filter(AdicionalItemPedido::isContemIngredienteHamburguer)
                .collect(Collectors.toList());

        BigDecimal valorDescontoTotal = BigDecimal.ZERO;
        BigDecimal valorQtdeDesconto = getQtdeDesconto(qtdeCarneAdicionada);

        for (AdicionalItemPedido a : collect) {
            BigDecimal valorTotal = a.getCalculoValorTotal();
            BigDecimal custo = a.getCustoAdicional();

            BigDecimal valorDesconto = custo.multiply(valorQtdeDesconto);
            valorQtdeDesconto = valorTotal.subtract(valorDesconto);
            valorDesconto = valorTotal.subtract(valorQtdeDesconto);
            valorDescontoTotal = valorDescontoTotal.add(valorDesconto);
        }

        return valorDescontoTotal;
    }
}
