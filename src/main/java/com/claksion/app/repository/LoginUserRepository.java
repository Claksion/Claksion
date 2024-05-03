package com.claksion.app.repository;

import com.claksion.app.data.dto.LoginUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoginUserRepository extends CrudRepository<LoginUser,Integer> {
}