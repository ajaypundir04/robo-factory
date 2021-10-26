package de.tech26.robotfactory.util;

import de.tech26.robotfactory.entity.Order;
import de.tech26.robotfactory.entity.Part;
import de.tech26.robotfactory.entity.Type;
import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.model.Invoice;

import java.util.List;
import java.util.Map;

public class TestUtil {

    public static Map<String, Part> parts = Map.of(
            "A", new Part("Humanoid Face", 10.28, "A", new Type("Face")),
            "I", new Part("Material Bioplastic", 90.12, "I", new Type("Material")),
            "D", new Part("Arms with Hands", 28.94, "D", new Type("Arms")),
            "F", new Part("Mobility with Wheels", 30.77, "F", new Type("Mobility"))
    );

    public static Map<String, Integer> stocks = Map.of(
            "A", 1,
            "I", 2,
            "D", 3,
            "F", 4
    );

    private TestUtil() {
    }

    public static Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setComponents(List.of("I", "A", "D", "F"));
        return configuration;
    }

    public static Invoice createInvoice() {
        return new Invoice("26061992", 16.11f);
    }

    public static Order createOrder() {
        return new Order("26061992", 16.11f);
    }

    public static Map<String, Integer> createStock() {
        return Map.of("A", 1, "B", 2);
    }

    public static List<Type> getRequiredTypes() {
        return List.of(new Type("Face"), new Type("Arms"), new Type("Mobility"), new Type("Material"));
    }
}
