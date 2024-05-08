package com.claksion.app.service;

import com.claksion.app.data.dto.ClassMate;

import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements BaseService<Integer, UserEntity> {

    final UserRepository userRepository;
    final RedisTemplate<String, Object> redisTemplate;

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

    public UserEntity getByOauthId(String oauthId) throws Exception {
        return userRepository.selectByOauthId(oauthId);
    }

    public List<ClassMate> getClassMates(int classroomId) throws Exception {
        List<UserEntity> users = userRepository.selectByClassroomId(classroomId);
        List<ClassMate> classMates = new ArrayList<>();
        for (UserEntity user : users) {
            int userId = user.getId();
            boolean online = Boolean.TRUE.equals(redisTemplate.hasKey("loginUser:" + userId));
            classMates.add(new ClassMate(user.getId(), user.getName(), user.getType(), user.getProfileImg(), user.getEmail(), online));
        }
        return classMates;
    }

    public String findNameById(int id) {
        return userRepository.findNameById(id);
    }
}
