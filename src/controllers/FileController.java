package controllers;

import models.ImageModel;
import views.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Kontroler odpowiedzialny za operacje na plikach, takie jak zapisywanie obrazów.
 */
public class FileController {

    private final MainFrame mainFrame;

    public FileController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Zapisuje bieżący obraz z panelu do wybranego pliku.
     * Na razie obsługuje tylko zapis obrazów 2D.
     * @param file Plik, do którego ma zostać zapisany obraz.
     */
    public void saveFile(File file) {
        ImageModel imageModel = mainFrame.getPanel().getModel(); // Pobierz model obrazu z ImagePanel

        if (imageModel == null || imageModel.getImage() == null) {
            JOptionPane.showMessageDialog(mainFrame, "Brak obrazu 2D do zapisania.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String filePath = file.getAbsolutePath();
        String format = "png"; // Domyślny format
        if (filePath.toLowerCase().endsWith(".bmp")) {
            format = "bmp";
        } else if (filePath.toLowerCase().endsWith(".png")) {
            format = "png";
        } else {
            // Jeśli rozszerzenie nie zostało podane, dodaj domyślne (.png)
            filePath += ".png";
        }

        try {
            File outputFile = new File(filePath);
            ImageIO.write(imageModel.getImage(), format, outputFile);
            JOptionPane.showMessageDialog(mainFrame, "Obraz zapisano pomyślnie do: " + outputFile.getName(), "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Błąd podczas zapisu obrazu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}