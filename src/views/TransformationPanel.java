package views;

import javax.swing.*;
import java.awt.*;


public class TransformationPanel extends JPanel {
    private final DefaultListModel<String> pointListModel;
    private final JList<String> pointList;

    private final JTextField rotateField;
    private final JTextField scaleXField;
    private final JTextField scaleYField;
    private final JTextField translateXField;
    private final JTextField translateYField;

    private final JButton rotateButton;
    private final JButton scaleButton;
    private final JButton translateButton;

    private final JTextArea matrixDisplay;
    public TransformationPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Transformacja i Punkty"));

        pointListModel = new DefaultListModel<>();
        pointList = new JList<>(pointListModel);
        JScrollPane listScroll = new JScrollPane(pointList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Punkty wiodące"));


        JPanel operationPanel = new JPanel(new GridLayout(5,2,5,5));
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operacje"));

        rotateField = new JTextField();
        scaleXField = new JTextField();
        scaleYField = new JTextField();
        translateXField = new JTextField();
        translateYField = new JTextField();

        rotateButton = new JButton("Obróć");
        scaleButton = new JButton("Skaluj");
        translateButton = new JButton("Przesuń");

        operationPanel.add(new JLabel("Skala X:"));
        operationPanel.add(scaleXField);
        operationPanel.add(new JLabel("Skala Y:"));
        operationPanel.add(scaleYField);
        operationPanel.add(scaleButton);

        operationPanel.add(new JLabel("Przesunięcie X:"));
        operationPanel.add(translateXField);
        operationPanel.add(new JLabel("Przesunięcie Y:"));
        operationPanel.add(translateYField);
        operationPanel.add(translateButton);

        matrixDisplay = new JTextArea(4, 20);
        matrixDisplay.setEditable(false);
        JScrollPane matrixScroll = new JScrollPane(matrixDisplay);
        matrixScroll.setBorder(BorderFactory.createTitledBorder("Macierz przekształceń"));

        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.add(operationPanel, BorderLayout.NORTH);
        rightSide.add(matrixScroll, BorderLayout.CENTER);

        add(listScroll, BorderLayout.WEST);
        add(rightSide, BorderLayout.CENTER);
    }
    public int getSelectedPointIndex() {
        return pointList.getSelectedIndex();
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

}
