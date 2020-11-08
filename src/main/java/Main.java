import java.io.File;

public class Main {

  public static void main(String[] args) {
    String srcFolder = "/users/sortedmap/Desktop/src";
    String dstFolder = "/users/sortedmap/Desktop/dst";

    File srcDir = new File(srcFolder);

    File[] files = srcDir.listFiles();

    int countProcessor = Runtime.getRuntime().availableProcessors();
    int step = files.length / countProcessor;
    for (int i = 0; i < countProcessor; i++) {
      int start = step * i;
      File[] filesForThread = new File[step];
      System.arraycopy(files, start, filesForThread, 0, filesForThread.length);
      Thread thread = ImageResize.createAndStart(new ImageResize(filesForThread, 300, dstFolder));
    }
  }
}
