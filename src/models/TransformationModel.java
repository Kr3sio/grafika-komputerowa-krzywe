package models;


import java.util.ArrayList;
import java.util.List;

public class TransformationModel {

    private final List<Point3D> points;

    private double[][] currentTransformMatrix;

    public TransformationModel(){
        this.points = new ArrayList<>();
        this.currentTransformMatrix = new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

    }

    public void addPoint(double x, double y, double z){

        points.add(new Point3D(x, y, z));
    }

    public List<Point3D> getPoints(){return points;}


    public void clearPoints(){
        points.clear();
        this.currentTransformMatrix = new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }




    public String getMatrixString(){
        // Wyświetlanie macierzy 4x4
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append("[ ");
            for (int j = 0; j < 4; j++) {
                sb.append(String.format("%.2f ", currentTransformMatrix[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    // Metoda do transformacji pojedynczego punktu (będzie rozbudowana)
    public void transformPoint(int index, double[][] transformationMatrix){
        if (index >=0 && index < points.size()){
            Point3D src = points.get(index);
            // Tutaj w przyszłości będzie mnożenie punktu przez macierz
            // Na razie pozostawimy to do implementacji w Zadaniach 4 i 6
            // Będzie to wymagało stworzenia klasy Matrix4x4 i metody multiply
            // na razie nic tu nie zmieniamy w logice, tylko typy
        }
        // W przyszłości będziemy preConcatenate transformacji
        // currentTransformMatrix = multiply(transformationMatrix, currentTransformMatrix);
    }

    // Metoda do transformacji listy punktów
    public void transformPoints(List<Integer> indices, double[][] elementTransformMatrix) {
        // Ta metoda będzie głównym miejscem, gdzie będziemy stosować transformacje
        // do wierzchołków bryły
        for (int index : indices) {
            if (index >= 0 && index < points.size()) {
                Point3D src = points.get(index);
                // Tworzymy nowy punkt, który będzie wynikiem transformacji
                Point3D dst = new Point3D(0, 0, 0); // Placeholder
                // Tutaj będziemy mnożyć punkt przez macierz elementTransformMatrix
                // Na razie tylko zmieniamy typy, implementacja mnożenia będzie później
                // np. dst = Matrix4x4.multiply(elementTransformMatrix, src);
                points.set(index, dst); // Zastępujemy stary punkt nowym
            }
        }
        // Aktualizacja macierzy przekształceń
        // currentTransformMatrix = Matrix4x4.multiply(elementTransformMatrix, currentTransformMatrix);
    }


    public void removePoint(int index) {
        if (index >= 0 && index < points.size()) {
            points.remove(index);
        }
    }

    public List<Point3D> getTransformedPoints() {
        // Zwracamy kopię, aby nie modyfikować oryginalnej listy z zewnątrz
        return new ArrayList<>(points);
    }

    public double[][] getCurrentTransformMatrix(){
        return currentTransformMatrix;
    }
}
