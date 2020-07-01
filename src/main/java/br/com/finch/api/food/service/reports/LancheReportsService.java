package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.dtos.LanchesWrapper;
import br.com.finch.api.food.repository.LancheRepository;
import br.com.finch.api.food.service.ILancheService;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LancheReportsService implements ILancheReportsService {

    private final ILancheService lancheService;
    private final LancheRepository lancheRepository;

    @Override
    public LanchesWrapper listarPorId(Long id) throws ValidadorException {
        Optional<Lanche> lancheOptional = this.lancheRepository.findById(id);
        return lancheOptional.map(lanche -> getWrapper(Collections.singletonList(lanche))).orElse(getWrapper(Collections.emptyList()));
    }

    @Override
    public LanchesWrapper listarTodos() {
        return getWrapper(this.lancheService.recuperarTodos());
    }

    @Override
    public LanchesWrapper listarAtivos() {
        return getWrapper(this.lancheService.recuperarAtivos());
    }

    @Override
    public LanchesWrapper listarInativos() {
        return getWrapper(this.lancheService.recuperarInativos());
    }

    @Override
    public LanchesWrapper listarPorDescricaoNome(String descricao) throws ValidadorException {
        return getWrapper(this.lancheService.recuperarPorDescricao(descricao));
    }

    @Override
    public LanchesWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return LanchesWrapper.builder()
                .lanches(lancheRepository.findAllByValorTotalIsNotNullAndValorTotalGreaterThanEqual(valor))
                .build();
    }

    @Override
    public LanchesWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return getWrapper(lancheRepository.findAllByValorTotalIsNotNullAndValorTotalLessThanEqual(valor));
    }

    @Override
    public LanchesWrapper listarPorDataInsercao(LocalDate data) throws ValidadorException {
        return getWrapper(this.lancheRepository.recuperarTodosPorDataInsercao(data));
    }

    @Override
    public LanchesWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException {
        return getWrapper(this.lancheRepository.recuperarTodosPorPeriodo(dataInicio, dataFim));
    }

    private LanchesWrapper getWrapper(List<Lanche> lanches) {
        return LanchesWrapper.builder().lanches(lanches).build();
    }
}
