package com.example.ecommerce.controller;

import com.example.ecommerce.dto.RoleDto;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleDto> roleDtos = roleService.convertToDtoList(roles);
        return ResponseEntity.ok(roleDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        RoleDto roleDto = roleService.convertToDto(role);
        return ResponseEntity.ok(roleDto);
    }

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        Role role = roleService.convertToEntity(roleDto);
        Role createdRole = roleService.createRole(role);
        RoleDto createdRoleDto = roleService.convertToDto(createdRole);
        return ResponseEntity.ok(createdRoleDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        Role role = roleService.convertToEntity(roleDto);
        Role updatedRole = roleService.updateRole(id, role);
        RoleDto updatedRoleDto = roleService.convertToDto(updatedRole);
        return ResponseEntity.ok(updatedRoleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}