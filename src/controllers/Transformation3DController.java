package controllers;

import models.Matrix4x4;
import models.Object3DModel;
import views.Transformation3DPanel;

import javax.swing.*;
import java.util.function.DoubleSupplier;

public class Transformation3DController {

    private final Object3DModel model;
    private final Transformation3DPanel panel;
    private final JTextArea matrixDisplay; // np. z `MainFrame` lub osobnego panelu

    public Transformation3DController(Object3DModel model, Transformation3DPanel panel, JTextArea matrixDisplay) {
        this.model = model;
        this.panel = panel;
        this.matrixDisplay = matrixDisplay;

        panel.getApplyButton().addActionListener(e -> applyTransformations());
    }

    private void applyTransformations() {
        double[][] t = Matrix4x4.translation(panel.getDx(), panel.getDy(), panel.getDz());
        double[][] s = Matrix4x4.scaling(panel.getSx(), panel.getSy(), panel.getSz());
        double[][] rx = Matrix4x4.rotationX(panel.getRx());
        double[][] ry = Matrix4x4.rotationY(panel.getRy());
        double[][] rz = Matrix4x4.rotationZ(panel.getRz());

        // Kolejność: rotacje -> skalowanie -> translacja
        double[][] composed = Matrix4x4.multiply(t, Matrix4x4.multiply(s, Matrix4x4.multiply(rz, Matrix4x4.multiply(ry, rx))));

        model.applyTransformation(composed);
        updateMatrixDisplay();
    }

    private void updateMatrixDisplay() {
        StringBuilder sb = new StringBuilder();
        double[][] m = model.getTransformMatrix();
        for (double[] row : m) {
            for (double val : row) {
                sb.append(String.format("%8.3f ", val));
            }
            sb.append("\n");
        }
        matrixDisplay.setText(sb.toString());
    }
}

