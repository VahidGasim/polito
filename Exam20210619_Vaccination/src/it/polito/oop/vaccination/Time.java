package it.polito.oop.vaccination;

public class Time {
	private int h,m;

	public Time(int h, int m) {
		this.h = h;
		this.m = m;
	}

	public int getH() {
		return h;
	}

	public int getM() {
		return m;
	}
	
	public void makeInterval(int time) {
		m+=time;
		if(m==60) {
			m=0;
			h+=1;
		}
		if(h==24) {
			h=0;
		}
	}

	@Override
	public String toString() {
		return String.format("%02d:%02d", h, m);
	}
	
	

}
