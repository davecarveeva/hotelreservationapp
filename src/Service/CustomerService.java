package Service;

import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        if(customerList.contains(email)){
            System.out.println("Customer with this email already exists!");
        }
        customerList.add(new Customer(firstName, lastName, email));
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




