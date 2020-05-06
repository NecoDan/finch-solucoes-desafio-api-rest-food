package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.reports.LanchesWrapper;
import br.com.finch.api.food.repository.LancheRepository;
import br.com.finch.api.food.service.ILancheService;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class LancheReportsService implements ILancheReportsService {

    private final ILancheService lancheService;
    private final LancheRepository lancheRepository;

    @Override
    public LanchesWrapper listarPorId(Long id) throws ValidadorException {
        return LanchesWrapper.builder().build().adicionar(this.lancheService.recuperarPorId(id));
    }

    @Override
    public LanchesWrapper listarTodos() {
        return LanchesWrapper.builder().lanches(this.lancheService.recuperarTodos()).build();
    }

    @Override
    public LanchesWrapper listarAtivos() {
        return LanchesWrapper.builder().lanches(this.lancheService.recuperarAtivos()).build();
    }

    @Override
    public LanchesWrapper listarInativos() {
        return LanchesWrapper.builder().lanches(this.lancheService.recuperarInativos()).build();
    }

    @Override
    public LanchesWrapper listarPorDescricaoNome(String descricao) throws ValidadorException {
        return LanchesWrapper.builder().lanches(this.lancheService.recuperarPorDescricao(descricao)).build();
    }

    @Override
    public LanchesWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return LanchesWrapper.builder()
                .lanches(lancheRepository.findAllByValorTotalIsNotNullAndValorTotalGreaterThanEqual(valor))
                .build();
    }

    @Override
    public LanchesWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return LanchesWrapper.builder()
                .lanches(lancheRepository.findAllByValorTotalIsNotNullAndValorTotalLessThanEqual(valor))
                .build();
    }

    @Override
    public LanchesWrapper listarPorDataInsercao(LocalDate data) throws ValidadorException {
        return LanchesWrapper.builder().lanches(lancheRepository.recuperarTodosPorDataInsercao(data)).build();
    }

    @Override
    public LanchesWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException {
        return LanchesWrapper.builder().lanches(lancheRepository.recuperarTodosPorPeriodo(dataInicio, dataFim)).build();
    }
}
