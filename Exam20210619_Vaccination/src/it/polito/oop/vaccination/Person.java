package it.polito.oop.vaccination;

public class Person {
	private String firstName;
	private String lastName;
	private String ssn;
	private int year;
	private AgeInterval interval;
	private boolean allocated = false;
	
	
	public Person(String firstName, String lastName, String ssn, int year) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.ssn = ssn;
		this.year = year;
	}

	public boolean getAllocate() {
	       return allocated;
	}
	public void SetAllocate() {
	       allocated = true;
	}
	public void CancelAllocate() {
	       allocated = false;
	}
	public void SetInterval(AgeInterval interval){
		this.interval = interval;
	}

	public AgeInterval getInterval() {
	    return interval;
	}
	
	public String getFirstName() {
		return firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public String getSsn() {
		return ssn;
	}


	public int getYear() {
		return year;
	}


	@Override
	public String toString() {
		return  ssn + ", " + lastName + ", " + firstName;
	}
	
	
	
	
	

}
