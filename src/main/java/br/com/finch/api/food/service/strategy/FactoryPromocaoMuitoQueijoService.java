package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.service.negocio.Promocao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FactoryPromocaoMuitoQueijoService implements IFactoryPromocaoService {

    @Override
    public boolean isAppliable(ItemPedido itemPedido) {
        return (Objects.nonNull(itemPedido) && Objects.nonNull(itemPedido.getLanche()) && itemPedido.isContainsAdicionais() && isContemQueijo(itemPedido));
    }

    @Override
    public Promocao obterPromocao(ItemPedido itemPedido) {
        if (isAppliable(itemPedido))
            return Promocao.MUITO_QUEIJO;
        return null;
    }

    private boolean isContemQueijo(ItemPedido itemPedido) {
        return isContemQueijoWithAdicionais(itemPedido);
    }

    private boolean isContemQueijoWithAdicionais(ItemPedido itemPedido) {
        return isContemCarneWithIngredientes(itemPedido.getAdicionais().stream()
                .map(AdicionalItemPedido::getIngrediente)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    private boolean isContemCarneWithIngredientes(List<Ingrediente> ingredientes) {
        return Objects.nonNull(ingredientes.stream()
                .filter(Objects::nonNull)
                .filter(Ingrediente::isQueijo)
                .findAny()
                .orElse(null));
    }
}
