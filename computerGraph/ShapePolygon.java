package computerGraph;




class Point {
    public  double x;
    public  double y;

    Point(double x, double y){
        this.x = x;
        this.y = y;

    }
}

public class ShapePolygon {
    public final Point[] points; // Points making up the boundary

    public boolean contains(Point test) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].y > test.y) != (points[j].y > test.y) &&
                    (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y-points[i].y) + points[i].x)) {
                result = !result;
            }
        }
        return result;
    }


    ShapePolygon(Point[] points){
        this.points = points;
    }


}
