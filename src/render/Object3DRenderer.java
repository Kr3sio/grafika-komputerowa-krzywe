package render;

import models.Face;
import models.Object3DModel;
import models.Vertex3D;

import java.awt.*;
import java.util.List;

public class Object3DRenderer {

    private final int width;
    private final int height;

    public Object3DRenderer(int canvasWidth, int canvasHeight) {
        this.width = canvasWidth;
        this.height = canvasHeight;
    }

    public void draw(Graphics2D g, Object3DModel model) {
        List<Vertex3D> verts = model.getVertices();
        double[][] matrix = model.getTransformMatrix();

        // Przekształcone i rzutowane punkty 2D
        Point[] projected = new Point[verts.size()];
        for (int i = 0; i < verts.size(); i++) {
            Vertex3D v = verts.get(i);
            double[] vec = new double[] { v.x, v.y, v.z, 1 };
            double[] transformed = multiplyMatrixVec(matrix, vec);

            // Rzutowanie ortogonalne na XY
            int x2D = (int) Math.round(transformed[0] + width / 2.0);
            int y2D = (int) Math.round(height / 2.0 - transformed[1]);

            projected[i] = new Point(x2D, y2D);
        }

        // Rysuj krawędzie ścian
        g.setColor(Color.BLUE);
        for (Face f : model.getFaces()) {
            List<Integer> indices = f.indices;
            for (int i = 0; i < indices.size(); i++) {
                Point p1 = projected[indices.get(i)];
                Point p2 = projected[indices.get((i + 1) % indices.size())];
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    private double[] multiplyMatrixVec(double[][] m, double[] v) {
        double[] r = new double[4];
        for (int i = 0; i < 4; i++) {
            r[i] = m[i][0] * v[0] + m[i][1] * v[1] + m[i][2] * v[2] + m[i][3] * v[3];
        }
        return r;
    }
}
