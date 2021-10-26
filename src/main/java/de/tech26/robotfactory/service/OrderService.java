package de.tech26.robotfactory.service;

import de.tech26.robotfactory.entity.Order;
import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.model.Invoice;

public interface OrderService {

    Order placeOrder(Configuration configuration);

    Invoice generateInvoice(Order order);

    void cancelOrder(Order order);

}
