package timesheet;

import java.util.ArrayList;
import java.util.List;

public class Worker {
	String name;
	String surname;
	Profile profile;
	private String id;
	private List<Report> reports = new ArrayList<>();
	
	public Worker(String id, String name, String surname, Profile profile) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.profile = profile;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public Profile getProfile() {
		return profile;
	}

	public String getId() {
		return id;
	}
	
	public List<Report> getReports() {
		return reports;
	}
	
	public void addReport(Report r) {
		reports.add(r);
	}
	

	@Override
	public String toString() {
		return name + " " + surname + " (" + profile.toString() + ")";
	}
	
	

}
