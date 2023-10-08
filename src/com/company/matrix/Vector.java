package com.company.matrix;


public class Vector extends Full {

    public Vector(double... values) {
        assert (values != null);
        assert (values.length > 0);

        double[][] valuesCopy = new double[values.length][1];
        for (int i = 0; i < values.length; i++) {
            valuesCopy[i][0] = values[i];
        }

        setShape(values.length, 1);
        setValues(valuesCopy);
    }

    protected Vector(double[][] values) {
        setShape(values.length, 1);
        setValues(values);
    }

    @Override
    protected Matrix construct(double[][] values) {
        return new Vector(values);
    }

    public double get(int x) {
        return get(x, 0);
    }

    protected Matrix add(Matrix matrix) {
        return matrix.addVector(this);
    }

    @Override
    protected Matrix addSparse(Sparse matrix) {
        return matrix.addVector(this);
    }

    public Matrix multiply(Matrix matrix) {
        return matrix.multiplyReverseVector(this);
    }
}
