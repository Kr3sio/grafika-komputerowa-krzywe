package models;

import java.util.List;
import java.util.ArrayList;

public class Face {
    private final List<Integer> vertexIndices;

    public Face(List<Integer> vertexIndices) {
        this.vertexIndices = new ArrayList<>(vertexIndices); // Kopiujemy listę
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }
}
