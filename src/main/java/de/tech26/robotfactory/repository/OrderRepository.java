package de.tech26.robotfactory.repository;

import de.tech26.robotfactory.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ajay Singh Pundir
 * It handles the order persistance operations.
 */
@Component
public class OrderRepository {

    private final List<Order> orders;

    public OrderRepository() {
        orders = new CopyOnWriteArrayList<>();
    }

    public void saveOrder(Order order) {
        orders.add(order);
    }

    public void deleteOrder(Order order) {
        orders.remove(order);
    }

}
