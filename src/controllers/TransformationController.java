package controllers;

import models.TransformationModel;
import views.ImagePanel;
import views.TransformationPanel;
import views.MenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TransformationController {

    private final TransformationModel model;
    private final TransformationPanel transformationPanel;
    private final ImagePanel imagePanel;
    private final MenuBar menuBar;

    public TransformationController(TransformationPanel transformationPanel, ImagePanel imagePanel, TransformationModel model,MenuBar menuBar) {
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
                Point p = imagePanel.panelToCentered(e.getX(),e.getY());
                model.addPoint(p.getX(), p.getY());
                transformationPanel.addPointToList(String.format("(%.0f, %.0f)", p.getX(), p.getY()));
                updateMatrixAndList();

                if (menuBar.getShowPolylineMenuItem().isSelected()) {
                    List<Point2D.Double> transformed = model.getTransformedPoints();
                    imagePanel.setPolyline(transformed, true);
                }
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

        transformationPanel.getRemovePointButton().addActionListener(e -> {
            int[] selected = transformationPanel.getSelectedPointIndices();
            if (selected.length == 0) {
                showError("Wybierz punkt do usunięcia.");
                return;
            }

            // Usuwamy w odwrotnej kolejności, by nie przesuwać indeksów
            for (int i = selected.length - 1; i >= 0; i--) {
                model.removePoint(selected[i]);
            }

            updateMatrixAndList();
        });

    }

    private List<Integer> getSelectedIndices() {
        List<Integer> list = new ArrayList<>();

        if (menuBar.isTransformAllEnabled()) {
            // Transformujemy wszystkie punkty
            for (int i = 0; i < model.getPoints().size(); i++) {
                list.add(i);
            }
        } else {
            int[] indices = transformationPanel.getSelectedPointIndices();
            if (indices.length == 0) {
                showError("Wybierz przynajmniej jeden punkt z listy.");
                throw new IllegalStateException("Brak zaznaczonych punktów.");
            }
            for (int i : indices) list.add(i);
        }

        return list;
    }


    private void updateMatrixAndList() {
        transformationPanel.clearPointList();
        for (var pt : model.getPoints()) {
            transformationPanel.addPointToList(String.format("(%.0f, %.0f)", pt.getX(), pt.getY()));
        }
        transformationPanel.updateMatrixDisplay(model.getMatrixString());
        imagePanel.setDisplayedPoints(model.getPoints());

        // 🔄 Dodaj to: automatyczne odświeżenie łamanej
        if (menuBar.getShowPolylineMenuItem().isSelected()) {
            List<Point2D.Double> transformed = model.getTransformedPoints();
            imagePanel.setPolyline(transformed, true);
        }

        if(menuBar.getShowBezierCurveMenuItem().isSelected()){
            double step = transformationPanel.getBezierStep();
            List<Point2D.Double> bezier = model.calculateBezierPoints(step);
            imagePanel.setBezierCurve(bezier, true);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(transformationPanel, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

}
