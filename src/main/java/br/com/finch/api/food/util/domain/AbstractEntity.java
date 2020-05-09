package br.com.finch.api.food.util.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@SuperBuilder
@Data
@EqualsAndHashCode
@JacksonXmlRootElement
public class AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(value = AccessLevel.PUBLIC)
    @JacksonXmlProperty
    private Long id;

    @JacksonXmlProperty
    @Column(name = "dt_cadastro")
    @Setter(value = AccessLevel.PUBLIC)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", locale = "pt-BR", timezone = "UTC")
    private LocalDateTime dataCadastro;

    @JacksonXmlProperty
    @Setter(value = AccessLevel.PUBLIC)
    @Column(name = "ativo", columnDefinition = "tinyint(1) default 1", nullable = false)
    private boolean ativo = true;

    @Tolerate
    public AbstractEntity() {
    }

    public void gerarDataCorrente() {
        if (Objects.isNull(this.getDataCadastro()))
            this.setDataCadastro(LocalDateTime.now());
    }

    public void ativado() {
        this.ativo = true;
    }

    public void desativado() {
        this.ativo = false;
    }
}
