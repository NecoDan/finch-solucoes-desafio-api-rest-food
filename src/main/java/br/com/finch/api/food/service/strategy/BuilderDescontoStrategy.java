package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.service.negocio.Promocao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BuilderDescontoStrategy {

    private final List<IFactoryDescontoService> factoryDescontoService;

    public BuilderDescontoStrategy(List<IFactoryDescontoService> factoryDescontoServices) {
        this.factoryDescontoService = factoryDescontoServices;
    }

    public Promocao obter(BigDecimal quantidade) {
        for (IFactoryDescontoService factoryDescontoService : this.factoryDescontoService) {
            if (factoryDescontoService.isAppliable(quantidade))
                return factoryDescontoService.obterPromocao(quantidade);
        }
        return Promocao.SEM_PROMOCAO;
    }
}
