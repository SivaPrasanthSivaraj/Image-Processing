import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




class Imageio{

    static void getRGB(int [][]red,int [][]green,int [][]blue,int width,int height,BufferedImage Image){
        try{
            System.out.println("Getting R,G,B values.."); 
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = Image.getRGB(x, y);
                    red[x][y] = (rgb >> 16) & 0xFF;
                    green[x][y] = (rgb >> 8) & 0xFF;
                    blue[x][y] = rgb & 0xFF;
                }
            }
            Thread.sleep(400);
        }catch(Exception e){
            System.out.println("Error While Reading Pixel Values");
            System.exit(1);}
        
    }

    static void combine(int width,int height,int filteredRed[][],int filteredGreen[][],int filteredBlue[][],String path ){
        System.out.println("Creating Buffered Image..");
        BufferedImage filteredImage = new BufferedImage(width+1, height+1, BufferedImage.TYPE_INT_RGB);
        try{
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = (filteredRed[x][y] << 16) | (filteredGreen[x][y] << 8) | filteredBlue[x][y];
                    filteredImage.setRGB(x, y, rgb);
                }
            }
            Thread.sleep(400);
        }catch(Exception e){
            System.out.println("Error While Creating Output Buffered Image..");
            System.exit(1);}
        writeIM(filteredImage,path);
    }

    static void writeIM(BufferedImage filteredImage,String path){
        System.out.println("Writing Pixel Data..\n");
        try {
                File output = new File(path);
                ImageIO.write(filteredImage,"jpeg", output);
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(("Error While Writing File"));
                System.exit(1);
            }
        }

}

class Filters{

    Filters(){
        System.out.println("Applying Filters..");
        try{
            Thread.sleep(400);
            }catch(Exception e){System.exit(1);}
    }

    static int[][] applyMeanFilter(int[][] input, int width, int height) {
        int[][] output = new int[width+2][height+2];
        int[][] ans = new int[width+2][height+2];

        int x=width,y=height;
        output[0][0]=0;
        output[0][y+1]=0;
        output[x+1][0]=0;
        output[x+1][y+1]=0;
        for(int i=1;i<=y;i++)
            output[0][i]=0;
        for(int i=1;i<=y;i++)
            output[x+1][i]=0;
        for(int i=1;i<=x;i++)
            output[i][0]=0;
        for(int i=1;i<=x;i++)
            output[i][y+1]=0;

        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                output[i+1][j+1]=input[i][j];
            }
        }
        int sum1=0;
        for(int i=1;i<x+1;i++){
            for(int j=1;j<y+1;j++){
                sum1=output[i-1][j-1]+output[i-1][j]+output[i-1][j+1]+output[i][j-1]+output[i][j]+output[i][j+1]+output[i+1][j-1]+output[i+1][j]+output[i+1][j+1];
                int avg=sum1/9;
                 ans[i][j]=avg;
        }
    }
    return ans;
}

    static int[][] applyMedianFilter(int[][] input, int width, int height) {
        int[][] output = new int[width+2][height+2];
        int[][] ans = new int[width+2][height+2];
        int x=width,y=height,e[]=new int[9];
        output[0][0]=input[0][0];
        output[0][y+1]=input[0][y-1];
        output[x+1][0]=input[x-1][0];
        output[x+1][y+1]=input[x-1][y-1];
    
    
        for(int i=1;i<=y;i++)
            output[0][i]=input[0][i-1];
        for(int i=1;i<=y;i++)
            output[x+1][i]=input[x-1][i-1];
        for(int i=1;i<=x;i++)
            output[i][0]=input[i-1][0];
        for(int i=1;i<=x;i++)
            output[i][y+1]=input[i-1][y-1];
    
        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                output[i+1][j+1]=input[i][j];
            }
        }
    
        for(int i=1;i<x;i++){
            for(int j=1;j<y;j++){
                e[0]=output[i-1][j-1];
                e[1]=output[i-1][j];
                e[2]=output[i-1][j+1];
                e[3]=output[i][j-1];
                e[4]=output[i][j];
                e[5]=output[i][j+1];
                e[6]=output[i+1][j-1];
                e[7]=output[i+1][j];
                e[8]=output[i+1][j+1];
    
                ans[i][j]=median_matrix(e);
            }
        }
        return ans;
    }
    
    static int median_matrix(int a[]){
        int k[]=a;
        for (int i=0;i<9-1;i++){
            for (int j=0;j<9-i-1;j++){
                if (k[j] < k[j + 1]) {
                    int temp=k[j];
                    k[j]=k[j + 1];
                    k[j + 1]=temp;
                }
    
            }
        }
        return k[4];
    }

    static BufferedImage applyIntensityTransformation(BufferedImage img, double gamma) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage gammaCorrected = new BufferedImage(width, height, img.getType());

        int[] gammaLUT = new int[256];
        for (int i = 0; i < 256; i++) {
            gammaLUT[i] = (int) (255 * Math.pow(i / 255.0, gamma));
        }
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int correctedR = gammaLUT[r];
                int correctedG = gammaLUT[g];
                int correctedB = gammaLUT[b];

                int correctedRGB = (correctedR << 16) | (correctedG << 8) | correctedB;
                gammaCorrected.setRGB(x, y, correctedRGB);
            }
        }

        return gammaCorrected;
    }

    static BufferedImage applylog(BufferedImage image){
        int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            double C = 1.0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(image.getRGB(x, y));
                    double r = color.getRed();
                    double S = C * Math.log(1 + r);
                    double T = 255 / (C * Math.log(256));
                    int transformedValue = (int) (T * S);
                    transformedValue = Math.min(255, Math.max(0, transformedValue));

                    Color transformedColor = new Color(transformedValue, transformedValue, transformedValue);

                    transformedImage.setRGB(x, y, transformedColor.getRGB());
                }
            }
            return transformedImage;
    }

}

public class ImageProcessing extends JFrame{
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        
        try {
            File Inpath=new File("D:\\CLG WORK\\Trash\\Sem2\\Code\\JAVA\\Test_img_3.jpg");
            String Opath="NULL";
            BufferedImage Image = ImageIO.read(Inpath);
            int width = Image.getWidth();
            int height = Image.getHeight();
            int[][][]result;
            int[][] red = new int[width][height];
            int[][] green = new int[width][height];
            int[][] blue = new int[width][height];
            
            System.out.print("1.Mean Filter\n2.Median Filter\n3.Filters in Seperate File\n4.Filters in Single File\n5.Intensity Transformation \n6.Log Intensity Transformation\nEnter your option : ");
            int opt=sc.nextInt();
            Imageio.getRGB(red,green,blue,width,height,Image);
            switch (opt) {
                case 1:
                    Opath="D:\\CLG WORK\\Trash\\Sem2\\Code\\JAVA\\mean.jpg";
                    result=applyMean(red, blue, green, width, height);
                    Imageio.combine(width, height, result[0], result[1],result[2],Opath);
                    break;
            
                case 2:
                    Opath="D:\\\\CLG WORK\\\\Trash\\\\Sem2\\\\Code\\\\JAVA\\\\Median.jpg";
                    result=applyMedian(red, blue, green, width, height);
                    Imageio.combine(width, height, result[0], result[1],result[2],Opath);
                    break;
                    
                case 3:
                    System.out.print("Enter gamma value for intensity transformation: ");
                    double gamma = sc.nextDouble();
                    Opath="C:\\Users\\Suganth\\OneDrive\\Pictures\\Mean.jpeg";
                    Thread.sleep(300);
                    System.out.println("Applying For Mean..");
                    result=applyMean(red, blue, green, width, height);
                    Imageio.combine(width, height, result[0], result[1],result[2],Opath);
                    new frames(Opath);

                    Thread.sleep(300);
                    Opath="C:\\Users\\Suganth\\OneDrive\\Pictures\\Median.jpeg";
                    System.out.println("Applying For Median..");
                    result=applyMedian(red, blue, green, width, height);
                    Imageio.combine(width, height, result[0], result[1],result[2],Opath);
                    new frames(Opath);

                    Thread.sleep(300);
                    Opath="C:\\Users\\Suganth\\OneDrive\\Pictures\\Intensity.jpeg";
                    System.out.println("Applying For Intensity Transformation..");
                    Imageio.writeIM(Filters.applyIntensityTransformation(Image,gamma), Opath);
                    new frames(Opath);

                    Thread.sleep(300);
                    System.out.println("Applying For Log Intensity Transformation..");
                    Opath="C:\\Users\\Suganth\\OneDrive\\Pictures\\log.jpeg";
                    Imageio.writeIM(Filters.applylog(Image),Opath);
                    new frames(Opath);
                    break;

                case 4:
                    Opath="C:\\Users\\Suganth\\OneDrive\\Pictures\\Mean,Median and Intensity.jpeg";
                    System.out.print("Enter gamma value for intensity transformation: ");
                    gamma = sc.nextDouble();
                    result=applyMedian(red, blue, green, width, height);
                    result=applyMean(result[0],result[1],result[2],width, height);

                    Thread.sleep(400);
                    System.out.println("Applying Filters");
                    BufferedImage filteredImage = new BufferedImage(width+1, height+1, BufferedImage.TYPE_INT_RGB);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            int rgb = (result[0][x][y] << 16) | (result[1][x][y] << 8) | (result[2][x][y]);
                            filteredImage.setRGB(x, y, rgb);
                        }
                    }
                    Imageio.writeIM((Filters.applyIntensityTransformation(filteredImage, gamma)), Opath);
                    break;
                    
                case 5:
                    Opath="C:\\Users\\Suganth\\OneDrive\\Pictures\\Intensity.jpeg";
                    System.out.print("Enter gamma value for intensity transformation: ");
                    gamma = sc.nextDouble();
                    Imageio.writeIM(Filters.applyIntensityTransformation(Image,gamma), Opath);
                    break;

                case 6:
                    Opath="C:\\Users\\Suganth\\OneDrive\\Pictures\\log Intensity.jpeg";
                    Imageio.writeIM(Filters.applylog(Image),Opath);
                    break;

                default :
                    System.out.println("Invalid Option.");
                    System.exit(1);
                    break;
            }
            if((opt>=1 & opt<3)|(opt>3 & opt<=6))
            new frames(Opath);
            sc.close();
            
        } catch (Exception e) {
            System.out.println("Error Occured");
            System.exit(1);
        }
        System.out.println("Filtered image created successfully.");
        
    }

    

    static int[][][]  applyMean(int[][] red,int[][] blue,int[][] green,int width,int height){

            int[][]  meanRed   = Filters.applyMeanFilter(red, width, height);
            int[][]  meanGreen = Filters.applyMeanFilter(green, width, height);
            int[][]  meanBlue  = Filters.applyMeanFilter(blue, width, height);
            int [][][]result=new int[3][][];
            result[0]=meanRed;
            result[1]=meanGreen;
            result[2]=meanBlue;
            return result;
    }

    static int[][][] applyMedian(int[][] red,int[][] blue,int[][] green,int width,int height){
            int[][]  medianRed   = Filters.applyMedianFilter(red, width, height);
            int[][]  medianGreen = Filters.applyMedianFilter(green, width, height);
            int[][]  medianBlue  = Filters.applyMedianFilter(blue, width, height); 
            int [][][]result=new int[3][][];
            result[0]=medianRed;
            result[1]=medianGreen;
            result[2]=medianBlue;
            return result;
    }
    
}

class frames extends JFrame{

    public frames(String path) {
        setTitle("Image Options");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        JButton openF = new JButton("Open Folder");
        openF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFolder(path);
            }
        });
        JButton openI = new JButton("Open Output Image");
        openI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispImage(path);
            }
        });
        
        openF.setPreferredSize(new Dimension(120, 40));
        openI.setPreferredSize(new Dimension(200, 40));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select an Option :"));

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(openF, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(openI, gbc);

        add(panel);
        setSize(halfScreen());
        setLocationRelativeTo(null);
        setVisible(true);
}

        private Dimension halfScreen() {
            Dimension Ss = Toolkit.getDefaultToolkit().getScreenSize();
            int HW = Ss.width / 2;
            int HH = Ss.height / 2;
            return new Dimension(HW, HH);
        }

        private void openFolder(String path) {
            try {
                File f=new File(path);
                Desktop.getDesktop().open(f.getParentFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private void dispImage(String path) {
            setTitle("Output Image");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            ImageIcon icon = new ImageIcon(path);
            JLabel label = new JLabel(icon);
            label.setHorizontalAlignment((SwingConstants.CENTER));
            JLabel fname = new JLabel(new File(path).getName());
            fname.setHorizontalAlignment(SwingConstants.CENTER);
            JFrame frame = new JFrame();
            frame.add(new JScrollPane(fname), BorderLayout.NORTH);
            frame.add(new JScrollPane(label), BorderLayout.CENTER);
            frame.setSize(halfScreen());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
    }
}