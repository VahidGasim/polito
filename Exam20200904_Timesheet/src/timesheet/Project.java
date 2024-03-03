package timesheet;

import java.util.ArrayList;
import java.util.List;

public class Project {
	String projectName;
	int maxHours;
	private List<Activity> activity = new ArrayList<>();
	private List<Report> reports = new ArrayList<>();
	
	
	public Project(String projectName, int maxHours) {
		this.projectName = projectName;
		this.maxHours = maxHours;
	}


	public String getProjectName() {
		return projectName;
	}


	public int getMaxHours() {
		return maxHours;
	}


	public void setMaxHours(int maxHours) {
		this.maxHours = maxHours;
	}
	
	

	public List<Activity> getActivity() {
		return activity;
	}


	public void addActivity(Activity activity) {
		this.activity.add(activity);
	}


	@Override
	public String toString() {
		return projectName+": "+ maxHours;
	}


	public List<Report> getReports() {
		return reports;
	}
	
	public void addReport(Report r) {
		reports.add(r);
	}
	
	
}
