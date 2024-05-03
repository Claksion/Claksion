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
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

    @Value("${app.oauth.naver.client-id}")
    String naverClientId;

    @Value("${app.oauth.naver.client-secret-key}")
    String naverClientSecretKey;

    public String getAccessToken(String authorizeCode) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://nid.naver.com/oauth2.0/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true); // POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(naverClientId);
            sb.append("&client_secret=").append(naverClientSecretKey);
            sb.append("&code=").append(authorizeCode);
            sb.append("&state=1234");
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
//            log.info(">> response body : " + result);

            JSONParser jsonParser = new JSONParser();
            JSONObject returnObject = (JSONObject) jsonParser.parse(result);

            access_Token = (String) returnObject.get("access_token");
            refresh_Token = (String) returnObject.get("refresh_token");

            log.info("access_token : " + access_Token);
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
        String reqURL = "https://openapi.naver.com/v1/nid/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
//            log.info("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("*** response body : " + result);

            JSONParser jsonParser = new JSONParser();
            JSONObject resultObject = (JSONObject) jsonParser.parse(result);

            JSONObject responseObject = (JSONObject) resultObject.get("response");

            String id = (String) responseObject.get("id");
            String thumbnail_image = (String) responseObject.get("profile_image");
            String email = (String) responseObject.get("email");

            userInfo = new UserInfo(OauthType.NAVER, id, email, thumbnail_image);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
