package de.tech26.robotfactory.entity;

import java.util.Objects;

/**
 * @author Ajay Singh Pundir
 * It handles the stock details.
 */
public class Stock {
    private String code;
    private int quantity;

    public Stock(String code, int quantity) {
        this.code = code;
        this.quantity = quantity;
    }

    public Stock() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return quantity == stock.quantity && Objects.equals(code, stock.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, quantity);
    }
}
