# 외부접속 링크
http://faraway.iptime.org/login

# 개요
- Spring Boot와 데이터베이스를 연동한 CRUD를 구현하는 개인프로젝트

# 목표
- 평소 사용하고 있던 노트 프로그램인 에버노트와 같이 각 유저에게 개인이 저장소를 나눠 게시글을 CRUD 할 수 있는 공간을 할당할 수 있는 웹 애플리케이션 작성
![plan](image/../images/willdo_export.png)

# 사용 기술
## BackEnd
- JDK 11
- Spring Boot v2.7.10
- MyBatis
- Spring Security

## Database
- Oracle Database 19c

## FrontEnd
- HTML
- Thymleaf
- BootStrap v5.1

## IDE
- IntelliJ IDEA

# 구조도

## 1. View-Controller 와이어프레임
![View-Controller](images/Wireframe_export.png)

## 2. 서비스 흐름도
![Pages](images/pages.png)

## 3. MVC 계층
![MVC3계층](images/MVC3.png)
- MVC 계층에 따라 URL 루팅을 관리하는 Controller, 비즈니스 로직을 관리하는 Service, DAO를 관리하는 Repository 계층으로 코드를 나누어 진행함.

## 4. DB 구조
![DB구조](images/tables.png)
- 계정 정보를 저장하는 ACCOUNT_TBL, 저장소를 저장하는 CONTAINER_TBL, 게시글을 저장하는 POST_TBL을 생성함.
- 각 저장소와 포스트에 독립적인 키를 할당하기 위해 CONTAINER_SEQ_TBL와 POST_SEQ_TBL을 추가적으로 만들어 관리함.

# 한 것들
- DB 구성
- CRUD 코드 작성
- Spring Security를 활용한 로그인 페이지 코드 작성
- 외부 입력에 대한 검증 코드 작성
- 외부 접속 구성

# 하면서 깨달은 것

1. MyBatis를 XML로 구성할 경우 Java 코드와 SQL 코드가 분리되어 IDE 기능인 Ctrl + 좌클릭 으로 SQL 문으로 즉각적으로 확인하기 어려움
   - 이를 해결하기 위해 MyBatis 어노테이션을 활용함. 하지만 JDK11에서 """ 문을 활용할 수 없어 + 문을 써야 하기 때문에 가독성이 좀 안 좋다.

2. View -> Controller의 Model은 직접적으로 유저의 입력을 받는 부분이기 때문에 Validation에 신경을 많이 써야 함. 그리고 Controller 단에서 Validation을 해야하는 것을 배움.
   - Validation 할 때에는 hibernate 어노테이션과 스프링 BindingResult의 기능을 활용하는 것이 좋은 것 같다.

3. 외부 접속 관련해서 방화벽 규칙, 패스포워딩 등등을 배움. 실제로 넷상에서 접속을 할 수 있게 만듦. 근데 iptime 공유기를 통해서 하는 거라 CAA Record를 수정할 수 없어서 HTTPS 발급이 불가능한 것을 알았다.
    - 단순한 서버 배포만이 아니라 HTTPS 등 보안 접속이나 24시간 배포를 목적으로 한다면 AWS 등의 클라우드 서버를 생각해본다.


# 추가로 학습해야할 거...
- JPA (ORM)
- REST API 방식
- 클라우드 서버
- Spring Security
- Database 설계