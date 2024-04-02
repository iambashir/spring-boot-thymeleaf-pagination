package com.bezkoder.spring.thymeleaf.pagination.controller;

import java.util.ArrayList;
import java.util.List;

import com.bezkoder.spring.thymeleaf.pagination.Dto.TutorialDTO;
import com.bezkoder.spring.thymeleaf.pagination.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezkoder.spring.thymeleaf.pagination.entity.Tutorial;
import com.bezkoder.spring.thymeleaf.pagination.repository.TutorialRepository;

@Controller
@RequiredArgsConstructor
public class TutorialController {

  private final TutorialService tutorialService;

  @GetMapping("/tutorials")
  public String getAll(Model model, @RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
    try {
      Page<TutorialDTO> tutorials = tutorialService.getAllTutorials(keyword, page, size);

      model.addAttribute("tutorials", tutorials.getContent());
      model.addAttribute("currentPage", tutorials.getNumber() + 1);
      model.addAttribute("totalItems", tutorials.getTotalElements());
      model.addAttribute("totalPages", tutorials.getTotalPages());
      model.addAttribute("pageSize", size);
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
    }

    return "tutorials";
  }

  @GetMapping("/tutorials/new")
  public String addTutorial(Model model) {
    TutorialDTO tutorial = new TutorialDTO();
    tutorial.setPublished(true);

    model.addAttribute("tutorial", tutorial);
    model.addAttribute("pageTitle", "Create new Patient Profile");

    return "tutorial_form";
  }

  @PostMapping("/tutorials/save")
  public String saveTutorial(@ModelAttribute("tutorial") TutorialDTO tutorialDTO, RedirectAttributes redirectAttributes) {
    try {
      TutorialDTO savedTutorial = tutorialService.saveTutorial(tutorialDTO);

      redirectAttributes.addFlashAttribute("message", "The Tutorial has been saved successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/tutorials";
  }

  @GetMapping("/tutorials/{id}")
  public String editTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      TutorialDTO tutorial = tutorialService.getTutorialById(id);

      model.addAttribute("tutorial", tutorial);
      model.addAttribute("pageTitle", "Edit Tutorial (ID: " + id + ")");

      return "tutorial_form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());

      return "redirect:/tutorials";
    }
  }

  @GetMapping("/tutorials/delete/{id}")
  public String deleteTutorial(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
    try {
      tutorialService.deleteTutorial(id);

      redirectAttributes.addFlashAttribute("message", "The Tutorial with id=" + id + " has been deleted successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/tutorials";
  }

  @GetMapping("/tutorials/{id}/published/{status}")
  public String updateTutorialPublishedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean published,
                                              RedirectAttributes redirectAttributes) {
    try {
      tutorialService.updateTutorialPublishedStatus(id, published);

      String status = published ? "published" : "disabled";
      String message = "The Tutorial id=" + id + " has been " + status;

      redirectAttributes.addFlashAttribute("message", message);
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/tutorials";
  }
}
