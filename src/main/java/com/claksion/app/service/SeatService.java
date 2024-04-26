package com.claksion.app.service;

import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService implements BaseService<Integer, SeatEntity> {
    
    final SeatRepository seatRepository;
    
    @Override
    public int add(SeatEntity SeatEntity) throws Exception {
        return seatRepository.insert(SeatEntity);
    }

    @Override
    public int del(Integer integer) throws Exception {
        return seatRepository.delete(integer);
    }

    @Override
    public int modify(SeatEntity SeatEntity) throws Exception {
        return seatRepository.update(SeatEntity);
    }

    @Override
    public SeatEntity get(Integer integer) throws Exception {
        return seatRepository.selectOne(integer);
    }

    @Override
    public List<SeatEntity> get() throws Exception {
        return seatRepository.select();
    }
}
