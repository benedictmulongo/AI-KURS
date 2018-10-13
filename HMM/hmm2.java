package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Collections;
import java.util.*;

public class hmm2
{
    public static void main(String[] args) throws IOException
    {

        Scanner scr = new Scanner(System.in);
        //File text = new File("hmm2.txt");
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

        /*
        This initialize the first delta
        delta(1) = element_wise_product(pi, first observation)
         */
        int ob_i = obm[0];
        float [] bi = getCol(ob_i, Bm);
        float [] delta_i = element_wise_product(pim[0], bi);
        int [] p = new int[Am.length];
        for(int i = 0; i < p.length; i++)
        {
            p[i] = 0;
        }
        ArrayList<int[]> phi =new ArrayList<int[]>();
        phi.add(p);

        for(int k = 1; k < obm.length; k++)
        {
            float [] s = new float[Am.length];
            int [] s_phi = new int[Am.length];
            for(int j = 0; j < Am.length; j++)
            {
                // Get the max element in each product
                float [] temp = element_wise_product(delta_i, getCol(j,Am));
                s[j] = getmax(temp);
                // the index of the maximal element
                s_phi[j] = argMax(temp);
            }
            int ind = obm[k];
            float [] o = getCol(ind, Bm);
            // compute the delta(i + 1)
            delta_i = element_wise_product(s, o);
            // Add all phi computed so for in order
            // to make it easier to retract and find the
            // optimal path P given the observation obm
            phi.add(s_phi);

        }

        /*
        This loop find the optimal path after the matrix delta
        and phi have been computed
         */
        int q_star = argMax(delta_i);
        int t = obm.length;
        ArrayList<Integer> k = new ArrayList<Integer>();
        k.add(q_star);
        while(t > 1)
        {
            t = t - 1 ;
            q_star = phi.get(t)[q_star];
            k.add(q_star);
        }

        StringBuilder st = new StringBuilder();

        for(int j = 0; j < k.size(); j++)
        {
            st.append(" " + k.get(j));
        }

        System.out.println(st.reverse());
    }

    public static int argMax(float [] num)
    {
        float max = num[0];
        int ind = 0;
        for(int j = 0; j < num.length; j++)
        {
            if (num[j] > max) {
                max = num[j];
                ind = j ;
            }
        }

        return ind;
    }

    public static float getmax(float [] num)
    {
        float max = num[0];
        for(int j = 0; j < num.length; j++)
        {
            if (num[j] > max)
                max = num[j];
        }

        return max;
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
