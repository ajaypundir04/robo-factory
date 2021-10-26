package de.tech26.robotfactory.validators;

import de.tech26.robotfactory.exception.UnParsableConfigurationException;
import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.service.StockService;
import de.tech26.robotfactory.util.TestUtil;
import de.tech26.robotfactory.validator.CombinationValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class CombinationValidationTest {
    private final StockService stockService = Mockito.mock(StockService.class);
    private final CombinationValidation combinationValidation = new CombinationValidation(stockService);

    @BeforeEach
    public void setUp() {
        Mockito.reset(stockService);
    }

    @Test
    public void testValidate() {
        Mockito.when(stockService.getPartByCode("A")).thenReturn(TestUtil.parts.get("A"));
        Mockito.when(stockService.getPartByCode("I")).thenReturn(TestUtil.parts.get("I"));
        Mockito.when(stockService.getPartByCode("D")).thenReturn(TestUtil.parts.get("D"));
        Mockito.when(stockService.getPartByCode("F")).thenReturn(TestUtil.parts.get("F"));
        Mockito.when(stockService.getRequiredCombinations())
                .thenReturn(TestUtil.getRequiredTypes());
        combinationValidation.validate(TestUtil.createConfiguration());
        Mockito.verify(stockService, Mockito.times(1))
                .getRequiredCombinations();
    }

    @Test
    public void testValidateThrowsException() {
        Mockito.when(stockService.getRequiredCombinations())
                .thenReturn(TestUtil.getRequiredTypes());
        Mockito.when(stockService.getPartByCode("A")).thenReturn(TestUtil.parts.get("A"));
        Configuration c = new Configuration();
        c.setComponents(List.of("A"));
        Assertions.assertThrows(UnParsableConfigurationException.class,
                () -> combinationValidation.validate(c));
        Mockito.verify(stockService, Mockito.times(1))
                .getRequiredCombinations();
    }
}
