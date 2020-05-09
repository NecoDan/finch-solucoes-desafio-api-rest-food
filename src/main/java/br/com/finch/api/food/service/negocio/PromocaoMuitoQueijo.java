package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PromocaoMuitoQueijo implements IRegraCalculoPromocao {

    private final ICalculadoraDescontoQtdePorcoesService calculadoraDescontoQtdePorcoesService;

    @Override
    public BigDecimal calculaDescontoNaPromocao(ItemPedido itemPedido) {
        if (calculadoraDescontoQtdePorcoesService.isParamInvalidos(itemPedido))
            return BigDecimal.ZERO;

        BigDecimal qtdeQueijoAdicionada = getQtdeQueijoAdicionada(itemPedido);
        return calculadoraDescontoQtdePorcoesService.efetuarCalculoDescontoNaPromocaoPor(itemPedido, qtdeQueijoAdicionada);
    }

    @Override
    public double calcularDescontoNaPromocao(ItemPedido itemPedido) {
        return this.calculaDescontoNaPromocao(itemPedido).doubleValue();
    }

    private BigDecimal getQtdeQueijoAdicionada(ItemPedido itemPedido) {
        return BigDecimal.valueOf(itemPedido.getAdicionais()
                .stream()
                .filter(Objects::nonNull)
                .filter(AdicionalItemPedido::isParamValidosFilterCalculoQtdeAdicionada)
                .filter(AdicionalItemPedido::isContemIngredienteQueijo)
                .mapToDouble(AdicionalItemPedido::getQuantidadeNumeric)
                .sum())
                .setScale(2, BigDecimal.ROUND_UP);
    }
}
