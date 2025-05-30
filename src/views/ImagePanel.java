package views;

import models.ImageModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Panel do wyświetlania obrazu, wykorzystujący model {@link ImageModel}.
 * Panel odświeża się automatycznie po zmianie modelu.
 */
public class ImagePanel extends JPanel {

    private ImageModel model;

    private boolean drawPolyline = false;

    private List<Point2D.Double> polylinePoints = null;

    private List<Point2D.Double> displayPoints = null;

    private boolean drawBezier = false;

    private List<Point2D.Double> bezierPoints = null;

    public ImagePanel(String title) {
        setBorder(BorderFactory.createTitledBorder(title)); // Ustawienie obramowania z tytułem
        setBackground(Color.LIGHT_GRAY); // Ustawienie koloru tła panelu
        this.model = null;
    }

    /**
     * Ustawia nowy model obrazu i odświeża panel.
     * @param model Nowy model obrazu do wyświetlenia.
     */
    public void setModel(ImageModel model) {
        this.model = model;
        this.repaint(); // Odświeżenie panelu, aby wyświetlić nowy obraz
    }

    public ImageModel getModel() {
        return model;
    }



    /**
     * Przesłonięta metoda rysowania komponentu. Wyświetla obraz na środku panelu.
     * @param g Kontekst graficzny wykorzystywany do rysowania obrazu.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (model != null && model.getImage() != null) {
            // Obliczenie współrzędnych, aby obraz był wyśrodkowany w panelu
            int x = (getWidth() - model.getImage().getWidth()) / 2;
            int y = (getHeight() -  model.getImage().getHeight()) / 2;

            // Rysowanie obrazu na panelu
            g.drawImage( model.getImage(), x, y, this);
        }
        g.setColor(Color.gray);
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        g.drawLine(0,cy,getWidth(),cy);
        g.drawLine(cx,0,cx,getHeight());

        if (displayPoints != null) {
            g.setColor(Color.BLACK);
            for (Point2D.Double pt : displayPoints) {
                Point p = centeredToPanel(pt.x,pt.y);
                int r = 5; //promień
                g.setColor(Color.BLACK);
                g.fillOval((p.x -r),(p.y-r),r*2,r*2);
            }
        }

        if(drawPolyline && polylinePoints != null && polylinePoints.size() > 1) {
            g.setColor(Color.RED);
            for(int i = 0 ; i < polylinePoints.size()-1 ; i++) {
                Point p1 = centeredToPanel(polylinePoints.get(i).x, polylinePoints.get(i).y);
                Point p2 = centeredToPanel(polylinePoints.get(i+1).x, polylinePoints.get(i+1).y);
                g.drawLine(p1.x,p1.y,p2.x,p2.y);
            }
        }
        if (drawBezier && bezierPoints != null && bezierPoints.size() > 1) {
            g.setColor(Color.BLACK);
            for (int i = 0; i < bezierPoints.size() - 1; i++) {
                Point p1 = centeredToPanel(bezierPoints.get(i).x, bezierPoints.get(i).y);
                Point p2 = centeredToPanel(bezierPoints.get(i + 1).x, bezierPoints.get(i + 1).y);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
        System.out.println("[DEBUG] repaint triggered");

    }

    public void setDisplayedPoints(List<Point2D.Double> points) {
        this.displayPoints = points;
        repaint();
    }

    public Point panelToCentered(int x,int y){
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        return new Point(x - cx, cy - y);
    }

    public Point centeredToPanel(double x, double y){
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        return new Point((int)(x+cx),(int)(cy-y));
    }

    public void setPolyline(List<Point2D.Double> transformedPoints, boolean enable) {
        System.out.println("[DEBUG] setPolyline: " + (transformedPoints != null ? transformedPoints.size() : "null"));
        this.polylinePoints = transformedPoints;
        this.drawPolyline = enable;
        repaint();
    }

    public void setBezierCurve(List<Point2D.Double> points, boolean enable){
        this.bezierPoints=points;
        this.drawBezier=enable;
        repaint();
    }


}