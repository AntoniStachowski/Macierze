package com.company.test;

import com.company.matrix.IDoubleMatrix;

import static com.company.utils.DoubleMatrixFactory.*;
import static com.company.utils.MatrixCellValue.cell;
import static com.company.utils.Shape.matrix;

public class TestMatrixData {

  private TestMatrixData() {
  }

  public static final IDoubleMatrix FULL_2X3 = full(new double[][]{
    new double[]{1, 2, 3},
    new double[]{4, 5, 6}
  });

  public static final IDoubleMatrix FULL_3X2 = full(new double[][]{
    new double[]{1, 2},
    new double[]{3, 4},
    new double[]{5, 6}
  });

  public static final IDoubleMatrix DIAGONAL_3X3 = diagonal(7, 8, 9);

  public static final IDoubleMatrix ANTI_DIAGONAL_3X3 = antiDiagonal(10, 11, 12);

  public static final IDoubleMatrix SPARSE_2X3 = sparse(matrix(2, 3),
    cell(0, 0, 1),
    cell(0, 1, 2),
    cell(0, 2, 3),
    cell(1, 0, 4),
    cell(1, 1, 5),
    cell(1, 2, 6)
  );

  public static final IDoubleMatrix SPARSE_3X2 = sparse(matrix(3, 2),
    cell(0, 0, 1),
    cell(0, 1, 2),
    cell(1, 0, 3),
    cell(1, 1, 4),
    cell(2, 0, 5),
    cell(2, 1, 6)
  );

  public static final IDoubleMatrix VECTOR_3 = vector(15, 16, 17);

  public static final IDoubleMatrix VECTOR_2 = vector(18, 19);

  public static final IDoubleMatrix ID_2 = identity(2);

  public static final IDoubleMatrix ID_3 = identity(3);

  public static final IDoubleMatrix ZERO_3X2 = zero(matrix(3, 2));
}
