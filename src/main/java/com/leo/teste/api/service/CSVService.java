package com.leo.teste.api.service;

import com.leo.teste.api.domain.Conta;
import com.leo.teste.api.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import com.leo.teste.api.helper.CSVHelper;

@Service
public class CSVService {

    @Autowired
    private ContaRepository contaRepository;

    public void save(MultipartFile file) {
        try {
            List<Conta> tutorials = CSVHelper.csvToContas(file.getInputStream());
            contaRepository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("Ops... fail to store csv data: " + e.getMessage());
        }
    }

    public List<Conta> getAllTutorials() {
        return contaRepository.findAll();
    }

}
