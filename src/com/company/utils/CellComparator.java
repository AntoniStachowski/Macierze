package com.company.utils;

import java.util.Comparator;

public class CellComparator implements Comparator<MatrixCellValue> {
    private static CellComparator rowComparator = null;
    private static CellComparator columnComparator = null;

    boolean order;

    public static CellComparator giveRowComparator() {
        if(rowComparator == null) {
            rowComparator = new CellComparator(false);
        }
        return rowComparator;
    }

    public static CellComparator giveColumnComparator() {
        if(columnComparator == null) {
            columnComparator = new CellComparator(true);
        }
        return columnComparator;
    }

    private CellComparator(boolean order){
        this.order = order;
    }

    public int compare(MatrixCellValue cell1, MatrixCellValue cell2) {
        if (cell1.row == cell2.row && cell1.column == cell2.column) {
            return 0;
        }
        if(order == false) {
            if(cell1.row > cell2.row || (cell1.row == cell2.row && cell1.column > cell2.column)) {
                return 1;
            }
        } else {
            if(cell1.column > cell2.column || (cell1.column == cell2.column && cell1.row > cell2.row)) {
                return 1;
            }
        }
        return -1;
    }
}
