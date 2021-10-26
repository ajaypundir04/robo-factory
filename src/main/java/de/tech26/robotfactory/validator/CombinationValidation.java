package de.tech26.robotfactory.validator;

import de.tech26.robotfactory.entity.Type;
import de.tech26.robotfactory.exception.ConfigurationException;
import de.tech26.robotfactory.exception.UnParsableConfigurationException;
import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CombinationValidation implements ConfigurationRequestValidation {

    private final StockService stockService;

    @Autowired
    public CombinationValidation(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Validate Configuration for the possible combination.
     *
     * @param configuration @{@link Configuration} to be validated
     * @throws ConfigurationException if validation fails
     */
    @Override
    public void validate(Configuration configuration) throws ConfigurationException {
        List<Type> configTypes = configuration.getComponents()
                .stream()
                .map(e -> stockService.getPartByCode(e).getType())
                .collect(Collectors.toList());
        List<Type> missingTypes = stockService.getRequiredCombinations().stream()
                .filter(t -> !configTypes.contains(t))
                .collect(Collectors.toList());
        if (missingTypes.size() > 0) {
            throw new UnParsableConfigurationException(String.format("Missing Combinations %s",
                    missingTypes.stream().map(Object::toString)
                            .collect(Collectors.joining(", "))));
        }
    }

}
