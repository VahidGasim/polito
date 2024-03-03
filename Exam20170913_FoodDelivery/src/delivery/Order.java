package delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Order {
	private int id;
	private Customer customer;
	private Map<Item, Integer> items;
	
	
	public Order(int id, Customer customer) {
		this.id = id;
		this.customer = customer;
		items = new HashMap<>();
	}


	public int getId() {
		return id;
	}


	public Customer getCustomer() {
		return customer;
	}
	
	public int addItem(Item item, int qty) {
		  
        if(items.containsKey(item)){
            qty += items.get(item).intValue();
        }
        
        items.put(item, qty);
        
        return qty;
	}

	//return items.entrySet().stream().map(e -> String.format("%s, %d", e.getKey().getDescription(), e.getValue()))
		//	.collect(Collectors.toList());
	//public List<String> toStringList(){
       // return 
      //  items.entrySet().stream().
         //   map(e -> e.getKey().getDescription() + ", " + e.getValue()).collect(Collectors.toList());
   // }


}
