package sports;

public class Rating {
	Product product;
	int numStars;
	String comment;
	
	public Rating(Product product, int numStars, String comment) {
		this.product = product;
		this.numStars = numStars;
		this.comment = comment;
	}

	public Product getProduct() {
		return product;
	}

	public int getNumStars() {
		return numStars;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public String toString() {
		return numStars + " : " + comment;
	}
	
	
	

}
