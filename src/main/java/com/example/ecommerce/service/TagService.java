package com.example.ecommerce.service;

import com.example.ecommerce.dto.TagDto;
import com.example.ecommerce.model.Tag;
import com.example.ecommerce.repository.TagRepository;
import org.springframework.stereotype.Service;
import com.example.ecommerce.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TagDto getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        return convertToDto(tag);
    }

    public TagDto createTag(TagDto tagDto) {
        Tag tag = convertToEntity(tagDto);
        Tag savedTag = tagRepository.save(tag);
        return convertToDto(savedTag);
    }

    public TagDto updateTag(Long id, TagDto tagDto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        tag.setName(tagDto.getName());
        Tag updatedTag = tagRepository.save(tag);

        return convertToDto(updatedTag);
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    private TagDto convertToDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

    private Tag convertToEntity(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        return tag;
    }
}