//package binary_reconstruction;

import java.awt.Graphics2D;
import java.awt.Image;
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
 * @author abobakr osman Mohammed
 */
public class Binary_Reconstruction {

    /**
     * @param args the command line arguments
     */
    static int board[][];//Result
    static int height;
    static int width;
    static int[] horizontal;
    static int[] vertical;

    public static void main(String[] args) {

        String path
                = "E:\\abobakr\\Pictures\\images\\Binary_reconstructio\\";
        resize(path + "f.png", path + "input.png", 128, 128);
        display(path + "input.png", "Input", 300, 200);

        BufferedImage nImg = binary_reconstruct(read_image(path + "f.png"));
        solveNQ(0, 0);

        if (sum(horizontal) == 0 & sum(vertical) == 0) {
            System.out.println();
            printSolution(board, nImg);
        } else {
            System.out.print("No Solution Found");
        }

        resize(path + "result.png", path + "result2.png", 128, 128);
        String path2 = path + "result2.png";
        display(path2, "Result", 100, 200);

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

    public static BufferedImage binary_reconstruct(BufferedImage image) {

        BufferedImage nImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        width = image.getWidth();
        height = image.getHeight();

        horizontal = new int[width];
        vertical = new int[height];

        board = new int[width][height];

        WritableRaster wr = image.getRaster();

        int clr = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //clr = imageb[j][i];
                clr = wr.getSample(j, i, 0);
                vertical[i] += (clr < 128) ? 1 : 0;
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //clr = imageb[i][j];
                clr = wr.getSample(i, j, 0);
                horizontal[i] += (clr < 128) ? 1 : 0;
            }
        }

        bubbleSort(horizontal);
        bubbleSort(vertical);

        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < vertical.length; i++) {
            System.out.print(vertical[i] + " ");
        }

        System.out.println();
        for (int i = 0; i < horizontal.length; i++) {
            System.out.println(" " + horizontal[i] + " ");
        }

        System.out.println();

        return nImg;

    }

    public static void printSolution(int board[][], BufferedImage nImg) {
        WritableRaster er = nImg.getRaster();
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(" " + board[i][j]
                        + " ");
            }
            System.out.println();
        }

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                int nVal = board[y][x];
                if (nVal == 0) {
                    nVal = 255;
                }
                if (nVal == 1) {
                    nVal = 0;
                }
                er.setSample(x, y, 0, nVal);
            }
        }

        nImg.setData(er);

        String image_path
                = "E:\\abobakr\\Pictures\\images\\Binary_reconstructio\\";

        write_image(image_path + "result.png", nImg);
        System.out.println("Done");

    }

    public static boolean isSafe(int i, int j) {
        System.out.println("Ver = " + vertical[j]);
        System.out.println(i);
        System.out.println("Hori = " + horizontal[i]);

        return (horizontal[i] > 0 & vertical[j] > 0) ? true : false;
    }

    public static boolean solveNQ(int i, int j) {
        System.out.println("I = " + i + "   J = " + j);
        if(i == 13 || j==13)
        return false;

        if (i<width && isSafe(i, j)) {
            board[i][j] = 1;
            horizontal[i] = horizontal[i] - 1;
            vertical[j] = vertical[j] - 1;
            if ((j + 1) < width) {
                solveNQ(i, ++j);
            } else {
                j = 0;
                solveNQ(++i, j);

            }

        } else if (horizontal[i] != 0) {
            if ((j + 1) < width) {
                solveNQ(i, ++j);
            } else {
                j = 0;
                solveNQ(++i, j);

            }

        } else {
            if ((i + 1) < width) {
                j = 0;
                solveNQ(++i, j);
            }
        }

        return false;
    }

    public static int sum(int[] horizontal) {
        int sum = 0;
        for (int i = 0; i < horizontal.length; i++) {
            sum += horizontal[i];
        }

        return sum;
    }

    public static void bubbleSort(int arr[]) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    // swap arr[j+1] and arr[i]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static BufferedImage resize(String path, String output_image, int height, int width) {

        BufferedImage resized = null;
        try {
            File input = new File(path);
            BufferedImage img = ImageIO.read(input);

            //BufferedImage resized = resize(image3, 128, 128);
            //File output = new File(path + "abobakr.png");
            //ImageIO.write(resized, "png", output);
            Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            File output = new File(output_image);
            ImageIO.write(resized, "png", output);

        } catch (IOException e) {
            System.out.println("Exception " + e);
        }
        return resized;

    }

}
