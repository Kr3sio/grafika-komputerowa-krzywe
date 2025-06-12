package views;

import models.Point3D;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class PointListPanel extends JPanel {
    private final DefaultListModel<String> pointListModel;
    private final JList<String> pointList;
    private final JButton removePointButton;

    public PointListPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Punkty kontrolne"));

        pointListModel = new DefaultListModel<>();
        pointList = new JList<>(pointListModel);
        pointList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScroll = new JScrollPane(pointList);
        add(listScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        removePointButton = new JButton("Usuń zaznaczone punkty");
        buttonPanel.add(removePointButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(200, 250)); // Sugerowany rozmiar
    }

    public DefaultListModel<String> getPointListModel() {
        return pointListModel;
    }

    public int[] getSelectedPointIndices() {
        return pointList.getSelectedIndices();
    }

    public JButton getRemovePointButton() {
        return removePointButton;
    }

    public void refreshPointsList(List<Point3D> points) {
        pointListModel.clear();
        for (Point3D p : points) {
            pointListModel.addElement(String.format("(%.0f, %.0f, %.0f)", p.getX(), p.getY(), p.getZ()));
        }
    }

    /**
     * NOWA METODA: Czyści listę punktów wyświetlanych w panelu.
     */
    public void clearPointList() {
        pointListModel.clear();
    }
}