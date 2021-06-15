package com.voucherExcel.unitTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voucherExcel.controller.VoucherController;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.repository.FiltroVoucherRepository;
import com.voucherExcel.repository.VoucherRepository;
import com.voucherExcel.services.VoucherService;


@RunWith(SpringRunner.class)
@WebMvcTest(VoucherController.class)
class VoucherControllerTest {

	@MockBean
	Voucher voucher;
	
	@MockBean
	private VoucherService voucherServiceMock;
	
	@MockBean
	private FiltroVoucherRepository filtroVoucherRepository;
	
	@MockBean
	VoucherRepository voucherRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	VoucherController voucherController = new VoucherController();
	
	private SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yyyy");
	
	
	@BeforeEach
	void setUp() throws Exception {
		
		Optional<Voucher> mockVoucher1 = Optional.of(voucher1());
		Optional<Voucher> mockVoucher1ND = Optional.of(voucher1ND());
		Optional<Voucher> mockVoucher1E = Optional.of(voucher1E());
		Optional<Voucher> mockVoucher2 = Optional.of(voucher2());
		Optional<Voucher> mockVoucher5 = Optional.of(voucher5());
		
		//Mockito.when(voucherRepositoryMock.findByCodigoVoucherAndDni("138569", "27890663")).thenReturn(mockVoucher1);
		//Controlador estadoEliminarVoucher
		Mockito.when(voucherRepository.findById("222")).thenReturn(mockVoucher2);
		
		//Extender vigencia
		Mockito.when(voucherRepository.findById("555")).thenReturn(mockVoucher5);
		
		//Estado No Dipsonible
		Mockito.when(voucherRepository.findById("111")).thenReturn(mockVoucher1);
		
		//Duplicado de voucher
		Mockito.when(voucherRepository.findById("111")).thenReturn(mockVoucher1ND);
		Mockito.when(voucherRepository.findById("000")).thenReturn(mockVoucher1E);
		
		//get ppor codigo de voucher
		Mockito.when(voucherRepository.findByCodigoVoucher("138592")).thenReturn(voucher2());
		
	}
	
	@Test
	public void estadoEliminarVoucher() throws Exception {
	
		//Cambiar de estado un voucher E a EL
		Voucher voucherIng = voucher2();
		Voucher voucherResp;
		
		Mockito.when(voucherServiceMock.estadoEliminarVoucher(voucherIng)).thenReturn(voucher4());
		voucherResp = voucherController.estadoEliminarVoucher(voucherIng);
		Assertions.assertEquals(voucher4().getEstado(), voucherResp.getEstado());
		
//		mockMvc.perform(put("/api/voucher/eliminar")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(asJsonString(voucherIng)));
	}
	
	
	@Test
	public void estadoExtenderVigencia() throws Exception {
	 //Un voucher que ya esta en estado vencido, extender vigencia
		Voucher voucherIng = voucher5();
		Voucher voucherResp;
		
		Mockito.when(voucherServiceMock.estadoExtenderVigencia(voucherIng)).thenReturn(voucher5E());
		voucherResp = voucherController.estadoExtenderVigencia(voucherIng);
		Assertions.assertEquals(voucher5E().getEstado(), voucherResp.getEstado());

	}
	
	@Test
	public void estadoNoDisponible() throws Exception {
	 //Un voucher que ya esta en estado utilizado, pasa a estado no-disponible
		Voucher voucherIng = voucher1();
		Voucher voucherResp;
		
		Mockito.when(voucherServiceMock.estadoNoDisponible(voucherIng)).thenReturn(voucher1ND());
		voucherResp = voucherController.estadoNoDisponible(voucherIng);
		Assertions.assertEquals(voucher1ND().getEstado(), voucherResp.getEstado());

	}
	
	@Test
	public void duplicadoVoucher() throws Exception {
	 //Un voucher que ya esta en estado no-disponible
		Voucher voucherIng = voucher1ND();
		Voucher voucherIngDup = voucher1NDDupl();
		Voucher voucherDup = voucher1E();
		Voucher voucherResp;
		
		Mockito.when(voucherServiceMock.voucherDupliAsociado(voucherIng, voucherDup)).thenReturn(voucherIngDup);
		Mockito.when(voucherServiceMock.duplicadoVoucher(voucherIng)).thenReturn(voucher1E());
		
		
		System.out.println(voucherServiceMock.duplicadoVoucher(voucherIng));
		System.out.println(voucherServiceMock.voucherDupliAsociado(voucherIng, voucherDup).getIdCopia());
		
		//voucherResp = voucherController.duplicadoVoucher(voucherIng);
		
		mockMvc.perform(put("/api/voucher/duplicado")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(voucherIng)));
		
		voucherResp = voucherController.duplicadoVoucher(voucherIng);
		System.out.println(voucherResp);
	//	Assertions.assertEquals(voucher1E().get_id(), voucherResp.getIdCopia());
		
	}
	
	@Test
	public void getVouchersPorCodigo() throws Exception {
	 //Get de voucher por codigo del voucher
		Voucher voucherResp;
			
		Mockito.when(voucherServiceMock.getCodigoVoucher("138592")).thenReturn(voucher2());
		voucherResp = voucherController.getVouchersPorCodigo("138592");
		Assertions.assertEquals(voucher2().get_id(), voucherResp.get_id());

	}
	
	@Test
	public void getVouchersPorId() throws Exception {
	 //Get de Voucher por Id
		Optional<Voucher> voucher2 = Optional.of(voucher2());
		Optional<Voucher>  voucherResp;
			
		Mockito.when(voucherServiceMock.getVoucher("222")).thenReturn(voucher2);
		voucherResp = voucherController.getVouchersPorId("222");
		Assertions.assertEquals(voucher2().get_id(), voucherResp.get().get_id());

	}

	
	private Voucher voucher1() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("111");
		mockVoucher.setCodigoVoucher("138569");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/09/24"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("U");
		mockVoucher.setDni("27890663");
		mockVoucher.setFacturaAsociada("1111112222");

		return mockVoucher;
	}
	
	private Voucher voucher1ND() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("111");
		mockVoucher.setCodigoVoucher("138569");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/09/24"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("ND");
		mockVoucher.setDni("27890663");
		mockVoucher.setFacturaAsociada("1111112222");

		return mockVoucher;
	}
	
	private Voucher voucher1NDDupl() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("111");
		mockVoucher.setCodigoVoucher("138569");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/09/24"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("ND");
		mockVoucher.setDni("27890663");
		mockVoucher.setFacturaAsociada("1111112222");
		mockVoucher.setIdCopia("000");

		return mockVoucher;
	}
	private Voucher voucher1E() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("000");
		mockVoucher.setCodigoVoucher("138569-D");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/09/24"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("E");
		mockVoucher.setDni("27890663");
		mockVoucher.setFacturaAsociada(null);

		return mockVoucher;
	}
	
	private Voucher voucher2() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("222");
		mockVoucher.setCodigoVoucher("138592");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/06/07"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("E");
		mockVoucher.setDni("18085996");

		return mockVoucher;
	}
	
	private Voucher voucher3() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("333");
		mockVoucher.setCodigoVoucher("135370");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/09/24"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("E");
		mockVoucher.setDni("27890663");

		return mockVoucher;
	}
	
	private Voucher voucher4() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("222");
		mockVoucher.setCodigoVoucher("138592");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/06/07"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("EL");
		mockVoucher.setDni("18085996");

		return mockVoucher;
	}
	
	private Voucher voucher5() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("555");
		mockVoucher.setCodigoVoucher("135311");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/05/24"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("V");
		mockVoucher.setDni("27890688");

		return mockVoucher;
	}
	
	private Voucher voucher5E() throws ParseException {
		Voucher mockVoucher = new Voucher();
		mockVoucher.set_id("555");
		mockVoucher.setCodigoVoucher("135311");
		mockVoucher.setFechaDesde(f2.parse("2020/08/25"));
		mockVoucher.setFechaHasta(f2.parse("2021/10/24"));
		mockVoucher.setHabilitado(true);
		mockVoucher.setEstado("E");
		mockVoucher.setDni("27890688");

		return mockVoucher;
	}
	
	private static String asJsonString(final Object obj) {
		try {
			return (new ObjectMapper()).writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
