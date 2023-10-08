package com.company.matrix;

import com.company.matrix.permutation.Permutation;
import com.company.utils.CellComparator;
import com.company.utils.MatrixCellValue;
import com.company.utils.Shape;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;

public class Sparse extends Matrix {
    // Macierze rzadkie nieregularne przechowujemy w postaci dwóch tablic,
    // jednej posortowanej leksykograficznie według wierszy,
    // drugiej według kolumn. Obie zawierają wszystkie niezerowe komórki macierzy.
    private final MatrixCellValue[] rowSorted;
    private final MatrixCellValue[] columnSorted;

    public Sparse(Shape shape, MatrixCellValue... values) {
        assert (shape != null);
        assert (values != null);

        Arrays.sort(values, CellComparator.giveRowComparator());
        rowSorted = Arrays.copyOf(values, values.length);

        Arrays.sort(values, CellComparator.giveColumnComparator());
        columnSorted = Arrays.copyOf(values, values.length);

        setShape(shape.rows, shape.columns);

        for (int i = 0; i < values.length; i++) {
            shape().assertInShape(values[i].row, values[i].column);
        }

        for (int i = 1; i < values.length; i++) {
            assert (!(values[i].row == values[i - 1].row && values[i].column == values[i - 1].column));
        }
    }

    public static Sparse SparseCompressed(Shape shape, MatrixCellValue... values) {
        Arrays.sort(values, CellComparator.giveRowComparator());

        ArrayList<MatrixCellValue> compressedCells = new ArrayList<>();
        double sum = values[0].value;

        for (int i = 1; i < values.length; i++) {
            if (values[i].row == values[i - 1].row && values[i].column == values[i - 1].column) {
                sum += values[i].value;
            } else {
                compressedCells.add(new MatrixCellValue(values[i - 1].row, values[i - 1].column, sum));
                sum = values[i].value;
            }
        }

        compressedCells.add(new MatrixCellValue(values[values.length - 1].row, values[values.length - 1].column, sum));

        MatrixCellValue[] result = new MatrixCellValue[compressedCells.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = compressedCells.get(i);
        }

        return new Sparse(shape, result);
    }

    public double getValue(int x, int y) {
        MatrixCellValue target = MatrixCellValue.cell(x, y, 0);
        int index = Arrays.binarySearch(rowSorted, target, CellComparator.giveRowComparator());
        if (index >= 0) {
            return rowSorted[index].value;
        }
        return 0;
    }

    public MatrixCellValue getCell(int n) {
        return rowSorted[n];
    }

    public int cellCount() {
        return rowSorted.length;
    }

    @Override
    public double calculateNormOne() {
        if (cellCount() == 0) {
            return 0;
        }

        double max = 0;
        double columnSum = abs(columnSorted[0].value);

        for (int i = 1; i < cellCount(); i++) {
            if (columnSorted[i].column != columnSorted[i - 1].column) {
                max = max(max, columnSum);
                columnSum = 0;
            }
            columnSum += abs(columnSorted[i].value);
        }

        max = max(max, columnSum);

        return max;
    }

    @Override
    public double calculateNormInfinity() {
        if (cellCount() == 0) {
            return 0;
        }

        double max = 0;
        double rowSum = abs(getCell(0).value);

        for (int i = 1; i < cellCount(); i++) {
            if (getCell(i).row != getCell(i - 1).row) {
                max = max(max, rowSum);
                rowSum = 0;
            }
            rowSum += abs(getCell(i).value);
        }

        max = max(max, rowSum);

        return max;
    }

    @Override
    public double calculateFrobeniusNorm() {
        double sum = 0;

        for (int i = 0; i < cellCount(); i++) {
            sum += getCell(i).value * getCell(i).value;
        }

        return sqrt(sum);
    }

    private Matrix addBrutal(Matrix matrix) {
        boolean[][] done = new boolean[shape().rows][shape().columns];
        double[][] values = new double[shape().rows][shape().columns];
        for (MatrixCellValue cell : rowSorted) {
            done[cell.row][cell.column] = true;
            values[cell.row][cell.column] = cell.value + matrix.get(cell.row, cell.column);
        }
        for (int i = 0; i < shape().rows; i++) {
            for (int j = 0; j < shape().columns; j++) {
                if (!done[i][j]) {
                    values[i][j] = matrix.get(i, j);
                }
            }
        }

        return new Full(values);
    }

    @Override
    protected Matrix addFull(Full matrix) {
        return addBrutal(matrix);
    }

    @Override
    protected Matrix addColumn(Column matrix) {
        return addBrutal(matrix);
    }

    @Override
    protected Matrix addConstant(Constant matrix) {
        return addBrutal(matrix);
    }

    @Override
    protected Matrix addRow(Row matrix) {
        return addBrutal(matrix);
    }

    @Override
    protected Matrix addSparse(Sparse matrix) {
        ArrayList<MatrixCellValue> result = new ArrayList<>();

        for (int i = 0; i < cellCount(); i++) {
            result.add(getCell(i));
        }

        for (int i = 0; i < matrix.cellCount(); i++) {
            result.add(matrix.getCell(i));
        }

        MatrixCellValue[] cells = new MatrixCellValue[result.size()];

        for (int i = 0; i < result.size(); i++) {
            cells[i] = result.get(i);
        }

        return SparseCompressed(shape(), cells);
    }

    @Override
    public Matrix addPermutation(Permutation matrix) {
        return matrix.addSparse(this);
    }

    @Override
    protected Matrix addVector(Vector matrix) {
        double[] values = new double[matrix.shape().rows];

        for (int i = 0; i < matrix.shape().rows; i++) {
            values[i] = matrix.get(i);
        }

        for (MatrixCellValue cell : rowSorted) {
            values[cell.row] += cell.value;
        }

        return new Vector(values);
    }

    public Matrix times(double scalar) {
        MatrixCellValue[] cells = new MatrixCellValue[cellCount()];

        for (int i = 0; i < cellCount(); i++) {
            cells[i] = new MatrixCellValue(getCell(i).row, getCell(i).column, getCell(i).value * scalar);
        }

        return new Sparse(shape(), cells);
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addSparse(this);
    }

    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReverseSparse(this);
    }

    public Matrix multiplySparse(Sparse matrix) {
        int x = 0;
        int y = 0;
        int xBegin, yBegin;
        ArrayList<MatrixCellValue> cells = new ArrayList<>();

        while (x < cellCount() && y < matrix.cellCount()) {
            // Szukamy komórek w lewej macierzy, których numer kolumny jest zgodny
            // z numerem wiersza komórki w prawej macierzy.
            if (columnSorted[x].column == matrix.getCell(y).row) {
                // Kiedy znajdziemy "pasujące komórki", wyznaczamy przedziały komórek na których
                // wszystkie komórki mają tę samą kolumnę/wiersz (odpowiednio dla lewej/prawej macierzy).
                // Następnie wymnażamy wszystkie pary komórek z tych przedziałów (po jednej z przedziału).
                yBegin = y;
                while (y < matrix.cellCount() && columnSorted[x].column == matrix.getCell(y).row) {
                    y++;
                }
                xBegin = x;
                while (x < cellCount() && columnSorted[x].column == matrix.getCell(y - 1).row) {
                    x++;
                }
                for (int i = xBegin; i < x; i++) {
                    for (int j = yBegin; j < y; j++) {
                        cells.add(new MatrixCellValue(columnSorted[i].row, matrix.getCell(j).column,
                                columnSorted[i].value * matrix.getCell(j).value));
                    }
                }
            } else if (columnSorted[x].column < matrix.getCell(y).row) {
                x++;
            } else {
                y++;
            }
        }

        MatrixCellValue[] result = new MatrixCellValue[cells.size()];

        for (int i = 0; i < cells.size(); i++) {
            result[i] = cells.get(i);
        }

        return SparseCompressed(Shape.matrix(shape().rows, matrix.shape().columns), result);
    }

    @Override
    protected Matrix multiplyReverseSparse(Sparse matrix) {
        return matrix.multiplySparse(this);
    }

    @Override
    protected Matrix multiplyReverseRow(Row matrix) {
        double[] values = new double[shape().columns];

        for (int i = 0; i < cellCount(); i++) {
            values[getCell(i).column] += matrix.get(getCell(i).row) * getCell(i).value;
        }

        return new Row(matrix.shape().rows, values);
    }

    @Override
    protected Matrix multiplyReverseConstant(Constant matrix) {
        double[] values = new double[shape().columns];

        for (int i = 0; i < cellCount(); i++) {
            values[getCell(i).column] += matrix.get(0, 0) * getCell(i).value;
        }

        return new Row(matrix.shape().rows, values);
    }

    @Override
    protected Matrix multiplyReverseFull(Full matrix) {
        ArrayList<MatrixCellValue> cells = new ArrayList<>();

        for (int i = 0; i < cellCount(); i++) {
            for (int j = 0; j < matrix.shape().rows; j++) {
                cells.add(new MatrixCellValue(j, getCell(i).column, matrix.get(j, getCell(i).row) * getCell(i).value));
            }
        }

        MatrixCellValue[] result = new MatrixCellValue[cells.size()];

        for (int i = 0; i < cells.size(); i++) {
            result[i] = cells.get(i);
        }

        return Sparse.SparseCompressed(Shape.matrix(matrix.shape().rows, shape().columns), result);
    }

    @Override
    protected Matrix multiplyReverseColumn(Column matrix) {
        ArrayList<MatrixCellValue> cells = new ArrayList<>();

        for (int i = 0; i < cellCount(); i++) {
            for (int j = 0; j < matrix.shape().rows; j++) {
                cells.add(new MatrixCellValue(j, getCell(i).column, matrix.get(j) * getCell(i).value));
            }
        }

        MatrixCellValue[] result = new MatrixCellValue[cells.size()];

        for (int i = 0; i < cells.size(); i++) {
            result[i] = cells.get(i);
        }

        return Sparse.SparseCompressed(Shape.matrix(matrix.shape().rows, shape().columns), result);
    }

    @Override
    public Matrix multiplyReversePermutation(Permutation matrix) {
        MatrixCellValue[] cells = new MatrixCellValue[cellCount()];

        for (int i = 0; i < cellCount(); i++) {
            cells[i] = new MatrixCellValue(matrix.rowFromColumn(getCell(i).row), getCell(i).column,
                    matrix.getFromColumn(getCell(i).row) * getCell(i).value);
        }

        return new Sparse(Shape.matrix(matrix.shape().rows, shape().columns), cells);
    }
}
