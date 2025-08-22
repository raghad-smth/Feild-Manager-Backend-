package com.example.feilds.repository;

import com.example.feilds.model.Users;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UpdateProfileRepository {

    private Map<Integer, Users> userDatabase = new HashMap<>();

    public Optional<Users> findById(Integer id) {
        return Optional.ofNullable(userDatabase.get(id));
    }

    public Users save(Users user) {
        userDatabase.put(user.getId(), user);
        return user;
    }
}