package computerGraph;

import com.sun.org.apache.xml.internal.security.Init;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


enum ShapeType { NoShape, ZShape, SShape, LineShape,
    TShape, SquareShape, LShape, MirroredLShape,
    newShape1,newShape2,newShape3,newShape4,
    newShape5,newShape6,newShape7,newShape8 }


public class Shape {



    //shape type
    private ShapeType currShape;
    // coordinate in the coordsTable
    private int coords[][];
    // coordinate to record shape type
    private int[][][] coordsTable;


    //init method
    public Shape() {

        coords = new int[4][2];
        setShape(ShapeType.NoShape);

    }



    // detect which type of shape
    public void setShape(ShapeType shape) {

        coordsTable = new int[][][]{
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}},
                // new shape
                {{-1, 0}, {0, 0}, {0, -1}, {0, 0}},
                {{-1, -1}, {0, 0}, {0, 0}, {1, 0}},
                {{-1, 0}, {0, 0}, {0, 0}, {1, 0}},
                {{0, 0}, {0, 0}, {0, 0}, {1, -1}},

                {{0, 0}, {0, 0}, {0, 0}, {0, -1}},
                {{-1, 0}, {0, -1}, {0, -1}, {1, 0}},
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{-1, 1}, {0, 0}, {0, 0}, {1, -1}},





        };
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        currShape = shape;

    }



    // helper func
    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public ShapeType getCurrShape()  { return currShape; }



    //ramdonlly generate a shape
//    public void setRandomShape()
//    {
//        Random r = new Random();
//        int x = Math.abs(r.nextInt()) % 7 + 1;
//        ShapeType[] values = ShapeType.values();
//        setShape(values[x]);
//    }


    // current shape's lowest horizontal boundary
    public int lowerst_Y_boundary()
    {
        int m = coords[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }




    // method for rotate the currShape

    public Shape rotateLeft()
    {
        if (currShape == ShapeType.SquareShape)
            return this;

        Shape result = new Shape();
        result.currShape = currShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public Shape rotateRight()
    {
        if (currShape == ShapeType.SquareShape)
            return this;

        Shape result = new Shape();
        result.currShape = currShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }



}
