package models;


public class Matrix4x4 {
    private final double[][] data;


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
        if (transformedW != 0) {
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
     * Tworzy macierz projekcji perspektywicznej.
     * @param fovY Kąt widzenia w pionie w stopniach.
     * @param aspectRatio Stosunek szerokości do wysokości widocznego obszaru.
     * @param near Odległość do płaszczyzny bliskiego obcinania.
     * @param far Odległość do płaszczyzny dalekiego obcinania.
     * @return Macierz projekcji perspektywicznej.
     */
    public static Matrix4x4 perspective(double fovY, double aspectRatio, double near, double far ) {
        double fovRad = Math.toRadians(fovY);
        double tanHalfFov = Math.tan(fovRad/2.0);

        double[][] data = new double[4][4];
        data[0][0] = 1.0 / (aspectRatio * tanHalfFov);
        data[1][1] = 1.0 / tanHalfFov;
        data[2][2] = -(far + near)/(far-near);
        data[2][3] = -(2.0 * far * near)/(far-near);
        data[3][2] = -1.0;
        data[3][3] = 0.0;

        return new Matrix4x4(data);
    }

    /**
     * Tworzy macierz projekcji ortograficznej.
     * @param left Minimalna współrzędna X widocznego obszaru.
     * @param right Maksymalna współrzędna X widocznego obszaru.
     * @param bottom Minimalna współrzędna Y widocznego obszaru.
     * @param top Maksymalna współrzędna Y widocznego obszaru.
     * @param near Minimalna współrzędna Z widocznego obszaru.
     * @param far Maksymalna współrzędna Z widocznego obszaru.
     * @return Macierz projekcji ortograficznej.
     */
    public static Matrix4x4 orthographic(double left, double right, double bottom, double top, double near, double far) {
        double[][] data = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = 0.0;
            }
        }

        data[0][0] = 2.0 / (right - left);
        data[1][1] = 2.0 / (top - bottom);
        data[2][2] = -2.0 / (far - near);
        data[0][3] = -(right + left) / (right - left);
        data[1][3] = -(top + bottom) / (top - bottom);
        data[2][3] = -(far + near) / (far - near);
        data[3][3] = 1.0;

        return new Matrix4x4(data);
    }


    /**
     * Tworzy macierz widoku (LookAt), która ustawia kamerę.
     * @param eye Pozycja kamery w przestrzeni świata.
     * @param center Punkt, na który patrzy kamera.
     * @param up Wektor "góry" dla kamery.
     * @return Macierz widoku.
     */
    public static Matrix4x4 lookAt(Point3D eye, Point3D center, Point3D up){
        Point3D f = Point3D.normalize(Point3D.subtract(center, eye)); // Forward vector
        Point3D s = Point3D.normalize(Point3D.crossProduct(f,up));    // Side vector
        Point3D u = Point3D.crossProduct(s,f);                        // Up vector (re-orthogonalized)

        // Macierz orientacji (rotation part of view matrix)
        double[][] R = {
                {s.x, s.y, s.z, 0},
                {u.x, u.y, u.z, 0},
                {-f.x, -f.y, -f.z, 0}, // Z-axis for view space is usually -f
                {0, 0, 0, 1}
        };

        // Macierz translacji (translation part of view matrix)
        double[][] T = {
                {1, 0, 0, -eye.x},
                {0, 1, 0, -eye.y},
                {0, 0, 1, -eye.z},
                {0, 0, 0, 1}
        };

        // Macierz widoku = Translacja * Orientacja (lub Rotacja * Translacja w zależności od konwencji)
        // W tym przypadku jest to odwrotność transformacji kamery, więc (R * T)
        return Matrix4x4.multiply(new Matrix4x4(R), new Matrix4x4(T));
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