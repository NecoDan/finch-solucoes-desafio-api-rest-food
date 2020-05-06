package br.com.finch.api.food.controller;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.reports.IngredientesWrapper;
import br.com.finch.api.food.service.IIngredienteService;
import br.com.finch.api.food.service.reports.IIngredienteReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@Validated
@RequestMapping(path = "ingredientes")
@RequiredArgsConstructor
@Api(value = "Ingrediente")
public class IngredienteController {

    private final IIngredienteService ingredienteService;
    private final IIngredienteReportService ingredienteReportService;

    @ApiOperation(value = "Retornar todos os ingredientes existentes...")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAll() {
        return retornarListaIngredientes();
    }

    @ApiOperation(value = "Retornar todos os ingredientes existentes, em formato XML...")
    @GetMapping(path = "/exibirTodosEmXML", produces = {MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllInXML() {
        return retornarListaIngredientes();
    }

    @ApiOperation(value = "Retornar todos os ingredientes existentes, em formato JSON...")
    @GetMapping(path = "/exibirTodosEmJSON", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> listAllInJson() {
        return retornarListaIngredientes();
    }

    @ApiOperation(value = "Retorna um único ingredientes existente, a partir de seu id registrado...")
    @GetMapping(path = "/{ingredienteId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> buscarPorId(@PathVariable Long ingredienteId) {
        try {
            return getResponseDefault(this.ingredienteReportService.listarPorId(ingredienteId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Responsável por persistir uma única instância de Ingrediente, a partir de um filtro consumer como corpo da requisição...")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> save(@RequestBody Ingrediente ingrediente) {
        try {
            return new ResponseEntity<>(ingredienteService.gerar(Collections.singletonList(ingrediente)), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar ingrediente(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por persistir múltiplas instâncias de Ingrediente, a partir de um filtro consumer como corpo da requisição...")
    @PostMapping(path = "/salvarVarios", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> saveAll(@RequestBody List<Ingrediente> ingredientes) {
        try {
            return new ResponseEntity<>(ingredienteService.gerar(ingredientes), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar ingrediente(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por atualizar um Ingrediente, a partir de um @PathVariable contendo o id do registro e consumer como corpo da requisição...")
    @PutMapping("/{ingredienteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long ingredienteId, @Valid @RequestBody Ingrediente ingrediente) {
        try {
            if (!ingredienteService.atualizarPor(ingredienteId, ingrediente))
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar atualizar ingrediente(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por exluir um Ingrediente, a partir de um @PathVariable contendo o id do registro...")
    @DeleteMapping("/{ingredienteId}")
    public ResponseEntity<?> remover(@PathVariable Long ingredienteId) {
        try {
            if (!ingredienteService.excluir(Ingrediente.builder().id(ingredienteId).build()))
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar remover ingrediente(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Retornar todos os ingredientes existentes, encontram-se ativos...")
    @GetMapping(path = "/exibirAtivos", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllAtivos() {
        try {
            return getResponseDefault(this.ingredienteReportService.listarAtivos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retornar todos os ingredientes existentes, encontram-se inativos...")
    @GetMapping(path = "/exibirInativos", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllInAtivos() {
        try {
            return getResponseDefault(this.ingredienteReportService.listarInativos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os ingredientes existentes, a partir do filtro <b>descricao</b> que estejam igual ou semelhante a passada como parâmetro ...")
    @GetMapping(path = "/buscarPorDescricao", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllInAtivos(@RequestParam("descricao") String descricao) {
        try {
            return getResponseDefault(this.ingredienteReportService.listarPorDescricaoNome(descricao));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os ingredientes existentes, a partir do filtro <b>valor</b> que estejam igual ou maior ao passado como parâmetro ...")
    @GetMapping(path = "/buscarPorValorIgualOuMaior", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorValorMaiorOuIgual(@RequestParam("valor") BigDecimal valor) {
        try {
            return getResponseDefault(this.ingredienteReportService.listarPorValorMaiorOuIgualQue(valor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os ingredientes existentes, a partir do filtro <b>valor</b> que estejam menor ou maior ao passado como parâmetro ...")
    @GetMapping(path = "/buscarPorValorIgualOuMenor", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorValorMenorOuIgual(@RequestParam("valor") BigDecimal valor) {
        try {
            return getResponseDefault(this.ingredienteReportService.listarPorValorMenorOuIgualQue(valor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os ingredientes existentes, a partir do filtro <b>data</b> aos quais foram gravadas no banco de dados. Com o formato da data (dd/MM/yyyy).")
    @GetMapping(path = "/buscarPorDataCadastro", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorDataCadastro(@RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data) {
        try {
            return getResponseDefault(this.ingredienteReportService.listarPorDataInsercao(data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os ingredientes existentes, a partir dos filtro(s) <b>Data Inicial</b> e <b>Data Final</b> aos quais foram gravadas no banco de dados. Com o formato da data (dd/MM/yyyy).")
    @GetMapping(path = "/buscarPorPeriodo", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorPeriodo(@RequestParam("dataInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
                                               @RequestParam("dataFim") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim) {
        try {
            return getResponseDefault(this.ingredienteReportService.listarPorPeriodo(dataInicio, dataFim));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    private ResponseEntity<?> getResponseDefault(IngredientesWrapper ingredientesWrapper) {
        if (Objects.isNull(ingredientesWrapper) || (ingredientesWrapper.getIngredientes().isEmpty()))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ingredientesWrapper, HttpStatus.OK);
    }

    private ResponseEntity<?> retornarListaIngredientes() {
        try {
            return getResponseDefault(this.ingredienteReportService.listarTodos());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao executar a busca de dados: " + ex.getMessage());
        }
    }
}
