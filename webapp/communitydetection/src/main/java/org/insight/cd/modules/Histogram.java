package org.insight.cd.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Histogram {

	private Integer[] bins;
	private static final int MIN = 0;
	private static final int BIN_SIZE = 10;
	private static final int NUM_BINS = 10;

	public Histogram() {
		this.bins = new Integer[NUM_BINS + 1];
		for (int i = 0; i < NUM_BINS + 1; i++) {
			this.bins[i] = 0;
		}
	}

	public void addDataPoint(int point) {
		int index = point / BIN_SIZE;

		if (index > BIN_SIZE - 1) {
			this.bins[BIN_SIZE]++;
		} else {
			this.bins[index]++;
		}
	}

	public List<String> getCategoryLabels() {
		List<String> labels = new ArrayList<>();
		String label = MIN + "";
		int current = MIN;
		for (int i = 0; i < NUM_BINS; i++) {
			label += "-" + (current + BIN_SIZE);
			
			labels.add(label);
			
			current += BIN_SIZE;
			label = current + "";
		}
		labels.add(current+" & more");
		return labels;
	}

	public List<Integer> getBins() {
		return Arrays.asList(this.bins);
	}
}
