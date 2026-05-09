package se.lnu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import java.util.List;

public class Order {

    private static int counter = 1;
    private static String lastDate = "";

    // file in project folder
    private static final Path FILE_PATH = Path.of("order_data.txt");

    private final String orderNumber;
    private final List<Cart.CartItem> items;

    // Load saved data when class loads
    static {
        loadOrderData();
    }

    public Order(List<Cart.CartItem> items) {
        this.items = items;

        // reset counter every new day
        String today = java.time.LocalDate.now().toString();
        if (!today.equals(lastDate)) {
            counter = 1;
            lastDate = today;
        }

        // generate 4‑digit order number
        this.orderNumber = String.format("%04d", counter);

        counter++;
        if (counter > 9999) {
            counter = 1;
        }
        // Save updated counter + date
        saveOrderData();
    }

    // Save to file
    private static void saveOrderData() {
        try {
            String data = lastDate + "\n" + counter;
            Files.writeString(FILE_PATH, data);
        } catch (IOException e) {
            System.out.println("Failed to save order data: " + e.getMessage());
        }
    }

    // Load from file
    private static void loadOrderData() {
        try {
            if (Files.exists(FILE_PATH)) {
                List<String> lines = Files.readAllLines(FILE_PATH);

                if (lines.size() >= 2) {
                    lastDate = lines.get(0).trim();
                    counter = Integer.parseInt(lines.get(1).trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load order data: " + e.getMessage());
        }
    }

    // getter for order number
    public String getOrderNumber() {
        return orderNumber;
    }

    public String getItemList() {
        StringBuilder sb = new StringBuilder();

        for (Cart.CartItem item : items) {
            sb.append(item.getQuantity())
                    .append(" x ")
                    .append(item.getMenuItem().getName())
                    .append("\n");
        }

        return sb.toString();
    }

    public double getTotalPrice() {
        double total = 0;

        for (Cart.CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public List<Cart.CartItem> getItems() {
        return items;
    }
}
