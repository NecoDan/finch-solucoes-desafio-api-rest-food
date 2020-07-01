package br.com.finch.api.food.service.generate;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.ItemLancheIngrediente;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.dtos.LanchesWrapper;
import br.com.finch.api.food.repository.IngredienteRepository;
import br.com.finch.api.food.repository.LancheRepository;
import br.com.finch.api.food.service.ILancheService;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import br.com.finch.api.food.validation.ILancheValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeraLancheService implements IGeraLancheService {

    private final ILancheValidation lancheValidation;
    private final ILancheService lancheService;
    private final LancheRepository lancheRepository;
    private final IngredienteRepository ingredienteRepository;

    @Override
    public LanchesWrapper gerar(List<Lanche> lanches) throws ValidadorException {
        if (Objects.isNull(lanches) || lanches.isEmpty())
            throw new ValidadorException("Não existem pedido(s) ou pedido(s) encontram-se inexistente(s) a serem processados pela API.");

        LanchesWrapper lanchesWrapper = LanchesWrapper.builder().build();
        for (Lanche lanche : lanches)
            lanchesWrapper.add(this.lancheService.salvar(lanche));
        return lanchesWrapper;
    }

    @Override
    @Transactional
    public LanchesWrapper adicionarIngrediente(Long lancheId, BigDecimal qtde, Ingrediente ingrediente) throws ValidadorException {
        this.lancheValidation.validarParametrosActionReferenteIngredienteLanche(lancheId, ingrediente);
        LanchesWrapper lanchesWrapper = LanchesWrapper.builder().build();

        if (verificaExistsParametrosActionReferenteIngredientesLanche(ingrediente, lancheId))
            return lanchesWrapper;

        ItemLancheIngrediente itemLancheIngrediente = ItemLancheIngrediente.builder()
                .lanche(Lanche.builder().id(lancheId).build())
                .ingrediente(ingrediente)
                .quantidade(qtde)
                .build()
                .gerarDataCorrente()
                .ativado();

        if (lancheRepository.existsItemIngredienteLanche(itemLancheIngrediente)) {
            this.lancheRepository.atualizarItemIngredienteLanche(itemLancheIngrediente);
        } else {
            this.lancheRepository.inserirItemIngredienteLanche(itemLancheIngrediente);
        }

        lanchesWrapper.add(recalcularValorTotalLanche(this.lancheService.recuperarPorId(lancheId)));
        return lanchesWrapper;
    }

    @Override
    @Transactional
    public boolean removerIngrediente(Long lancheId, Ingrediente ingrediente) throws ValidadorException {
        this.lancheValidation.validarParametrosActionReferenteIngredienteLanche(lancheId, ingrediente);

        if (verificaExistsParametrosActionReferenteIngredientesLanche(ingrediente, lancheId))
            return false;

        ItemLancheIngrediente itemLancheIngrediente = ItemLancheIngrediente.builder()
                .ingrediente(ingrediente)
                .lanche(Lanche.builder().id(lancheId).build())
                .build();

        if (lancheRepository.existsItemIngredienteLanche(itemLancheIngrediente))
            return false;

        this.lancheRepository.removerItemIngredienteLanche(itemLancheIngrediente);
        recalcularValorTotalLanche(this.lancheService.recuperarPorId(lancheId));
        return true;
    }

    @Override
    @Transactional
    public Lanche recalcularValorTotalLanche(Lanche lanche) throws ValidadorException {
        this.lancheValidation.validarSomenteLanche(lanche);

        if (Objects.isNull(lanche.getIngredientes()) || lanche.getIngredientes().isEmpty())
            lanche.addAllIngrediente(this.ingredienteRepository.recuperarIngredientesContidoAoLanche(lanche));

        lanche.recalculaValorTotal(this.lancheRepository.recuperarValorTotalLancheRecalculado(lanche));
        this.lancheRepository.saveAndFlush(lanche);
        return lanche;
    }

    @Override
    public boolean atualizarLanchePor(Long lancheId, Lanche lanche) throws ValidadorException {
        return this.lancheService.atualizarPor(lancheId, lanche);
    }

    @Override
    public boolean excluirLanche(Lanche lanche) throws ValidadorException {
        return this.lancheService.excluir(lanche);
    }

    private boolean verificaExistsParametrosActionReferenteIngredientesLanche(Ingrediente ingrediente, Long lancheId) {
        return !ingredienteRepository.existsById(ingrediente.getId()) || !this.lancheRepository.existsById(lancheId);
    }
}
