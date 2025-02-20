package com.homestay.management.repository;

import com.homestay.management.model.Customer;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    
    Optional<Customer> findByEmail(String email); // Tìm khách hàng theo email
//    Customer findByName(String name);  // Tìm người dùng qua name
    
//    List<Customer> findByName(String name);

}
