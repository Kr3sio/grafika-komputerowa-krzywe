package controllers;

import models.Matrix4x4;
import models.Point3D;
import models.TransformationModel;
import views.ImagePanel;
import views.TransformationPanel;
import views.MenuBar;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TransformationController {

    private final TransformationModel model;
    private final TransformationPanel transformationPanel;
    private final ImagePanel imagePanel;
    private final MenuBar menuBar;

    public TransformationController(TransformationPanel transformationPanel, ImagePanel imagePanel, TransformationModel model, MenuBar menuBar) {
        this.model = model;
        this.menuBar = menuBar;
        this.transformationPanel = transformationPanel;
        this.imagePanel = imagePanel;
        setupListeners();
    }


    private void setupListeners() {
        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point3D p = imagePanel.panelToCentered3D(e.getX(), e.getY());
                model.addPoint(p.getX(), p.getY(), p.getZ());
                transformationPanel.refreshPointsList(model.getPoints());
                imagePanel.repaint();
            }
        });

        // --- Zmienione listenery dla obrotu ---
        transformationPanel.getRotateXButton().addActionListener(e -> {
            try {
                double angle = Double.parseDouble(transformationPanel.getRotateXValue());
                Matrix4x4 rotationMatrix = Matrix4x4.rotationX(angle); // Obrót wokół X
                model.applyTransformation(rotationMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowy kąt obrotu dla osi X.");
            }
        });

        transformationPanel.getRotateYButton().addActionListener(e -> {
            try {
                double angle = Double.parseDouble(transformationPanel.getRotateYValue());
                Matrix4x4 rotationMatrix = Matrix4x4.rotationY(angle); // Obrót wokół Y
                model.applyTransformation(rotationMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowy kąt obrotu dla osi Y.");
            }
        });

        transformationPanel.getRotateZButton().addActionListener(e -> {
            try {
                double angle = Double.parseDouble(transformationPanel.getRotateZValue());
                Matrix4x4 rotationMatrix = Matrix4x4.rotationZ(angle); // Obrót wokół Z
                model.applyTransformation(rotationMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowy kąt obrotu dla osi Z.");
            }
        });
        // --- Koniec zmienionych listenerów dla obrotu ---


        transformationPanel.getScaleButton().addActionListener(e -> {
            try {
                double sx = Double.parseDouble(transformationPanel.getScaleXValue());
                double sy = Double.parseDouble(transformationPanel.getScaleYValue());
                double sz = Double.parseDouble(transformationPanel.getScaleZValue()); // NOWE: Używamy sz

                Matrix4x4 scaleMatrix = Matrix4x4.scaling(sx, sy, sz);

                model.applyTransformation(scaleMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowe wartości skalowania.");
            }
        });

        transformationPanel.getTranslateButton().addActionListener(e -> {
            try {
                double dx = Double.parseDouble(transformationPanel.getTranslateXValue());
                double dy = Double.parseDouble(transformationPanel.getTranslateYValue());
                double dz = Double.parseDouble(transformationPanel.getTranslateZValue()); // NOWE: Używamy dz

                Matrix4x4 translateMatrix = Matrix4x4.translation(dx, dy, dz);

                model.applyTransformation(translateMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowa wartość przesunięcia.");
            }
        });

        transformationPanel.getRemovePointButton().addActionListener(e -> {
            int[] selected = transformationPanel.getSelectedPointIndices();
            if (selected.length == 0) {
                showError("Wybierz punkt do usunięcia.");
                return;
            }

            for (int i = selected.length - 1; i >= 0; i--) {
                model.removePoint(selected[i]);
            }
            updateDisplay();
        });
    }

    private void updateDisplay() {
        transformationPanel.updateMatrixDisplay(model.getMatrixString());
        transformationPanel.refreshPointsList(model.getPoints());
        imagePanel.repaint();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(transformationPanel, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }
}