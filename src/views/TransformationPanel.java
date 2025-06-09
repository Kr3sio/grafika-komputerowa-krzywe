package views;

import models.Point3D; // Import Point3D
import javax.swing.*;
import java.awt.*;
import java.util.List; // Import List

public class TransformationPanel extends JPanel {
    private final DefaultListModel<String> pointListModel;
    private final JList<String> pointList;

    private final JTextField rotateField = new JTextField("90",8);
    private final JTextField scaleXField = new JTextField("1.5",8);
    private final JTextField scaleYField = new JTextField("1.5",8);
    private final JTextField translateXField = new JTextField("1",8);
    private final JTextField translateYField = new JTextField("1",8);

    private final JButton rotateButton;
    private final JButton scaleButton;
    private final JButton translateButton;
    // Usunięto bezierStepField
    // private final JTextField bezierStepField;

    private final JButton removePointButton;

    private final JTextArea matrixDisplay;

    public TransformationPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Transformacja i Punkty"));

        pointListModel = new DefaultListModel<>();
        pointList = new JList<>(pointListModel);
        pointList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScroll = new JScrollPane(pointList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Punkty wiodące"));

        JPanel operationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,2,4,2);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operacje"));

        rotateButton = new JButton("Obróć");
        scaleButton = new JButton("Skaluj");
        translateButton = new JButton("Przesuń");
        removePointButton = new JButton("Usuń punkt");

        // Kąt obrotu
        gbc.gridx = 0; gbc.gridy = 0;
        operationPanel.add(new JLabel("Kąt obrotu:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        operationPanel.add(rotateField, gbc);
        gbc.gridx = 2; gbc.gridy = 0;
        operationPanel.add(rotateButton, gbc);

        // Skalowanie
        gbc.gridx = 0; gbc.gridy = 1;
        operationPanel.add(new JLabel("Skala X:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        operationPanel.add(scaleXField, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        operationPanel.add(new JLabel("Skala Y:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        operationPanel.add(scaleYField, gbc);
        gbc.gridx = 2; gbc.gridy = 2;
        operationPanel.add(scaleButton, gbc);

        // Przesunięcie
        gbc.gridx = 0; gbc.gridy = 3;
        operationPanel.add(new JLabel("Przesunięcie X:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        operationPanel.add(translateXField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        operationPanel.add(new JLabel("Przesunięcie Y:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        operationPanel.add(translateYField, gbc);
        gbc.gridx = 2; gbc.gridy = 4;
        operationPanel.add(translateButton, gbc);

        matrixDisplay = new JTextArea(4, 20);
        matrixDisplay.setEditable(false);
        JScrollPane matrixScroll = new JScrollPane(matrixDisplay);
        matrixScroll.setBorder(BorderFactory.createTitledBorder("Macierz przekształceń"));

        // Usunięto panel Beziera
        // JPanel bezierPanel = new JPanel(new BorderLayout());
        // bezierPanel.setBorder(BorderFactory.createTitledBorder("Krzywa Beziera"));
        // bezierStepField = new JTextField("0.01",10);
        // bezierPanel.add(new JLabel("Dokładność:"));
        // bezierPanel.add(bezierStepField);

        JPanel rightSide = new JPanel(new BorderLayout());
        JPanel rightSideup = new JPanel(new BorderLayout());
        JPanel rightSidedown = new JPanel(new BorderLayout());
        add(operationPanel, BorderLayout.CENTER);
        rightSideup.add(matrixScroll);
        // Zmieniono, aby nie dodawać bezierPanel
        rightSidedown.add(removePointButton,BorderLayout.WEST); // removePointButton przeniesiony, aby był widoczny

        rightSide.add(rightSideup,BorderLayout.NORTH);
        rightSide.add(rightSidedown,BorderLayout.SOUTH);

        add(listScroll, BorderLayout.WEST);
        add(rightSide, BorderLayout.EAST);
    }

    public int[] getSelectedPointIndices() {
        return pointList.getSelectedIndices();
    }

    public JButton getRotateButton() {return  rotateButton;}
    public JButton getScaleButton() {return  scaleButton;}
    public JButton getTranslateButton() {return  translateButton;}
    public String getRotateValue(){return rotateField.getText();}
    public String getScaleXValue(){return scaleXField.getText();}
    public String getScaleYValue(){return scaleYField.getText();}
    public String getTranslateXValue(){return translateXField.getText();}
    public String getTranslateYValue(){return translateYField.getText();}
    public void updateMatrixDisplay(String matrixText){matrixDisplay.setText(matrixText);}
    public void addPointToList(String point){pointListModel.addElement(point);}
    public void clearPointList(){pointListModel.clear();}

    // Usunięto getBezierStep
    // public double getBezierStep() { ... }

    public JButton getRemovePointButton() {
        return removePointButton;
    }

    /**
     * Odświeża listę wyświetlanych punktów w panelu.
     * @param points Lista punktów 3D do wyświetlenia.
     */
    public void refreshPointsList(List<Point3D> points) {
        pointListModel.clear();
        for (Point3D p : points) {
            pointListModel.addElement(String.format("(%.0f, %.0f, %.0f)", p.getX(), p.getY(), p.getZ()));
        }
    }
}