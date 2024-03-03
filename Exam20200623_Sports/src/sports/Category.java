package sports;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private String name;
	private List<Activity> activities = new ArrayList<>();
	private List<Product> products = new ArrayList<>();
	
	public Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addActivities(Activity a) {
		activities.add(a);
	}

	public List<Activity> getActivities() {
		return activities;
	}
	
	public void addProducts(Product p) {
		products.add(p);
	}

	public List<Product> getProducts() {
		return products;
	}
	
	
	
}
