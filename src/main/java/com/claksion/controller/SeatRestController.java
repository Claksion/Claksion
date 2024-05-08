package com.claksion.controller;

import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.service.RankingService;
import com.claksion.app.service.SeatSelectService;
import com.claksion.app.service.SeatService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/seat")
public class SeatRestController {

    final SeatService seatService;
    final SeatSelectService seatSelectService;

    final RedisTemplate<String, Object> redisTemplate;
    private final ConcurrentMap<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();
    int seatNum = 10;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(seatNum);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ConcurrentHashMap<String, Boolean> connections = new ConcurrentHashMap<>();
    @Autowired
    private RankingService rankingService;

    @PostMapping("/reset")
    public boolean resetSeat(@RequestParam(name = "classroomId") int classroomId) throws Exception {
        // REDIS reset
        Set<String> keys = redisTemplate.keys("seat:" + classroomId + ":*");
        for (String key : keys) {
            redisTemplate.delete(key);
        }
        redisTemplate.delete("completedSeat:" + classroomId);

        // SQL reset
        seatService.deleteByClassroomId(classroomId);

        String[] zoneArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        int numberMax = 4;

        for (String zone : zoneArray) {
            for (int number = 1; number <= numberMax; number++) {
                seatService.add(
                        SeatEntity.builder()
                                .classroomId(classroomId)
                                .zone(zone)
                                .number(number)
                                .build()
                );
            }
        }

        return true;
    }


    @RequestMapping("/firstselect")
    public String firstSelectSeat(@RequestParam(name = "seatId") int seatId, HttpSession session) throws Exception {
        log.info("seatId:" + seatId);
        SeatEntity seat = seatService.get(seatId);

//        seatSelectService.addToSet(seatId, session.getAttribute("userId").toString());

        String currentId = "task-" + seatId;
        ScheduledFuture<?> scheduledFuture = scheduler.schedule(() -> {
            seatSelectService.addQueue(
                    seat.getClassroomId(),
                    seatId,
                    (Integer) session.getAttribute("userId"));

            tasks.remove(currentId);
        }, 1, TimeUnit.SECONDS);


        try {
            scheduledFuture.get();
            SeatEntity seatEntity = SeatEntity.builder().id(seatId).userId((int) session.getAttribute("userId")).build();
            System.out.println(seatEntity.toString());
            try {
                seatService.updateUserSelected(seatEntity);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        scheduler.shutdown();

        tasks.put(currentId, scheduledFuture);
        System.out.println(scheduledFuture.toString());

        return currentId;
    }

    @RequestMapping("/checkselect")
    public boolean checkSelectSeat(@RequestParam("seatId") String  seatId, HttpSession session) throws Exception {
        SeatEntity seat = seatService.get(Integer.valueOf(seatId));
        tasks.forEach((id, future) -> {
            System.out.println(id.toString()+" "+future.toString());
            log.info(id.toString(), future.toString());
        });
        seatSelectService.addQueue(
                seat.getClassroomId(),
                Integer.parseInt(seatId),
                (Integer) session.getAttribute("userId"));
        return tasks.containsKey("task-"+seatId) && !tasks.get("task-"+seatId).isDone();
    }


    @GetMapping("/result/detail")
    public ResponseEntity<?> resultDetail(@RequestParam(name = "seatId") int seatId, HttpSession session) throws Exception {
        Map<String, String> result = new HashMap<>();

        SeatEntity seat = seatService.get(seatId);
        Set<Object> userSet = seatSelectService.getAllMembersFromZSet(seat.getClassroomId(), seatId);

        StringBuilder resultText = new StringBuilder();
        userSet.forEach(u -> {
            System.out.println(u.toString());
            resultText.append(u);
            resultText.append("\n");
        });

        if (resultText.length() > 0) {
            resultText.deleteCharAt(resultText.length() - 1);
        }
        result.put("text", resultText.toString());

        return ResponseEntity.ok(result);
    }
}
