package controllers;

import models.Matrix4x4;
import models.Point3D;
import models.TransformationModel;
import views.ImagePanel;
import views.PointListPanel;
import views.MatrixDisplayPanel;
import views.OperationPanel;
import views.MenuBar;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TransformationController {

    private final TransformationModel model;
    private final OperationPanel operationPanel;
    private final PointListPanel pointListPanel;
    private final MatrixDisplayPanel matrixDisplayPanel;
    private final ImagePanel imagePanel;
    private final MenuBar menuBar;

    public TransformationController(OperationPanel operationPanel, PointListPanel pointListPanel,
                                    MatrixDisplayPanel matrixDisplayPanel, ImagePanel imagePanel,
                                    TransformationModel model, MenuBar menuBar) {
        this.model = model;
        this.menuBar = menuBar;
        this.operationPanel = operationPanel;
        this.pointListPanel = pointListPanel;
        this.matrixDisplayPanel = matrixDisplayPanel;
        this.imagePanel = imagePanel;
        setupListeners();
    }


    private void setupListeners() {
        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point3D p = imagePanel.panelToCentered3D(e.getX(), e.getY());
                model.addPoint(p.getX(), p.getY(), p.getZ());
                pointListPanel.refreshPointsList(model.getPoints());
                imagePanel.repaint();
            }
        });

        operationPanel.getRotateXButton().addActionListener(e -> {
            try {
                double angle = Double.parseDouble(operationPanel.getRotateXValue());
                Matrix4x4 rotationMatrix = Matrix4x4.rotationX(angle);
                model.applyTransformation(rotationMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowy kąt obrotu dla osi X.");
            }
        });

        operationPanel.getRotateYButton().addActionListener(e -> {
            try {
                double angle = Double.parseDouble(operationPanel.getRotateYValue());
                Matrix4x4 rotationMatrix = Matrix4x4.rotationY(angle);
                model.applyTransformation(rotationMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowy kąt obrotu dla osi Y.");
            }
        });

        operationPanel.getRotateZButton().addActionListener(e -> {
            try {
                double angle = Double.parseDouble(operationPanel.getRotateZValue());
                Matrix4x4 rotationMatrix = Matrix4x4.rotationZ(angle);
                model.applyTransformation(rotationMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowy kąt obrotu dla osi Z.");
            }
        });

        operationPanel.getScaleButton().addActionListener(e -> {
            try {
                double sx = Double.parseDouble(operationPanel.getScaleXValue());
                double sy = Double.parseDouble(operationPanel.getScaleYValue());
                double sz = Double.parseDouble(operationPanel.getScaleZValue());

                Matrix4x4 scaleMatrix = Matrix4x4.scaling(sx, sy, sz);

                model.applyTransformation(scaleMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowe wartości skalowania.");
            }
        });

        operationPanel.getTranslateButton().addActionListener(e -> {
            try {
                double dx = Double.parseDouble(operationPanel.getTranslateXValue());
                double dy = Double.parseDouble(operationPanel.getTranslateYValue());
                double dz = Double.parseDouble(operationPanel.getTranslateZValue());

                Matrix4x4 translateMatrix = Matrix4x4.translation(dx, dy, dz);

                model.applyTransformation(translateMatrix);
                updateDisplay();
            } catch (NumberFormatException ex) {
                showError("Nieprawidłowa wartość przesunięcia.");
            }
        });

        // UWAGA: Usunięto listener dla changeDistanceButton, ponieważ ta funkcjonalność została przeniesiona.
        // operationPanel.getChangeDistanceButton().addActionListener(e -> { ... });


        pointListPanel.getRemovePointButton().addActionListener(e -> {
            int[] selected = pointListPanel.getSelectedPointIndices();
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
        matrixDisplayPanel.updateMatrixDisplay(model.getMatrixString());
        pointListPanel.refreshPointsList(model.getPoints());
        imagePanel.repaint();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }
}