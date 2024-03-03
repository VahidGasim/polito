package supermarket;

import java.util.ArrayList;
import java.util.List;

public class Purchase {
	private int id=0;
	private PointsCard pointsCard;
	private int pointsRedeemed;
	private List<Product> products = new ArrayList<>();
	private double price=0;
	
	public Purchase(int id, PointsCard pointsCard, int pointsRedeemed) {
		this.id = id;
		this.pointsCard = pointsCard;
		this.pointsRedeemed = pointsRedeemed;
	}

	public void addProduct(Product p) {
		products.add(p);
	}

	public int getId() {
		return id;
	}

	public PointsCard getPointsCard() {
		return pointsCard;
	}

	public int getPointsRedeemed() {
		return pointsRedeemed;
	}

	public List<Product> getProducts() {
		return products;
	}
	
	public double getOldPrices() {
		double res=0;
		for(Product p: products) {
			res += p.getPrice();
		}
		return res;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
}
