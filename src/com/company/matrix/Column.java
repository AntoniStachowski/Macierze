package com.company.matrix;

import com.company.matrix.permutation.Permutation;
import com.company.utils.Shape;

import java.util.Arrays;

import static java.lang.Math.*;

public class Column extends Matrix {
    private final double[] values;

    public Column(int width, double... values) {
        assert (values != null);
        assert (values.length > 0 && width > 0);

        this.values = Arrays.copyOf(values, values.length);

        setShape(values.length, width);
    }

    public double getValue(int x, int y) {
        return values[x];
    }

    public double get(int x) {
        return get(x, 0);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(shape().rows + "x" + shape().columns + '\n');

        for (int i = 0; i < shape().rows; i++) {
            appendCopies(string, shape().columns, get(i));
            string.append('\n');
        }

        return string.toString();
    }

    @Override
    public double calculateNormOne() {
        double sum = 0;

        for (int i = 0; i < shape().rows; i++) {
            sum += abs(get(i));
        }

        return sum;
    }

    @Override
    public double calculateNormInfinity() {
        double max = abs(get(0));

        for (int i = 1; i < shape().rows; i++) {
            max = max(max, abs(get(i)));
        }

        return max * shape().columns;
    }

    @Override
    public double calculateFrobeniusNorm() {
        double sum = 0;

        for (int i = 0; i < shape().rows; i++) {
            sum += get(i) * get(i);
        }

        return sqrt(sum * shape().columns);
    }

    public Matrix times(double scalar) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = get(i) * scalar;
        }

        return new Column(shape().columns, values);
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addColumn(this);
    }

    @Override
    protected Matrix addRow(Row matrix) {
        return super.standardAdd(matrix);
    }

    @Override
    protected Matrix addVector(Vector matrix) {
        return matrix.addColumn(this);
    }

    @Override
    public Matrix addPermutation(Permutation matrix) {
        return super.standardAdd(matrix);
    }

    @Override
    protected Matrix addFull(Full matrix) {
        return super.standardAdd(matrix);
    }

    @Override
    protected Matrix addSparse(Sparse matrix) {
        return matrix.addColumn(this);
    }

    private Column addColumnHelp(Matrix matrix) {
        double[] values = new double[shape().rows];

        for (int i = 0; i < shape().rows; i++) {
            values[i] = get(i) + matrix.get(i, 0);
        }

        return new Column(shape().columns, values);
    }

    @Override
    protected Matrix addColumn(Column matrix) {
        return addColumnHelp(matrix);
    }

    @Override
    protected Matrix addConstant(Constant matrix) {
        return addColumnHelp(matrix);
    }

    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReverseColumn(this);
    }

    @Override
    protected Matrix multiplyReverseSparse(Sparse matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.cellCount(); i++) {
            values[matrix.getCell(i).row] += matrix.getCell(i).value * get(matrix.getCell(i).column);
        }

        return new Column(shape().columns, values);
    }

    @Override
    protected Matrix multiplyReverseConstant(Constant matrix) {
        double values = 0;

        for (int i = 0; i < shape().rows; i++) {
            values += get(i);
        }
        values *= matrix.get(0, 0);

        return new Constant(Shape.matrix(matrix.shape().rows, shape().columns), values);
    }

    @Override
    protected Matrix multiplyReverseRow(Row matrix) {
        double values = 0;

        for (int i = 0; i < shape().rows; i++) {
            values += matrix.get(i) * get(i);
        }

        return new Constant(Shape.matrix(matrix.shape().rows, shape().columns), values);
    }

    @Override
    protected Matrix multiplyReverseFull(Full matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().rows; j++) {
                values[i] += matrix.get(i, j) * get(j);
            }
        }

        return new Column(shape().columns, values);
    }

    @Override
    protected Matrix multiplyReverseColumn(Column matrix) {
        double[] values = new double[matrix.shape().rows];
        double columnSum = 0;

        for (int i = 0; i < shape().rows; i++) {
            columnSum += get(i);
        }

        for (int i = 0; i < matrix.shape().rows; i++) {
            values[i] = matrix.get(i) * columnSum;
        }

        return new Column(shape().columns, values);
    }

    @Override
    public Matrix multiplyReversePermutation(Permutation matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            values[i] = matrix.getFromRow(i) * get(matrix.columnFromRow(i));
        }

        return new Column(shape().columns, values);
    }
}
