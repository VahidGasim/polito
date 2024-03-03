package delivery;

public class Item {
	private String description;
	private double price;
	private String category;
	private int prepTime;
	private Order order;
	
	public Item(String description, double price, String category, int prepTime) {
		this.description = description;
		this.price = price;
		this.category = category;
		this.prepTime = prepTime;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public String getCategory() {
		return category;
	}

	public int getPrepTime() {
		return prepTime;
	}
	
	public boolean match(String search) {
		return description.equals(search);
	}
	
	
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString(){
        return ("[" + category + "] " + description + " : " + String.format("%.2f", price));
    }
	

}
