import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Scantron {
    static final int BLACK = -16777216;
    static final int GREEN = (0) | (255 << 8);
    static final int RED = (0) | (255 << 16);

    static boolean[][] visited;
    public static void main(String[] args) throws IOException {
        File img = new File("images.jpg");
        BufferedImage bufferedImage = ImageIO.read(img);
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                int rgb = bufferedImage.getRGB(j,i);
                int r =(rgb>>16) & 0xFF;
                int g =(rgb>>8) & 0xFF;
                int b =(rgb) & 0xFF;
                int grey = (Math.max(Math.max(r,b),g)+Math.min(Math.min(r,b),g))/2;
                grey = (grey>150)?255:0;
                int newPixel = (grey) | ((grey << 16)) | ((grey << 8));
                bufferedImage.setRGB(j, i, newPixel);
            }
        }
       // visited = new boolean[bufferedImage.getWidth()][bufferedImage.getHeight()];
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                if (bufferedImage.getRGB(j, i) == BLACK) {
                    ArrayList<Point>contour = traceContour(bufferedImage, new Point(j,i));
                    //double circularity = 4 * Math.PI * contour.meat.size() / (contour.perimeter.size() * contour.perimeter.size());
                    if (contour.size()>14){
                        ArrayList<Integer> x = new ArrayList<>();
                        ArrayList<Integer> y = new ArrayList<>();

                        for (Point p:contour){
                            x.add((int) p.getX())
                            bufferedImage.setRGB(p.getX(),p.getY(),RED);
                        }
                        int maxx = Util.max(x);
                        int maxy = Util.max(y);

                        for (int k = Util.min(x); k < maxx; k++) {
                            for (int l = Util.min(y); l < maxy; l++) {

                            }
                        }
                    }
                }
            }
        }
        ImageIO.write(bufferedImage, "jpg",img);
    }
    public record Pair<U, V>(U perimeter, V meat) {}
    public static ArrayList<Point> traceContour(BufferedImage bufferedImage,  Point p){
        ArrayList<Point> perimeter = new ArrayList<>();
        perimeter.add(p);
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};
        int pDirection = 7;
        Point c = p;
        Point prevP = p;
        do {
            for (int i = 0; i < 8; i++) {
                prevP = p;
                int clockwiseDir = (pDirection+1+i)%8;
                int nx = p.getX() + dx[clockwiseDir];
                int ny = p.getY() + dy[clockwiseDir];
                if (nx >= 0 && nx < bufferedImage.getWidth() && ny >= 0 && ny < bufferedImage.getHeight() && bufferedImage.getRGB(nx,ny) == BLACK) {
                    p = new Point(nx, ny);
                    perimeter.add(p);
                    pDirection = (clockwiseDir + 6) % 8;
                    bufferedImage.setRGB(nx,ny,GREEN);
                    break;
                }
            }
            if (prevP.equals(p)) break;;
        } while (!p.equals(c));
        return perimeter;
    }
}
