package de.tech26.robotfactory.repository;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tech26.robotfactory.entity.Stock;
import de.tech26.robotfactory.exception.RoboFactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Ajay Singh Pundir
 * It handles the stock persistance operations.
 */
@Component
public class StockRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartRepository.class);

    private final List<Stock> stocks;
    private final Map<String, Integer> stockMap;

    public StockRepository() {
        stockMap = new ConcurrentHashMap<>();
        stocks = new CopyOnWriteArrayList<>();
    }

    @PostConstruct
    public void initializeStock() {
        try {
            stocks.addAll(new ObjectMapper()
                    .readValue(new File("src/main/resources/stocks.json"),
                            new TypeReference<>() {
                            }));
            stockMap.putAll(stocks.stream()
                    .collect(Collectors
                            .toMap(Stock::getCode, Stock::getQuantity)));

        } catch (IOException e) {
            LOGGER.error("Unable to load parts due to", e);
            throw new RoboFactoryException("Unable to load stock due to", e);
        }
    }

    public int getAvailableQuantities(String code) {
        return this.stockMap
                .getOrDefault(code, 0);
    }

    public void addOrUpdate(String code, int quantToModify) {
        quantToModify += stockMap.getOrDefault(code, 0);
        stockMap.put(code, quantToModify);
        stocks.add(new Stock(code, quantToModify));
    }

    public void remove(String code) {
        stocks.remove(new Stock(code, stockMap.get(code)));
        stockMap.remove(code);
    }

    public Map<String, Integer> getStockMap() {
        return stockMap;
    }
}
