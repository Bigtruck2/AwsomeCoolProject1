import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
//1 3 2 1 0 2 1 2 0 3 1 3 2 1 2 0 3 1 2 3
public class Scantron {
    static final int BLACK = -16777216;
    static final int GREEN = (0) | (255 << 8);
    static final int BLUE = 255;
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> corrects = new ArrayList<>();

        for (String s:scanner.nextLine().trim().split(" ")){
            corrects.add(Integer.parseInt(s));
        }
        ArrayList<Double> scores = new ArrayList<>();
        Path folder = Paths.get("Test Checker.v3i.retinanet/train");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            for (Path entry : stream) {
                System.out.println("Test Checker.v3i.retinanet/train/"+entry.getFileName());
                scores.add(getScore("Test Checker.v3i.retinanet/train/"+entry.getFileName(),corrects));
            }
        }
        ScoreDistribution classA = new ScoreDistribution(scores);
        System.out.println(classA.mean());
        System.out.println(scores);
        //System.out.println(getScore("Test Checker.v3i.retinanet/test/41_jpg.rf.7f5f4c0332061694490d6418157deee1.jpg",corrects));
    }

    public static double getScore(String fileName,ArrayList<Integer> corrects) throws IOException {
        File img = new File(fileName);
        BufferedImage bufferedImage = ImageIO.read(img);
        greyScale(bufferedImage);
        ArrayList<Shape> shapes = new ArrayList<>();
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < (bufferedImage.getWidth()) / 3; j++) {
                if (bufferedImage.getRGB(j, i) == BLACK) {
                    ArrayList<Point> contour = traceContour(bufferedImage, new Point(j, i));
                    if (contour.size() > 25) {
                        ArrayList<Integer> x = new ArrayList<>();
                        ArrayList<Integer> y = new ArrayList<>();

                        for (Point p : contour) {
                            x.add(p.getX());
                            y.add(p.getY());
                            bufferedImage.setRGB(p.getX(), p.getY(), BLUE);
                        }
                        int maxx = x.stream().max(Comparator.naturalOrder()).orElse(0);
                        int maxy = y.stream().max(Comparator.naturalOrder()).orElse(0);
                        int minx = x.stream().min(Comparator.naturalOrder()).orElse(0);
                        int miny = y.stream().min(Comparator.naturalOrder()).orElse(0);
                        Shape shape = new Shape(minx, miny, maxx, maxy, contour);

                        int area = shape.getArea(bufferedImage);
                        if (shape.isCircle(area)) {
                            Bubble bubble = new Bubble(shape);
                            shapes.add(bubble);
                            if (shapes.size() < 2 || !shape.isInside(shapes.get(shapes.size() - 2))) {
                                bubble.color(bufferedImage);
                            } else {
                                shapes.remove(shapes.size() - 1);
                            }
                        }
                    }
                }
            }
        }
        shapes = shapes.stream().sorted(Comparator.comparingInt(Point::getX)).collect(Collectors.toCollection(ArrayList::new));
        int centerBubbles = (shapes.get(0).getX()+ shapes.get(shapes.size()-1).getX()) /2;
        shapes = shapes.stream().sorted(Comparator.comparingInt(Point::getY)).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> ab = new ArrayList<>();
        ArrayList<Integer> cd = new ArrayList<>();

        for (Shape x: shapes){
            if(centerBubbles<x.getX()){
                ab.add(x.getX());
            }else {
                cd.add(x.getX());
            }
        }
        int c = ((ab.stream().max(Comparator.naturalOrder()).orElse(0)+ab.stream().min(Comparator.naturalOrder()).orElse(0))/2);
        int a = ((cd.stream().max(Comparator.naturalOrder()).orElse(0)+cd.stream().min(Comparator.naturalOrder()).orElse(0))/2);
        int[] answers = new int[shapes.size()];

        for (int i = 0; i< shapes.size(); i++){
            if(a> shapes.get(i).getX()){
                answers[i]=0;
            }else if(shapes.get(i).getX()>a&& shapes.get(i).getX()<centerBubbles) {
                answers[i]=1;
            }else if(shapes.get(i).getX()<c) {
                answers[i]=2;
            }else {
                answers[i]=3;
            }
        }
        System.out.println(Arrays.toString(answers));
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            bufferedImage.setRGB(a,i,GREEN);
            bufferedImage.setRGB(centerBubbles,i,GREEN);
            bufferedImage.setRGB(c,i,GREEN);

        }
        ImageIO.write(bufferedImage, "jpg",img);
        int rights = 0;
        for (int i = 0; i < corrects.size(); i++){
            if (corrects.get(i) < 0 || (corrects.get(i)) > 3){
                throw new IllegalStateException("Number Not Allowed");
            }
            else{
                if (corrects.get(i).equals(answers[i])){
                    rights++;
                }
            }
        }
        System.out.println(100.0*rights / answers.length);
        return  100.0*rights / answers.length;
        //return answers;
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
