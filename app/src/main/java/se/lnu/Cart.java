package se.lnu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {

    private static final Cart instance = new Cart();
    private final List<CartItem> items = new ArrayList<>();

    private Cart() {}

    public static Cart getInstance() {
        return instance;
    }

    public void addItem(MenuItem menuItem, int quantity) {
        addItem(menuItem, quantity, new ArrayList<>(), 0.0, new ArrayList<>(), new ArrayList<>());
    }

    public void addItem(MenuItem menuItem, int quantity, List<String> extras, double extrasPrice) {
        addItem(menuItem, quantity, extras, extrasPrice, new ArrayList<>(), new ArrayList<>());
    }

    public void addItem(MenuItem menuItem, int quantity, List<String> removed) {
        addItem(menuItem, quantity, new ArrayList<>(), 0.0, removed, new ArrayList<>());
    }

    public void addItem(
            MenuItem menuItem,
            int quantity,
            List<String> extras,
            double extrasPrice,
            List<String> removed,
            List<String> comboChoices
    ) {
        for (CartItem item : items) {
            if (item.isSameOrderItem(menuItem, extras, extrasPrice, removed, comboChoices)) {
                item.increaseQuantity(quantity);
                return;
            }
        }

        items.add(new CartItem(menuItem, quantity, extras, extrasPrice, removed, comboChoices));
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

    public void removeItem(CartItem item) {
        items.remove(item);
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

    public void reset() {
        clear();
    }

    public static class CartItem {
        private final MenuItem menuItem;
        private int quantity;
        private final List<String> extras;
        private final double extrasPrice;
        private final List<String> removed;
        private final List<String> comboChoices;

        public CartItem(
                MenuItem menuItem,
                int quantity,
                List<String> extras,
                double extrasPrice,
                List<String> removed,
                List<String> comboChoices
        ) {
            this.menuItem = menuItem;
            this.quantity = quantity;
            this.extras = new ArrayList<>(extras);
            this.extrasPrice = extrasPrice;
            this.removed = new ArrayList<>(removed);
            this.comboChoices = new ArrayList<>(comboChoices);
        }

        public boolean isSameOrderItem(
                MenuItem otherMenuItem,
                List<String> otherExtras,
                double otherExtrasPrice,
                List<String> otherRemoved,
                List<String> otherComboChoices
        ) {
            return menuItem.getId() == otherMenuItem.getId()
                    && Double.compare(extrasPrice, otherExtrasPrice) == 0
                    && normalizeList(extras).equals(normalizeList(otherExtras))
                    && normalizeList(removed).equals(normalizeList(otherRemoved))
                    && normalizeList(comboChoices).equals(normalizeList(otherComboChoices));
        }

        private static List<String> normalizeList(List<String> list) {
            List<String> normalized = new ArrayList<>();

            for (String value : list) {
                if (value != null && !value.isBlank()) {
                    normalized.add(value.trim());
                }
            }

            Collections.sort(normalized);
            return normalized;
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        public int getQuantity() {
            return quantity;
        }

        public List<String> getExtras() {
            return extras;
        }

        public double getExtrasPrice() {
            return extrasPrice;
        }

        public List<String> getRemoved() {
            return removed;
        }

        public List<String> getComboChoices() {
            return comboChoices;
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

        public double getUnitPriceWithExtras() {
            return menuItem.getPrice() + extrasPrice;
        }

        public double getSubtotal() {
            return getUnitPriceWithExtras() * quantity;
        }

        public String getExtrasText() {
            if (extras.isEmpty()) {
                return "";
            }

            return String.join(", ", extras);
        }

        public String getRemovedText() {
            if (removed.isEmpty()) {
                return "";
            }

            return String.join(", ", removed);
        }

        public String getComboChoicesText() {
            if (comboChoices.isEmpty()) {
                return "";
            }

            return String.join("\n", comboChoices);
        }
    }
}