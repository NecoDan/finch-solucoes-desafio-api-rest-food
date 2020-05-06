package br.com.finch.api.food.service;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.reports.IngredientesWrapper;
import br.com.finch.api.food.repository.IngredienteRepository;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import br.com.finch.api.food.validation.IngredienteValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngredienteService implements IIngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final IngredienteValidation ingredienteValidation;

    @Override
    public Ingrediente recuperarPorId(Long id) throws ValidadorException {
        if (Objects.isNull(id))
            throw new ValidadorException("Parametro com a referência [ID] ao Ingrediente encontra-se inválida e/ou inexistente [NULA].");
        return this.ingredienteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ingrediente> recuperarTodos() {
        return this.ingredienteRepository.findAll();
    }

    @Override
    public List<Ingrediente> recuperarAtivos() {
        return this.ingredienteRepository.findAllByAtivo(true);
    }

    @Override
    public List<Ingrediente> recuperarInativos() {
        return this.ingredienteRepository.findAllByAtivo(false);
    }

    @Override
    public List<Ingrediente> recuperarPorDescricao(String descricao) throws ValidadorException {
        if (Objects.isNull(descricao) || descricao.isEmpty())
            throw new ValidadorException("Parametro com a descricao referente a busca do(s) Ingrediente(s), encontra-se inválida [NULA] e/ou conteudo vazio.");
        return this.ingredienteRepository.findAllByDescricaoContainingIgnoreCase(descricao);
    }

    @Override
    public List<Ingrediente> recuperarTodosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException {
        if (Objects.isNull(dataInicio) || Objects.isNull(dataFim))
            throw new ValidadorException("Parametro com a datas referente a busca do(s) Ingrediente(s), encontra-se inválida e/ou inexistente [NULA].");
        return this.ingredienteRepository.recuperarTodosPorPeriodo(dataInicio, dataFim);
    }

    @Override
    public List<Ingrediente> recuperarPorDataInsercao(LocalDate data) throws ValidadorException {
        if (Objects.isNull(data))
            throw new ValidadorException("Parametro com a data referente a busca do(s) Ingrediente(s), encontra-se inválida e/ou inexistente [NULA].");
        return this.ingredienteRepository.recuperarTodosPorDataInsercao(data);
    }

    @Override
    public IngredientesWrapper gerar(List<Ingrediente> ingredientes) throws ValidadorException {
        if (Objects.isNull(ingredientes) || ingredientes.isEmpty())
            throw new ValidadorException("Não existem pedido(s) ou pedido(s) inexistente(s) a serem processados pela API.");

        IngredientesWrapper ingredientesWrapper = IngredientesWrapper.builder().build();
        for (Ingrediente ingrediente : ingredientes)
            ingredientesWrapper.add(salvar(ingrediente));

        return ingredientesWrapper;
    }

    @Override
    @Transactional
    public Ingrediente salvar(Ingrediente ingrediente) throws ValidadorException {
        this.ingredienteValidation.validarIngrediente(ingrediente);
        ingrediente.gerarDataCorrente();
        return this.ingredienteRepository.saveAndFlush(ingrediente);
    }

    @Override
    @Transactional
    public boolean atualizarPor(Long ingredienteId, Ingrediente ingrediente) throws ValidadorException {
        if (Objects.isNull(ingredienteId))
            throw new ValidadorException("Parametro com a referência [ID] ao Ingrediente encontra-se inválida e/ou inexistente [NULA].");

        Ingrediente ingredienteAtualizar = this.ingredienteRepository.findById(ingredienteId)
                .map(i -> {
                    i.setDescricao((Objects.isNull(ingrediente.getDescricao()) || ingrediente.getDescricao().isEmpty()) ? i.getDescricao() : ingrediente.getDescricao());
                    i.setCusto(Objects.isNull(ingrediente.getCusto()) ? i.getCusto() : ingrediente.getCusto());
                    i.setAtivo(ingrediente.isAtivo());
                    i.gerarDataCorrente();
                    return i;
                }).orElse(null);

        if (Objects.isNull(ingredienteAtualizar))
            return false;

        this.ingredienteRepository.save(ingredienteAtualizar);
        return true;
    }

    @Override
    @Transactional
    public boolean excluir(Ingrediente ingrediente) throws ValidadorException {
        this.ingredienteValidation.validarIdReferenteIngrediente(ingrediente);

        return this.ingredienteRepository.findById(ingrediente.getId())
                .map(i -> {
                    this.ingredienteRepository.deleteById(i.getId());
                    return true;
                }).orElse(false);
    }
}
