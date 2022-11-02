package net.dudios.invoicemanagerbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudios.invoicemanagerbackend.security.SecurityConfig;
import net.dudios.invoicemanagerbackend.user.AppUser;
import net.dudios.invoicemanagerbackend.user.Role;
import net.dudios.invoicemanagerbackend.user.UserController;
import net.dudios.invoicemanagerbackend.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class AppUserControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;

    @MockBean
    private UserService appUserService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private SecurityConfig securityConfig;


    @Autowired
    public AppUserControllerTests(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;

    }

    @Test
    public void shouldAddUser() throws Exception {
        //Given
        var user = AppUser.builder().id(1L).username("filip")
                .password("1234").email("filipduda99@wp.pl").roles(Role.ADMIN.name()).build();
        given(appUserService.addUser(any(AppUser.class))).willReturn(user);

        //When
        var result = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.post("/users")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZHJpYW4iLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoiQWRtaW4ifQ.50HBc1ZORtEVIblAmLYAJ0HzF63M0wu_h0vMnjn6J0U")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(), AppUser.class);

        //Then
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRoles(), result.getRoles());
        //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZHJpYW4iLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoiQWRtaW4ifQ.50HBc1ZORtEVIblAmLYAJ0HzF63M0wu_h0vMnjn6J0U

    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        //Given
        var userList = List.of(AppUser.builder().id(1L).username("filip")
                        .password("1234").email("filipduda99@wp.pl").roles(Role.ADMIN.name()).build(),
                AppUser.builder().id(2L).username("jan")
                        .password("1234").email("janKowalski@wp.pl").roles(Role.ADMIN.name()).build(),
                AppUser.builder().id(3L).username("kasia")
                        .password("1234").email("kasia12@wp.pl").roles(Role.ADMIN.name()).build());

        given(appUserService.getAllUsers()).willReturn(userList);

        //When
        var result = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), AppUser[].class);

        //Then
        assertNotNull(result);
        assertEquals(userList.size(), result.length);
        assertEquals(userList.get(0).getId(), result[0].getId());
        assertEquals(userList.get(0).getUsername(), result[0].getUsername());
        assertEquals(userList.get(1).getPassword(), result[1].getPassword());
        assertEquals(userList.get(2).getEmail(), result[2].getEmail());
        assertEquals(userList.get(0).getRoles(), result[0].getRoles());
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        //Given
        var user = AppUser.builder().id(1L).username("filip")
                .password("1234").email("filipduda99@wp.pl").roles(Role.ADMIN.name()).build();

        //When
        var result = mockMvc.perform(MockMvcRequestBuilders.delete("/users")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();
        //Then
        assertNotNull(result);


    }

    @Test
    public void shouldGetUserById() throws Exception {
        //Given
        var user = AppUser.builder().id(1L).username("filip")
                .password("1234").email("filipduda99@wp.pl").roles(Role.ADMIN.name()).build();
        given(appUserService.getUser(any(Long.class))).willReturn(user);

        //When
        var result = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), AppUser.class);

        //Then
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRoles(), result.getRoles());
    }

}
