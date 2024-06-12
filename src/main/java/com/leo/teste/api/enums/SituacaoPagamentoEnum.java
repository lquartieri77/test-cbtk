package com.leo.teste.api.enums;

public enum SituacaoPagamentoEnum {
    PAGAMENTO_REALIZADO("PAGAMENTO REALIZADO"),
    PENDENTE_DE_PAGAMENTO("PENDENTE DE PAGAMENTO"),
    EM_CONTESTACAO("PAGAMENTO EM CONTESTACAO");

    private final String descricao;
    SituacaoPagamentoEnum(String descr){
        descricao = descr;
    }

    public String getDescricao() {
        return descricao;
    }
}
