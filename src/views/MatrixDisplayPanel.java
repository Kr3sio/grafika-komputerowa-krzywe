package views;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MatrixDisplayPanel extends JPanel {
    private final JTextArea matrixDisplay;

    public MatrixDisplayPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Macierz przekształceń globalnych"));

        matrixDisplay = new JTextArea(5, 25); // 5 wierszy, 25 kolumn
        matrixDisplay.setEditable(false);
        matrixDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane matrixScroll = new JScrollPane(matrixDisplay);
        add(matrixScroll, BorderLayout.CENTER);

        // Ustaw preferowany i maksymalny rozmiar, aby zapobiec rozciąganiu
        Dimension preferredSize = new Dimension(280, 150); // Szerokość i wysokość
        setPreferredSize(preferredSize);
        setMaximumSize(preferredSize); // Ważne, aby ograniczyć rozciąganie
    }

    public void updateMatrixDisplay(String matrixText) {
        matrixDisplay.setText(matrixText);
    }
}