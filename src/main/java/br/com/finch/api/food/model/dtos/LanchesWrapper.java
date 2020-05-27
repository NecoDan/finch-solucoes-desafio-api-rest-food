package br.com.finch.api.food.model.dtos;

import br.com.finch.api.food.model.Lanche;
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
@JacksonXmlRootElement(localName = "Lanches")
@AllArgsConstructor
public class LanchesWrapper implements Serializable{

    private static final long serialVersionUID = 22L;

    @JacksonXmlProperty(localName = "Lanche")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Lanche> lanches;

    public void add(Lanche lanche) {
        if (Objects.isNull(this.lanches))
            this.lanches = new ArrayList<>();
        this.lanches.add(lanche);
    }

    public LanchesWrapper adicionar(Lanche lanche) {
        this.add(lanche);
        return this;
    }
}
