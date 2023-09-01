package org.example.repository;
import org.springframework.data.repository.CrudRepository;

import org.example.model.User;
/**
 * UserRepository
 */
public interface UserRepository  extends CrudRepository<User, String>{
    
    
}