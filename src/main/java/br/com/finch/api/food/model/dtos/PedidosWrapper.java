package br.com.finch.api.food.model.dtos;

import br.com.finch.api.food.model.Pedido;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@ToString
@JacksonXmlRootElement(localName = "Pedidos")
@AllArgsConstructor
public class PedidosWrapper implements Serializable {

    private static final long serialVersionUID = 22L;

    @JacksonXmlProperty(localName = "Pedido")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Pedido> pedidos;

    public void add(Pedido pedido) {
        if (Objects.isNull(this.pedidos))
            this.pedidos = new ArrayList<>();
        this.pedidos.add(pedido);
    }

    public PedidosWrapper adicionar(Pedido pedido) {
        this.add(pedido);
        return this;
    }
}
