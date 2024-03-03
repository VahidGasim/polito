package supermarket;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Product {
	private Category category;
	private String productNames;
	private String productPrices;
	private int discount = 0;
	private List<Integer> discounts = new LinkedList<>();
	private double newPrice = 0;
	
	public Product(Category category, String productNames, String productPrices) {
		this.category = category;
		this.productNames = productNames;
		this.productPrices = productPrices;
		discounts.add(0);
	}

	public Category getCategory() {
		return category;
	}

	public String getProductNames() {
		return productNames;
	}

	public String getProductPrices() {
		return productPrices;
	}
	
	public double getPrice() {
		return Double.parseDouble(productPrices);
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	public void addDiscount(int d) {
		discounts.add(d);
	}
	
	public List<Integer> getDiscounts() {
		return discounts;
	}

	public double getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(double newPrice) {
		this.newPrice = newPrice;
	}
	
	public int maxDiscount() {
		return discounts.stream().max(Comparator.comparing(d->d)).get();
	}
	
}
