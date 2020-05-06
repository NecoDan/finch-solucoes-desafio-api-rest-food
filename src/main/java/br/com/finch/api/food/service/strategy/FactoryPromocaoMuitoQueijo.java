package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.service.negocio.Promocao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class FactoryPromocaoMuitoQueijo implements IFactoryDescontoService {

    private final static double VALOR_QTDE_PADRAO_DEZ_PORCENTO = 10.00;

    @Override
    public boolean isAppliable(BigDecimal quantidade) {
        return (Objects.nonNull(quantidade) && quantidade.compareTo(BigDecimal.ZERO) != 0 && quantidade.doubleValue() >= VALOR_QTDE_PADRAO_DEZ_PORCENTO);
    }

    @Override
    public Promocao obterPromocao(BigDecimal quantidade) {
        if (isAppliable(quantidade))
            return Promocao.MUITO_QUEIJO;
        return null;
    }
}
