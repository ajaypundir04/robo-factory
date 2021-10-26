package de.tech26.robotfactory.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tech26.robotfactory.entity.Part;
import de.tech26.robotfactory.exception.ConfigurationException;
import de.tech26.robotfactory.exception.RoboFactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ajay Singh Pundir
 * It handles the part persistance operations.
 */
@Component
public class PartRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartRepository.class);

    private final List<Part> parts;
    private final Map<String, Part> partMap;

    public PartRepository() {
        parts = new CopyOnWriteArrayList<>();
        partMap = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void initializeParts() {
        try {
            parts.addAll(new ObjectMapper()
                    .readValue(new File("src/main/resources/parts.json"),
                            new TypeReference<>() {
                            }));
            partMap.putAll(parts.stream().collect(Collectors.toMap(Part::getCode,
                    Function.identity())));

        } catch (IOException e) {
            LOGGER.error("Unable to load parts due to", e);
            throw new RoboFactoryException("Unable to load parts due to", e);
        }
    }


    public void add(Part part) {
        parts.add(part);
        partMap.put(part.getCode(), part);

    }

    public void remove(Part part) {
        parts.remove(part);
        partMap.remove(part);

    }

    public Part getByCode(String code) {
        if (partMap.containsKey(code))
            return partMap.get(code);
        else throw new ConfigurationException(String.format("Invalid Code %s", code));
    }
}
