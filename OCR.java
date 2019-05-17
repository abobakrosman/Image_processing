package ocr;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Aboabkr Osman Gasmelsied Mohammed
 */
public class OCR {

    public static int line = 1;
    public static int letter = 1;
    public static int step = 1;
    public static int first = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String path
                = "E:\\abobakr\\Pictures\\images\\OCR";

        predict(432, 0);

        //int num = segmentaion_resize(path + "\\predict.PNG");
        //creat_dataset(26, 96);

        /*
        
        
        
         int first[][]={
         {1,2,3},
         {4,5,6},
         {7,8,9}
            
         };
        
         int second[][]={
         {9,8,7},
         {6,5,4},
         {3,2,1}
            
         };

         String path = "E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\final\\64_64";
         int[][] pixel;
         //for(int i=0;i<segmentaion_resize();i++)
         pixel = _2d(read_image(path + "\\" + 1 + ".png"));
         System.out.println(pixel[0][0]);
         */
//System.out.println("\n\n HERE"+segmentaion_resize());
    }

    public static int[][] _2d(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster wr = image.getRaster();

        int[][] pixels_clr = new int[height][width];

        int clr = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                clr = wr.getSample(j, i, 0);
                pixels_clr[i][j] = (clr >= 128) ? 255 : 0;

            }
        }
        return pixels_clr;

    }

    public static int segmentaion_resize(String image_path) {

        BufferedImage image = read_image(image_path);

        Segment1(image);
        step = 2;

        for (int z = 1; z < line; z++) {
            Segment2(read_image("E:\\abobakr\\Pictures\\images\\OCR\\processing\\" + z + ".PNG"));
        }

        step = 3;
        line = 1;
        for (int z = 1; z < letter; z++) {
            Segment1(read_image("E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\" + z + ".PNG"));
        }

        String input_path = "E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\final\\";
        String output_path = "E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\final\\64_64\\";
        int z = 1;
        for (z = 1; z < line; z++) {
            resize(input_path + z + ".PNG", output_path + z + ".PNG", 64, 64);
        }

        return z;
    }

    public static BufferedImage read_image(String image_path) {

        BufferedImage image = null;
        File f = null;
        try {

            f = new File(image_path); //image file path
            image = ImageIO.read(f);
            System.out.println("Reading complete.");
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
            System.out.println("Writing complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

    }

    public static void Segment1(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        int clr = 0;
        int y, i, j;
        i = j = 0;
        y = -1;
        int height2 = 0;

        int red, green, blue, color;
        red = green = blue = color = 0;
        a:
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                clr = image.getRGB(j, i);

                red = (clr & 0x00ff0000) >> 16;
                if (red >= 128) {
                    red = 255;
                } else {
                    red = 0;
                }

                green = (clr & 0x0000ff00) >> 8;
                if (green >= 128) {
                    green = 255;
                } else {
                    green = 0;
                }

                blue = clr & 0x000000ff;
                if (blue >= 128) {
                    blue = 255;
                } else {
                    blue = 0;
                }

                color = red + green + blue;

                if ((red == 0 | green == 0 | blue == 0)) {
                    if (y == -1) {
                        y = i;
                    }

                    if (i + 1 == height) {
                        System.out.println("Y=" + y + "    i= " + i);
                        height2 = (i + 1) - y;
                        cropImage(image, 0, y, width, height2);
                        line++;
                        y = -1;
                    }

                    continue a;
                }

            }
            if (y != -1 || y + 1 == height) {
                height2 = i - y;
                cropImage(image, 0, y, width, height2);
                System.out.println("check i = " + i + "    y=  " + y);
                line++;
                y = -1;
            }
        }

    }

    public static void Segment2(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        int clr = 0;
        int x, y, xx, yy, i, j;
        xx = yy = i = j = 0;
        y = -1;
        x = -1;
        int height2 = 0;
        int widtht2 = 0;
        int red, green, blue, color;
        red = green = blue = color = 0;
        a:
        for (i = 0; i < width; i++) {
            for (j = 0; j < height; j++) {
                clr = image.getRGB(i, j);
                red = (clr & 0x00ff0000) >> 16;
                if (red >= 128) {
                    red = 255;
                } else {
                    red = 0;
                }
                green = (clr & 0x0000ff00) >> 8;
                if (green >= 128) {
                    green = 255;
                } else {
                    green = 0;
                }
                blue = clr & 0x000000ff;
                if (blue >= 128) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                color = red + green + blue;

                if ((red == 0 | green == 0 | blue == 0)) {
                    if (x == -1) {
                        x = i;
                    }
                    continue a;
                }

            }
            if (x != -1) {
                widtht2 = Math.abs(i - x);

                cropImage(image, x, 0, widtht2, height);
                System.out.println("check this   " + i);
                letter++;
                x = -1;
            }
        }

    }

    public static void cropImage(BufferedImage src, int x, int y, int width, int height) {
        BufferedImage dest = src.getSubimage(x, y, width, height);

        if (step == 2) {
            write_image("E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\" + letter + ".PNG", dest);
        } else if (step == 1) {
            write_image("E:\\abobakr\\Pictures\\images\\OCR\\processing\\" + line + ".PNG", dest);
        } else {
            write_image("E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\final\\" + line + ".PNG", dest);
        }

    }

    public static void display(String path) {

        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(path);
        JLabel label = new JLabel(icon);
        frame.add(label);
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

    public static int[][] multiplication(int[][] first, int second[][]) {
        int multiply[][] = new int[64][64];
        int sum = 0;
        int c;
        int d;
        for (c = 0; c < 64; c++) {
            for (d = 0; d < 64; d++) {
                for (int k = 0; k < 64; k++) {
                    sum = sum + first[c][k] * second[k][d];
                }

                multiply[c][d] = sum;
                sum = 0;
            }
        }

        /*System.out.println("Product of the matrices:");

         for (c = 0; c < 64; c++) {
         for (d = 0; d < 64; d++) {
         System.out.print(multiply[c][d] + "\t");
         }

         System.out.print("\n");
         }*/
        return multiply;
    }

    public static void creat_dataset(int num, int type) throws Exception {
        OCR_dataset walsh_function = new OCR_dataset();

        int walsh[][][] = walsh_function.walsh("walsh.json");
        //System.out.println("checking\n");
        //walsh_function.print_walsh(walsh);
// = "E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\final\\64_64";

        String path;
        int[][] pixel;
        int a[][];
        int dataset = 1;
        char chr = '1';

        System.out.println("NO. = " + num + "\n\n");
        path = "E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\final\\64_64";
        while (dataset <= num) {
            System.out.println("Character No.  " + dataset + "\n\n");
            pixel = _2d(read_image(path + "\\" + dataset + ".png"));
            for (int i = 0; i < 64; i++) {
                a = multiplication(pixel, walsh[i]);
                //System.out.println((i + 1) + " Multipication");
                chr = (char) (dataset + type);
                walsh_function.write_json(a, "DataSet\\" + chr + ".json", i);
            }
            dataset++;

        }
        System.out.println("\n\n-Sonr-\n\n");

    }

    public static void predict(int num, int type) throws Exception {
        OCR_dataset walsh_function = new OCR_dataset();
        int walsh[][][] = walsh_function.walsh("walsh.json");

        String path
                = "E:\\abobakr\\Pictures\\images\\OCR\\processing\\Letters\\final\\64_64";

        int[][] pixel;
        int dataset = 1;
        int[][][] char_walsh = new int[64][64][64];
        while (dataset <= num) {
            System.out.println("Character No.  " + dataset + "\n\n");
            pixel = _2d(read_image(path + "\\" + dataset + ".png"));
            for (int i = 0; i < 64; i++) {
                char_walsh[i] = multiplication(pixel, walsh[i]);
            }
            
            calc(char_walsh);
            dataset++;

        }

        System.out.println("\n\n-Sonr-\n\n");

    }

    public static void calc(int[][][] char_walsh) throws Exception {
        int predict = 0;
        int sum = 0;
        double[] distances = new double[64];
        double[] char_final = new double[26];
        while (predict < 26) {
            int character[][][]
                    = OCR_dataset.walsh("DataSet\\" + (char) (97 + predict) + ".json");
            for (int i = 0; i < 64; i++) {
                sum+=  Math.abs(OCR_dataset.getDistance(char_walsh[i], character[i]));
            }
            

            char_final[predict] = sum;
            sum=0;
            predict++;
        }
        for (int xxx = 0; xxx < char_final.length; xxx++) {
            System.out.println(char_final[xxx]);
        }

        System.out.println("\n\n\n#--------   "
                + switching((indexOfSmallest(char_final) + 1))
                + " --------#\n\n");

    }

    public static int indexOfSmallest(double[] array) {

        // add this
        if (array.length == 0) {
            return -1;
        }

        int index = 0;
        double min = array[index];

        for (int i = 1; i < array.length; i++) {
            if (array[i] <= min) {
                min = array[i];
                index = i;
            }
        }
        return index;
    }

    public static char switching(int predict) {
        char x = 'a';
        switch (predict) {
            case 1:
                x = 'a';
                break;
            case 2:
                x = 'b';
                break;
            case 3:
                x = 'c';
                break;
            case 4:
                x = 'd';
                break;
            case 5:
                x = 'e';
                break;
            case 6:
                x = 'f';
                break;
            case 7:
                x = 'g';
                break;
            case 8:
                x = 'h';
                break;
            case 9:
                x = 'i';
                break;
            case 10:
                x = 'j';
                break;
            case 11:
                x = 'k';
                break;
            case 12:
                x = 'l';
                break;
            case 13:
                x = 'm';
                break;
            case 14:
                x = 'n';
                break;
            case 15:
                x = 'o';
                break;
            case 16:
                x = 'p';
                break;
            case 17:
                x = 'q';
                break;
            case 18:
                x = 'r';
                break;
            case 19:
                x = 's';
                break;
            case 20:
                x = 't';
                break;
            case 21:
                x = 'u';
                break;
            case 22:
                x = 'v';
                break;
            case 23:
                x = 'w';
                break;
            case 24:
                x = 'x';
                break;
            case 25:
                x = 'y';
                break;
            case 26:
                x = 'z';
                break;

        }

        return x;

    }

}
