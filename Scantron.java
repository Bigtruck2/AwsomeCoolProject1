
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Scantron {
    static final int BLACK = -16777216;
    static final int GREEN = (0) | (255 << 8);
    static final int RED = (0) | (255 << 16);
    static final int BLUE = 255;

    public static void main(String[] args) throws IOException {
        File img = new File("Test Checker.v3i.retinanet/test/2_jpg.rf.8fe85db4575b2b6c4a4552d2f2cfc13b.jpg");
        BufferedImage bufferedImage = ImageIO.read(img);
        greyScale(bufferedImage);
        ArrayList<Bubble> bubbles = new ArrayList<>();
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = bufferedImage.getWidth()*1/3; j < bufferedImage.getWidth()*2/3; j++) {
                if (bufferedImage.getRGB(j, i) == BLACK) {
                    ArrayList<Point>contour = traceContour(bufferedImage, new Point(j,i));
                    ArrayList<Integer> xList = new ArrayList<>();
                    ArrayList<Integer> yList = new ArrayList<>();
                    if (contour.size()>18){
                        for (Point p:contour){
                            xList.add(p.getX());
                            yList.add(p.getY());
                            bufferedImage.setRGB(p.getX(), p.getY(), BLUE);
                        }
                        int maxx = xList.stream().max(Comparator.naturalOrder()).orElse(0);
                        int maxy = yList.stream().max(Comparator.naturalOrder()).orElse(0);
                        int minx = xList.stream().min(Comparator.naturalOrder()).orElse(0);
                        int miny = yList.stream().min(Comparator.naturalOrder()).orElse(0);
                        Bubble bubble = new Bubble(minx,miny,maxx,maxy,contour);
                        int area = bubble.getArea(bufferedImage);
                        System.out.println(area);
                        System.out.println(bubble.getCircularity(area));
                        if(bubble.getCircularity(area)>.3&&area>8) {
                            bubbles.add(bubble);
                        }
                    }
                }
            }
        }
        int centerBubbles = (int) ((bubblex.stream().max(Comparator.naturalOrder()).orElse(0.0)+bubblex.stream().min(Comparator.naturalOrder()).orElse(0.0))/2);
        ArrayList<Double> ab = new ArrayList<>();
        ArrayList<Double> cd = new ArrayList<>();

        for (double x: bubblex){
            if(centerBubbles<x){
                ab.add(x);
            }else {
                cd.add(x);
            }
        }
        int c = (int) ((ab.stream().max(Comparator.naturalOrder()).orElse(0.0)+ab.stream().min(Comparator.naturalOrder()).orElse(0.0))/2);
        int a = (int) ((cd.stream().max(Comparator.naturalOrder()).orElse(0.0)+cd.stream().min(Comparator.naturalOrder()).orElse(0.0))/2);
        int[] answers = new int[bubblex.size()];
        for (int i = 0;i<bubblex.size();i++){
            if(a>bubblex.get(i)){
                answers[i]=0;
            }else if(bubblex.get(i)>a&&bubblex.get(i)<centerBubbles) {
                answers[i]=1;
            }else if(bubblex.get(i)<c) {
                answers[i]=2;
            }else {
                answers[i]=3;
            }
        }
        System.out.println(Arrays.toString(answers));
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            bufferedImage.setRGB(a,i,GREEN);
            bufferedImage.setRGB(c,i,GREEN);
            bufferedImage.setRGB(centerBubbles,i,GREEN);
        }
        ImageIO.write(bufferedImage, "jpg",img);
    }
    public static void greyScale(BufferedImage bufferedImage){
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
    }
    //inspired by https://github.com/biometrics/imagingbook/blob/master/src/contours/ContourTracer.java
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
