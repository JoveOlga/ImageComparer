package comparer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ImageComparer {
    private final static double MAX_DIFF = 1677721.6;
    private final static double MAX_DISTANCE = 3;

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(new File("res/1.png"));
            BufferedImage imageForComparison = ImageIO.read(new File("res/2.png"));

            int width = image.getWidth();
            int height = image.getHeight();
            int widthForComparison = imageForComparison.getWidth();
            int heightForComparison = imageForComparison.getHeight();

            if (width != widthForComparison || height != heightForComparison) {
                return;
            }

            int[] imageRGB = image.getRGB(0, 0, width, height, null, 0, width);
            int[] imageRGBForComparison = imageForComparison.getRGB(0, 0, width, height, null, 0, width);

            List<PicturePoint> differentPoints = getDifferentPoints(imageRGB, imageRGBForComparison, width);

            List<List> setsOfDifferentPoints = getSetsOfPoints(differentPoints);

            printDiffRectangles(imageForComparison, setsOfDifferentPoints);

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static List getDifferentPoints(int[] rgbArr, int[] rgbArr2, int width) {
        List<PicturePoint> points = new ArrayList<PicturePoint>();

        int x = 0;
        int y = 0;
        for (int i = 0; i < rgbArr.length; i++) {
            if (rgbArr[i] != rgbArr2[i] & Math.abs(rgbArr[i] - rgbArr2[i]) > MAX_DIFF) {
                points.add(new PicturePoint(x, y));
            }
            x++;
            if( x%width == 0) {
                x = 0;
                y++;
            }
        }

        return points;
    }

    public static List getSetsOfPoints(List<PicturePoint> points) {
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

    public static PicturePoint[] getExtremePoints(List pointsList){
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

        PicturePoint[] extremePoints = {new PicturePoint(xMax, yMax), new PicturePoint(xMin, yMin)};

        return extremePoints;
    }

    public static void printDiffRectangles(BufferedImage bufferedImage, List<List> sets) {
        List<PicturePoint[]> rectangles = new ArrayList<>();
        for (List<List> set: sets) {
            rectangles.add(getExtremePoints(set));
        }

        int rgb = Color.RED.getRGB();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int dxm = 2;
        int dxp = 2;
        int dxp2 = 3;
        int dym = 2;
        int dyp = 2;


        for (PicturePoint[] rectangle: rectangles) {
            if (width - rectangle[0].x -1 < 2) {
                dxp = 0;
            }
            if (width - rectangle[0].x - 1 < 3) {
                dxp2 = 0;
            }
            if(rectangle[1].x < 2) {
                dxm = 0;
            }
            if (height - rectangle[0].y - 1 < 2) {
                dyp = 0;
            }
            if(rectangle[1].y -1 < 2) {
                dym = 0;
            }

            for(int i = rectangle[1].x - dxm ; i < rectangle[0].x + dxp2; i++){
                bufferedImage.setRGB(i, rectangle[1].y - dym, rgb);
                bufferedImage.setRGB(i, rectangle[0].y + dyp, rgb);
            }
            for(int j = rectangle[1].y - dym ; j < rectangle[0].y + dyp; j++){
                bufferedImage.setRGB(rectangle[1].x - dxm, j, rgb);
                bufferedImage.setRGB(rectangle[0].x + dxp, j, rgb);
            }

            dxm = dxp = dym = dyp = 2;
            dxp2 = 3;
        }

        try {
            ImageIO.write(bufferedImage, "png", new File("res/outputfile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
