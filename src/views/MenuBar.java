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
        ButtonGroup transformButtonGroup = new ButtonGroup();
        transformButtonGroup.add(transformSelectedOnlyItem);
        transformButtonGroup.add(transformAllPointsItems);

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


        // Dodawanie wszystkich menu do paska menu
        add(fileMenu);
        add(PanelMenu);
        add(editPanelMenu);
        add(lineMenu);
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




}
