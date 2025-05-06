package controllers;

import views.ImagePanel;
import views.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Kontroler odpowiedzialny za zapisywanie obrazu znajdującego się w prawym panelu graficznym.
 */
public class FileController {

    private final MainFrame mainFrame;

    private final ImagePanel Panel;

    public FileController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.Panel = mainFrame.getPanel();
    }

    /**
     * Zapisuje obraz znajdujący się w prawym panelu do wskazanego pliku.
     *
     * @param file Obiekt reprezentujący plik, do którego obraz ma zostać zapisany.
     */
    public void saveFile(File file) {
        if (Panel.getModel() == null) {
            JOptionPane.showMessageDialog(mainFrame, "Brak obrazu do zapisania!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Pobranie formatu zapisu na podstawie rozszerzenia pliku
            String format = file.getName().substring(file.getName().lastIndexOf(".") + 1);

            // Zapis obrazu do pliku w wybranym formacie
            ImageIO.write(Panel.getModel().getImage(), format, file);

            // Komunikat o pomyślnym zapisie pliku
            JOptionPane.showMessageDialog(mainFrame, "Obraz zapisany pomyślnie!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Błąd zapisu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
