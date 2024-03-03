package sports;

import java.util.ArrayList;
import java.util.List;

public class Activity {
	private String name;
	private List<Product> products = new ArrayList<>();
    private List<Category> categories=new ArrayList<>();

	

	public Activity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public void addProducts(Product p) {
		products.add(p);
	}

	public List<Product> getProducts() {
		return products;
	}
	
	public void addCategory(Category cat) {
		categories.add(cat);
    }

	public List<Category> getCategories() {
		return categories;
	}

	
	
}
