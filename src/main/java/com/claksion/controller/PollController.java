package com.claksion.controller;

import com.claksion.app.data.entity.PollContentEntity;
import com.claksion.app.data.entity.PollEntity;
import com.claksion.app.service.PollContentService;
import com.claksion.app.service.PollService;
import com.claksion.app.service.RankingService;
import com.claksion.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/poll")
@RequiredArgsConstructor
public class PollController {
    final private UserService userService;
    final private PollService pollService;
    final private PollContentService pollContentService;

    @Autowired
    private RankingService rankingService;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @RequestMapping("")
    public String poll(Model model) throws Exception {
        pollService.get();
        List<PollEntity> pollList = pollService.get();
        pollService.isPollFinished();

        model.addAttribute("pollList", pollList);
        model.addAttribute("center", "poll_list");
        return "index";
    }
    @RequestMapping("/creation")
    public String pollcreation(Model model){
        model.addAttribute("center", "poll_creation");
        return "index";
    }
    @PostMapping(value = "/creationimpl")
    @ResponseBody
    public Integer pollcreationimpl(@RequestBody Map<String, Object> requestData, HttpSession httpSession) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Map<String, Object> dataMap = (Map<String, Object>) requestData.get("requestData");

        Map<String, Object> pollMap = (Map<String, Object>) dataMap.get("poll");
        List<Map<String, Object>> pollContentsMap = (List<Map<String, Object>>) dataMap.get("pollContents");

        PollEntity pollEntity = objectMapper.convertValue(pollMap, PollEntity.class);

        pollEntity.setMadeById(httpSession.getAttribute("userId").toString());
        pollEntity.setMadeByName(httpSession.getAttribute("userName").toString());
        pollEntity.setClassroomId(1);

        pollService.add(pollEntity);

        Map<Integer, Integer> voteCnt = new HashMap<>();
        int pollId = pollService.selectRecentlyAddedPollId();

        for (Map<String, Object> contentMap : pollContentsMap) {
            PollContentEntity content = objectMapper.convertValue(contentMap, PollContentEntity.class);
            content.setPollId(pollService.get(pollId).getId());
            pollContentService.add(content);

            voteCnt.put(pollContentService.selectRecentlyAddedPollContentId(), 0);
        }
        rankingService.addPollCnt(pollId, voteCnt);

        return pollId;
    }
    @RequestMapping("/form")
    public String pollform(Model model, @RequestParam("pollId") int pollId, HttpSession httpSession) throws Exception {
        PollEntity pollEntity = null;
        PollContentEntity pollContentEntity = null;
        List<PollContentEntity> pollContentsList = null;
        pollEntity = pollService.get(pollId);
        pollContentsList = pollContentService.selectPollContents(pollId);

        model.addAttribute("poll",pollEntity);
        model.addAttribute("pollContents", pollContentsList);
        model.addAttribute("center", "poll_form");

        return "index";
    }
    @PostMapping(value = "/formimpl")
    public ResponseEntity<?> pollformimpl(@RequestBody List<PollContentEntity> pollContentList, @RequestParam("pollId") int pollId, HttpSession httpSession) throws Exception {
        pollContentList.forEach(content -> {
            try {
                pollContentService.modify(content);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        pollContentService.selectPollContents(pollId).forEach(content -> {
            if(content.getSelected()==true) {
                rankingService.incrementPollCnt(content.getPollId(), content.getId());
                pollService.addToSet(pollId, httpSession.getAttribute("userId").toString());
                pollContentService.addToSet(pollId, content.getId(), httpSession.getAttribute("userId").toString());
            }
        });
        pollContentList.forEach(content -> {
            try {
                content.setSelected(false);
                pollContentService.modify(content);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.ok("success");
    }
    @RequestMapping("/result")
    public String pollresult(Model model, @RequestParam("pollId") int pollId, HttpSession httpSession) throws Exception {
        PollEntity pollEntity = pollService.get(pollId);
        List<PollContentEntity> pollContentsList = pollContentService.selectPollContents(pollId);

        Map<String, String> rankings = rankingService.getAllPollContentsWithCntAndRanks(pollId);

        model.addAttribute("poll", pollEntity);
        model.addAttribute("pollContents", pollContentsList);

        if(pollService.isMember(pollId, httpSession.getAttribute("userId").toString())) {
            model.addAttribute("center", "poll_result");
        } else {
            model.addAttribute("center", "poll_form");
        }
        return "index";
    }
    @GetMapping("/resultimpl")
    public SseEmitter resultimpl(@RequestParam("pollId") int pollId) throws Exception {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        executor.execute(() -> {
            try {
                while (true) {
                    Map<String, String> rankings = rankingService.getAllPollContentsWithCntAndRanks(pollId);
                    emitter.send(rankings, MediaType.APPLICATION_JSON);
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @RequestMapping("/finalresult")
    public String pollfinalresult(Model model, @RequestParam("pollId") int pollId) throws Exception {
        PollEntity pollEntity = null;
        List<PollContentEntity> pollContentList = null;
        pollEntity = pollService.get(pollId);
        pollContentList = pollContentService.selectPollContents(pollId);

        if(rankingService.isResultSaved(pollId)) {
            pollContentList.forEach(content -> {
                content.setCnt(rankingService.getPollContentCnt(pollId, content.getId()));
                content.setRanking(rankingService.getPollContentRank(pollId, content.getId()));
                try {
                    pollContentService.modify(content);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            rankingService.deleteFinishedPoll(pollId);
        }

        model.addAttribute("poll",pollEntity);
        model.addAttribute("pollContents",pollContentList);
        model.addAttribute("center", "poll_final_result");

        return "index";
    }

    @GetMapping("/voteduser")
    public ResponseEntity<?> getVotedUsers(@RequestParam("pollId") int pollId, @RequestParam("contentId") int contentId) {
        Map<String, String> result = new HashMap<>();

        String contentName = pollContentService.findNameById(contentId);
        result.put("title", "'"+contentName+"' 투표");

        StringBuilder resultText = new StringBuilder();
        Set<String> users = pollService.findValues(pollId, contentId);
        pollService.findValues(pollId, contentId).forEach(u -> {
            resultText.append(userService.findNameById(Integer.parseInt(u)));
            resultText.append("\n");
        });

        if (resultText.length() > 0) {
            resultText.deleteCharAt(resultText.length() - 1);
        }
        result.put("text", resultText.toString());

        return ResponseEntity.ok(result);
    }
}

