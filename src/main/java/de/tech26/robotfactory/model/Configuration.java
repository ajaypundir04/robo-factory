package de.tech26.robotfactory.model;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

public class Configuration {

    @NotEmpty
    private List<String> components;

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "components=" + components +
                '}';
    }
}
