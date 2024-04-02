package com.bezkoder.spring.thymeleaf.pagination.service;

import com.bezkoder.spring.thymeleaf.pagination.Dto.TutorialDTO;
import org.springframework.data.domain.Page;

public interface TutorialService {

    Page<TutorialDTO> getAllTutorials(String keyword, int page, int size);
    TutorialDTO getTutorialById(Integer id);
    TutorialDTO saveTutorial(TutorialDTO tutorialDTO);
    void deleteTutorial(Integer id);
    void updateTutorialPublishedStatus(Integer id, boolean published);
}

