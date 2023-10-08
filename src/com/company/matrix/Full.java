package com.company.matrix;

import com.company.matrix.permutation.Permutation;

public class Full extends Matrix {
    private double[][] values;

    public Full(double[][] values) {
        assert (values != null);
        assert (values.length > 0 && values[0].length > 0);
        this.values = new double[values.length][values[0].length];
        for (int i = 0; i < values.length; i++) {
            assert (values[i].length == values[0].length);
            for (int j = 0; j < values[0].length; j++) {
                this.values[i][j] = values[i][j];
            }
        }

        setShape(values.length, values[0].length);
    }

    protected Full() {
    }

    protected Matrix construct(double[][] values) {
        return new Full(values);
    }

    protected void setValues(double[][] values) {
        this.values = values;
    }

    public double getValue(int x, int y) {
        return values[x][y];
    }

    public Matrix times(double scalar) {
        double[][] values = new double[shape().rows][shape().columns];

        for (int i = 0; i < shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = get(i, j) * scalar;
            }
        }

        return construct(values);
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addFull(this);
    }

    protected double[][] standardAddToArray(Matrix matrix) {
        double[][] values = new double[shape().rows][shape().columns];

        for (int i = 0; i < shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = get(i, j) + matrix.get(i, j);
            }
        }

        return values;
    }

    @Override
    protected Matrix addVector(Vector matrix) {
        return construct(standardAddToArray(matrix));
    }

    @Override
    protected Matrix addSparse(Sparse matrix) {
        return matrix.addFull(this);
    }

    @Override
    public Matrix addPermutation(Permutation matrix) {
        return construct(standardAddToArray(matrix));
    }

    @Override
    protected Matrix addFull(Full matrix) {
        return construct(standardAddToArray(matrix));
    }

    @Override
    protected Matrix addColumn(Column matrix) {
        return construct(standardAddToArray(matrix));
    }

    @Override
    protected Matrix addConstant(Constant matrix) {
        return construct(standardAddToArray(matrix));
    }

    @Override
    protected Matrix addRow(Row matrix) {
        return construct(standardAddToArray(matrix));
    }

    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReverseFull(this);
    }

    private double[][] standardMultiplyReverseToArray(Matrix matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                for (int k = 0; k < shape().rows; k++) {
                    values[i][j] += matrix.get(i, k) * get(k, j);
                }
            }
        }

        return values;
    }

    @Override
    protected Matrix multiplyReverseFull(Full matrix) {
        return construct(standardMultiplyReverseToArray(matrix));
    }

    @Override
    protected Matrix multiplyReverseColumn(Column matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];
        double[] columnSums = new double[shape().columns];

        for (int i = 0; i < shape().columns; i++) {
            for (int j = 0; j < shape().rows; j++) {
                columnSums[i] += get(j, i);
            }
        }

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = matrix.get(i) * columnSums[j];
            }
        }

        return construct(values);
    }

    protected Matrix multiplyReverseRowHelp(Matrix matrix) {
        double[] result = new double[shape().columns];

        for (int i = 0; i < shape().columns; i++) {
            for (int j = 0; j < shape().rows; j++) {
                result[i] += matrix.get(0, j) * get(j, i);
            }
        }

        return new Row(matrix.shape().rows, result);
    }

    @Override
    protected Matrix multiplyReverseRow(Row matrix) {
        return multiplyReverseRowHelp(matrix);
    }

    @Override
    protected Matrix multiplyReverseConstant(Constant matrix) {
        return multiplyReverseRowHelp(matrix);
    }

    @Override
    protected Matrix multiplyReverseSparse(Sparse matrix) {
        double[][] result = new double[matrix.shape().rows][shape().columns];

        for (int i = 0; i < matrix.cellCount(); i++) {
            for (int j = 0; j < shape().columns; j++) {
                result[matrix.getCell(i).row][j] += matrix.getCell(i).value * get(matrix.getCell(i).column, j);
            }
        }

        return construct(result);
    }

    @Override
    public Matrix multiplyReversePermutation(Permutation matrix) {
        double[][] values = new double[matrix.shape().rows][shape().columns];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = matrix.getFromRow(i) * get(matrix.columnFromRow(i), j);
            }
        }

        return construct(values);
    }
}
