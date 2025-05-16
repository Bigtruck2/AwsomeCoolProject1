import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scantron {
    static final int BLACK = -16777216;
    static final int RED = (0) | (255 << 8);
    static final int GREEN = (0) | (255 << 16);

    static boolean[][] visited;
    public static void main(String[] args) throws IOException {
        File img = new File("exam1.jpg");
        BufferedImage bufferedImage = ImageIO.read(img);
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                int rgb = bufferedImage.getRGB(j,i);
                int r =(rgb>>16) & 0xFF;
                int g =(rgb>>8) & 0xFF;
                int b =(rgb) & 0xFF;
                int grey = (Math.max(Math.max(r,b),g)+Math.min(Math.min(r,b),g))/2;
                grey = (grey>200)?255:0;
                int newPixel = (grey) | ((grey << 16)) | ((grey << 8));
                bufferedImage.setRGB(j, i, newPixel);
            }
        }
       // visited = new boolean[bufferedImage.getWidth()][bufferedImage.getHeight()];
        System.out.println(BLACK);
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                if (bufferedImage.getRGB(j, i) == BLACK) {
                    System.out.println(bufferedImage.getRGB(j, i));
                    ArrayList<Point> contour = traceContour(bufferedImage, new Point(j,i));
                    if (contour.size()>13){
                        for (Point p:contour){
                            bufferedImage.setRGB(p.getX(),p.getY(),GREEN);
                        }
                    }
                }
            }
        }
        ImageIO.write(bufferedImage, "jpg",img);
    }
    public static ArrayList<Point> traceContour(BufferedImage bufferedImage,  Point p){
        ArrayList<Point> perimeter = new ArrayList<Point>();
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
                    bufferedImage.setRGB(nx,ny,RED);
                    break;
                }
            }
            if (prevP.equals(p)) break;;
        } while (!p.equals(c));
        return perimeter;
    }
}
