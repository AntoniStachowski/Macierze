package com.company.utils;

import com.company.matrix.IDoubleMatrix;
import com.company.matrix.*;
import com.company.matrix.permutation.Antidiagonal;
import com.company.matrix.permutation.Diagonal;
import com.company.matrix.permutation.Identity;
import com.company.matrix.permutation.Permutation;

public class DoubleMatrixFactory {

    private DoubleMatrixFactory() {
    }

    public static IDoubleMatrix sparse(Shape shape, MatrixCellValue... values) {
        return new Sparse(shape, values);
    }

    public static IDoubleMatrix full(double[][] values) {
        return new Full(values);
    }

    public static IDoubleMatrix identity(int size) {
        return new Identity(size);
    }

    public static IDoubleMatrix diagonal(double... diagonalValues) {
        return new Diagonal(diagonalValues);
    }

    public static IDoubleMatrix antiDiagonal(double... antiDiagonalValues) {
        return new Antidiagonal(antiDiagonalValues);
    }

    public static IDoubleMatrix vector(double... values) {
        return new Vector(values);
    }

    public static IDoubleMatrix zero(Shape shape) {
        return new Zero(shape);
    }

    public static IDoubleMatrix constant(double value, Shape shape) {
        return new Constant(shape, value);
    }

    public static IDoubleMatrix row(double[] values, int rows) {
        return new Row(rows, values);
    }

    public static IDoubleMatrix column(double[] values, int columns) {
        return new Column(columns, values);
    }

    public static IDoubleMatrix permutation(double[] values, int[] permutation) {
        return new Permutation(values, permutation);
    }
}
