import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class Bubble extends Point{
    private int minx, miny, maxx, maxy;
    private ArrayList<Point> contour;

    public Bubble(int minx, int miny, int maxx, int maxy, ArrayList<Point> contour) {
        super((minx+maxx)/2, (miny+maxy)/2);
        this.minx =minx;
        this.miny = miny;
        this.maxx = maxx;
        this.maxy = maxy;
        this.contour = contour;
    }
    public boolean isPointInEllipse(double x, double y) {
        return (x * x) / (super.getX() * super.getX()) + (y * y) / (super.getY() * super.getY()) <= 1.0;
    }
    public int getArea(BufferedImage bufferedImage){
        int area = 0;

        for (int k = minx; k < maxx; k++) {
            for (int l = miny; l < maxy; l++) {
                if((bufferedImage.getRGB(k,l)==-16777216||bufferedImage.getRGB(k,l)==-16776961)&&isPointInEllipse(k - getX(), l - getY()))area++;
            }
        }
        return area;
    }
    public double getCircularity(int area){
        return 4 * Math.PI * area / (contour.size() * contour.size());
    }

    @Override
    public String toString() {
        return "Bubble{" +
                "maxy=" + maxy +
                ", maxx=" + maxx +
                ", miny=" + miny +
                ", minx=" + minx +
                '}';
    }
}
