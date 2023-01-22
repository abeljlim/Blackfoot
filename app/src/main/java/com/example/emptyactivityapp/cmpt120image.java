package com.example.emptyactivityapp;

//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
import static android.content.ContentValues.TAG;

import java.io.File;
import java.io.FileOutputStream;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import java.awt.image.DataBufferByte;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class cmpt120image {

// cmpt120image.java
// CMPT 120
// November 2022
// Some helper functions to wrap the Pygame image functions
/*
        public static int[][][] getImage(String filename) throws IOException {
            // Load image from file
            BufferedImage image = ImageIO.read(new File(filename));

            // Convert image to 3D array of RGB values
            int[][][] pixels = new int[image.getHeight()][image.getWidth()][3];
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    pixels[i][j][0] = (image.getRGB(j, i) >> 16) & 0xff;
                    pixels[i][j][1] = (image.getRGB(j, i) >> 8) & 0xff;
                    pixels[i][j][2] = image.getRGB(j, i) & 0xff;
                }
            }
            return pixels;
        }

        public static void saveImage(int[][][] pixels, String filename) throws IOException {
            // Create new image with the same dimensions as the input image
            BufferedImage image = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);

            // Convert 3D array of RGB values to image
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    int r = pixels[i][j][0];
                    int g = pixels[i][j][1];
                    int b = pixels[i][j][2];
                    image.setRGB(j, i, (r << 16) | (g << 8) | b);
                }
            }

            // Save image to file
            ImageIO.write(image, "png", new File(filename));
        }

    public static void showImage(int[][][] pixels) throws IOException {
        // Convert 3D array of RGB values to image
        BufferedImage image = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int r = pixels[i][j][0];
                int g = pixels[i][j][1];
                int b = pixels[i][j][2];
                image.setRGB(j, i, (r << 16) | (g << 8) | b);
            }
        }

        // Create window to display image
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(new javax.swing.ImageIcon(image));
        label.setPreferredSize(new java.awt.Dimension(image.getWidth(), image.getHeight()));
        frame.getContentPane().add(label, java.awt.BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    */
    public static void saveImage(int[][][] pixels, String filename) {
        // Get the dimensions of the image.
        int width = pixels[0].length;
        int height = pixels.length;

        // Create a new bitmap to hold the image.
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Iterate through each pixel in the image and set its color.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int r = pixels[row][col][0];
                int g = pixels[row][col][1];
                int b = pixels[row][col][2];
                image.setPixel(col, row, Color.rgb(r, g, b));
            }
        }

        // Save the image to a file.
        try {
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ImageView imageView;

    public static int[][][] getImage_blackWhiteBG(Context context, String assetName) {
        /*
        // Get the AssetManager instance.
        AssetManager assetManager = context.getAssets();

        // Load the image from the asset.
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(assetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        int drawableID = context.getResources().getIdentifier(assetName, "drawable", context.getPackageName());
        ImageView iv = new ImageView(context);
        iv.setImageResource(drawableID);

        //Bitmap image = BitmapFactory.decodeStream(inputStream);

        Bitmap image=((BitmapDrawable)iv.getDrawable()).getBitmap();
        if (image == null) {
            // handle the case where the image is null, for example by logging an error
            Log.e(TAG, "Failed to decode image from asset '" + assetName + "' and PackageName "+context.getPackageName());
            return null; // or some other default value
        }

        // Get the dimensions of the image.
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a new 3D array to hold the image pixels.
        int[][][] pixels = new int[height][width][4];

        // Iterate through each pixel in the image and get its color.
        // Convert the color to a corresponding 'more opaque' black color and corresponding alpha
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = image.getPixel(col, row);
                int origR = Color.red(pixel);
                int origG = Color.green(pixel);
                int origB = Color.blue(pixel);
                //int origA = 255;


                // Convert the color to a corresponding 'more opaque' black color and corresponding alpha
                // Get the distance from white, then divide that by a max distance from white of 255*3 to get the alpha
                double differenceFromWhite = ((255 - origR) + (255 - origG) + (255 - origB));
                double A = differenceFromWhite / (255*3);

                // So for an alpha of 127.5, we get the corresponding 'more opaque' color to be where the distance from white (i.e. RGB of (255,255,255)) would be divided by (127.5/255),
                // so RDistanceFromWhite_ForMO would be 255 - (255+255+0+0)/4, and this would be divided by (127.5/255) then the new R_MO color would be 255 - RDistanceFromWhite_ForMO after said division
                // and the same would go for G and B
                // where this would all ultimately result in R, G, and B being 0 or close to 0, where I can treat all of them as being 0

                pixels[row][col][0] = 0;
                pixels[row][col][1] = 0;
                pixels[row][col][2] = 0;
                pixels[row][col][3] = (int)(A * 255);
            }
        }

        return pixels;
    }

    public static int[][][] getImage_blackWhiteBG(ImageView iv) {
        /*
        // Get the AssetManager instance.
        AssetManager assetManager = context.getAssets();

        // Load the image from the asset.
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(assetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        //int drawableID = context.getResources().getIdentifier(assetName, "drawable", context.getPackageName());
        //ImageView iv = new ImageView(context);
        //iv.setImageResource(drawableID);

        //Bitmap image = BitmapFactory.decodeStream(inputStream);
        //iv.setDrawingCacheEnabled(true);
        /*
        if((BitmapDrawable)iv.getDrawable() == null) {
            Log.e(TAG, "Failed to get drawable from ImageView");
            return null; // or some other default value
        }*/
        Bitmap image=((BitmapDrawable)iv.getDrawable()).getBitmap();
        //iv.buildDrawingCache();
        //Bitmap image = iv.getDrawingCache();
        //iv.destroyDrawingCache();

/*
        if (image == null) {
            // handle the case where the image is null, for example by logging an error
            Log.e(TAG, "Failed to decode image from ImageView");
            return null; // or some other default value
        }*/
        //Log.e(TAG, "Passed through getting a Bitmap");

        // Get the dimensions of the image.
        int width = image.getWidth();
        int height = image.getHeight();

        Log.e(TAG, "image.getWidth() (bitmap width): "+width+"; image.getHeight() (bitmap height): "+height);

        // Create a new 3D array to hold the image pixels.
        int[][][] pixels = new int[height][width][4]; //4th color array element is for alpha as a value out of 255

        // Iterate through each pixel in the image and get its color.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = image.getPixel(col, row);
                pixels[row][col][0] = Color.red(pixel);
                pixels[row][col][1] = Color.green(pixel);
                pixels[row][col][2] = Color.blue(pixel);
                pixels[row][col][3] = 255;
            }
        }

        return pixels;
    }

    public static void showImage(int[][][] pixels, Context context) {
        // Get the dimensions of the image.
        int width = pixels[0].length;
        int height = pixels.length;

        // Create a new bitmap to hold the image.
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Iterate through each pixel in the image and set its color in the bitmap.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int[] pixel = pixels[row][col];
                image.setPixel(col, row, Color.rgb(pixel[0], pixel[1], pixel[2]));
            }
        }

        // TODO: Display the image on the screen using an ImageView or similar component.
        // Create an ImageView to display the image.
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(image);

        // Add the ImageView to the layout of the activity.
        LinearLayout layout = (LinearLayout) ((Activity) context).findViewById(R.id.activity_main);
        layout.addView(imageView);
    }


    public static void renderImageToView_whiteBG(int[][][] pixels, ImageView iv) {
        // Get the dimensions of the image.
        int width = pixels[0].length;
        int height = pixels.length;

        // Create a new bitmap to hold the image.
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Iterate through each pixel in the image and set its color in the bitmap.
        // Default color for a pixel that has 0 alpha is white
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int[] pixel = pixels[row][col];


                /*
                *
                Short answer:
                if we want to overlay c0 over c1 both with some alpha then
                a01 = (1 - a0)·a1 + a0
                r01 = ((1 - a0)·a1·r1 + a0·r0) / a01
                g01 = ((1 - a0)·a1·g1 + a0·g0) / a01
                b01 = ((1 - a0)·a1·b1 + a0·b0) / a01
                *
                * For overlaying over white, this would be
                * r0 over 1 = (1 - a0)·r1 (white background fraction) + a0·r0 (alpha-adjusted color fraction)
                * and the same for g & b components
                * where I would consider this to be where a, r, g, and b being from 0 to 1, or just a being from 0 to 1 and where the rest would be 0 to 255
                * */
                // These are colors when adjusted for alpha when having a white background.
                int redColor = (int) ((pixel[0] * (pixel[3] / 255.0)) /* alpha-adjusted color fraction, a0·r0 */ + (255 - pixel[3]) /* white background fraction, (1 - a0)·r1 */);
                int greenColor = (int) ((pixel[1] * (pixel[3] / 255.0)) + (255 - pixel[3]));
                int blueColor = (int) ((pixel[2] * (pixel[3] / 255.0)) + (255 - pixel[3]));

                image.setPixel(col, row, Color.rgb(redColor, greenColor, blueColor));
            }
        }

        // TODO: Display the image on the screen using an ImageView or similar component.
        // Create an ImageView to display the image.
        //ImageView imageView = new ImageView(context);
        iv.setImageBitmap(image);

        // Add the ImageView to the layout of the activity.
        //LinearLayout layout = (LinearLayout) ((Activity) context).findViewById(R.id.activity_main);
        //layout.addView(imageView);
    }

    /*
    public static void displayImage(int[][][] pixels) {
// Get the dimensions of the image.
        int width = pixels[0].length;
        int height = pixels.length;

        // Create a new bitmap to hold the image.
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Iterate through each pixel in the image and set its color in the bitmap.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int[] pixel = pixels[row][col];
                image.setPixel(col, row, Color.rgb(pixel[0], pixel[1], pixel[2]));
            }
        }

        // Display the image on the screen using an ImageView.
        ImageView imageView = findViewById(R.id.activity_main);
        imageView.setImageBitmap(image);
    }
*/
    public static int[][][] getBlackImage(int width, int height) {
        int[][][] pixels = new int[height][width][4];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j][0] = 0;
                pixels[i][j][1] = 0;
                pixels[i][j][2] = 0;
                pixels[i][j][3] = 255;
            }
        }
        return pixels;
    }

    public static int[][][] getWhiteImage(int width, int height) {
        int[][][] pixels = new int[height][width][4];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j][0] = 255;
                pixels[i][j][1] = 255;
                pixels[i][j][2] = 255;
                pixels[i][j][3] = 255;
            }
        }
        return pixels;
    }
}
