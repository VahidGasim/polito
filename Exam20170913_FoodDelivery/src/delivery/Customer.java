package delivery;

public class Customer implements Comparable<Customer>{
	private int id;
	private String name;
	private String address;
	private String phone;
	private String email;
	
	
	public Customer(int id, String name, String address, String phone, String email) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}
	
	

	public int getId() {
		return id;
	}



	public String getName() {
		return name;
	}


	public String getAddress() {
		return address;
	}


	public String getPhone() {
		return phone;
	}


	public String getEmail() {
		return email;
	}


	@Override
	public String toString() {
		return name + ", " + address + ", " + phone + ", " + email;
	}



	@Override
	public int compareTo(Customer o) {
		return name.compareTo(o.name);
	}
	
	
	
	

}
