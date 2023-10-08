package com.company.testAS;

import com.company.matrix.IDoubleMatrix;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static com.company.testAS.TestMatrixData.TEST_PRECISION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatrixBinaryOperationTest {
    static void assertArrayEqualsPrecision(double[][] a, double[][] b, double precision) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                assertEquals(a[i][j], b[i][j], precision, "Różnica na indeksach [" + i + "][" + j + "]. Oczekiwano " + a[i][j] + " a otrzymano " + b[i][j]);
            }
        }
    }


    // Testy sprawdzające poprawność zoptymalizowanego mnożenia.
    // Porównują wynik z mnożeniem brutalnym (zakładamy jego poprawność).
    @ParameterizedTest
    @ArgumentsSource(TestMatrixArgumentProvider.class)
    void TestMultiplication(IDoubleMatrix l, IDoubleMatrix r) {
        if (l.shape().columns != r.shape().rows) {
            // Jeśli macierze są złych wymiarów oczekujemy asercji.
            assertThrows(AssertionError.class, () -> l.times(r));
        } else {
            double[][] standard = l.standardMultiply(r).data();
            double[][] optimised = l.times(r).data();

            assertArrayEqualsPrecision(standard, optimised, TEST_PRECISION);
        }
    }

    // Testy sprawdzające poprawność zoptymalizowanego dodawania.
    // Porównują wynik z dodawaniem brutalnym (zakładamy jego poprawność).
    @ParameterizedTest
    @ArgumentsSource(TestMatrixArgumentProvider.class)
    void TestAddition(IDoubleMatrix l, IDoubleMatrix r) {
        if (!l.shape().equals(r.shape())) {
            // Jeśli macierze są złych wymiarów oczekujemy asercji.
            assertThrows(AssertionError.class, () -> l.plus(r));
        } else {
            double[][] standard = l.standardAdd(r).data();
            double[][] optimised = l.plus(r).data();

            assertArrayEqualsPrecision(standard, optimised, TEST_PRECISION);
        }
    }
}
