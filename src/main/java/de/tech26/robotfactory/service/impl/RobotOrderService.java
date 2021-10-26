package de.tech26.robotfactory.service.impl;

import de.tech26.robotfactory.entity.Order;
import de.tech26.robotfactory.entity.Part;
import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.model.Invoice;
import de.tech26.robotfactory.repository.OrderRepository;
import de.tech26.robotfactory.service.OrderService;
import de.tech26.robotfactory.service.StockService;
import de.tech26.robotfactory.validator.ConfigurationRequestValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ajay Singh Pundir
 * It handles the order.
 */
@Service
public class RobotOrderService implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobotOrderService.class);
    private final StockService stockService;
    private final OrderRepository orderRepository;
    private final List<ConfigurationRequestValidation> validators;

    @Autowired
    public RobotOrderService(StockService stockService,
                             OrderRepository orderRepository, List<ConfigurationRequestValidation> validators) {
        this.stockService = stockService;
        this.orderRepository = orderRepository;
        this.validators = validators;
    }

    /**
     * Used to place the order.
     *
     * @param configuration @{@link Configuration} of the order
     * @return @{@link Order} generated
     */
    @Override
    public Order placeOrder(Configuration configuration) {
        Assert.notNull(configuration, "Configuration can't be null");
        validators.forEach(v -> v.validate(configuration));
        double total = configuration.getComponents()
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)))
                .entrySet()
                .stream()
                .mapToDouble(e -> {
                    Part part = stockService.getPartByCode(e.getKey());
                    stockService.addOrUpdateStock(part, -1 * e.getValue());
                    return part.getPrice() * e.getValue();
                })
                .sum();
        LOGGER.info("Total Order price {}", total);
        Order order = new Order(UUID.randomUUID().toString(), total);
        orderRepository.saveOrder(order);
        return order;

    }


    /**
     * Invoice generation.
     *
     * @param order @{@link Order} for invoice generation
     * @return @{@link Invoice} generated
     */
    @Override
    public Invoice generateInvoice(Order order) {
        LOGGER.info("Invoice Generated {}", order.toString());
        return new Invoice(order.getId(), order.getTotal());
    }

    /**
     * Cancellation of Order.
     *
     * @param order @{@link Order} to be cancelled
     */
    @Override
    public void cancelOrder(Order order) {
        LOGGER.info("Order to be cancelled {}", order.toString());
        orderRepository.deleteOrder(order);
    }
}
