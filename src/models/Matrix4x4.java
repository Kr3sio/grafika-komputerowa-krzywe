package models;

/**
 * Reprezentuje macierz 4x4 do transformacji homogenicznych w przestrzeni 3D.
 */
public class Matrix4x4 {
    private final double[][] data;

    /**
     * Tworzy macierz 4x4 i inicjalizuje ją podanymi danymi.
     * @param data Tablica 4x4 z danymi macierzy.
     * @throws IllegalArgumentException Jeśli podana tablica nie jest macierzą 4x4.
     */
    public Matrix4x4(double[][] data) {
        if (data.length != 4 || data[0].length != 4) {
            throw new IllegalArgumentException("Matrix must be 4x4.");
        }
        this.data = new double[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, 4);
        }
    }

    /**
     * Tworzy macierz jednostkową 4x4.
     * @return Macierz jednostkowa.
     */
    public static Matrix4x4 identity() {
        return new Matrix4x4(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    /**
     * Mnoży dwie macierze 4x4.
     * @param A Pierwsza macierz.
     * @param B Druga macierz.
     * @return Wynik mnożenia macierzy A * B.
     */
    public static Matrix4x4 multiply(Matrix4x4 A, Matrix4x4 B) {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    result[i][j] += A.data[i][k] * B.data[k][j];
                }
            }
        }
        return new Matrix4x4(result);
    }

    /**
     * Mnoży wektor Point3D (reprezentowany homogenicznie jako [x, y, z, 1]) przez macierz.
     * @param matrix Macierz transformacji.
     * @param point Punkt 3D do transformacji.
     * @return Nowy obiekt Point3D po transformacji.
     */
    public static Point3D transformPoint(Matrix4x4 matrix, Point3D point) {
        // Punkt w postaci homogenicznej [x, y, z, 1]
        double x = point.x;
        double y = point.y;
        double z = point.z;
        double w = 1.0; // Homogeniczna współrzędna

        double transformedX = matrix.data[0][0] * x + matrix.data[0][1] * y + matrix.data[0][2] * z + matrix.data[0][3] * w;
        double transformedY = matrix.data[1][0] * x + matrix.data[1][1] * y + matrix.data[1][2] * z + matrix.data[1][3] * w;
        double transformedZ = matrix.data[2][0] * x + matrix.data[2][1] * y + matrix.data[2][2] * z + matrix.data[2][3] * w;
        double transformedW = matrix.data[3][0] * x + matrix.data[3][1] * y + matrix.data[3][2] * z + matrix.data[3][3] * w;

        // Normalizacja (jeśli transformedW != 1, co często ma miejsce przy projekcji perspektywicznej)
        if (transformedW != 0 && transformedW != 1) {
            transformedX /= transformedW;
            transformedY /= transformedW;
            transformedZ /= transformedW;
        }

        return new Point3D(transformedX, transformedY, transformedZ);
    }

    /**
     * Tworzy macierz translacji (przesunięcia).
     * @param dx Przesunięcie w osi X.
     * @param dy Przesunięcie w osi Y.
     * @param dz Przesunięcie w osi Z.
     * @return Macierz translacji.
     */
    public static Matrix4x4 translation(double dx, double dy, double dz) {
        return new Matrix4x4(new double[][]{
                {1, 0, 0, dx},
                {0, 1, 0, dy},
                {0, 0, 1, dz},
                {0, 0, 0, 1}
        });
    }

    /**
     * Tworzy macierz skalowania.
     * @param sx Skalowanie w osi X.
     * @param sy Skalowanie w osi Y.
     * @param sz Skalowanie w osi Z.
     * @return Macierz skalowania.
     */
    public static Matrix4x4 scaling(double sx, double sy, double sz) {
        return new Matrix4x4(new double[][]{
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}
        });
    }

    /**
     * Tworzy macierz obrotu wokół osi X.
     * @param angleDegrees Kąt obrotu w stopniach.
     * @return Macierz obrotu wokół osi X.
     */
    public static Matrix4x4 rotationX(double angleDegrees) {
        double angleRad = Math.toRadians(angleDegrees);
        double cosA = Math.cos(angleRad);
        double sinA = Math.sin(angleRad);
        return new Matrix4x4(new double[][]{
                {1, 0, 0, 0},
                {0, cosA, -sinA, 0},
                {0, sinA, cosA, 0},
                {0, 0, 0, 1}
        });
    }

    /**
     * Tworzy macierz obrotu wokół osi Y.
     * @param angleDegrees Kąt obrotu w stopniach.
     * @return Macierz obrotu wokół osi Y.
     */
    public static Matrix4x4 rotationY(double angleDegrees) {
        double angleRad = Math.toRadians(angleDegrees);
        double cosA = Math.cos(angleRad);
        double sinA = Math.sin(angleRad);
        return new Matrix4x4(new double[][]{
                {cosA, 0, sinA, 0},
                {0, 1, 0, 0},
                {-sinA, 0, cosA, 0},
                {0, 0, 0, 1}
        });
    }

    /**
     * Tworzy macierz obrotu wokół osi Z.
     * @param angleDegrees Kąt obrotu w stopniach.
     * @return Macierz obrotu wokół osi Z.
     */
    public static Matrix4x4 rotationZ(double angleDegrees) {
        double angleRad = Math.toRadians(angleDegrees);
        double cosA = Math.cos(angleRad);
        double sinA = Math.sin(angleRad);
        return new Matrix4x4(new double[][]{
                {cosA, -sinA, 0, 0},
                {sinA, cosA, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    /**
     * Zwraca dane macierzy.
     * @return Tablica 4x4 z danymi macierzy.
     */
    public double[][] getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append("[ ");
            for (int j = 0; j < 4; j++) {
                sb.append(String.format("%.2f ", data[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}