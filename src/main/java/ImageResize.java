import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

public class ImageResize implements Runnable {

  private File[] files;
  private int newWidth;
  private String dstFolder;

  public ImageResize(File[] files, int newWidth, String dstFolder) {
    this.files = files;
    this.newWidth = newWidth;
    this.dstFolder = dstFolder;
  }

  public static Thread createAndStart(ImageResize image) {
    Thread thread = new Thread(image);
    thread.start();
    return thread;
  }

  @Override
  public void run() {
    try {
      long startTime = System.currentTimeMillis();
      for (File file : files) {
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
          continue;
        }
        BufferedImage newImage = Scalr
            .resize(image, Method.SPEED, (newWidth * 3), Scalr.THRESHOLD_BALANCED_SPEED);
        newImage = Scalr
            .resize(newImage, Method.ULTRA_QUALITY, newWidth, Scalr.THRESHOLD_QUALITY_BALANCED);

        File newFile = new File(dstFolder + "/" + file.getName());
        ImageIO.write(newImage, "jpg", newFile);
      }
      System.out.println("Время обработки потока - " + (System.currentTimeMillis() - startTime));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private BufferedImage resize(BufferedImage image, int newWidth) {
    int newHeight = (int) Math.round(
        image.getHeight() / (image.getWidth() / (double) newWidth)
    );
    BufferedImage newImage = new BufferedImage(
        newWidth, newHeight, BufferedImage.TYPE_INT_RGB
    );

    int widthStep = image.getWidth() / newWidth;
    int heightStep = image.getHeight() / newHeight;

    for (int x = 0; x < newWidth; x++) {
      for (int y = 0; y < newHeight; y++) {
        int rgb = image.getRGB(x * widthStep, y * heightStep);
        newImage.setRGB(x, y, rgb);
      }
    }

    return newImage;
  }
}
