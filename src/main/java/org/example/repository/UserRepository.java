package org.example.repository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import org.example.model.User;
/**
 * UserRepository
 */
public interface UserRepository  extends CrudRepository<User, String>{
    public User findByUsername(String username);
	
	public Optional<User> findById(String Id);
    
    
}