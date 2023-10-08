package com.company;

import com.company.matrix.IDoubleMatrix;
import com.company.utils.MatrixCellValue;
import com.company.utils.Shape;

import static com.company.utils.DoubleMatrixFactory.*;


// Autor: Antoni Stachowski
// IntelliJ

public class Main {

    public static void main(String[] args) {
        // Tworzenie przykładowych macierzy.

        IDoubleMatrix ANTIDIAGONAL = antiDiagonal(4, 7, 2, -4, 0, 6, 6, 1, -7, -1);

        IDoubleMatrix COLUMN = column(new double[]{4, 1, 1, 1, -9, 0, -6, 7, 1, -2}, 10);

        IDoubleMatrix CONSTANT = constant(6, Shape.matrix(10, 10));

        IDoubleMatrix DIAGONAL = diagonal(7, -2, -3, 0, 5, -1, -9, -6, 1, 2);

        IDoubleMatrix FULL = full(new double[][]{
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{7, 2, 4, 8, -1, -5, 7, 9, 11, -3},
                new double[]{7, 2, 5, 0, 0, 6, 5, 5, -9, 1},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{7, 2, 4, 8, -1, -5, 7, 9, 11, -3},
                new double[]{7, 2, 5, 0, 0, 6, 5, 5, -9, 1},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{7, 2, 4, 8, -1, -5, 7, 9, 11, -3},
                new double[]{7, 2, 5, 0, 0, 6, 5, 5, -9, 1},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        });

        IDoubleMatrix IDENTITY = identity(10);

        IDoubleMatrix PERMUTATION = permutation(new double[]{6, -2, 6, 7, 1, 2, -1, -3, 9, -5}, new int[]{4, 7, 0, 2, 6, 1, 9, 3, 8, 5});

        IDoubleMatrix ROW = row(new double[]{8, 0, 5, -1, -7, 4, -3, 5, 7, 8}, 10);

        IDoubleMatrix SPARSE = sparse(Shape.matrix(10, 10), new MatrixCellValue(3, 5, -5),
                new MatrixCellValue(1, 8, 2), new MatrixCellValue(9, 0, -5));

        IDoubleMatrix VECTOR = vector(5, -1, 5, 5, -7, -9, 0, 2, 6, 7);

        IDoubleMatrix ZERO = zero(Shape.matrix(10, 10));


        // Wypisanie stworzonych macierzy.
        System.out.println("ANTIDIAGONAL\n" + ANTIDIAGONAL + '\n');
        System.out.println("COLUMN\n" + COLUMN + '\n');
        System.out.println("CONSTANT\n" + CONSTANT + '\n');
        System.out.println("DIAGONAL\n" + DIAGONAL + '\n');
        System.out.println("FULL\n" + FULL + '\n');
        System.out.println("IDENTITY\n" + IDENTITY + '\n');
        System.out.println("PERMUTATION\n" + PERMUTATION + '\n');
        System.out.println("ROW\n" + ROW + '\n');
        System.out.println("SPARSE\n" + SPARSE + '\n');
        System.out.println("VECTOR\n" + VECTOR + '\n');
        System.out.println("ZERO\n" + ZERO + '\n');

        // Przykładowe operacje na macierzach.
        System.out.println("ANTIDIAGONAL + CONSTANT\n" + ANTIDIAGONAL.plus(CONSTANT).toString() + '\n');
        System.out.println("SPARSE + COLUMN\n" + SPARSE.plus(COLUMN).toString() + '\n');
        System.out.println("DIAGONAL - ZERO\n" + DIAGONAL.minus(ZERO).toString() + '\n');
        System.out.println("PERMUTATION - SPARSE\n" + PERMUTATION.minus(SPARSE).toString() + '\n');
        System.out.println("PERMUTATION x ROW\n" + PERMUTATION.times(ROW).toString() + '\n');
        System.out.println("FULL x VECTOR\n" + FULL.times(VECTOR).toString() + '\n');

        // Przykładowe normy macierzy.
        System.out.println("FULL norm one " + FULL.normOne());
        System.out.println("SPARSE norm infinity " + SPARSE.normInfinity());
        System.out.println("PERMUTATION Frobenius norm " + PERMUTATION.frobeniusNorm());
    }
}
