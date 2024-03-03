package it.polito.oop.vaccination;

public class Staff {
	private int doctors;
	private int countNurse;
	private int o;
	
	
	public Staff(int doctors, int countNurse, int o) {
		this.doctors = doctors;
		this.countNurse = countNurse;
		this.o = o;
	}


	public int getDoctors() {
		return doctors;
	}


	public int getCountNurse() {
		return countNurse;
	}


	public int getO() {
		return o;
	}


	public void setDoctors(int doctors) {
		this.doctors = doctors;
	}


	public void setCountNurse(int countNurse) {
		this.countNurse = countNurse;
	}


	public void setO(int o) {
		this.o = o;
	}
	
	
	

}
