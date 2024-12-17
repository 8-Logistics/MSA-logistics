# Chapter 2. 대규모 AI 시스템 설계 프로젝트

## ✔️ 프로젝트 소개

- 물류 관리 및 배송 시스템을 위한 **MSA** 기반 플랫폼 개발
- B2B 물류 관리 및 배송 시스템
- **AI API**를 연동하여 주문이 들어오면 발송 허브 담당자에게 배송 예상 시간 알림이 가도록 지원합니다.

---

## 🧑🏻‍💻 팀원 역할 분담
- **민지수** : product-service, order-service(Slack, Gemini) 
- **김성훈** : gateway, user-service
- **서연주** : Hub-service, vendor-service
- **박강현** : Delivery-service

---
## ✔️ MSA 구성
- **server** : 유레카 서버
- **gateway** : 게이트웨이
- **user-service** : 사용자, 권한, 배달 담당자 서비스 (/api/v1/users, /api/v1/delivery-manager)
- **hub-service** : 허브, 허브 경로 서비스 (/api/v1/hubs, /api/v1/hubPaths)
- **vendor-service** : 업체 서비스 (/api/v1/vendors)
- **product-service** : 상품 서비스 (/api/v1/products)
- **order-service** : 주문, 슬랙 서비스 (/api/v1/orders)
- **delivery-service** : 배송, 배송 경로 서비스 (/api/v1/deliveries, /api/v1/deliveryPaths)

---

## ✔️ 인프라 설계서
![image](https://github.com/user-attachments/assets/93f38e64-6c1b-4d1f-9bdb-0ccd28cb2f08)

---

## ✔️ ERD
![image](https://github.com/user-attachments/assets/f2ddeaa5-433f-4de0-af59-800b5030738a)

---

## 💡 개발환경
- **IDE** : IntelliJ
- **Language** : Java 17
    - 보편적으로 가장 많이 사용되는 버전 중 하나로, 안정성이 검증되었으며 레퍼런스를 찾기 쉬워 개발에 도입하였습니다.
- **Framework** : Spring Boot 3
    - 널리 사용되는 프레임워크 버전으로, 다양한 레퍼런스와 커뮤니티 지원이 뛰어나 개발 및 유지보수에 용이하다고 판단했습니다.
- **TestTool** : PostMan, Swagger
- **형상관리** : GitHub

## ⚒️ 기술 스택

### 🛠 Back-end
- **Spring Security**
    - 역할 기반의 접근 제어를 구현하여 보안을 강화하였습니다.
- **JWT (JSON Web Token)**
    - 세션 방식보다 서버 부하가 적고, Stateless한 인증 관리가 가능합니다.
- **JPA (Java Persistence API)**
    - 데이터베이스와의 상호 작용을 위해 도입되었습니다.
- **QueryDSL**
    - 복잡한 쿼리 작성 시 타입 안전성과 가독성을 높이기 위해 사용되었습니다.
- **Swagger**
    - REST API를 자동 문서화하여 프론트엔드와의 협업 및 API 명세 공유를 원활히 지원합니다.

---

### 🧾 데이터베이스 / Infra
- **MySQL**
    - 성능과 안정성이 검증된 오픈소스 DB로, 확장성과 커뮤니티 지원이 뛰어나 프로젝트에 적합하다고 판단했습니다.
- **Redis**
    - 빠른 **캐싱 시스템**을 제공하여 **데이터 접근 속도**를 크게 개선하였습니다.
- **Docker Compose**
    - 개발 및 테스트 환경에서 **여러 컨테이너 기반 서비스**를 쉽고 빠르게 관리합니다.
- **ZipKin**
    - 분산 추적(Zipkin)은 마이크로서비스 간의 요청 흐름을 추적하고 시각화하여 성능 문제와 오류를 분석

---

## 🤝 협업 방식
- **버전 관리**
    - Git을 활용하여 코드의 버전 관리 하였으며, 팀원 간의 코드 충돌을 최소화하였습니다.
- **코드 리뷰**
    - PR을 통한 코드 리뷰를 통해 코드의 품질을 높이고, 팀원 간의 지식 공유를 촉진하였습니다.
- **협업 문서화**
    - API 설계 문서, ERD 다이어그램, 요구사항 정의서를 **Notion**에 정리하였습니다.
- **협업 환경 구축**
    - **Slack**과 **Zep**을 통해 업무 진행 상황을 공유하고, 해결이 필요한 문제를 논의했습니다.

---

## 🛠️ 트러블 슈팅

### 1. 문제: Redis 캐시에서 `LocalDateTime` 객체 직렬화 시 **SerializationException**
- **문제 상황**  
  성능 개선을 위해 Redis 캐시를 적용하였으나, Redis는 직렬화 가능한 객체만 저장 가능하므로 `LocalDateTime` 같은 비직렬화 객체는 문제가 발생하였습니다.

- **해결 과정**
    - 커스텀 직렬화 적용
        - `ObjectMapper`를 설정하여 `LocalDateTime`, `LocalTime` 객체도 직렬화 및 역직렬화 가능하도록 설정.
    - `jackson-datatype-jsr310` 모듈을 사용하여 **Java 8 날짜 및 시간 API**를 JSON 형식으로 직렬화/역직렬화가 가능하도록 처리.

- **결과**
    - `LocalDateTime` 직렬화 문제를 해결하여 Redis 캐시에 데이터 저장 및 조회가 원활해졌습니다.
    - **서비스 응답 시간**: 평균 800ms → **150ms**로 약 **81% 성능 개선**을 달성하였습니다.

---
### 2. 문제: 초기 설계에서 auth-service에만 security적용시 생기는 문제
- **문제 상황**  
  1. 공통적인 auditing, 권한 체크, 인증 등의 비용문제 발생(가독성 X, 통일성 X)
2. FeignClient에서는 Gateway를 통하지 않아 각 service별 통신에서 header 값을 넣어줘야 하는 문제 발생

- **해결 과정**
 - MSA 환경에서는 모든 service들이 서로 독립적이지만 팀 프로젝트이니 security 공통 적용도 괜찮다는 피드백을 통해 해결
- 모든 service에 공통적으로 security 적용해 @PreAuthorize, auditting 적용
- fegin 통신할때 FeignIntercepter로 custom인증 객체에서 가져온 데이터 추가
- 모든 요청들이 security filter를 통하여 관리되게 구축

- **결과**
    - 공통적으로 권한체크, auditing, 인증을 하는데 성공
    - 모든 통신은 filter로 관리하고 처리하는데 성공

---
### 3. 문제: AuditorAware를 이용해서 auditing 할때 일어난 문제
- **문제 상황**  
  1. p_user가 생성되는 시점이 회원가입 API인데 인증객체 안에 있는 username이 없었기 때문에 직접 추가.
2. 그러나 이벤트가 일어나며 추가한 값은 상관없이 인증객체에 값이 없어 anonymoususer가 컬럼에 들어가는 문제

- **해결 과정**
- ThreadLocal은 멀티 쓰레드 환경에서 좋지 않아 사용하지 않았다.
- HttpServletRequest 객체는 요청당 하나의 인스턴스이므로, 멀티스레드 환경에서도 충돌이 없다.(setattribute)
- 각 요청은 독립적인 객체이므로, 여러 요청이 동시에 들어와도 서로 영향을 주지 않는다고 함

- **결과**
    - 성공적으로 auditing 성공
---


## 🧑🏻‍🤝‍🧑🏻 팀원 소개 / 회고

### 김성훈
- **회고**:
  
  설계에 대한 중요성을 깨닫게 되었습니다. 초반 설계에서 조금씩 틀어지며 변경 될 때 사이드 이펙트로 인한 팀원들의 변경도 일어 날 수 있었기 때문에 서로의 의견을 조율해서 다시 설계하였습니다.
  모든 service로 통신하며 에러 추적이 어려웠습니다. 
  에러처리나 공통 response 처리를 config 서버나 공통 모듈을 통해 message를 통일화 시키는 것이 MSA 환경에서는 많은 서버가 연결되어 있으니 더욱 중요하게 느껴졌습니다.
  
### 민지수
- **회고**:  
  Feign 클라이언트를 사용해 서비스 간 통신을 구현하며 REST API 호출을 인터페이스로 추상화함으로써 코드의 가독성과 재사용성을 높였지만, 실제 운영 환경에서 발생할 수 있는 네트워크 장애와 요청 실패 시의 복구 전략에 대해 추가적인 고민이 필요하다는 점을 느꼈습니다.

### 박강현
- **회고**:

  MSA 환경에서 FeignClient를 사용해 팀원 간 API 통신을 구현하면서 여러 서비스 간 데이터를 주고받았습니다. 하지만 API 명세서는 작성되었음에도 실제 통합 과정에서 DTO의 필드명이 일치하지 않거나 URI가 다르게 설정되어 통신이 실패하는 문제가 자주 발생했습니다. 이 경험을 통해 API 명세서와 같은 기획 문서를 정확하게 작성하고 관리하는 것의 중요성을 느꼈습니다.
### 서연주
- **회고**:  
  MSA에 DDD를 적용한 개발은 개념과 설계 모두 쉽지 않았습니다. 특히 서비스 간의 데이터 흐름을 이해하고 분리된 도메인 간의 협력을 구현하는 과정이 어려웠습니다. 특히 FeignClient를 이용한 서비스 간의 통신에서의 예외처리와 연결 관리가 복잡하게 느껴졌습니다. CommonResponse를 설계하고 적용하는 것이 Debug 과정에도 도움이 되겠다고 느꼈습니다.

---
