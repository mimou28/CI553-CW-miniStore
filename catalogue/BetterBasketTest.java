package catalogue;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test; 

class BetterBasketTest {
    @Test 
    void testMergeAddProduct() { 
        BetterBasket br = new BetterBasket();  
        Product p1 = new Product("0001", "Toaster", 12.3, 1); 
        Product p2 = new Product("0002", "Kettle", 12.7, 1); 
        Product p3 = new Product("0002", "Kettle", 12.7, 1); 
        Product p4 = new Product("0002", "Kettle", 12.7, 1); 
        br.add(p1);
        br.add(p2);
        br.add(p3);
        br.add(p4);
        assertEquals(2, br.size(), "Incorrect size after merge");
        assertEquals(3, br.get(1).getQuantity(), "Incorrect quantity after merge");
    }
}
