package controllers;

import models.ImageModel; // Nadal potrzebne, jeśli chcemy zachować możliwość wczytywania obrazów 2D
import models.MeshModel;  // Nowy import
import models.Point3D;     // Nowy import
import models.TransformationModel;
import views.ImagePanel;
import views.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.BufferedReader; // Do czytania pliku tekstowego
import java.io.File;
import java.io.FileReader;    // Do czytania pliku tekstowego
import java.io.IOException;
import java.util.ArrayList; // Do zbierania indeksów ścian
import java.util.List;

/**
 * Kontroler odpowiedzialny za zarządzanie operacjami na obrazach/modelach 3D w aplikacji.
 * Obsługuje wczytywanie, czyszczenie oraz zarządzanie modelem transformacji.
 */
public class ImageController {

    private final MainFrame mainFrame;
    private final ImagePanel imagePanel; // Zmieniono nazwę zmiennej z leftPanel na imagePanel dla jasności
    private TransformationModel transformationModel; // Zmieniono nazwę zmiennej z model na transformationModel

    // Nowe pole do przechowywania wczytanego modelu 3D
    private MeshModel currentMesh;

    public ImageController(MainFrame mainFrame, TransformationModel transformationModel) {
        this.mainFrame = mainFrame;
        this.imagePanel = mainFrame.getPanel(); // imagePanel jest teraz panelem do rysowania 3D
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
            // Zachowujemy wczytywanie obrazów 2D dla kompatybilności, jeśli ma być taka opcja
            try {
                var image = ImageIO.read(file);
                var imageModel = new ImageModel(image);
                imagePanel.setModel(imageModel); // Ustawia model obrazu 2D
                imagePanel.repaint();

                // Przy wczytaniu obrazu 2D, resetujemy model 3D i punkty transformacji
                this.currentMesh = null;
                transformationModel.clearPoints();
                mainFrame.getTransformationPanel().clearPointList(); // Czyścimy listę punktów w panelu transformacji

                mainFrame.adjustWindowSize(); // Dostosowujemy rozmiar okna do obrazu 2D
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
                            newMesh.addFace(new models.Face(vertexIndices)); // Użyj pełnej nazwy dla Face, aby uniknąć konfliktu
                        }
                        break;
                    // Można dodać obsługę innych typów (np. vn dla normalnych, vt dla tekstur), ale na razie nie są potrzebne
                }
            }

            this.currentMesh = newMesh; // Ustaw nowy model siatki
            imagePanel.setMeshModel(this.currentMesh); // Przekaż model siatki do panelu
            imagePanel.setModel(null); // Upewnij się, że panel nie próbuje rysować starego obrazu 2D

            // Inicjalizujemy punkty kontrolne w TransformationModel z wierzchołków bryły
            transformationModel.clearPoints(); // Wyczyść stare punkty
            for (Point3D vertex : newMesh.getVertices()) {
                transformationModel.addPoint(vertex.getX(), vertex.getY(), vertex.getZ());
            }
            mainFrame.getTransformationPanel().clearPointList(); // Wyczyść listę w GUI
            mainFrame.getTransformationPanel().updateMatrixDisplay(transformationModel.getMatrixString()); // Zaktualizuj macierz
            mainFrame.getTransformationPanel().refreshPointsList(transformationModel.getPoints()); // Odśwież listę punktów w GUI

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
        transformationModel.clearPoints(); // Czyści punkty transformacji
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