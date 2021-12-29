import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Program {
    static int size = 0;
    static Stack<Point> stack = new Stack();
    static boolean isMoreThanOneSpace = false;
    static Point pointFind;
    static ArrayList<Point> maxPoint = new ArrayList<>();
    static int tempoSizeCell = 0;
    static int maxSizeCell = 0;

    public static void main(String[] args) throws FileNotFoundException {

        PrintWriter out = new PrintWriter(System.out);
        var matrix = readFIle();
        printMatrix(matrix);
        var arrayPoints = initArrayPoints(matrix);

        var resultWhite = SolveTask("W", arrayPoints);
        maxSizeCell = 0;
        maxPoint = new ArrayList<>();
        var resultBlack = SolveTask("B", arrayPoints);
        System.out.println(resultWhite);
        System.out.println(resultBlack);

    }
    private static String[][] readFIle() throws FileNotFoundException{
        Scanner in=new Scanner(new File("input.txt"));
        var matrix=new String[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if (i == 0 || j == 0 || i == 9 || j == 9){
                    matrix[i][j] = ".";
                }
                else {
                    matrix[i][j] = " ";
                }
            }
        }

        int x=in.nextInt();
        int y=in.nextInt();

        while(x!=0&&y!=0){
            matrix[y][x]="W";
            size+=1;
            x=in.nextInt();
            y=in.nextInt();
        }

        x=in.nextInt();
        y=in.nextInt();

        while(x!=0&&y!=0){
            matrix[y][x]="B";
            size+=1;
            x=in.nextInt();
            y=in.nextInt();
        }

        return matrix;
    }

    private static void printMatrix(String[][] matrix){

        System.out.println("  0 1 2 3 4 5 6 7 8 9");

        for (var j = 0; j <10; j++){
            System.out.print(j);
            for (var i = 0; i < 10; i++)
                System.out.print(" " + matrix[j][i]);
            System.out.print("\n");
        }
    }

    private static Point[] initArrayPoints(String[][] matrix) {

        var arrayPoint = new ArrayList<Point>();

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix.length; x++){

                if (matrix[y][x].equals(" "))
                    continue;

                var point = new Point(y, x , " ");

                if (matrix[y][x].equals("B"))
                    arrayPoint.add(point = new Point(x, y, "B"));

                if (matrix[y][x].equals("W"))
                    arrayPoint.add(point = new Point(x, y, "W"));

                if (matrix[y][x].equals("."))
                    arrayPoint.add(point = new Point(x, y, "."));

                AddNeighbor(1, 0, point, matrix, arrayPoint);
                AddNeighbor(-1, 0, point, matrix, arrayPoint);
                AddNeighbor(0, 1, point, matrix, arrayPoint);
                AddNeighbor(0, -1, point, matrix, arrayPoint);
            }
        }

        return arrayPoint.toArray(new Point[arrayPoint.size()]);
    }

    private static void AddNeighbor(int indexX, int indexY, Point point, String[][] matrix, ArrayList<Point> arrayPoint) {
        Point neighborPoint;
        if (point.x + indexX >= matrix.length ||
            point.y + indexY >= matrix.length ||
            point.x + indexX < 0 ||
            point.y + indexY < 0) {
            return;
        }
        if (matrix[point.y + indexY][point.x + indexX].equals(" ")) {
            return;
        }

        neighborPoint = new Point(point.x + indexX, point.y + indexY, matrix[point.y + indexY][point.x + indexX]);
        if (!arrayPoint.contains(neighborPoint)){
            return;
        }

        switch (indexX) {
            case -1 -> {
                arrayPoint.get(arrayPoint.indexOf(point)).neighborLeft = arrayPoint.get(arrayPoint.indexOf(neighborPoint));
                arrayPoint.get(arrayPoint.indexOf(neighborPoint)).neighborRight = arrayPoint.get(arrayPoint.indexOf(point));
            }
            case 1 -> {
                arrayPoint.get(arrayPoint.indexOf(point)).neighborRight = arrayPoint.get(arrayPoint.indexOf(neighborPoint));
                arrayPoint.get(arrayPoint.indexOf(neighborPoint)).neighborLeft = arrayPoint.get(arrayPoint.indexOf(point));
            }
            default -> {
            }
        }

        switch (indexY) {
            case -1 -> {
                arrayPoint.get(arrayPoint.indexOf(point)).neighborUp = arrayPoint.get(arrayPoint.indexOf(neighborPoint));
                arrayPoint.get(arrayPoint.indexOf(neighborPoint)).neighborBottom = arrayPoint.get(arrayPoint.indexOf(point));
            }
            case 1 -> {
                arrayPoint.get(arrayPoint.indexOf(point)).neighborBottom = arrayPoint.get(arrayPoint.indexOf(neighborPoint));
                arrayPoint.get(arrayPoint.indexOf(neighborPoint)).neighborUp = arrayPoint.get(arrayPoint.indexOf(point));
            }
            default -> {
            }
        }
    }

    private static boolean CheckPoint(Point point, String name, Point[] arrayPoints) {
        if (point == null) {
            if (pointFind != null) isMoreThanOneSpace = true;
            else return true;
            return false;
        }

        if (point.name.equals(name)) {
            for (Point arrayPoint : arrayPoints)
                if (arrayPoint.equals(point))
                    arrayPoint.name = "V";
            stack.push(point);
            }
        return false;
    }

    public static String SolveTask(String name, Point[] arrayPoints) {

        while (true) {
            Point firstPoint = null;

            for (int i =0; i < arrayPoints.length; i++){
                if (arrayPoints[i].name.equals(name)) {
                    firstPoint = arrayPoints[i];
                    arrayPoints[i].name = "V";
                    break;
                }
            }

            if (firstPoint == null) {
                break;
            }
            tempoSizeCell = 0;
            pointFind = null;
            isMoreThanOneSpace = false;
            stack.push(firstPoint);

            while (stack.size() != 0) {
                tempoSizeCell += 1;
                var point = stack.pop();

                if (CheckPoint(point.neighborLeft, name, arrayPoints)){
                    pointFind = new Point(point.x - 1, point.y, "B");
                }

                if (CheckPoint(point.neighborRight, name, arrayPoints)) {
                    pointFind = new Point( point.x + 1, point.y, "B");
                }

                if (CheckPoint(point.neighborBottom, name, arrayPoints)) {
                    pointFind = new Point( point.x, point.y + 1, "B");
                }
                if (CheckPoint(point.neighborUp, name, arrayPoints)) {
                    pointFind = new Point( point.x, point.y + 1, "B");
                }

                if (isMoreThanOneSpace) {
                     while (stack.size() != 0) {
                         var p = stack.pop();
                         for (int i =0; i < arrayPoints.length; i++){
                             if (arrayPoints[i].equals(p)){
                                 arrayPoints[i].name = "V";
                             }
                         }
                     }
                }
            }
            if (!isMoreThanOneSpace) {
                if (maxSizeCell <= tempoSizeCell) {
                    maxPoint.add(pointFind);
                    System.out.println("Найдена брешь " + pointFind);
                    maxSizeCell = tempoSizeCell;
                }

            }
        }
        if (maxPoint.size() == 0){
            return "N";
        }
        StringBuilder result = new StringBuilder();
        for (var cell : maxPoint){
            result.append(cell.x).append(" ").append(cell.y).append(" ");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}