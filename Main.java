package com.company;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException
    {
	// write your code here
/*        String NEW_LINE_SEPARATOR = "\n";
        Scanner sc = new Scanner(System.in);
        int antal_horn = Integer.valueOf(sc.nextLine());
        int antal_kanter = Integer.valueOf(sc.nextLine());
        int antal_farg = Integer.valueOf(sc.nextLine());*/

        /* Create a file with the name "hmm0.txt" */
        // FileOutputStream output = new FileOutputStream("hmm0.txt");
        File text = new File("hmm0.txt");
        Scanner scr = new Scanner(text);
        String A = scr.nextLine();
        String B = scr.nextLine();
        String pi = scr.nextLine();
        System.out.println("HMM A : " + A );
        System.out.println("HMM B : " + B );
        System.out.println("HMM pi : " + pi );

        float [][] Am = initialize_matrix(A);
        print_matrix(Am);

        float [][] Bm = initialize_matrix(B);
        print_matrix(Bm);

        float [][] pim = initialize_matrix(pi);
        print_matrix(pim);

        //
        System.out.println("Result of pi*A : ");
        float [][] result = mult(pim,Am);
        System.out.println("Result of pi*A*B  : ");
        float [][] result2 = mult(result,Bm);
        //mult()

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
//            k = k + 1;
        }
        return A_matrix;
    }

    public static void print_matrix(float [][] A)
    {
        System.out.println("MATRIX : ");
        for(int row = 0; row < A.length; row++)
        {
            for(int column = 0; column < A[row].length; column++)
            {
                System.out.print(A[row][column] + " ");
            }
            System.out.println();
        }
    }


    public static float[][] mult(float [][] testMatrix1, float [][] testMatrix2 )
    {
        int i, j, k, l, m ,n;
/*        float [][] testMatrix1 = {{0.5f ,0.5f} //första raden
        }; //andra raden

        float [][] testMatrix2 = {{0.5f, 0.5f},
                {0.5f, 0.5f}};*/

        int raderMatris1 = testMatrix1.length;
        int kolumnMatris1 = testMatrix1[0].length; //kollar längden på raden
        int kolumnMatris2 = testMatrix2[0].length;

        //skapa den nya matrisen
        float [][] nyMatrix = new float[raderMatris1][kolumnMatris2];

        //loopen som gör multiplikationen samt additionerna
        for (i=0; i < raderMatris1; i++) { //går igenom raderna i matris1

            for (j= 0;j < kolumnMatris2; j++ ){ //går igenom kolumnerna i matris2

                for (k = 0; k < kolumnMatris1; k++){ //utför addition och multiplikation på rad&kolumn
                    nyMatrix[i][j] += testMatrix1[i][k] * testMatrix2[k][j];
                }
            }
        }

        System.out.println("New matrix: ");
        System.out.println(nyMatrix.length + " " + nyMatrix[0].length);
        for(m=0; m < nyMatrix.length; m++) {
            for(n=0; n < nyMatrix[0].length; n++){
                System.out.print(nyMatrix[m][n] + " ");
            } System.out.println();
        }
        return nyMatrix;
    }


}
