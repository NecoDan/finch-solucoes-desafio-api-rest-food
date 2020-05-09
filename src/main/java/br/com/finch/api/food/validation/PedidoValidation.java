package br.com.finch.api.food.validation;

import br.com.finch.api.food.model.AdicionalItemPedido;
import br.com.finch.api.food.model.ItemPedido;
import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.model.dtos.FilterPedido;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Daniel Santos
 */
@Service
public class PedidoValidation implements IPedidoValidation {

    @Override
    public void validarSomentePedido(Pedido pedido) throws ValidadorException {
        if (Objects.isNull(pedido))
            throw new ValidadorException("Pedido encontra-se inválido e/ou inexistente [NULO].");
    }

    @Override
    public void validarParamsFilterPedidoAddItemPedido(FilterPedido filterPedido) throws ValidadorException {
        if (Objects.isNull(filterPedido))
            throw new ValidadorException("Parametros pra adicionar um novo item encontra-se inválido ou inexistente [NULO].");

        if (Objects.isNull(filterPedido.getIdPedido()) || filterPedido.getIdPedido().equals(0L))
            throw new ValidadorException("O [ID] referente ao Pedido encontra-se inválido e/ou inexistente [NULO].");

        if (Objects.isNull(filterPedido.getIdLanche()))
            throw new ValidadorException("O [ID] referente ao Lanche encontra-se inválido e/ou inexistente [NULO].");

        if (Objects.isNull(filterPedido.getQtde()) || filterPedido.getQtde().compareTo(BigDecimal.ZERO) != 0)
            throw new ValidadorException("A quantidade a ser inserida ao item encontra-se inválida e/ou inexistente [NULA] ou valor igual à zero (0).");
    }

    @Override
    public void validarIdReferentePedido(Pedido pedido) throws ValidadorException {
        if (Objects.nonNull(pedido) && Objects.isNull(pedido.getId()))
            throw new ValidadorException("Pedido encontra-se com a referência do [ID] inválida e/ou inexistente [NULA].");
    }

    @Override
    public void validarSomenteItemPedidoLanche(ItemPedido itemPedido) throws ValidadorException {
        if (Objects.isNull(itemPedido))
            throw new ValidadorException("Item contido no Pedido encontra-se inválido e/ou inexistente [NULO].");
    }

    @Override
    public void validarIdReferenteItemPedido(ItemPedido itemPedido) throws ValidadorException {
        if (Objects.isNull(itemPedido.getId()))
            throw new ValidadorException("Item contido no Pedido encontra-se com a referência do [ID] inválida e/ou inexistente [NULA].");
    }

    @Override
    public void validarSomenteAdicionalItem(AdicionalItemPedido adicionalItemPedido) throws ValidadorException {
        if (Objects.isNull(adicionalItemPedido))
            throw new ValidadorException("Adicional contido no item do Pedido, encontra-se inválido e/ou inexistente [NULO].");
    }

    @Override
    public void validarIdReferenteAdicionalItem(AdicionalItemPedido adicionalItemPedido) throws ValidadorException {
        if (Objects.isNull(adicionalItemPedido.getId()))
            throw new ValidadorException("Adicional contido no item do Pedido, encontra-se com a referência do [ID] inválida e/ou inexistente [NULA].");
    }

    @Override
    public void validarParamsEDependenciasPedido(Pedido pedido) throws ValidadorException {
        validarSomentePedido(pedido);

        if (Objects.isNull(pedido.getNomeCliente()) || pedido.getNomeCliente().isEmpty())
            throw new ValidadorException("Nome do Cliente contido ao Pedido encontra-se inválido e/ou inexistente.");

        if (Objects.isNull(pedido.getTelefone()) || pedido.getTelefone().isEmpty())
            throw new ValidadorException("Telefone de contato do Cliente contido ao Pedido encontra-se inválido e/ou inexistente.");

        if (Objects.isNull(pedido.getItens()) || pedido.getItens().isEmpty())
            throw new ValidadorException("Nenhum item e/ou lanched encontrado ou adicionado ao Pedido. Encontra-se inválido(s) e/ou inexistente(s). Adicione um item e tente novamente...");
    }
}
