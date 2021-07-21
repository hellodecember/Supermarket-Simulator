/**
 * Name: Autumn Arnold
 * Date: 7/20/2021
 */

package market;


public class Cashier {
	// the cashier and the customer they're serving
	private int cashierID;
	private Customer serveCustomer;
	
	// will calculate how long it take to serve a customer
	private int startTime;
	private int endTime;
	
	// to keep statistical data
	private int totalFreeTime;
	private int totalBusyTime;
	private int totalCustomers;
	
	// default constructor
	Cashier() {
		cashierID = 0;
		startTime = 0;
		endTime = 0;
		totalFreeTime = 0;
		totalBusyTime = 0;
		totalCustomers = 0;
	}
	
	// alternate constructor
	Cashier(int cashierid) {
		cashierID = cashierid;
		startTime = 0;
		endTime = 0;
		totalFreeTime = 0;
		totalBusyTime = 0;
		totalCustomers = 0;
	}
	
	int getCashierID() {
		return cashierID;
	}
	
	Customer getCurrentCustomer() {
		return serveCustomer;
	}
	
	// switch from free to busy interval
	void freeToBusy(Customer serveCustomer, int currentTime) {
		totalFreeTime += currentTime - startTime;
		startTime = currentTime;
		this.serveCustomer = serveCustomer;
		endTime = currentTime + serveCustomer.getServiceTime();
		totalCustomers++;
	}
	
	// switch from busy to free interval
	Customer busyToFree() {
		totalBusyTime += endTime - startTime;
		startTime = endTime;
		return serveCustomer;
	}
	
	// returns the end of the cashier's busy interval
	// used in priority queue
	int getEndBusyTime() {
		return endTime;
	}
	
	// for the free interval at the end of the simulation
	void setEndFreeTime(int endsimulationtime) {
		endTime = endsimulationtime;
		totalFreeTime += endTime - startTime;
	}
	
	// for the busy interval at the end of the simulation
	void setEndBusyTime(int endsimulationtime) {
		endTime = endsimulationtime;
		totalBusyTime += endTime - startTime;
	}
	
	// to print the statistics to screen
	void printStatistics() {
		System.out.println("\t\tCashier ID				: " + cashierID);
		System.out.println("\t\tTotal Free Time				: " + totalFreeTime);
		System.out.println("\t\tTotal Busy Time				: " + totalBusyTime);
		System.out.println("\t\tTotal # of Customers			: " + totalCustomers);
		if(totalCustomers > 0) {
			System.out.format("\t\tAverage checkout time			: %.2f%n\n", (totalBusyTime*1.0) / totalCustomers);
		}
	}
	
	@Override
	public String toString() {
		return "Cashier ID: " + cashierID + "\nStart Time: " + startTime +
				"\nEnd Time: " + endTime + "\nServed Customer: \n" + serveCustomer;
	}
}
