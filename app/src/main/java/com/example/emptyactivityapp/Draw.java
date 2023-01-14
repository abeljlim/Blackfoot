package com.example.emptyactivityapp;

//import cmpt120image;

import java.util.Arrays;
import java.util.Random;

public class Draw {

    // Recolor image from the color black to a different color
    // Now considers alpha such that the BG is white and the original image color is black
    public static int[][][] recolorImage_blackOrigImgWhiteBG(int[][][] img, int[] color) {
        int[][][] newImg = cmpt120image.getWhiteImage(img[0].length, img.length);
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                if (!(Arrays.equals(img[i][j], new int[] {255, 255, 255}) || img[i][j][3] == 0) /*if alpha is 0 or the color is white*/) {
                    int[] oldColor = img[i][j];
                    double blackPercent = ((255-oldColor[0]) + (255-oldColor[1]) + (255-oldColor[2])) / (255.0 * 3);
                    // So for color (255,0,0) when blackPercent is 100, newRed would be 255, newGreen would be 0, and newBlue would be 0; and when blackPercent is 0, newRed would be 255, newGreen would be 255, and newBlue would be 255.

                    /*
                    int redRange = 255 - color[0];
                    int greenRange = 255 - color[1];
                    int blueRange = 255 - color[2];

                    int newRed = (int)(color[0] + (redRange * (1-blackPercent)));
                    int newGreen = (int)(color[1] + (greenRange * (1-blackPercent)));
                    int newBlue = (int)(color[2] + (blueRange * (1-blackPercent)));


                    int newRed = (int) ((color[0] * blackPercent) + (255 * (1 - blackPercent)));
                    int newGreen = (int) ((color[1] * blackPercent) + (255 * (1 - blackPercent)));
                    int newBlue = (int) ((color[2] * blackPercent) + (255 * (1 - blackPercent)));

                     */

                    /*
                    newImg[i][j][0] = newRed;
                    newImg[i][j][1] = newGreen;
                    newImg[i][j][2] = newBlue;
                     */
                    newImg[i][j][0] = color[0];
                    newImg[i][j][1] = color[1];
                    newImg[i][j][2] = color[2];
                    newImg[i][j][3] = (int)(blackPercent * 255);
                }
            }
        }
        return newImg;
    }

    public static int[][][] recolorImage_pureRecolor(int[][][] img, int[] color) {
        int[][][] newImg = cmpt120image.getWhiteImage(img[0].length, img.length);
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                if (!Arrays.equals(img[i][j], new int[] {255, 255, 255})) {
                    //int[] oldColor = img[i][j];
                    //double blackPercent = ((255-oldColor[0]) + (255-oldColor[1]) + (255-oldColor[2])) / (255.0 * 3);
                    // So for color (255,0,0) when blackPercent is 100, newRed would be 255, newGreen would be 0, and newBlue would be 0; and when blackPercent is 0, newRed would be 255, newGreen would be 255, and newBlue would be 255.

                    //int redRange = 255 - color[0];
                    //int greenRange = 255 - color[1];
                    //int blueRange = 255 - color[2];

                    //int newRed = (int)(color[0] + (redRange * (1-blackPercent)));
                    //int newGreen = (int)(color[1] + (greenRange * (1-blackPercent)));
                    //int newBlue = (int)(color[2] + (blueRange * (1-blackPercent)));

                    /*
                    int newRed = (int) ((color[0] * blackPercent) + (255 * (1 - blackPercent)));
                    int newGreen = (int) ((color[1] * blackPercent) + (255 * (1 - blackPercent)));
                    int newBlue = (int) ((color[2] * blackPercent) + (255 * (1 - blackPercent)));

                     */

                    newImg[i][j][0] = color[0];
                    newImg[i][j][1] = color[1];
                    newImg[i][j][2] = color[2];

                    //newImg[i][j] = color;
                }
            }
        }
        return newImg;
    }


    // Now considers alpha where colors that are 255,255,255,255 are treated as 0,0,0,0
    // where this may not work for multiple overlapping images ... or would it? As it converts a color to an alpha-based color, i.e. a color that takes alpha into account
    // where this would basically mean that minify would mean that the color would have a white BG for 255,255,255,255 to be converted to a 0-alpha color
    public static int[][][] minify_whiteBG(int[][][] img) {

        // Calculate the dimensions of the new image.
        int newHeight = img.length / 2;
        int newWidth = img[0].length / 2;

        // Create a new blank image with the new dimensions.
        int[][][] newImg = cmpt120image.getWhiteImage(newWidth, newHeight);

        // Iterate through each pixel in the new image.
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                // Calculate the sum of the R/G/B values for the corresponding 2x2 block of pixels in the original image.
                int sumR = 0;
                int sumG = 0;
                int sumB = 0;
                for (int ii = 0; ii < 2; ii++) {
                    for (int jj = 0; jj < 2; jj++) {
                        sumR += img[i * 2 + ii][j * 2 + jj][0];
                        sumG += img[i * 2 + ii][j * 2 + jj][1];
                        sumB += img[i * 2 + ii][j * 2 + jj][2];
                    }
                }

                // Set the R/G/B values of the current pixel in the new image to the average of the corresponding 2x2 block of pixels in the original image.
                newImg[i][j][0] = sumR / 4;
                newImg[i][j][1] = sumG / 4;
                newImg[i][j][2] = sumB / 4;
            }
        }
        return newImg;
    }

    /**
     * Flips the image img left-to-right, and returns a new image with the result.
     *
     * @param img The image to flip.
     * @return The flipped image.
     */
    public static int[][][] mirror(int[][][] img) {
        // Create a new blank image with the same dimensions as the original image.
        int[][][] newImg = cmpt120image.getWhiteImage(img[0].length, img.length);

        // Iterate through each pixel in the new image.
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                // Set the R/G/B values of the current pixel in the new image to the R/G/B values of the corresponding pixel in the original image, flipped horizontally.
                newImg[i][j] = img[i][img[0].length - j - 1];
            }
        }
        return newImg;
    }

    public static void drawItem(int[][][] canvas, int[][][] item, int row, int col) {
        // Iterate through each pixel in the item image.
        for (int i = 0; i < item.length; i++) {
            for (int j = 0; j < item[0].length; j++) {
                // If the current pixel in the item image is not white, draw it on the corresponding pixel in the canvas image.
                if (!Arrays.equals(item[i][j], new int[] {255, 255, 255})) {
                    canvas[row + i][col + j] = item[i][j];
                }
            }
        }
    }

    public static void distributeItems(int[][][] canvas, int[][][] item, int n) {
        // Create a new Random object for generating random numbers.
        Random random = new Random();

        // Draw the item on the canvas n times.
        for (int i = 0; i < n; i++) {
            // Generate random row and column values within the bounds of the canvas image.
            int row = random.nextInt(canvas.length - item.length + 1);
            int col = random.nextInt(canvas[0].length - item[0].length + 1);

            // Draw the item on the canvas at the generated location.
            drawItem(canvas, item, row, col);
        }
    }
}
