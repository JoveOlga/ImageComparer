package comparer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class ImageComparer {
    private final static double MAX_DIFF = 25.5;
    private final static double MAX_DISTANCE = 3;

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedImage img = ImageIO.read(new File("res/1.png"));
            BufferedImage img2 = ImageIO.read(new File("res/2.png"));

            int width = img.getWidth();
            int height = img.getHeight();
            int width2 = img2.getWidth();
            int height2 = img2.getHeight();

            if (width != width2 || height != height2) {
                return;
            }

            int rgbHighlight = Color.ORANGE.getRGB();
            int[] p = img.getRGB(0, 0, width, height, null, 0, width);
            int[] p2 = img2.getRGB(0, 0, width, height, null, 0, width);

            BufferedImage tmpBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int[] buffer = new int[width * height];

            tmpBufferedImage.setRGB(0, 0, width, height, buffer, 0, width);

            for (int i = 0; i < p.length; i++) {
                if (p[i] != p2[i]) {
                    buffer[i] = rgbHighlight;
                }
            }


            tmpBufferedImage.setRGB(0, 0, width, height, buffer, 0, width);


            List<PicturePoint> points = getPoints(tmpBufferedImage, width, height, rgbHighlight);
            List<List> lists = getListsOfPoints(points);

            List<PicturePoint[]> rectangles = new ArrayList<>();
            for (List<List> list: lists) {
                rectangles.add(getMaxXY(list));
            }

            Color color1 = new Color(0, 0,0);
            Color color2 = new Color(255, 255,255);

            System.out.println(color1.getRGB() + "  " + color2.getRGB());

            printDiffRectangles(img2, rectangles);

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static List getPoints(BufferedImage bufferedImage, int width, int height, int rgb) {

        List<PicturePoint> points = new ArrayList<PicturePoint>();

        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height - 1; y++) {
                if (bufferedImage.getRGB(x, y) == rgb ) {
                    points.add(new PicturePoint(x, y));
                }
            }
        }

        return points;
    }

    public static List getListsOfPoints(List<PicturePoint> points) {
        List<List> lists = new ArrayList<>();

        List<PicturePoint> setPoints;
        Iterator<PicturePoint> pointsIterator;
        while (points.size() > 0) {
            setPoints = new ArrayList<>();
            setPoints.add(points.get(0));
            for (int i = 0; i < setPoints.size(); i++) {
                PicturePoint pointR = setPoints.get(i);
                pointsIterator = points.iterator();
                while (pointsIterator.hasNext()) {
                    PicturePoint point = pointsIterator.next();
                    if (point.getDistance(pointR) <= MAX_DISTANCE) {
                        if (!setPoints.contains(point)) {
                            setPoints.add(point);
                        }
                        pointsIterator.remove();
                    }
                }
            }
            lists.add(setPoints);
        }

        return lists;
    }

    public static PicturePoint[] getMaxXY(List pointsList){
        int xMax = 0;
        int yMax = 0;
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;

        for (int i = 0; i < pointsList.size(); i++ ) {
            PicturePoint point = (PicturePoint) pointsList.get(i);
            if (point.x > xMax) {
                xMax = point.x;
            }
            if (point.y > yMax) {
                yMax = point.y;
            }
            if (point.x < xMin) {
                xMin = point.x;
            }
            if (point.y < yMin) {
                yMin = point.y;
            }
        }

        PicturePoint[] maxXY = {new PicturePoint(xMax, yMax), new PicturePoint(xMin, yMin)};

        return maxXY;
    }

    public static void printDiffRectangles(BufferedImage bufferedImage, List<PicturePoint[]> rectangles) {
        int rgb = Color.RED.getRGB();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();



        for (PicturePoint[] rectangle: rectangles) {
            for(int i = rectangle[1].x - 2 ; i < rectangle[0].x + 3; i++){
                bufferedImage.setRGB(i, rectangle[1].y - 2, rgb);
                bufferedImage.setRGB(i, rectangle[0].y + 2, rgb);
            }
            for(int j = rectangle[1].y - 2 ; j < rectangle[0].y + 2; j++){
                bufferedImage.setRGB(rectangle[1].x - 2, j, rgb);
                bufferedImage.setRGB(rectangle[0].x + 2, j, rgb);
            }
        }

        try {
            ImageIO.write(bufferedImage, "png", new File("res/outputfile1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
