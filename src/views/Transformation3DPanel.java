package views;

import javax.swing.*;
import java.awt.*;

public class Transformation3DPanel extends JPanel {

    private final JTextField dxField,dyField,dzField;
    private final JTextField sxField,syField,szField;
    private final JTextField rxField,ryField,rzField;
    private final JButton applyButton;

    public Transformation3DPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Transforacje 3D"));

        JPanel grid = new JPanel(new GridLayout(4,1));

        JPanel translatePanel = new  JPanel(new FlowLayout(FlowLayout.LEFT));
        translatePanel.setBorder(BorderFactory.createTitledBorder("Przesunięcie"));
        dxField = new JTextField("0", 4);
        dyField = new JTextField("0", 4);
        dzField = new JTextField("0", 4);
        translatePanel.add(new JLabel("dx"));
        translatePanel.add(dxField);
        translatePanel.add(new JLabel("dy"));
        translatePanel.add(dyField);
        translatePanel.add(new JLabel("dz"));
        translatePanel.add(dzField);
        grid.add(translatePanel);

        // Skalowanie
        JPanel scalePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scalePanel.setBorder(BorderFactory.createTitledBorder("sKalowanie"));
        sxField = new JTextField("1", 4);
        syField = new JTextField("1", 4);
        szField = new JTextField("1", 4);
        scalePanel.add(new JLabel("sx: "));
        scalePanel.add(sxField);
        scalePanel.add(new JLabel("sy: "));
        scalePanel.add(syField);
        scalePanel.add(new JLabel("sz: "));
        scalePanel.add(szField);
        grid.add(scalePanel);

        // obrót
        JPanel rotatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rotatePanel.setBorder(BorderFactory.createTitledBorder("Obrót (°)"));
        rxField = new JTextField("0", 4);
        ryField = new JTextField("0", 4);
        rzField = new JTextField("0", 4);
        rotatePanel.add(new JLabel("rx: "));
        rotatePanel.add(rxField);
        rotatePanel.add(new JLabel("ry: "));
        rotatePanel.add(ryField);
        rotatePanel.add(new JLabel("rz: "));
        rotatePanel.add(rzField);
        grid.add(rotatePanel);

        //przycisk

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        applyButton = new JButton("Zastosuj");
        buttonPanel.add(applyButton);
        grid.add(buttonPanel);

        add(grid,BorderLayout.CENTER);

    }

    public double getDx() { return parse(dxField); }
    public double getDy() { return parse(dyField); }
    public double getDz() { return parse(dzField); }

    public double getSx() { return parse(sxField); }
    public double getSy() { return parse(syField); }
    public double getSz() { return parse(szField); }

    public double getRx() { return Math.toRadians(parse(rxField)); }
    public double getRy() { return Math.toRadians(parse(ryField)); }
    public double getRz() { return Math.toRadians(parse(rzField)); }

    public JButton getApplyButton() { return applyButton; }

    private double parse(JTextField field) {
        try {
            return Double.parseDouble(field.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
