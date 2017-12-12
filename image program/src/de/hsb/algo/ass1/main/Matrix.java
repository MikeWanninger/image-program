package de.hsb.algo.ass1.main;

public class Matrix {

	public double[][] m_values;

	private Matrix(double[][] values) {
		m_values = values;
	}

	static Matrix translation(int dx, int dy) {
		double d[][] = { { 1, 0, 0 }, { 0, 1, 0 }, { -dx, -dy, 1 } };
		return new Matrix(d);
	}

	static Matrix rotate(double a) {
		double d[][] = { { Math.cos(a), Math.sin(a), 0 },
				{ Math.sin(a), -Math.cos(a), 0 }, { 0, 0, 1 } };
		return new Matrix(d);
	}

	static Matrix scale(double x, double y) {
		double d[][] = { { 1 / x, 0, 0 }, { 0, 1 / y, 0 }, { 0, 0, 1 } };
		return new Matrix(d);
	}

	static Matrix shearX(double x) {
		double d[][] = { { 1, 0, 0 }, { -x, 1, 0 }, { 0, 0, 1 } };
		return new Matrix(d);
	}

	static Matrix shearY(double y) {
		double d[][] = { { 1, -y, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
		return new Matrix(d);
	}

	public static Matrix multiplyMM(Matrix a, Matrix b) {
		double[][] res = new double[3][3];
		for (int i = 0; i < a.m_values.length; i++) {
			for (int j = 0; j < a.m_values[i].length; j++) {
				for (int z = 0; z < 3; z++) {
					res[i][j] += a.m_values[z][j] * b.m_values[i][z];
				}
			}
		}
		return new Matrix(res);
	}

	public static Vector multiplyMV(Matrix a, Vector v) {
		int[] res = new int[a.m_values[0].length];
		for (int i = 0; i < a.m_values[0].length; i++) {
			for (int j = 0; j < a.m_values.length; j++) {
				res[i] += a.m_values[j][i] * v.ith(j);
			}
		}
		return new Vector(res[0], res[1]);
	}

}
