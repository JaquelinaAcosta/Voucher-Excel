package com.voucherExcel.unitTest;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voucherExcel.controller.ExcelController;
import com.voucherExcel.model.Excel;
import com.voucherExcel.model.res.ExcelDisponible;
import com.voucherExcel.repository.ExcelRepository;
import com.voucherExcel.repository.FiltroExcelRepository;
import com.voucherExcel.repository.VoucherRepository;
import com.voucherExcel.services.ExcelService;
import com.voucherExcel.services.VoucherService;


@RunWith(SpringRunner.class)
@WebMvcTest(ExcelController.class)
class ExcelControllerTest {

	@MockBean
	Excel excel;
	
	@MockBean
	VoucherService voucherService;
	@MockBean
	ExcelService excelService;
	@MockBean
	private ExcelRepository excelRepository;
	@MockBean
	private VoucherRepository voucherRepository;

	@MockBean
	private FiltroExcelRepository filtroExcelRepository;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ExcelController excelController = new ExcelController();
	
	private SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yyyy");
	
	
	@BeforeEach
	void setUp() throws Exception {
		
		Optional<Excel> mockExcel1 = Optional.of(excel1());
		Optional<Excel> mockExcel2 = Optional.of(excel2());
		
		//Cabio de estado a diponible
		Mockito.when(excelRepository.findById("111")).thenReturn(mockExcel1);
		
		//Cambio de estado a Cancelado
		Mockito.when(excelRepository.findById("222")).thenReturn(mockExcel2);
		
		Mockito.when(excelRepository.findByEstado("IMPORTADO")).thenReturn(excelList());
		
		//get ppor codigo de voucher
		//Mockito.when(voucherRepository.findByCodigoVoucher("138592")).thenReturn(voucher2());
		
	}
	
	@Test
	public void updateExcel() throws Exception {
		//Cambiar de estado de un Excel de importado a disponible
		Excel excelIng = excel1();
		Excel excelResp;
		ExcelDisponible excelDisp = excelDisp();
		
		Mockito.when(excelService.updateExcel(excelDisp)).thenReturn(excel1D());
		
		excelResp = excelController.updateExcel(excelDisp);
		Assertions.assertEquals(excel1D().getEstado(), excelResp.getEstado());
		
	}
	
	@Test
	public void cancelarExcel() throws Exception {
		//Cambiar de estado de un Excel de importado a cancelado
		Excel excelIng = excel2();
		Excel excelResp;
		
		Mockito.when(excelService.cancelarExcel(excelIng)).thenReturn(excel2C());
		
		excelResp = excelController.cancelarExcel(excelIng);
		Assertions.assertEquals(excel2C().getEstado(), excelResp.getEstado());
		
	}

	@Test
	public void getExcelEstados() throws Exception {
		//Get por estados
		List<Excel> excelResp;
		
		Mockito.when(excelService.getExcelsEstado("IMPORTADO")).thenReturn(excelList());
		excelResp = excelController.getExcelEstados("IMPORTADO");
		Assertions.assertEquals(excelList().get(0).get_id(), excelResp.get(0).get_id());
		Assertions.assertEquals(excelList().get(1).get_id(), excelResp.get(1).get_id());
		
	}

	
	private Excel excel1() throws ParseException {
		Excel mockExcel = new Excel();
		mockExcel.set_id("111");
		mockExcel.setNombreExcel("Excel 1");
		mockExcel.setFecha(f2.parse("2021/06/10"));
		mockExcel.setCantidadRegistros(20);
		mockExcel.setEstado("IMPORTADO");

		return mockExcel;
	}
	
	private ExcelDisponible excelDisp() throws ParseException {
		ExcelDisponible ed = new ExcelDisponible();
		ed.setExcel(excel1());
		ed.setResponsable("Nombre User");
		return ed;	
	}
	
	private Excel excel1D() throws ParseException {
		Excel mockExcel = new Excel();
		mockExcel.set_id("111");
		mockExcel.setNombreExcel("Excel 1");
		mockExcel.setFecha(f2.parse("2021/06/10"));
		mockExcel.setCantidadRegistros(20);
		mockExcel.setEstado("DISPONIBLE");

		return mockExcel;
	}
	
	private Excel excel2() throws ParseException {
		Excel mockExcel = new Excel();
		mockExcel.set_id("222");
		mockExcel.setNombreExcel("Excel 2");
		mockExcel.setFecha(f2.parse("2021/06/10"));
		mockExcel.setCantidadRegistros(20);
		mockExcel.setEstado("IMPORTANDO");

		return mockExcel;
	}
	
	private Excel excel2C() throws ParseException {
		Excel mockExcel = new Excel();
		mockExcel.set_id("222");
		mockExcel.setNombreExcel("Excel 2");
		mockExcel.setFecha(f2.parse("2021/06/10"));
		mockExcel.setCantidadRegistros(20);
		mockExcel.setEstado("CANCELADO");

		return mockExcel;
	}
	
	private List<Excel> excelList() throws ParseException {
		List<Excel> mockExcel = new ArrayList<Excel>();
		
		mockExcel.add(excel1());
		mockExcel.add(excel2());

		return mockExcel;
	}
	

	private static String asJsonString(final Object obj) {
		try {
			return (new ObjectMapper()).writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
