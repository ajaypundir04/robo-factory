package de.tech26.robotfactory.rest;

import de.tech26.robotfactory.entity.Type;
import de.tech26.robotfactory.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Ajay Singh Pundir
 * REST endpoints for mangement of stock.
 */
@Api(value = "Stock Controller")
@RestController
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @ApiOperation(value = "Get Stock")
    @GetMapping("/stock/")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Integer> getStock() {
        return stockService.getStock();
    }

    // TODO: type controller
    @ApiOperation(value = "Update Valid Combination")
    @PutMapping("/combinations")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Type> modifyCombinations(@RequestBody Type type,
                                         @RequestParam(required = false) boolean remove) {
        return stockService.modifyCombinations(type, remove);
    }

}
