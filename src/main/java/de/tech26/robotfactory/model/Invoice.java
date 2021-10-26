package de.tech26.robotfactory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Invoice {

    @JsonProperty("order_id")
    private String orderId;

    private double total;

    public Invoice(String orderId, double total) {
        this.orderId = orderId;
        this.total = total;
    }

    public Invoice() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
        Invoice invoice = (Invoice) o;
        return Double.compare(invoice.total, total) == 0 && Objects.equals(orderId, invoice.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, total);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "orderId='" + orderId + '\'' +
                ", total=" + total +
                '}';
    }
}
