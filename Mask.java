import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

class Mask {
	static int x = 0;static int y = 0;//image dimensions
	static int x1 = 0;static int y1 = 0;//Starting point
	static int x2 = 0;static int y2 = 0;//Ending point
	byte [][]mask=new byte[3][3];

 public static void main(String args[]) {
	 File[] files=(new File(".")).listFiles(new FilenameFilter() {
		 @Override
		 public boolean accept(File dir, String name) {
			 return name.endsWith(".txt");
			 }
			 });
	 for (File file : files) {
		 imageProcess(file.getName());//Take all .txt file as input for the program (image files 0's and 1's)
	 }
 } //main

 public static void imageProcess(String fileName){
	 byte[][] image = null;
	 try {
	  File file = new File(fileName); //creates a new file instance
 	  FileReader fr = new FileReader(file); //reads the file
   	  BufferedReader br = new BufferedReader(fr); //creates a buffering character input stream

   	  if(countDim(file)==1){//Find the dimensions and check if this is a valid image file(0's and 1's)
		  image = new byte[y][x];
	      toArray(image, br);
   	      findsLine(fileName,image);
   	      }
   	  else
   	  System.out.println(fileName+" is not a valid image.txt File");

  } catch (IOException e) {
	  e.printStackTrace();
  }

 }//imageProcess

 public static void findsLine(String fileName,byte[][] image) {
	 List <String> lines = new ArrayList <> ();
	 int direction=0;

	 col: for (int j = 0; j < image.length; j++) {
		 row: for (int i = 0; i < image[0].length; i++) {
			 if (image[j][i] == 1 && checked(image,j,i)==0) {
				 direction=directions(image,j,i);
				 if(direction!=4 && direction!=-1 ){
					 lines.add("" + i);lines.add("" + j);
					 mask(image,j,i);
					 lines.add("" + x2);lines.add("" + y2);

				 }
				 else
				 if(direction==4){
				 mask4(image,j,i);
				 lines.add("" + x1);lines.add("" + y1);lines.add("" + x2);lines.add("" + y2);
				 }

				 }//black pixel not been checked yet
   		 } //row
  	 } //col

	 displayLines(fileName,lines);
	 System.out.println();

 } //findsLine

 public static int mask4(byte[][] image, int j, int i){
	 int dir=directions(image,j,i);

	  if(dir==4){
	  x1=i;
	  y1=j;
	  mask(image,j,i+1);
	  //mask(image,j+1,i-1);


  }
 	 return 0;

 }//maskl

  public static int mask(byte[][] image, int j, int i){
	  int dir=directions(image,j,i);

	  if(dir==0)
	  return mask(image,j,i+1);
	  if(dir==1)
	  return mask(image,j+1,i+1);
	  else
	  if(dir==2)
	  return mask(image,j+1,i);
	  else
	  if(dir==3)
	  return mask(image,j+1,i-1);

	  x2=i;
	  y2=j;
 	 return 0;

 }//mask

 public static int countDim(File file) throws IOException {
	 int c = 0;char last='\n';
	 x=0;y=0;
	 boolean flag = true;

	 FileReader fr = new FileReader(file); //reads the file
     BufferedReader br = new BufferedReader(fr); //creates a buffering character input stream

     while ((c = br.read()) != -1){
		 last=(char)c;
		 if(Character.getNumericValue(c)!=0 && Character.getNumericValue(c)!=1 && Character.getNumericValue(c)!=-1)
			 return 0;
		 if ((char) c == '\n') {
			 y++;
			 flag = false;
   		 }
   		 if (flag)
   		 x++;
     }
     x--;
     if(Character.getNumericValue(last)==0 || Character.getNumericValue(last)==1)
     y++;

     //System.out.println(x + "    " + y);
     return 1;
 } //countDim

 public static void toArray(byte[][] image, BufferedReader br) throws IOException {
	 int c = 0;int i = 0;int j = 0;

	 a: while ((c = br.read()) != -1){
		 if ((char) c == '\n') {
			 j++;
			 i = 0;
			 continue a;
		 }
		 else
		 if (i < x)
		 image[j][i] = (byte)Character.getNumericValue(c);

		 i++;
	    }//while

 } //toArray

public static int checked(byte [][]image,int j,int i){
	byte flag=0;
	if( ( i>0 && image[j][i-1]==1)  || ( j>0 && ( (image[j-1][i]==1) || (i>0 && image[j-1][i-1]==1) || ( (i+1) <image[0].length && image[j-1][i+1]==1 ) ) ) )
	flag=1;

	return flag;

}

public static int directions(byte [][]image,int j,int i){

	byte result=-1;
	if(i+1<image[0].length && image[j][i+1]==1)
	result++;

	if(j+1<image.length && i+1 <image[0].length && image[j+1][i+1]==1)
	result=1;

	if(j+1<image.length && image[j+1][i]==1)
	result=2;

	if(j+1<image.length && i>0 && image[j+1][i-1]==1)
	result+=4;


	return result;

}

public static void displayLines(String fileName,List < String > lines ){
	System.out.print(fileName+" ");
	for (int i = 0; i < lines.size(); i++) {
		if (i % 2 == 0) {
			System.out.print("(");
			System.out.print(lines.get(i) + ",");
		}
		else
		System.out.print(lines.get(i) + ") ");
     }
      System.out.println();
}//displayLines

} //class