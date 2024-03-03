package timesheet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Timesheet {
	private List<Integer> holidays = new ArrayList<>();
	private int firstweekDay = 1;
	private Map<String, Project> projects = new HashMap<>();
	private Map<String, Activity> activities = new HashMap<>();
	private Map<String, Profile> profiles = new HashMap<>();
	private int id=0;
	private Map<String, Worker> workers = new HashMap<>();
	private int wid=0;
	private List<Report> reports = new ArrayList<>();



	// R1
	public void setHolidays(int... holidays) {
		for(int h: holidays) {
			if(h>0)
				this.holidays.add(h);
		}
	}
	
	public boolean isHoliday(int day) {
		if(holidays.contains(day)) return true;
		return false;
	}
	
	public void setFirstWeekDay(int weekDay) throws TimesheetException {
		if(weekDay<0 || weekDay>6) throw new TimesheetException();
		firstweekDay = weekDay;
	}
	
	public int getWeekDay(int day) throws TimesheetException {
	    return (firstweekDay + day -1) % 7;
	}
	
	// R2
	public void createProject(String projectName, int maxHours) throws TimesheetException {
		if(maxHours<0) throw new TimesheetException();
		if(!projects.containsKey(projectName)) {
			Project project = new Project(projectName, maxHours);
			projects.put(projectName, project);
		}
		Project p = projects.get(projectName);
		p.setMaxHours(maxHours);
	}
	
	public List<String> getProjects() {
		return projects.values().stream().sorted(Comparator.comparing(Project::getMaxHours).reversed()
				.thenComparing(Project::getProjectName)).map(Project::toString).collect(Collectors.toList());
	}
	
	public void createActivity(String projectName, String activityName) throws TimesheetException {
		if(!projects.containsKey(projectName)) throw new TimesheetException();
		Project p = projects.get(projectName);
		Activity activity = new Activity(p, activityName);
		activities.put(activityName, activity);
		p.addActivity(activity);
	}
	
	public void closeActivity(String projectName, String activityName) throws TimesheetException {
		if(!projects.containsKey(projectName)) throw new TimesheetException();
		Activity a = activities.get(activityName);
		a.close();
	}
	
	public List<String> getOpenActivities(String projectName) throws TimesheetException {
		if(!projects.containsKey(projectName)) throw new TimesheetException();
		return projects.values().stream().flatMap(p->p.getActivity().stream()).filter(a->a.isClosed()==false)
			.map(Activity::getActivityName).sorted().collect(Collectors.toList());
	}

	// R3
	public String createProfile(int... workHours) throws TimesheetException {
		if(workHours.length!=7) throw new TimesheetException();
		String uid = "Profile" + (id++);
		Profile p = new Profile(uid, workHours);
		profiles.put(uid, p);
        return uid;
	}
	
	public String getProfile(String profileID) throws TimesheetException {
		if(!profiles.containsKey(profileID)) throw new TimesheetException();
		return profiles.get(profileID).toString();
	}
	
	public String createWorker(String name, String surname, String profileID) throws TimesheetException {
		if(!profiles.containsKey(profileID)) throw new TimesheetException();
		String uid = "Profile" + (wid++);
		Profile p = profiles.get(profileID);
		Worker w = new Worker(uid, name, surname, p);
		workers.put(uid, w);
        return uid;
	}
	
	public String getWorker(String workerID) throws TimesheetException {
		if(!workers.containsKey(workerID)) throw new TimesheetException();
        return workers.get(workerID).toString();
	}
	
	// R4
	public void addReport(String workerID, String projectName, String activityName, int day, int workedHours) throws TimesheetException {
		if(!workers.containsKey(workerID)) throw new TimesheetException();
		if(!projects.containsKey(projectName)) throw new TimesheetException();
		if(!activities.containsKey(activityName)) throw new TimesheetException();
		if(day<0 || day>366) throw new TimesheetException();
		if(workedHours<0) throw new TimesheetException();
		Worker w = workers.get(workerID);
		Project p = projects.get(projectName);
		Activity a = activities.get(activityName);
		Report r = new Report(w, p, a, day, workedHours);
		p.addReport(r);
		w.addReport(r);
		reports.add(r);
	}
	
	public int getProjectHours(String projectName) throws TimesheetException {
		if(!projects.containsKey(projectName)) throw new TimesheetException();
		Project p = projects.get(projectName);
		return p.getReports().stream().collect(Collectors.summingInt(Report::getWorkedHours));
	}
	
	public int getWorkedHoursPerDay(String workerID, int day) throws TimesheetException {
		if(!workers.containsKey(workerID)) throw new TimesheetException();
		if(day<0 || day>366) throw new TimesheetException();
		Worker w = workers.get(workerID);
		return w.getReports().stream().filter(r->r.getDay() == day).collect(Collectors.summingInt(Report::getWorkedHours));
	}
	
	// R5
	public Map<String, Integer> countActivitiesPerWorker() {
		return reports.stream().collect(Collectors.groupingBy(
				x -> x.getWorker().getId(),
				Collectors.collectingAndThen(Collectors.mapping(Report::getActivity, Collectors.toSet()), x -> x.size())));
	}
	
	public Map<String, Integer> getRemainingHoursPerProject() {
		return reports.stream()
				.collect(Collectors.groupingBy(
						Report::getProject,
						Collectors.summingInt(Report::getWorkedHours)))
				.entrySet().stream()
				.collect(Collectors.groupingBy(
						x -> x.getKey().getProjectName(),
						Collectors.mapping(x -> x.getKey().getMaxHours() - x.getValue(),
								Collectors.summingInt(x -> x))
						));
	}
	
	public Map<String, Map<String, Integer>> getHoursPerActivityPerProject() {
        return null;
	}
}
