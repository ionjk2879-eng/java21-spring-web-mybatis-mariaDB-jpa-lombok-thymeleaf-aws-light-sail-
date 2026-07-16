# java21-spring-web-mybatis-mariaDB-jpa-lombok-thymeleaf-aws-light-sail-
# Spring Boot 게시판

Java 21 + Spring Boot + JPA/MyBatis + MariaDB로 구현한 서버 사이드 렌더링 게시판입니다.
회원 관리, 게시글 CRUD, 댓글, 검색 기능을 갖췄습니다.

> ⚠️ 학습 및 비용 절감을 위해 AWS Lightsail 배포는 현재 중단된 상태입니다.
> 아래 가이드를 따라 로컬 환경에서 동일하게 실행해볼 수 있습니다.

---

## 사용 기술

**Back-End**
- Java 21
- Spring Boot 4.0.7
- Spring Data JPA — 정적 쿼리(CRUD)
- MyBatis — 동적 쿼리(검색 기능)
- Lombok

**Front-End**
- Thymeleaf (서버 사이드 렌더링)
- Bootstrap 5.3

**Database**
- MariaDB

**Infra / 배포**
- AWS Lightsail (Ubuntu)
- Nginx (리버스 프록시)

---

## 로컬 실행 방법

### 1. 사전 준비물

- JDK 21
- MariaDB (로컬 설치 또는 Docker)
- Git

### 2. 저장소 클론

```bash
git clone https://github.com/ionjk2879-eng/java21-spring-web-mybatis-mariaDB-jpa-lombok-thymeleaf-aws-light-sail-.git
cd java21-spring-web-mybatis-mariaDB-jpa-lombok-thymeleaf-aws-light-sail-
```

### 3. MariaDB 준비

**옵션 A) 로컬에 직접 설치한 경우**

```sql
CREATE DATABASE exam_board CHARACTER SET utf8mb4;
CREATE USER 'boarduser'@'localhost' IDENTIFIED BY '원하는비밀번호';
GRANT ALL PRIVILEGES ON exam_board.* TO 'boarduser'@'localhost';
FLUSH PRIVILEGES;
```

**옵션 B) Docker로 간단히 띄우는 경우**

```bash
docker run -d --name board-mariadb \
  -e MARIADB_DATABASE=exam_board \
  -e MARIADB_USER=boarduser \
  -e MARIADB_PASSWORD=원하는비밀번호 \
  -e MARIADB_ROOT_PASSWORD=root비밀번호 \
  -p 3306:3306 \
  mariadb:11
```

### 4. 환경 설정

`src/main/resources/application.properties`에서 DB 접속 정보를 로컬 환경에 맞게 수정합니다.

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/exam_board
spring.datasource.username=boarduser
spring.datasource.password=원하는비밀번호
```

### 5. 빌드 & 실행

```bash
./gradlew bootRun
```

또는 IntelliJ 등 IDE에서 `Exam0625Application.java`를 직접 실행해도 됩니다.

### 6. 접속 확인

브라우저에서 아래 주소로 접속하면 게시판 화면을 확인할 수 있습니다.

```
http://localhost:8080
```

---

## 주요 기능

### 회원 기능
- 회원가입 (아이디/이메일 중복 검증, 비밀번호 확인)
- 로그인 / 로그아웃 (HttpSession 기반)
- 마이페이지 (회원 정보 조회)

### 게시글 CRUD
- 게시글 작성 / 조회 / 수정 / 삭제
- 조회수 기능
- 내가 작성한 게시글 모아보기
- 작성자 본인만 수정/삭제 가능 (권한 체크)

### 댓글 기능
- 댓글 작성 / 수정 / 삭제
- 인라인 수정 (JavaScript 토글)
- 작성자 본인만 수정/삭제 가능

### 검색 기능
- 제목 / 내용 / 작성자 기준 검색
- MyBatis 동적 쿼리로 구현
- 페이지네이션 적용

---

## 화면 구성

| 페이지 | URL | 설명 |
| --- | --- | --- |
| 메인 | `/` | 환영 메시지, 네비게이션 |
| 회원가입 | `/member/register` | 아이디, 비밀번호, 닉네임, 이메일 입력 |
| 로그인 | `/member/login` | 아이디, 비밀번호 입력 |
| 마이페이지 | `/member/mypage` | 회원 정보 조회 |
| 게시글 목록 | `/board/list` | 전체 게시글 목록, 검색, 페이지네이션 |
| 게시글 작성 | `/board/write` | 제목, 내용 입력 (로그인 필요) |
| 게시글 상세 | `/board/detail/{id}` | 게시글 내용, 댓글 목록 |
| 게시글 수정 | `/board/edit/{id}` | 게시글 수정 (작성자만) |
| 내 게시글 | `/board/my` | 내가 작성한 게시글 목록 |

---

## ERD (Entity Relationship Diagram)

```
+------------+       +------------+       +------------+
|   member   |       |   board    |       |  comments  |
+------------+       +------------+       +------------+
| id (PK)    |<──┐   | id (PK)    |<──┐   | id (PK)    |
| username   |   │   | title      |   │   | content    |
| password   |   │   | content    |   │   | created_at |
| nickname   |   ├──>| member_id  |   ├──>| board_id   |
| email      |   │   | view_count |   │   | member_id  |
| created_at |   │   | created_at |   │   +------------+
| updated_at |   │   | updated_at |   │        │
+------------+   │   +------------+   │        │
                 │                    │        │
                 └────────────────────┴────────┘
```

| 테이블 | 설명 | 관계 |
| --- | --- | --- |
| member | 회원 정보 | - |
| board | 게시글 | member : board = 1 : N |
| comments | 댓글 | member : comments = 1 : N, board : comments = 1 : N |

---

## 프로젝트 구조

```
src/main/java/com/example/exam0625/
├── controller/
│   ├── HomeController.java         # 메인 페이지
│   ├── MemberController.java       # 회원 관련 요청 처리
│   ├── BoardController.java        # 게시글 관련 요청 처리
│   └── CommentController.java      # 댓글 관련 요청 처리
├── service/
│   ├── MemberService.java          # 회원 비즈니스 로직
│   ├── BoardService.java           # 게시글 비즈니스 로직
│   └── CommentService.java         # 댓글 비즈니스 로직
├── repository/
│   ├── MemberRepository.java       # 회원 JPA Repository
│   ├── BoardRepository.java        # 게시글 JPA Repository
│   └── CommentRepository.java      # 댓글 JPA Repository
├── mapper/
│   └── BoardMapper.java            # 게시글 검색 MyBatis Mapper
├── entity/
│   ├── Member.java                 # 회원 엔티티
│   ├── Board.java                  # 게시글 엔티티
│   └── Comment.java                # 댓글 엔티티
├── dto/
│   ├── MemberRegisterDto.java      # 회원가입 DTO
│   ├── MemberLoginDto.java         # 로그인 DTO
│   ├── BoardWriteDto.java          # 게시글 작성 DTO
│   ├── BoardSearchDto.java         # 게시글 검색 DTO
│   └── CommentWriteDto.java        # 댓글 작성 DTO
└── Exam0625Application.java        # 메인 클래스

src/main/resources/
├── mapper/
│   └── BoardMapper.xml             # MyBatis 검색 쿼리 XML
├── templates/
│   ├── layout/header.html          # 공통 헤더 (네비게이션)
│   ├── index.html                  # 메인 페이지
│   ├── member/                     # 회원 관련 화면
│   │   ├── register.html
│   │   ├── login.html
│   │   └── mypage.html
│   └── board/                      # 게시글 관련 화면
│       ├── list.html
│       ├── write.html
│       ├── detail.html
│       ├── edit.html
│       └── my.html
└── application.properties          # 애플리케이션 설정
```

---

## 기술적 의사결정

### JPA + MyBatis 혼용

| 구분 | 사용 기술 | 이유 |
| --- | --- | --- |
| CRUD (정적 쿼리) | Spring Data JPA | 단순 CRUD는 JPA Repository로 간결하게 처리 |
| 검색 (동적 쿼리) | MyBatis | 조건별 동적 쿼리 생성에 MyBatis의 XML 매핑이 유리 |

### N+1 문제 해결
- 게시글 목록 조회 시 작성자(Member) 정보를 함께 로드해야 하는 상황
- `@EntityGraph(attributePaths = {"member"})` 적용으로 JOIN FETCH 처리
- 게시글 10개 조회 시 기존 11쿼리 → 1쿼리로 최적화

### 조회수 증가 최적화
- 기존: `SELECT` 후 엔티티 수정 (2쿼리)
- 개선: `@Modifying @Query`로 JPQL UPDATE 1쿼리 처리

### MyBatis ResultMap 활용
- MyBatis로 검색 시 JPA 엔티티의 연관 객체(Member)가 매핑되지 않는 문제 발생
- `resultMap`에 `association` 태그로 Member 객체 매핑 처리

---

## 트러블 슈팅

### 1. MariaDB 예약어 충돌
- **문제**: `comment`가 MariaDB 예약어라 테이블 생성 시 FK 제약조건 오류 발생
- **해결**: `@Table(name = "comments")` 어노테이션으로 테이블명 변경

### 2. Hibernate DDL 호환성 문제
- **문제**: Hibernate 7.x가 생성하는 `ALTER TABLE IF EXISTS` 구문을 MariaDB 10.4가 지원하지 않음
- **원인**: `ALTER TABLE IF EXISTS`는 MariaDB 10.5.2부터 지원
- **해결**: 로컬 환경에서는 FK 제약조건을 수동 추가, AWS 서버는 MariaDB 10.6+ 사용으로 자동 해결

### 3. 세션 직렬화 문제
- **문제**: `Member` 엔티티를 HttpSession에 저장하지만 `Serializable` 미구현
- **해결**: `Member` 클래스에 `implements Serializable` 추가

---

## 향후 개선 사항

- Spring Security 적용 (비밀번호 암호화, 인증/인가)
- 파일 업로드 기능 (게시글 이미지 첨부)
- 회원 정보 수정 기능
- REST API 전환 및 프론트엔드 분리
- Docker 컨테이너화 및 CI/CD 파이프라인 구축