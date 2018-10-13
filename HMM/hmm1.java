package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class hmm1 {
    public static void main(String[] args) throws IOException
    {

        Scanner scr = new Scanner(System.in);
        //File text = new File("hmm1.txt");
        //Scanner scr = new Scanner(text);
        String A = scr.nextLine();
        String B = scr.nextLine();
        String pi = scr.nextLine();
        String ob = scr.nextLine();
        // Initialise A = state transition matrix
        //            B = observation/ emission matrix
        //            pi = initial state probability
        // given the standard input
        float [][] Am = initialize_matrix(A);
        float [][] Bm = initialize_matrix(B);
        float [][] pim = initialize_matrix(pi);
        int [] obm = initialize_observationvector(ob);

        int ob_i = obm[0];
        float [] bi = getCol(ob_i, Bm);
        float [] alpha_i = element_wise_product(pim[0], bi);
        //System.out.println(" ");
        //System.out.println(" Alpha(1) :");
        //print_vec(alpha_i);
        float sum = 0;
        //sum = sum + vec_sum(alpha_i);

        for(int k = 1; k < obm.length; k++)
        {
            float [] s = new float[Am.length];
            for(int j = 0; j < Am.length; j++)
            {
                s[j] = dot_product(alpha_i, getCol(j,Am));
            }
            int ind = obm[k];
            float [] o = getCol(ind, Bm);
            alpha_i = element_wise_product(s, o);
//            System.out.println(" ");
//            System.out.println(" Alpha" + "(" + (k + 1)  + ") :");
//            print_vec(alpha_i);
        }
        sum = sum + vec_sum(alpha_i);
//        System.out.println("The probability of the observation : " +  sum);
        System.out.println(sum);
    }

    public static void print_vec(float [] v)
    {
        for(int i = 0; i < v.length; i++)
        {
            System.out.print(v[i] + " ");
        }
        System.out.println(" ");
    }

    public static int [] initialize_observationvector(String A)
    {
        String[] tokens = A.split(" ");
        int n = Integer.valueOf(tokens[0]);
        int [] A_matrix = new int[n];
        int k = 1;
        for(int row = 0; row < A_matrix.length; row++)
        {
                A_matrix[row] = Integer.valueOf(tokens[k]);
                k = k + 1;
        }
        return A_matrix;
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

    public static float [] element_wise_product(float [] a, float [] b )
    {
        int n = a.length;
        int m  = b.length;
        float [] prod = new float[n];
        if( n == m )
        {
            for(int j = 0; j < n; j++)
            {
                prod[j] = a[j]*b[j];
            }
        }
        else
        {
            System.out.println("ERROR : Element wise product of two vectors of different lengths ");
        }
        return prod;
    }

    public static float [] getCol(int index ,float [][] a )
    {
        float [] col = new float[a.length];

        for(int i = 0; i < a.length; i++)
        {
            col[i] = a[i][index];
        }
        return col;
    }

    public static float vec_sum(float [] arr)
    {
        float sum = 0;
        for(int i = 0; i < arr.length; i++)
        {
            sum = sum + arr[i];
        }
        return sum;
    }

    public static float dot_product(float [] a, float [] b )
    {
        float [] arr = element_wise_product(a, b );
        float sum = vec_sum(arr);
        return sum;
    }


}
