package br.com.finch.api.food.service.reports;

import br.com.finch.api.food.model.dtos.IngredientesWrapper;
import br.com.finch.api.food.repository.IngredienteRepository;
import br.com.finch.api.food.service.IIngredienteService;
import br.com.finch.api.food.util.exceptions.ValidadorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngredienteReportService implements IIngredienteReportService {

    private final IIngredienteService ingredienteService;
    private final IngredienteRepository ingredienteRepository;

    @Override
    public IngredientesWrapper listarPorId(Long id) throws ValidadorException {
        return IngredientesWrapper.builder().build().adicionar(this.ingredienteService.recuperarPorId(id));
    }

    @Override
    public IngredientesWrapper listarTodos() {
        return IngredientesWrapper.builder().ingredientes(ingredienteService.recuperarTodos()).build();
    }

    @Override
    public IngredientesWrapper listarAtivos() {
        return IngredientesWrapper.builder().ingredientes(ingredienteService.recuperarAtivos()).build();
    }

    @Override
    public IngredientesWrapper listarInativos() {
        return IngredientesWrapper.builder().ingredientes(ingredienteService.recuperarInativos()).build();
    }

    @Override
    public IngredientesWrapper listarPorDescricaoNome(String descricao) throws ValidadorException {
        return IngredientesWrapper.builder().ingredientes(ingredienteService.recuperarPorDescricao(descricao)).build();
    }

    @Override
    public IngredientesWrapper listarPorValorMaiorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return IngredientesWrapper.builder()
                .ingredientes(ingredienteRepository.findAllByCustoIsNotNullAndCustoGreaterThanEqual(valor))
                .build();
    }

    @Override
    public IngredientesWrapper listarPorValorMenorOuIgualQue(BigDecimal valor) throws ValidadorException {
        return IngredientesWrapper.builder()
                .ingredientes(ingredienteRepository.findAllByCustoIsNotNullAndCustoLessThanEqual(valor))
                .build();
    }

    @Override
    public IngredientesWrapper listarPorDataInsercao(LocalDate data) throws ValidadorException {
        return IngredientesWrapper.builder().ingredientes(ingredienteService.recuperarPorDataInsercao(data)).build();
    }

    @Override
    public IngredientesWrapper listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ValidadorException {
        return IngredientesWrapper.builder().ingredientes(ingredienteService.recuperarTodosPorPeriodo(dataInicio, dataFim)).build();
    }
}
