package com.company.matrix;

import com.company.matrix.permutation.Permutation;
import com.company.utils.Shape;

public class Zero extends Constant {

    public Zero(Shape shape) {
        super(shape, 0);
    }

    public Matrix times(double scalar) {
        return this;
    }

    protected Matrix add(Matrix matrix) {
        return matrix;
    }

    @Override
    protected Matrix addRow(Row matrix) {
        return matrix;
    }

    @Override
    protected Matrix addConstant(Constant matrix) {
        return matrix;
    }

    @Override
    protected Matrix addColumn(Column matrix) {
        return matrix;
    }

    @Override
    protected Matrix addFull(Full matrix) {
        return matrix;
    }

    @Override
    public Matrix addPermutation(Permutation matrix) {
        return matrix;
    }

    @Override
    protected Matrix addSparse(Sparse matrix) {
        return matrix;
    }

    @Override
    protected Matrix addVector(Vector matrix) {
        return matrix;
    }

    public Zero multiply(Matrix matrix) {
        return new Zero(Shape.matrix(shape().rows, matrix.shape().columns));
    }

    public Zero multiplyReverse(Matrix matrix) {
        return new Zero(Shape.matrix(matrix.shape().rows, shape().columns));
    }


    @Override
    protected Zero multiplyReverseColumn(Column matrix) {
        return multiplyReverse(matrix);
    }

    @Override
    protected Zero multiplyReverseConstant(Constant matrix) {
        return multiplyReverse(matrix);
    }

    @Override
    protected Zero multiplyReverseFull(Full matrix) {
        return multiplyReverse(matrix);
    }

    @Override
    protected Zero multiplyReverseRow(Row matrix) {
        return multiplyReverse(matrix);
    }

    @Override
    protected Zero multiplyReverseSparse(Sparse matrix) {
        return multiplyReverse(matrix);
    }

    @Override
    public Matrix multiplyReversePermutation(Permutation matrix) {
        return multiplyReverse(matrix);
    }
}
