package views;

import controllers.FileController;
import controllers.ImageController;
import controllers.TransformationController;
import models.TransformationModel;

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

    // Panele do wyświetlania obrazów/modeli
    private final ImagePanel Panel;

    // Pasek menu aplikacji
    private final MenuBar menuBar;

    // Kontrolery obsługujące obrazy oraz pliki
    private final ImageController imageController;
    private final FileController fileController;

    private final TransformationModel transformationModel;
    private final TransformationPanel transformationPanel;

    public MainFrame() {
        super("Grafika komputerowa - 3D");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicjalizacja komponentów
        transformationModel = new TransformationModel(); // TransformationModel musi być zainicjalizowany PRZED ImagePanel
        Panel = new ImagePanel("Scena 3D", transformationModel); // Zmieniono: przekazujemy transformationModel
        menuBar = new MenuBar();
        transformationPanel = new TransformationPanel();

        // Inicjalizacja kontrolerów
        imageController = new ImageController(this, transformationModel);
        fileController = new FileController(this);


        // Utworzenie kontenera do organizacji komponentów interfejsu użytkownika
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        contentPanel.add(Panel);
        add(contentPanel, BorderLayout.CENTER);

        // Panel transformacji
        TransformationController transformationController = new TransformationController(transformationPanel, Panel, transformationModel, menuBar);
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
     * Zwraca instancję TransformationPanel.
     * Jest potrzebna, aby ImageController mógł odświeżyć listę punktów
     * po załadowaniu modelu 3D z pliku.
     */
    public TransformationPanel getTransformationPanel() {
        return transformationPanel;
    }

    /**
     * Dostosowuje rozmiar okna do załadowanego obrazu z lewego panelu.
     * Na tym etapie funkcjonalność dostosowywania do obrazu nie będzie używana
     * dla obiektów 3D, ale metoda zostaje zachowana.
     */
    public void adjustWindowSize() {
        var image = Panel.getModel();
        if (image == null || image.getImage() == null) {
            return;
        }

        int newWidth = Math.max(getWidth(), image.getImage().getWidth() * 2 + 100);
        int newHeight = Math.max(getHeight(), image.getImage().getHeight() + 100);
        setSize(newWidth, newHeight);
        setLocationRelativeTo(null);
    }

    /**
     * Ustawia nasłuchiwacze dla elementów menu.
     */
    private void setMenuBarListeners() {
        menuBar.getOpenFileMenuItem().addActionListener(_ -> showFileChooserDialog());
        menuBar.getSaveFileMenuItem().addActionListener(_ -> showSaveFileDialog());
        menuBar.getExitMenuItem().addActionListener(_ -> System.exit(0));
        menuBar.getClearLeftPanelMenuItem().addActionListener(_ -> imageController.clearLeftPanel());
    }


    /**
     * Metoda otwiera okno dialogowe wyboru pliku graficznego lub modelu 3D.
     */
    private void showFileChooserDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Wybierz plik graficzny lub model 3D (.obj)");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Obrazy (BMP, PNG) i Modele 3D (OBJ)", "bmp", "png", "obj"));
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imageController.loadImage(file);
        }
    }

    /**
     * Metoda otwiera okno dialogowe zapisu pliku graficznego.
     * Nadal dotyczy zapisu obrazu 2D.
     */
    private void showSaveFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz obraz (na razie tylko 2D)");
        fileChooser.setFileFilter(new FileNameExtensionFilter("BMP & PNG Images", "bmp", "png"));
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileController.saveFile(file);
        }
    }
}