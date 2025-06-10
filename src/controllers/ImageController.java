package controllers;

import models.ImageModel;
import models.MeshModel;
import models.Point3D;
import models.TransformationModel;
import views.ImagePanel;
import views.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler odpowiedzialny za zarządzanie operacjami na obrazach/modelach 3D w aplikacji.
 * Obsługuje wczytywanie, czyszczenie oraz zarządzanie modelem transformacji.
 */
public class ImageController {

    private final MainFrame mainFrame;
    private final ImagePanel imagePanel;
    private TransformationModel transformationModel;

    private MeshModel currentMesh;

    public ImageController(MainFrame mainFrame, TransformationModel transformationModel) {
        this.mainFrame = mainFrame;
        this.imagePanel = mainFrame.getPanel();
        this.transformationModel = transformationModel;
    }


    /**
     * Wczytuje obraz (dla 2D) lub model 3D (dla .obj) z pliku
     * i ustawia go w panelu.
     *
     * @param file Plik do wczytania.
     */
    public void loadImage(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".obj")) {
            loadObjModel(file);
        } else {
            // Zachowujemy wczytywanie obrazów 2D dla kompatybilności
            try {
                var image = ImageIO.read(file);
                var imageModel = new ImageModel(image);
                imagePanel.setModel(imageModel);
                imagePanel.repaint();

                // Przy wczytaniu obrazu 2D, resetujemy model 3D i punkty transformacji
                this.currentMesh = null;
                transformationModel.clearPoints(); // Czyści zarówno originalVertices, jak i transformedVertices
                mainFrame.getTransformationPanel().clearPointList(); // Czyścimy listę punktów w panelu transformacji

                mainFrame.adjustWindowSize();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Błąd wczytywania obrazu 2D: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Wczytuje model 3D z pliku .obj.
     * @param file Plik .obj do wczytania.
     */
    private void loadObjModel(File file) {
        MeshModel newMesh = new MeshModel();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Pomiń puste linie i komentarze
                }

                String[] parts = line.split("\\s+"); // Podziel linię spacjami

                switch (parts[0]) {
                    case "v": // Wierzchołek
                        if (parts.length >= 4) {
                            double x = Double.parseDouble(parts[1]);
                            double y = Double.parseDouble(parts[2]);
                            double z = Double.parseDouble(parts[3]);
                            newMesh.addVertex(new Point3D(x, y, z));
                        }
                        break;
                    case "f": // Ściana
                        if (parts.length >= 4) { // Ściana musi mieć co najmniej 3 wierzchołki (trójkąt)
                            List<Integer> vertexIndices = new ArrayList<>();
                            // Indeksy w pliku OBJ są liczone od 1
                            for (int i = 1; i < parts.length; i++) {
                                vertexIndices.add(Integer.parseInt(parts[i]) - 1); // Konwertuj na indeksy 0-based
                            }
                            newMesh.addFace(new models.Face(vertexIndices));
                        }
                        break;
                    // Można dodać obsługę innych typów (np. vn dla normalnych, vt dla tekstur), ale na razie nie są potrzebne
                }
            }

            this.currentMesh = newMesh; // Ustaw nowy model siatki
            imagePanel.setMeshModel(this.currentMesh); // Przekaż model siatki do panelu
            imagePanel.setModel(null); // Upewnij się, że panel nie próbuje rysować starego obrazu 2D

            // WAŻNE: Przekazujemy oryginalne wierzchołki do TransformationModel
            transformationModel.setOriginalVertices(newMesh.getVertices());
            // TransformationModel sam wywoła applyCurrentTransformation() po ustawieniu oryginalnych wierzchołków

            mainFrame.getTransformationPanel().clearPointList(); // Czyścimy starą listę
            mainFrame.getTransformationPanel().refreshPointsList(transformationModel.getPoints()); // Odświeżamy listę przekształconymi punktami
            mainFrame.getTransformationPanel().updateMatrixDisplay(transformationModel.getMatrixString());

            JOptionPane.showMessageDialog(mainFrame, "Model 3D wczytany pomyślnie!", "Sukces", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Błąd wczytywania pliku OBJ: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, "Błąd parsowania danych w pliku OBJ: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Usuwa obraz/model z panelu.
     */
    public void clearLeftPanel() {
        imagePanel.setModel(null); // Czyści obraz 2D
        imagePanel.setMeshModel(null); // Czyści model 3D
        imagePanel.repaint();
        transformationModel.clearPoints(); // Czyści punkty transformacji (zarówno original, jak i transformed)
        mainFrame.getTransformationPanel().clearPointList(); // Czyści listę punktów w GUI
        mainFrame.getTransformationPanel().updateMatrixDisplay(transformationModel.getMatrixString()); // Resetuje macierz wyświetlaną
    }

    public TransformationModel getModel() {
        return transformationModel;
    }

    public MeshModel getCurrentMesh() {
        return currentMesh;
    }
}