package com.claksion.controller;

import com.claksion.app.data.dto.LoginUser;
import com.claksion.app.data.dto.UserInfo;
import com.claksion.app.data.entity.ClassroomEntity;
import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.data.entity.UserType;
import com.claksion.app.repository.LoginUserRepository;
import com.claksion.app.service.ClassroomService;
import com.claksion.app.service.UserService;
import com.claksion.app.service.oauth.KakaoService;
import com.claksion.app.service.oauth.NaverService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

    private final NaverService naverService;
    private final LoginUserRepository loginUserRepository;


    @Value("${app.oauth.kakao.rest-api-key}")
    String kakaoRestApiKey;

    @Value("${app.oauth.naver.client-secret-key}")
    String naverClientSecretKey;

    @Value("${app.oauth.naver.client-id}")
    String naverClientId;

    final private KakaoService kakaoService;
    final private UserService userService;
    final private ClassroomService classroomService;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("kakaoRestApiKey", kakaoRestApiKey);
        model.addAttribute("naverClientSecretKey", naverClientSecretKey);
        model.addAttribute("naverClientId", naverClientId);
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpSession httpSession) {
        if (httpSession != null) {
            loginUserRepository.deleteById((int) httpSession.getAttribute("userId"));
            httpSession.invalidate();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model,
                           HttpSession httpSession,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "type") UserType type,
                           @RequestParam(name = "oauthId") String oauthId,
                           @RequestParam(name = "profileImg") String profileImg,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "classroomId") int classroomId
    ) throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name(name)
                .type(type)
                .oauthId(oauthId)
                .profileImg(profileImg)
                .email(email)
                .classroomId(classroomId)
                .build();
        userService.add(userEntity);

        UserEntity user = userService.getByOauthId(oauthId);
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("userName", user.getName());
        httpSession.setAttribute("userProfileImg", user.getProfileImg());
        loginUserRepository.save(new LoginUser(user.getId()));

        return "redirect:/";
    }

    @RequestMapping(value = "/login/kakao/oauth", method = RequestMethod.GET)
    public String kakaoOauth(
            Model model,
            HttpSession httpSession,
            @RequestParam(value = "code", required = false) String code
    ) throws Exception {
        String accessToken = kakaoService.getAccessToken(code);
        UserInfo userInfo = kakaoService.getUserInfo(accessToken);

        UserEntity user = userService.getByOauthId(userInfo.getOauthId());

        // 회원가입 전 > 회원가입 화면으로 이동
        if (user == null) {
            model.addAttribute("userInfo", userInfo);

            List<ClassroomEntity> classrooms = classroomService.get();
            model.addAttribute("classrooms", classrooms);

            return "register";
        }

        // 회원가입 후 > 홈 화면으로 이동
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("userName", user.getName());
        httpSession.setAttribute("userProfileImg", user.getProfileImg());
        loginUserRepository.save(new LoginUser(user.getId()));
        return "redirect:/";
    }

    @RequestMapping(value = "/login/naver/oauth", method = RequestMethod.GET)
    public String naverOauth(
            Model model,
            HttpSession httpSession,
            @RequestParam(value = "code", required = false) String code
    ) throws Exception {
        String accessToken = naverService.getAccessToken(code);
        UserInfo userInfo = naverService.getUserInfo(accessToken);

        UserEntity user = userService.getByOauthId(userInfo.getOauthId());

        // 회원가입 전 > 회원가입 화면으로 이동
        if (user == null) {
            model.addAttribute("userInfo", userInfo);

            List<ClassroomEntity> classrooms = classroomService.get();
            model.addAttribute("classrooms", classrooms);

            return "register";
        }

        // 회원가입 후 > 홈 화면으로 이동
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("userName", user.getName());
        httpSession.setAttribute("userProfileImg", user.getProfileImg());
        loginUserRepository.save(new LoginUser(user.getId()));
        return "redirect:/";
    }
}
