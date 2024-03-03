package supermarket;
import java.util.*;
import java.util.stream.Collectors;

public class Supermarket {
	private Map<String, Category> categories = new HashMap<>();
	private Map<String, Product> products = new HashMap<>();
	private Map<Integer, PointsCard> pointsCards = new HashMap<>();
	private SortedMap<Integer, Integer> pointsToDiscounts = new TreeMap<>();
	private Map<Integer, Purchase> purchases = new HashMap<>();
	private int id=999;
	private int pid=99;
	private int rid=-1;
	private Map<Integer, Receipt> receipts = new HashMap<>();



	//R1
	public int addProducts (String categoryName, String productNames, String productPrices) throws SMException {
		if(categories.containsKey(categoryName)) throw new SMException("z");
		Category c = new Category(categoryName);
		String[] pn = productNames.split(",");
		String[] pp = productPrices.split(",");
		for(int i=0; i<pn.length; ++i) {
			if(pn.length!=pp.length) throw new SMException("z");
			for(Category ca: categories.values()) {
				if(ca.getProducts().containsKey(pn[i])) throw new SMException("z");
			}
			Product p = new Product(c, pn[i], pp[i]);
			p.setNewPrice(p.getPrice());
			c.addProduct(p);
			products.put(pn[i], p);
		}
		categories.put(categoryName, c);
		return pn.length;
	}

	public double getPrice (String productName) throws SMException {
		if(!products.containsKey(productName)) throw new SMException("z");
		Product p = products.get(productName);
		double res = Double.parseDouble(p.getProductPrices());
		return res;
	}

	public SortedMap<String,String> mostExpensiveProductPerCategory () {	
		return categories.values().stream().collect(Collectors.toMap(Category::getName, Category::maxPriceName, (a1,a2) ->{ throw new RuntimeException(String.format("Duplicate key for values %s and %s", a1, a2));}, TreeMap::new));
	}
	

	//R2
	public void setDiscount (String categoryName, int percentage) throws SMException {
		if(!categories.containsKey(categoryName)) throw new SMException("z");
		if(percentage>40 ||percentage<0) throw new SMException("z");
		Category c = categories.get(categoryName);
		c.setDiscount(percentage);
		c.addDiscount(percentage);
	}

	public void setDiscount (int percentage, String... productNames) throws SMException {
		for(String s: productNames) {
			Product p = products.get(s);
			p.setDiscount(percentage);
			p.addDiscount(percentage);
		}
	}

	public List<Integer> getDiscountHistoryForCategory(String categoryName) {
		Category c = categories.get(categoryName);
		return c.getDiscounts();
	}

	public List<Integer> getDiscountHistoryForProduct(String productName) {
		Product p = products.get(productName);
		return p.getDiscounts();
	}

	//R3
	public int issuePointsCard (String name, String dateOfBirth) throws SMException {
		for(PointsCard pcs: pointsCards.values()) {
			if(pcs.getName().equals(name) && pcs.getDateOfBirth().equals(dateOfBirth)) throw new SMException("z");
			
		}
		PointsCard pc = new PointsCard(++id, name, dateOfBirth); 
		pointsCards.put(id, pc);
		return id;
	}



	public void fromPointsToDiscounts (String points, String discounts) throws SMException {
		String[] p = points.split(",");
		String[] d = discounts.split(",");
		if(p.length!=d.length) throw new SMException("z");
		for(int i=0; i<p.length; ++i) {
			pointsToDiscounts.put(Integer.parseInt(p[i]), Integer.parseInt(d[i]));
		}
	}

	public SortedMap<Integer, Integer>  getMapPointsDiscounts() {
		return pointsToDiscounts;
	}

	public int getDiscountFromPoints (int points) {
		if(!pointsToDiscounts.containsKey(points)) return 0;
		return pointsToDiscounts.get(points);
	}

	//R4

	public int getCurrentPoints (int pointsCardCode) throws SMException {
		if(!pointsCards.containsKey(pointsCardCode)) throw new SMException("a");
		PointsCard pc = pointsCards.get(pointsCardCode);
		return pc.getPoints();
	}

	public int getTotalPoints (int pointsCardCode) throws SMException {
		if(!pointsCards.containsKey(pointsCardCode)) throw new SMException("a");
		PointsCard pc = pointsCards.get(pointsCardCode);
		return pc.getTotalpoints();
	}

	public int addPurchase (int pointsCardCode, int pointsRedeemed, String ... productNames) throws SMException {
		PointsCard pc = pointsCards.get(pointsCardCode);
		if(pointsRedeemed>pc.getPoints()) throw new SMException("z");
		Purchase pur = new Purchase(++pid, pc, pointsRedeemed);
		double price=0;
		for(String s: productNames) {
			Product p = products.get(s);
			double newprice = p.getPrice() - (p.getPrice()*p.getDiscount()/100);
			pur.addProduct(p);
			p.setNewPrice(newprice);
			price+=newprice;
			p.getCategory().addCustomer(pc.getName());}
		pur.setPrice(price);
		if(pointsToDiscounts.containsKey(pointsRedeemed)) {
			double pointdiscount = getDiscountFromPoints(pointsRedeemed);
			pur.setPrice(price - pointdiscount);
			pc.setPoints(pc.getPoints() - pointsRedeemed);}
		pc.setPoints(pc.getPoints() + (int) Math.round(pur.getPrice()));
		pc.setTotalpoints(pc.getTotalpoints() + (int) Math.round(pur.getPrice()));
		purchases.put(pid, pur);
		return pid;
	}


	public double getPurchasePrice (int purchaseCode) throws SMException {
		if(!purchases.containsKey(purchaseCode)) throw new SMException("x");
		Purchase pur = purchases.get(purchaseCode);
		return pur.getPrice();
	}

	public double getPurchaseDiscount (int purchaseCode) throws SMException {
		if(!purchases.containsKey(purchaseCode)) throw new SMException("x");
		Purchase pur = purchases.get(purchaseCode);
		return pur.getOldPrices() - pur.getPrice();
	}

	
	//R5

	public SortedMap<Integer, List<Integer>> pointsCardsPerTotalPoints () {
		return pointsCards.values().stream().filter(p->p.getPoints()>0).collect(Collectors.groupingBy(PointsCard::getTotalpoints, TreeMap::new, Collectors.mapping(PointsCard::getId, Collectors.toList())));
	}


	public SortedMap<String, SortedSet<String>> customersPerCategory () {
		return categories.values().stream().collect(Collectors.toMap(Category::getName, Category::getCustomers, (a1,a2) ->{ throw new RuntimeException(String.format("Duplicate key for values %s and %s", a1, a2));}, TreeMap::new));
	}

	public SortedMap<Integer, List<String>> productsPerDiscount() {
		return products.values().stream().filter(p->p.maxDiscount()>0).sorted(Comparator.comparing(Product::getProductNames)).collect(Collectors.groupingBy(Product::maxDiscount,() -> new TreeMap<Integer, List<String>>(Comparator.reverseOrder()), Collectors.mapping(Product::getProductNames, Collectors.toList())));
	}


	// R6

	public int newReceipt() { // return code of new receipt
		receipts.put(++rid, new Receipt(rid));
		return rid;
	}

	public void receiptAddCard(int receiptCode, int PointsCard)  throws SMException { // add the points card info to the receipt
		if(!receipts.containsKey(receiptCode)) throw new SMException("13");
		if(!pointsCards.containsKey(PointsCard)) throw new SMException("13");
		Receipt re = receipts.get(receiptCode);
		PointsCard pc = pointsCards.get(PointsCard);
		if(re.isClose()==false) {re.setCard(pc);}
	}

	public int receiptGetPoints(int receiptCode)  throws SMException { // return available points on points card if added before
		if(!receipts.containsKey(receiptCode)) throw new SMException("13");
		if(receipts.get(receiptCode).getCard()==null) throw new SMException("13");
		Receipt re = receipts.get(receiptCode);
		return re.getCard().getPoints();
	}

	public void receiptAddProduct(int receiptCode, String product)  throws SMException { // add a new product to the receipt
		if(!receipts.containsKey(receiptCode)) throw new SMException("13");
		if(!products.containsKey(product)) throw new SMException("13");
		Receipt re = receipts.get(receiptCode);
		if(re.isClose()==true) throw new SMException("13");
		Product p = products.get(product);
		re.addProduct(p);
	}

	public double receiptGetTotal(int receiptCode)  throws SMException { // return current receipt code
		if(!receipts.containsKey(receiptCode)) throw new SMException("13");
		Receipt re = receipts.get(receiptCode);
		return re.getProducts().stream().collect(Collectors.summingDouble(p->p.getNewPrice()));
	}

	public void receiptSetRedeem(int receiptCode, int points)  throws SMException { // sets the amount of points to be redeemed
		if(!receipts.containsKey(receiptCode)) throw new SMException("13");
		if(receipts.get(receiptCode).getCard()==null) throw new SMException("13");
		if(!pointsToDiscounts.containsKey(points)) throw new SMException("13");
		Receipt re = receipts.get(receiptCode);
		if(points>re.getCard().getPoints()) throw new SMException("13");
		int discount = pointsToDiscounts.get(points);
		re.setRedeemedpoints(points);
		re.getProducts().stream().forEach(p->p.setNewPrice(p.getPrice()-discount));
	}

	public int closeReceipt(int receiptCode)  throws SMException { // close the receipt and add the purchase (calls addPurchase() ) and return purchase code (could be the same as receipt code)
		if(!receipts.containsKey(receiptCode)) throw new SMException("13");
		Receipt re = receipts.get(receiptCode);
		re.setClose(true);
		return addPurchase(re.getCard().getId(), re.getRedeemedpoints(), re.getProducts().stream().map(Product::getProductNames).toArray(String[]::new));
	}


}