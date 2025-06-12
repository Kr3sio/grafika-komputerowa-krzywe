// models/Face.java
package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca ścianę (Face) modelu 3D.
 * Przechowuje indeksy wierzchołków tworzących tę ścianę.
 */
public class Face {
    private List<Integer> vertexIndices;

    public Face(List<Integer> vertexIndices) {
        // Tworzymy nową listę, aby nie modyfikować oryginalnej
        this.vertexIndices = new ArrayList<>(vertexIndices);
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }
}