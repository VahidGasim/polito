package sports;
import java.util.*;
import java.util.stream.Collectors;

 
/**
 * Facade class for the research evaluation system
 *
 */
public class Sports {
	private Map<String, Activity> activities = new HashMap<>();
	private Map<String, Category> categories = new HashMap<>();
	private Map<String, Product> products = new HashMap<>();
	private Map<String, List<Rating>> ratings = new HashMap<>();

	

    //R1
    /**
     * Define the activities types treated in the portal.
     * The method can be invoked multiple times to add different activities.
     * 
     * @param actvities names of the activities
     * @throws SportsException thrown if no activity is provided
     */
    public void defineActivities (String... activities) throws SportsException {
    	if(activities.length == 0) throw new SportsException("");
    	for(String a: activities) {
    		this.activities.put(a, new Activity(a));
    	}
    }

    /**
     * Retrieves the names of the defined activities.
     * 
     * @return activities names sorted alphabetically
     */
    public List<String> getActivities() {
    	List<String> res = new ArrayList<>();
    	for(Activity a: activities.values()) {
    		res.add(a.getName());
    	}
        return res;
    }


    /**
     * Add a new category of sport products and the linked activities
     * 
     * @param name name of the new category
     * @param activities reference activities for the category
     * @throws SportsException thrown if any of the specified activity does not exist
     */
    public void addCategory(String name, String... linkedActivities) throws SportsException {
		Category c = new Category(name);
    	for(String a: linkedActivities) {
    		if(!activities.containsKey(a)) throw new SportsException("");
    		c.addActivities(activities.get(a));
    		categories.put(name, c);
    	}
    }

    /**
     * Retrieves number of categories.
     * 
     * @return categories count
     */
    public int countCategories() {
        return categories.size();
    }

    /**
     * Retrieves all the categories linked to a given activity.
     * 
     * @param activity the activity of interest
     * @return list of categories (sorted alphabetically)
     */
    public List<String> getCategoriesForActivity(String activity) {
    	List<String> res = new ArrayList<>();
    	if(!activities.containsKey(activity)) {
    		return new ArrayList<>();
    	}
    	
    	for(Category c: categories.values()) {
    		for(Activity a: c.getActivities()) {
    			if(a.getName().equals(activity)) {
    				res.add(c.getName());
    			}
    		}
    	}
    	Collections.sort(res);
        return res;
    }

    //R2
    /**
     * Add a research group and the relative disciplines.
     * 
     * @param name name of the research group
     * @param disciplines list of disciplines
     * @throws SportsException thrown in case of duplicate name
     */
    public void addProduct(String name, String activityName, String categoryName) throws SportsException {
    	if(products.containsKey(name)) throw new SportsException("");
    	Category c = categories.get(categoryName);
    	Activity a = activities.get(activityName);
    	Product product = new Product(name, a, c);
    	c.addProducts(product);
    	a.addProducts(product);
    	products.put(name, product);
    }

    /**
     * Retrieves the list of products for a given category.
     * The list is sorted alphabetically.
     * 
     * @param categoryName name of the category
     * @return list of products
     */
    public List<String> getProductsForCategory(String categoryName){
    	if(!categories.containsKey(categoryName)) {
    		return new ArrayList<>();
    	}
    	Category c = categories.get(categoryName);
    	return c.getProducts().stream().map(Product::getName).sorted().collect(Collectors.toList());
    }

    /**
     * Retrieves the list of products for a given activity.
     * The list is sorted alphabetically.
     * 
     * @param activityName name of the activity
     * @return list of products
     */
    public List<String> getProductsForActivity(String activityName){
    	if(!activities.containsKey(activityName)) {
    		return new ArrayList<>();
    	}
    	Activity a = activities.get(activityName);
        return a.getProducts().stream().map(Product::getName).sorted().collect(Collectors.toList());
    }

    /**
     * Retrieves the list of products for a given activity and a set of categories
     * The list is sorted alphabetically.
     * 
     * @param activityName name of the activity
     * @param categoryNames names of the categories
     * @return list of products
     */
    public List<String> getProducts(String activityName, String... categoryNames){
    	if(!activities.containsKey(activityName)) {
    		return new ArrayList<>();
    	}
    	for(String c: categoryNames) {
    		if(!categories.containsKey(c)) {
        		return new ArrayList<>();
        	}
    	}
		Activity a = activities.get(activityName);
		List<String> x = new ArrayList<>();;
    	for(String c: categoryNames) {
    		Category ca = categories.get(c);
    		a.getProducts().stream().filter(p->ca.getProducts().contains(p)).map(Product::getName).collect(Collectors.toList());
    		for(String s: a.getProducts().stream().filter(p->ca.getProducts().contains(p)).map(Product::getName).collect(Collectors.toList())) {
    			x.add(s);
    		}
    		
    	}
    	Collections.sort(x);
        return x;
    }

    //    //R3
    /**
     * Add a new product rating
     * 
     * @param productName name of the product
     * @param userName name of the user submitting the rating
     * @param numStars score of the rating in stars
     * @param comment comment for the rating
     * @throws SportsException thrown numStars is not correct
     */
    public void addRating(String productName, String userName, int numStars, String comment) throws SportsException {
    	if(numStars<0 || numStars>5) throw new SportsException("");
    	Product p = products.get(productName);
    	Rating rating = new Rating(p, numStars, comment);
    	p.addRating(rating);
    	List<Rating> r = new ArrayList<>();
    	r.add(rating);
    	ratings.put(userName, r);
    }



    /**
     * Retrieves the ratings for the given product.
     * The ratings are sorted by descending number of stars.
     * 
     * @param productName name of the product
     * @return list of ratings sorted by stars
     */
    public List<String> getRatingsForProduct(String productName) {
    	Product p = products.get(productName);
    	return p.getRatings().stream().sorted(Comparator.comparing(Rating::getNumStars).reversed()).map(Rating::toString).collect(Collectors.toList());
    }


    //R4
    /**
     * Returns the average number of stars of the rating for the given product.
     * 
     * 
     * @param productName name of the product
     * @return average rating
     */
    public double getStarsOfProduct (String productName) {
    	Product p = products.get(productName);
    	return p.getRatings().stream().collect(Collectors.averagingInt(Rating::getNumStars));
    }

    /**
     * Computes the overall average stars of all ratings
     *  
     * @return average stars
     */
    public double averageStars() {
    	return Math.round(ratings.values().stream().flatMap(Collection::stream).mapToInt(Rating::getNumStars).average().orElse(0.0));

    }

    //R5 Statistiche
    /**
     * For each activity return the average stars of the entered ratings.
     * 
     * Activity names are sorted alphabetically.
     * 
     * @return the map associating activity name to average stars
     */
    public SortedMap<String, Double> starsPerActivity() {
    	return ratings.values().stream().flatMap(Collection::stream).collect(Collectors.groupingBy(r->r.getProduct().getActivity().getName(), TreeMap::new, Collectors.averagingDouble(Rating::getNumStars)));
    }

    /**
     * For each average star rating returns a list of
     * the products that have such score.
     * 
     * Ratings are sorted in descending order.
     * 
     * @return the map linking the average stars to the list of products
     */
    public SortedMap<Double, List<String>> getProductsPerStars () {
    	
        return  products.values().stream()
                .filter(p -> p.getRatings().size()>0)
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.groupingBy(Product::avgStars,
                                    () -> new TreeMap<Double, List<String>>(Comparator.reverseOrder()),
                                    Collectors.mapping(Product::getName,Collectors.toList())
                                    ));
    }

}