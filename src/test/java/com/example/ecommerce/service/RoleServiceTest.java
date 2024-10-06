package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRoles_ReturnsListOfRoles() {
        Role role1 = new Role();
        role1.setName("ADMIN");
        Role role2 = new Role();
        role2.setName("USER");

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        List<Role> roles = roleService.getAllRoles();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertEquals("ADMIN", roles.get(0).getName());
        assertEquals("USER", roles.get(1).getName());
    }

    @Test
    void getRoleById_ValidId_ReturnsRole() {
        Long roleId = 1L;
        Role role = new Role();
        role.setName("ADMIN");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        Role foundRole = roleService.getRoleById(roleId);

        assertNotNull(foundRole);
        assertEquals("ADMIN", foundRole.getName());
    }

    @Test
    void getRoleById_InvalidId_ThrowsResourceNotFoundException() {
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            roleService.getRoleById(roleId);
        });
    }

    @Test
    void createRole_SavesAndReturnsRole() {
        Role role = new Role();
        role.setName("USER");

        when(roleRepository.save(role)).thenReturn(role);

        Role createdRole = roleService.createRole(role);

        assertNotNull(createdRole);
        assertEquals("USER", createdRole.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void updateRole_ValidId_UpdatesAndReturnsRole() {
        Long roleId = 1L;
        Role existingRole = new Role();
        existingRole.setName("Old Role");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));

        Role updatedRole = new Role();
        updatedRole.setName("New Role");

        when(roleRepository.save(existingRole)).thenReturn(existingRole);

        Role result = roleService.updateRole(roleId, updatedRole);

        assertNotNull(result);
        assertEquals("New Role", result.getName());

        verify(roleRepository, times(1)).save(existingRole);
    }


    @Test
    void updateRole_InvalidId_ThrowsResourceNotFoundException() {
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        Role updatedRole = new Role();
        updatedRole.setName("New Role");

        assertThrows(ResourceNotFoundException.class, () -> {
            roleService.updateRole(roleId, updatedRole);
        });
    }

    @Test
    void deleteRole_ValidId_DeletesRole() {
        Long roleId = 1L;
        Role role = new Role();
        role.setName("USER");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        roleService.deleteRole(roleId);

        verify(roleRepository, times(1)).deleteById(roleId);
    }
}