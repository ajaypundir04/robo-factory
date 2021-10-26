package de.tech26.robotfactory.service;

import de.tech26.robotfactory.entity.Part;
import de.tech26.robotfactory.entity.Type;
import de.tech26.robotfactory.repository.PartRepository;
import de.tech26.robotfactory.repository.StockRepository;
import de.tech26.robotfactory.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Ajay Singh Pundir
 * It handles the stock.
 */
@Service
public class StockService {

    private final PartRepository partRepository;
    private final StockRepository stockRepository;
    private final TypeRepository typeRepository;

    @Autowired
    public StockService(PartRepository partRepository,
                        StockRepository stockRepository,
                        TypeRepository typeRepository) {
        this.partRepository = partRepository;
        this.typeRepository = typeRepository;
        this.stockRepository = stockRepository;
    }

    /**
     * @param code On the basis of code it will return available quantity
     * @return available quantity in @int
     */
    public int getAvailableQuantities(String code) {
        return stockRepository.getAvailableQuantities(code);
    }


    /**
     * @param code On the basis of part it will return it's details
     * @return @{@link Part} object is returned from the @partMap
     */
    public Part getPartByCode(String code) {
        return partRepository.getByCode(code);
    }


    /**
     * @param part          @{@link Part} is passed for modifying the stock.
     * @param quantToModify quantity of part to be modified.It can be +ve for add and -ve for delete
     */
    public void addOrUpdateStock(Part part, int quantToModify) {
        stockRepository.addOrUpdate(part.getCode(), quantToModify);
        partRepository.add(part);
    }

    /**
     * @param part @{@link Part} is passed to remove it from stock
     */
    public void removePartFromStock(Part part) {
        stockRepository.remove(part.getCode());
        partRepository.remove(part);
    }

    /**
     * Get the stock details.
     *
     * @return @{@link Map} map of stock.
     */
    public Map<String, Integer> getStock() {
        return stockRepository.getStockMap();
    }

    /**
     * Modification of required Types.
     *
     * @param type   @{@link Type} to be modified
     * @param remove added or removed
     * @return @{@link List<Type>} required types
     */
    public List<Type> modifyCombinations(Type type, Boolean remove) {
        typeRepository.addOrUpdateRequiredTypes(type, remove);
        return typeRepository.getRequiredTypes();
    }

    /**
     * Return the lis to of required types.
     *
     * @return @{@link List<Type>} required types
     */
    public List<Type> getRequiredCombinations() {
        return typeRepository.getRequiredTypes();
    }

}
