package comparer;

public class PicturePoint {
    int x;
    int y;

    PicturePoint(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public double getDistance(PicturePoint point) {
        return Math.sqrt(Math.pow(this.x-point.x, 2) + Math.pow(this.y - point.y, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PicturePoint)) return false;

        PicturePoint point = (PicturePoint) o;

        if (x != point.x) return false;
        return y == point.y;
    }
}