package com.company.matrix.permutation;

import com.company.matrix.Matrix;

public class Antidiagonal extends Permutation {

    public Antidiagonal(double... values) {
        super(values);
    }

    public int columnFromRow(int x) {
        return shape().rows - 1 - x;
    }

    public int rowFromColumn(int x) {
        return shape().rows - 1 - x;
    }

    public Matrix times(double scalar) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = get(i) * scalar;
        }

        return new Antidiagonal(values);
    }

    public double get(int x) {
        return values[x];
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addAntidiagonal(this);
    }

    @Override
    public Matrix addAntidiagonal(Antidiagonal matrix) {
        double[] result = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            result[i] = get(i) + matrix.get(i);
        }

        return new Antidiagonal(result);
    }


    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReverseAntidiagonal(this);
    }

    @Override
    public Matrix multiplyReverseAntidiagonal(Antidiagonal matrix) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = matrix.get(i) * get(shape().rows - 1 - i);
        }

        return new Diagonal(values);
    }

    @Override
    public Matrix multiplyReverseDiagonal(Diagonal matrix) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = matrix.get(i) * get(i);
        }

        return new Antidiagonal(values);
    }

}
