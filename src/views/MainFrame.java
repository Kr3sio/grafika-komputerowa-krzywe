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
import java.io.File;

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