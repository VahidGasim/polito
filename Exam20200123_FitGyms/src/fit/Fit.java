package fit;

import java.util.*;
import java.util.stream.Collectors;


public class Fit {
	private Map<String, Gym> gyms = new HashMap<>();
	private Map<String, Lesson> lessons = new HashMap<>();
	private Map<Integer, Customer> customers = new HashMap<>();
	private int id=0;
    
    public static int MONDAY    = 1;
    public static int TUESDAY   = 2;
    public static int WEDNESDAY = 3;
    public static int THURSDAY  = 4;
    public static int FRIDAY    = 5;
    public static int SATURDAY  = 6;
    public static int SUNDAY    = 7;
    
	public Fit() {
	
	}
	// R1 
	
	public void addGymn (String name) throws FitException {
		if(gyms.containsKey(name)) throw new FitException();
		gyms.put(name, new Gym(name));
	}
	
	public int getNumGymns() {
		return gyms.size();
	}
	
	//R2

	public void addLessons (String gymnname, 
	                        String activity, 
	                        int maxattendees, 
	                        String slots, 
	                        String ...allowedinstructors) throws FitException{
	    if(!gyms.containsKey(gymnname)) throw new FitException();
		Gym gym = gyms.get(gymnname);
		String[] z = slots.split(",");
		for(String s: z) {
			String[] x = s.split("\\.");
			int day = Integer.parseInt(x[0]);
			int slot = Integer.parseInt(x[1]);
			if(day<1 || day>7 || slot<8 || slot>20) throw new FitException();
			Lesson lesson = new Lesson(gym, activity, maxattendees, day, slot, allowedinstructors);
			lessons.put(lesson.getDay()+"."+lesson.getSlot(), lesson);
			gym.addLessons(lesson);
		}
	}
	
	//R3
	public int addCustomer(String name) {
		customers.put(++id, new Customer(id, name));
		return id;
	}
	
	public String getCustomer (int customerid) throws FitException{
		if(!customers.containsKey(customerid)) throw new FitException();
	    return customers.get(customerid).getName();
	}
	
	//R4
	
	public void placeReservation(int customerid, String gymnname, int day, int slot) throws FitException{
		if(!customers.containsKey(customerid)) throw new FitException();
	    if(!gyms.containsKey(gymnname)) throw new FitException();
	    if(day<1 || day>7 || slot<8 || slot>20) throw new FitException();
	    Customer customer = customers.get(customerid);
	    Gym gym = gyms.get(gymnname);
	    gym.getLesson(day, slot).addCustomer(customer);
	    customer.addLesson(gym.getLesson(day, slot));
	    
	}
	
	public int getNumLessons(int customerid) {
	    Customer customer = customers.get(customerid);
		return customer.getLessons().size();
	}
	
	
	//R5
	
	public void addLessonGiven (String gymnname, int day, int slot, String instructor) throws FitException{
		if(!gyms.containsKey(gymnname)) throw new FitException();
	    if(day<1 || day>7 || slot<8 || slot>20) throw new FitException();
	    Gym gym = gyms.get(gymnname);
	    Lesson lesson = gym.getLesson(day, slot);
	    lesson.setInstructor(instructor);
	}
	
	public int getNumLessonsGiven (String gymnname, String instructor) throws FitException {
		if(!gyms.containsKey(gymnname)) throw new FitException();
	    Gym gym = gyms.get(gymnname);
	    return gym.numLessons(instructor);
	}
	//R6
	
	public String mostActiveGymn() {
		Gym g = gyms.values().stream().max((g1,g2)-> Integer.compare(g1.getLessons().size(), g2.getLessons().size())).get();
		return g.getName();
	}
	
	public Map<String, Integer> totalLessonsPerGymn() {	
		return gyms.values().stream().collect(Collectors.toMap(g->g.getName(), g->g.getLessons().size()));
	}
	
	public SortedMap<Integer, List<String>> slotsPerNofParticipants(String gymnname) throws FitException{
		Gym g = gyms.get(gymnname);
		return g.getLessons().values().stream().collect(Collectors.groupingBy(l->l.getCustomers().size(), TreeMap::new, 
				Collectors.mapping(l->l.getDay()+"."+l.getSlot(), Collectors.toList())));
	}
	

	
	
	
	


}
