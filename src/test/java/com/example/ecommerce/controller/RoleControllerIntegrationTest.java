package com.example.ecommerce.controller;

import com.example.ecommerce.model.Role;
import com.example.ecommerce.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
public class RoleControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRoles_ReturnsListOfRoles() throws Exception {
        Role role1 = new Role();
        role1.setName("ROLE_USER_MAIN");
        Role role2 = new Role();
        role2.setName("ROLE_ADMIN_MAIN");

        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);

        roleRepository.saveAll(roles);

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[*].name", hasItem("ROLE_USER_MAIN")));
    }

    @Test
    void getRoleById_ReturnsRole() throws Exception {
        Role role = new Role();
        role.setName("ROLE_USER_MAIN");
        role = roleRepository.save(role);

        mockMvc.perform(get("/api/roles/{id}", role.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ROLE_USER_MAIN")));
    }

    @Test
    void createRole_ReturnsCreatedRole() throws Exception {
        Role newRole = new Role();
        newRole.setName("ROLE_MANAGER");

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ROLE_MANAGER")));
    }

    @Test
    void updateRole_ReturnsUpdatedRole() throws Exception {
        Role role = new Role();
        role.setName("ROLE_USER_MAIN");
        role = roleRepository.save(role);

        Role updatedRole = new Role();
        updatedRole.setName("ROLE_SUPERVISOR");

        mockMvc.perform(put("/api/roles/{id}", role.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ROLE_SUPERVISOR")));
    }

    @Test
    void deleteRole_ReturnsNoContent() throws Exception {
        Role role = new Role();
        role.setName("ROLE_USER_MAIN");
        role = roleRepository.save(role);

        mockMvc.perform(delete("/api/roles/{id}", role.getId()))
                .andExpect(status().isNoContent());

        Optional<Role> deletedRole = roleRepository.findById(role.getId());
        assert(deletedRole.isEmpty());
    }
}
