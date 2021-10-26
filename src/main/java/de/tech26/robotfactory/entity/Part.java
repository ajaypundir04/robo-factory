package de.tech26.robotfactory.entity;

import java.util.Objects;

/**
 * @author Ajay Singh Pundir
 * It handles the part details.
 */
public class Part {

    private String name;

    private double price;

    private String code;

    private Type type;

    public Part(String name, double price, String code, Type type) {
        this.name = name;
        this.price = price;
        this.code = code;
        this.type = type;
    }

    public Part() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Objects.equals(code, part.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Part{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", code=" + code +
                ", type=" + type +
                '}';
    }
}
