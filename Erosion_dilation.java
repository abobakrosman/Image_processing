package erosion_dilation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.WritableRaster;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author abobakr Osman
 */
public class Erosion_dilation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String input_image_path
                = "E:\\abobakr\\Pictures\\images\\erosion_dilation\\testing.png";

        BufferedImage image = read_image(input_image_path);
        display(input_image_path, "Original", 100, 100);

        System.out.println("Call erosion Function ");

        BufferedImage result1 = erosion(image);
        input_image_path
                = "E:\\abobakr\\Pictures\\images\\erosion_dilation\\erosion\\result.png";
        display(input_image_path, "Erosion", 300, 100);
        System.out.println();

        System.out.println("Call dilation Function ");

        BufferedImage result2 = dilation(image);
        input_image_path
                = "E:\\abobakr\\Pictures\\images\\erosion_dilation\\dilation\\result.png";
        display(input_image_path, "Dilation", 500, 100);
        System.out.println();

        System.out.println("Call Opening Function ");
        opening(image);

        System.out.println("Call Closing Function ");
        closing(image);

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
        public static BufferedImage resize(String path, String output_image, int height, int width) {

        BufferedImage resized = null;
        try {
            File input = new File(path);
            BufferedImage img = ImageIO.read(input);
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

    public static void display(BufferedImage image, String path) {

        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(path);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public static BufferedImage erosion(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage nImg = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        int[][] dilation = new int[height][width];
        WritableRaster wr = image.getRaster();
        WritableRaster er = nImg.getRaster();

        int clr, red, green, blue;
        clr = red = blue = green = 0;
        int index0 = 0;
        for (int y = 0; y < dilation.length; y++) {
            for (int x = 0; x < dilation[0].length; x++) {

                clr = image.getRGB(x, y);

                red = (clr & 0x00ff0000) >> 16;
                green = (clr & 0x0000ff00) >> 8;
                blue = clr & 0x000000ff;

                red = (red < 127) ? 0 : 255;
                green = (green < 127) ? 0 : 255;
                blue = (blue < 127) ? 0 : 255;

                clr = red + green + blue;

                dilation[y][x] = (clr == 0) ? 0 : 255;

            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dilation[y][x] == 0) {
                    dilation[y][x] = 1;
                } else {
                    dilation[y][x] = 0;
                }
            }
        }
        int[][] result = new int[height][width];
        int[][] mask = {
            {0, 1, 0},
            {1, 1, 1},
            {0, 1, 0}
        };
        int sum=5;//sum of 1's
        int flag = 0;

        for (int y = 0; y < dilation.length; y++) { //test.length
            for (int x = 0; x < dilation[0].length; x++) { //test[0].length

                if (dilation[y][x] == mask[1][1] & dilation[y][x]==1) {
                    flag++;
                }

                if (y - 1 >= 0) {
                    if (dilation[y - 1][x] == mask[0][1]& dilation[y-1][x]==1) {
                        flag++;
                    }
                }

                if (x - 1 >= 0) {
                    if (dilation[y][x - 1] == mask[1][0]& dilation[y][x-1]==1) {
                        flag++;
                    }
                }

                if (x + 1 < dilation[0].length) {
                    if (dilation[y][x + 1] == mask[1][2]& dilation[y][x+1]==1) {
                        flag++;
                    }
                }

                if (y + 1 < dilation.length) {
                    if (dilation[y + 1][x] == mask[2][1]& dilation[y+1][x]==1) {
                        flag++;
                    }
                }

                if (flag >= sum) {
                    result[y][x] = 1;

                }

                flag = 0;

            }
            flag=0;
        }
        System.out.println("-----");

        for (int y = 0; y < dilation.length; y++) {
            for (int x = 0; x < dilation[0].length; x++) {
                int nVal = result[y][x];
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
                = "E:\\abobakr\\Pictures\\images\\erosion_dilation\\erosion\\result.png";
        write_image(image_path, nImg);
        System.out.println("Done");

        return nImg;
    }

    public static BufferedImage dilation(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage nImg = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        int[][] dilation = new int[height][width];
        WritableRaster wr = image.getRaster();
        WritableRaster er = nImg.getRaster();

        int clr, red, green, blue;
        clr = red = blue = green = 0;
        int index0 = 0;
        for (int y = 0; y < dilation.length; y++) {
            for (int x = 0; x < dilation[0].length; x++) {

                clr = image.getRGB(x, y);

                red = (clr & 0x00ff0000) >> 16;
                green = (clr & 0x0000ff00) >> 8;
                blue = clr & 0x000000ff;

                red = (red < 127) ? 0 : 255;
                green = (green < 127) ? 0 : 255;
                blue = (blue < 127) ? 0 : 255;

                clr = red + green + blue;

                dilation[y][x] = (clr == 0) ? 0 : 255;

            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dilation[y][x] == 0) {
                    dilation[y][x] = 1;
                } else {
                    dilation[y][x] = 0;
                }
            }
        }
        int[][] result = new int[height][width];
        int[][] mask = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        int flag = 0;
        int sum =1;
        for (int y = 0; y < dilation.length; y++) { //test.length
            for (int x = 0; x < dilation[0].length; x++) { //test[0].length

                if (dilation[y][x] == mask[1][1]) {
                    flag++;
                }

                if (y - 1 >= 0) {
                    if (dilation[y - 1][x] == mask[0][1]& dilation[y-1][x]==1) {
                        flag++;
                    }
                }

                if (x - 1 >= 0) {
                    if (dilation[y][x - 1] == mask[1][0]& dilation[y][x-1]==1) {
                        flag++;
                    }
                }

                if (x + 1 < dilation[0].length) {
                    if (dilation[y][x + 1] == mask[1][2]& dilation[y][x+1]==1) {
                        flag++;
                    }
                }

                if (y + 1 < dilation.length) {
                    if (dilation[y + 1][x] == mask[2][1]& dilation[y+1][x]==1) {
                        flag++;
                    }
                }

                if (flag > 0) {
                    result[y][x] = 1;

                }

                flag = 0;

            }
            flag=0;
        }
        System.out.println("-----");

        for (int y = 0; y < dilation.length; y++) {
            for (int x = 0; x < dilation[0].length; x++) {
                int nVal = result[y][x];
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
                = "E:\\abobakr\\Pictures\\images\\erosion_dilation\\dilation\\result.png";

        write_image(image_path, nImg);
        System.out.println("Done");

        return nImg;
    }

    public static void opening(BufferedImage image) {
        BufferedImage result1 = erosion(image);
        BufferedImage result2 = dilation(result1);
        System.out.println("Done");
        String image_path
                = "E:\\abobakr\\Pictures\\images\\erosion_dilation\\opening.png";
        write_image(image_path, result2);

        display(image_path, "Opening", 100, 200);
        System.out.println();
    }

    public static void closing(BufferedImage image) {

        BufferedImage result1 = dilation(image);
        BufferedImage result2 = erosion(result1);
        System.out.println("Done");
        String image_path
                = "E:\\abobakr\\Pictures\\images\\erosion_dilation\\closing.png";
        write_image(image_path, result2);

        display(image_path, "Closing", 300, 200);
        System.out.println();
    }

}
