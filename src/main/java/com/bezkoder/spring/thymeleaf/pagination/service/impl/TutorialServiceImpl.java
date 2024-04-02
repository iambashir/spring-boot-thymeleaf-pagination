package com.bezkoder.spring.thymeleaf.pagination.service.impl;

import com.bezkoder.spring.thymeleaf.pagination.Dto.TutorialDTO;
import com.bezkoder.spring.thymeleaf.pagination.entity.Tutorial;
import com.bezkoder.spring.thymeleaf.pagination.repository.TutorialRepository;
import com.bezkoder.spring.thymeleaf.pagination.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
@RequiredArgsConstructor
public class TutorialServiceImpl implements TutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;

    private final ModelMapper modelMapper;

    @Override
    public Page<TutorialDTO> getAllTutorials(String keyword, int page, int size) {
        Pageable paging = (Pageable) PageRequest.of(page - 1, size);
        Page<Tutorial> pageTuts;
        if (keyword == null) {
            pageTuts = tutorialRepository.findAll((org.springframework.data.domain.Pageable) paging);
        } else {
            pageTuts = tutorialRepository.findByTitleContainingIgnoreCase(keyword, (org.springframework.data.domain.Pageable) paging);
        }
        return pageTuts.map(tutorial -> modelMapper.map(tutorial, TutorialDTO.class));
    }

    @Override
    public TutorialDTO getTutorialById(Integer id) {
        Tutorial tutorial = tutorialRepository.findById(id).orElse(null);
        return modelMapper.map(tutorial, TutorialDTO.class);
    }

    @Override
    public TutorialDTO saveTutorial(TutorialDTO tutorialDTO) {
        Tutorial tutorial = modelMapper.map(tutorialDTO, Tutorial.class);
        Tutorial savedTutorial = tutorialRepository.save(tutorial);
        return modelMapper.map(savedTutorial, TutorialDTO.class);
    }

    @Override
    public void deleteTutorial(Integer id) {
        tutorialRepository.deleteById(id);
    }

    @Override
    public void updateTutorialPublishedStatus(Integer id, boolean published) {
        tutorialRepository.updatePublishedStatus(id, published);
    }

}
