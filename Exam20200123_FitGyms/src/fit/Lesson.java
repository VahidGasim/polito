package fit;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
	Gym gym; 
    String activity; 
    int maxattendees; 
    int slot;
    int day;
    String[] allowedinstructors;
    private List<Customer> customers = new ArrayList<>();
    private String instructor = null;
    
	public Lesson(Gym gym, String activity, int maxattendees, int day, int slot, String[] allowedinstructors) {
		this.gym = gym;
		this.activity = activity;
		this.maxattendees = maxattendees;
		this.day = day;
		this.slot = slot;
		this.allowedinstructors = allowedinstructors;
	}

	public Gym getGym() {
		return gym;
	}

	public String getActivity() {
		return activity;
	}

	public int getMaxattendees() {
		return maxattendees;
	}

	public int getSlot() {
		return slot;
	}

	public int getDay() {
		return day;
	}

	public String[] getAllowedinstructors() {
		return allowedinstructors;
	}
    
    public void addCustomer(Customer customer) {
    	customers.add(customer);
    }
    
    
    
	public List<Customer> getCustomers() {
		return customers;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

    
    
}
