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
        for (CartItem item : items) {
            if (item.getMenuItem().getId() == menuItem.getId()) {
                item.increaseQuantity(quantity);
                return;
            }
        }

        items.add(new CartItem(menuItem, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void increaseQuantity(CartItem item) {
        item.increaseQuantity();
    }

    public void decreaseQuantity(CartItem item) {
        item.decreaseQuantity();
    }

    public double getTotalPrice() {
        double total = 0;

        for (CartItem item : items) {
            total += item.getSubtotal();
        }

        return total;
    }

    public int getItemCount() {
        int count = 0;

        for (CartItem item : items) {
            count += item.getQuantity();
        }

        return count;
    }

    public void clear() {
        items.clear();
    }

    public static class CartItem {
        private final MenuItem menuItem;
        private int quantity;

        public CartItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        public int getQuantity() {
            return quantity;
        }

        public void increaseQuantity() {
            quantity++;
        }

        public void increaseQuantity(int amount) {
            if (amount > 0) {
                quantity += amount;
            }
        }

        public void decreaseQuantity() {
            if (quantity > 1) {
                quantity--;
            }
        }

        public double getSubtotal() {
            return menuItem.getPrice() * quantity;
        }
    }
}
