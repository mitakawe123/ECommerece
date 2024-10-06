package com.example.ecommerce.service;

import com.example.ecommerce.dto.TagDto;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Tag;
import com.example.ecommerce.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findTagById_ValidId_ReturnsTag() {
        Tag tag = new Tag();
        tag.setName("Food");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        var foundTag = tagService.getTagById(1L);

        assertNotNull(foundTag);
        assertEquals("Food", foundTag.getName());
    }

    @Test
    void findTagById_InvalidId_ThrowsResourceNotFoundException() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tagService.getTagById(1L);
        });
    }

    @Test
    void getAllTags_ReturnsListOfTags() {
        Tag tag1 = new Tag();
        tag1.setName("Food");
        Tag tag2 = new Tag();
        tag2.setName("Drink");

        when(tagRepository.findAll()).thenReturn(Arrays.asList(tag1, tag2));

        var tags = tagService.getAllTags();

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertEquals("Food", tags.get(0).getName());
        assertEquals("Drink", tags.get(1).getName());
    }

    @Test
    void createTag_SavesAndReturnsTag() {
        var tagDto = new TagDto();
        tagDto.setName("Food");

        var tagEntity = new Tag();
        tagEntity.setName("Food");

        when(tagRepository.save(any(Tag.class))).thenReturn(tagEntity);

        var createdTag = tagService.createTag(tagDto);

        assertNotNull(createdTag);
        assertEquals("Food", createdTag.getName());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void updateTag_ValidId_UpdatesAndReturnsTag() {
        Long tagId = 1L;

        var existingTag = new Tag();
        existingTag.setName("Old Food");

        var updatedTagDto = new TagDto();
        updatedTagDto.setName("New Food");

        when(tagRepository.findById(tagId)).thenReturn(Optional.of(existingTag));

        when(tagRepository.save(existingTag)).thenReturn(existingTag);

        var result = tagService.updateTag(tagId, updatedTagDto);

        assertNotNull(result);
        assertEquals("New Food", result.getName());
        verify(tagRepository, times(1)).findById(tagId);
        verify(tagRepository, times(1)).save(existingTag);
    }

    @Test
    void updateTag_InvalidId_ThrowsResourceNotFoundException() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        var updatedTag = new TagDto();
        updatedTag.setName("New Food");

        assertThrows(ResourceNotFoundException.class, () -> {
            tagService.updateTag(1L, updatedTag);
        });
    }

    @Test
    void deleteTag_ValidId_DeletesTag() {
        Tag tag = new Tag();
        tag.setName("Food");
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        tagService.deleteTag(1L);

        verify(tagRepository, times(1)).deleteById(1L);
    }
}