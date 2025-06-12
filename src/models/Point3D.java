package models;

public class Point3D {
    public double x;
    public double y;
    public double z;


    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX(){return x;}
    public double getY(){return y;}
    public double getZ(){return z;}

    public void setX(double x){this.x = x;}
    public void setY(double y){this.y = y;}
    public void setZ(double z){this.z = z;}

    public static Point3D subtract(Point3D p1, Point3D p2) {
        return new Point3D(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
    }

    public static Point3D normalize(Point3D p) {
        double lenght = Math.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
        if(lenght == 0.0) return new Point3D(0, 0, 0);
        return new Point3D(p.x / lenght, p.y / lenght, p.z / lenght);
    }

    public static Point3D crossProduct(Point3D p1, Point3D p2) {
        return new Point3D(
                p1.y * p2.z - p1.z * p2.y,
                p1.z * p2.x - p1.x * p2.z,
                p1.x * p2.y - p1.y * p2.x
        );
    }

    public static double dotProduct(Point3D p1, Point3D p2) {
        return p1.x * p2.x + p1.y * p2.y + p1.z * p2.z;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
