/**
 * Name: Autumn Arnold
 * Date: 7/20/2021
 */

package market;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;


class CompareCashier implements Comparator<Cashier> {
	@Override
	public int compare(Cashier o1, Cashier o2) {
		return o1.getEndBusyTime() - o2.getEndBusyTime();
	}
}


public class CheckoutArea {
	private PriorityQueue<Cashier> busyCashierQ;
	private Queue<Customer> customerQ;
	private Queue<Cashier> freeCashierQ;
	private int customerQLimit;
	
	// default constructor
	public CheckoutArea() {
		
	}
	
	// alternate constructor
	public CheckoutArea(int numCashiers, int customerQlimit) {
		// establish queues & set customer limit
		customerQ = new ArrayDeque<Customer>();
		freeCashierQ = new ArrayDeque<Cashier>();
		busyCashierQ = new PriorityQueue<Cashier>(numCashiers, new CompareCashier());	
		customerQLimit = customerQlimit;
		
		// construct cashiers and insert them into freeCashierQ
		for (int i = 1; i <= numCashiers; i++) {
			freeCashierQ.add(new Cashier(i));
		}
	}
	
	// free cashier queue methods
	public Cashier removeFreeCashierQ() {
		return freeCashierQ.poll(); // retrieves and removes from queue
	}
	
	public void insertFreeCashierQ(Cashier cashier) {
		freeCashierQ.add(cashier);
	}
	
	public boolean emptyFreeCashierQ() {
		return freeCashierQ.isEmpty();
	}
	
	public int sizeFreeCashierQ() {
		return freeCashierQ.isEmpty()? 0 : freeCashierQ.size();
	}
	
	// busy cashier queue methods
	public Cashier removeBusyCashierQ() {
		return busyCashierQ.poll();
	}
	
	public void insertBusyCashierQ(Cashier cashier) {
		busyCashierQ.add(cashier);
	}
	
	public boolean emptyBusyCashierQ() {
		return busyCashierQ.isEmpty();
	}
	
	public int sizeBusyCashierQ() {
		return busyCashierQ.isEmpty()? 0 : busyCashierQ.size();
	}
	
	public Cashier peekBusyCashierQ() {
		return busyCashierQ.peek(); // retrieves but doesn't remove
	}
	
	// customer queue methods
	public Customer removeCustomerQ() {
		return customerQ.poll();
	}
	
	public void insertCustomerQ(Customer customer) {
		customerQ.add(customer);
	}
	
	public boolean emptyCustomerQ() {
		return customerQ.isEmpty();
	}
	
	public int sizeCustomerQ() {
		return customerQ.isEmpty()? 0 : customerQ.size();
	}
	
	public boolean isCustomerQTooLong() {
		return !customerQ.isEmpty() && customerQ.size() >= customerQLimit;
	}
	
	
	public void printStatistics() {
		System.out.println("\t# of waiting customers		: " + sizeCustomerQ());
		System.out.println("\t# of busy cashiers		: " + sizeBusyCashierQ());
		System.out.println("\t# of free cashiers		: " + sizeFreeCashierQ());
	}
}
