package views;

import models.ImageModel;
import models.MeshModel;
import models.Point3D;
import models.TransformationModel;
import models.Matrix4x4;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Arrays;

public class ImagePanel extends JPanel {

    private ImageModel imageModel;
    private MeshModel meshModel;
    private List<Point3D> displayPoints = null;

    private TransformationModel transformationModel;

    private BufferedImage bufferImage;
    private double[][] zBuffer;

    // Parametry kamery (do projekcji perspektywicznej)
    private double fovY = 60.0;
    private double near = 0.1;
    private double far = 100.0; // Zmniejszyłem far dla lepszej kontroli na początku

    private Point3D eye = new Point3D(0,0,-10);
    private  Point3D center = new Point3D(0,0,0);
    private Point3D up = new Point3D(0,1,0);

    public ImagePanel(String title, TransformationModel transformationModel) {
        setBorder(BorderFactory.createTitledBorder(title));
        setBackground(Color.LIGHT_GRAY);
        this.imageModel = null;
        this.meshModel = null;
        this.transformationModel = transformationModel;
        // Inicjalizacja buforów w invalidate() i initBuffers()
    }



    private void initBuffers() {
        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0) return;

        if (bufferImage == null || bufferImage.getWidth() != width || bufferImage.getHeight() != height) {
            bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            zBuffer = new double[width][height];
        }

        // Wyczyść Z-bufor dla nowej klatki
        for (int x = 0; x < width; x++) {
            Arrays.fill(zBuffer[x], Double.POSITIVE_INFINITY);
        }

        // Wyczyść bufor obrazu kolorem tła
        Graphics2D g2dBuffer = bufferImage.createGraphics();
        g2dBuffer.setColor(getBackground());
        g2dBuffer.fillRect(0, 0, width, height);
        g2dBuffer.dispose();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Upewnij się, że bufory są zainicjalizowane i mają poprawny rozmiar
        initBuffers();

        // Pobieramy Graphics2D z bufora obrazu, aby rysować na nim wszystko
        Graphics2D g2dBuffer = bufferImage.createGraphics();

        // Rysowanie obrazu 2D (jeśli jest ustawiony)
        if (imageModel != null && imageModel.getImage() != null) {
            int x = (getWidth() - imageModel.getImage().getWidth()) / 2;
            int y = (getHeight() - imageModel.getImage().getHeight()) / 2;
            g2dBuffer.drawImage(imageModel.getImage(), x, y, this);
        }

        // Rysowanie osi układu współrzędnych (na środku panelu)
        g2dBuffer.setColor(Color.gray);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        g2dBuffer.drawLine(0, cy, getWidth(), cy); // Oś X
        g2dBuffer.drawLine(cx, 0, cx, getHeight()); // Oś Y


        // Rysowanie wierzchołków i krawędzi modelu 3D
        if (meshModel != null && transformationModel != null && bufferImage != null) {
            List<Point3D> transformedMeshVertices = transformationModel.getTransformedVertices();

            if (transformedMeshVertices != null && !transformedMeshVertices.isEmpty()) {

                double aspectRatio = (double) getWidth() / getHeight();
                Matrix4x4 projectionMatrix = Matrix4x4.perspective(fovY, aspectRatio, near, far);
                Matrix4x4 viewMatrix = Matrix4x4.lookAt(eye,center,up);

                for (models.Face face : meshModel.getFaces()) {
                    List<Integer> indices = face.getVertexIndices();
                    for (int i = 0; i < indices.size(); i++) {
                        int idx1 = indices.get(i);
                        int idx2 = indices.get((i + 1) % indices.size());

                        if (idx1 < transformedMeshVertices.size() && idx2 < transformedMeshVertices.size()) {
                            Point3D p1_3d_world = transformedMeshVertices.get(idx1);
                            Point3D p2_3d_world = transformedMeshVertices.get(idx2);

                            Point3D p1_3d_view = Matrix4x4.transformPoint(viewMatrix,p1_3d_world);
                            Point3D p2_3d_view = Matrix4x4.transformPoint(viewMatrix,p2_3d_world);

                            Point3D p1_projected = Matrix4x4.transformPoint(projectionMatrix, p1_3d_view);
                            Point3D p2_projected = Matrix4x4.transformPoint(projectionMatrix, p2_3d_view);

                            if (p1_projected.z >= -1.0 && p1_projected.z <= 1.0 &&
                                    p2_projected.z >= -1.0 && p2_projected.z <= 1.0) {

                                Point p1_screen = ndcToScreen(p1_projected);
                                Point p2_screen = ndcToScreen(p2_projected);

                                drawLineWithZBuffer(
                                        p1_screen.x, p1_screen.y, p1_projected.z,
                                        p2_screen.x, p2_screen.y, p2_projected.z,
                                        Color.BLUE // Kolor linii
                                );
                            } else {
                                // Linia poza frustumem Z (nie rysujemy jej)
                            }
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
        if (displayPoints != null) {
            g2dBuffer.setColor(Color.RED);
            for (Point3D pt : displayPoints) {
                Point p = centeredToPanel(pt);
                int r = 5;
                g2dBuffer.fillOval((p.x - r), (p.y - r), r * 2, r * 2);
            }
        }

        // Zwalniamy kontekst graficzny dla bufora
        g2dBuffer.dispose();

        // Na koniec, narysuj zawartość bufora obrazu na panelu
        g.drawImage(bufferImage, 0, 0, this);
    }

    private Point ndcToScreen(Point3D pPoint) {
        int screenX = (int) ( (pPoint.x + 1.0) * getWidth() / 2.0 );
        int screenY = (int) ( (1.0 - pPoint.y) * getHeight() / 2.0 );
        return new Point(screenX, screenY);
    }

    private void setPixel(int x, int y, double z, Color color) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            if (z < zBuffer[x][y]) {
                zBuffer[x][y] = z;
                bufferImage.setRGB(x, y, color.getRGB());
            }
        }
    }

    private void drawLineWithZBuffer(int x0, int y0, double z0, int x1, int y1, double z1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        int err = dx - dy;

        int currentX = x0;
        int currentY = y0;
        double currentZ;

        while(true) {
            // Oblicz t na podstawie aktualnej pozycji (x,y) względem (x0,y0) i (x1,y1)
            double t = 0.0;
            if (dx > dy) { // Dominująca oś to X
                if (x1 != x0) { // Unikaj dzielenia przez zero
                    t = (double)(currentX - x0) / (x1 - x0);
                }
            } else if (dy > 0) { // Dominująca oś to Y
                if (y1 != y0) { // Unikaj dzielenia przez zero
                    t = (double)(currentY - y0) / (y1 - y0);
                }
            }
            // If dx and dy are 0 (single point), t will remain 0.0.

            currentZ = z0 + t * (z1 - z0); // Interpolacja Z

            setPixel(currentX, currentY, currentZ, color);

            if (currentX == x1 && currentY == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; currentX += sx; }
            if (e2 < dx) { err += dx; currentY += sy; }
        }
    }

    // Pozostałe metody (setModel, getModel, setMeshModel, getMeshModel, setDisplayedPoints,
    // panelToCentered3D, centeredToPanel, invalidate) pozostają bez zmian
    public void setModel(ImageModel imageModel) {
        this.imageModel = imageModel;
        this.repaint();
    }

    public ImageModel getModel() {
        return imageModel;
    }

    public void setMeshModel(MeshModel meshModel) {
        this.meshModel = meshModel;
        this.repaint();
    }

    public MeshModel getMeshModel() {
        return meshModel;
    }

    public void setDisplayedPoints(List<Point3D> points) {
        this.displayPoints = points;
        repaint();
    }

    public Point3D panelToCentered3D(int x, int y){
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        return new Point3D(x - cx, cy - y, 0);
    }

    public Point centeredToPanel(Point3D p){
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        return new Point((int)(p.x + cx),(int)(cy - p.y));
    }

    @Override
    public void invalidate() {
        super.invalidate();
        initBuffers();
    }
}