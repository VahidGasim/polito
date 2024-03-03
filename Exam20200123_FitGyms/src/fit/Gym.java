package fit;

import java.util.HashMap;
import java.util.Map;

public class Gym {
	private String name;
	private Map<String, Lesson> lessons = new HashMap<>();

	public Gym(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addLessons(Lesson lesson) throws FitException {
		if(lessons.containsKey(lesson.getDay()+"."+lesson.getSlot())) throw new FitException();
		lessons.put(lesson.getDay()+"."+lesson.getSlot(), lesson);
	}
	
	public int numLessons(String instructor) {
		int n=0;
		for(Lesson l: lessons.values()) {
			if(l.getInstructor()!=null) {
				if(l.getInstructor().equals(instructor)) {
					n++;
				}
			}
		}
		
		return n;
	}
	
	public Map<String, Lesson> getLessons() {
		return lessons;
	}

	public Lesson getLesson (int day, int slot) throws FitException {
		Lesson l = lessons.get(day+"."+slot);
	    if (l == null) { 
	    	throw new FitException ();
	    } else {
	   		return l;
	   	}
	}

}
