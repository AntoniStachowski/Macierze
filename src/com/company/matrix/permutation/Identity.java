package com.company.matrix.permutation;

import com.company.matrix.*;

import static java.lang.Math.sqrt;

public class Identity extends Diagonal {

    public Identity(int size) {
        super(size);
    }

    public double getValue(int x, int y) {
        if (x != y) {
            return 0;
        }

        return 1;
    }

    @Override
    public double getFromRow(int x) {
        return 1;
    }

    public int columnFromRow(int x) {
        return x;
    }

    public int rowFromColumn(int x) {
        return x;
    }

    @Override
    public double calculateNormInfinity() {
        return 1;
    }

    @Override
    public double calculateNormOne() {
        return 1;
    }

    @Override
    public double calculateFrobeniusNorm() {
        return sqrt(shape().rows);
    }

    public Matrix times(double scalar) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = scalar;
        }

        return new Diagonal(values);
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addIdentity(this);
    }

    public Matrix multiply(Matrix matrix) {
        return matrix;
    }

    @Override
    protected Matrix multiplyReverseVector(Vector matrix) {
        return matrix;
    }

    @Override
    protected Matrix multiplyReverseSparse(Sparse matrix) {
        return matrix;
    }

    @Override
    protected Matrix multiplyReverseRow(Row matrix) {
        return matrix;
    }

    @Override
    protected Matrix multiplyReverseFull(Full matrix) {
        return matrix;
    }

    @Override
    public Matrix multiplyReverseDiagonal(Diagonal matrix) {
        return matrix;
    }

    @Override
    protected Matrix multiplyReverseConstant(Constant matrix) {
        return matrix;
    }

    @Override
    protected Matrix multiplyReverseColumn(Column matrix) {
        return matrix;
    }

    @Override
    public Matrix multiplyReverseAntidiagonal(Antidiagonal matrix) {
        return matrix;
    }

    @Override
    public Matrix multiplyReversePermutation(Permutation matrix) {
        return matrix;
    }
}
