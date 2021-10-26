package de.tech26.robotfactory.validator;

import de.tech26.robotfactory.exception.ConfigurationException;
import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OutOfStockValidation implements ConfigurationRequestValidation {

    private final StockService stockService;

    @Autowired
    public OutOfStockValidation(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Validate Configuration for the out of Stock .
     *
     * @param configuration @{@link Configuration} to be validated
     * @throws ConfigurationException if validation fails
     */
    @Override
    public void validate(Configuration configuration) throws ConfigurationException {

        Map<String, Integer> notInStock = configuration.getComponents()
                .stream()
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.summingInt(e -> 1))).entrySet()
                .stream()
                .filter(e -> stockService.getAvailableQuantities(e.getKey()) < e.getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (notInStock.size() > 0) {
            throw new ConfigurationException(String.format("Quantity Not Available for %s", notInStock));
        }
    }
}
