package de.tech26.robotfactory.entity;

import java.util.Objects;

/**
 * @author Ajay Singh Pundir
 * It handles the order details.
 */
public class Order {

    private String id;
    private double total;

    public Order(String id, double total) {
        this.id = id;
        this.total = total;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.total, total) == 0 && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, total);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", total=" + total +
                '}';
    }
}
