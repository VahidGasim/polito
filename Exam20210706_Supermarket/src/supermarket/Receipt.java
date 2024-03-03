package supermarket;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
	private int id;
	private PointsCard card;
	private List<Product> products = new ArrayList<>();
	private int redeemedpoints=0;
	private boolean close=false;

	
	public Receipt(int id) {
		this.id = id;
	}
	
	
	public int getRedeemedpoints() {
		return redeemedpoints;
	}

	public void setRedeemedpoints(int redeemedpoints) {
		this.redeemedpoints = redeemedpoints;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	public int getId() {
		return id;
	}

	public PointsCard getCard() {
		return card;
	}

	public void setCard(PointsCard card) {
		this.card = card;
	}

	public List<Product> getProducts() {
		return products;
	}
	
	public void addProduct(Product p) {
		products.add(p);
	}

}
