package com.bookstore.controller.frontend.shoppingcart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bookstore.entity.Book;

public class ShoppingCart {
	private Map<Book, Integer> cart = new HashMap<Book, Integer>();

	public void addItem(Book book) {
		if (cart.containsKey(book)) {
			Integer quatity = cart.get(book) + 1;
			cart.put(book, quatity);
		} else {
			cart.put(book, 1);
		}
	}

	public Map<Book, Integer> getItems() {
		return this.cart;
	}

	public void removeItems(Book book) {
		cart.remove(book);
	}

	public int getTotalQuantity() {
		int total = 0;
		Iterator<Book> iterator = cart.keySet().iterator();

		while (iterator.hasNext()) {
			Book next = iterator.next();
			Integer quantity = cart.get(next);
			total += quantity;
		}

		return total;
	}

	public int getTotalItems() {
		return cart.size();
	}
	
	public float getTax() {
		float tax=getTotalAmount()*0.1f;
		return tax;
	}

	public float getTotalAmount() {
		float total = 0.0f;
		Iterator<Book> iterator = cart.keySet().iterator();

		while (iterator.hasNext()) {
			Book book = iterator.next();
			Integer quantity = cart.get(book);
			double subTotal = quantity * book.getPrice();
			total += subTotal;
		}

		return total;
	}
	
	public float getTotal() {
		float total = 0.0f;
		total=getTotalAmount() + getTax();

		return total;
	}
	
	public void updateCart(int[] bookIds, int[] quantities) {
		for (int i = 0; i < bookIds.length; i++) {
			Integer value = quantities[i];
			Book key = new Book(bookIds[i]);
			cart.put(key, value);
		}
	}

	public void clear() {
		cart.clear();
	}

}
