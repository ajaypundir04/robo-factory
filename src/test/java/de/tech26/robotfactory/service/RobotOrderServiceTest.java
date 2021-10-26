package de.tech26.robotfactory.service;

import de.tech26.robotfactory.entity.Order;
import de.tech26.robotfactory.entity.Part;
import de.tech26.robotfactory.model.Invoice;
import de.tech26.robotfactory.repository.OrderRepository;
import de.tech26.robotfactory.service.impl.RobotOrderService;
import de.tech26.robotfactory.util.TestUtil;
import de.tech26.robotfactory.validator.CombinationValidation;
import de.tech26.robotfactory.validator.ConfigurationRequestValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Iterator;
import java.util.List;

public class RobotOrderServiceTest {
    private final StockService stockService = Mockito.mock(StockService.class);
    private final List<ConfigurationRequestValidation> validators = Mockito.mock(List.class);
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final RobotOrderService robotOrderService = new RobotOrderService(stockService,
            orderRepository, validators);

    @BeforeEach
    public void setUp() {
        Iterator mockIterator = Mockito.mock(Iterator.class);
        Mockito.doNothing().when(validators).forEach(Mockito.any());
        Mockito.when(validators.iterator()).thenReturn(mockIterator);
        Mockito.when(mockIterator.hasNext()).thenReturn(true, false);
        Mockito.when(mockIterator.next()).thenReturn(Mockito.mock(CombinationValidation.class));
        Mockito.reset(stockService, validators, orderRepository);
    }

    @Test
    public void testPlaceOrder() {
        Mockito.doNothing().when(stockService)
                .addOrUpdateStock(Mockito.any(Part.class), Mockito.anyInt());
        Mockito.doNothing().when(orderRepository).saveOrder(Mockito.any(Order.class));
        Mockito.when(stockService.getPartByCode("A")).thenReturn(TestUtil.parts.get("A"));
        Mockito.when(stockService.getPartByCode("I")).thenReturn(TestUtil.parts.get("I"));
        Mockito.when(stockService.getPartByCode("D")).thenReturn(TestUtil.parts.get("D"));
        Mockito.when(stockService.getPartByCode("F")).thenReturn(TestUtil.parts.get("F"));
        Order order = robotOrderService.placeOrder(TestUtil.createConfiguration());
        Assertions.assertNotNull(order);
        Assertions.assertEquals(160.11, order.getTotal());
        Mockito.verify(stockService, Mockito.times(4))
                .addOrUpdateStock(Mockito.any(Part.class), Mockito.anyInt());
        Mockito.verify(orderRepository, Mockito.times(1))
                .saveOrder(Mockito.any(Order.class));
        Mockito.verify(stockService, Mockito.times(4))
                .getPartByCode(Mockito.anyString());
    }

    @Test
    public void testGenerateInvoice() {
        Invoice invoice = robotOrderService.generateInvoice(TestUtil.createOrder());
        Assertions.assertEquals(TestUtil.createInvoice(), invoice);
    }

    @Test
    public void testCancelOrder() {
        robotOrderService.cancelOrder(TestUtil.createOrder());
        Mockito.verify(orderRepository).deleteOrder(Mockito.any(Order.class));
    }

}
