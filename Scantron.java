import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scantron {
    public static void main(String[] args) throws IOException {
        File img = new File("images.jpg");
        BufferedImage bufferedImage = ImageIO.read(img);
        bufferedImage.getHeight();
    }
}
