package Service;

import model.Customer;

import java.util.Collection;
import java.util.HashSet;

public class CustomerService {
    private static CustomerService customerService;

    public Collection<Customer> customerList = new HashSet<>();
    private CustomerService(){

    }
    public static CustomerService getInstance() {
        if(customerService == null){
            customerService = new CustomerService();
        }
        return customerService;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        if (checkCustomer(email)) {
            customerList.add(new Customer(firstName, lastName, email));
        }
    }
    public boolean checkCustomer(String email){
        for(Customer customer : customerList){
            if(customer.getEmail().equals(email)){
                throw new IllegalArgumentException("Customer already exists");
            }
        }
        return true;
    }
    public Customer getCustomer(String customerEmail){
        for(Customer customer : customerList){
            if(customer.getEmail().equals(customerEmail)){
                return customer;
            }
        }
        return null;
    }
    public Collection<Customer> getAllCustomers(){
        return customerList;
    }
}




