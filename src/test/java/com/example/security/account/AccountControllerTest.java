package com.example.security.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *  [권한에 따른 접근 테스트]
 *  Security Test 에서 인증을 mocking 할 수 있음.
 *  1. perform(get("/").with(anonymous())) 과 같이 with 구문 활용
 *  2. @WithMockUser(username = "tester", roles = "USER") 과 같이 어노테이션 활용
 *
 *  [Form Login 테스트]
 *  Security Test 에서 제공하는 formLogin method 사용
 *  테스트가 끝나면 작업들이 롤백 처리되도록 @Transactional 어노테이션 추가.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    @DisplayName("익명 사용자의 인덱스 페이지 접근")
    void index_anonymous() throws Exception {
        mockMvc.perform(get("/").with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("일반 사용자의 인덱스 페이지 접근")
    @WithMockUser(username = "tester", roles = "USER")
    void index_user() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("일반 사용자의 어드민 페이지 접근")
    void admin_user() throws Exception {
        mockMvc.perform(get("/admin").with(user("tester").roles("USER")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("어드민 사용자의 어드민 페이지 접근")
    @WithMockUser(username = "tester", roles = "ADMIN")
    void admin_admin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Form Login 성공 테스트")
    @Transactional
    void login() throws Exception {
        String username = "tester";
        String password = "123";
        createUser(username, password);

        mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("Form Login 실패 테스트")
    @Transactional
    void login_fail() throws Exception {
        String username = "tester";
        String password = "123";
        createUser(username, password);

        mockMvc.perform(formLogin().user(username).password("321"))
                .andExpect(unauthenticated());
    }

    private void createUser(String username, String password) {
        Account account = Account.builder()
                .username(username)
                .password(password)
                .role("USER")
                .build();

        accountService.createNew(account);
    }
}