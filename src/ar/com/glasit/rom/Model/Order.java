package ar.com.glasit.rom.Model;

import java.util.ArrayList;
import java.util.List;

public class Order {

	private List<ItemProduct> products;
	private int totalPrice;
	
	public Order() {
		this.products= new ArrayList<ItemProduct>();
		this.totalPrice = 0;
	}
	
	public void addProduct(ItemProduct item) {
		this.products.add(item);
		
	}
	
	public void removeProduct() {
		
	}
	
	public int getPrice() {
		return this.totalPrice;
	}
}
