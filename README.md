# 🐳 Whale 🐳
___

### 🛡️ Spring Security 를 활용한 JWT 인증 방식을 구현해보기
- [x] ⚒️ User Domain 생성하기
- [x] ⚒️ Spring Security Config 생성하기
- [x] ⚒️ Login 성공했을 때 토큰 발행하기
    - [x] ⚒️ AccessToken 을 응답 헤더에 담아서 전달하기
    - [x] ⚒️ RefreshToken 을 쿠키에 담아서 전달하기 
- [x] ⚒️ Jwt Filter 생성하기
    - Refresh Token 과 AccessToken 을 통한 인증 방식을 구현한다
    - Refresh Token 의 유효 기간을 길게, AccessToken 의 유효 기간을 짧게 설정한다
    - Refresh Token 을 Redis 에 저장한다
- [x] ⚒️ JsonUsernamePasswordFilter 에서 Login 이 성공적으로 마치면 RefreshToken 과 AccessToken 을 발급하여 전달한다
- [x] ⚒️ JwtAuthenticationFilter 에서는 요청과 함께 전달된 AccessToken 과 RefreshToken 의 유효성을 검증한다

### 🔐 JWT 인증 진행 방식
![img.png](docs/resource/img.png)

토큰을 검사함과 동시에 각 경우에 대해서 토큰의 유효기간을 확인하여 재발급 여부를 결정한다

- 🐬 case1 : AccessToken 은 만료됐지만, RefreshToken은 유효한 경우 →  RefreshToken 을 검증하여 AccessToken 재발급
- 🐬 case2 : AccessToken 은 유효하지만, RefreshToken은 만료된 경우 →  AccessToken 을 검증하여 RefreshToken 재발급
- 🐬 case3 : AccessToken 과 RefreshToken 모두가 만료된 경우 → 에러 발생 (재 로그인하여 둘다 새로 발급)

---

### ERD Table

![image](https://github.com/BullChallenger/Whale/assets/87288460/4b44abc0-02cd-4331-8ce8-9df9219498d9)

---

![image](docs/resource/erd-v2.png)

---

### 🕶 Ecommerce 관련 도메인 추가

![image](docs/resource/Drawing%202024-02-05%2015.08.21.excalidraw.png)
**예상되는 전체적인 Ecommerce 도메인의 모습(추후 변경 가능성 높음)**

- 개인적으로 Ecommerce 도메인에서 가장 중요한 기능은 바로 주문이라고 생각함.
- 주문 진행 방식에 대해 좀 더 잘 이해하기 위해서 아래와 같이 주문의 흐름을 그림으로 표현
![image](docs/resource/Drawing%202024-02-07%2009.15.39.excalidraw.png)

---

### 생성한 Entity의 의존관계 상황 다이어그램
- 양방향 의존성과 객체 참조를 이용한 강한 연관관계로 인해 강합 결합도를 보인다.

**객체 참조를 통한 강한 연관관계는 객체 그룹의 조회 경계를 모호하게 만들고, 트랜잭션의 경계를 과도하게 부풀려 트랜잭션 경합으로 인한 성능 저하를 발생시킨다.** 
![image](/docs/resource/Whale_Dependency_Diagram.png)

이를 해결하기 위해 불필요하게 결합도를 높이는 객체 참조를 끊고, 양방향 연관관계 또한 제거하고자 한다.
