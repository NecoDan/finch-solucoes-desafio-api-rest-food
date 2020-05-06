package br.com.finch.api.food.service.strategy;

import br.com.finch.api.food.service.negocio.Promocao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BuilderDescontoService implements IBuilderDescontoService {

    private final BuilderDescontoStrategy builderDescontoStrategy;

    @Override
    public Promocao obterPromocaoAPartir(BigDecimal quantidade) {
        return builderDescontoStrategy.obter(quantidade);
    }
}
