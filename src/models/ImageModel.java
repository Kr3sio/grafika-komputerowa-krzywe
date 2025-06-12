package models;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Model reprezentujący obraz, umożliwiający jego modyfikację oraz kopiowanie.
 *
 * <p>
 *     Przechowuje obraz w postaci obiektu {@link BufferedImage} i udostępnia metody do jego modyfikacji.
 *     Tylko ta klasa powinna udostępniać metody do modyfikacji obrazu.
 * </p>
 *
 */
public class ImageModel {

    private BufferedImage image;

    public ImageModel(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }


    public BufferedImage getCopyImage() {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return copy;
    }




}

