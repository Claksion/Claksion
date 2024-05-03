package com.claksion.controller;

import com.claksion.app.data.entity.PollContentEntity;
import com.claksion.app.data.entity.PollEntity;
import com.claksion.app.service.PollContentService;
import com.claksion.app.service.PollService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/poll")
@Slf4j
@RequiredArgsConstructor
public class PollController {
    final private PollService pollService;
    final private PollContentService pollContentService;
    final private RedisTemplate<String, Object> redisTemplate;
    @RequestMapping("")
    public String poll(Model model) throws Exception {
        pollService.get();
        List<PollEntity> pollList = pollService.get();
        model.addAttribute("pollList", pollList);
        model.addAttribute("center", "poll_list");
        return "index";
    }
    @RequestMapping("/creation")
    public String pollcreation(Model model){

        return "poll_creation";
    }
    @PostMapping(value = "/creationimpl")
    @ResponseBody
    public Integer pollcreationimpl(@RequestBody Map<String, Object> requestData/*, @RequestBody PollContentEntity pollContentEntity*/) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Map<String, Object> dataMap = (Map<String, Object>) requestData.get("requestData");

        Map<String, Object> pollMap = (Map<String, Object>) dataMap.get("poll");
        List<Map<String, Object>> pollContentsMap = (List<Map<String, Object>>) dataMap.get("pollContents");
        log.info(pollMap.toString());
        log.info(pollContentsMap.toString());

        PollEntity pollEntity = objectMapper.convertValue(pollMap, PollEntity.class);
        pollEntity.setClassroomId(1);
        pollService.add(pollEntity);

        log.info(pollEntity.toString());
        int pollId = pollService.selectPollId(pollEntity);
        log.info("=============================pollid: "+pollId);

        for (Map<String, Object> contentMap : pollContentsMap) {
            PollContentEntity content = objectMapper.convertValue(contentMap, PollContentEntity.class);
            content.setPollId(pollService.get(pollId).getId());
            pollContentService.add(content);
        }

        log.info(pollMap.toString());
        log.info(pollContentsMap.toString());


        return pollId;
    }
    @RequestMapping("/form")
    public String pollform(Model model, @RequestParam("pollId") int pollId) throws Exception {
        PollEntity pollEntity = null;
        List<PollContentEntity> pollList = null;
        pollEntity = pollService.get(pollId);
        pollList = pollContentService.selectPollContents(pollId);

        model.addAttribute("poll",pollEntity);
        model.addAttribute("pollContents", pollList);

        return "poll_form";
    }
    @PostMapping(value = "/formimpl")
    public String pollformimpl(@RequestBody List<PollContentEntity> pollContentList) throws Exception {
        log.info("hi");
        log.info(pollContentList.toString());
        pollContentList.forEach(content -> {
            try {
                pollContentService.modify(content);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        log.info(pollContentService.selectCheckedContents().toString());

        pollContentService.selectCheckedContents().forEach(content -> {
            System.out.println(content.getName()+" 투표");
            redisTemplate.opsForValue().increment("poll:pollid3:"+content.getName());

//
        });


        // update
        // result
        return "poll_creation";
    }
    @RequestMapping("/result")
    public String pollresult(Model model, @RequestParam("pollId") int pollId) throws Exception {
        PollEntity pollEntity = null;
        List<PollContentEntity> pollContentList = null;
        pollEntity = pollService.get(pollId);
        pollContentList = pollContentService.selectPollContents(pollId);

        model.addAttribute("poll",pollEntity);
        model.addAttribute("pollContents",pollContentList);


        // 투표 버튼 클릭
        // pub->sub 증가한 카운트 publish -> 랭킹 표기

        return "poll_result";
    }

    public void pollimpl() {
        // 유저 DB(mysql)에 선택한 항목 저장

        // 항목 DB(redis) 카운트 증가
    }
}
