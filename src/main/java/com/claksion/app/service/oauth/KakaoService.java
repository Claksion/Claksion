package com.claksion.app.service.oauth;

import com.claksion.app.data.dto.OauthType;
import com.claksion.app.data.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${app.oauth.kakao.rest-api-key}")
    String kakaoRestApiKey;

    public String getAccessToken(String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true); // POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(kakaoRestApiKey);
            sb.append("&code=" + authorize_code);
            sb.append("&redirect_uri=").append("http://localhost:80/user/login/kakao/oauth");
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
//            log.info("responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//            log.info("response body : " + result);

            JSONParser jsonParser = new JSONParser();
            JSONObject returnObject = (JSONObject) jsonParser.parse(result);

            access_Token = (String) returnObject.get("access_token");
            refresh_Token = (String) returnObject.get("refresh_token");

//            log.info("access_token : " + access_Token);
//            log.info("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    public UserInfo getUserInfo(String accessToken) {
        // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        UserInfo userInfo = null;
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
//            log.info("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//            log.info("response body : " + result);

            JSONParser jsonParser = new JSONParser();
            JSONObject responseObject = (JSONObject) jsonParser.parse(result);

            JSONObject properties = (JSONObject) responseObject.get("properties");
            JSONObject kakao_account = (JSONObject) responseObject.get("kakao_account");

            long id = (long) responseObject.get("id");
            String thumbnail_image = (String) properties.get("thumbnail_image");
            String email = (String) kakao_account.get("email");

            userInfo = new UserInfo(OauthType.KAKAO, Long.toString(id), email,thumbnail_image);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
