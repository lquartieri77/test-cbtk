package com.leo.teste.api.service;

import com.leo.teste.api.domain.Conta;
import com.leo.teste.api.dto.ContaDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContaService {
    ContaDTO createConta(ContaDTO conta);

    ContaDTO getContaById(Long contaId);

    Page getAllContas(int pageNumber, int qtdElementos);

    ContaDTO updateConta(ContaDTO conta);

    void deleteConta(Long contaId);
}
