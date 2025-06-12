package views;

import javax.swing.*;

/**
 * Pasek menu aplikacji, zawierający opcje zarządzania plikami, edycji oraz operacji na panelach.
 * Klasa rozszerza {@link JMenuBar} i definiuje strukturę menu dla aplikacji.
 */
public class MenuBar extends JMenuBar {

    private final JMenu fileMenu;
    private final JMenu PanelMenu;
    private final JMenu editPanelMenu;

    private final JMenuItem openFileMenuItem;
    private final JMenuItem saveFileMenuItem;
    private final JMenuItem exitMenuItem;

    private final JMenuItem clearPanelMenuItem;

    private final JCheckBoxMenuItem showPolylineMenuItem;

    private final JRadioButtonMenuItem transformSelectedOnlyItem;

    private final JRadioButtonMenuItem transformAllPointsItems;

    private final JCheckBoxMenuItem showBezierCurveMenuItem;

    private final JMenuItem clearCurveMenuItem;

    private final JMenuItem generateTextCurveMenuItem;

    private final JMenu view3DMenu;

    private final JRadioButtonMenuItem orthoProjectionItem;
    private final JRadioButtonMenuItem perspectiveProjectionItem;
    private final JCheckBoxMenuItem visibleOnlyFaceItem;

    private final JTextField observerDistanceField;

    public MenuBar() {
        // Tworzenie głównych menu
        fileMenu = new JMenu("Plik");
        PanelMenu = new JMenu("Lewy panel");
        editPanelMenu = new JMenu("Edycja");

        // Menu plik
        openFileMenuItem = new JMenuItem("Otwórz");
        saveFileMenuItem = new JMenuItem("Zapisz");
        exitMenuItem = new JMenuItem("Zakończ");

        // Menu Panel lewy
        clearPanelMenuItem = new JMenuItem("Wyczyść");

        // Menu Linie
        JMenu lineMenu = new JMenu("Linie");
        showPolylineMenuItem = new JCheckBoxMenuItem("Pokaż linię łamaną");
        transformSelectedOnlyItem = new JRadioButtonMenuItem("Transformuj zaznaczone",true);
        transformAllPointsItems = new JRadioButtonMenuItem("Transformuj wszystkie");
        showBezierCurveMenuItem = new JCheckBoxMenuItem("Pokaż krzywą Beziera");
        ButtonGroup transformButtonGroup = new ButtonGroup();
        transformButtonGroup.add(transformSelectedOnlyItem);
        transformButtonGroup.add(transformAllPointsItems);

        clearCurveMenuItem = new JMenuItem("Wyczyść krzywą i punkty");
        lineMenu.addSeparator();
        lineMenu.add(clearCurveMenuItem);

        generateTextCurveMenuItem = new JMenuItem("Generuj krzywą z tekstu");
        lineMenu.add(generateTextCurveMenuItem);

        // Dodanie elementów do menu Plik
        fileMenu.add(openFileMenuItem);
        fileMenu.add(saveFileMenuItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitMenuItem);

        // Dodanie elementów do menu Panel
        PanelMenu.add(clearPanelMenuItem);

        // Dodawanie elementów do menu Lini
        lineMenu.add(showPolylineMenuItem);
        lineMenu.addSeparator();
        lineMenu.add(transformSelectedOnlyItem);
        lineMenu.add(transformAllPointsItems);
        lineMenu.add(showBezierCurveMenuItem);


        // === [3D] Zakładka Widok 3D ===

        view3DMenu = new JMenu("Widok 3D");

        orthoProjectionItem = new JRadioButtonMenuItem("Rzut równoległy",true); // Domyślnie równoległy
        perspectiveProjectionItem = new JRadioButtonMenuItem("Rzut perspektywiczny");
        visibleOnlyFaceItem = new JCheckBoxMenuItem("Tylko widoczne ściany");

        ButtonGroup projectionGroup = new ButtonGroup(); // Poprawiona nazwa zmiennej
        projectionGroup.add(orthoProjectionItem);
        projectionGroup.add(perspectiveProjectionItem);

        observerDistanceField = new JTextField("10.0",5);

        view3DMenu.add(orthoProjectionItem);
        view3DMenu.add(perspectiveProjectionItem);
        view3DMenu.addSeparator();
        view3DMenu.add(new JLabel("Odległość obserwatora (Z): "));
        view3DMenu.add(observerDistanceField);
        view3DMenu.addSeparator();
        view3DMenu.add(visibleOnlyFaceItem);

        // Dodawanie wszystkich menu do paska menu
        add(fileMenu);
        add(PanelMenu);
        add(editPanelMenu);
        add(lineMenu);
        add(view3DMenu);
    }

    public JMenuItem getOpenFileMenuItem() {
        return openFileMenuItem;
    }

    public JMenuItem getSaveFileMenuItem() {
        return saveFileMenuItem;
    }

    public JMenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public JMenuItem getClearLeftPanelMenuItem() {
        return clearPanelMenuItem;
    }

    public JCheckBoxMenuItem getShowPolylineMenuItem() {return showPolylineMenuItem;}
    public boolean isTransformAllEnabled() {return transformAllPointsItems.isSelected();}
    public JCheckBoxMenuItem getShowBezierCurveMenuItem(){return showBezierCurveMenuItem;}
    public JMenuItem getClearCurveMenuItem() {return clearCurveMenuItem;}
    public JMenuItem getGenerateTextCurveMenuItem() {return generateTextCurveMenuItem;}
    public boolean isOrthoProjectionSelected() { return orthoProjectionItem.isSelected(); }
    public boolean isPerspectiveProjectionSelected() { return perspectiveProjectionItem.isSelected(); }
    public boolean isVisibleOnlySelected() { return visibleOnlyFaceItem.isSelected(); }
    public double getObserverDistance() {
        try {
            return Double.parseDouble(observerDistanceField.getText());
        } catch (NumberFormatException e) {
            return 10.0;
        }
    }

    public JTextField getObserverDistanceField() {
        return observerDistanceField;
    }

    // NOWE GETTERY DLA PRZYCISKÓW WIDOKU 3D
    public JCheckBoxMenuItem getVisibleOnlyFaceItem() {
        return visibleOnlyFaceItem;
    }

    public JRadioButtonMenuItem getOrthoProjectionItem() {
        return orthoProjectionItem;
    }

    public JRadioButtonMenuItem getPerspectiveProjectionItem() {
        return perspectiveProjectionItem;
    }
}