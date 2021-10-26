package de.tech26.robotfactory.rest;

import de.tech26.robotfactory.model.Configuration;
import de.tech26.robotfactory.model.Invoice;
import de.tech26.robotfactory.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Ajay Singh Pundir
 * REST endpoints for placing orders for robot manufacturing.
 */
@Api(value = "Robot Order Controller")
@RestController
@Validated
public class RobotOrderController {

    private final OrderService orderService;

    @Autowired
    public RobotOrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @ApiOperation(value = "Place Robot Order")
    @PostMapping("/orders")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Invoice placeOrder(@Valid @RequestBody Configuration configuration) {
        return orderService.generateInvoice(
                orderService.placeOrder(configuration)
        );
    }


}
