package views;

import models.ImageModel;
import models.MeshModel;
import models.Point3D;
import models.TransformationModel; // Dodaj ten import!
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ImagePanel extends JPanel {

    private ImageModel imageModel;
    private MeshModel meshModel;
    private List<Point3D> displayPoints = null;

    // NOWE: Dodaj pole transformationModel
    private TransformationModel transformationModel;

    // Zmodyfikowany konstruktor, aby przyjmował TransformationModel
    public ImagePanel(String title, TransformationModel transformationModel) {
        setBorder(BorderFactory.createTitledBorder(title));
        setBackground(Color.LIGHT_GRAY);
        this.imageModel = null;
        this.meshModel = null;
        this.transformationModel = transformationModel; // Inicjalizacja pola
    }

    /**
     * Ustawia nowy model obrazu 2D i odświeża panel.
     * @param imageModel Nowy model obrazu 2D do wyświetlenia.
     */
    public void setModel(ImageModel imageModel) {
        this.imageModel = imageModel;
        this.repaint();
    }

    public ImageModel getModel() {
        return imageModel;
    }

    /**
     * Ustawia nowy model siatki 3D i odświeża panel.
     * @param meshModel Nowy model siatki 3D do wyświetlenia.
     */
    public void setMeshModel(MeshModel meshModel) {
        this.meshModel = meshModel;
        this.repaint();
    }

    public MeshModel getMeshModel() {
        return meshModel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Rysowanie obrazu 2D, jeśli jest ustawiony
        if (imageModel != null && imageModel.getImage() != null) {
            int x = (getWidth() - imageModel.getImage().getWidth()) / 2;
            int y = (getHeight() - imageModel.getImage().getHeight()) / 2;
            g.drawImage(imageModel.getImage(), x, y, this);
        }

        // Rysowanie osi układu współrzędnych (na środku panelu)
        g.setColor(Color.gray);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        g.drawLine(0, cy, getWidth(), cy); // Oś X
        g.drawLine(cx, 0, cx, getHeight()); // Oś Y

        // Rysowanie wierzchołków i krawędzi modelu 3D
        if (meshModel != null && transformationModel != null) {
            g.setColor(Color.BLUE);

            // Pobierz aktualne WIERZCHOŁKI BRYŁY z TransformationModel, po transformacji
            List<Point3D> transformedMeshVertices = transformationModel.getTransformedVertices(); // Używamy nowej metody

            if (transformedMeshVertices != null && !transformedMeshVertices.isEmpty()) {
                for (models.Face face : meshModel.getFaces()) {
                    List<Integer> indices = face.getVertexIndices();
                    for (int i = 0; i < indices.size(); i++) {
                        // Sprawdź, czy indeksy są w zakresie listy transformedMeshVertices
                        int idx1 = indices.get(i);
                        int idx2 = indices.get((i + 1) % indices.size());
                        if (idx1 < transformedMeshVertices.size() && idx2 < transformedMeshVertices.size()) {
                            Point3D p1_3d = transformedMeshVertices.get(idx1);
                            Point3D p2_3d = transformedMeshVertices.get(idx2);

                            // Konwertuj punkty 3D na 2D
                            Point p1_2d = centeredToPanel(p1_3d);
                            Point p2_2d = centeredToPanel(p2_3d);

                            g.drawLine(p1_2d.x, p1_2d.y, p2_2d.x, p2_2d.y);
                        } else {
                            System.err.println("Błąd: Indeks wierzchołka poza zakresem w Face! Sprawdź plik .obj lub logikę ładowania/transformacji.");
                        }
                    }
                }
            } else {
                System.out.println("[DEBUG] Transformed mesh vertices list is empty or null. Not drawing mesh lines.");
            }
        }


        // Rysowanie wyświetlanych punktów (kontrolnych, dodanych myszą)
        // Te punkty nie są objęte globalną transformacją bryły na razie
        if (displayPoints != null) {
            g.setColor(Color.BLACK);
            for (Point3D pt : displayPoints) {
                Point p = centeredToPanel(pt); // Używamy nowej metody
                int r = 5; //promień
                g.setColor(Color.BLACK);
                g.fillOval((p.x - r), (p.y - r), r * 2, r * 2);
            }
        }

        System.out.println("[DEBUG] repaint triggered");
    }

    public void setDisplayedPoints(List<Point3D> points) {
        this.displayPoints = points;
        repaint();
    }

    public Point3D panelToCentered3D(int x, int y){
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        return new Point3D(x - cx, cy - y, 0); // Z=0 na tym etapie
    }

    public Point centeredToPanel(Point3D p){
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        return new Point((int)(p.x + cx),(int)(cy - p.y));
    }
}