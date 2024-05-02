package com.claksion.controller;

import com.claksion.app.data.dto.UserInfo;
import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.data.entity.UserType;
import com.claksion.app.service.UserService;
import com.claksion.app.service.oauth.KakaoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

    @Value("${app.key.kakao.rest_api_key}")
    String kakaoRestApiKey;

    final private KakaoService kakaoService;
    final private UserService userService;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("kakaoRestApiKey", kakaoRestApiKey);
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpSession httpSession) {
        if (httpSession != null) {
//            loginCustRepository.deleteById((String) httpSession.getAttribute("id"));
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
                           @RequestParam(name = "email") String email
    ) throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name(name)
                .type(type)
                .oauthId(oauthId)
                .profileImg(profileImg)
                .email(email)
                .build();
        userService.add(userEntity);

        int userId = userService.getByOauthId(oauthId).getId();
        httpSession.setAttribute("userId", userId);

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

        UserEntity userEntity = userService.getByOauthId(userInfo.getOauthId());

        // 회원가입 전 > 회원가입 화면으로 이동
        if (userEntity == null) {
            model.addAttribute("userInfo", userInfo);
            return "register";
        }

        // 회원가입 후 > 로그인 정보 세션 저장
        httpSession.setAttribute("userId", userEntity.getId());

        // 반을 선택하지 않았다면 반 선택 화면으로 이동
        if (userEntity.getClassroomId() == 0) {
            return "redirect:/";
        }

        // 모든 설정이 완료되었다면 홈화면으로 이동
        return "redirect:/";
    }
}
