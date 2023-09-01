package org.example.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.example.model.User;
/**
 * UserRepository
 */
@Repository
public interface UserRepository  extends CrudRepository<User, String>{
    public User findByUsername(String username);
}