
package hough_transformation;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Hough_transformation {
        public void run(String[] args) {
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;

        // [load]
        String default_file = "E:/abobakr/Pictures/images/hough/sudoku.png";
        String filename = ((args.length > 0) ? args[0] : default_file);

        // Load an image
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);

        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }
      
        // Edge detection
        Imgproc.Canny(src, dst, 50, 200, 3, false);
        

        // Copy edges to the images that will display the results 
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();

        
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); // runs the actual detection
        /*
        dst: Output of the edge detector.
        lines: A vector that will store the parameters (r,θ) of the detected lines
        rho : The resolution of the parameter r in pixels. We use 1 pixel.
        theta: The resolution of the parameter θ in radians. We use 1 degree (CV_PI/180)
        threshold: The minimum number of intersections to detect a line
        */
        
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            
            //convert Polar coordinate (rho, theta) to Cartesian coordinate (x,y) 
            double a = Math.cos(theta);
            double b = Math.sin(theta);
            double x0 = a*rho;
            double y0 = b*rho;
            //Calculate the point in the coordinate (x,y)
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 255, 255), 3, Imgproc.LINE_AA, 0);
        }
        
        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); //
         //output  (x0,y0,x1,y1)
         
        /*
        . . .
        minLinLength: The minimum number of points that can form a line.
        Lines with less than this number of points are disregarded.
        maxLineGap: The maximum gap between two points to be considered in the same line.
        */
        
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        // Show results
        HighGui.imshow("Source", src);
        HighGui.imshow("Detected Lines (in red) - Standard Hough Line Transform", cdst);
        HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform", cdstP);
        
        System.out.println("Done");
        
        HighGui.waitKey();
        System.exit(0);
        //! [exit]
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Start ");
         new Hough_transformation().run(args);
         
       }
    

    
}
