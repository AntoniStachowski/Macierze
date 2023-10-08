package com.company.testAS;

import com.company.matrix.IDoubleMatrix;
import com.company.utils.MatrixCellValue;
import com.company.utils.Shape;

import static com.company.utils.DoubleMatrixFactory.*;

public class TestMatrixData {

    public static final double TEST_PRECISION = 0.000001d;

    // Definicja macierzy używanych w testach.
    public static final IDoubleMatrix FULL = full(new double[][]{
            new double[]{2, 5, -3},
            new double[]{0, -7, 4},
            new double[]{3, 9, -1}
    });
    public static final IDoubleMatrix ANTIDIAONAL = antiDiagonal(4, -1, 3);
    public static final IDoubleMatrix COLUMN = column(new double[]{-7, 1, 1}, 3);
    public static final IDoubleMatrix DIAGONAL = diagonal(9, -2, 0);
    public static final IDoubleMatrix IDENTITY = identity(3);
    public static final IDoubleMatrix ROW = row(new double[]{-1, -1, 2}, 3);
    public static final IDoubleMatrix FULL2x3 = full(new double[][]{
            new double[]{2, -5, 4},
            new double[]{6, -3, 1},
    });
    public static final IDoubleMatrix COLUMN2x3 = column(new double[]{5, -4}, 3);
    public static final IDoubleMatrix ROW2x3 = row(new double[]{-7, 8, 0}, 2);
    public static final IDoubleMatrix FULL3x2 = full(new double[][]{
            new double[]{8, 9},
            new double[]{-1, -1},
            new double[]{0, -2}
    });
    public static final IDoubleMatrix COLUMN3x2 = column(new double[]{5, -4, 9}, 2);
    public static final IDoubleMatrix ROW3x2 = row(new double[]{2, 3}, 3);
    public static final IDoubleMatrix VECTOR2 = vector(8, -2);
    public static final IDoubleMatrix VECTOR3 = vector(7, 4, -5);
    private static final Shape shape = Shape.matrix(3, 3);
    public static final IDoubleMatrix SPARSE = sparse(
            shape, new MatrixCellValue(0, 2, 6),
            new MatrixCellValue(1, 0, -2),
            new MatrixCellValue(2, 2, 1)
    );
    public static final IDoubleMatrix CONSTANT = constant(3, shape);
    public static final IDoubleMatrix ZERO = zero(shape);
    private static final Shape shape2x3 = Shape.matrix(2, 3);
    public static final IDoubleMatrix SPARSE2x3 = sparse(
            shape2x3, new MatrixCellValue(0, 2, 5),
            new MatrixCellValue(1, 0, -1)
    );
    public static final IDoubleMatrix CONSTANT2x3 = constant(5, shape2x3);
    public static final IDoubleMatrix ZERO2x3 = zero(shape2x3);
    private static final Shape shape3x2 = Shape.matrix(3, 2);
    public static final IDoubleMatrix SPARSE3x2 = sparse(
            shape3x2, new MatrixCellValue(0, 0, 9),
            new MatrixCellValue(1, 1, 7),
            new MatrixCellValue(2, 0, -4)
    );
    public static final IDoubleMatrix CONSTANT3x2 = constant(-1, shape3x2);
    public static final IDoubleMatrix ZERO3x2 = zero(shape3x2);

}
