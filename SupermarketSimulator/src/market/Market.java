/**
 * Name: Autumn Arnold
 * Date: 7/20/2021
 */
package market;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;


public class Market {
	// user input parameters
	private int numCashiers, customerQLimit, chancesOfArrival;
	private int maxServiceTime, simulationTime, dataSource;
	
	// for statistical data
	private int numGoAway, numServed, totalWaitingTime;
	
	// internal data
	private int counter;				// customer id
	private CheckoutArea checkoutArea;	// CheckoutArea object
	private Scanner dataFile;			// get customer data from file
	private Random dataRandom;			// get customer data from random function
	
	// most recent customer arrival info
	private boolean anyNewArrival;
	private int serviceTime;
	
	// constructor
	private Market() {
		numCashiers = 0;
		customerQLimit = 0;
		chancesOfArrival = 0;
		maxServiceTime = 0;
		simulationTime = 0;
		dataSource = 0;
		numGoAway = 0;
		numServed = 0;
		totalWaitingTime = 0;
		counter = 0;
		checkoutArea = null;
		dataFile = null;
		dataRandom = null;
		anyNewArrival = false;
		serviceTime = 0;
	}
	
	private void setupParameters() {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter simulation time (positive integer) > ");
		do {
			simulationTime = input.nextInt();
		} while(simulationTime < 1 || simulationTime > 10000);
		
		System.out.println("Enter number of cashiers > ");
		do {
			numCashiers = input.nextInt();
		} while(numCashiers < 1 || numCashiers > 10);
		
		System.out.println("Enter maximum service time > ");
		do {
			maxServiceTime = input.nextInt();
		} while(maxServiceTime < 1 || maxServiceTime > 500);
		
		System.out.println("Enter chance of new customer (1 - 100%) > ");
		do {
			chancesOfArrival = input.nextInt();
		} while(chancesOfArrival < 1 || chancesOfArrival > 100);
		
		System.out.println("Enter customer queue limit > ");
		do {
			customerQLimit = input.nextInt();
		} while(customerQLimit < 1 || customerQLimit > 50);
		
		System.out.println("Enter 0/1 to get data from random/file > ");
		do {
			dataSource = input.nextInt();
		} while(dataSource < 0 || dataSource > 1);
		
		if(dataSource == 1) {
			System.out.println("Enter filename > ");
			
			String file;
			Scanner scanner = new Scanner(System.in);
			file = scanner.nextLine();
			try {
				dataFile = new Scanner(new File(file));
			} catch(FileNotFoundException e) {
				System.out.println("Error opening file: " + file);
				System.exit(0);
			}
			
			scanner.close();
		}
		
		input.close();
	}
	
	public void getCustomerData() {
		dataRandom = new Random();
		
		if(dataSource == 1) {
			int data = dataFile.nextInt();
			int data1 = dataFile.nextInt();
			
			anyNewArrival = (((data%100) + 1) <= chancesOfArrival);
			serviceTime = (data1%maxServiceTime) + 1;
		}
		else {
			anyNewArrival = ((dataRandom.nextInt(100) + 1) <= chancesOfArrival);
			serviceTime = dataRandom.nextInt(maxServiceTime) + 1;
		}
	}
	
	public void doSimulation() {
		// initialize CheckoutArea
		checkoutArea = new CheckoutArea(numCashiers, customerQLimit);
		
		// time simulation loop
		for(int currentTime = 0; currentTime < simulationTime; currentTime++) {
			System.out.println("-------------------------------------------");
			System.out.println("Time " + (currentTime+1));
			
			// any new customers in checkout area?
			getCustomerData();
			
			if(anyNewArrival) {
				// setup customer data
				counter++;
				Customer newCustomer = new Customer(counter, serviceTime, currentTime);
				System.out.println("Customer #" + counter + " has a checkout time of " + newCustomer.getServiceTime() + " units.");
				
				// is customer waiting in queue too long?
				if(checkoutArea.isCustomerQTooLong()) {
					System.out.println("Customer queue is full. The customer is leaving.");
					numGoAway++;
				}
				else {
					checkoutArea.insertCustomerQ(newCustomer);
					System.out.println("Customer #" + counter + " is now waiting for a Cashier.");
				}
			}
			else {
				System.out.println("\tNo new customers!");
			}
			
			// free busy cashiers, add to free cashierQ
			for(int i = 0; i < checkoutArea.sizeBusyCashierQ(); i++) {
				Cashier newCashier = checkoutArea.peekBusyCashierQ();
				
				if(newCashier.getEndBusyTime() <= currentTime) {
					newCashier = checkoutArea.removeBusyCashierQ();
					Customer newCustomer = newCashier.busyToFree();
					System.out.println("Customer #" + newCustomer.getCustomerID() + " is done.");
					checkoutArea.insertFreeCashierQ(newCashier);
					System.out.println("Cashier  #" + newCashier.getCashierID() + " is free.");
				}
			}
			
			// get free cashiers to serve waiting customers
			for(int i = 0; i < checkoutArea.sizeFreeCashierQ(); i++) {
				if(checkoutArea.sizeCustomerQ() != 0) {
					Customer newCustomer = checkoutArea.removeCustomerQ();
					Cashier newCashier = checkoutArea.removeFreeCashierQ();
					newCashier.freeToBusy(newCustomer, currentTime);
					
					checkoutArea.insertBusyCashierQ(newCashier);
					System.out.println("Customer #" + newCustomer.getCustomerID() + " has a cashier.");
					System.out.println("Cashier #" + newCashier.getCashierID() + " is now serving Customer #" + newCustomer.getCustomerID() + " for " + newCustomer.getServiceTime() + " interval(s).");
					System.out.println("Customer #" + newCashier.getCurrentCustomer().getCustomerID() + " is being served by Cashier #" + newCashier.getCashierID() + ".");
					numServed++;
					totalWaitingTime += (currentTime - newCustomer.getArrivalTime());
				}
			}
		} // end of simulation loop
	}
	
	private void printStatistics() {
		System.out.println("\n\n========================================\n\n");
        System.out.println("End of Simulation Report\n");
        System.out.println("     # total arrived customers	:    " + counter);
        System.out.println("     # customers turned away	:    " + numGoAway);
        System.out.println("     # customers served		:    " + numServed);
        System.out.println("\n\n*** Current Cashiers Info ***\n");
        
        checkoutArea.printStatistics();
        
        System.out.println("\nTotal Waiting Time : " + totalWaitingTime);
        System.out.println("Average Waiting Time : " + ((totalWaitingTime * 1.0) / numServed));
        
        System.out.println("\n\nBusy Cashier Info. : \n");  
        while(checkoutArea.sizeBusyCashierQ() > 0) {
        	Cashier cashier = checkoutArea.removeBusyCashierQ();
        	cashier.setEndBusyTime(simulationTime);
        	cashier.printStatistics();
        }
        
        System.out.println("\n\nFree Cashier Info. : \n"); 
        while(checkoutArea.sizeFreeCashierQ() > 0) {
        	Cashier cashier = checkoutArea.removeFreeCashierQ();
        	cashier.setEndFreeTime(simulationTime);
        	cashier.printStatistics();
        }
	}
	
	
	public static void main(String[] args) {
		Market runMarket = new Market();

		runMarket.setupParameters();
		runMarket.doSimulation();
		runMarket.printStatistics();
	}
}
