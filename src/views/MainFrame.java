package views;

import controllers.Transformation3DController;
import models.Object3DModel;
import render.Object3DRenderer;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final Object3DModel model3D;
    private final Transformation3DPanel transformation3DPanel;
    private final JTextArea matrixArea;
    private final JPanel canvasPanel;

    public MainFrame() {
        super("Grafika 3D - Transformacje");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === MODEL ===
        model3D = new Object3DModel(); // macierz jednostkowa
        // Możesz załadować bryłę z pliku .mesh: model3D = Object3DModel.loadFromFile(...)

        // === PANEL RYSUJĄCY ===
        canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(Color.BLACK);
                g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
                g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

                Object3DRenderer renderer = new Object3DRenderer(getWidth(), getHeight());
                renderer.draw(g2, model3D);
            }
        };
        add(canvasPanel, BorderLayout.CENTER);

        // === PANEL TRANSFORMACJI ===
        transformation3DPanel = new Transformation3DPanel();
        add(transformation3DPanel, BorderLayout.SOUTH);

        // === POLE MACIERZY ===
        matrixArea = new JTextArea(4, 20);
        matrixArea.setEditable(false);
        JScrollPane matrixScroll = new JScrollPane(matrixArea);
        matrixScroll.setBorder(BorderFactory.createTitledBorder("Macierz przekształceń"));
        add(matrixScroll, BorderLayout.EAST);

        // === KONTROLER ===
        new Transformation3DController(model3D, transformation3DPanel, matrixArea, canvasPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
