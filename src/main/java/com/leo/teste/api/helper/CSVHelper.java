package com.leo.teste.api.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.leo.teste.api.domain.Conta;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "data_vencimento", "data_pagamento", "valor", "descricao", "situacao" };

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Conta> csvToContas(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<Conta> contas = new ArrayList<Conta>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Conta contaLoop = Conta.builder()
                        .dataVencimento(LocalDate.parse(csvRecord.get("data_vencimento")))
                        .dataPagamento(LocalDate.parse(csvRecord.get("data_pagamento")))
                        .valor(new BigDecimal(csvRecord.get("valor")))
                        .descricao(csvRecord.get("descricao"))
                        .situacao(csvRecord.get("situacao"))
                        .build();

                contas.add(contaLoop);
            }

            return contas;
        } catch (IOException e) {
            throw new RuntimeException("Ops... fail to parse CSV file: " + e.getMessage());
        }
    }
}
