package br.com.finch.api.food.service;

import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.repository.IngredienteRepository;
import br.com.finch.api.food.repository.LancheRepository;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import br.com.finch.api.food.validation.ILancheValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LancheService implements ILancheService {

    private final LancheRepository lancheRepository;
    private final IngredienteRepository ingredienteRepository;
    private final ILancheValidation lancheValidation;

    @Override
    public Lanche recuperarPorId(Long id) throws ValidadorException {
        if (Objects.isNull(id))
            throw new ValidadorException("Parametro com a referência [ID] ao Ingrediente encontra-se inválida e/ou inexistente [NULA].");
        return this.lancheRepository.findById(id).orElse(null);
    }

    @Override
    public List<Lanche> recuperarTodos() {
        return this.lancheRepository.findAll();
    }

    @Override
    public List<Lanche> recuperarAtivos() {
        return this.lancheRepository.findAllByAtivo(true);
    }

    @Override
    public List<Lanche> recuperarInativos() {
        return this.lancheRepository.findAllByAtivo(false);
    }

    @Override
    public List<Lanche> recuperarPorDescricao(String descricao) throws ValidadorException {
        if (Objects.isNull(descricao) || descricao.isEmpty())
            throw new ValidadorException("Parametro com a descricao referente a busca do(s) Lanche(s), encontra-se inválida [NULA] e/ou conteudo vazio.");
        return this.lancheRepository.findAllByDescricaoContainingIgnoreCase(descricao);
    }

    @Override
    public List<Lanche> recuperarTodosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException {
        if (Objects.isNull(dataInicio) || Objects.isNull(dataFim))
            throw new ValidadorException("Parametro com a datas referente a busca do(s) Lanche(s), encontra-se inválida e/ou inexistente [NULA].");
        return this.lancheRepository.recuperarTodosPorPeriodo(dataInicio, dataFim);
    }

    @Override
    public List<Lanche> recuperarPorDataInsercao(LocalDate data) throws ValidadorException {
        if (Objects.isNull(data))
            throw new ValidadorException("Parametro com a data referente a busca do(s) Lanche(s), encontra-se inválida e/ou inexistente [NULA].");
        return this.lancheRepository.recuperarTodosPorDataInsercao(data);
    }

    @Override
    @Transactional
    public Lanche salvar(Lanche lanche) throws ValidadorException {
        this.lancheValidation.validarLanche(lanche);
        lanche.gerarDataCorrente();
        return this.lancheRepository.saveAndFlush(lanche);
    }

    @Override
    @Transactional
    public boolean atualizarPor(Long lancheId, Lanche lanche) throws ValidadorException {
        if (Objects.isNull(lancheId))
            throw new ValidadorException("Parametro com a referência [ID] ao Lanche, encontra-se inválida e/ou inexistente [NULA].");

        Lanche lancheAtualizar = this.lancheRepository.findById(lancheId)
                .map(l -> {
                    l.setDescricao((Objects.isNull(lanche.getDescricao()) || lanche.getDescricao().isEmpty()) ? l.getDescricao() : lanche.getDescricao());
                    l.setValorTotal(Objects.isNull(lanche.getValorTotal()) ? l.getValorTotal() : lanche.getValorTotal());
                    l.setAtivo(lanche.isAtivo());
                    l.gerarDataCorrente();
                    return l;
                }).orElse(null);

        if (Objects.isNull(lancheAtualizar))
            return false;

        this.lancheRepository.save(lancheAtualizar);
        return true;
    }

    @Override
    @Transactional
    public boolean excluir(Lanche lanche) throws ValidadorException {
        this.lancheValidation.validarIdReferenteLanche(lanche);

        return this.lancheRepository.findById(lanche.getId())
                .map(i -> {
                    this.lancheRepository.deleteById(i.getId());
                    return true;
                }).orElse(false);
    }
}
