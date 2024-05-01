package com.claksion.app.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;


@Slf4j
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

    public UserEntity getByOauthId(String oauthId) throws Exception {
        return userRepository.selectByOauthId(oauthId);
    }
}
