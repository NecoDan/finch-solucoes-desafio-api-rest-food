package br.com.finch.api.food.model.dtos;

import br.com.finch.api.food.model.Ingrediente;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@ToString
@JacksonXmlRootElement(localName = "Ingredientes")
@AllArgsConstructor
public class IngredientesWrapper {

    private static final long serialVersionUID = 22L;

    @JacksonXmlProperty(localName = "Ingrediente")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Ingrediente> ingredientes;

    public void add(Ingrediente ingrediente) {
        if (Objects.isNull(this.ingredientes))
            this.ingredientes = new ArrayList<>();
        this.ingredientes.add(ingrediente);
    }

    public IngredientesWrapper adicionar(Ingrediente ingrediente){
        this.add(ingrediente);
        return this;
    }
}
