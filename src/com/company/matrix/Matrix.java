package com.company.matrix;

import com.company.matrix.permutation.Antidiagonal;
import com.company.matrix.permutation.Diagonal;
import com.company.matrix.permutation.Identity;
import com.company.matrix.permutation.Permutation;
import com.company.utils.Shape;

import static java.lang.Math.*;

public abstract class Matrix implements IDoubleMatrix {
    private Shape shape;
    private double normOne;
    private double normInfinity;
    private double frobeniusNorm;

    public Matrix() {

        // Norma nie może mieć wartości ujemnych, więc
        // -1 jest dobrym reprezentantem macierzy nieobliczonej.
        normOne = -1;
        normInfinity = -1;
        frobeniusNorm = -1;
    }

    // Normy liczymy tylko jeśli jeszcze tego nie zrobiliśmy.
    // Po policzeniu normy zapisujemy w macierzy jej wartość.

    @Override
    public double normOne() {
        if (normOne == -1) {
            normOne = calculateNormOne();
        }
        return normOne;
    }

    @Override
    public double normInfinity() {
        if (normInfinity == -1) {
            normInfinity = calculateNormInfinity();
        }
        return normInfinity;
    }

    @Override
    public double frobeniusNorm() {
        if (frobeniusNorm == -1) {
            frobeniusNorm = calculateFrobeniusNorm();
        }
        return frobeniusNorm;
    }

    protected void setShape(int x, int y) {
        shape = Shape.matrix(x, y);
    }

    protected void setShape(int x) {
        setShape(x, x);
    }

    public Shape shape() {
        return shape;
    }

    public abstract double getValue(int x, int y);


    // Funkcja pomocnicza do dodawania do ciągu kilku kopii tej samej wartości,
    // kompresująca je do w ... w jeśli jest ich 3 lub więcej.
    protected void appendCopies(StringBuilder string, int copies, double value) {
        if (copies == 1) {
            string.append(value);
        } else if (copies == 2) {
            string.append(value + " " + value);
        } else if (copies > 2) {
            string.append(value + " ... " + value);
        }
    }

    @Override
    public Matrix asMatrix() {
        return this;
    }

    public double get(int x, int y) {
        shape.assertInShape(x, y);
        return getValue(x, y);
    }

    @Override
    public double[][] data() {
        double[][] values = new double[shape().rows][shape().columns];

        for (int i = 0; i < shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                values[i][j] = get(i, j);
            }
        }

        return values;
    }

    public String toString() {
        double[][] values = data();
        StringBuilder string = new StringBuilder();
        string.append(shape().rows + "x" + shape().columns + '\n');


        for (int i = 0; i < shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                string.append(values[i][j] + " ");
            }
            string.append('\n');
        }

        return string.toString();
    }

    public double calculateNormInfinity() {
        double max = Double.MIN_VALUE;
        double rowSum = 0;

        for (int i = 0; i < shape().rows; i++) {
            rowSum = 0;
            for (int j = 0; j < shape().columns; j++) {
                rowSum += abs(get(i, j));
            }
            max = max(max, rowSum);
        }

        return max;
    }

    public double calculateNormOne() {
        double max = Double.MIN_VALUE;
        double columnSum = 0;

        for (int i = 0; i < shape().columns; i++) {
            columnSum = 0;
            for (int j = 0; j < shape().rows; j++) {
                columnSum += abs(get(j, i));
            }
            max = max(max, columnSum);
        }

        return max;
    }

    public double calculateFrobeniusNorm() {
        double sum = 0;

        for (int i = 0; i < shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                sum += get(i, j) * get(i, j);
            }
        }

        return sqrt(sum);
    }


    public Matrix plus(IDoubleMatrix matrix) {
        assert (shape.equals(matrix.shape()));

        if (matrix.asMatrix() != null) {
            return add(matrix.asMatrix());
        }

        return standardAdd(matrix);
    }

    public Matrix plus(double scalar) {
        return add(new Constant(shape(), scalar));
    }

    public Matrix minus(double scalar) {
        return plus(-scalar);
    }

    public Matrix minus(IDoubleMatrix matrix) {
        return plus(matrix.times(-1));
    }


    // Funkcja dodająca macierze brutalnie.
    public Matrix standardAdd(IDoubleMatrix matrix) {
        double[][] sum = new double[matrix.shape().rows][matrix.shape().columns];

        for (int i = 0; i < matrix.shape().rows; i++) {
            for (int j = 0; j < matrix.shape().columns; j++) {
                sum[i][j] = get(i, j) + matrix.get(i, j);
            }
        }

        return new Full(sum);
    }

    // Funkcje potrzebne do podejścia double dispatch.

    protected abstract Matrix add(Matrix matrix);

    public Matrix addAntidiagonal(Antidiagonal matrix) {
        return addPermutation(matrix);
    }

    protected abstract Matrix addColumn(Column matrix);

    protected abstract Matrix addConstant(Constant matrix);

    public Matrix addDiagonal(Diagonal matrix) {
        return addPermutation(matrix);
    }

    protected Matrix addFull(Full matrix) {
        return standardAdd(matrix);
    }

    public Matrix addIdentity(Identity matrix) {
        return addDiagonal(matrix);
    }

    protected abstract Matrix addRow(Row matrix);

    protected abstract Matrix addVector(Vector matrix);

    protected abstract Matrix addSparse(Sparse matrix);

    public abstract Matrix addPermutation(Permutation matrix);




    public Matrix times(IDoubleMatrix matrix) {
        assert (shape().columns == matrix.shape().rows);

        if (matrix.asMatrix() != null) {
            return multiply(matrix.asMatrix());
        }

        return standardMultiply(matrix);
    }

    //Funkcja mnożąca brutalnie.

    public Matrix standardMultiply(IDoubleMatrix matrix) {
        double[][] product = new double[shape().rows][matrix.shape().columns];

        for (int i = 0; i < shape().rows; i++) {
            for (int j = 0; j < matrix.shape().columns; j++) {
                for (int k = 0; k < shape().columns; k++) {
                    product[i][j] += get(i, k) * matrix.get(k, j);
                }
            }
        }

        return new Full(product);
    }

    //Funkcje potrzebne do podejścia double dispatch

    public Matrix multiply(Matrix matrix) {
        return standardMultiply(matrix);
    }

    public Matrix multiplyReverseAntidiagonal(Antidiagonal matrix) {
        return multiplyReversePermutation(matrix);
    }

    protected abstract Matrix multiplyReverseColumn(Column matrix);

    protected abstract Matrix multiplyReverseConstant(Constant matrix);

    public Matrix multiplyReverseDiagonal(Diagonal matrix) {
        return multiplyReversePermutation(matrix);
    }

    protected abstract Matrix multiplyReverseFull(Full matrix);

    protected abstract Matrix multiplyReverseRow(Row matrix);

    protected Matrix multiplyReverseVector(Vector matrix) {
        return multiplyReverseFull(matrix);
    }

    protected abstract Matrix multiplyReverseSparse(Sparse matrix);

    public abstract Matrix multiplyReversePermutation(Permutation matrix);

}
