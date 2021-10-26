package de.tech26.robotfactory.validator;

import de.tech26.robotfactory.exception.ConfigurationException;
import de.tech26.robotfactory.model.Configuration;

@FunctionalInterface
public interface ConfigurationRequestValidation {

    void validate(Configuration configuration) throws ConfigurationException;
}