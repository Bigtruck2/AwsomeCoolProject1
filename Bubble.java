import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bubble extends Shape{
    static final int RED = (0) | (255 << 16);
    public Bubble(int minx, int miny, int maxx, int maxy, ArrayList<Point> contour) {
        super(minx, miny, maxx, maxy, contour);
    }
    public Bubble(Shape shape){
        super(shape.minx, shape.miny, shape.maxx, shape.maxy, shape.contour);
    }
    public void color(BufferedImage bufferedImage){
        for (Point p : contour) {

            bufferedImage.setRGB(p.getX(), p.getY(), RED);
        }
    }
}
