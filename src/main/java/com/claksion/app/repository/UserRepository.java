package com.claksion.app.repository;

import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.frame.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepository extends BaseRepository<Integer, UserEntity> {
    UserEntity selectByOauthId(String oauthId) throws Exception;
}
