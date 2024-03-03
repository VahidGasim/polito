package supermarket;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Category {
	private String name;
	private Map<String, Product> products = new HashMap<>();
	private int discount = 0;
	private List<Integer> discounts = new LinkedList<>();
	private SortedSet<String> customers = new TreeSet<>();

	public Category(String name) {
		this.name = name;
		discounts.add(0);
	}

	public void addCustomer(String name) {
		customers.add(name);
	}
	
	
	public SortedSet<String> getCustomers() {
		return customers;
	}

	public String getName() {
		return name;
	}

	public Map<String, Product> getProducts() {
		return products;
	}
	
	public void addProduct(Product p) {
		products.put(p.getProductNames(), p);
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
		for(Product p: products.values()) {
			p.setDiscount(discount);
			p.addDiscount(discount);
		}
	}

	public List<Integer> getDiscounts() {
		return discounts;
	}
	
	public void addDiscount(int d) {
		discounts.add(d);
	}
	
	public String maxPriceName() {
		return products.values().stream().max(Comparator.comparing(p->p.getPrice())).map(Product::getProductNames).get();
	}
	
}
