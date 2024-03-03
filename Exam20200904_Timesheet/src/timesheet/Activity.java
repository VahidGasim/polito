package timesheet;

public class Activity {
	Project project;
	String activityName;
	private boolean closed = false;
	
	
	public Activity(Project project, String activityName) {
		this.project = project;
		this.activityName = activityName;
	}


	public Project getProject() {
		return project;
	}


	public String getActivityName() {
		return activityName;
	}
	
	public void close() {
		closed = true;
	}


	public boolean isClosed() {
		return closed;
	}
	
	
}
