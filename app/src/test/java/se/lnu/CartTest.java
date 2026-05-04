package se.lnu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartTest {

    private final Cart cart = Cart.getInstance();

    @BeforeEach
    void clearCart() {
        cart.clear();
    }

    @Test
    void calculatesSubtotalFromUnitPriceAndQuantity() {
        MenuItem menuItem = new MenuItem(1, "Burger", "Cheese burger", 49.50);

        cart.addItem(menuItem, 3);

        assertEquals(148.50, cart.getItems().get(0).getSubtotal(), 0.001);
        assertEquals(148.50, cart.getTotalPrice(), 0.001);
    }

    @Test
    void keepsCartItemsWhenAddingMoreOfTheSameMenuItem() {
        MenuItem menuItem = new MenuItem(1, "Burger", "Cheese burger", 49.50);

        cart.addItem(menuItem, 1);
        cart.addItem(menuItem, 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());
        assertEquals(3, cart.getItemCount());
    }
}
