package models;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TransformationModel {

    private final List<Point2D.Double> points;

    private final AffineTransform currentTransform;

    public TransformationModel(){
        this.points = new ArrayList<>();
        this.currentTransform = new AffineTransform();

    }

    public void addPoint(double x, double y){
        points.add(new Point2D.Double(x, y));
    }

    public List<Point2D.Double> getPoints(){return points;}


    public void clearPoints(){
        points.clear();
        currentTransform.setToIdentity();
    }




    public String getMatrixString(){
        double[] matrix = new double[6];
        currentTransform.getMatrix(matrix);

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

        currentTransform.preConcatenate(op);
    }

    public void transformPoints(List<Integer> indices, AffineTransform elementTransform) {
        for (int index : indices) {
            if (index >= 0 && index < points.size()) {
                Point2D src = points.get(index);
                Point2D dst = new Point2D.Double();
                elementTransform.transform(src, dst);
                points.set(index, (Point2D.Double) dst);
            }
        }

        // Aktualizacja macierzy przekształceń
        currentTransform.preConcatenate(elementTransform);
    }

    public List<Point2D.Double> calculateBezierPoints(double step){
        List<Point2D.Double> result = new ArrayList<>();
        int n = points.size() -1;

        if(n<1)return result;

        for(double t=0.0;t<=1.0;t+=step){
            Point2D.Double pt = deCasteljau(points,t);
            result.add(pt);
        }

        result.add(deCasteljau(points,1.0));
        return result;
    }

    private Point2D.Double deCasteljau(List<Point2D.Double> ctrlPoints, double t){
        List<Point2D.Double> temp = new ArrayList<>(ctrlPoints);

        while(temp.size()>1){
            List<Point2D.Double> next = new ArrayList<>();
            for(int i = 0; i<temp.size()-1;i++){
                double x = (1-t)*temp.get(i).x + t * temp.get(i+1).x;
                double y = (1-t)*temp.get(i).y + t * temp.get(i+1).y;
                next.add(new Point2D.Double(x,y));
            }
            temp = next;
        }
        return temp.get(0);
    }

    public List<Point2D.Double> getTransformedPoints() {
        return new ArrayList<>(points);
    }


    public AffineTransform getCurrentTransform(){
        return currentTransform;
    }
}
