package supermarket;

public class PointsCard {
	private int id;
	private String name;
	private String dateOfBirth;
	private int points = 0;
	private int totalpoints = 0;
	
	public PointsCard(int id, String name, String dateOfBirth) {
		this.id = id;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
	}

	public String getName() {
		return name;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public int getId() {
		return id;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getTotalpoints() {
		return totalpoints;
	}

	public void setTotalpoints(int totalpoints) {
		this.totalpoints = totalpoints;
	}

	
	
}
