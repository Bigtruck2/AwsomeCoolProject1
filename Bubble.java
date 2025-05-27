import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class Bubble extends Point{
    public int minx, miny, maxx, maxy;
    public ArrayList<Point> contour;

    public Bubble(int minx, int miny, int maxx, int maxy, ArrayList<Point> contour) {
        super((minx+maxx)/2, (miny+maxy)/2);
        this.minx =minx;
        this.miny = miny;
        this.maxx = maxx;
        this.maxy = maxy;
        this.contour = contour;
    }
    public boolean isPointInEllipse(double x, double y, double rx, double ry) {
        return (x * x) / (rx * rx) + (y * y) / (ry * ry) <= 1.0;
    }
    public int getArea(BufferedImage bufferedImage){
        int area = 0;

        for (int k = minx; k < maxx; k++) {
            for (int l = miny; l < maxy; l++) {
                double rx = super.getX();
                double ry = super.getY();
                if(bufferedImage.getRGB(k,l)==-16777216&&isPointInEllipse(k - getX(), l - getY(), rx, ry))area++;
            }
        }
        return area;
    }
    public double getCircularity(int area){
        return 4 * Math.PI * area / (contour.size() * contour.size());
    }
}
