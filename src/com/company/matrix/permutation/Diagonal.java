package com.company.matrix.permutation;

import com.company.matrix.Matrix;

public class Diagonal extends Permutation {

    public Diagonal(int size) {
        super(size);
    }

    public Diagonal(double... values) {
        super(values);
    }

    public double get(int x) {
        return values[x];
    }

    public int columnFromRow(int x) {
        return x;
    }

    public int rowFromColumn(int x) {
        return x;
    }

    public Matrix times(double scalar) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = get(i) * scalar;
        }

        return new Diagonal(values);
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addDiagonal(this);
    }

    @Override
    public Matrix addDiagonal(Diagonal matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            values[i] = getFromRow(i) + matrix.getFromRow(i);
        }

        return new Diagonal(values);
    }

    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReverseDiagonal(this);
    }

    @Override
    public Matrix multiplyReverseDiagonal(Diagonal matrix) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = matrix.get(i) * get(i);
        }

        return new Diagonal(values);
    }

    @Override
    public Matrix multiplyReverseAntidiagonal(Antidiagonal matrix) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = matrix.get(i) * get(shape().rows - 1 - i);
        }

        return new Antidiagonal(values);
    }
}
