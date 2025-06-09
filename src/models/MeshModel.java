package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje model siatki 3D, składający się z wierzchołków i ścian.
 */
public class MeshModel {
    private final List<Point3D> vertices; // Lista wierzchołków
    private final List<Face> faces;       // Lista ścian

    public MeshModel() {
        this.vertices = new ArrayList<>();
        this.faces = new ArrayList<>();
    }

    public void addVertex(Point3D vertex) {
        this.vertices.add(vertex);
    }

    public void addFace(Face face) {
        this.faces.add(face);
    }

    public List<Point3D> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() {
        return faces;
    }

    /**
     * Zwraca listę wierzchołków, które są punktami kontrolnymi.
     * Na tym etapie są to wszystkie wierzchołki modelu.
     * W przyszłości mogą być to tylko wybrane wierzchołki.
     */
    public List<Point3D> getControlPoints() {
        return new ArrayList<>(vertices); // Zwracamy kopię
    }
}