// models/MeshModel.java
package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Dodaj import dla klasy Face
import models.Face; // <-- DODAJ TEN IMPORT

public class MeshModel {
    private List<Point3D> vertices;
    private List<Face> faces; // Teraz używa klasy models.Face

    public MeshModel() {
        this.vertices = new ArrayList<>();
        this.faces = new ArrayList<>();
    }

    public MeshModel(String filePath) throws IOException {
        this();
        loadFromObj(filePath);
    }

    public List<Point3D> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() { // Teraz zwraca listę models.Face
        return faces;
    }

    private void loadFromObj(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");

            if (parts.length > 0) {
                switch (parts[0]) {
                    case "v":
                        if (parts.length >= 4) {
                            try {
                                double x = Double.parseDouble(parts[1]);
                                double y = Double.parseDouble(parts[2]);
                                double z = Double.parseDouble(parts[3]);
                                vertices.add(new Point3D(x, y, z));
                            } catch (NumberFormatException e) {
                                System.err.println("Błąd parsowania współrzędnych wierzchołka: " + line);
                            }
                        }
                        break;
                    case "f":
                        if (parts.length >= 4) {
                            List<Integer> faceIndices = new ArrayList<>();
                            for (int i = 1; i < parts.length; i++) {
                                String[] indexParts = parts[i].split("/");
                                try {
                                    faceIndices.add(Integer.parseInt(indexParts[0]) - 1);
                                } catch (NumberFormatException e) {
                                    System.err.println("Błąd parsowania indeksu ściany: " + line);
                                }
                            }
                            if (faceIndices.size() >= 3) {
                                faces.add(new Face(faceIndices)); // Używa teraz models.Face
                            }
                        }
                        break;
                }
            }
        }
        reader.close();
    }
}