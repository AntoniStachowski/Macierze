package com.company.matrix.permutation;

import com.company.matrix.*;
import com.company.utils.MatrixCellValue;
import com.company.utils.Shape;

import java.util.Arrays;

import static java.lang.Math.*;

// Macierze które mają po jednej niezerowej wartości w każdym wierszu i kolumnie.
// Szczególnym przypadkiem są macierze diagonalne i antydiagonalne.
public class Permutation extends Matrix {
    double[] values;
    int[] permutation;
    int[] antipermutation;

    public Permutation(double[] values, int[] permutation) {
        assert (values != null && permutation != null);
        assert (values.length == permutation.length && values.length > 0);

        this.values = Arrays.copyOf(values, values.length);
        antipermutation = new int[values.length];
        this.permutation = new int[values.length];
        Arrays.fill(antipermutation, -1);


        for (int i = 0; i < values.length; i++) {
            assert (permutation[i] <= values.length);
            assert (antipermutation[permutation[i]] == -1);
            this.permutation[i] = permutation[i];
            antipermutation[permutation[i]] = i;
        }

        setShape(values.length);
    }

    protected Permutation(double[] values) {
        assert (values != null);
        assert (values.length > 0);

        this.values = Arrays.copyOf(values, values.length);

        setShape(values.length);
    }

    protected Permutation(int size) {
        assert (size > 0);

        setShape(size);
    }


    public double getFromRow(int x) {
        return values[x];
    }

    public double getFromColumn(int x) {
        return values[rowFromColumn(x)];
    }

    public int columnFromRow(int x) {
        return permutation[x];
    }

    public int rowFromColumn(int x) {
        return antipermutation[x];
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(shape().rows + "x" + shape().columns + '\n');

        for (int i = 0; i < shape().rows; i++) {
            appendCopies(string, columnFromRow(i), 0);
            string.append(" " + getFromRow(i) + " ");
            appendCopies(string, shape().columns - 1 - columnFromRow(i), 0);
            string.append('\n');
        }

        return string.toString();
    }

    private double maximum() {
        double max = abs(getFromRow(0));

        for (int i = 1; i < shape().rows; i++) {
            max = max(max, abs(getFromRow(i)));
        }

        return max;
    }

    @Override
    public double calculateNormOne() {
        return maximum();
    }

    @Override
    public double calculateNormInfinity() {
        return maximum();
    }

    @Override
    public double calculateFrobeniusNorm() {
        double sum = 0;

        for (int i = 0; i < shape().rows; i++) {
            sum += getFromRow(i) * getFromRow(i);
        }

        return sqrt(sum);
    }

    @Override
    public IDoubleMatrix times(double scalar) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = getFromRow(i) * scalar;
        }

        return new Permutation(values, permutation);
    }

    @Override
    public double getValue(int x, int y) {
        if (columnFromRow(x) == y) {
            return getFromRow(x);
        }
        return 0;
    }

    @Override
    protected Matrix add(Matrix matrix) {
        return matrix.addPermutation(this);
    }

    @Override
    public Matrix addPermutation(Permutation matrix) {
        MatrixCellValue[] cells = new MatrixCellValue[shape().rows * 2];

        for (int i = 0; i < shape().rows; i++) {
            cells[2 * i] = new MatrixCellValue(i, columnFromRow(i), getFromRow(i));
            cells[2 * i + 1] = new MatrixCellValue(i, matrix.columnFromRow(i), matrix.getFromRow(i));
        }

        return Sparse.SparseCompressed(shape(), cells);
    }

    @Override
    public Matrix addAntidiagonal(Antidiagonal matrix) {
        return addPermutation(matrix);
    }

    @Override
    protected Matrix addColumn(Column matrix) {
        return standardAdd(matrix);
    }

    @Override
    protected Matrix addConstant(Constant matrix) {
        return standardAdd(matrix);
    }

    @Override
    public Matrix addDiagonal(Diagonal matrix) {
        return addPermutation(matrix);
    }

    @Override
    public Matrix addIdentity(Identity matrix) {
        return addPermutation(matrix);
    }

    @Override
    protected Matrix addRow(Row matrix) {
        return standardAdd(matrix);
    }

    @Override
    protected Matrix addVector(Vector matrix) {
        return standardAdd(matrix);
    }

    @Override
    public Matrix addSparse(Sparse matrix) {
        MatrixCellValue[] cells = new MatrixCellValue[shape().rows + matrix.cellCount()];

        for (int i = 0; i < shape().rows; i++) {
            cells[i] = new MatrixCellValue(i, columnFromRow(i), getFromRow(i));
        }

        for (int i = 0; i < matrix.cellCount(); i++) {
            cells[shape().rows + i] = matrix.getCell(i);
        }

        return Sparse.SparseCompressed(shape(), cells);
    }

    @Override
    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReversePermutation(this);
    }

    @Override
    protected Matrix multiplyReverseColumn(Column matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = matrix.get(i) * getFromColumn(j);
            }
        }

        return new Full(values);
    }

    @Override
    protected Matrix multiplyReverseConstant(Constant matrix) {
        double[] values = new double[shape().columns];

        for (int i = 0; i < shape().columns; i++) {
            values[i] = matrix.get(0, 0) * getFromColumn(i);
        }

        return new Row(matrix.shape().rows, values);
    }

    @Override
    protected Matrix multiplyReverseFull(Full matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = matrix.get(i, rowFromColumn(j)) * getFromColumn(j);
            }
        }

        return new Full(values);
    }

    @Override
    protected Matrix multiplyReverseRow(Row matrix) {
        double[] values = new double[shape().columns];

        for (int i = 0; i < shape().columns; i++) {
            values[i] = matrix.get(0, rowFromColumn(i)) * getFromColumn(i);
        }

        return new Row(matrix.shape().rows, values);
    }

    @Override
    protected Matrix multiplyReverseVector(Vector matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            values[i] = matrix.get(i) * getFromRow(0);
        }

        return new Vector(values);
    }

    @Override
    protected Matrix multiplyReverseSparse(Sparse matrix) {
        MatrixCellValue[] cells = new MatrixCellValue[matrix.cellCount()];

        for (int i = 0; i < matrix.cellCount(); i++) {
            cells[i] = new MatrixCellValue(matrix.getCell(i).row, columnFromRow(matrix.getCell(i).column),
                    matrix.getCell(i).value * getFromRow(matrix.getCell(i).column));
        }

        return new Sparse(Shape.matrix(matrix.shape().rows, shape().columns), cells);
    }

    @Override
    public Matrix multiplyReversePermutation(Permutation matrix) {
        double[] value = new double[shape().rows];
        int[] permutation = new int[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            value[i] = matrix.getFromRow(i) * getFromRow(matrix.columnFromRow(i));
            permutation[i] = columnFromRow(matrix.columnFromRow(i));
        }

        return new Permutation(value, permutation);
    }
}
