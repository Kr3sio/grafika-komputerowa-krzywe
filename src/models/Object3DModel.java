package models;

import java.io.*;
import java.util.*;

public class Object3DModel {
    private final List<Vertex3D> vertices = new ArrayList<>();
    private final List<Face> faces = new ArrayList<>();

    private double[][] trasformMatrix = new double[4][4];

    public Object3DModel() {
        setIdentityMatrix();
    }

    public List<Vertex3D> getVertices() {return vertices;}
    public List<Face> getFaces() {return faces;}

    public double[][] getTransformMatrix() {return trasformMatrix;}

    public void setIdentityMatrix() {
        trasformMatrix = new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }

    public static Object3DModel loadModel(File file) throws IOException {
        Object3DModel model = new Object3DModel();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if(line.startsWith("v ")){
                    String[] parts = line.split("\\s+");
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    model.vertices.add(new Vertex3D(x, y, z));
                }else if(line.startsWith("f ")){
                    String[] parts = line.split("\\s+");
                    List<Integer> indices = new ArrayList<>();
                    for(int i = 1; i < parts.length; i++){
                        indices.add(Integer.parseInt(parts[i]));
                    }
                    model.faces.add(new Face(indices));
                }
            }
        }
        return model;
    }

    public  void applyTransformation(double[][] matrix) {
        this.trasformMatrix = Matrix4x4.multiply(matrix, this.trasformMatrix);
    }
}
