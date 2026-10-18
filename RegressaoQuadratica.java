import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RegressaoQuadratica {

    public static void main(String[] args) {
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();

        // PASSO 1: Leitura direta do arquivo dados.csv
        try (BufferedReader br = new BufferedReader(new FileReader("dados.csv"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue;
                }
                String[] partes = linha.split(",");
                if (partes.length >= 2) {
                    xList.add(Double.parseDouble(partes[0].trim()));
                    yList.add(Double.parseDouble(partes[1].trim()));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo dados.csv: " + e.getMessage());
            return;
        }

        int n = xList.size();
        if (n < 4) {
            System.err.println("É necessário fornecer no mínimo 4 pontos.");
            return;
        }

        // PASSO 2: Somatórios
        double sumX = 0, sumX2 = 0, sumX3 = 0, sumX4 = 0;
        double sumY = 0, sumXY = 0, sumX2Y = 0;

        for (int i = 0; i < n; i++) {
            double x = xList.get(i);
            double y = yList.get(i);
            double x2 = x * x;

            sumX += x;
            sumX2 += x2;
            sumX3 += x2 * x;
            sumX4 += x2 * x2;

            sumY += y;
            sumXY += x * y;
            sumX2Y += x2 * y;
        }

        // PASSO 3: Matriz M e vetor B
        double[][] M = {
            {sumX4, sumX3, sumX2},
            {sumX3, sumX2, sumX},
            {sumX2, sumX, (double) n}
        };

        double[] B = {sumX2Y, sumXY, sumY};

        // PASSO 4: Regra de Cramer
        double detM = calcularDeterminante3x3(M);

        if (Math.abs(detM) < 1e-12) {
            System.err.println("O sistema não possui solução única (determinante nulo).");
            return;
        }

        double[][] M2 = {
            {B[0], M[0][1], M[0][2]},
            {B[1], M[1][1], M[1][2]},
            {B[2], M[2][1], M[2][2]}
        };

        double[][] M1 = {
            {M[0][0], B[0], M[0][2]},
            {M[1][0], B[1], M[1][2]},
            {M[2][0], B[2], M[2][2]}
        };

        double[][] M0 = {
            {M[0][0], M[0][1], B[0]},
            {M[1][0], M[1][1], B[1]},
            {M[2][0], M[2][1], B[2]}
        };

        double alpha2 = calcularDeterminante3x3(M2) / detM;
        double alpha1 = calcularDeterminante3x3(M1) / detM;
        double alpha0 = calcularDeterminante3x3(M0) / detM;

        // PASSO 5: Saída dos coeficientes
        System.out.printf("%.6f%n", alpha2);
        System.out.printf("%.6f%n", alpha1);
        System.out.printf("%.6f%n", alpha0);
    }

    private static double calcularDeterminante3x3(double[][] m) {
        return m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
             - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
             + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
    }
}