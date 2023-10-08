package com.company.test;

import com.company.matrix.IDoubleMatrix;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

import static com.company.utils.DoubleMatrixFactory.*;
import static com.company.utils.MatrixCellValue.cell;
import static com.company.utils.Shape.matrix;
import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatrixNormTest {

    private static final double FROBENIUS_NORM = sqrt(IntStream.range(1, 7).mapToDouble(it -> it * it).sum());

    private static void testMatrixNorm(IDoubleMatrix m, Function<IDoubleMatrix, Double> matrixNorm, double expectedNorm) {
        final var norm = matrixNorm.apply(m);
        assertEquals(expectedNorm, norm);
    }

    @Nested
    public class FrobeniusNormTest {

        @Test
        void testFullMatrices() {
            testMatrixNorm(
                    full(new double[][]{
                            new double[]{1, -2},
                            new double[]{3, -4},
                            new double[]{5, -6},
                    }),
                    IDoubleMatrix::frobeniusNorm,
                    FROBENIUS_NORM
            );
        }

        @Test
        void testSparseMatrices() {
            testMatrixNorm(
                    sparse(
                            matrix(1_000_000, 1_000_000),
                            cell(0, 999_999, 1),
                            cell(23, 1, -2),
                            cell(424_242, 242_242, 3),
                            cell(123, 789, -4),
                            cell(456, 101, 5),
                            cell(999_999, 111, -6)
                    ),
                    IDoubleMatrix::frobeniusNorm,
                    FROBENIUS_NORM
            );
        }

        @Test
        void testVector() {
            testMatrixNorm(
                    vector(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::frobeniusNorm,
                    FROBENIUS_NORM
            );
        }

        @Test
        void testDiagonal() {
            testMatrixNorm(
                    diagonal(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::frobeniusNorm,
                    FROBENIUS_NORM
            );
        }

        @Test
        void testAntiDiagonal() {
            testMatrixNorm(
                    antiDiagonal(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::frobeniusNorm,
                    FROBENIUS_NORM
            );
        }
    }

    @Nested
    public class NormOneTest {

        @Test
        void testFullMatrices() {
            testMatrixNorm(
                    full(new double[][]{
                            new double[]{1, -2},
                            new double[]{3, -4},
                            new double[]{5, -6},
                    }),
                    IDoubleMatrix::normOne,
                    2 + 4 + 6
            );
        }

        @Test
        void testSparseMatrices() {
            testMatrixNorm(
                    sparse(
                            matrix(1_000_000, 1_000_000),
                            cell(0, 999_999, 1),
                            cell(23, 1, -2),
                            cell(424_242, 242_242, 3),
                            cell(123, 789, -4),
                            cell(456, 101, 5),
                            cell(999_999, 111, -6)
                    ),
                    IDoubleMatrix::normOne,
                    6
            );
        }

        @Test
        void testVector() {
            testMatrixNorm(
                    vector(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::normOne,
                    1 + 2 + 3 + 4 + 5 + 6
            );
        }

        @Test
        void testDiagonal() {
            testMatrixNorm(
                    diagonal(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::normOne,
                    6
            );
        }

        @Test
        void testAntiDiagonal() {
            testMatrixNorm(
                    antiDiagonal(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::normOne,
                    6
            );
        }
    }

    @Nested
    public class NormInfinityTest {

        @Test
        void testFullMatrices() {
            testMatrixNorm(
                    full(new double[][]{
                            new double[]{1, -2},
                            new double[]{3, -4},
                            new double[]{5, -6},
                    }),
                    IDoubleMatrix::normInfinity,
                    5 + 6
            );
        }

        @Test
        void testSparseMatrices() {
            testMatrixNorm(
                    sparse(
                            matrix(1_000_000, 1_000_000),
                            cell(0, 999_999, 1),
                            cell(23, 1, -2),
                            cell(424_242, 242_242, 3),
                            cell(123, 789, -4),
                            cell(456, 101, 5),
                            cell(999_999, 111, -6)
                    ),
                    IDoubleMatrix::normInfinity,
                    6
            );
        }

        @Test
        void testVector() {
            testMatrixNorm(
                    vector(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::normInfinity,
                    6
            );
        }

        @Test
        void testDiagonal() {
            testMatrixNorm(
                    diagonal(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::normInfinity,
                    6
            );
        }

        @Test
        void testAntiDiagonal() {
            testMatrixNorm(
                    antiDiagonal(1, -2, 3, -4, 5, -6),
                    IDoubleMatrix::normInfinity,
                    6
            );
        }
    }
}
