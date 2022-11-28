import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Matrix newM = new Matrix(5, 4);
        newM.printmatrix();
        newM.deg();
        newM.deleteEdge(3, 2);
        newM.addEdge(1, 5);
        newM.printmatrix();
    }
}