package br.com.finch.api.food.controller;

import br.com.finch.api.food.model.Ingrediente;
import br.com.finch.api.food.model.Lanche;
import br.com.finch.api.food.model.dtos.LanchesWrapper;
import br.com.finch.api.food.service.ILancheService;
import br.com.finch.api.food.service.reports.ILancheReportsService;
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
@RequestMapping(path = "lanches")
@RequiredArgsConstructor
@Api(value = "Lanche")
public class LancheController {

    private final ILancheService lancheService;
    private final ILancheReportsService lancheReportsService;

    @ApiOperation(value = "Retornar todos os lanches existentes...")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAll() {
        return retornarListaLanches();
    }

    @ApiOperation(value = "Retornar todos os lanches existentes, em formato XML...")
    @GetMapping(path = "/exibirTodosEmXML", produces = {MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllInXML() {
        return retornarListaLanches();
    }

    @ApiOperation(value = "Retornar todos os lanches existentes, em formato JSON...")
    @GetMapping(path = "/exibirTodosEmJSON", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> listAllInJson() {
        return retornarListaLanches();
    }

    @ApiOperation(value = "Retorna um único lanches existente, a partir de seu id registrado...")
    @GetMapping(path = "/{lancheId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> buscarPorId(@PathVariable Long lancheId) {
        try {
            return getResponseDefault(lancheReportsService.listarPorId(lancheId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Responsável por persistir uma única instância de Lanche, a partir de um filtro consumer como corpo da requisição...")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> save(@RequestBody Lanche lanche) {
        try {
            return new ResponseEntity<>(lancheService.gerar(Collections.singletonList(lanche)), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar lanches(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por persistir múltiplas instâncias de Lanche, a partir de um filtro consumer como corpo da requisição...")
    @PostMapping(path = "/salvarVarios", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> saveAll(@RequestBody List<Lanche> lancheList) {
        try {
            return new ResponseEntity<>(lancheService.gerar(lancheList), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar lanches(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por atualizar um Lanche, a partir de um @PathVariable contendo o id do registro e consumer como corpo da requisição...")
    @PutMapping("/{lancheId}")
    public ResponseEntity<?> atualizar(@PathVariable Long lancheId, @Valid @RequestBody Lanche lanche) {
        try {
            if (!lancheService.atualizarPor(lancheId, lanche))
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar atualizar lanches(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por exluir um Lanche, a partir de um @PathVariable contendo o id do registro...")
    @DeleteMapping("/{lancheId}")
    public ResponseEntity<?> remover(@PathVariable Long lancheId) {
        try {
            if (!lancheService.excluir(Lanche.builder().id(lancheId).build()))
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar remover lanche(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por adicionar um novo item ingrediente pertecente ao Lanche, a partir de um @RequestParam contendo o [ID] do registro Lanche e o [ID] do registro Ingrediente...")
    @PostMapping(path = "/adicionaIngrediente", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> adicionarIngrediente(@RequestParam("lancheId") Long lancheId, @RequestParam("qtde") BigDecimal qtde,
                                                  @RequestBody Ingrediente ingrediente) {
        try {
            return new ResponseEntity<>(lancheService.adicionarIngrediente(lancheId, qtde, ingrediente), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar lanche(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por excluir um item ingrediente pertecente ao Lanche, a partir de um @RequestParam contendo o [ID] do registro Lanche e o [ID] do registro Ingrediente...")
    @DeleteMapping(path = "/removeIngrediente")
    public ResponseEntity<?> removerIngrediente(@RequestParam("lancheId") Long lancheId, @RequestParam("ingredienteId") Long ingredienteId) {
        try {
            if (!lancheService.removerIngrediente(lancheId, Ingrediente.builder().id(ingredienteId).build()))
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao tentar remover lanche(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Retornar todos os lanches existentes, encontram-se ativos...")
    @GetMapping(path = "/exibirAtivos", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllAtivos() {
        try {
            return getResponseDefault(this.lancheReportsService.listarAtivos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retornar todos os lanches existentes, encontram-se inativos...")
    @GetMapping(path = "/exibirInativos", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllInativos() {
        try {
            return getResponseDefault(this.lancheReportsService.listarInativos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os lanches existentes, a partir do filtro <b>descricao</b> que estejam igual ou semelhante a passada como parâmetro ...")
    @GetMapping(path = "/buscarPorDescricao", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorDescricao(@RequestParam("descricao") String descricao) {
        try {
            return getResponseDefault(this.lancheReportsService.listarPorDescricaoNome(descricao));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os lanches existentes, a partir do filtro <b>valor</b> que estejam igual ou maior ao passado como parâmetro ...")
    @GetMapping(path = "/buscarPorValorIgualOuMaior", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorValorMaiorOuIgual(@RequestParam("valor") BigDecimal valor) {
        try {
            return getResponseDefault(this.lancheReportsService.listarPorValorMaiorOuIgualQue(valor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os lanches existentes, a partir do filtro <b>valor</b> que estejam menor ou maior ao passado como parâmetro ...")
    @GetMapping(path = "/buscarPorValorIgualOuMenor", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorValorMenorOuIgual(@RequestParam("valor") BigDecimal valor) {
        try {
            return getResponseDefault(this.lancheReportsService.listarPorValorMenorOuIgualQue(valor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os lanches existentes, a partir do filtro <b>data</b> aos quais foram gravadas no banco de dados. Com o formato da data (dd/MM/yyyy).")
    @GetMapping(path = "/buscarPorDataCadastro", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorDataCadastro(@RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data) {
        try {
            return getResponseDefault(this.lancheReportsService.listarPorDataInsercao(data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os lanches existentes, a partir dos filtro(s) <b>Data Inicial</b> e <b>Data Final</b> aos quais foram gravadas no banco de dados. Com o formato da data (dd/MM/yyyy).")
    @GetMapping(path = "/buscarPorPeriodo", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorPeriodo(@RequestParam("dataInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
                                               @RequestParam("dataFim") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim) {
        try {
            return getResponseDefault(this.lancheReportsService.listarPorPeriodo(dataInicio, dataFim));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @GetMapping("/testException")
    public void testException() {
        Integer x = 2 / 0;
    }

    private ResponseEntity<?> getResponseDefault(LanchesWrapper lanchesWrapper) {
        if (Objects.isNull(lanchesWrapper) || (lanchesWrapper.getLanches().isEmpty()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(lanchesWrapper);
    }

    private ResponseEntity<?> retornarListaLanches() {
        try {
            return getResponseDefault(this.lancheReportsService.listarTodos());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao executar a busca de dados: " + ex.getMessage());
        }
    }
}
