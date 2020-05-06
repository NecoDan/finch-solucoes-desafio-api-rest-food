package br.com.finch.api.food.service;

import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.reports.LanchesWrapper;
import br.com.finch.api.food.repository.LancheRepository;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import br.com.finch.api.food.validation.LancheValidation;
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
public class LancheService implements ILancheService {

    private final LancheRepository lancheRepository;
    private final LancheValidation lancheValidation;

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
        return null;
    }

    @Override
    public List<Lanche> recuperarTodosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException {
        return null;
    }

    @Override
    public List<Lanche> recuperarPorDataInsercao(LocalDate data) throws ValidadorException {
        return null;
    }

    @Override
    public LanchesWrapper gerar(List<Lanche> lanches) throws ValidadorException {
        if (Objects.isNull(lanches) || lanches.isEmpty())
            throw new ValidadorException("Não existem pedido(s) ou pedido(s) inexistente(s) a serem processados pela API.");

        LanchesWrapper lanchesWrapper = LanchesWrapper.builder().build();
        for (Lanche lanche : lanches)
            lanchesWrapper.add(salvar(lanche));
        return lanchesWrapper;
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
