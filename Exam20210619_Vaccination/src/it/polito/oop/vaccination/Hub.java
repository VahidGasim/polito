package it.polito.oop.vaccination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hub {
	private String name;
	private Staff staff;
	private List<String> times;
    int capacity;
    

	public Hub(String name) {
		this.name = name;
		times = new ArrayList<>();
	}
	
	 void setCapacity() {
	        this.capacity = Math.min(staff.getDoctors()*10,Math.min(staff.getCountNurse()*12,staff.getO()*20));
	 }
	 

	public String getName() {
		return name;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	
	public void addTime(String time) {
		times.add(time);
	}
	
	public List<String> getTimes() {
		return times;
	}
	
	

}
