package models;

import java.util.List;
import java.util.Objects;

public class Face {
    private final List<Integer> vertexIndices;

    public Face(List<Integer> vertexIndices) {
        this.vertexIndices = Objects.requireNonNull(vertexIndices);
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    /**
     * Oblicza wektor normalny do ściany w przestrzeni świata.
     * Wierzchołki muszą być podane już po wszystkich transformacjach obiektu.
     * Zakłada, że wierzchołki w liście są w kolejności CCW (Counter-ClockWise) patrząc od zewnątrz.
     * @param allTransformedVertices Lista wszystkich przekształconych wierzchołków modelu.
     * @return Znormalizowany wektor normalny do ściany.
     */
    public Point3D calculateNormal(List<Point3D> allTransformedVertices) {
        if (vertexIndices.size() < 3) {
            return new Point3D(0, 0, 0); // Nie można obliczyć normalnej dla mniej niż 3 wierzchołków
        }

        // Pobieramy pierwsze trzy wierzchołki ściany (po transformacji)
        Point3D p1 = allTransformedVertices.get(vertexIndices.get(0));
        Point3D p2 = allTransformedVertices.get(vertexIndices.get(1));
        Point3D p3 = allTransformedVertices.get(vertexIndices.get(2));

        // Obliczamy dwa wektory leżące na płaszczyźnie ściany
        Point3D vec1 = Point3D.subtract(p2, p1);
        Point3D vec2 = Point3D.subtract(p3, p1); // Od P1 do P3

        // Normalna jest iloczynem wektorowym (vec1 x vec2)
        Point3D normal = Point3D.crossProduct(vec1, vec2);

        return Point3D.normalize(normal); // Zawsze zwracaj znormalizowany wektor
    }
}