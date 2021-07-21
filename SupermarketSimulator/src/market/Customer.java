/**
 * Name: Autumn Arnold
 * Date: 7/20/2021
 */

package market;


public class Customer {
	private int customerID;
	private int serviceTime;
	private int arrivalTime;
	
	// default constructor
	Customer() {
		customerID = 0;
		serviceTime = 0;
		arrivalTime = 0;
	}
	
	// alternate constructor
	// sets the customerID, serviceTime, and arrivalTime
	Customer(int customerid, int servicetime, int arrivaltime) {
		customerID = customerid;
		serviceTime = servicetime;
		arrivalTime = arrivaltime;
	}
	
	int getCustomerID() {
		return customerID;
	}
	
	int getServiceTime() {
		return serviceTime;
	}
	
	int getArrivalTime() {
		return arrivalTime;
	}
	
	@Override
	public String toString() {
		return "customerID: " + customerID + "\nserviceTime: " +
				serviceTime + "\narrivalTime: " + arrivalTime;
	}
}
