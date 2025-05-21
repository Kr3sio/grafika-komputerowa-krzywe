package controllers;

import models.ImageModel;
import models.TransformationModel;
import views.ImagePanel;
import views.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Kontroler odpowiedzialny za zarządzanie operacjami na obrazach w aplikacji.
 * Obsługuje wczytywanie, czyszczenie, kopiowanie oraz rysowanie kształtów na obrazach.
 */
public class ImageController {

    private final MainFrame mainFrame;

    private final ImagePanel leftPanel;

    private TransformationModel model;

    public ImageController(MainFrame mainFrame, TransformationModel model) {
        this.mainFrame = mainFrame;
        this.leftPanel = mainFrame.getPanel();
        this.model = model;
    }


    /**
     * Wczytuje obraz z pliku i ustawia go w lewym panelu.
     *
     * @param file Plik obrazu do wczytania.
     */
    public void loadImage(File file) {
        try {
            var image = ImageIO.read(file);
            var imageModel = new ImageModel(image);
            leftPanel.setModel(imageModel);
            leftPanel.repaint();

            if (model == null) {
                model = new TransformationModel(); // do punktów i macierzy
            }

            mainFrame.adjustWindowSize();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Nieznany błąd!", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Usuwa obraz z lewego panelu.
     */
    public void clearLeftPanel() {
        leftPanel.setModel(null);
        leftPanel.repaint();
    }


    public TransformationModel getModel(){
        return model;
    }







}
