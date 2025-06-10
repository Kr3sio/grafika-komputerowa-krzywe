package models;

import java.util.ArrayList;
import java.util.List;

public class TransformationModel {

    private List<Point3D> originalVertices;
    private final List<Point3D> transformedVertices;
    private Matrix4x4 currentTransformMatrix;

    public TransformationModel(){
        this.originalVertices = new ArrayList<>();
        this.transformedVertices = new ArrayList<>();
        this.currentTransformMatrix = Matrix4x4.identity();
    }

    public void setOriginalVertices(List<Point3D> vertices) {
        this.originalVertices = new ArrayList<>(vertices);
        applyCurrentTransformation();
    }

    public void addPoint(double x, double y, double z){
        this.transformedVertices.add(new Point3D(x, y, z));
    }

    public List<Point3D> getPoints(){
        // To jest nadal pewna niekonsekwencja, ale dla obecnego projektu
        // zwracamy listę zawierającą zarówno przekształcone wierzchołki bryły,
        // jak i punkty dodane myszą. Docelowo można to rozdzielić.
        return new ArrayList<>(transformedVertices);
    }

    public void clearPoints(){
        originalVertices.clear();
        transformedVertices.clear();
        this.currentTransformMatrix = Matrix4x4.identity();
    }

    // USUNIĘTO ADNOTACJĘ @Override
    public String getMatrixString(){
        return currentTransformMatrix.toString();
    }

    public void applyTransformation(Matrix4x4 elementTransformMatrix) {
        this.currentTransformMatrix = Matrix4x4.multiply(elementTransformMatrix, this.currentTransformMatrix);
        applyCurrentTransformation();
    }

    public void applyCurrentTransformation() {
        transformedVertices.clear();
        // Dodaj wierzchołki bryły po transformacji
        for (Point3D originalVertex : originalVertices) {
            transformedVertices.add(Matrix4x4.transformPoint(currentTransformMatrix, originalVertex));
        }
        // Jeżeli punkty dodane myszą mają być objęte transformacjami,
        // to trzeba by je tutaj również transformować i dodać do transformedVertices.
        // Na razie pozostają niezależne.
    }

    public void removePoint(int index) {
        if (index >= 0 && index < transformedVertices.size()) {
            transformedVertices.remove(index);
        }
    }

    public List<Point3D> getTransformedVertices() {
        // Ta metoda zwraca listę wierzchołków BRYŁY po transformacji
        List<Point3D> currentTransformedMeshVertices = new ArrayList<>();
        for (Point3D originalVertex : originalVertices) {
            currentTransformedMeshVertices.add(Matrix4x4.transformPoint(currentTransformMatrix, originalVertex));
        }
        return currentTransformedMeshVertices;
    }
}