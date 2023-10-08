package com.company.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;
import static com.company.test.TestMatrixData.*;

public class TestMatrixTransposedShapeArgumentProvider implements ArgumentsProvider {

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
    return Stream.of(
      of(FULL_2X3, FULL_3X2),
      of(FULL_2X3, SPARSE_3X2),
      of(SPARSE_2X3, FULL_3X2),
      of(SPARSE_2X3, SPARSE_3X2)
    );
  }
}
