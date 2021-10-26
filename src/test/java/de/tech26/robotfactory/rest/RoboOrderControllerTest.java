package de.tech26.robotfactory.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tech26.robotfactory.entity.Order;
import de.tech26.robotfactory.exception.RoboOrderException;
import de.tech26.robotfactory.exception.UnParsableConfigurationException;
import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.model.Invoice;
import de.tech26.robotfactory.service.OrderService;
import de.tech26.robotfactory.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = RobotOrderController.class)
@ContextConfiguration(classes = {RobotOrderController.class,
        GlobalExceptionHandler.class})
public class RoboOrderControllerTest {

    private static final String ORDERS_URL = "/orders";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private OrderService orderService;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }


    @Test
    public void testPost() throws Exception {
        Mockito.when(orderService.placeOrder(Mockito.any(Configuration.class))).thenReturn(TestUtil.createOrder());
        Mockito.when(orderService.generateInvoice(Mockito.any(Order.class))).thenReturn(TestUtil.createInvoice());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(ORDERS_URL).content(objectMapper.writeValueAsString(TestUtil.createConfiguration()))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Invoice actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Invoice.class);
        Assertions.assertTrue(actual.equals(TestUtil.createInvoice()));

    }

    @Test
    public void testPostWithUnProcessableEntity() throws Exception {
        Mockito.when(orderService.placeOrder(Mockito.any(Configuration.class)))
                .thenThrow(new UnParsableConfigurationException("Invalid Configuration"));
        mockMvc.perform(MockMvcRequestBuilders
                        .post(ORDERS_URL).content(objectMapper.writeValueAsString(TestUtil.createConfiguration()))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andReturn();
        Mockito.verify(orderService, Mockito.times(0)).generateInvoice(Mockito.any(Order.class));

    }

    @Test
    public void testPostWithBadRequest() throws Exception {
        Mockito.when(orderService.placeOrder(Mockito.any(Configuration.class)))
                .thenThrow(new RoboOrderException("Invalid Configuration"));
        mockMvc.perform(MockMvcRequestBuilders
                        .post(ORDERS_URL).content(objectMapper.writeValueAsString(TestUtil.createConfiguration()))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        Mockito.verify(orderService, Mockito.times(0)).generateInvoice(Mockito.any(Order.class));

    }

    @Profile("embedded")
    @SpringBootApplication
    public static class Config {

    }

}
