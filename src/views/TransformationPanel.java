package views;

import models.Point3D;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TransformationPanel extends JPanel {
    private final DefaultListModel<String> pointListModel;
    private final JList<String> pointList;

    // Pola tekstowe dla transformacji
    private final JTextField rotateXField = new JTextField("0", 5); // Domyślnie 0 stopni
    private final JTextField rotateYField = new JTextField("0", 5); // Domyślnie 0 stopni
    private final JTextField rotateZField = new JTextField("0", 5); // Domyślnie 0 stopni (było 90)

    private final JTextField scaleXField = new JTextField("1.0", 5); // Domyślnie 1.0
    private final JTextField scaleYField = new JTextField("1.0", 5); // Domyślnie 1.0
    private final JTextField scaleZField = new JTextField("1.0", 5); // NOWE: Skalowanie Z

    private final JTextField translateXField = new JTextField("0", 5); // Domyślnie 0
    private final JTextField translateYField = new JTextField("0", 5); // Domyślnie 0
    private final JTextField translateZField = new JTextField("0", 5); // NOWE: Przesunięcie Z

    // Przyciski
    private final JButton rotateXButton; // NOWE
    private final JButton rotateYButton; // NOWE
    private final JButton rotateZButton; // NOWE (zastępuje rotateButton)

    private final JButton scaleButton;
    private final JButton translateButton;
    private final JButton removePointButton; // Nadal używany do punktów klikanych myszą

    private final JTextArea matrixDisplay;

    public TransformationPanel() {
        setLayout(new BorderLayout(10, 10)); // Dodano odstępy
        setBorder(BorderFactory.createTitledBorder("Transformacje i Punkty Kontrolne")); // Zmieniono tytuł

        // Panel listy punktów
        pointListModel = new DefaultListModel<>();
        pointList = new JList<>(pointListModel);
        pointList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScroll = new JScrollPane(pointList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Punkty kontrolne"));
        listScroll.setPreferredSize(new Dimension(180, 0)); // Ustalona preferowana szerokość

        // Panel operacji
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.Y_AXIS)); // Układ wertykalny dla grup
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operacje na obiekcie"));

        // Grupa dla rotacji
        JPanel rotateGroup = new JPanel(new GridLayout(3, 3, 5, 5)); // 3x3 dla czytelności
        rotateGroup.setBorder(BorderFactory.createTitledBorder("Obrót (kąt w stopniach)"));
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
        operationPanel.add(rotateGroup);
        operationPanel.add(Box.createVerticalStrut(10)); // Odstęp

        // Grupa dla skalowania
        JPanel scaleGroup = new JPanel(new GridLayout(3, 2, 5, 5)); // 3x2
        scaleGroup.setBorder(BorderFactory.createTitledBorder("Skalowanie"));
        scaleButton = new JButton("Skaluj");

        scaleGroup.add(new JLabel("Skala X:"));
        scaleGroup.add(scaleXField);
        scaleGroup.add(new JLabel("Skala Y:"));
        scaleGroup.add(scaleYField);
        scaleGroup.add(new JLabel("Skala Z:")); // NOWE
        scaleGroup.add(scaleZField); // NOWE
        operationPanel.add(scaleGroup);
        // Przycisk skalowania na dole grupy skalowania
        JPanel scaleButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scaleButtonPanel.add(scaleButton);
        operationPanel.add(scaleButtonPanel);
        operationPanel.add(Box.createVerticalStrut(10)); // Odstęp


        // Grupa dla przesunięcia
        JPanel translateGroup = new JPanel(new GridLayout(3, 2, 5, 5)); // 3x2
        translateGroup.setBorder(BorderFactory.createTitledBorder("Przesunięcie"));
        translateButton = new JButton("Przesuń");

        translateGroup.add(new JLabel("Przesunięcie X:"));
        translateGroup.add(translateXField);
        translateGroup.add(new JLabel("Przesunięcie Y:"));
        translateGroup.add(translateYField);
        translateGroup.add(new JLabel("Przesunięcie Z:")); // NOWE
        translateGroup.add(translateZField); // NOWE
        operationPanel.add(translateGroup);
        // Przycisk przesunięcia na dole grupy przesunięcia
        JPanel translateButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        translateButtonPanel.add(translateButton);
        operationPanel.add(translateButtonPanel);
        operationPanel.add(Box.createVerticalStrut(10)); // Odstęp


        // Wyświetlanie macierzy
        matrixDisplay = new JTextArea(5, 25); // Zwiększono rozmiar
        matrixDisplay.setEditable(false);
        matrixDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Czcionka stałej szerokości
        JScrollPane matrixScroll = new JScrollPane(matrixDisplay);
        matrixScroll.setBorder(BorderFactory.createTitledBorder("Macierz przekształceń globalnych"));
        matrixScroll.setPreferredSize(new Dimension(300, 150)); // Preferowany rozmiar

        // Panel dla przycisku "Usuń punkt" (do punktów kontrolnych klikanych myszą)
        JPanel removePointPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        removePointButton = new JButton("Usuń zaznaczone punkty");
        removePointPanel.add(removePointButton);


        // Panel prawy (macierz + przycisk usuwania)
        JPanel rightSidePanel = new JPanel(new BorderLayout());
        rightSidePanel.add(matrixScroll, BorderLayout.CENTER);
        rightSidePanel.add(removePointPanel, BorderLayout.SOUTH);

        // Dodanie paneli do głównego TransformationPanel
        add(listScroll, BorderLayout.WEST);
        add(operationPanel, BorderLayout.CENTER);
        add(rightSidePanel, BorderLayout.EAST);
    }

    // --- Gettery dla nowych pól i przycisków ---
    public String getRotateXValue() { return rotateXField.getText(); }
    public String getRotateYValue() { return rotateYField.getText(); }
    public String getRotateZValue() { return rotateZField.getText(); }

    public JButton getRotateXButton() { return rotateXButton; }
    public JButton getRotateYButton() { return rotateYButton; }
    public JButton getRotateZButton() { return rotateZButton; }

    public String getScaleZValue() { return scaleZField.getText(); } // NOWE
    public String getTranslateZValue() { return translateZField.getText(); } // NOWE

    // Pozostałe gettery bez zmian
    public int[] getSelectedPointIndices() {
        return pointList.getSelectedIndices();
    }

    public JButton getScaleButton() {return  scaleButton;}
    public JButton getTranslateButton() {return  translateButton;}
    public String getRotateValue(){
        // To pole nie jest już używane, ale zostawiamy je na wszelki wypadek,
        // jeśli gdzieś w kontrolerze jest odwołanie do niego.
        // Będziemy je usuwać w ImageController.
        return rotateZField.getText(); // Zwracamy Z jako domyślne dla kompatybilności
    }
    public String getScaleXValue(){return scaleXField.getText();}
    public String getScaleYValue(){return scaleYField.getText();}
    public String getTranslateXValue(){return translateXField.getText();}
    public String getTranslateYValue(){return translateYField.getText();}
    public void updateMatrixDisplay(String matrixText){matrixDisplay.setText(matrixText);}
    public void addPointToList(String point){pointListModel.addElement(point);}
    public void clearPointList(){pointListModel.clear();}

    public JButton getRemovePointButton() {
        return removePointButton;
    }

    public void refreshPointsList(List<Point3D> points) {
        pointListModel.clear();
        for (Point3D p : points) {
            pointListModel.addElement(String.format("(%.0f, %.0f, %.0f)", p.getX(), p.getY(), p.getZ()));
        }
    }
}