package se.lnu;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private static final Cart instance = new Cart();
    private final List<CartItem> items = new ArrayList<>();

    private Cart() {}

    public static Cart getInstance() {
        return instance;
    }

    public void addItem(MenuItem menuItem, int quantity) {
        items.add(new CartItem(menuItem, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public static class CartItem {
        public final MenuItem menuItem;
        public final int quantity;

        public CartItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }
    }
}
