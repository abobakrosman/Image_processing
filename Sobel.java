/*
 * 
 * 
 * 
 */
package sobel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.WritableRaster;

/**
 *
 * @author abobakr osman
 */
public class Sobel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("\nSobel_Operator\n");
        String input_image_path
                = "E:\\abobakr\\Pictures\\images\\Edge_Detection\\test01.png";
        BufferedImage image = read_image(input_image_path);

        display(input_image_path, "Original", 100, 100);

        System.out.println("Call Sobel Operator  Function ");

        sobel_operator(image);
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

    public static void sobel_operator(BufferedImage image) {

        BufferedImage nImg = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        int width = image.getWidth();
        int height = image.getHeight();

        WritableRaster wr = image.getRaster();
        WritableRaster er = nImg.getRaster();

        int[][] pixels_clr = new int[height][width];

        int clr = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                clr = wr.getSample(j, i, 0);
                pixels_clr[i][j] = (clr >= 128) ? 255 : 0;

            }
        }
        System.out.println("------------");

        //This laplace operator but simply change to other operator ex.Sobel 
        int[][] mask = {
            {0, 1, 0},
            {1, -4, 1},
            {0, 1, 0}
        };

        int[][] result = new int[pixels_clr.length][pixels_clr[0].length];

        int padding[][] = new int[(pixels_clr.length) + 2][(pixels_clr[0].length) + 2];

        padding[0][0] = pixels_clr[0][0];
        padding[0][padding[0].length - 1] = pixels_clr[0][pixels_clr[0].length - 1];

        padding[padding.length - 1][0] = pixels_clr[pixels_clr.length - 1][0];
        padding[padding.length - 1][padding[0].length - 1] = pixels_clr[pixels_clr.length - 1][pixels_clr[0].length - 1];

        for (int x = 0; x < pixels_clr[0].length; x++) {
            padding[0][x + 1] = pixels_clr[0][x];
        }

        for (int x = 0; x < pixels_clr[0].length; x++) {
            padding[padding.length - 1][x + 1] = pixels_clr[pixels_clr.length - 1][x];
        }

        for (int y = 0; y < pixels_clr.length; y++) {
            padding[y + 1][0] = pixels_clr[y][0];
        }

        for (int y = 0; y < pixels_clr.length; y++) {
            padding[y + 1][padding[0].length - 1] = pixels_clr[y][pixels_clr[0].length - 1];
        }

        for (int y = 0; y < pixels_clr.length; y++) {
            for (int x = 0; x < pixels_clr[0].length; x++) {
                padding[y + 1][x + 1] = pixels_clr[y][x];
            }
        }

        int sum = 0;
        for (int y = 1; y <= pixels_clr.length; y++) {
            for (int x = 1; x <= pixels_clr[0].length; x++) {
                sum = (mask[0][0] * padding[y - 1][x - 1]) + (padding[y - 1][x] * mask[0][1]) + (padding[y - 1][x + 1] * mask[0][2]);
                sum = sum + (mask[1][0] * padding[y][x - 1]) + (padding[y][x] * mask[1][1]) + (padding[y][x + 1] * mask[1][2]);
                sum = sum + (mask[2][0] * padding[y + 1][x - 1]) + (padding[y + 1][x] * mask[2][1]) + (padding[y + 1][x + 1] * mask[2][2]);
                result[y - 1][x - 1] = sum;
                sum = 0;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int nVal = result[y][x];

                er.setSample(x, y, 0, nVal);
            }
        }

        nImg.setData(er);
        String image_path
                = "E:\\abobakr\\Pictures\\images\\Edge_Detection\\result.png";
        write_image(image_path, nImg);
        System.out.println("Done");
        display(image_path, "Result", 400, 100);
    }

}
