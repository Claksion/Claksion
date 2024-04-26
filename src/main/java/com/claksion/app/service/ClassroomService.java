package com.claksion.app.service;

import com.claksion.app.data.entity.ClassroomEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomService implements BaseService<Integer, ClassroomEntity> {

    final ClassroomRepository classroomRepository;

    @Override
    public int add(ClassroomEntity classroomEntity) throws Exception {
        return classroomRepository.insert(classroomEntity);
    }

    @Override
    public int del(Integer integer) throws Exception {
        return classroomRepository.delete(integer);
    }

    @Override
    public int modify(ClassroomEntity classroomEntity) throws Exception {
        return classroomRepository.update(classroomEntity);
    }

    @Override
    public ClassroomEntity get(Integer integer) throws Exception {
        return classroomRepository.selectOne(integer);
    }

    @Override
    public List<ClassroomEntity> get() throws Exception {
        return classroomRepository.select();
    }
}
