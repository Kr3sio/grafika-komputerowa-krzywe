package views;

import models.ImageModel;
import models.MeshModel; // Nowy import
import models.Point3D;
import models.TransformationModel;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ImagePanel extends JPanel {

    private ImageModel imageModel; // Zmieniono nazwę zmiennej, aby odróżnić od modelu 3D
    private MeshModel meshModel;   // Nowe pole na model 3D

    private List<Point3D> displayPoints = null; // Punkty wyświetlane (kontrolne)

    private TransformationModel transformationModel;

    public ImagePanel(String title, TransformationModel transformationModel) {
        setBorder(BorderFactory.createTitledBorder(title));
        setBackground(Color.LIGHT_GRAY);
        this.imageModel = null;
        this.meshModel = null; // Inicjalizacja nowego pola
        this.transformationModel = transformationModel;
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
        if (meshModel != null && transformationModel != null) { // Dodano sprawdzenie transformationModel
            g.setColor(Color.BLUE);

            // Na razie rysujemy wszystkie krawędzie (model druciany)
            // Bez usuwania niewidocznych linii i bez wypełniania ścian
            // Projekcja perspektywiczna będzie w Zadaniach 5 i 6

            // Pobierz aktualne wierzchołki z TransformationModel, ponieważ to on je transformuje
            List<Point3D> transformedVertices = transformationModel.getPoints(); // Zakładamy, że transformationModel.getPoints() zwróci wszystkie wierzchołki, które są przetwarzane

            if (transformedVertices != null && !transformedVertices.isEmpty()) { // Dodano sprawdzenie, czy lista nie jest pusta
                for (models.Face face : meshModel.getFaces()) {
                    List<Integer> indices = face.getVertexIndices();
                    for (int i = 0; i < indices.size(); i++) {
                        // Sprawdź, czy indeksy są w zakresie listy transformedVertices
                        if (indices.get(i) < transformedVertices.size() && indices.get((i + 1) % indices.size()) < transformedVertices.size()) {
                            Point3D p1_3d = transformedVertices.get(indices.get(i));
                            Point3D p2_3d = transformedVertices.get(indices.get((i + 1) % indices.size()));

                            // Konwertuj punkty 3D na 2D
                            Point p1_2d = centeredToPanel(p1_3d);
                            Point p2_2d = centeredToPanel(p2_3d);

                            g.drawLine(p1_2d.x, p1_2d.y, p2_2d.x, p2_2d.y);
                        } else {
                            System.err.println("Błąd: Indeks wierzchołka poza zakresem w Face! Sprawdź plik .obj");
                        }
                    }
                }
            } else {
                System.out.println("[DEBUG] Transformed vertices list is empty or null. Not drawing mesh lines.");
            }
        }



        // Rysowanie wyświetlanych punktów (kontrolnych) - np. tych, które dodaliśmy myszą
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