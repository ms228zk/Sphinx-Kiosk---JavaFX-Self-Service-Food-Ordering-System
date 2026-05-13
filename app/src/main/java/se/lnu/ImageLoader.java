package se.lnu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Utility class for loading item images with fallback to placeholder
 */
public class ImageLoader {

    private static final String ITEMS_FOLDER = "/images/items/";
    private static final Color PLACEHOLDER_COLOR = Color.web("#e8e8e8");

    /**
     * Creates an ImageView with the specified image or placeholder
     * 
     * @param imageFileName Name of the image file (e.g., "burger.png")
     * @param fitWidth Width to fit the image to
     * @param fitHeight Height to fit the image to
     * @return ImageView with the loaded image or placeholder
     */
    public static ImageView createImageView(String imageFileName, double fitWidth, double fitHeight) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);

        if (imageFileName != null && !imageFileName.isEmpty()) {
            try {
                String imagePath = ITEMS_FOLDER + imageFileName;
                Image image = new Image(ImageLoader.class.getResource(imagePath).toExternalForm());

                if (!image.isError()) {
                    imageView.setImage(image);
                } else {
                    imageView.setImage(createPlaceholder((int) fitWidth, (int) fitHeight));
                }
            } catch (Exception e) {
                imageView.setImage(createPlaceholder((int) fitWidth, (int) fitHeight));
            }
        } else {
            imageView.setImage(createPlaceholder((int) fitWidth, (int) fitHeight));
        }

        return imageView;
    }

    /**
     * Creates a placeholder image with solid color background
     */
    public static Image createPlaceholder(int width, int height) {
        WritableImage placeholder = new WritableImage(width, height);
        PixelWriter writer = placeholder.getPixelWriter();

        // Fill with light gray background
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, PLACEHOLDER_COLOR);
            }
        }

        return placeholder;
    }

    /**
     * Sanitizes item name to create valid image filename
     * e.g., "BBQ Smash Burger" -> "bbq_smash_burger"
     */
    public static String sanitizeImageName(String name) {
        return name.toLowerCase()
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-z0-9_]", "");
    }
}