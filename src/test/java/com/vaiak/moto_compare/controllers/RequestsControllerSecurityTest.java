package com.vaiak.moto_compare.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.security.SecurityConfig;
import com.vaiak.moto_compare.security.jwt.JwtTokenProvider;
import com.vaiak.moto_compare.services.UserRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RequestsController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = "frontend.origin=http://localhost:3000")
class RequestsControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRequestService userRequestService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void anonymousUserCannotAccessAdminRequestsEndpoint() throws Exception {
        mockMvc.perform(get("/api/motorcycles/requests"))
                .andExpect(status().isForbidden());
    }

    @Test
    void nonAdminUserGetsForbiddenForAdminRequestsEndpoint() throws Exception {
        mockMvc.perform(get("/api/motorcycles/requests").with(user("user@example.com").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessRequestsEndpoint() throws Exception {
        when(userRequestService.getAllRequests()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/motorcycles/requests").with(user("admin@example.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void anonymousUserCanSubmitNewMotorcycleRequest() throws Exception {
        UserRequest createdRequest = UserRequest.builder()
                .requestContent("{\"manufacturer\":\"HONDA\",\"model\":\"NC750X\",\"yearRange\":\"2024\"}")
                .build();

        when(userRequestService.createNewMotorcycleRequest(any(), any())).thenReturn(createdRequest);

        mockMvc.perform(post("/api/motorcycles/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  \"manufacturer\": \"HONDA\",
                                  \"model\": \"NC750X\",
                                  \"yearRange\": \"2024\"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"manufacturer\":\"HONDA\",\"model\":\"NC750X\",\"yearRange\":\"2024\"}"));
    }
}



