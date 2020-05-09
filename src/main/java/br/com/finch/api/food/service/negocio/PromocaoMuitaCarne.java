package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PromocaoMuitaCarne implements IRegraCalculoPromocao {

    private final ICalculadoraDescontoQtdePorcoesService calculadoraDescontoQtdePorcoesService;

    @Override
    public BigDecimal calculaDescontoNaPromocao(ItemPedido itemPedido) {
        if (calculadoraDescontoQtdePorcoesService.isParamInvalidos(itemPedido))
            return BigDecimal.ZERO;

        BigDecimal qtdeCarneAdicionada = getQtdeCarneAdicionada(itemPedido);
        return calculadoraDescontoQtdePorcoesService.efetuarCalculoDescontoNaPromocaoPor(itemPedido, qtdeCarneAdicionada);
    }

    @Override
    public double calcularDescontoNaPromocao(ItemPedido itemPedido) {
        return this.calculaDescontoNaPromocao(itemPedido).doubleValue();
    }

    private BigDecimal getQtdeCarneAdicionada(ItemPedido itemPedido) {
        return BigDecimal.valueOf(itemPedido.getAdicionais()
                .stream()
                .filter(Objects::nonNull)
                .filter(AdicionalItemPedido::isParamValidosFilterCalculoQtdeAdicionada)
                .filter(AdicionalItemPedido::isContemIngredienteHamburguer)
                .mapToDouble(a -> a.getQuantidade().doubleValue())
                .sum())
                .setScale(2, BigDecimal.ROUND_UP);
    }
}
