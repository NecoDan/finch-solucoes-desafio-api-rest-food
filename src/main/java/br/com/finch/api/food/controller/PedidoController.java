package br.com.finch.api.food.controller;

import br.com.finch.api.food.model.Pedido;
import br.com.finch.api.food.model.dtos.FilterPedido;
import br.com.finch.api.food.model.dtos.PedidosWrapper;
import br.com.finch.api.food.service.geradores.IGeradorPedidoLancheService;
import br.com.finch.api.food.service.reports.IPedidoReportService;
import br.com.finch.api.food.util.exceptions.ResourceStatusNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@Validated
@RequestMapping(path = "pedidos")
@RequiredArgsConstructor
@Api(value = "Pedido")
public class PedidoController {

    private final IPedidoReportService pedidoReportService;
    private final IGeradorPedidoLancheService geradorPedidoLancheService;

    @ApiOperation(value = "Retornar todos os pedidos de lanches existentes...")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAll() {
        return retornarListaPedidos();
    }

    @ApiOperation(value = "Retornar todos os pedidos de lanches, em formato XML...")
    @GetMapping(path = "/exibirTodosEmXML", produces = {MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllInXML() {
        return retornarListaPedidos();
    }

    @ApiOperation(value = "Retornar todos os pedidos de lanches, em formato JSON...")
    @GetMapping(path = "/exibirTodosEmJSON", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> listAllInJson() {
        return retornarListaPedidos();
    }

    @ApiOperation(value = "Retorna um único pedido de lanche existente, a partir de seu [ID] (número pedido) registrado...")
    @GetMapping(path = "/{pedidoId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> buscarPorId(@PathVariable Long pedidoId) {
        try {
            return getResponseDefault(pedidoReportService.listarPorId(pedidoId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna um único pedido de lanche existente, a partir de seu [ID] (número pedido) registrado...")
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Pedido> buscarPorIdPedido(@PathVariable("id") Long pedidoId) {
        return new ResponseEntity<>(pedidoReportService.listarPorBuscaId(pedidoId).orElseThrow(
                () -> new ResourceStatusNotFoundException("Pedido")), HttpStatus.OK);
    }

    @ApiOperation(value = "Responsável por persistir somente um único Pedido, a partir de um filtro consumer como corpo da requisição...")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> save(@Valid @RequestBody Pedido pedido) {
        try {
            return new ResponseEntity<>(geradorPedidoLancheService.gerar(Collections.singletonList(pedido)), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar pedido(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por persistir múltiplas Pedidos, a partir de um filtro consumer como corpo da requisição...")
    @PostMapping(path = "/salvarVarios", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> saveAll(@Valid @RequestBody List<Pedido> pedidoList) {
        try {
            return new ResponseEntity<>(geradorPedidoLancheService.gerar(pedidoList), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar pedido(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por adicionar um novo item ao Pedido, a partir de um @RequestParam contendo o [ID] do registro pedido e o [ID] do registro Lanche...")
    @PutMapping(path = "/adicionaItem/{pedidoId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> adicionarItem(@PathVariable("pedidoId") Long pedidoId, @RequestParam("lancheId") Long lancheId, @RequestParam("qtde") BigDecimal qtde) {
        try {
            return new ResponseEntity<>(geradorPedidoLancheService.adicionarItemLanchePedido(FilterPedido.builder()
                    .idLanche(lancheId)
                    .idPedido(pedidoId)
                    .qtde(qtde)
                    .build()), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar pedido(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Responsável por adicionar um novo item ao Pedido, a partir de um @RequestParam contendo o [ID] do registro pedido e o [ID] do registro Lanche...")
    @PutMapping(path = "/adicionaPorcaoItem/{pedidoId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> adicionarPorcaoAdicionalItem(@PathVariable("pedidoId") Long pedidoId, @RequestParam("lancheId") Long lancheId,
                                                          @RequestParam("ingredienteId") Long ingredienteId,
                                                          @RequestParam("qtde") BigDecimal qtde) {
        try {
            return new ResponseEntity<>(geradorPedidoLancheService.adicionarIngredienteAdicionaAItemPedido(FilterPedido.builder()
                    .idLanche(lancheId)
                    .idPedido(pedidoId)
                    .idIngrediente(ingredienteId)
                    .qtde(qtde)
                    .build()), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar pedido(s): " + ex.getMessage());
        }
    }

    @ApiOperation(value = "Retorna todos os pedidos de lanches, a partir do filtro <b>valor</b> que estejam igual ou maior ao passado como parâmetro ...")
    @GetMapping(path = "/buscarPorValorIgualOuMaior", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorValorMaiorOuIgual(@RequestParam("valor") BigDecimal valor) {
        try {
            return getResponseDefault(pedidoReportService.listarPorValorMaiorOuIgualQue(valor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os pedidos de lanches, a partir do filtro <b>valor</b> que estejam menor ou maior ao passado como parâmetro ...")
    @GetMapping(path = "/buscarPorValorIgualOuMenor", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorValorMenorOuIgual(@RequestParam("valor") BigDecimal valor) {
        try {
            return getResponseDefault(pedidoReportService.listarPorValorMenorOuIgualQue(valor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os pedidos de existentes, a partir do filtro <b>data</b> aos quais foram gravadas no banco de dados. Com o formato da data (dd/MM/yyyy).")
    @GetMapping(path = "/buscarPorDataCadastro", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorDataCadastro(@RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data) {
        try {
            return getResponseDefault(pedidoReportService.listarPorDataInsercao(data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os pedidos de lanches existentes, a partir do filtro contedo o nome do cliente.")
    @GetMapping(path = "/buscarPorNomeCliente", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorNomeCliente(@RequestParam("nomeCliente") String nomeCliente) {
        try {
            return getResponseDefault(pedidoReportService.listarPorNomeCliente(nomeCliente));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os pedidos de lanches existentes paginado(s), a partir do filtro contedo o nome do cliente.")
    @GetMapping
    public ResponseEntity<?> listAllPorNomeClientePaginavel(@PathParam("nomeCliente") String nomeCliente, Pageable pageable) {
        try {
            if (Objects.isNull(nomeCliente))
                return getResponseDefault(pedidoReportService.listarTodos(pageable));
            return getResponseDefault(pedidoReportService.listarPorNomeCliente(nomeCliente, pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Retorna todos os lanches existentes, a partir dos filtro(s) <b>Data Inicial</b> e <b>Data Final</b> aos quais foram gravadas no banco de dados. Com o formato da data (dd/MM/yyyy).")
    @GetMapping(path = "/buscarPorPeriodo", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> listAllPorPeriodo(@RequestParam("dataInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
                                               @RequestParam("dataFim") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim) {
        try {
            return getResponseDefault(pedidoReportService.listarPorPeriodo(dataInicio, dataFim));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).build();
        }
    }

    private ResponseEntity<?> getResponseDefault(PedidosWrapper pedidosWrapper) {
        if (Objects.isNull(pedidosWrapper) || (pedidosWrapper.getPedidos().isEmpty()) ||
                pedidosWrapper.getPedidos().size() == 1 && Objects.isNull(pedidosWrapper.getPedidos().get(0)))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pedidosWrapper);
    }

    private ResponseEntity<?> retornarListaPedidos() {
        try {
            return getResponseDefault(pedidoReportService.listarTodos());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao executar a busca de dados: " + ex.getMessage());
        }
    }
}
