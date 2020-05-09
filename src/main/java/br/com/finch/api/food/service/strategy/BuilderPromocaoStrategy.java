package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.service.negocio.Promocao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuilderPromocaoStrategy {

    private final List<IFactoryPromocaoService> factoryPromocaDescontoService;

    public BuilderPromocaoStrategy(List<IFactoryPromocaoService> factoryPromocaoDescontoServiceList) {
        this.factoryPromocaDescontoService = factoryPromocaoDescontoServiceList;
    }

    public Promocao obter(ItemPedido itemPedido) {
        for (IFactoryPromocaoService factoryDescontoService : this.factoryPromocaDescontoService) {
            if (factoryDescontoService.isAppliable(itemPedido))
                return factoryDescontoService.obterPromocao(itemPedido);
        }
        return Promocao.SEM_PROMOCAO;
    }
}
