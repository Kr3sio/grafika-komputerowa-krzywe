package controllers;

import models.Matrix4x4;
import models.Object3DModel;
import views.Transformation3DPanel;

import javax.swing.*;

public class Transformation3DController {

    private final Object3DModel model;
    private final Transformation3DPanel panel;
    private final JTextArea matrixDisplay;
    private final JComponent renderTarget;

    public Transformation3DController(Object3DModel model, Transformation3DPanel panel,
                                      JTextArea matrixDisplay, JComponent renderTarget) {
        this.model = model;
        this.panel = panel;
        this.matrixDisplay = matrixDisplay;
        this.renderTarget = renderTarget;

        panel.getApplyButton().addActionListener(e -> applyTransformations());
    }

    private void applyTransformations() {
        double[][] t = Matrix4x4.translation(panel.getDx(), panel.getDy(), panel.getDz());
        double[][] s = Matrix4x4.scaling(panel.getSx(), panel.getSy(), panel.getSz());
        double[][] rx = Matrix4x4.rotationX(panel.getRx());
        double[][] ry = Matrix4x4.rotationY(panel.getRy());
        double[][] rz = Matrix4x4.rotationZ(panel.getRz());

        // Kolejność: rotacje → skalowanie → przesunięcie
        double[][] composed =
                Matrix4x4.multiply(t,
                        Matrix4x4.multiply(s,
                                Matrix4x4.multiply(rz,
                                        Matrix4x4.multiply(ry, rx))));

        model.applyTransformation(composed);
        updateMatrixDisplay();
        renderTarget.repaint(); // wymusza przerysowanie modelu
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
