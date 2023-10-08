package com.company.matrix;

import com.company.matrix.permutation.Permutation;
import com.company.utils.Shape;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Constant extends Matrix {
    private final double value;

    public Constant(Shape shape, double value) {
        assert (shape != null);
        this.value = value;

        setShape(shape.rows, shape.columns);
    }

    public double getValue(int x, int y) {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(shape().rows + "x" + shape().columns + '\n');

        for (int i = 0; i < shape().rows; i++) {
            appendCopies(string, shape().columns, get(0, 0));
            string.append('\n');
        }

        return string.toString();
    }

    @Override
    public double calculateNormOne() {
        return abs(get(0, 0)) * shape().rows;
    }

    @Override
    public double calculateNormInfinity() {
        return abs(get(0, 0)) * shape().columns;
    }

    @Override
    public double calculateFrobeniusNorm() {
        return abs(get(0, 0)) * sqrt(shape().rows * shape().columns);
    }

    public Matrix times(double scalar) {
        return new Constant(shape(), get(0, 0) * scalar);
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addConstant(this);
    }

    @Override
    protected Matrix addColumn(Column matrix) {
        return matrix.addConstant(this);
    }

    @Override
    protected Matrix addConstant(Constant matrix) {
        return new Constant(shape(), get(0, 0) + matrix.get(0, 0));
    }

    @Override
    protected Matrix addSparse(Sparse matrix) {
        return matrix.addConstant(this);
    }

    @Override
    protected Matrix addFull(Full matrix) {
        return super.standardAdd(matrix);
    }

    @Override
    public Matrix addPermutation(Permutation matrix) {
        return super.standardAdd(matrix);
    }

    @Override
    protected Matrix addVector(Vector matrix) {
        return matrix.addConstant(this);
    }

    @Override
    protected Matrix addRow(Row matrix) {
        return matrix.addConstant(this);
    }

    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReverseConstant(this);
    }

    @Override
    protected Matrix multiplyReverseSparse(Sparse matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.cellCount(); i++) {
            values[matrix.getCell(i).row] += matrix.getCell(i).value * get(0, 0);
        }

        return new Column(shape().columns, values);
    }

    @Override
    protected Matrix multiplyReverseColumn(Column matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            values[i] = matrix.get(i) * get(0, 0) * shape().rows;
        }

        return new Column(shape().columns, values);
    }

    @Override
    protected Matrix multiplyReverseFull(Full matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < matrix.shape().columns; j++) {
                values[i] += matrix.get(i, j) * get(0, 0);
            }
        }

        return new Column(shape().columns, values);
    }

    @Override
    protected Matrix multiplyReverseConstant(Constant matrix) {
        return new Constant(Shape.matrix(matrix.shape().rows, shape().columns), matrix.get(0, 0) * get(0, 0) * shape().rows);
    }

    @Override
    protected Matrix multiplyReverseRow(Row matrix) {
        double value = 0;

        for (int i = 0; i < matrix.shape().columns; i++) {
            value += matrix.get(i);
        }
        value *= get(0, 0);

        return new Constant(Shape.matrix(matrix.shape().rows, shape().columns), value);
    }

    @Override
    public Matrix multiplyReversePermutation(Permutation matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            values[i] = matrix.getFromRow(i) * get(0, 0);
        }

        return new Column(shape().columns, values);
    }
}
