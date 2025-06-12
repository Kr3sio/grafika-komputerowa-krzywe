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

    private final static Integer DEFAULT_WIDTH = 1200;
    private final static Integer DEFAULT_HEIGHT = 800;

    private final ImagePanel Panel;
    private final MenuBar menuBar;

    private final ImageController imageController;
    private final FileController fileController;

    private final TransformationModel transformationModel;

    private final PointListPanel pointListPanel;
    private final MatrixDisplayPanel matrixDisplayPanel;
    private final OperationPanel operationPanel;

    public MainFrame() {
        super("Grafika komputerowa - 3D");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        transformationModel = new TransformationModel();

        Panel = new ImagePanel("Scena 3D", transformationModel);
        menuBar = new MenuBar();

        pointListPanel = new PointListPanel();
        matrixDisplayPanel = new MatrixDisplayPanel();
        operationPanel = new OperationPanel();

        imageController = new ImageController(this, transformationModel);
        fileController = new FileController(this);
        TransformationController transformationController = new TransformationController(
                operationPanel, pointListPanel, matrixDisplayPanel, Panel, transformationModel, menuBar
        );

        add(Panel, BorderLayout.CENTER);

        add(pointListPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        bottomPanel.add(operationPanel);
        bottomPanel.add(Box.createHorizontalStrut(10));
        bottomPanel.add(matrixDisplayPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        setJMenuBar(menuBar);
        setMenuBarListeners();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ImagePanel getPanel() {
        return Panel;
    }

    public PointListPanel getPointListPanel() {
        return pointListPanel;
    }

    public MatrixDisplayPanel getMatrixDisplayPanel() {
        return matrixDisplayPanel;
    }


    public void adjustWindowSize() {
        var image = Panel.getModel();
        if (image == null || image.getImage() == null) {
            return;
        }
    }

    private void setMenuBarListeners() {
        menuBar.getOpenFileMenuItem().addActionListener(_ -> showFileChooserDialog());
        menuBar.getSaveFileMenuItem().addActionListener(_ -> showSaveFileDialog());
        menuBar.getExitMenuItem().addActionListener(_ -> System.exit(0));
        menuBar.getClearLeftPanelMenuItem().addActionListener(_ -> imageController.clearLeftPanel());

        menuBar.getObserverDistanceField().addActionListener(e -> {
            try {
                double distance = menuBar.getObserverDistance();
                Panel.setObserverDistance(distance);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowa wartość odległości obserwatora. Wprowadź liczbę.", "Błąd Wprowadzania", JOptionPane.ERROR_MESSAGE);
            }
        });

        // NOWE LISTENERY DLA OPCJI WIDOKU 3D
        menuBar.getVisibleOnlyFaceItem().addActionListener(e -> {
            JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
            Panel.setVisibleOnlyFace(source.isSelected());
        });

        menuBar.getOrthoProjectionItem().addActionListener(e -> {
            Panel.setOrthoProjectionSelected(true);
        });

        menuBar.getPerspectiveProjectionItem().addActionListener(e -> {
            Panel.setOrthoProjectionSelected(false);
        });
    }

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