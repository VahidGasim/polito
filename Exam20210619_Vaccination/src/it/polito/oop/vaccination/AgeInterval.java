package it.polito.oop.vaccination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgeInterval implements Comparable<AgeInterval>{
	private int[] breaks;
	private List<String> intervals;
	private int start=0;

	public AgeInterval(int[] breaks) {
		this.breaks = breaks;
		intervals = new ArrayList<>();
	}
	

	public void defineIntervals() {
		for(int b: breaks) {
			String x = String.format("[%d,%d)", start, b);
			start = b;
			intervals.add(x);
			if(b == (breaks[breaks.length-1])) {
				x = String.format("[%d,+)", b);
				intervals.add(x);
			}
		}
	}

	public List<String> getIntervals() {
		return intervals;
	}
	

	public boolean notIn(int i) {
		for(String s: intervals) {
			String[] x = s.split(",");
			int bir = Integer.parseInt(x[0].replace("[", ""));
			String iki = x[1].replace(")", "");
			int iki2 = 1000;
			if(!iki.equals("+")) {
				iki2 = Integer.parseInt(iki);
			}
			if(bir<=i && iki2>i ) return true;
		}
		return false;
	}
	
	
	
	@Override
	public String toString() {
		String z = null;
		for(String x: intervals) {
			z=x;
		}
		return z;
	}


	@Override
	public int compareTo(AgeInterval o) {
		return this.compareTo(o);
	}
	
}
