# **Notebook**
### [프로젝트 외부접속 링크입니다.](http://faraway.iptime.org:131/login)

<br>

# 개요
- Spring Boot와 데이터베이스를 연동한 CRUD를 구현하는 개인프로젝트


- Notion이나 Evernote와 같이 로그인한 사용자가 독립적으로 데이터를 저장할 수 있는 공간을 만들어 보고 싶었음.

<br>

# 목표
>- 평소 사용하고 있던 노트 프로그램인 노션이나 에버노트와 같이 각 유저에게 개인이 저장소를 나눠 게시글을 CRUD 할 수 있는 공간을 할당할 수 있는 웹 애플리케이션 작성  

![plan](image/../images/willdo_export.png)

# 사용 기술
> ## BackEnd
- JDK 11
- Spring Boot v2.7.10
- MyBatis
- Spring Security
- Spring Validation
- Lombok

> ## Database
- Oracle Database 19c

> ## FrontEnd
- HTML
- Thymleaf
- BootStrap v5.1

> ## IDE
- IntelliJ IDEA

<br>

# 구조도

> ## 1. View-Controller 와이어프레임
![View-Controller](images/Wireframe_export.png)

<br>

> ## 2. 서비스 흐름도
![Pages](images/pages.png)

<br>

> ## 3. MVC 계층
![MVC3계층](images/MVC3.png)
- MVC 계층에 따라 URL 루팅을 관리하는 Controller, 비즈니스 로직을 관리하는 Service, DAO를 관리하는 Repository 계층으로 코드를 나누어 진행함.

<br>

> ## 4. DB 구조
![DB구조](images/tables.png)
- 계정 정보를 저장하는 ACCOUNT_TBL, 저장소를 저장하는 CONTAINER_TBL, 게시글을 저장하는 POST_TBL을 생성함.

- 각 저장소와 포스트에 독립적인 키를 할당하기 위해 CONTAINER_SEQ_TBL와 POST_SEQ_TBL을 추가적으로 만들어 관리함.

<br>

# 기능
> ## 로그인
- Spring Security를 활용하여 DB와 연동된 로그인 페이지를 구현함.

<details>
<summary>상세 내용 확인</summary>
<div markdown="1">

- SecurityFilterChain 을 활용하여 로그인과 로그아웃을 관리함.

```java
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.

            //...Builder Pattern

        return http.build();
    }

```
[SpringSecurityConfig.java](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/java/com/my/notebook/config/SpringSecurityConfig.java)

> ### BCryptPasswordEncoder()를 사용하여 Password를 Encoding 함.
```java
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
```

[SpringSecurityConfig.java](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/java/com/my/notebook/config/SpringSecurityConfig.java)


- UserDetailsService를 구현하여 데이터베이스와 연동함.

```Java
@Component
public class CustomUserDetailService implements UserDetailsService {
   //...

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EncodedAccountDTO> accountOptional = accountService.selectEncodedPasswordByUsername(username);
        EncodedAccountDTO account = accountOptional.orElseThrow(
                ()-> new UsernameNotFoundException("없는 회원입니다.")
        );

        //...
    }
}
```
[CustomUserDetailService.java](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/java/com/my/notebook/config/CustomUserDetailService.java)

</div>
</details>

<br>

> ## Controller : 검증 (Validation)
- Validation 어노테이션과 BindingResult를 활용하여 백엔드 데이터 검증을 실시함
- Thymeleaf의 기능을 활용해 사용자에게 즉각적인 피드백을 줄 수 있음.
<details>
<summary>상세 내용 확인</summary>
<div markdown="1">

- 가입 검증을 위해 RegisterForm은 다음과 같이 어노테이션과 함께 정규표현식을 사용하였음
- 다른 검증에 대해서도 다음과 같이 적절한 검증 어노테이션을 사용함
- 또한 검증을 사용하는 DTO 모델 이름은 ~Form으로 통일함. 

``` java
@Getter
@Setter
public class RegisterForm {

    // ...

    /**'
     * @Range를 Pattern에서 정규표현식으로 빼고 길이 제한을 할 수도 있지만
     * 길이 에러 메시지와 패턴 메시지를 따로 주기 위해 다 붙임
     */
    @NotNull
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^([a-z0-9]*)$")
    private String username;

    @NotNull
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^([A-Za-z0-9!@#$%]*)$")
    private String password;

    @NotNull
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^([A-Za-z0-9!@#$%]*)$")
    private String passwordCheck;
}
```
[RegisterForm.java](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/java/com/my/notebook/domain/account/RegisterForm.java)

- 어노테이션으로 할 수 없는 검증은 Controller 단에서 처리하였고, BindingResult를 통해 에러메시지를 추가함.

```java
@Slf4j
@Controller
@RequestMapping("/login")
public class AccountController {
   
   //...

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("registerForm") RegisterForm registerForm,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        log.info("errors = {}", bindingResult);

        // Validation
        if (!registerForm.getPassword().equals(registerForm.getPasswordCheck())){
            bindingResult.reject("passwordCheck", null, "비밀번호 확인이 맞지 않습니다.");
            return "login/registerForm";
        }

        if (bindingResult.hasErrors()){
            return "login/registerForm";
        }

        // Service
        LoginDTO loginDTO = new LoginDTO(registerForm.getUsername(), registerForm.getPassword());
        String registerMessage = accountService.register(loginDTO);

        // Looting
        // 쿼리 스트링 추가
        redirectAttributes.addAttribute("registerMessage", registerMessage);

        if (registerMessage.equals("ok")){
            return "redirect:/login";
        } else {
            bindingResult.reject("serviceError", null, registerMessage);
            return "login/registerForm";
        }
    }
}
```
[AccountController.java](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/java/com/my/notebook/controller/AccountController.java)

- properties 파일을 사용하여 에러 메시지를 관리하여 수정이 용이하게 함.

```java
Size.registerForm.username=유저이름은 {2}자에서 {1}자 길이의 문자로 이루어져야 합니다.
Size.registerForm.password=비밀번호는 {2}자에서 {1}자 길이의 문자로 이루어져야 합니다.
Size.registerForm.passwordCheck=비밀번호 확인은 {2}자에서 {1}자 길이의 문자로 이루어져야 합니다.

...
```
[errorMessages.properties](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/resources/messages/errorMessages.properties)


- Thymleaf의 기능을 활용해 사용자의 입력에 대한 검증 메시지를 보여줄 수 있도록 함.

```html
...

            <form th:action method="post" th:object="${registerForm}">

                <div th:if="${#fields.hasGlobalErrors()}">
                    <p class="text-danger" th:each="e : ${#fields.globalErrors()}" th:text="${e}"></p>
                </div>

                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name = "username" aria-describedby="username"
                           th:field="*{username}" th:errorclass="border-danger">

                    <div class="form-text" th:errors="*{username}" >아이디를 입력하세요</div>
                </div>

                ...

            </form>


...
```
[registerForm.html](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/resources/templates/login/registerForm.html)


</div>
</details>

<br>

> ## Repository : DB 연동
<details>
<summary>상세 내용 확인</summary>
<div markdown="1">

- @Mapper 어노테이션과 interface를 사용하여 MyBatis로 DB와 연동하였음.

```java
@Mapper
public interface ContainerMapper {
    @Insert("insert into CONTAINER_TBL (a_id, c_id, c_title)\n" +
            "VALUES (${accountId}, (select C_id_seq from CONTAINER_seq_tbl where a_id=${accountId}), #{containerTitle})")
    void insertContainer(CreateContainerDTO createContainerDTO);

    // ...

}

```

[ContainerMapper.java](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/java/com/my/notebook/mapper/ContainerMapper.java)


- 테스트를 위한 DDL 문 같은 경우는 XML로 따로 관리하였음.

```xml

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//ibatis.apache.org//DTD Mapper 3.0//EN" "http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd">
<mapper namespace="com.my.notebook.mapper.seq.AccountSeqMapper">

    <select id="getAccountIdSeqCurrval" resultType="_long">
        SELECT a_id_seq.currval FROM dual
    </select>

    <select id="createAccountIdSeq">
        CREATE SEQUENCE a_id_seq
            START WITH 1
            INCREMENT BY 1
            MINVALUE 1
            NOCYCLE
    </select>

    <select id="dropAccountIdSeq">
        BEGIN
            EXECUTE IMMEDIATE 'DROP SEQUENCE ' || 'a_id_seq';
        EXCEPTION
            WHEN OTHERS THEN
                IF SQLCODE != -2289 THEN
                    RAISE;
            END IF;
        END;
    </select>

</mapper>

```
[AccountSeqMapper.xml](https://github.com/INGPlay/SpringMVC_Practice/blob/main/notebook/src/main/resources/mapper/AccountSeqMapper.xml)

</div>
</details>

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