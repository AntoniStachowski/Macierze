package com.company.testAS;

import com.company.matrix.IDoubleMatrix;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static com.company.testAS.TestMatrixData.*;
import static org.junit.jupiter.params.provider.Arguments.of;

public class TestMatrixArgumentProvider implements ArgumentsProvider {

    private static IDoubleMatrix give(int i) {
        switch (i) {
            case 0:
                return FULL;
            case 1:
                return SPARSE;
            case 2:
                return ANTIDIAONAL;
            case 3:
                return DIAGONAL;
            case 4:
                return ROW;
            case 5:
                return COLUMN;
            case 6:
                return CONSTANT;
            case 7:
                return IDENTITY;
            case 8:
                return ZERO;
            case 9:
                return FULL2x3;
            case 10:
                return SPARSE2x3;
            case 11:
                return COLUMN2x3;
            case 12:
                return CONSTANT2x3;
            case 13:
                return ROW2x3;
            case 14:
                return ZERO2x3;
            case 15:
                return FULL3x2;
            case 16:
                return SPARSE3x2;
            case 17:
                return COLUMN3x2;
            case 18:
                return CONSTANT3x2;
            case 19:
                return ROW3x2;
            case 20:
                return ZERO3x2;
            case 21:
                return VECTOR2;
            default:
                return VECTOR3;

        }
    }

    //Tworzymy stream wszystkich możliwych par macierzy z TestMatrixData.
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Arguments[] args = new Arguments[484];

        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 22; j++) {
                args[22 * i + j] = of(give(i), give(j));
            }
        }

        return Stream.of(args);
    }

}
