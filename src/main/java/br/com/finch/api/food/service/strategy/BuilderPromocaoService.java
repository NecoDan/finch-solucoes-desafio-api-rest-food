package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.service.negocio.Promocao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuilderPromocaoService implements IBuilderPromocaoService {

    private final BuilderPromocaoStrategy builderPromocaoStrategy;

    @Override
    public Promocao obterPromocaoAPartir(ItemPedido itemPedido) {
        return builderPromocaoStrategy.obter(itemPedido);
    }
}
