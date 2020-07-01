package br.com.finch.api.food.service.negocio;

import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.TipoIngrediente;
import br.com.finch.api.food.model.domain.CalculadoraDescontoQtdePorcoes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@Slf4j
public class PromocaoMuitaCarne implements IRegraCalculoPromocao {

    @Override
    public BigDecimal calculaDescontoNaPromocao(ItemPedido itemPedido) {
        if (Objects.isNull(itemPedido) || Objects.isNull(itemPedido.getCalculadoraDescontoQtdePorcoes()))
            return BigDecimal.ZERO;

        CalculadoraDescontoQtdePorcoes calculadoraDescontoQtdePorcoes = itemPedido.getCalculadoraDescontoQtdePorcoes();
        if (calculadoraDescontoQtdePorcoes.isParamInvalidos(itemPedido))
            return BigDecimal.ZERO;

        TipoIngrediente tipoQueijo = TipoIngrediente.builder().id(TipoIngrediente.TIPO_CARNE).build();
        return calculadoraDescontoQtdePorcoes.efetuarCalculoDescontoNaPromocaoPor(itemPedido, tipoQueijo);
    }

    @Override
    public double calcularDescontoNaPromocao(ItemPedido itemPedido) {
        return this.calculaDescontoNaPromocao(itemPedido).doubleValue();
    }
}
