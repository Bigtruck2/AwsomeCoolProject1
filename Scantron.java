import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class Scantron {
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
                grey = (grey<100)?0:grey;
                grey = (grey>200)?355:grey;
                int alpha = (rgb >> 24) & 0xFF;
                int newPixel = (alpha << 24) | (grey << 16) | (grey << 8) | grey;

                bufferedImage.setRGB(j, i, newPixel);
            }
        }
        ImageIO.write(bufferedImage, "jpg",img);
    }
}
