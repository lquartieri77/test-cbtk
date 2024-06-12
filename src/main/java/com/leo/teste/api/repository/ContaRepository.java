package com.leo.teste.api.repository;

import com.leo.teste.api.domain.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long>{

    Page findAll(Pageable pageable);
}
