package de.tech26.robotfactory.service;

import de.tech26.robotfactory.entity.Part;
import de.tech26.robotfactory.entity.Type;
import de.tech26.robotfactory.repository.PartRepository;
import de.tech26.robotfactory.repository.StockRepository;
import de.tech26.robotfactory.repository.TypeRepository;
import de.tech26.robotfactory.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StockServiceServiceTest {
    private final PartRepository partRepository = Mockito.mock(PartRepository.class);
    private final StockRepository stockRepository = Mockito.mock(StockRepository.class);
    private final TypeRepository typeRepository = Mockito.mock(TypeRepository.class);
    private final StockService stockService = new StockService(partRepository, stockRepository,
            typeRepository);

    @BeforeEach
    public void setUp() {
        Mockito.reset(stockRepository, typeRepository, partRepository);
    }

    @Test
    public void testGetAvailableQuantities() {
        Mockito.when(stockRepository.getAvailableQuantities(Mockito.anyString()))
                .thenReturn(1);
        Assertions.assertEquals(1, stockService.getAvailableQuantities("A"));
    }

    @Test
    public void testGetPartByCode() {
        Part part = TestUtil.parts.get("A");
        Mockito.when(partRepository.getByCode(Mockito.anyString())).thenReturn(part);
        Part actual = stockService.getPartByCode("A");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(part, actual);
    }

    @Test
    public void testAddOrUpdate() {
        stockService.addOrUpdateStock(TestUtil.parts.get("A"), 2);
        Mockito.verify(partRepository, Mockito.times(1)).add(Mockito.any(Part.class));
        Mockito.verify(stockRepository, Mockito.times(1))
                .addOrUpdate(Mockito.anyString(), Mockito.anyInt());

    }

    @Test
    public void testRemovePartFromStock() {
        stockService.removePartFromStock(TestUtil.parts.get("A"));
        Mockito.verify(stockRepository, Mockito.times(1)).remove(Mockito.anyString());
        Mockito.verify(partRepository, Mockito.times(1)).remove(Mockito.any(Part.class));
    }

    @Test
    public void testGetStock() {
        Mockito.when(stockRepository.getStockMap()).thenReturn(TestUtil.stocks);
        Assertions.assertEquals(TestUtil.stocks, stockService.getStock());
    }

    @Test
    public void testModifyCombinations() {
        Type t = new Type("Arms");
        Mockito.when(typeRepository.getRequiredTypes())
                .thenReturn(TestUtil.getRequiredTypes());
        Assertions.assertEquals(TestUtil.getRequiredTypes(),
                stockService.modifyCombinations(t, false));
        Mockito.verify(typeRepository, Mockito.times(1))
                .addOrUpdateRequiredTypes(Mockito.any(Type.class), Mockito.anyBoolean());
    }

    @Test
    public void testValidCombinations() {
        Mockito.when(typeRepository.getRequiredTypes())
                .thenReturn(TestUtil.getRequiredTypes());
        Assertions.assertEquals(TestUtil.getRequiredTypes(),
                stockService.getRequiredCombinations());
    }
}
