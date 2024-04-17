package com.bezkoder.spring.thymeleaf.pagination.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezkoder.spring.thymeleaf.pagination.entity.Tutorial;
import com.bezkoder.spring.thymeleaf.pagination.repository.TutorialRepository;

@Controller
public class TutorialController {

  @Autowired
  private TutorialRepository tutorialRepository;

  @GetMapping("/tutorials")
  public String getAll(Model model, @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
    try {
      List<Tutorial> tutorials = new ArrayList<Tutorial>();
      Pageable paging = PageRequest.of(page - 1, size);

      Page<Tutorial> pageTuts;
      if (keyword == null) {
        pageTuts = tutorialRepository.findAll(paging);
      } else {
        pageTuts = tutorialRepository.findByTitleContainingIgnoreCase(keyword, paging);
        model.addAttribute("keyword", keyword);
      }

      tutorials = pageTuts.getContent();

      model.addAttribute("tutorials", tutorials);
      model.addAttribute("currentPage", pageTuts.getNumber() + 1);
      model.addAttribute("totalItems", pageTuts.getTotalElements());
      model.addAttribute("totalPages", pageTuts.getTotalPages());
      model.addAttribute("pageSize", size);
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
    }

    return "tutorials";
  }

  @PostMapping("/tutorial")
  public String getTutorialsByDate(Model model,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "3") int size) {
    try {
      List<Tutorial> tutorials = new ArrayList<>();
      Pageable paging = PageRequest.of(page - 1, size);
      Page<Tutorial> pageTuts;

      if (startDate != null && endDate != null) {
        // If start and end dates are provided, use the findByCreateDateBetweenAndActiveStatus method
        pageTuts = (Page<Tutorial>) tutorialRepository.findByCreateDateBetweenAndActiveStatus(startDate, endDate, 1, paging);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
      } else if (keyword != null && !keyword.isEmpty()) {
        // If keyword is provided, use findByTitleContainingIgnoreCase method
        pageTuts = tutorialRepository.findByTitleContainingIgnoreCase(keyword, paging);
        model.addAttribute("keyword", keyword);
      } else {
        // Otherwise, fallback to findAll method
        pageTuts = tutorialRepository.findAll(paging);
      }

      tutorials = pageTuts.getContent();

      model.addAttribute("tutorials", tutorials);
      model.addAttribute("currentPage", pageTuts.getNumber() + 1);
      model.addAttribute("totalItems", pageTuts.getTotalElements());
      model.addAttribute("totalPages", pageTuts.getTotalPages());
      model.addAttribute("pageSize", size);
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
    }

    return "tutorials";
  }


  @GetMapping("/tutorials/new")
  public String addTutorial(Model model) {
    Tutorial tutorial = new Tutorial();
    tutorial.setPublished(true);

    model.addAttribute("tutorial", tutorial);
    model.addAttribute("pageTitle", "Create new Tutorial");

    return "tutorial_form";
  }

  @PostMapping("/tutorials/save")
  public String saveTutorial(Tutorial tutorial, RedirectAttributes redirectAttributes) {
    try {
      tutorialRepository.save(tutorial);

      redirectAttributes.addFlashAttribute("message", "The Tutorial has been saved successfully!");
    } catch (Exception e) {
      redirectAttributes.addAttribute("message", e.getMessage());
    }

    return "redirect:/tutorials";
  }

  @GetMapping("/tutorials/{id}")
  public String editTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Tutorial tutorial = tutorialRepository.findById(id).get();

      model.addAttribute("tutorial", tutorial);
      model.addAttribute("pageTitle", "Edit Tutorial (ID: " + id + ")");

      return "tutorial_form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());

      return "redirect:/tutorials";
    }
  }

  @GetMapping("/tutorials/delete/{id}")
  public String deleteTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      tutorialRepository.deleteById(id);

      redirectAttributes.addFlashAttribute("message", "The Tutorial with id=" + id + " has been deleted successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/tutorials";
  }

  @GetMapping("/tutorials/{id}/published/{status}")
  public String updateTutorialPublishedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean published,
      Model model, RedirectAttributes redirectAttributes) {
    try {
      tutorialRepository.updatePublishedStatus(id, published);

      String status = published ? "published" : "disabled";
      String message = "The Tutorial id=" + id + " has been " + status;

      redirectAttributes.addFlashAttribute("message", message);
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/tutorials";
  }
}
