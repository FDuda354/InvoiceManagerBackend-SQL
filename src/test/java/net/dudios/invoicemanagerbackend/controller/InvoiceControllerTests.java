package net.dudios.invoicemanagerbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudios.invoicemanagerbackend.invoice.Invoice;
import net.dudios.invoicemanagerbackend.invoice.InvoiceController;
import net.dudios.invoicemanagerbackend.invoice.InvoiceService;
import net.dudios.invoicemanagerbackend.invoice.Payload;
import net.dudios.invoicemanagerbackend.user.AppUser;
import net.dudios.invoicemanagerbackend.user.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;

    @MockBean
    private InvoiceService invoiceService;


    @Autowired
    public InvoiceControllerTests(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    @Test
    public void shouldAddInvoice() throws Exception {
        //Given
        var user = AppUser.builder().id(10L).username("filip")
                .password("1234").email("filipduda99@wp.pl").roles(Role.ROLE_ADMIN).build();
        var invoice = Invoice.builder().id(1L).invoiceDate("2011-01-01").invoiceNumber("1")
                        .companyName("comqdsafdsapany").companyAddress("address").companyNIP("112414567890")
                        .invoiceDescription("descripdsaation").title("title").priceNetto(BigDecimal.valueOf(3022))
                        .priceBrutto(BigDecimal.valueOf(302)).paid(false).appUser(user).build();
        var payload = new Payload(invoice,10L);

        given(invoiceService.addInvoice(any(Payload.class))).willReturn(payload);

        //When
        var result = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.post("/invoice")
                        .content(mapper.writeValueAsString(payload))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(), Payload.class);

        //Then
        assertNotNull(result);
        assertEquals(payload.userId(), result.userId());
        assertEquals(payload.invoice().getInvoiceDate(), result.invoice().getInvoiceDate());
        assertEquals(payload.invoice().getInvoiceNumber(), result.invoice().getInvoiceNumber());
        assertEquals(payload.invoice().getCompanyName(), result.invoice().getCompanyName());
        assertEquals(payload.invoice().getCompanyAddress(), result.invoice().getCompanyAddress());
        assertEquals(payload.invoice().getCompanyNIP(), result.invoice().getCompanyNIP());
        assertEquals(payload.invoice().getInvoiceDescription(), result.invoice().getInvoiceDescription());
        assertEquals(payload.invoice().getTitle(), result.invoice().getTitle());
        assertEquals(payload.invoice().getPriceNetto(), result.invoice().getPriceNetto());
        assertEquals(payload.invoice().getPriceBrutto(), result.invoice().getPriceBrutto());
        assertEquals(payload.invoice().isPaid(), result.invoice().isPaid());
    }

    @Test
    public void shouldGetAllInvoices() throws Exception {
        //Given
        var user = AppUser.builder().id(10L).username("filip")
                .password("1234").email("filipduda99@wp.pl").roles(Role.ROLE_ADMIN).build();

        var invoiceList = List.of(
                        Invoice.builder().id(1L).invoiceDate("2011-01-01").invoiceNumber("1")
                        .companyName("hercu").companyAddress("address").companyNIP("112414567890")
                        .invoiceDescription("service").title("title").priceNetto(BigDecimal.valueOf(3022))
                        .priceBrutto(BigDecimal.valueOf(302)).paid(false).build(),
                Invoice.builder().id(2L).invoiceDate("2011-01-01").invoiceNumber("1")
                        .companyName("virtusPro").companyAddress("address").companyNIP("112414567890")
                        .invoiceDescription("machine").title("title").priceNetto(BigDecimal.valueOf(3022))
                        .priceBrutto(BigDecimal.valueOf(302)).paid(false).build(),
                Invoice.builder().id(3L).invoiceDate("2011-01-01").invoiceNumber("1")
                        .companyName("SKTt1").companyAddress("address").companyNIP("112414567890")
                        .invoiceDescription("coaching").title("title").priceNetto(BigDecimal.valueOf(3022))
                        .priceBrutto(BigDecimal.valueOf(302)).paid(false).build());


        user.setInvoices(Set.of(invoiceList.get(0), invoiceList.get(1), invoiceList.get(2)));

        given(invoiceService.getAllInvoices(10L)).willReturn(Set.of(invoiceList.get(0), invoiceList.get(1), invoiceList.get(2)));

        //When
        var result = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/invoice/all")
                        .param("userId", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), Invoice[].class);

        //Then
        assertNotNull(result);
        assertEquals(invoiceList.size(), result.length);
        assertEquals(invoiceList.get(0).getInvoiceDate(), result[0].getInvoiceDate());
        assertEquals(invoiceList.get(1).getInvoiceNumber(), result[1].getInvoiceNumber());
        assertEquals(invoiceList.get(2).isPaid(), result[2].isPaid());
        assertEquals(invoiceList.get(0).getTitle(), result[0].getTitle());
        assertEquals(invoiceList.get(1).getPriceNetto(), result[1].getPriceNetto());
        assertEquals(invoiceList.get(2).getPriceBrutto(), result[2].getPriceBrutto());
    }

    @Test
    public void shouldDeleteInvoice() throws Exception {
        //Given
        var user = AppUser.builder().id(1L).username("filip")
                .password("1234").email("filipduda99@wp.pl").roles(Role.ROLE_ADMIN).build();
        var invoice = Invoice.builder().id(2L).invoiceDate("2011-01-01").invoiceNumber("1")
                .companyName("comqdsafdsapany").companyAddress("address").companyNIP("112414567890")
                .invoiceDescription("descripdsaation").title("title").priceNetto(BigDecimal.valueOf(3022))
                .priceBrutto(BigDecimal.valueOf(302)).paid(false).appUser(user).build();

        //When
        var result = mockMvc.perform(MockMvcRequestBuilders.delete("/invoice")
                        .param("invoiceId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();
        //Then
        assertNotNull(result);


    }

    @Test
    public void shouldGetInvoiceById() throws Exception {
        //Given
        var user = AppUser.builder().id(1L).username("filip")
                .password("1234").email("filipduda99@wp.pl").roles(Role.ROLE_ADMIN).build();
        var invoice = Invoice.builder().id(2L).invoiceDate("2011-01-01").invoiceNumber("1")
                .companyName("comqdsafdsapany").companyAddress("address").companyNIP("112414567890")
                .invoiceDescription("descripdsaation").title("title").priceNetto(BigDecimal.valueOf(3022))
                .priceBrutto(BigDecimal.valueOf(302)).paid(false).appUser(user).build();

        given(invoiceService.getInvoice(any(Long.class))).willReturn(invoice);

        //When
        var result = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/invoice")
                        .param("invoiceId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), Invoice.class);

        //Then
        assertNotNull(result);
        assertEquals(invoice.getId(), result.getId());
        assertEquals(invoice.getInvoiceDate(), result.getInvoiceDate());
        assertEquals(invoice.getInvoiceNumber(), result.getInvoiceNumber());
        assertEquals(invoice.isPaid(), result.isPaid());
        assertEquals(invoice.getTitle(), result.getTitle());
        assertEquals(invoice.getPriceNetto(), result.getPriceNetto());
        assertEquals(invoice.getPriceBrutto(), result.getPriceBrutto());

    }
}
