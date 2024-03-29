package it.polito.oop.vaccination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Vaccines {
	private Map<String, Person> persons = new HashMap<>();
	private List<AgeInterval> ageintervals = new ArrayList<>();
	private List<Interval> intervals = new ArrayList<>();
	private Map<String, Hub> hubs = new HashMap<>();
	private int n = 0;
	private Map<Integer, Integer> hours = new HashMap<>();
	private int next = 0;
	private Map<AgeInterval, List<Person>> intervalswithpeople = new HashMap<>();
	
	private BiConsumer<Integer,String> biConsumer=null;

	public final static int CURRENT_YEAR = java.time.LocalDate.now().getYear();

	// R1
	/**
	 * Add a new person to the vaccination system.
	 *
	 * Persons are uniquely identified by SSN (italian "codice fiscale")
	 *
	 * @param firstName first name
	 * @param lastName  last name
	 * @param ssn       italian "codice fiscale"
	 * @param year      birth year
	 * @return {@code false} if ssn is duplicate,
	 */
	public boolean addPerson(String firstName, String lastName, String ssn, int year) {
		if (!persons.containsKey(ssn)) {
			persons.put(ssn, new Person(firstName, lastName, ssn, year));
			return true;
		}

		return false;
	}

	/**
	 * Count the number of people added to the system
	 *
	 * @return person count
	 */
	public int countPeople() {
		return persons.size();
	}

	/**
	 * Retrieves information about a person. Information is formatted as ssn, last
	 * name, and first name separate by {@code ','} (comma).
	 *
	 * @param ssn "codice fiscale" of person searched
	 * @return info about the person
	 */
	public String getPerson(String ssn) {
		Person person = persons.get(ssn);
		return person.toString();
	}

	/**
	 * Retrieves of a person given their SSN (codice fiscale).
	 *
	 * @param ssn "codice fiscale" of person searched
	 * @return age of person (in years)
	 */
	public int getAge(String ssn) {
		Person person = persons.get(ssn);
		return CURRENT_YEAR - person.getYear();
	}

	/**
	 * Define the age intervals by providing the breaks between intervals. The first
	 * interval always start at 0 (non included in the breaks) and the last interval
	 * goes until infinity (not included in the breaks). All intervals are closed on
	 * the lower boundary and open at the upper one.
	 * <p>
	 * For instance {@code setAgeIntervals(40,50,60)} defines four intervals
	 * {@code "[0,40)", "[40,50)", "[50,60)", "[60,+)"}.
	 *
	 * @param breaks the array of breaks
	 */
	public void setAgeIntervals(int... breaks) {
		AgeInterval inter = new AgeInterval(breaks);
		ageintervals.add(inter);
		inter.defineIntervals();
		intervals.add(new Interval(0,breaks[0])); 
	    for(int i=0;i<breaks.length;i++) {
	    	if(i==breaks.length-1) {
	    		intervals.add(new Interval(breaks[i],10000));
	    	}else
	    		intervals.add(new Interval(breaks[i],breaks[i+1]));}
		for(AgeInterval i : ageintervals) {
			persons.values().stream().filter(e->i.notIn(CURRENT_YEAR - e.getYear())).forEach(e->e.SetInterval(i));
		}
	}

	/**
	 * Retrieves the labels of the age intervals defined.
	 *
	 * Interval labels are formatted as {@code "[0,10)"}, if the upper limit is
	 * infinity {@code '+'} is used instead of the number.
	 *
	 * @return labels of the age intervals
	 */
	public Collection<String> getAgeIntervals() {
		return ageintervals.stream().flatMap(a -> a.getIntervals().stream()).collect(Collectors.toList());
	}

	/**
	 * Retrieves people in the given interval.
	 *
	 * The age of the person is computed by subtracting the birth year from current
	 * year.
	 *
	 * @param range age interval label
	 * @return collection of SSN of person in the age interval
	 */
	public Collection<String> getInInterval(String range) {
		String[] r = range.split(",");
		int bir = Integer.parseInt(r[0].replace("[", ""));
		String iki = r[1].replace(")", "");
		if (!iki.equals("+")) {
			int iki2 = Integer.parseInt(iki);
			return persons.values().stream()
					.filter(p -> (CURRENT_YEAR - p.getYear()) >= bir && (CURRENT_YEAR - p.getYear()) < iki2)
					.map(Person::getSsn).distinct().collect(Collectors.toList());
		}
		return persons.values().stream().filter(p -> (CURRENT_YEAR - p.getYear()) >= bir).map(Person::getSsn).distinct()
				.collect(Collectors.toList());
	}

	// R2
	/**
	 * Define a vaccination hub
	 *
	 * @param name name of the hub
	 * @throws VaccineException in case of duplicate name
	 */
	public void defineHub(String name) throws VaccineException {
		if (hubs.containsKey(name))
			throw new VaccineException("Name dublicated...");
		hubs.put(name, new Hub(name));

	}

	/**
	 * Retrieves hub names
	 *
	 * @return hub names
	 */
	public Collection<String> getHubs() {
		return hubs.values().stream().map(Hub::getName).collect(Collectors.toList());
	}

	/**
	 * Define the staffing of a hub in terms of doctors, nurses and other personnel.
	 *
	 * @param name       name of the hub
	 * @param doctors    number of doctors
	 * @param countNurse number of nurses
	 * @param o          number of other personnel
	 * @throws VaccineException in case of undefined hub, or any number of personnel
	 *                          not greater than 0.
	 */
	public void setStaff(String name, int doctors, int countNurse, int o) throws VaccineException {
		if (!hubs.containsKey(name))
			throw new VaccineException("Name not exist...");
		if (doctors <= 0 || countNurse <= 0 || o <= 0)
			throw new VaccineException("No 0...");
		Hub hub = hubs.get(name);
		Staff staff = new Staff(doctors, countNurse, o);
		hub.setStaff(staff);
	}

	/**
	 * Estimates the hourly vaccination capacity of a hub
	 *
	 * The capacity is computed as the minimum among 10*number_doctor,
	 * 12*number_nurses, 20*number_other
	 *
	 * @param hubName name of the hub
	 * @return hourly vaccination capacity
	 * @throws VaccineException in case of undefined or hub without staff
	 */
	public int estimateHourlyCapacity(String hubName) throws VaccineException {
		if (!hubs.containsKey(hubName))
			throw new VaccineException("Name not exist...");
		Hub hub = hubs.get(hubName);
		if (hubs.get(hubName).getStaff() == null)
			throw new VaccineException("Staff not exist...");
		return (int) Math.min(10 * hub.getStaff().getDoctors(),
				Math.min(12 * hub.getStaff().getCountNurse(), 20 * hub.getStaff().getO()));
	}

	// R3
	/**
	 * Load people information stored in CSV format.
	 *
	 * The header must start with {@code "SSN,LAST,FIRST"}. All lines must have at
	 * least three elements.
	 *
	 * In case of error in a person line the line is skipped.
	 *
	 * @param people {@code Reader} for the CSV content
	 * @return number of correctly added people
	 * @throws IOException      in case of IO error
	 * @throws VaccineException in case of error in the header
	 */
	public long loadPeople(Reader people) throws IOException, VaccineException {
		String line = "";
		int n=0;
		// Hint:
		BufferedReader br = new BufferedReader(people);
		if (!br.readLine().contains("SSN,LAST,FIRST,YEAR"))
			throw new VaccineException("SSN,LAST,FIRST,YEAR not correct..");
		while (((line = br.readLine()) != null)) {
			String[] p = line.split(",");
			n++;
			if (p.length != 4)
				if(biConsumer!=null) biConsumer.accept(n, line);
				if (persons.containsKey(p[0])) {
					if(biConsumer!=null) biConsumer.accept(n, line);
				}
			if (!persons.containsKey(p[0]) && p.length == 4) {
				int year = Integer.parseInt(p[3]);
				addPerson(p[2], p[1], p[0], year);
				n++;
			}
		}

		br.close();
		return n;
	}

	// R4
	/**
	 * Define the amount of working hours for the days of the week.
	 *
	 * Exactly 7 elements are expected, where the first one correspond to Monday.
	 *
	 * @param hours workings hours for the 7 days.
	 * @throws VaccineException if there are not exactly 7 elements or if the sum of
	 *                          all hours is less than 0 ore greater than 24*7.
	 */
	public void setHours(int... hours) throws VaccineException {
		for (int h : hours) {
			if (h > 12 || hours.length != 7)
				throw new VaccineException("hours are not matching");
			this.hours.put(next++, h);
			Time start = new Time(9, 0);
			for(Hub hub: hubs.values()) {
				while (start.getH() < (h+9)) {
					hub.addTime(start.toString());
					start.makeInterval(15);
				}
			}
		}
	}

	/**
	 * Returns the list of standard time slots for all the days of the week.
	 *
	 * Time slots start at 9:00 and occur every 15 minutes (4 per hour) and they
	 * cover the number of working hours defined through method {@link #setHours}.
	 * <p>
	 * Times are formatted as {@code "09:00"} with both minuts and hours on two
	 * digits filled with leading 0.
	 * <p>
	 * Returns a list with 7 elements, each with the time slots of the corresponding
	 * day of the week.
	 *
	 * @return the list hours for each day of the week
	 */
	public List<List<String>> getHours() {
		List<List<String>> res = new ArrayList<>();
		for (int t : hours.values()) {
			List<String> r = new ArrayList<>();
			Time start = new Time(9, 0);
			while (start.getH() < (t+9)) {
				r.add(start.toString());
				start.makeInterval(15);
			}
			res.add(r);
		}
		return res;
	}

	/**
	 * Compute the available vaccination slots for a given hub on a given day of the
	 * week
	 * <p>
	 * The availability is computed as the number of working hours of that day
	 * multiplied by the hourly capacity (see {@link #estimateCapacity} of the hub.
	 *
	 * @return
	 */
	public int getDailyAvailable(String hubName, int d) {
		Hub hub = hubs.get(hubName);
		int day = hours.get(d);
		int r = day * (Math.min(10 * hub.getStaff().getDoctors(),Math.min(12 * hub.getStaff().getCountNurse(), 20 * hub.getStaff().getO())));
		return r;
	}

	/**
	 * Compute the available vaccination slots for each hub and for each day of the
	 * week
	 * <p>
	 * The method returns a map that associates the hub names (keys) to the lists of
	 * number of available hours for the 7 days.
	 * <p>
	 * The availability is computed as the number of working hours of that day
	 * multiplied by the capacity (see {@link #estimateCapacity} of the hub.
	 *
	 * @return
	 */
	public Map<String, List<Integer>> getAvailable() {
		Map<String, List<Integer>> map = new HashMap<>();
		int r=0;
		for(Hub hub: hubs.values()) {
			List<Integer> list = new ArrayList<>();
			for(int h: hours.values()) {
				r = h * (Math.min(10 * hub.getStaff().getDoctors(),Math.min(12 * hub.getStaff().getCountNurse(), 20 * hub.getStaff().getO())));
				list.add(r);
			}
			map.put(hub.getName(), list);
		}
		return map;
	}

	/**
	 * Computes the general allocation plan a hub on a given day. Starting with the
	 * oldest age intervals 40% of available places are allocated to persons in that
	 * interval before moving the the next interval and considering the remaining
	 * places.
	 * <p>
	 * The returned value is the list of SSNs (codice fiscale) of the persons
	 * allocated to that day
	 * <p>
	 * <b>N.B.</b> no particular order of allocation is guaranteed
	 *
	 * @param hubName name of the hub
	 * @param d       day of week index (0 = Monday)
	 * @return the list of daily allocations
	 */
	public List<String> allocate(String hubName, int d) {
		Collections.sort(intervals, Comparator.comparing(Interval::getLower).reversed());
        int num=getDailyAvailable(hubName,d);
        List<String> res=new ArrayList<>();
        for(Interval c: intervals) {
            List<Person> i=persons.values().stream().filter((Person p)->!p.getAllocate()).filter(p->c.notIn(CURRENT_YEAR - p.getYear())).limit((int)(num*0.4)).collect(Collectors.toList());
            for(Person person: i) {
            	person.SetAllocate();
            	res.add(person.getSsn());
            }
            num-=i.size();
        }
        if(num>0) {
            for(Interval c: intervals) {
                List<Person> i=persons.values().stream().filter((Person p)->!p.getAllocate()).filter(p->c.notIn(CURRENT_YEAR - p.getYear())).limit((int)(num)).collect(Collectors.toList());
                for(Person person: i) {
                	person.SetAllocate();
                	res.add(person.getSsn());
                }
                num-=i.size();
            }
        }
        return res;
	}

	/**
	 * Removes all people from allocation lists and clears their allocation status
	 */
	public void clearAllocation() {
		for(Person p:persons.values()) 
	         p.CancelAllocate();
	}

	/**
	 * Computes the general allocation plan for the week. For every day, starting
	 * with the oldest age intervals 40% available places are allocated to persons
	 * in that interval before moving the the next interval and considering the
	 * remaining places.
	 * <p>
	 * The returned value is a list with 7 elements, one for every day of the week,
	 * each element is a map that links the name of each hub to the list of SSNs
	 * (codice fiscale) of the persons allocated to that day in that hub
	 * <p>
	 * <b>N.B.</b> no particular order of allocation is guaranteed but the same
	 * invocation (after {@link #clearAllocation}) must return the same allocation.
	 *
	 * @return the list of daily allocations
	 */
	public List<Map<String, List<String>>> weekAllocate() {
		clearAllocation();
		List<Map<String, List<String>>> res = new ArrayList<>();
		for(int t=0; t<7; ++t) {
			Map<String, List<String>> re = new HashMap<>();
			for(Hub h: hubs.values()) {
				re.put(h.getName(), allocate(h.getName(), t));
			}
			res.add(re);
		}

		
		return res;
	}

	// R5
	/**
	 * Returns the proportion of allocated people w.r.t. the total number of persons
	 * added in the system
	 *
	 * @return proportion of allocated people
	 */
	public double propAllocated() {
		return weekAllocate().stream().flatMap(m -> m.values().stream()).mapToDouble(List::size).sum() / persons.size();
	}

	/**
	 * Returns the proportion of allocated people w.r.t. the total number of persons
	 * added in the system, divided by age interval.
	 * <p>
	 * The map associates the age interval label to the proportion of allocates
	 * people in that interval
	 *
	 * @return proportion of allocated people by age interval
	 */
	public Map<String, Double> propAllocatedAge() {
		Map<String, Double> res = new HashMap<>();
		for(AgeInterval a: ageintervals) {
			for(String s: a.getIntervals()) {
				double n=0;
				Collection<String> ssn = getInInterval(s);
				for(String st: ssn) {
					if(persons.get(st).getAllocate()) n++;
				}
				res.put(s, (n / persons.size()));
			}
		}
		return res;
	}

	/**
	 * Retrieves the distribution of allocated persons among the different age
	 * intervals.
	 * <p>
	 * For each age intervals the map reports the proportion of allocated persons in
	 * the corresponding interval w.r.t the total number of allocated persons
	 *
	 * @return
	 */
	public Map<String, Double> distributionAllocated() {
		Map<String, Double> res = new HashMap<>();
		for(AgeInterval a: ageintervals) {
			for(String s: a.getIntervals()) {
				double n=0;
				Collection<String> ssn = getInInterval(s);
				for(String st: ssn) {
					if(persons.get(st).getAllocate()) n++;
				}
				res.put(s, (n / persons.values().stream().filter((Person p)->p.getAllocate()).count()));
			}
		}
		return res;
	}

	// R6
	/**
	 * Defines a listener for the file loading method. The {@ accept()} method of
	 * the listener is called passing the line number and the offending line.
	 * <p>
	 * Lines start at 1 with the header line.
	 *
	 * @param lst the listener for load errors
	 */
	public void setLoadListener(BiConsumer<Integer, String> lst) {
		biConsumer=lst;
	}
}
