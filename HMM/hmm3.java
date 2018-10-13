//package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.*;
import java.util.Collections;
import java.util.*;

public class hmm3
{
    public static void main(String[] args) throws IOException
    {

        Scanner scr = new Scanner(System.in);
 //       File text = new File("hmm3.txt");
//        Scanner scr = new Scanner(text);
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

        int N = Am.length; // number of states
        int T = obm.length; // number of time steps
        int M = Bm[0].length; // number of possible observations

        float[][] alpha = new float[T][N];
        float[][] beta = new float[T][N];

        float[][][] digamma = new float[T][N][N];
        float[][] gamma = new float[T][N];

        float[] scale_values = new float[T];

        int minIters = 150;
        double oldLogProb = Double.NEGATIVE_INFINITY;
        int iters = 0;
        double logProb = 0;

        // repeat until convergence
        while(true){

            // fill up alpha matrix

            alpha = forward_algorithm(N,T,Am,Bm,obm,pim[0],scale_values);

            // fill up beta matrix
            beta = backward_algorithm(N,T,Am,Bm,obm,pim[0],scale_values);

            // calculate di-gamma and gamma
            gammaDiGamma(digamma, gamma, Am, Bm, obm, alpha, beta,N,T);

            // re-estimate A, B and pi
            pim[0] = gamma[0];
            Am = reestimate_A(N,T,digamma,gamma);
            Bm = reestimateB(N,T,M,obm,gamma);

            // compute probability of emissions given new model
            logProb = 0;
            double prob = 1;

            for(int t = 0; t < T; t++){
                logProb += Math.log(scale_values[t]);
                prob *= scale_values[t];
            }

            logProb = -logProb;
            iters++;



            if(iters < minIters && logProb > oldLogProb){
                oldLogProb = logProb;
                continue;
            } else {
                break;
            }
        }

        //print_matrix(Am);
        System.out.println(print_mat(Am));
        //print_matrix(Bm);
        System.out.println(print_mat(Bm));

    }

    public static float [][] forward_algorithm(int N, int T, float[][] A, float[][] B, int[] em, float[] pi, float[] scale_values)
    {
        // initialize the Alpha matrix
        float[][] alpha = new float[T][N];
        //Compute alpha0(i)
        float c0 = 0;
        for(int i = 0; i < N; i++){
            alpha[0][i] = pi[i] * B[i][em[0]];
            c0 += alpha[0][i];
        }
        // scale alpha_0(i)
        c0 = 1/c0;
        scale_values[0] = c0;

        for(int i = 0; i < N; i++)
            alpha[0][i] = c0 * alpha[0][i];

        // compute alpha_t(i)
        float ct;
        for(int t = 1; t < T; t++){
            ct = 0;
            for(int i = 0; i < N; i++){
                alpha[t][i] = 0;
                for(int j = 0; j < N; j++){
                    alpha[t][i] += alpha[t-1][j] * A[j][i];
                }
                alpha[t][i] = alpha[t][i] * B[i][em[t]];
                ct = ct + alpha[t][i];
            }

            // scale alpha_t(i)
            ct = 1/ct;
            scale_values[t] = ct;
            for(int i = 0; i < N; i++)
                alpha[t][i] = ct * alpha[t][i];
        }

        return alpha;
    }

    public static float [][] backward_algorithm(int N, int T, float[][] A, float[][] B, int[] em, float[] pi, float[] scale_values)
    {
        // initialize the Alpha matrix
        float[][] beta = new float[T][N];

        // compute beta_0(i)
        for(int i = 0; i < N; i++)
            beta[T-1][i] = scale_values[T-1];

        // compute beta_t(i)
        for(int t = T-2; t > 0; t--){
            for(int i = 0; i < N; i++){
                beta[t][i] = 0;
                for(int j = 0; j < N; j++){
                    beta[t][i] += A[i][j] * B[j][em[t+1]] * beta[t+1][j];
                }
                // scale beta_t(i) with the same factor as alpha_t(i)
                beta[t][i] = beta[t][i] * scale_values[t];
            }
        }

        return beta;
    }

    private static void gammaDiGamma(float[][][] digamma, float[][] gamma, float[][] A, float[][] B, int[] em, float[][] alpha, float[][] beta,int N, int T){

        float denom, numer;
        for(int t = 0; t < T-1; t++){
            denom = 0;
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    denom = denom + alpha[t][i] * A[i][j] * B[j][em[t+1]] * beta[t+1][j];
                }
            }
            for(int i = 0; i < N; i++){
                gamma[t][i] = 0;
                for(int j = 0; j < N; j++){
                    digamma[t][i][j] = (alpha[t][i] * A[i][j] * B[j][em[t+1]] * beta[t+1][j]) / denom;
                    gamma[t][i] += digamma[t][i][j];
                }
            }
        }

        denom = 0;
        for(int i = 0; i < N; i++)
            denom = denom + alpha[T-1][i];

        for(int i = 0; i < N; i++)
            gamma[T-1][i] = alpha[T-1][i] / denom;

    }

    private static float[][] reestimate_A(int N, int T, float[][][] digamma, float[][] gamma)
    {
        float[][] A = new float[N][N];
        float numer, denom;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                numer = 0;
                denom = 0;
                for(int t = 0; t < T - 1; t++){
                    numer += digamma[t][i][j];
                    denom += gamma[t][i];
                }
                A[i][j] = numer / denom;
            }
        }
        return A;

    }

    private static float[][] reestimateB(int N, int T, int M, int[] em, float[][] gamma)
    {
        float denom, numer;
        float[][] B = new float[N][M];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                numer = 0;
                denom = 0;
                for(int t = 0; t < T; t++){
                    if(em[t] == j) numer += gamma[t][i];
                    denom += gamma[t][i];
                }
                B[i][j] = numer/denom;
            }
        }
        return B;

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

    public static String print_mat(float [][] A)
    {
        StringBuilder st = new StringBuilder();
        st.append(A.length);
        st.append(" ");
        st.append(A[0].length);

        for(int row = 0; row < A.length; row++)
        {
            for(int column = 0; column < A[row].length; column++)
            {
                st.append(" " + A[row][column]);

            }
        }
        return st.toString();
    }


}
