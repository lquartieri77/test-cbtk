package com.leo.teste.api.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class ContaDTO implements Serializable {

    private Long id;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String descricao;
    private String situacao;

    public ContaDTO(
            Long id,
            LocalDate dataVencimento,
            LocalDate dataPagamento,
            BigDecimal valor,
            String descricao,
            String situacao
    ){
        this.setId(id);
        this.setDataPagamento(dataPagamento);
        this.setDataVencimento(dataVencimento);
        this.setValor(valor);
        this.setDescricao(descricao);
        this.setSituacao(situacao);

    }



}
