package histogram;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author Aboabkr Osman Mohammed
 */
public class Histogram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String input_image_path = "E:\\abobakr\\Pictures\\images\\histogram\\hawkes.jpg";
        BufferedImage image = read_image(input_image_path);
        display(input_image_path, "Input", 0, 100);
        System.out.println("Call Histogram Function ");
        histogram(image);

    }

    public static BufferedImage read_image(String image_path) {

        BufferedImage image = null;
        File f = null;
        try {

            f = new File(image_path); //image file path
            image = ImageIO.read(f);
            System.out.println("Reading image complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        return image;

    }

    public static void write_image(String image_path, BufferedImage image) {

        File f = null;
        try {

            f = new File(image_path); //image file path
            ImageIO.write(image, "png", f);
            System.out.println("Writing image complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

    }

    public static void display(String path, String title, int x, int y) {

        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(path);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setTitle(title);
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void histogram(BufferedImage image) throws IOException {

        BufferedImage nImg = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        int width = image.getWidth();
        int height = image.getHeight();
        float total = width * height;

        WritableRaster wr = image.getRaster();
        WritableRaster er = nImg.getRaster();

        int[] graylevels = new int[width * height];
        int[] G = new int[256];
        int H[] = new int[256];
        float T[] = new float[256];

        int index = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int grayLevelPixel = wr.getSample(i, j, 0);
                graylevels[index] = grayLevelPixel;
                index++;
                G[grayLevelPixel]++;

            }
        }

        H[0] = G[0];
        for (int i = 1; i <= 255; i++) {
            H[i] = G[i] + H[i - 1];
        }

        for (int i = 0; i < H.length; i++) {
            T[i] = (H[i]) / total;
        }
        int max = getMax(graylevels);
        for (int i = 0; i < H.length; i++) {
            T[i] = Math.round(T[i] * max);
        }

        for (int x = 0; x < wr.getWidth(); x++) {
            for (int y = 0; y < wr.getHeight(); y++) {
                int nVal = (int) T[wr.getSample(x, y, 0)];

                er.setSample(x, y, 0, nVal);
            }
        }

        nImg.setData(er);
        String image_path = "E:\\abobakr\\Pictures\\images\\histogram\\result.png";
        write_image(image_path, nImg);
        System.out.println("Done");
        display(image_path, "Output", 480, 100);

        
        //#---- For Ploating in Octave;
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter("H.txt"));
        for (int i = 0; i < H.length; i++) {
            // Maybe:
            outputWriter.write(H[i] + "");
            // Or:
            outputWriter.write(Integer.toString(H[i]));
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();

        outputWriter = new BufferedWriter(new FileWriter("T.txt"));
        for (int i = 0; i < T.length; i++) {
            // Maybe:
            outputWriter.write(T[i] + "");
            // Or:
            outputWriter.write(Float.toString(T[i]));
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
        //#---------- Octave

    }

    public static int getMax(int[] inputArray) {
        int maxValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }

}
