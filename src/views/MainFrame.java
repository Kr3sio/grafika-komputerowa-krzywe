package views;

import controllers.FileController;
import controllers.ImageController;
import controllers.TransformationController;
import models.TransformationModel;
import  views.TransformationPanel;



import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    // Domyślne wymiary okna aplikacji
    private final static Integer DEFAULT_WIDTH = 800;
    private final static Integer DEFAULT_HEIGHT = 600;

    // Panele do wyświetlania obrazów
    private final ImagePanel Panel;


    // Pasek menu aplikacji
    private final MenuBar menuBar;

    // Kontrolery obsługujące obrazy oraz pliki
    private final ImageController imageController;
    private final FileController fileController;

    private final TransformationModel transformationModel;
    private final TransformationPanel transformationPanel;


    public MainFrame() {
        super("Grafika komputerowa");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicjalizacja komponentów
        Panel = new ImagePanel("Obraz wczytany");
        menuBar = new MenuBar();
        transformationPanel = new TransformationPanel();
        transformationModel = new TransformationModel();
        // Inicjalizacja kontrolerów
        imageController = new ImageController(this,transformationModel);
        fileController = new FileController(this);


        // Utworzenie kontenera do organizacji komponentów interfejsu użytkownika, ustawiamy siatkę 1x2 dla 2 paneli z obrazami
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        contentPanel.add(Panel);
        add(contentPanel, BorderLayout.CENTER);

        // Panel tranformacji

        TransformationController transformationController = new TransformationController(transformationPanel,Panel,transformationModel,menuBar);
        add(transformationPanel, BorderLayout.SOUTH);

        // Pasek menu
        setJMenuBar(menuBar); // Dodanie menu do okna
        setMenuBarListeners(); // Ustawienie nasłuchu na zdarzenia wywołania opcji z menubar

        setLocationRelativeTo(null); // Centrowanie okna na ekranie
        setVisible(true); // Ustawienie widoczności okna
    }



    public ImagePanel getPanel() {
        return Panel;
    }

    /**
     * Dostosowuje rozmiar okna do załadowanego obrazu z lewego panelu.
     *
     * <p>
     * Metoda pobiera obraz z lewego panelu i sprawdza jego rozmiary.
     * Szerokość i wysokość okna są zwiększane w zależności od wymiarów obrazu
     * </p>
     *
     * @see javax.swing.JFrame#setSize(int, int)
     * @see javax.swing.JFrame#setLocationRelativeTo(java.awt.Component)
     */
    public void adjustWindowSize() {
        var image = Panel.getModel().getImage();
        if (image == null) {
            return;
        }

        // Obliczenie nowej szerokości okna.
        // Okno powinno mieć co najmniej dwukrotność szerokości obrazu.
        // + 100 - dodaje dodatkową przestrzeń dla marginesów.
        int newWidth = Math.max(getWidth(), image.getWidth() * 2 + 100);

        // Obliczenie nowej wysokości okna.
        // + 100 - dodaje dodatkową przestrzeń dla marginesów.
        int newHeight = Math.max(getHeight(), image.getHeight() + 100);
        setSize(newWidth, newHeight);
        setLocationRelativeTo(null);
    }

    /**
     * @see java.awt.event.ActionListener
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     * @see javax.swing.AbstractButton#addActionListener(ActionListener)
     */
    private void setMenuBarListeners() {
        menuBar.getOpenFileMenuItem().addActionListener(_ -> showFileChooserDialog());
        menuBar.getSaveFileMenuItem().addActionListener(_ -> showSaveFileDialog());
        menuBar.getExitMenuItem().addActionListener(_ -> System.exit(0));
        menuBar.getClearLeftPanelMenuItem().addActionListener(_ -> imageController.clearLeftPanel());
        menuBar.getShowPolylineMenuItem().addActionListener(_->updatePolylineVisibility());
        menuBar.getShowBezierCurveMenuItem().addActionListener(_->updateCurveVisibility());
        menuBar.getClearCurveMenuItem().addActionListener(_->clearCurve());
        menuBar.getGenerateTextCurveMenuItem().addActionListener(_->showTextInputDialog());

    }


    /**
     * Metoda otwiera okno dialogowe wyboru pliku graficznego.
     *
     * <p>Tworzy instancję {@code JFileChooser}, umożliwiając użytkownikowi wybór pliku.
     * Przekazuje plik do kontrolera {@code ImageController} w celu załadowania zdjęcia do lewego panelu graficznego.</p>
     *
     * @see JFileChooser
     * @see ImageController#loadImage(File)
     */
    private void showFileChooserDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Wybierz plik graficzny");
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imageController.loadImage(file);
        }
    }

    private void updatePolylineVisibility() {
        System.out.println("[DEBUG] updatePolylineVisibility() called");

        if (imageController.getModel() == null) {
            System.out.println("[DEBUG] model is null!");
            return;
        }

        var transformed = imageController.getModel().getTransformedPoints();
        System.out.println("[DEBUG] transformed points: " + transformed.size());

        boolean showLine = menuBar.getShowPolylineMenuItem().isSelected();
        Panel.setPolyline(transformed, showLine);
    }

    private void updateCurveVisibility() {
        boolean showBezier = menuBar.getShowBezierCurveMenuItem().isSelected();
        double step = transformationPanel.getBezierStep();
        Panel.setBezierCurve(transformationModel.calculateBezierPoints(step), showBezier);
    }

    private void clearCurve() {
        transformationModel.clearPoints();
        transformationPanel.clearPointList();
        Panel.setDisplayedPoints(null);
        Panel.setPolyline(null,false);
        Panel.setBezierCurve(null,false);

        transformationPanel.updateMatrixDisplay(transformationModel.getMatrixString());
    }

    private void showTextInputDialog() {
        JDialog dialog = new JDialog(this, "Wygeneruj krzywą z tekstu", true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel tekstowy
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textPanel.add(new JLabel("Tekst:"));
        JTextField textField = new JTextField(15);
        textPanel.add(textField);

        // Panel dokładności
        JPanel stepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stepPanel.add(new JLabel("Dokładność (np. 0.01):"));
        JTextField stepField = new JTextField("0.01", 6);
        stepPanel.add(stepField);

        // Panel checkboxa
        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox showPointsCheckBox = new JCheckBox("Wyświetl punkty kontrolne", true); // domyślnie zaznaczone
        checkPanel.add(showPointsCheckBox);

        // Panel przycisku
        JPanel buttonPanel = new JPanel();
        JButton generateButton = new JButton("Generuj");
        buttonPanel.add(generateButton);

        // Dodanie paneli
        dialog.add(textPanel);
        dialog.add(stepPanel);
        dialog.add(checkPanel);
        dialog.add(buttonPanel);

        generateButton.addActionListener(e -> {
            String text = textField.getText();
            double step;
            boolean showPoints = showPointsCheckBox.isSelected();

            try {
                step = Double.parseDouble(stepField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Nieprawidłowa dokładność.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            generateBezierFromText(text, step, showPoints);
            dialog.dispose();
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void generateBezierFromText(String text, double step, boolean showPoints) {
        transformationModel.clearPoints();
        transformationPanel.clearPointList();

        Font font = new Font("Arial", Font.PLAIN, 100);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        GlyphVector gv = font.createGlyphVector(frc, text);

        Shape shape = gv.getOutline();
        PathIterator pi = shape.getPathIterator(null, 1.0);

        double[] coords = new double[6];
        Point2D.Double currentPoint = new Point2D.Double();
        List<Point2D.Double> bezierCurvePoints = new ArrayList<>();

        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);

            switch (type) {
                case PathIterator.SEG_MOVETO -> {
                    currentPoint = new Point2D.Double(coords[0], -coords[1]);
                }

                case PathIterator.SEG_CUBICTO -> {
                    Point2D.Double ctrl1 = new Point2D.Double(coords[0], -coords[1]);
                    Point2D.Double ctrl2 = new Point2D.Double(coords[2], -coords[3]);
                    Point2D.Double end = new Point2D.Double(coords[4], -coords[5]);

                    List<Point2D.Double> controlPoints = List.of(currentPoint, ctrl1, ctrl2, end);
                    bezierCurvePoints.addAll(sampleBezier(controlPoints, step));

                    currentPoint = end;
                }

                case PathIterator.SEG_LINETO -> {
                    //  Konwersja linii na „fałszywą” krzywą Béziera
                    Point2D.Double end = new Point2D.Double(coords[0], -coords[1]);

                    List<Point2D.Double> pseudoCurve = List.of(
                            currentPoint,
                            currentPoint,
                            end,
                            end
                    );
                    bezierCurvePoints.addAll(sampleBezier(pseudoCurve, step));
                    currentPoint = end;
                }
            }
            pi.next();
        }

        // Wyświetlenie punktów kontrolnych
        if (showPoints) {
            for (var pt : bezierCurvePoints) {
                transformationModel.addPoint(pt.x, pt.y);
                transformationPanel.addPointToList(String.format("(%.0f, %.0f)", pt.getX(), pt.getY()));
            }
            Panel.setDisplayedPoints(transformationModel.getPoints());
        } else {
            Panel.setDisplayedPoints(null);
        }

        transformationPanel.updateMatrixDisplay(transformationModel.getMatrixString());
        Panel.setBezierCurve(bezierCurvePoints, true);
    }


    private List<Point2D.Double> sampleBezier(List<Point2D.Double> ctrlPoints, double step) {
        List<Point2D.Double> result = new ArrayList<>();
        for (double t = 0.0; t <= 1.0; t += step) {
            result.add(bezierPoint(ctrlPoints, t));
        }
        result.add(bezierPoint(ctrlPoints, 1.0));
        return result;
    }


    private Point2D.Double bezierPoint(List<Point2D.Double> ctrlPoints, double t) {
        int n = ctrlPoints.size() - 1;
        double x = 0;
        double y = 0;

        for (int i = 0; i <= n; i++) {
            double binomial = binomialCoefficient(n, i);
            double coefficient = binomial * Math.pow(1 - t, n - i) * Math.pow(t, i);
            x += coefficient * ctrlPoints.get(i).x;
            y += coefficient * ctrlPoints.get(i).y;
        }

        return new Point2D.Double(x, y);
    }

    private double binomialCoefficient(int n, int k) {
        double result = 1;
        for (int i = 1; i <= k; i++) {
            result *= (n - (k - i));
            result /= i;
        }
        return result;
    }





    /**
     * Metoda otwiera okno dialogowe zapisu pliku graficznego.
     *
     * <p>
     * Tworzy instancję {@code JFileChooser}, ustawiając filtr plików obsługujący formaty BMP i PNG.
     * Po wybraniu miejscu zapisu przekazuje plik do kontrolera {@code FileController} w celu zapisania obrazu z prawego panelu graficznego.
     * </p>
     *
     * @see JFileChooser
     * @see FileController#saveFile(File)
     */
    private void showSaveFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz obraz");
        fileChooser.setFileFilter(new FileNameExtensionFilter("BMP & PNG Images", "bmp", "png"));
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileController.saveFile(file);
        }
    }
}