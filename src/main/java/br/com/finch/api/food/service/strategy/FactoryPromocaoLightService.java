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
public class FactoryPromocaoLightService implements IFactoryPromocaoService {

    private final static String PARAM_VALIDACAO_INGREDIENTE_ALFACE = "Alface";

    @Override
    public boolean isAppliable(ItemPedido itemPedido) {
        return (Objects.nonNull(itemPedido) && Objects.nonNull(itemPedido.getLanche()) && itemPedido.getLanche().isContainsIngrediente()
                && isContemAlface(itemPedido) && isNotContemBacon(itemPedido));
    }

    @Override
    public Promocao obterPromocao(ItemPedido itemPedido) {
        if (isAppliable(itemPedido))
            return Promocao.LIGHT;
        return null;
    }

    private boolean isContemAlface(ItemPedido itemPedido) {
        return (isContemIngredienteAlfaceWithIngredientes(itemPedido.getLanche().getIngredientes()) || isContemIngredienteAlfaceWithAdicionais(itemPedido.getAdicionais()));
    }

    private boolean isNotContemBacon(ItemPedido itemPedido) {
        return (isNotContemIngredienteBaconWithIngredientes(itemPedido.getLanche().getIngredientes()) || isNotContemIngredienteBaconWithAdicionais(itemPedido.getAdicionais()));
    }

    private boolean isNotContemIngredienteBaconWithIngredientes(List<Ingrediente> ingredientes) {
        return Objects.isNull(ingredientes.stream()
                .filter(Objects::nonNull)
                .filter(Ingrediente::isBacon)
                .findAny()
                .orElse(null));
    }

    private boolean isContemIngredienteAlfaceWithIngredientes(List<Ingrediente> ingredientes) {
        return Objects.nonNull(ingredientes.stream()
                .map(Ingrediente::getDescricao)
                .collect(Collectors.toList())
                .stream()
                .filter(Objects::nonNull)
                .filter(i -> i.equalsIgnoreCase(PARAM_VALIDACAO_INGREDIENTE_ALFACE))
                .findAny()
                .orElse(null));
    }

    private boolean isContemIngredienteAlfaceWithAdicionais(List<AdicionalItemPedido> adicionalItemPedidoList) {
        if (isItensAdicionaisInvalidos(adicionalItemPedidoList))
            return false;
        return isContemIngredienteAlfaceWithIngredientes(adicionalItemPedidoList.stream().map(AdicionalItemPedido::getIngrediente).collect(Collectors.toList()));
    }

    private boolean isNotContemIngredienteBaconWithAdicionais(List<AdicionalItemPedido> adicionalItemPedidoList) {
        if (isItensAdicionaisInvalidos(adicionalItemPedidoList))
            return false;
        return isNotContemIngredienteBaconWithIngredientes(adicionalItemPedidoList.stream().map(AdicionalItemPedido::getIngrediente).collect(Collectors.toList()));
    }

    private boolean isItensAdicionaisInvalidos(List<AdicionalItemPedido> adicionalItemPedidoList) {
        return (Objects.isNull(adicionalItemPedidoList) || adicionalItemPedidoList.isEmpty());
    }
}
