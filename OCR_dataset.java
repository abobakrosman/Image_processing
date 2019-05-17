/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 *
 * @author abobakr
 */
public class OCR_dataset {

    //public static void main(String[] args) throws Exception {
    //  int[][][] walsh;
    //walsh = walsh("walsh.json");
    //print_walsh(walsh);
    // }
    public static int[][][] walsh(String json_path) throws Exception {
        JSONArray jsArray;
        jsArray = (JSONArray) new JSONParser().parse(
                new FileReader(json_path));

        int[][][] walsh = new int[64][64][64];
        int[][] walsh_temp = new int[64][64];
        Iterator i = jsArray.iterator();

        String temp_value;
        int[] result = new int[64];
        List< Integer> test = new ArrayList<>();

        int index = 0;
        while (i.hasNext()) {
            test = (List< Integer>) i.next();
            for (int king = 0; king < 64; king++) {
                temp_value = "" + test.get(king);
                //System.out.println("Check this sound "+temp_value);
                temp_value = temp_value.substring(1, temp_value.length() - 1);
                result = convert(temp_value);
                walsh_temp[king] = result;

            }
            for (int lll = 0; lll < 64; lll++) {
                System.arraycopy(walsh_temp[lll], 0, walsh[index][lll], 0, 64);
            }
            index++;
        }
        return walsh;

    }

    public static int[] convert(String arr) {
        int result[] = new int[64];
        StringTokenizer temp = new StringTokenizer(arr, ",");
        int i = 0;
        while (temp.hasMoreTokens()) {
            result[i] = Integer.parseInt(temp.nextToken());
            i++;
        }

        return result;
    }

    public static void print_walsh(int[][][] walsh) {

        for (int w = 0; w < 64; w++) {
            System.out.println("\n\n############### " + w);
            for (int ii = 0; ii < 64; ii++) {
                System.out.println();
                for (int j = 0; j < 64; j++) {
                    System.out.print(" " + walsh[w][ii][j]);
                }
            }
        }
    }

    public static void write_json(int a[][], String filename, int index) throws Exception {

        FileWriter fileWriter = new FileWriter(filename, true); //Set true for append mode
        PrintWriter pw = new PrintWriter(fileWriter);
        //JSONObject jo = new JSONObject();
        JSONArray jsArray = null;
        if (index == 0) {
            pw.println("[");
        }

        pw.println("[");
        for (int i = 0; i < 64; i++) {
            jsArray = new JSONArray();
            for (int j = 0; j < 64; j++) {
                jsArray.add(a[i][j]);
            }
            pw.println(jsArray.toJSONString());
            if ((i + 1) != 64) {
                pw.print(",");
            }
        }
        pw.println("]");
        if (index == 63) {
            pw.println("]");
        }

        pw.flush();
        pw.close();

    }

    public static double getDistance(int[][] city1, int[][] city2) {
        double[] Distance = new double[64];

        int sum = 0;
        for (int j = 0; j < 64; j++) {
            for (int i = 0; i < 64; i++) {
                int d = city1[j][i] - city2[j][i];
                sum += (d * d);
            }
            Distance[j] = (Math.sqrt(sum));

            sum = 0;
        }

        sum = 0;
        for (int i = 0; i < 64; i++) {
            sum += Math.abs(Distance[i]);
        }

        return sum;
    }

}
