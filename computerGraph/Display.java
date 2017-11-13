package computerGraph;


import com.sun.org.apache.bcel.internal.generic.ICONST;
import sun.jvm.hotspot.debugger.cdbg.Sym;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


/**
 * @class: this class is for the UI drawing
 */
public class Display extends JPanel implements ActionListener, ChangeListener {

    //================================================================================
    // configure  setting
    //================================================================================

    //=====================================
    // params for the whole windows
    //=====================================
    float pixelSize;
    int centerX;
    int centerY;
    float rWidth = 1200;  //(default 1.2)
    float rHeight = 960;

    //=====================================
    // UI for the whole windows
    //=====================================
    private JSlider sliderM;
    private JSlider sliderN;
    private JSlider sliderS;

    //icon img in the menu
    int ICONWIDTH = 70;
    int ICONHEIGHT = 70;

    //=====================================
    // params for the board data
    //=====================================
    int BoardHeight = 15;
    int BoardWidth = 10;
    // a board in the display/game zone to show the Tetris shape
    ShapeType[] board;
    // the coordinate of the board
    int curX = 0;
    int curY = 0;
    Shape currShape; // the shape currently move in the board
    ArrayList<ShapePolygon> currShapeSPolygonSet; //use for detect

    // for the new add
    ArrayList NEWShapeTypelst = new ArrayList();


    //=====================================
    // params for the game
    //====================BoardWidth=================
    boolean GAMEOVER = false;
    boolean NewShapeFall = true;  // init a new shape down
    boolean PAUSE = false;
    Timer timer;    // the timer to control the game process

    //=====================================
    // params for the game process
    //=====================================
    int CURRSHAPE = 0;
    int NEXTSHAPE = 0;

    //=====================================
    // params for data display
    //=====================================
    int Level = 1;
    int Lines = 0;
    int Score = 0;

    //=====================================
    // params for adjust game
    //=====================================
    int scoring_factor = 1;     // M(range: 1-10).
    int level_of_difficulty = 20;    // N (range: 20-50).
    double speed_factor = 0.1;   //  S (range: 0.1-1.0).
    int removed_Rows = 0;
    double falling_speed = 0.1;




    // setting up the  configure(math size)
    private void setConfig() {
        // set up global canvas size
        Dimension d = getSize();
        int maxX = d.width;
        int maxY = d.height;        // boundary-1
        //System.out.println("maxX:" + maxX + "/maxY:" + maxY);

        pixelSize = Math.max(rWidth / maxX, rHeight / maxY);   // insure the square can be show completely
        // System.out.println("pixelSize:" + pixelSize);

        centerX = maxX / 2;
        centerY = maxY / 2;
        // add shape
        NEWShapeTypelst.add(1);
        NEWShapeTypelst.add(2);
        NEWShapeTypelst.add(3);
        NEWShapeTypelst.add(4);
        NEWShapeTypelst.add(5);
        NEWShapeTypelst.add(6);
        NEWShapeTypelst.add(7);

    }



    public  Display (Tetris parent) {
        // init some config (math size)
        setConfig();


        //!!! need currShapeSPolygonSet init !!!
        currShapeSPolygonSet = new ArrayList<>();
        //=====================================
        // game system init
        //=====================================

        int delay = (int) (100 / falling_speed); //timer set up
//        System.out.println("delay:"+delay);
        timer = new Timer(delay, this);
        timer.start();


        this.setLayout(null);  // super important ! cancel the default layout. !!!!!!


        // set slider bottom
        initSlider();
        this.add(sliderM);
        this.add(sliderN);
        this.add(sliderS);

        //set menu
        createMenuBar(parent);

        //=====================================
        // displayZone init
        //=====================================
        currShape = new Shape();//first init the shape to store in the shapeType
        board = new ShapeType[BoardWidth * BoardHeight];//init board coordinate
        initBoard();
        addListener();
        ramdonlyShape("current");



    }


    private void createMenuBar(Tetris parent) {

        JMenuBar menubar = new JMenuBar();
        JMenu Menu1 = new JMenu("Board Size");
        JMenu Menu2 = new JMenu("Shape Size");
        JMenu Menu3 = new JMenu("Add New Shape");
        JMenu Menu4 = new JMenu("System");


        JMenu adjustBoard = new JMenu("Adjust Board Size");
        JMenuItem size1 = new JMenuItem("10 X 15");

        size1.addActionListener((ActionEvent event) -> {
            BoardWidth = 10;
            BoardHeight = 15;
            curX = BoardWidth / 2;
            curY = BoardHeight - 2;
            board = new ShapeType[BoardWidth * BoardHeight];//init board coordinate
            initBoard();
            repaint();




        });
        JMenuItem size2 = new JMenuItem("16 X 24");
        size2.addActionListener((ActionEvent event) -> {
            BoardWidth = 16;
            BoardHeight = 24;
            curX = BoardWidth / 2;
            curY = BoardHeight - 2;
            board = new ShapeType[BoardWidth * BoardHeight];//init board coordinate
            initBoard();
            board = new ShapeType[BoardWidth * BoardHeight];//init board coordinate
            initBoard();
            repaint();
        });
        JMenuItem size3 = new JMenuItem("20 X 30");
        size3.addActionListener((ActionEvent event) -> {
            BoardWidth = 20;
            BoardHeight = 30;
            curX = BoardWidth / 2;
            curY = BoardHeight - 2;
            board = new ShapeType[BoardWidth * BoardHeight];//init board coordinate
            initBoard();
            repaint();
        });
        adjustBoard.add(size1);
        adjustBoard.add(size2);
        adjustBoard.add(size3);





        JMenu adjustSquare = new JMenu("Adjust Square Size");
        JMenuItem square1 = new JMenuItem("small");
        square1.addActionListener((ActionEvent event) -> {
            rWidth = (float) (1000 * 1.5);
            rHeight = (float) (800 * 1.5);
        });
        JMenuItem square2 = new JMenuItem("medium");
        square2.addActionListener((ActionEvent event) -> {
            rWidth = (float) (1000 * 1.2);
            rHeight = (float) (800 * 1.2);
        });
        JMenuItem square3 = new JMenuItem("large");
        square3.addActionListener((ActionEvent event) -> {
            rWidth = (float) (1000 );
            rHeight = (float) (800 );
        });
        adjustSquare.add(square1);
        adjustSquare.add(square2);
        adjustSquare.add(square3);




        JCheckBoxMenuItem shape1 = new JCheckBoxMenuItem();
        ImageIcon img1 = new ImageIcon("images/1.png");
        img1.setImage(img1.getImage().getScaledInstance(ICONWIDTH, ICONHEIGHT,Image.SCALE_DEFAULT));
        shape1.setIcon(img1);
        shape1.addActionListener((ActionEvent event) -> {
            if( !shape1.isSelected()){
                shape1.setSelected(false);
                NEWShapeTypelst.remove(8);
            }else{
                shape1.setSelected(true);
                NEWShapeTypelst.add(8);
            }
        });

        JCheckBoxMenuItem shape2 = new JCheckBoxMenuItem();
        ImageIcon img2 = new ImageIcon("images/2.png");
        img2.setImage(img2.getImage().getScaledInstance(ICONWIDTH, ICONHEIGHT,Image.SCALE_DEFAULT));
        shape2.setIcon(img2);
        shape2.addActionListener((ActionEvent event) -> {
            if( !shape2.isSelected()){
                shape2.setSelected(false);
                NEWShapeTypelst.remove(9);
            }else{
                shape2.setSelected(true);
                NEWShapeTypelst.add(9);
            }
        });
        JCheckBoxMenuItem shape3 = new JCheckBoxMenuItem();
        ImageIcon img3 = new ImageIcon("images/3.png");
        img3.setImage(img3.getImage().getScaledInstance(ICONWIDTH, ICONHEIGHT,Image.SCALE_DEFAULT));
        shape3.setIcon(img3);
        shape3.addActionListener((ActionEvent event) -> {
            if( !shape3.isSelected()){
                shape3.setSelected(false);
                NEWShapeTypelst.remove(10);
            }else{
                shape3.setSelected(true);
                NEWShapeTypelst.add(10);
            }
        });
        JCheckBoxMenuItem shape4 = new JCheckBoxMenuItem();
        ImageIcon img4 = new ImageIcon("images/4.png");
        img4.setImage(img4.getImage().getScaledInstance(ICONWIDTH, ICONHEIGHT,Image.SCALE_DEFAULT));
        shape4.setIcon(img4);
        shape4.addActionListener((ActionEvent event) -> {
            if( !shape4.isSelected()){
                shape4.setSelected(false);
                NEWShapeTypelst.remove(11);
            }else{
                shape4.setSelected(true);
                NEWShapeTypelst.add(11);
            }

        });
        JCheckBoxMenuItem shape5 = new JCheckBoxMenuItem();
        ImageIcon img5 = new ImageIcon("images/5.png");
        img5.setImage(img5.getImage().getScaledInstance(ICONWIDTH, ICONHEIGHT,Image.SCALE_DEFAULT));
        shape5.setIcon(img5);
        shape5.addActionListener((ActionEvent event) -> {
            if( !shape5.isSelected()){
                shape5.setSelected(false);
                NEWShapeTypelst.remove(12);
            }else{
                shape5.setSelected(true);
                NEWShapeTypelst.add(12);
            }
        });
        JCheckBoxMenuItem shape6 = new JCheckBoxMenuItem();
        ImageIcon img6 = new ImageIcon("images/6.png");
        img6.setImage(img6.getImage().getScaledInstance(ICONWIDTH, ICONHEIGHT,Image.SCALE_DEFAULT));
        shape6.setIcon(img6);
        shape6.addActionListener((ActionEvent event) -> {
            if( !shape6.isSelected()){
                shape6.setSelected(false);
                NEWShapeTypelst.remove(13);
            }else{
                shape6.setSelected(true);
                NEWShapeTypelst.add(13);
            }
        });
        JCheckBoxMenuItem shape7 = new JCheckBoxMenuItem();
        ImageIcon img7 = new ImageIcon("images/7.png");
        img7.setImage(img7.getImage().getScaledInstance(ICONWIDTH, ICONHEIGHT,Image.SCALE_DEFAULT));
        shape7.setIcon(img7);
        shape7.addActionListener((ActionEvent event) -> {
            if( !shape7.isSelected()){
                shape7.setSelected(false);
                NEWShapeTypelst.remove(14);
            }else{
                shape7.setSelected(true);
                NEWShapeTypelst.add(14);
            }
        });
        JCheckBoxMenuItem shape8 = new JCheckBoxMenuItem();
        ImageIcon img8 = new ImageIcon("images/8.png");
        img8.setImage(img8.getImage().getScaledInstance(ICONWIDTH, ICONHEIGHT,Image.SCALE_DEFAULT));
        shape8.setIcon(img8);
        shape8.addActionListener((ActionEvent event) -> {
            if( !shape8.isSelected()){
                shape8.setSelected(false);
                NEWShapeTypelst.remove(15);
            }else{
                shape8.setSelected(true);
                NEWShapeTypelst.add(15);
            }
        });


        Menu3.add(shape1);
        Menu3.add(shape2);
        Menu3.add(shape3);
        Menu3.add(shape4);
        Menu3.add(shape5);
        Menu3.add(shape6);
        Menu3.add(shape7);
        Menu3.add(shape8);





        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });


        Menu1.add(adjustBoard);
        Menu2.add(adjustSquare);

        Menu4.add(exitItem);

        menubar.add(Menu1);
        menubar.add(Menu2);
        menubar.add(Menu3);
        menubar.add(Menu4);
        parent.setJMenuBar(menubar);
    }


    private void initSlider() {

        sliderM = new JSlider(JSlider.VERTICAL, 1, 10, 1);
        sliderM.setMinorTickSpacing(1);
        sliderM.setMajorTickSpacing(10);
        sliderM.setPaintTicks(true);
        sliderM.addChangeListener(this);
//        sliderM.setBounds(iX(650),iX(240),iWidth(100),iHeight(150));


//        System.out.println(iX(650));
//        System.out.println(iX(240));
        sliderN = new JSlider(JSlider.VERTICAL, 20, 50, 20);
        sliderN.setMinorTickSpacing(1);
        sliderN.setMajorTickSpacing(10);
        sliderN.setPaintTicks(true);
        sliderN.addChangeListener(this);
//        sliderN.setBounds(iX(750),iY(240),iWidth(100),iHeight(150));


        sliderS = new JSlider(JSlider.VERTICAL, 1, 10, 1);
        sliderS.setMinorTickSpacing(1);
        sliderS.setMajorTickSpacing(10);
        sliderS.setPaintTicks(true);
        sliderS.addChangeListener(this);
//        sliderS.setBounds(iX(850),iY(240),iWidth(100),iHeight(150));


    }

    @Override
    public void stateChanged(ChangeEvent e) {

        if (e.getSource() == sliderM) {
            scoring_factor = sliderM.getValue();
        } else if (e.getSource() == sliderN) {
            level_of_difficulty = sliderN.getValue();
        } else if (e.getSource() == sliderS) {
            speed_factor = (double) sliderS.getValue() / 10.0;
        }
        repaint();
    }


//================================================================================
    // drawine shape ,and shape function
    //================================================================================

    private void ramdonlyShape(String s) {
        //randomly get the next shape
        Random r = new Random();
        int i = Math.abs(r.nextInt()) % 15 + 1;;

        while (!NEWShapeTypelst.contains(i)){
           i = Math.abs(r.nextInt()) % 15 + 1;
        }
        if (s == "current") {
            CURRSHAPE = i;
        } else if (s == "next") {
            NEXTSHAPE = i;
        }
    }


    // the timer will use this method every delay time
    @Override
    public void actionPerformed(ActionEvent e) {



        if (PAUSE) {
            return;
        }
        if (NewShapeFall) {
            NewShapeFall = false;
            newShape();
        } else {
            oneLineDown();
        }


    }





    // the shape down right now
    private void dropDown()
    {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(currShape, curX, newY - 1))
                break;
            --newY;
        }
        TouchDown();
    }


    private void oneLineDown() {
        if (!tryMove(currShape, curX, curY - 1))
            TouchDown();
    }

    // the new shape touch the bottom
    private void TouchDown() {
        // once the current is touchdown, it get the shape type info from next
        CURRSHAPE = NEXTSHAPE;
        for (int i = 0; i < 4; ++i) {
            int x = curX + currShape.x(i);
            int y = curY - currShape.y(i);
            board[(y * BoardWidth) + x] = currShape.getCurrShape();
        }

        removeFullLines();

        if (!NewShapeFall)

            newShape();
    }

    private void removeFullLines() {


        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == ShapeType.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                // update a new line score
                Lines = Lines + 1;
                removed_Rows = removed_Rows + 1;
                Score = Score + Level * scoring_factor;

                if (removed_Rows == level_of_difficulty) {
                    Level = Level + 1;
                    falling_speed = falling_speed * (1 + Level * speed_factor);
                    removed_Rows = 0;
                }
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                        board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            NewShapeFall = true;
            currShape.setShape(ShapeType.NoShape);
            repaint();
        }
    }


    private void newShape() {
        ramdonlyShape("next");
        ShapeType[] values = ShapeType.values();
        currShape.setShape(values[CURRSHAPE]);



        // get the next shape from the next zone
        curX = BoardWidth / 2;
        curY = BoardHeight - 2 + currShape.lowerst_Y_boundary();


        //ends the game
        if (!tryMove(currShape, curX, curY)) {
            currShape.setShape(ShapeType.NoShape);
            timer.stop();
            System.out.println("game over");
            NewShapeFall = false;
            GAMEOVER = true;
            repaint();
        }
    }

    private boolean tryMove(Shape newShape, int newX, int newY) {


        for (int i = 0; i < 4; ++i) {
            int x = newX + newShape.x(i);
            int y = newY - newShape.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != ShapeType.NoShape)
                return false;
        }


        currShape = newShape;

        curX = newX;
        curY = newY;
//        System.out.println("tryMove.curX:" + curX);
//        System.out.println("tryMove.curY:" + curY);
        repaint();
        return true;
    }


    private void initBoard() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i] = ShapeType.NoShape;
    }


    //================================================================================
    // board  painting
    //================================================================================
    @Override
    public void paint(Graphics g) {
//        System.out.println("public void paint(Graphics g) {");
        super.paint(g);


        //=====================================
        // next shape zone
        //=====================================
        if (NEXTSHAPE != 0) {
            ShapeType[] values = ShapeType.values();
            Shape NextShape = new Shape();
            NextShape.setShape(values[NEXTSHAPE]);
            for (int i = 0; i < 4; ++i) {
                int x = NextShape.x(i);
                int y = NextShape.y(i);
                drawUnitSquare(g, 650 + x * 50,
                        80 + y * 50,
                        NextShape.getCurrShape(), "next");
            }


        }





        //=====================================
        // board zone
        //=====================================
        // draw the fixed shape in the board
        int boardTop = 750 - BoardHeight * squareSize()+11;
//        int boardTop = 11;// because the top of the display board is from 10

        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                ShapeType shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != ShapeType.NoShape)
                    drawUnitSquare(g, 1+j * squareSize(),
                            boardTop + i * squareSize(), shape, "unit");
            }
        }


//        Point[] shapePoint = new Point[];
        ArrayList<ArrayList<Point>> PointSet = new ArrayList<>();
        currShapeSPolygonSet = new ArrayList<>();
        // draw the Shape & Shape polygon
        if (currShape.getCurrShape() != ShapeType.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + currShape.x(i);
                int y = curY - currShape.y(i);
                drawUnitSquare(g, 1+x * squareSize(),
                        boardTop + (BoardHeight - y - 1) * squareSize(),
                        currShape.getCurrShape(), "unit");


                x = iX(1+x * squareSize());
                y = iY(boardTop + (BoardHeight - y - 1) * squareSize());


                int squareSize = iWidth(squareSize());
                ArrayList<Point> templst = new ArrayList<>();
                templst.add(new Point(x, y));
                templst.add(new Point(x, y + squareSize -1 ));
                templst.add(new Point(x + squareSize -1, y + squareSize-1 ));
                templst.add(new Point(x + squareSize-1, y));


                PointSet.add(new ArrayList<>(templst));

            }

            for ( ArrayList<Point> point: PointSet){

                Point[] shapeBoundary = new Point[4];
                for (int i = 0; i < shapeBoundary.length; i++){
                    shapeBoundary[i] = point.get(i);
                }


                ShapePolygon  s= new ShapePolygon(shapeBoundary);

                currShapeSPolygonSet.add(s);
            }
        }









        //=====================================
        //PAUSE situation
        //=====================================
        if (PAUSE) {
            //set font
            Font font = new Font("Helvetica", Font.BOLD, Math.round(20 / pixelSize));
            g.setFont(font);
            g.setColor(new Color(39, 113, 186));
            g.drawRect(iX(145), iY(300), iWidth(250), iHeight(40f));
            g.drawString("PAUSE", iX(237), iY(330));
            g.setColor(Color.BLACK);
        }

        //=====================================
        //GAMEOVER situation
        //=====================================
        if (GAMEOVER) {
            //set font
            Font font = new Font("Helvetica", Font.BOLD, Math.round(30 / pixelSize));
            g.setFont(font);
            g.setColor(new Color(0, 0, 0));
            g.drawString("GAMEOVER", iX(200), iY(330));

        }

    }


    //================================================================================
    // Draw Shape
    //================================================================================
    // adjust the Shape_unit size according to the boardSize
    int squareSize() {
        return 500 / BoardWidth;
    }
//    int squareHeight() { return iHeight(750 /  BoardHeight); }

    // reach the shape Type in the board. the x,y is the order. not coordinate
    ShapeType shapeAt(int x, int y) {
        return board[(y * BoardWidth) + x];
    }


    // draw a unit square, the color according to the shape.
    private void drawUnitSquare(Graphics g, int x, int y, ShapeType shape, String s) {
        //store the color provide
        Color colors[] = {
                new Color(0, 0, 0),
                new Color(112, 48, 160),
                new Color(255, 255, 0),
                new Color(0, 176, 240),
                new Color(255, 192, 0),
                new Color(0, 176, 8),
                new Color(255, 0, 0),
                new Color(0, 112, 192),
                //new  shape
                new Color(165, 165, 165),
                new Color(159, 205, 99),
                new Color(208, 152, 150),
                new Color(214, 114, 46),

                new Color(198, 212, 161),
                new Color(146, 136, 90),
                new Color(29, 55, 90),
                new Color(70, 131, 152),





        };
        Color color = colors[shape.ordinal()];
        g.setColor(color);

        //  trans the coordinate
        x = iX(x);
        y = iY(y);
        int squareSize = 50;
        if (s == "next") {
            squareSize = iWidth(50);
        } else if (s == "unit") {
            squareSize = iWidth(squareSize());
        }


        //draw inside
        g.fillRect(x + 1, y + 1, squareSize - 2, squareSize - 2);

        //draw line
        g.setColor(color.brighter());

        g.drawLine(x, y + squareSize - 1, x, y);
        g.drawLine(x, y, x + squareSize - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareSize - 1, x + squareSize - 1, y + squareSize - 1);
        g.drawLine(x + squareSize - 1, y + squareSize - 1, x + squareSize - 1, y + 1);
    }


    // method for coordinate
    int iX(float x) {
        return Math.round(centerX - (400 - x) / pixelSize);
    }

    int iY(float y) {
        return Math.round(centerY - (400 - y) / pixelSize);
    }// translate y for top-bottom

    int iWidth(float x) {
        return Math.round(x / pixelSize);
    }

    int iHeight(float y) {
        return Math.round(y / pixelSize);
    }


    //================================================================================
    // Draw background UI
    //================================================================================


    private void paintBackround(Graphics g) {
//        System.out.println("private void paintBackround(Graphics g) {");
        setConfig();


        sliderM.setBounds(iX(570f), iY(260), iWidth(100), iHeight(150));
        sliderN.setBounds(iX(670), iY(260), iWidth(100), iHeight(150));
        sliderS.setBounds(iX(770), iY(260), iWidth(100), iHeight(150));
        updateUI();


        // display game parameters zone
        //set state
        //set font
        Font font1 = new Font("Helvetica", Font.BOLD, Math.round(12 / pixelSize));
        g.setFont(font1);
        g.drawString("Scoring Factor:  " + scoring_factor, iX(570f), iY(450f));
        g.drawString("Difficulty:  " + level_of_difficulty, iX(690f), iY(450f));
        g.drawString("Speed Factor:  " + speed_factor, iX(780f), iY(450f));

        //set font
        Font font2 = new Font("Helvetica", Font.BOLD, Math.round(20 / pixelSize));
        g.setFont(font2);


        // display zone
        g.drawRect(iX(0f), iY(10f), iWidth(500), iHeight(750));

        // next shape zone
        g.drawRect(iX(555f), iY(10f), iWidth(320f), iHeight(240f));

        //quit function
        g.drawRect(iX(555f), iY(720f), iWidth(110f), iHeight(40f));
        g.drawString("QUIT", iX(585f), iY(745f));

        //set state
        g.drawString("Level:" + "         " + Level, iX(595f), iY(500f));
        g.drawString("Lines:" + "         " + Lines, iX(595f), iY(550f));
        g.drawString("Score:" + "        " + Score, iX(595f), iY(600f));


    }

    //================================================================================
    // addListener for the game
    //================================================================================
    void addListener() {


        //  mouseMoved in display zone
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent evt) {

                int xp = evt.getX();
                int yp = evt.getY();
                // in the display zone
                if (!GAMEOVER && xp > iX(20f) && xp < iX(20) + iWidth(525) && yp > iY(10f) && yp < iY(10) + iHeight(770)) {
                    PAUSE = true;
                    repaint();
                } else {
                    PAUSE = false;
                    // repaint();
                }

            }

        });


        // mouse button click and wheel move
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {

                // mouseClicked for QUITE bottom // in the display zone
                int xp = evt.getX();
                int yp = evt.getY();
                if (xp > iX(555f) && xp < iX(555) + iWidth(110) && yp > iY(720f) && yp < iY(720) + iHeight(40)) {
                    System.exit(0);
                }

                boolean Click = true;  // just react one click
                Point clickPoint = new Point(xp,yp);
                for (ShapePolygon polygon: currShapeSPolygonSet){
                    if (polygon.contains(clickPoint) && Click){
                        for (Point p:polygon.points ){

                            System.out.print("x:" + p.x);
                            System.out.println("y:" + p.y);
                            System.out.println("------");
                        }

                        System.out.print("xp:" + xp);
                        System.out.println("yp:" + yp);


                        System.out.println("click");
                        // is in the polygon
                        Score = Score - Level * scoring_factor;
//                        newShape();
                        Click = false;
                        // in case the next is equal to the current shape
                        while (CURRSHAPE == NEXTSHAPE){
                            ramdonlyShape("next");
                        }
                        CURRSHAPE = NEXTSHAPE;
                        ramdonlyShape("next");
                        ShapeType[] values = ShapeType.values();
                        currShape.setShape(values[CURRSHAPE]);
                        repaint();
                    }
                }





                if (evt.getModifiers() == MouseEvent.BUTTON1_MASK && !PAUSE) {
//                    System.out.print("left");
                    tryMove(currShape, curX - 1, curY);

                } else if (evt.getModifiers() == MouseEvent.BUTTON3_MASK && !PAUSE) {
//                    System.out.print("right");
                    tryMove(currShape, curX + 1, curY);
                }
            }
        });


        addMouseWheelListener(new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent evt) {
                if (evt.getWheelRotation() == 1 && !PAUSE) {
                    //System.out.println("wheel forward");
                    tryMove(currShape.rotateRight(), curX, curY);
                }
                if (evt.getWheelRotation() == -1 && !PAUSE) {
                    //System.out.println("wheel back");
                    tryMove(currShape.rotateLeft(), curX, curY);
                }
            }

        });


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackround(g);
    }



  

}






