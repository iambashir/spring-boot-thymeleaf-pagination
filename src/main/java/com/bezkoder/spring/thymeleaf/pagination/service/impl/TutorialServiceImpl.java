package com.bezkoder.spring.thymeleaf.pagination.service.impl;

import com.bezkoder.spring.thymeleaf.pagination.dto.TutorialDto;
import com.bezkoder.spring.thymeleaf.pagination.entity.Tutorial;
import com.bezkoder.spring.thymeleaf.pagination.repository.TutorialRepository;
import com.bezkoder.spring.thymeleaf.pagination.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class TutorialServiceImpl implements TutorialService {

    private final TutorialRepository tutorialRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<TutorialDto> getAllTutorials(String keyword, int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size); // Adjust page number here
        Page<Tutorial> pageTuts;
        if (keyword == null) {
            pageTuts = tutorialRepository.findAll(paging);
        } else {
            pageTuts = tutorialRepository.findByTitleContainingIgnoreCase(keyword, paging);
        }
        return pageTuts.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public TutorialDto getTutorialById(Integer id) {
        Tutorial tutorial = tutorialRepository.findById(id).orElse(null);
        return convertToDto(tutorial);
    }

    @Override
    public TutorialDto saveTutorial(TutorialDto tutorialDto) {
        Tutorial tutorial = convertToEntity(tutorialDto);
        Tutorial savedTutorial = tutorialRepository.save(tutorial);
        return convertToDto(savedTutorial);
    }

    @Override
    public void deleteTutorial(Integer id) {
        tutorialRepository.deleteById(id);
    }

    @Override
    public void updatePublishedStatus(Integer id, boolean published) {
        Tutorial tutorial = tutorialRepository.findById(id).orElse(null);
        if (tutorial != null) {
            tutorial.setPublished(published);
            tutorialRepository.save(tutorial);
        }
    }

    private TutorialDto convertToDto(Tutorial tutorial) {
        return modelMapper.map(tutorial, TutorialDto.class);
    }

    private Tutorial convertToEntity(TutorialDto tutorialDto) {
        return modelMapper.map(tutorialDto, Tutorial.class);
    }
}

