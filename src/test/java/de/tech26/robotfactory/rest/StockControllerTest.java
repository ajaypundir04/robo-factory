package de.tech26.robotfactory.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tech26.robotfactory.entity.Type;
import de.tech26.robotfactory.service.StockService;
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

import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = StockController.class)
@ContextConfiguration(classes = {StockController.class,
        GlobalExceptionHandler.class})
public class StockControllerTest {

    private static final String STOCK_URL = "/stock/";
    private static final String TYPE_URL = "/combinations/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private StockService stockService;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }


    @Test
    public void testGetStock() throws Exception {
        Mockito.when(stockService.getStock()).thenReturn(TestUtil.createStock());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(STOCK_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Map actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
        Assertions.assertEquals(actual, TestUtil.createStock());

    }

    @Test
    public void testPutCombination() throws Exception {
        Type arms = new Type("Arms");
        Mockito.when(stockService.modifyCombinations(arms, false)).thenReturn(TestUtil.getRequiredTypes());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(TYPE_URL)
                        .content(objectMapper.writeValueAsString(arms)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class,
                Class.forName(TestUtil.getRequiredTypes().get(0).getClass().getName()));
        List<Type> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), type);
        Assertions.assertEquals(TestUtil.getRequiredTypes(), actual);

    }


    @Profile("embedded")
    @SpringBootApplication
    public static class Config {

    }

}
