package models;

import java.util.ArrayList;
import java.util.List;

public class TransformationModel {

    // Globalna macierz transformacji
    private Matrix4x4 currentTransformMatrix;

    // Oryginalne wierzchołki modelu (załadowane z pliku OBJ)
    private List<Point3D> originalVertices;

    // Przekształcone wierzchołki (wynik currentTransformMatrix * originalVertices)
    private List<Point3D> transformedVertices;

    // Punkty dodane przez użytkownika na panelu (niepowiązane z modelem 3D)
    private List<Point3D> points;

    public TransformationModel() {
        this.currentTransformMatrix = Matrix4x4.identity(); // Początkowo macierz jednostkowa
        this.originalVertices = new ArrayList<>();
        this.transformedVertices = new ArrayList<>();
        this.points = new ArrayList<>();
    }

    /**
     * Ustawia oryginalne wierzchołki modelu 3D.
     * @param vertices Lista oryginalnych wierzchołków.
     */
    public void setOriginalVertices(List<Point3D> vertices) {
        this.originalVertices = new ArrayList<>(vertices); // Kopiujemy listę
        // Po ustawieniu oryginalnych wierzchołków, od razu oblicz transformedVertices
        applyTransformation(Matrix4x4.identity()); // Zastosuj macierz jednostkową, aby zainicjować transformedVertices
    }

    /**
     * Zwraca przekształcone wierzchołki modelu.
     * @return Lista przekształconych wierzchołków.
     */
    public List<Point3D> getTransformedVertices() {
        return transformedVertices;
    }

    /**
     * Dodaje punkt do listy punktów kontrolnych.
     * @param x Współrzędna X.
     * @param y Współrzędna Y.
     * @param z Współrzędna Z.
     */
    public void addPoint(double x, double y, double z) {
        points.add(new Point3D(x, y, z));
    }

    /**
     * Usuwa punkt z listy punktów kontrolnych.
     * @param index Indeks punktu do usunięcia.
     */
    public void removePoint(int index) {
        if (index >= 0 && index < points.size()) {
            points.remove(index);
        }
    }

    /**
     * Zwraca listę punktów kontrolnych.
     * @return Lista punktów.
     */
    public List<Point3D> getPoints() {
        return points;
    }

    /**
     * Czyści listę punktów kontrolnych.
     */
    public void clearPoints() {
        this.points.clear();
    }

    /**
     * Stosuje nową macierz transformacji do bieżącej macierzy globalnej
     * i przelicza transformedVertices.
     * @param transform Nowa macierz transformacji do zastosowania (np. rotacja, skalowanie, translacja).
     */
    public void applyTransformation(Matrix4x4 transform) {
        // Pomnóż bieżącą macierz globalną przez nową transformację
        // Kolejność: (nowa transformacja) * (obecna macierz globalna)
        // To jest zazwyczaj standard dla "object-space" transformacji
        // (gdzie transformacje są stosowane względem lokalnego układu obiektu).
        // Jeśli chcesz globalne transformacje (względem świata), to (obecna * nowa).
        // Na razie zakładam, że chcesz kumulować transformacje lokalnie.
        this.currentTransformMatrix = Matrix4x4.multiply(transform, currentTransformMatrix);

        // Przelicz transformedVertices
        updateTransformedVertices();
    }

    /**
     * Przelicza transformedVertices na podstawie currentTransformMatrix i originalVertices.
     */
    private void updateTransformedVertices() {
        transformedVertices.clear(); // Wyczyść poprzednie przekształcone wierzchołki
        for (Point3D originalVertex : originalVertices) {
            // Przekształć każdy oryginalny wierzchołek
            transformedVertices.add(Matrix4x4.transformPoint(currentTransformMatrix, originalVertex));
        }
    }

    /**
     * Zwraca tekstową reprezentację bieżącej macierzy transformacji.
     * @return String z macierzą.
     */
    public String getMatrixString() {
        return currentTransformMatrix.toString();
    }

    /**
     * NOWA METODA: Resetuje bieżącą macierz transformacji do macierzy jednostkowej
     * i przelicza transformedVertices.
     */
    public void resetTransformation() {
        this.currentTransformMatrix = Matrix4x4.identity();
        updateTransformedVertices(); // Przelicz transformedVertices
    }
}