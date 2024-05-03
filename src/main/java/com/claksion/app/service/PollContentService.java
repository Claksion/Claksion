package com.claksion.app.service;

import com.claksion.app.data.entity.PollContentEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.PollContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollContentService implements BaseService<Integer, PollContentEntity> {
    @Autowired
    final PollContentRepository pollContentRepository;

    @Override
    public int add(PollContentEntity pollContentEntity) throws Exception {
        return pollContentRepository.insert(pollContentEntity);
    }

    @Override
    public int del(Integer integer) throws Exception {
        return pollContentRepository.delete(integer);
    }

    @Override
    public int modify(PollContentEntity pollContentEntity) throws Exception {
        return pollContentRepository.update(pollContentEntity);
    }

    @Override
    public PollContentEntity get(Integer integer) throws Exception {
        return pollContentRepository.selectOne(integer);
    }

    @Override
    public List<PollContentEntity> get() throws Exception {
        return pollContentRepository.select();
    }

    public List<PollContentEntity> selectPollContents(Integer integer) throws Exception {
        return pollContentRepository.selectPollContents(integer);
    }

    public List<PollContentEntity> selectCheckedContents() throws Exception {
        return pollContentRepository.selectCheckedContents();
    }
}
