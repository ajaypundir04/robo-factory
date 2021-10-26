package de.tech26.robotfactory.validators;

import de.tech26.robotfactory.exception.ConfigurationException;
import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.service.StockService;
import de.tech26.robotfactory.validator.OutOfStockValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class OutOfStockValidationTest {
    private final StockService stockService = Mockito.mock(StockService.class);
    private final OutOfStockValidation outOfStockValidation = new OutOfStockValidation(stockService);

    @BeforeEach
    public void setUp() {
        Mockito.reset(stockService);
    }

    @Test
    public void testValidate() {
        Configuration c = new Configuration();
        c.setComponents(List.of("A"));
        Mockito.when(stockService.getAvailableQuantities("A")).thenReturn(1);
        outOfStockValidation.validate(c);
        Mockito.verify(stockService, Mockito.times(1))
                .getAvailableQuantities(Mockito.anyString());
    }

    @Test
    public void testValidateThrowsException() {
        Configuration c = new Configuration();
        c.setComponents(List.of("A"));
        Mockito.when(stockService.getAvailableQuantities("A")).thenReturn(0);
        Assertions.assertThrows(ConfigurationException.class,
                () -> outOfStockValidation.validate(c));
        Mockito.verify(stockService, Mockito.times(1))
                .getAvailableQuantities(Mockito.anyString());
    }
}
