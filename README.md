# Claksion 💥🚗💨
- [📌 프로젝트 소개 ](#-프로젝트-소개)
- [⚡️ 프로젝트 정보](#%EF%B8%8F-프로젝트-정보)
- [🔥 작업 기간](#-작업-기간)
- [🌳 목표](#-목표)
- [🛠️ Tech Stacks](#%EF%B8%8F-tech-stacks)
- [📚 프로젝트 구조](#-프로젝트-구조)
- [🦸🏻‍ Contributor](#-contributor)

<br/>

## 📌 프로젝트 소개 
> Redis를 활용한 교실 소통 플랫폼

학급 내 친구들과 함께 소통할 수 있는 플랫폼, **Claksion**입니다.
- 💬 친구들의 접속 현황을 실시간으로 확인하고, 단체 채팅방으로 소통할 수 있습니다. 
- 💺 빈번하게 진행하는 교실의 자리 배치를 웹에서 빠르고 간편하게 정할 수도 있습니다.
- ✅ 학급 내 투표하고 싶은 것이 있다면, 투표를 올려 결과를 확인해보세요! 익명도 가능합니다.

<br/>

## ⚡️ 프로젝트 정보
- [디지털 하나로](https://hanaro.recruiter.co.kr/career/home) 2기 개발반 1차 프로젝트

<br/>

## 🔥 작업 기간
- 2024.04.26 - 2024.05.10

<br/>

## 🌳 목표
> 🧙 교실 속 필요한 다양한 소통에 Redis의 장점을 활용하며 탐구  
- Redis의 **Sorted Set**과 AOP의 Around를 활용해 부하를 줄인 '선착순 자리선택'기능을 개발한다.
- Redis의 **Pub/Sub 패턴**과 **Socket**을 활용해 '실시간 채팅'기능을 개발한다.
- **Redis를 Session Storage로 사용**함으로써 빠른 응답속도의 장점을 취하고, 현재 서비스에 로그인돼 있는 클라이언트 정보를 출력한다.

<br/>

## 🛠️ Tech Stacks
#### Environment  
<img src="https://img.shields.io/badge/intellijidea-000000?style=flat&logo=intellijidea&logoColor=white">

#### Development  
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat&logo=Bootstrap&logoColor=white"> <img src="https://img.shields.io/badge/Javascript-F7DF1E?style=flat&logo=Javascript&logoColor=white">

#### DataBase
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white">

#### Communication
<img src="https://img.shields.io/badge/Slack-4A154B?style=flat&logo=Slack&logoColor=white"> <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white">

<br/>

## 📚 프로젝트 구조
```
📦 
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ claksion
   │  │        ├─ ClaksionApplication.java
   │  │        ├─ ServletInitializer.java
   │  │        ├─ app
   │  │        │  ├─ data
   │  │        │  │  ├─ dto
   │  │        │  │  │  ├─ ClassMate.java
   │  │        │  │  │  ├─ LoginUser.java
   │  │        │  │  │  ├─ OauthType.java
   │  │        │  │  │  ├─ SeatUser.java
   │  │        │  │  │  ├─ UserInfo.java
   │  │        │  │  │  ├─ enums
   │  │        │  │  │  │  └─ MessageType.java
   │  │        │  │  │  ├─ msg
   │  │        │  │  │  │  ├─ AdminMsg.java
   │  │        │  │  │  │  ├─ ChatRoom.java
   │  │        │  │  │  │  └─ Msg.java
   │  │        │  │  │  ├─ request
   │  │        │  │  │  │  ├─ ChatMessageRequest.java
   │  │        │  │  │  │  ├─ SelectSeatRequest.java
   │  │        │  │  │  │  └─ UpdateSeatUserRequest.java
   │  │        │  │  │  └─ response
   │  │        │  │  │     ├─ GetChatMessageResponse.java
   │  │        │  │  │     └─ GetSeatAndUserResponse.java
   │  │        │  │  └─ entity
   │  │        │  │     ├─ BaseEntity.java
   │  │        │  │     ├─ ClassroomEntity.java
   │  │        │  │     ├─ PollContentEntity.java
   │  │        │  │     ├─ PollEntity.java
   │  │        │  │     ├─ SeatEntity.java
   │  │        │  │     ├─ UserEntity.java
   │  │        │  │     └─ UserType.java
   │  │        │  ├─ frame
   │  │        │  │  ├─ BaseRepository.java
   │  │        │  │  └─ BaseService.java
   │  │        │  ├─ repository
   │  │        │  │  ├─ ClassroomRepository.java
   │  │        │  │  ├─ LoginUserRepository.java
   │  │        │  │  ├─ PollContentRepository.java
   │  │        │  │  ├─ PollRepository.java
   │  │        │  │  ├─ SeatRepository.java
   │  │        │  │  └─ UserRepository.java
   │  │        │  └─ service
   │  │        │     ├─ ClassroomService.java
   │  │        │     ├─ PollContentService.java
   │  │        │     ├─ PollService.java
   │  │        │     ├─ RankingService.java
   │  │        │     ├─ RedisMessageSubscriber.java
   │  │        │     ├─ SeatSelectService.java
   │  │        │     ├─ SeatService.java
   │  │        │     ├─ UserService.java
   │  │        │     ├─ WebSocketHandler.java
   │  │        │     ├─ aop
   │  │        │     │  ├─ AroundValidSeatOnRedis.java
   │  │        │     │  └─ SeatValidAop.java
   │  │        │     ├─ chat
   │  │        │     │  ├─ ChatRoomRepository.java
   │  │        │     │  ├─ MessageService.java
   │  │        │     │  ├─ RedisMessageStorage.java
   │  │        │     │  ├─ RedisPublisher.java
   │  │        │     │  ├─ RedisService.java
   │  │        │     │  └─ RedisSubscriber.java
   │  │        │     └─ oauth
   │  │        │        ├─ KakaoService.java
   │  │        │        └─ NaverService.java
   │  │        ├─ config
   │  │        │  ├─ JasyptConfig.java
   │  │        │  ├─ RedisConfig.java
   │  │        │  ├─ SecurityConfig.java
   │  │        │  └─ StomWebSocketConfig.java
   │  │        └─ controller
   │  │           ├─ ChatController.java
   │  │           ├─ ChatRoomController.java
   │  │           ├─ MainController.java
   │  │           ├─ MessageController.java
   │  │           ├─ PollController.java
   │  │           ├─ RedisController.java
   │  │           ├─ SeatController.java
   │  │           ├─ SeatRestController.java
   │  │           ├─ UserController.java
   │  │           └─ UserRestController.java
   │  ├─ resources
   │  │  ├─ application-aws.yml
   │  │  ├─ application-dev.yml
   │  │  ├─ application.yml
   │  │  ├─ log4jdbc.log4j2
   │  │  ├─ logback.properties
   │  │  ├─ mapper
   │  │  │  ├─ classroommapper.xml
   │  │  │  ├─ pollcontentmapper.xml
   │  │  │  ├─ pollmapper.xml
   │  │  │  ├─ seatmapper.xml
   │  │  │  └─ usermapper.xml
   │  └─ webapp
   │     └─ views
   │        ├─ chat.jsp
   │        ├─ chat
   │        │  ├─ room.jsp
   │        │  └─ roomdetail.jsp
   │        ├─ chatTest.jsp
   │        ├─ home.jsp
   │        ├─ index.jsp
   │        ├─ login.jsp
   │        ├─ loginother.jsp
   │        ├─ poll_creation.jsp
   │        ├─ poll_final_result.jsp
   │        ├─ poll_form.jsp
   │        ├─ poll_list.jsp
   │        ├─ poll_result.jsp
   │        ├─ register.jsp
   │        ├─ reservation.jsp
   │        ├─ seat.jsp
   │        ├─ seat_result.jsp
   │        └─ seat_select.jsp
```

<br/>

## 🦸🏻‍ Contributor
| **김하영** | **한원희** | **황혜림** |
| :------: |  :------: | :------: |
|[<img src="https://avatars.githubusercontent.com/u/90179774?v=4"  height=150 width=150> ](https://github.com/yhkkkkxx)| [<img src="https://avatars.githubusercontent.com/u/91041488?v=4"  height=150 width=150>](https://github.com/Wonhee0221) | [<img src="https://avatars.githubusercontent.com/u/70644449?v=4"  height=150 width=150>](https://github.com/hyerimmy) |

