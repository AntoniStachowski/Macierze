package com.company.matrix;

import com.company.matrix.permutation.Permutation;

import static java.lang.Math.*;

public class Row extends Matrix {
    private final double[] values;

    public Row(int height, double... values) {
        assert (values != null);
        assert (values.length > 0 && height > 0);

        this.values = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }

        setShape(height, values.length);
    }

    public double getValue(int x, int y) {
        return values[y];
    }

    public double get(int i) {
        return get(0, i);
    }

    @Override
    public double calculateNormInfinity() {
        double sum = 0;

        for (int i = 0; i < shape().columns; i++) {
            sum += abs(get(i));
        }

        return sum;
    }

    @Override
    public double calculateNormOne() {
        double max = abs(get(0));

        for (int i = 1; i < shape().columns; i++) {
            max = max(max, abs(get(i)));
        }

        return max * shape().rows;
    }

    @Override
    public double calculateFrobeniusNorm() {
        double sum = 0;

        for (int i = 0; i < shape().columns; i++) {
            sum += get(i) * get(i);
        }

        return sqrt(sum * shape().rows);
    }

    public Matrix times(double scalar) {
        double[] values = new double[shape().columns];

        for (int i = 0; i < shape().columns; i++) {
            values[i] = get(i) * scalar;
        }

        return new Row(shape().rows, values);
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addRow(this);
    }

    @Override
    protected Matrix addSparse(Sparse matrix) {
        return matrix.addRow(this);
    }

    @Override
    protected Matrix addFull(Full matrix) {
        return super.standardAdd(matrix);
    }

    @Override
    protected Matrix addColumn(Column matrix) {
        return super.standardAdd(matrix);
    }

    @Override
    public Matrix addPermutation(Permutation matrix) {
        return super.standardAdd(matrix);
    }

    @Override
    protected Matrix addVector(Vector matrix) {
        return matrix.addRow(this);
    }

    private Matrix addRowHelp(Matrix matrix) {
        double[] values = new double[shape().columns];

        for (int i = 0; i < shape().columns; i++) {
            values[i] = get(i) + matrix.get(0, i);
        }

        return new Row(shape().rows, values);
    }

    @Override
    protected Matrix addRow(Row matrix) {
        return addRowHelp(matrix);
    }

    @Override
    protected Matrix addConstant(Constant matrix) {
        return addRowHelp(matrix);
    }

    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReverseRow(this);
    }

    @Override
    protected Matrix multiplyReverseFull(Full matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];
        double[] rowSums = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < matrix.shape().columns; j++) {
                rowSums[i] += matrix.get(i, j);
            }
        }

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = rowSums[i] * get(j);
            }
        }

        return new Full(values);
    }

    @Override
    protected Matrix multiplyReverseSparse(Sparse matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];

        for (int i = 0; i < matrix.cellCount(); i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[matrix.getCell(i).row][j] += matrix.getCell(i).value * get(j);
            }
        }

        return new Full(values);
    }

    @Override
    protected Matrix multiplyReverseColumn(Column matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = matrix.get(i) * get(j) * shape().rows;
            }
        }

        return new Full(values);
    }

    @Override
    protected Matrix multiplyReverseRow(Row matrix) {
        double rowSum = 0;
        double[] values = new double[shape().columns];

        for (int i = 0; i < matrix.shape().columns; i++) {
            rowSum += matrix.get(i);
        }

        for (int i = 0; i < shape().columns; i++) {
            values[i] = rowSum * get(i);
        }

        return new Row(matrix.shape().rows, values);
    }

    @Override
    protected Matrix multiplyReverseConstant(Constant matrix) {
        double[] values = new double[shape().columns];

        for (int i = 0; i < shape().columns; i++) {
            values[i] = matrix.get(0, 0) * get(i) * shape().rows;
        }

        return new Row(matrix.shape().rows, values);
    }

    @Override
    public Matrix multiplyReversePermutation(Permutation matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = matrix.getFromRow(i) * get(j);
            }
        }

        return new Full(values);
    }
}
