package sports;

import java.util.ArrayList;
import java.util.List;

public class Product {
	String name;
	Activity activity;
	Category category;
	private List<Rating> ratings = new ArrayList<>();
	
	public Product(String name, Activity activity, Category category) {
		this.name = name;
		this.activity = activity;
		this.category = category;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public List<Rating> getRatings() {
		return ratings;
	}
	
	public void addRating(Rating r) {
		ratings.add(r);
	}

	public Activity getActivity() {
		return activity;
	}

	public Category getCategory() {
		return category;
	}
	
	public double avgStars() {
		return ratings.stream().mapToInt(Rating::getNumStars).average().orElse(0.0);
	}
	
}
