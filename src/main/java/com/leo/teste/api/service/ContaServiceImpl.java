package com.leo.teste.api.service;

import com.leo.teste.api.domain.Conta;
import com.leo.teste.api.dto.ContaDTO;
import com.leo.teste.api.enums.SituacaoPagamentoEnum;
import com.leo.teste.api.exception.ContaNotFoundException;
import com.leo.teste.api.exception.UnexpectedSituationException;
import com.leo.teste.api.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContaServiceImpl implements ContaService{

    private ContaRepository contaRepository;
    @Override
    @Transactional
    public ContaDTO createConta(ContaDTO contaDTO) {
        Conta contaToSave = Conta.builder()
                .dataVencimento(contaDTO.getDataVencimento())
                .dataPagamento(contaDTO.getDataPagamento())
                .valor(contaDTO.getValor())
                .descricao(contaDTO.getDescricao())
                .situacao(SituacaoPagamentoEnum.PENDENTE_DE_PAGAMENTO.getDescricao())
                .build();

        Conta retorno = contaRepository.save(contaToSave);
        return new ContaDTO(retorno.getId(),retorno.getDataVencimento(),retorno.getDataPagamento(),retorno.getValor(),retorno.getDescricao(),retorno.getSituacao());
    }

    @Override
    public ContaDTO getContaById(Long contaId) {
        Optional<Conta> optionalConta = contaRepository.findById(contaId);
        if(!optionalConta.isPresent()) {
            throw new ContaNotFoundException();
        }

        return new ContaDTO(
                optionalConta.get().getId(),
                optionalConta.get().getDataVencimento(),
                optionalConta.get().getDataPagamento(),
                optionalConta.get().getValor(),
                optionalConta.get().getDescricao(),
                optionalConta.get().getSituacao()
        );
    }

    @Override
    public Page getAllContas(int pageNumber, int qtdElementos) {
        Pageable paginador = PageRequest.of(pageNumber, qtdElementos);
        return contaRepository.findAll(paginador);
    }

    @Override
    @Transactional
    public ContaDTO updateConta(ContaDTO dto) {
        Optional<Conta> optionalConta = contaRepository.findById(dto.getId());
        if(!optionalConta.isPresent()) {
            throw new ContaNotFoundException();
        }
        validarSituacao(dto);

        Conta existingConta = optionalConta.get();
        existingConta.setDescricao(dto.getDescricao());
        existingConta.setValor(dto.getValor());
        existingConta.setDataPagamento(dto.getDataPagamento());
        existingConta.setDataVencimento(dto.getDataVencimento());
        existingConta.setSituacao(dto.getSituacao());

        Conta contaSalva = contaRepository.save(existingConta);
        return new ContaDTO(
                contaSalva.getId(),
                contaSalva.getDataVencimento(),
                contaSalva.getDataPagamento(),
                contaSalva.getValor(),
                contaSalva.getDescricao(),
                contaSalva.getSituacao()
        );
    }

    private void validarSituacao(ContaDTO conta) {
        String situacoesValidas = Arrays.stream(SituacaoPagamentoEnum.values()).toList().stream().map(SituacaoPagamentoEnum ::getDescricao).collect(Collectors.toList()).toString();
        if(!situacoesValidas.contains(conta.getSituacao())){
            throw new UnexpectedSituationException(situacoesValidas);
        }
    }

    @Override
    @Transactional
    public void deleteConta(Long contaId) {
        contaRepository.deleteById(contaId);
    }
}

