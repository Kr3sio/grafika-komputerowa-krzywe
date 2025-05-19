package controllers;

import models.TransformationModel;
import views.ImagePanel;
import views.TransformationPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class TransformationController {

    private final TransformationModel model;
    private final TransformationPanel transformationPanel;
    private final ImagePanel imagePanel;

    public TransformationController(TransformationPanel transformationPanel, ImagePanel imagePanel) {
        this.model = new TransformationModel();
        this.transformationPanel = transformationPanel;
        this.imagePanel = imagePanel;

        setupListeners();
    }

    private void setupListeners() {
        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = imagePanel.panelToCentered(e.getX(),e.getY());
                model.addPoint(p.getX(), p.getY());
                transformationPanel.addPointToList(String.format("(%.0f, %.0f)", p.getX(), p.getY()));
                updateMatrixAndList();
            }
        });
        transformationPanel.getRotateButton().addActionListener(e -> {
            try {
                List<Integer> indices = getSelectedIndices();
                double angle = Double.parseDouble(transformationPanel.getRotateValue());
                AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle));
                model.transformPoints(indices, at);
                updateMatrixAndList();
            }catch (NumberFormatException ex){
                showError("Nieprawidłowy kąt obrotu.");
            }
        });
        transformationPanel.getScaleButton().addActionListener(e -> {
            try{
                List<Integer> indices = getSelectedIndices();
                double sx = Double.parseDouble(transformationPanel.getScaleXValue());
                double sy = Double.parseDouble(transformationPanel.getScaleYValue());
                AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
                model.transformPoints(indices, at);
                updateMatrixAndList();
            }catch (NumberFormatException ex){
                showError("Nieprawidłowe wartości skalowania.");
            }
        });
        transformationPanel.getTranslateButton().addActionListener(e -> {
            try {
                List<Integer> indices = getSelectedIndices();
                double dx = Double.parseDouble(transformationPanel.getTranslateXValue());
                double dy = Double.parseDouble(transformationPanel.getTranslateYValue());
                AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
                model.transformPoints(indices, at);
                updateMatrixAndList();
            }catch (NumberFormatException ex){
                showError("Nieprawidłowa wartość przesunięcia.");
            }
        });
    }

    private List<Integer> getSelectedIndices() {
        int[] indices = transformationPanel.getSelectedPointIndices();
        if (indices.length == 0) {
            showError("Wybierz przynajmniej jeden punkt z listy.");
            throw new IllegalStateException("Brak zaznaczonych punktów.");
        }
        List<Integer> list = new ArrayList<>();
        for (int i : indices) list.add(i);
        return list;
    }

    private void updateMatrixAndList() {
        transformationPanel.clearPointList();
        for (var pt : model.getPoints()) {
            transformationPanel.addPointToList(String.format("(%.0f, %.0f)", pt.getX(), pt.getY()));
        }
        transformationPanel.updateMatrixDisplay(model.getMatrixString());
        imagePanel.setDisplayedPoints(model.getPoints());
    }

    private void showError(String message) {
        javax.swing.JOptionPane.showMessageDialog(transformationPanel, message,"Błąd",javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
