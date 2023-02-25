package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customer.setTripBookingList(new ArrayList<>());
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById((customerId));

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		boolean gotdriver=false;
		Driver availibleDriver=new Driver();
		List<Driver> drivers=driverRepository2.findAll();
		for(Driver driver:drivers)
		{
			if(driver.getCab().getAvailable())
			{
				gotdriver=true;
				availibleDriver=driver;
				break;
			}
		}
		if(!gotdriver)
		{
			throw new Exception("No cab available!");
		}

		TripBooking tripBooking=new TripBooking();
		tripBooking.setCustomer(customerRepository2.findById(customerId).get());
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setDriver(availibleDriver);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setBill(distanceInKm*10);

		availibleDriver.getCab().setAvailable(false);
		tripBookingRepository2.save(tripBooking);

		customerRepository2.findById(customerId).get().getTripBookingList().add(tripBooking);
		customerRepository2.save(customerRepository2.findById(customerId).get());

		availibleDriver.getTripBookingList().add(tripBooking);
		driverRepository2.save(availibleDriver);
		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking trip=tripBookingRepository2.findById(tripId).get();
		trip.getDriver().getCab().setAvailable(true);
		trip.setStatus(TripStatus.CANCELED);
		trip.setBill(0);
		driverRepository2.save(trip.getDriver());

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking trip=tripBookingRepository2.findById(tripId).get();
		trip.getDriver().getCab().setAvailable(true);
		trip.setStatus(TripStatus.COMPLETED);
		driverRepository2.save(trip.getDriver());

	}
}
