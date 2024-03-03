package fit;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private String name;
	private int id;
	private List<Lesson> lessons = new ArrayList<>();

	public Customer(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	};
	
	public void addLesson(Lesson lesson) {
		lessons.add(lesson);
	}

	public List<Lesson> getLessons() {
		return lessons;
	}
	
	

}
