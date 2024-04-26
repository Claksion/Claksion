package com.claksion.app.service;

import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<Integer, UserEntity> {

    final UserRepository userRepository;

    @Override
    public int add(UserEntity userEntity) throws Exception {
        return userRepository.insert(userEntity);
    }

    @Override
    public int del(Integer integer) throws Exception {
        return userRepository.delete(integer);
    }

    @Override
    public int modify(UserEntity userEntity) throws Exception {
        return userRepository.update(userEntity);
    }

    @Override
    public UserEntity get(Integer integer) throws Exception {
        return userRepository.selectOne(integer);
    }

    @Override
    public List<UserEntity> get() throws Exception {
        return userRepository.select();
    }
}
