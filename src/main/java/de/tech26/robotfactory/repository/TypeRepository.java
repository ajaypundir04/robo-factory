package de.tech26.robotfactory.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tech26.robotfactory.entity.Type;
import de.tech26.robotfactory.exception.RoboFactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ajay Singh Pundir
 * It handles the type persistance operations.
 */
@Component
public class TypeRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeRepository.class);

    private final List<Type> types;
    private final List<Type> validCombinations;

    public TypeRepository() {
        types = new CopyOnWriteArrayList<>();
        validCombinations = new CopyOnWriteArrayList<>();
    }

    @PostConstruct
    public void initializeTypes() {
        try {
            types.addAll(new ObjectMapper()
                    .readValue(new File("src/main/resources/types.json"),
                            new TypeReference<>() {
                            }));
            validCombinations.addAll(types);

        } catch (IOException e) {
            LOGGER.error("Unable to load parts due to", e);
            throw new RoboFactoryException("Unable to load parts due to", e);
        }
    }

    public List<Type> getRequiredTypes() {
        return validCombinations;
    }

    public void addOrUpdateRequiredTypes(Type type, boolean remove) {

        if (!remove) {
            validCombinations.add(type);
            types.add(type);
        } else {
            validCombinations.remove(type);
            types.remove(type);
        }
    }
}
