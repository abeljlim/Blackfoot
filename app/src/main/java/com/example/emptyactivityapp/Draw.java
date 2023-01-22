package com.example.emptyactivityapp;

//import cmpt120image;

import java.util.Arrays;
import java.util.Random;

public class Draw {

    // Recolor image from the color black to a different color
    // Now considers alpha such that the BG is white and the original image color is black
    // Where this now uses the alpha from getImage
    public static int[][][] recolorImage_blackOrigImgWhiteBG(int[][][] img, int[] color) {
        int[][][] newImg = cmpt120image.getWhiteImage(img[0].length, img.length);
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                if (!(Arrays.equals(img[i][j], new int[] {255, 255, 255, 255}) || img[i][j][3] == 0) /*if alpha is 0 or the color is white*/) {
                    int[] oldColor = img[i][j];
                    //double blackPercent = ((255-oldColor[0]) + (255-oldColor[1]) + (255-oldColor[2])) / (255.0 * 3);
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
                    //newImg[i][j][3] = (int)(blackPercent * 255);
                    newImg[i][j][3] = oldColor[3];
                }
            }
        }
        return newImg;
    }

    public static int[][][] recolorImage_pureRecolor(int[][][] img, int[] color) {
        int[][][] newImg = cmpt120image.getWhiteImage(img[0].length, img.length);
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                if (!(Arrays.equals(img[i][j], new int[] {255, 255, 255, 255}) || img[i][j][3] == 0)) {
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
                    newImg[i][j][3] = 255;
                    //newImg[i][j] = color;
                }
            }
        }
        return newImg;
    }


    // Now considers alpha where colors that are 255,255,255,255 are treated as 0,0,0,0
    // where this may not work for multiple overlapping images ... or would it? As it converts a color to an alpha-based color, i.e. a color that takes alpha into account
    // where this would basically mean that minify would mean that the color would have a white BG for 255,255,255,255 to be converted to a 0-alpha color
    public static int[][][] minify_whiteBGAndToBeDoneJustOnceOnAnImg(int[][][] img) {

        // Calculate the dimensions of the new image.
        int newHeight = img.length / 2;
        int newWidth = img[0].length / 2;

        // Create a new blank image with the new dimensions.
        int[][][] newImg = cmpt120image.getWhiteImage(newWidth, newHeight);

        // Iterate through each pixel in the new image.
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                // TODO: Calculate the weighted (by alpha value, where the lower the alpha value, the more white that the color would be) sum of the R/G/B values and the corresponding A value that would be adjusted for the weighting of the sum for the corresponding 2x2 block of pixels in the original image.
                // Then, convert the color to be one adjusted for alpha, where the corresponding 'more opaque' color and alpha value that would be the average alpha value would be obtained
                /* E.g. (255,0,0,0), (0,0,0,0), (0,0,0,255), (0,0,0,255) would be calculated to be first ((255+255+0+0), (255+255+0+0), (255+255+0+0), (255+255+255+255)),
                then we would get the average alpha to be (0+0+255+255)/4 = 127.5,
                then we would get the corresponding 'more opaque' color to be where the distance from white (i.e. RGB of (255,255,255)) would be divided by (127.5/255),
                so RDistanceFromWhite_ForMO would be 255 - (255+255+0+0)/4, and this would be divided by (127.5/255) then the new R_MO color would be 255 - RDistanceFromWhite_ForMO after said division
                and the same would go for G and B

                ABOUT THIS:
                (where in rendering the image, alpha would basically adjust the distance from white, being a multiplier for the distance from white, and so it would multiply back the distance from white by (127.5/255))

                ...where this would mean that the alpha-adjusted corresponding 'more opaque' colors that are averaged together would all have the same alpha value, which would be the average alpha value ... regardless of the original colors' alpha values
                well, this would be like the alpha-adjusted corresponding colors all having an alpha value of 100%
                where I guess this would mean that these colors (that would have alphas) going over other colors would no longer have the distribution of alpha transparencies when going over other colors and instead would have a single, shared (that would be equal) average alpha transparency.
                This is a loss that might not be that big...but to preserve the alphas, it would be ideal to not convert colors like this
                and instead do minify pixel resolution after drawing overlays where the range of pixels (- IOW, the size of the array of pixels -) for an image would be twice the minified image's width and height
                where all overlays of each pixel would be done first before minify pixel resolution (that would be merging the pixels into an average)
                Not T*DO anymore: complete clarification of the ideal if it's not completely clarified where I can note whether or not this would result in a different average depending on whether minify pixel resolution would be done after drawing overlays (which would take the distribution into account) or before drawing overlays (which wouldn't take the distribution into account)
                where I can try an example ...
                which could get really complicated ...
                but what I'm doing would also result in rather complicated programming of overlays ... for this method, and for drawItem ... but where I implemented drawItem ... and just have to test it now

                and from testing, it looks like minify didn't have much issue with overlapping colors.
                *
                * */
                // Or, calculate the sum of the R/G/B values and the average A value for the corresponding 2x2 block of pixels in the original image. This is where this would be like all of the colors would be added together unweighted and share the same alpha.
                int sumR = 0;
                int sumG = 0;
                int sumB = 0;
                int sumA = 0;
                for (int ii = 0; ii < 2; ii++) {
                    for (int jj = 0; jj < 2; jj++) {
                        // Get weighted (by alpha value) sum:

                        // 1) Get difference from white
                        // 2) Multiply the difference by the alpha value

                        // 3) Re-apply the difference to the color values
                        // Convert 255,255,255,255 colors to 0,0,0,0 colors
                        if (Arrays.equals(img[2*i+ii][2*j+jj], new int[] {255, 255, 255, 255})) {
                            int R = 255; // When adjusted by the alpha value, difference from 255 is 0
                            sumR += R;
                            sumG += 255;
                            sumB += 255;
                            sumA += 0;
                        } else {
                            int R = img[2*i+ii][2*j+jj][0];
                            int A = img[2*i+ii][2*j+jj][3];

                            // 1) Get difference from white
                            int RDifferenceFromWhite = R - 255;
                            // 2) Multiply the difference by the alpha value
                            RDifferenceFromWhite *= A;
                            // 3) Re-apply the difference to the color values
                            int RWeightedByAlpha = 255 + RDifferenceFromWhite;

                            //sumR += img[2*i+ii][2*j+jj][0];
                            sumR += RWeightedByAlpha;
                            // Done: Do rest

                            // Do steps 1-3 for GWeightedByAlpha
                            int GWeightedByAlpha = 255 + ((img[2*i+ii][2*j+jj][1] - 255) * A);
                            sumG += GWeightedByAlpha;

                            // Do steps 1-3 for BWeightedByAlpha
                            int BWeightedByAlpha = 255 + ((img[2*i+ii][2*j+jj][2] - 255) * A);
                            sumB += BWeightedByAlpha;

                            // Add the alpha to sumA
                            sumA += img[2*i+ii][2*j+jj][3];
                        }
                        /*
                        sumR += img[i * 2 + ii][j * 2 + jj][0];
                        sumG += img[i * 2 + ii][j * 2 + jj][1];
                        sumB += img[i * 2 + ii][j * 2 + jj][2];
                        */
                    }
                }

                // Set the R/G/B values of the current pixel in the new image to the average of the corresponding 2x2 block of pixels in the original image.
                //newImg[i][j][0] = sumR / 4;
                //newImg[i][j][1] = sumG / 4;
                //newImg[i][j][2] = sumB / 4;

                // Get "step 2" average colors
                int averageR = sumR / 4;
                int averageG = sumG / 4;
                int averageB = sumB / 4;

                // Get average alpha
                int averageA = sumA / 4;

                // Get corresponding 'more opaque' color
                // we would get the corresponding 'more opaque' color to be where the distance from white (i.e. RGB of (255,255,255)) would be divided by (127.5/255),
                // so RDistanceFromWhite_ForMO would be 255 - (255+255+0+0)/4, and this would be divided by (127.5/255) then the new R_MO color would be 255 - RDistanceFromWhite_ForMO after said division
                // and the same would go for G and B

                int RDistanceFromWhite_ForMO = 255 - averageR;

                // Divide the distance from white by averageA
                RDistanceFromWhite_ForMO = divide_cap255(RDistanceFromWhite_ForMO, (averageA / 255));
                int R_MO = 255 - RDistanceFromWhite_ForMO;

                // The steps in getting the corresponding 'more opaque' color, all in one
                int G_MO = 255 - divide_cap255((255 - averageG), (averageA / 255));
                int B_MO = 255 - divide_cap255((255 - averageB), (averageA / 255));

                // update newImg's pixel at row=i, col=j with the corresponding 'more opaque' colors
                newImg[i][j][0] = R_MO;
                newImg[i][j][1] = G_MO;
                newImg[i][j][2] = B_MO;
                newImg[i][j][3] = averageA;
            }
        }
        return newImg;
    }

    public static int divide_cap255(int dividend, int divisor) {
        if(divisor == 0) {
            return 255;
        }
        else {
            return Math.min(dividend/divisor, 255);
        }
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

    // Now takes into account alpha
    public static void drawItem_whiteBG(int[][][] canvas, int[][][] item, int row, int col) {
        // Iterate through each pixel in the item image.
        for (int i = 0; i < item.length; i++) {
            for (int j = 0; j < item[0].length; j++) {
                // If the current pixel in the item image is not white, draw it on the corresponding pixel in the canvas image.
                if (!Arrays.equals(item[i][j], new int[] {255, 255, 255, 255}) || item[i][j][3] == 0) {

                    // Overlay over the current color in the canvas image that has alpha if any
                    /*
                    Short answer:
                    if we want to overlay c0 over c1 both with some alpha then
                    a01 = (1 - a0)·a1 + a0
                    r01 = ((1 - a0)·a1·r1 + a0·r0) / a01
                    g01 = ((1 - a0)·a1·g1 + a0·g0) / a01
                    b01 = ((1 - a0)·a1·b1 + a0·b0) / a01

                    e.g.
                     */

                    // a01 = (1 - a0)·a1 + a0
                    double a_0 = item[i][j][3] / 255.0;
                    double a_1 = canvas[row+i][col+j][3] / 255.0;
                    double a_01 = ((1 - a_0) * a_1 + a_0); // looks like what I want
                    int drawnAlpha = (int)(a_01 * 255.0);

                    // r01 = ((1 - a0)·a1·r1 + a0·r0) / a01
                    int r_0 = item[i][j][0];
                    int r_1 = canvas[row+i][col+j][0];
                    int r_01 = (int)(((1 - a_0) *a_1*r_1 + a_0*r_0) / a_01);
                    // r0 over (1 over 2) = (1 - a0)·((1 - a1)·r2 + a1·r1) + a0·r0
                    // r01white = (1 - a0)*((1 - a1)*255 + a1*r1) + a0 * r0
                    int r_01white = (int)((1 - a_0) * ((1 - a_1)*255 + a_1 * r_1) + a_0 * r_0);
                    int drawnRed = r_01;

                    // g01 = ((1 - a0)·a1·g1 + a0·g0) / a01
                    int g_0 = item[i][j][1];
                    int g_1 = canvas[row+i][col+j][1];
                    int g_01 = (int)(((1 - a_0)*a_1*g_1 + a_0*g_0) / a_01);
                    int g_01white = (int)((1 - a_0) * ((1 - a_1)*255 + a_1 * g_1) + a_0 * g_0);
                    int drawnGreen = g_01;

                    // b01 = ((1 - a0)·a1·b1 + a0·b0) / a01
                    int b_0 = item[i][j][2];
                    int b_1 = canvas[row+i][col+j][2];
                    int b_01 = (int)(((1 - a_0)*a_1*b_1 + a_0*b_0) / a_01);
                    int b_01white = (int)((1 - a_0) * ((1 - a_1)*255 + a_1 * b_1) + a_0 * b_0);
                    int drawnBlue = b_01;
                    canvas[row + i][col + j] = new int[]{drawnRed, drawnGreen, drawnBlue, drawnAlpha};
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
            drawItem_whiteBG(canvas, item, row, col);
        }
    }
}
