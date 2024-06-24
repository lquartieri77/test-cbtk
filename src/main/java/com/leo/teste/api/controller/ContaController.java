package com.leo.teste.api.controller;

import com.leo.teste.api.domain.Conta;
import com.leo.teste.api.dto.ContaDTO;
import com.leo.teste.api.helper.CSVHelper;
import com.leo.teste.api.service.CSVService;
import com.leo.teste.api.service.ContaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/conta")
@CrossOrigin
public class ContaController {

    Logger logger = LoggerFactory.getLogger(ContaController.class);
    @Autowired
    private ContaService contaService;

    @Autowired
    CSVService csvService;

    @PostMapping
    public ResponseEntity<ContaDTO> createConta(@RequestBody ContaDTO conta){
        logger.info("Post - metodo createConta");
        logger.debug("DTO = "+ conta.toString());

        ContaDTO savedUser = contaService.createConta(conta);

        logger.debug("Salvo com sucesso");

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ContaDTO> getContaById(@PathVariable("id") Long contaId){
        logger.info("Get - metodo getContaById");
        logger.debug("ID = "+ contaId);

        ContaDTO conta = contaService.getContaById(contaId);
        return new ResponseEntity<>(conta, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllContas(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "5") Integer qtdElementos){
        logger.info("Get - metodo getAllContas");

        Page<Conta> retornoPaginado = contaService.getAllContas(pageNumber,qtdElementos);
        List<Conta> contas = retornoPaginado.getContent();
        Map<String, Object> response = new HashMap<>();

        response.put("contas", contas);
        response.put("currentPage", retornoPaginado.getNumber());
        response.put("totalItems", retornoPaginado.getTotalElements());
        response.put("totalPages", retornoPaginado.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ContaDTO> updateConta(@PathVariable("id") Long contaId, @RequestBody ContaDTO conta){
        logger.info("Put - metodo updateConta");
        logger.debug("ID = "+ contaId + " - DTO " + conta.toString());

        conta.setId(contaId);
        ContaDTO updatedConta = contaService.updateConta(conta);

        logger.debug("Salvo com sucesso");

        return new ResponseEntity<>(updatedConta, HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteConta(@PathVariable("id") Long contaId){
        logger.info("Delete - metodo deleteConta");
        logger.debug("ID = "+ contaId);

        contaService.deleteConta(contaId);

        logger.debug("Excluido com sucesso");

        return new ResponseEntity<>("Conta excluida com sucesso!", HttpStatus.OK);
    }

    @PutMapping("/pagamento/{id}")
    public ResponseEntity<Map<String, Object>> pagarConta(@PathVariable("id") Long id){
        logger.info("Put - metodo pagarConta");
        logger.debug("ID = "+ id);

        ContaDTO conta = contaService.getContaById(id);
        conta.setSituacao("PAGAMENTO REALIZADO");
        conta.setDataPagamento(LocalDateTime.now().toLocalDate());
        ContaDTO updatedConta = contaService.updateConta(conta);
        Map<String, Object> response = new HashMap<>();
        response.put("conta", updatedConta);
        response.put("situacao", "Pagamento realizado com sucesso!");
        logger.debug("Paga com sucesso");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("Post - metodo uploadFile");

        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                csvService.save(file);
                message = "Uploaded realizado com sucesso: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } catch (Exception e) {
                message = "Nao foi possivel realizar o upload do arquivo: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }

        message = "Por favor, faça o upload de um arquivo valido!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
