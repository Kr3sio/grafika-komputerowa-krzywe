package views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class OperationPanel extends JPanel {
    // Pola tekstowe dla transformacji
    private final JTextField rotateXField = new JTextField("0", 5);
    private final JTextField rotateYField = new JTextField("0", 5);
    private final JTextField rotateZField = new JTextField("0", 5);

    private final JTextField scaleXField = new JTextField("1.0", 5);
    private final JTextField scaleYField = new JTextField("1.0", 5);
    private final JTextField scaleZField = new JTextField("1.0", 5);

    private final JTextField translateXField = new JTextField("0", 5);
    private final JTextField translateYField = new JTextField("0", 5);
    private final JTextField translateZField = new JTextField("0", 5);


    // Przyciski
    private final JButton rotateXButton;
    private final JButton rotateYButton;
    private final JButton rotateZButton;

    private final JButton scaleButton;
    private final JButton translateButton;

    public OperationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setBorder(BorderFactory.createTitledBorder("Operacje na obiekcie"));

        // 1. Grupa dla rotacji
        JPanel rotateGroup = new JPanel(new GridLayout(3, 3, 5, 2));
        rotateGroup.setBorder(BorderFactory.createTitledBorder("Obrót (kąt)"));
        rotateXButton = new JButton("Obróć X");
        rotateYButton = new JButton("Obróć Y");
        rotateZButton = new JButton("Obróć Z");

        rotateGroup.add(new JLabel("Oś X:"));
        rotateGroup.add(rotateXField);
        rotateGroup.add(rotateXButton);

        rotateGroup.add(new JLabel("Oś Y:"));
        rotateGroup.add(rotateYField);
        rotateGroup.add(rotateYButton);

        rotateGroup.add(new JLabel("Oś Z:"));
        rotateGroup.add(rotateZField);
        rotateGroup.add(rotateZButton);
        add(rotateGroup);

        // 2. Grupa dla skalowania
        JPanel scaleGroup = new JPanel(new GridLayout(4, 2, 5, 2));
        scaleGroup.setBorder(BorderFactory.createTitledBorder("Skalowanie"));
        scaleButton = new JButton("Skaluj");

        scaleGroup.add(new JLabel("Skala X:"));
        scaleGroup.add(scaleXField);
        scaleGroup.add(new JLabel("Skala Y:"));
        scaleGroup.add(scaleYField);
        scaleGroup.add(new JLabel("Skala Z:"));
        scaleGroup.add(scaleZField);
        scaleGroup.add(new JLabel(""));
        scaleGroup.add(scaleButton);
        add(scaleGroup);

        // 3. Grupa dla przesunięcia
        JPanel translateGroup = new JPanel(new GridLayout(4, 2, 5, 2));
        translateGroup.setBorder(BorderFactory.createTitledBorder("Przesunięcie"));
        translateButton = new JButton("Przesuń");

        translateGroup.add(new JLabel("Przesunięcie X:"));
        translateGroup.add(translateXField);
        translateGroup.add(new JLabel("Przesunięcie Y:"));
        translateGroup.add(translateYField);
        translateGroup.add(new JLabel("Przesunięcie Z:"));
        translateGroup.add(translateZField);
        translateGroup.add(new JLabel(""));
        translateGroup.add(translateButton);
        add(translateGroup);

        // UWAGA: Usunięto grupę "Zmiana odległości", ponieważ została przeniesiona do MenuBar

        setPreferredSize(new Dimension(1000, 160));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
    }

    // --- Gettery dla pól i przycisków ---
    public String getRotateXValue() { return rotateXField.getText(); }
    public String getRotateYValue() { return rotateYField.getText(); }
    public String getRotateZValue() { return rotateZField.getText(); }

    public JButton getRotateXButton() { return rotateXButton; }
    public JButton getRotateYButton() { return rotateYButton; }
    public JButton getRotateZButton() { return rotateZButton; }

    public String getScaleXValue(){return scaleXField.getText();}
    public String getScaleYValue(){return scaleYField.getText();}
    public String getScaleZValue() { return scaleZField.getText(); }
    public JButton getScaleButton() {return  scaleButton;}

    public String getTranslateXValue(){return translateXField.getText();}
    public String getTranslateYValue(){return translateYField.getText();}
    public String getTranslateZValue() { return translateZField.getText(); }
    public JButton getTranslateButton() {return  translateButton;}

    // UWAGA: Usunięto gettery dla pól i przycisków zmiany odległości
}