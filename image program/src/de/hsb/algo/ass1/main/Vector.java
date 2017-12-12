package de.hsb.algo.ass1.main;

public class Vector {
	private int[] m_values = new int[3];

	Vector(int x, int y) {
		m_values[0] = x;
		m_values[1] = y;
		m_values[2] = 1;
	}

	public int x() {
		return m_values[0];
	}

	public int y() {
		return m_values[1];
	}

	public int ith(int i) {
		return m_values[i];
	}
}
