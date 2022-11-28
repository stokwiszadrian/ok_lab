package matrix;

import java.lang.reflect.Array;
import java.util.*;

public class Matrix {
    public int[][] matrix;
    public boolean directed;

    public Matrix(int n, int v) {
        this.matrix = new int[n][n];
        Scanner scanner = new Scanner(System.in);
        System.out.println("Directed? (y/n)");
        String directed = scanner.next();
        if (Objects.equals(directed, "y")) {
            this.directed = true;
            for (int i = 1; i <= v; i++) {
                System.out.println(i+")");
                String x = scanner.next();
                String y = scanner.next();
                this.matrix[Integer.parseInt(x) - 1][Integer.parseInt(y) - 1] = 1;
            }
        } else if (Objects.equals(directed, "n")) {
            for (int i = 0; i < v; i++) {
                System.out.println(i+")");
                String x = scanner.next();
                String y = scanner.next();
                this.matrix[Integer.parseInt(x) - 1][Integer.parseInt(y) - 1] = 1;
                this.matrix[Integer.parseInt(y) - 1][Integer.parseInt(x) - 1] = 1;
            }
        }
    }

    public Matrix(int[][] m, boolean d) {
        this.matrix = m;
        this.directed = d;
    }
    public void printmatrix() {
        for (int[] i : matrix) {
            System.out.println(Arrays.toString(i));
        }
    }

    public void addEdge(int x, int y) {
        if (this.directed) {
            this.matrix[x-1][y-1] = 1;
        }
        else {
            this.matrix[x-1][y-1] = 1;
            this.matrix[y-1][x-1] = 1;
        }
    }

    public void deleteEdge(int x, int y) {
        if (this.directed) {
            this.matrix[x-1][y-1] = 0;
        }
        else {
            this.matrix[x-1][y-1] = 0;
            this.matrix[y-1][x-1] = 0;
        }
    }

    public void deg() {
        if (this.directed) {
            int[] in = new int[matrix.length];
            int[] out = new int[matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    out[i] += matrix[i][j];
                    in[i] += matrix[j][i];
                }
            }
            for (int i = 0; i < in.length; i++) {
                System.out.println("degin("+(i+1)+") = "+in[i]);
                System.out.println("degout("+(i+1)+") = "+out[i]);
            }
        }
        else {
            for (int i = 0; i < matrix.length; i++) {
                int sum = 0;
                for (int j = 0; j < matrix.length; j++) {
                    sum += matrix[i][j];
                }
                System.out.println("deg("+(i+1)+") = "+sum);
            }
        }
    }


    public Matrix multiply(int[][] m) {
        int l = matrix.length;
        int[][] temp = new int[l][l];
        for(int k = 0; k < l; k++) {
            for (int i = 0; i < l; i++) {
                int sum = 0;
                for (int j = 0; j < l; j++) {
                    sum += this.matrix[k][j] * m[i][j];
                }
                temp[k][i] = sum;
            }
        }
        Matrix newM = new Matrix(temp, false);
        return newM;
    }

    private void visit(int[][] t, ArrayList<Integer> treelist, int v) {
        for (int i = 0; i < this.matrix.length; i++) {
            if( this.matrix[v][i] == 1 && !treelist.contains(i) ) {
                t[v][i] = 1;
                t[i][v] = 1;
                treelist.add(i);
                visit(t, treelist, i);
            }
        }
    }

    public Matrix DFS(){
        int l = this.matrix.length;
        int[][] t = new int[l][l];
        ArrayList<Integer> treelist = new ArrayList<>();
        for (int i = 0; i < this.matrix.length; i++) {
            if (this.matrix[0][i] == 1) {
                visit(t, treelist, i);
                return new Matrix(t, false);
            }
        }
        return new Matrix(t, false);
    }

}