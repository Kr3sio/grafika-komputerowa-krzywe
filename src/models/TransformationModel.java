package models;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TransformationModel {

    private final List<Point2D.Double> points;


    public TransformationModel(){
        this.points = new ArrayList<>();

    }

    public void addPoint(double x, double y){
        points.add(new Point2D.Double(x, y));
    }

    public List<Point2D.Double> getPoints(){return points;}


    public void clearPoints(){
        points.clear();

    }




    public String getMatrixString(AffineTransform transform){
        double[] matrix = new double[6];
        transform.getMatrix(matrix);

        return String.format(
                "[ %.2f %.2f 0.00 ]\n[ %.2f %.2f 0.00 ]\n[ %.2f %.2f 1.00 ]",
                matrix[0], matrix[2],
                matrix[1], matrix[3],
                matrix[4], matrix[5]
        );
    }

    public void transformPoint(int index, AffineTransform op){
        if (index >=0 && index < points.size()){
            Point2D src = points.get(index);
            Point2D dst = new Point2D.Double();
            op.transform(src, dst);
            points.set(index, (Point2D.Double) dst);
        }
    }
}
