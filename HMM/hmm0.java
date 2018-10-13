//package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class hmm0 {
    public static void main(String[] args) throws IOException
    {

        Scanner scr = new Scanner(System.in);
        String A = scr.nextLine();
        String B = scr.nextLine();
        String pi = scr.nextLine();

        // Initialise A = state transition matrix
        //            B = observation/ emission matrix
        //            pi = initial state probability
        // given the standard input
        float [][] Am = initialize_matrix(A);
        float [][] Bm = initialize_matrix(B);
        float [][] pim = initialize_matrix(pi);

        // Multiply pi*A
        float [][] result = mult(pim,Am,false);

        // Multiply (pi*A)*B to
        // the observation probability distribution
        float [][] result2 = mult(result,Bm,true);

    }

    public static float [][] initialize_matrix(String A)
    {
        String[] tokens = A.split(" ");
        int n = Integer.valueOf(tokens[0]);
        int m = Integer.valueOf(tokens[1]);
        float [][] A_matrix = new float[n][m];
        int k = 2;
        for(int row = 0; row < A_matrix.length; row++) {

            for(int column = 0; column < A_matrix[row].length; column++) {
                A_matrix[row][column] = Float.valueOf(tokens[k]);
                k = k + 1;
            }
        }
        return A_matrix;
    }

    public static void print_matrix(float [][] A)
    {
        for(int row = 0; row < A.length; row++)
        {
            for(int column = 0; column < A[row].length; column++)
            {
                System.out.print(A[row][column] + " ");
            }
            System.out.println();
        }
    }


    public static float[][] mult(float [][] testMatrix1, float [][] testMatrix2, boolean print )
    {
        int i, j, k, l, m ,n;

        // Look at the length of the matrix
        int raderMatris1 = testMatrix1.length;
        int kolumnMatris1 = testMatrix1[0].length;
        int kolumnMatris2 = testMatrix2[0].length;

        // Create a new matrix for the result of the multiplication
        float [][] nyMatrix = new float[raderMatris1][kolumnMatris2];

        // This loop computes the multiplication between
        // testMatrix1 and testMatrix2
        for (i=0; i < raderMatris1; i++)
        {
            //Loop over the rows of matrix 1
            for (j= 0;j < kolumnMatris2; j++)
            {
                //Loop over the colums of matrix 2

                for (k = 0; k < kolumnMatris1; k++)
                {
                    // Perform addition and multiplication
                    // on the rows and colums
                    nyMatrix[i][j] += testMatrix1[i][k] * testMatrix2[k][j];
                }
            }
        }

        //System.out.println("New matrix: ");
        if(print)
        {
            // Print out the result if needed
            System.out.print(nyMatrix.length + " " + nyMatrix[0].length + " ");
            for(m=0; m < nyMatrix.length; m++)
            {
                for(n=0; n < nyMatrix[0].length; n++)
                {
                    System.out.print(nyMatrix[m][n] + " ");
                }
                System.out.println();
            }
        }
        return nyMatrix;
    }

}
