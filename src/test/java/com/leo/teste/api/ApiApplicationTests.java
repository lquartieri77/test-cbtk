package com.leo.teste.api;

import com.leo.teste.api.domain.Conta;
import com.leo.teste.api.dto.ContaDTO;
import com.leo.teste.api.enums.SituacaoPagamentoEnum;
import com.leo.teste.api.exception.ContaNotFoundException;
import com.leo.teste.api.repository.ContaRepository;
import com.leo.teste.api.service.CSVService;
import com.leo.teste.api.service.ContaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ApiApplicationTests {

	@InjectMocks
	private ContaServiceImpl contaService;

	@InjectMocks
	private CSVService csvService;

	@Mock
	private ContaRepository contaRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void testCreateConta() {
		ContaDTO contaDTO = new ContaDTO(null,null,null,BigDecimal.valueOf(67.35),"Conta de água","PENDENTE DE PAGAMENTO");
		when(contaRepository.save(any(Conta.class))).thenReturn(new Conta());
		ContaDTO result = contaService.createConta(contaDTO);
		assertThat(result).isNotNull();
	}

	@Test
	public void testGetContaById() {
		when(contaRepository.findById(anyLong())).thenReturn(Optional.of(new Conta()));
		ContaDTO result = contaService.getContaById(1L);
		assertThat(result).isNotNull();
	}

	@Test
	public void testGetAllContas() {
		int pageNumber = 0;
		int qtdElementos = 1;
		Conta c1 = contaRepository.getReferenceById(1L);
		List<Conta> lista = new ArrayList<>();
		lista.add(c1);
		Page<Conta> contasPaginadas = new PageImpl<>(lista);
		when(contaRepository.findAll(PageRequest.of(pageNumber, qtdElementos))).thenReturn(contasPaginadas);
		Page<Conta> result = contaService.getAllContas(pageNumber, qtdElementos);
		assertThat(result).isEqualTo(contasPaginadas);
	}

	@Test
	public void testDeleteConta() {
		Long contaId = 1L;
		doNothing().when(contaRepository).deleteById(contaId);
		contaService.deleteConta(contaId);
		verify(contaRepository).deleteById(contaId);
	}

	@Test
	public void testUpdateConta() {
		// Arrange
		Long contaId = 1L;
		ContaDTO request = new ContaDTO(contaId,null,null,BigDecimal.valueOf(67.35),"Conta de água","PENDENTE DE PAGAMENTO");
		Conta originalConta = new Conta();
		originalConta.setId(contaId);
		when(contaRepository.findById(anyLong())).thenReturn(Optional.of(originalConta));
		when(contaRepository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));
		ContaDTO updatedConta = contaService.updateConta(request);
		assertThat(updatedConta).isNotNull();

	}

	@Test
	public void testUpdateConta_ContaNaoEncontrada() {
		Long idInexistente = 999L;
		ContaDTO request = new ContaDTO(idInexistente,null,null,BigDecimal.valueOf(67.35),"Conta de água","PENDENTE DE PAGAMENTO");
		when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ContaNotFoundException.class, () -> contaService.updateConta(request));
	}

	@Test
	public void testSave() throws IOException {
		MockMultipartFile file = new MockMultipartFile("carga_via_csv.csv", "content".getBytes());
		csvService.save(file);
		verify(contaRepository).saveAll(any(List.class));

	}

}
