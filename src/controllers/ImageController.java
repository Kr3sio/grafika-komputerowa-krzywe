package controllers;

import models.ImageModel;
import models.MeshModel;
import models.TransformationModel;
import views.MainFrame;
import views.PointListPanel;
import views.MatrixDisplayPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage; // DODAJ TEN IMPORT
import java.io.File;
import java.io.IOException;
import java.util.ArrayList; // DODAJ TEN IMPORT dla new ArrayList<>()

public class ImageController {

    private final MainFrame mainFrame;
    private final TransformationModel transformationModel;

    public ImageController(MainFrame mainFrame, TransformationModel transformationModel) {
        this.mainFrame = mainFrame;
        this.transformationModel = transformationModel;
    }

    public void loadImage(File file) {
        if (file == null) {
            return;
        }

        String filePath = file.getAbsolutePath();
        if (filePath.toLowerCase().endsWith(".obj")) {
            // Ładowanie modelu 3D
            try {
                MeshModel meshModel = new MeshModel(filePath); // Wczytaj plik OBJ
                transformationModel.setOriginalVertices(meshModel.getVertices()); // Ustaw wierzchołki
                transformationModel.resetTransformation(); // Resetuj transformację
                mainFrame.getPanel().setMeshModel(meshModel); // Ustaw model w ImagePanel
                // Punkty kontrolne nie są z oryginalnych wierzchołków modelu, a dodane przez użytkownika.
                // Jeśli chcesz wyświetlić wierzchołki modelu jako punkty, to:
                // mainFrame.getPanel().setDisplayedPoints(transformationModel.getTransformedVertices());
                // Ale jeśli punkty kontrolne to punkty DODANE MYSZĄ, to poniższe jest poprawne:
                mainFrame.getPanel().setDisplayedPoints(transformationModel.getPoints());


                // Aktualizuj panele po wczytaniu nowego modelu
                mainFrame.getPointListPanel().clearPointList(); // Wyczyść starą listę punktów z PointListPanel
                // Jeśli chcesz wyświetlić wierzchołki OBJ w liście punktów:
                // mainFrame.getPointListPanel().refreshPointsList(transformationModel.getTransformedVertices());
                // Jeśli lista punktów to punkty DODANE MYSZĄ (co jest obecnym założeniem):
                mainFrame.getPointListPanel().refreshPointsList(transformationModel.getPoints());

                mainFrame.getMatrixDisplayPanel().updateMatrixDisplay(transformationModel.getMatrixString());

                mainFrame.getPanel().repaint();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Błąd podczas ładowania pliku OBJ: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            // Ładowanie obrazu 2D (istniejąca funkcjonalność)
            try {
                BufferedImage image = ImageIO.read(file);
                mainFrame.getPanel().setModel(new ImageModel(image));
                mainFrame.adjustWindowSize();

                // DLA OBRAZÓW 2D: Czyścimy punkty i resetujemy transformacje 3D
                mainFrame.getPanel().setDisplayedPoints(null); // UKRYJ punkty wyświetlane na ImagePanel
                transformationModel.resetTransformation();
                transformationModel.clearPoints(); // Wyczyść listę punktów w TransformationModel
                mainFrame.getPointListPanel().clearPointList(); // Wyczyść listę punktów w PointListPanel
                mainFrame.getMatrixDisplayPanel().updateMatrixDisplay(transformationModel.getMatrixString());

                mainFrame.getPanel().repaint();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Błąd podczas ładowania obrazu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void clearLeftPanel() {
        mainFrame.getPanel().setModel(null);
        mainFrame.getPanel().setMeshModel(null); // Wyczyść też model 3D

        // Czyszczenie punktów w ImagePanel i TransformationModel
        mainFrame.getPanel().setDisplayedPoints(null); // UKRYJ punkty wyświetlane na ImagePanel
        transformationModel.resetTransformation();
        transformationModel.clearPoints(); // Wyczyść listę punktów w TransformationModel

        // Czyszczenie paneli kontrolnych
        mainFrame.getPointListPanel().clearPointList(); // Wyczyść listę punktów w PointListPanel
        mainFrame.getMatrixDisplayPanel().updateMatrixDisplay(transformationModel.getMatrixString()); // Zaktualizuj macierz do jednostkowej

        mainFrame.getPanel().repaint();
    }
}