package com.bezkoder.spring.thymeleaf.pagination.service;

import com.bezkoder.spring.thymeleaf.pagination.dto.TutorialDto;

import java.util.List;


public interface TutorialService {
    List<TutorialDto> getAllTutorials(String keyword, int page, int size);

    TutorialDto getTutorialById(Integer id);

    TutorialDto saveTutorial(TutorialDto tutorialDto);

    void deleteTutorial(Integer id);

    void updatePublishedStatus(Integer id, boolean published);
}

