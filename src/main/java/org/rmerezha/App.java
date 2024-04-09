package org.rmerezha;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    private static final Pane PANE = new Pane();
    private static final double WIDTH = 1200;
    private static final double HEIGHT = 800;

    private static final double RADIUS = 20;

    public static final int NUM_CIRCLES = 11;

    private final Point APEX_OF_TRIANGLE_1 = new Point(100, 700);
    private final Point APEX_OF_TRIANGLE_2 = new Point(700, 700);
    private final Point APEX_OF_TRIANGLE_3 = new Point(400, 200);

    private static int counter;

    @Override
    public void start(Stage stage) {

        System.out.println("Enter 0 or 1:");
        Scanner sc = new Scanner(System.in);

        int i = sc.nextInt();

        List<Point> points = drawCircles();
        Graph graph = new Graph(3414);
        double[][] matrix = graph.generatedAdjacencyMatrix(NUM_CIRCLES);

        if (i == 1) {
            drawOrientedGraphs(matrix, points);
        } else if (i == 0) {
            drawNonOrientedGraphs(matrix, points);
        } else {
            throw new IllegalArgumentException();
        }

        int size = matrix.length;
        for (int j = 0; j < size; j++) {
            System.out.println(Arrays.toString(matrix[j]));
        }

        setDefaultSettings(stage);
        stage.show();

    }

    private void drawNonOrientedGraphs(double[][] matrix, List<Point> points) {

        int size = matrix.length;

        for (int i = 0; i < size * size; i++) {
            int q = i % size;
            int w = i / size;

            if (matrix[q][w] == 1) {
                matrix[w][q] = 1;

                if (q >= w) {
                    drawArc(points, w + 1, q + 1, false);
                } else {
                    drawArc(points, q + 1, w + 1, false);
                }

            }
        }



    }

    private void drawOrientedGraphs(double[][] matrix, List<Point> points) {

        int size = matrix.length;

        for (int i = 0; i < size * size; i++) {
            int q = i % size;
            int w = i / size;

            if (matrix[q][w] == 1) {
                drawArc(points, q + 1, w + 1, true);
            }
        }


    }

    private Point findHeight(Point p1, Point p2, double heightLength) {

        double centerX = (p1.x() + p2.x()) / 2;
        double centerY = (p1.y() + p2.y()) / 2;

        double dx = p2.x() - p1.x();
        double dy = p2.y() - p1.y();


        double length = Math.sqrt(dx * dx + dy * dy);
        dx /= length;
        dy /= length;


        return new Point(centerX + heightLength / 2 * dy, centerY - heightLength / 2 * dx);
    }


    private void setDefaultSettings(Stage stage) {
        Scene scene = new Scene(PANE, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Graphs");
    }

    private void createCircle(Point point) {

        Circle circle = new Circle();
        circle.setCenterX(point.x());
        circle.setCenterY(point.y());
        circle.setRadius(RADIUS);
        circle.setFill(Color.AQUA);

        Label label = new Label(Integer.toString(++counter));
        label.setFont(new Font(20));
        label.setTextFill(Paint.valueOf("GREEN"));
        if (counter >= 10) {
            label.setLayoutX(point.x() - 13);
            label.setLayoutY(point.y() - 15);
        } else {
            label.setLayoutX(point.x() - 7);
            label.setLayoutY(point.y() - 15);
        }
        PANE.getChildren().addAll(circle, label);
    }

    private void createCircle(Point point, List<Point> points) {
        createCircle(point);
        points.add(point);
    }

    private List<Point> drawCircles() {
        List<Point> points = new ArrayList<>();
        int[] arr = distribution(NUM_CIRCLES);

        drawTriangleEdge(APEX_OF_TRIANGLE_1, APEX_OF_TRIANGLE_2, arr[0], points);
        drawTriangleEdge(APEX_OF_TRIANGLE_2, APEX_OF_TRIANGLE_3, arr[1], points);
        drawTriangleEdge(APEX_OF_TRIANGLE_3, APEX_OF_TRIANGLE_1, arr[2], points);

        return points;
    }

    private void drawTriangleEdge(Point p1, Point p2, int countDistributionCircles, List<Point> points) {

        if (!points.contains(p1)) {
            createCircle(p1, points);
        }

        for (int i = 1; i <= countDistributionCircles; i++) {
            double interpolatedX = p1.x() + (p2.x() - p1.x()) * i / (countDistributionCircles + 1);
            double interpolatedY = p1.y() + (p2.y() - p1.y()) * i / (countDistributionCircles + 1);
            createCircle(new Point(interpolatedX, interpolatedY), points);
        }

        if (!points.contains(p2)) {
            createCircle(p2, points);
        }
    }

    private double distance(Point p1, Point p2) {
        Point2D pp1 = new Point2D(p1.x(), p1.y());
        Point2D pp2 = new Point2D(p2.x(), p2.y());
        return pp1.distance(pp2);
    }

    private void drawArc(Point p1, Point p2, double k, boolean withArrowHead) {
        Path path = new Path();
        path.setStroke(Color.BLACK);
        path.setStrokeWidth(1);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(p1.x());
        moveTo.setY(p1.y());
        Point height = findHeight(p1, p2, distance(p1, p2) / k);
        CubicCurveTo cubicCurveTo = new CubicCurveTo();
        cubicCurveTo.setControlX1(p1.x());
        cubicCurveTo.setControlY1(p1.y());
        cubicCurveTo.setControlX2(height.x());
        cubicCurveTo.setControlY2(height.y());
        cubicCurveTo.setX(p2.x());
        cubicCurveTo.setY(p2.y());

        path.getElements().add(moveTo);

        path.getElements().add(cubicCurveTo);

        if (withArrowHead) {
            drawArrowHead(height, p2);
        }

        PANE.getChildren().add(path);
    }

    private void drawArrowHead(Point startPoint, Point endPoint) {

        double arrowSize = 10;
        double angle = Math.atan2(endPoint.y() - startPoint.y(), endPoint.x() - startPoint.x());


        double angleOffset = Math.PI / 6;
        double x1 = endPoint.x() - arrowSize * Math.cos(angle - angleOffset);
        double y1 = endPoint.y() - arrowSize * Math.sin(angle - angleOffset);

        double x2 = endPoint.x() - arrowSize * Math.cos(angle + angleOffset);
        double y2 = endPoint.y() - arrowSize * Math.sin(angle + angleOffset);

        // Draw the arrowhead
        Path arrowhead = new Path();
        arrowhead.setStroke(Color.BLACK);
        arrowhead.setStrokeWidth(1);
        arrowhead.setFill(Color.BLACK);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(endPoint.x());
        moveTo.setY(endPoint.y());

        LineTo lineTo1 = new LineTo();
        lineTo1.setX(x1);
        lineTo1.setY(y1);

        LineTo lineTo2 = new LineTo();
        lineTo2.setX(x2);
        lineTo2.setY(y2);

        arrowhead.getElements().addAll(moveTo, lineTo1, lineTo2, new ClosePath());

        PANE.getChildren().add(arrowhead);
    }
    private int[] distribution(int num) {
        int count = (num - 3) / 3;

        if (num % 3 == 0) {
            return new int[]{count, count, count};
        } else if (num % 3 == 1) {
            return new int[]{count + 1, count, count};
        } else {
            return new int[]{count, count + 1, count + 1};
        }
    }

    void drawArc(List<Point> points, int i1, int i2, boolean withArrowHead) {
        if (i1 == i2) {
            createCirclePath(points.get(i1 - 1));
            return;
        }

        var line1 = new Line(
                APEX_OF_TRIANGLE_1.x(), APEX_OF_TRIANGLE_1.y(),
                APEX_OF_TRIANGLE_2.x(), APEX_OF_TRIANGLE_2.y()
        );
        var line2 = new Line(
                APEX_OF_TRIANGLE_2.x(), APEX_OF_TRIANGLE_2.y(),
                APEX_OF_TRIANGLE_3.x(), APEX_OF_TRIANGLE_3.y()
        );
        var line3 = new Line(
                APEX_OF_TRIANGLE_3.x(), APEX_OF_TRIANGLE_3.y(),
                APEX_OF_TRIANGLE_1.x(), APEX_OF_TRIANGLE_1.y()
        );

        var p1 = points.get(i1 - 1);
        var p2 = points.get(i2 - 1);


        if (onOneLine(line3, p1, p2)) {
            if (i1 == 1) {
                int k = (points.size() + 1 - i2);
                drawArc(p1, p2, k == 1 ? 2 : k - 1, withArrowHead);
            } else if (i2 == 1) {
                int k = (points.size() + 1 - i1);
                drawArc(p1, p2, k == 1 ? 2 : k - 1, withArrowHead);
            } else {
                int k = Math.abs(i1 - i2);
                drawArc(p1, p2, k == 1 ? 2 : k, withArrowHead);
            }

        } else if (onOneLine(line2, p1, p2) || onOneLine(line1, p1, p2)) {
            int k = Math.abs(i1 - i2);
            drawArc(p1, p2, k == 1 ? 2 : k - 1, withArrowHead);
        } else {
            int k = Math.abs(i1 - i2);
            drawArc(p1, p2, k == 1 ? 2 : k + 3, withArrowHead);
        }


    }

    private void createCirclePath(Point p) {
        Path path = new Path();

        path.getElements().add(new MoveTo(p.x() - RADIUS, p.y()));

        // Рисуем окружность
        for (int angle = 1; angle <= 360; angle++) {
            double x = p.x() - 2 * RADIUS + RADIUS * Math.cos(Math.toRadians(angle));
            double y = p.y() + RADIUS * Math.sin(Math.toRadians(angle));
            path.getElements().add(new LineTo(x, y));
        }

        path.getElements().add(new ClosePath());

        PANE.getChildren().add(path);
    }

    private boolean onOneLine(Line line, Point p1, Point p2) {
        return line.contains(p1.x(), p1.y()) && line.contains(p2.x(), p2.y());
    }



}
