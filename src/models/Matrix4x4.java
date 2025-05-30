package models;

public class Matrix4x4 {

    public static double[][] identity() {
        return new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }
    public static double[][] multiply(double[][] a, double[][] b) {
        double[][] result = new double[4][4];
        for(int row = 0; row < 4; row++) {
            for(int col = 0; col < 4; col++) {
                result[row][col] = 0;
                for(int k = 0; k < 4; k++) {
                    result[row][col] += a[row][k] * b[k][col];
                }
            }
        }
        return result;
    }

    public static  double[][] translation(double dx, double dy, double dz) {
        double[][] m = identity();
        m[0][3] = dx;
        m[1][3] = dy;
        m[2][3] = dz;
        return m;
    }

    public static double[][] scaling(double sx, double sy, double sz) {
        double[][] m = identity();
        m[0][0] = sx;
        m[1][1] = sy;
        m[2][2] = sz;
        return m;
    }

    public static double[][] rotationX(double angleRad){
        double[][] m = identity();
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        m[1][1] = cos;
        m[1][2] = -sin;
        m[2][1] = sin;
        m[2][2] = cos;
        return m;
    }

    public static double[][] rotationY(double angleRad){
        double[][] m = identity();
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        m[0][0] = cos;
        m[0][2] = sin;
        m[2][0] = -sin;
        m[2][2] = cos;
        return m;
    }

    public static double[][] rotationZ(double angleRad){
        double[][] m = identity();
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        m[0][0] = cos;
        m[0][1] = -sin;
        m[1][0] = sin;
        m[1][1] = cos;
        return m;
    }
}

