package com.bezkoder.spring.thymeleaf.pagination.controller;

import com.bezkoder.spring.thymeleaf.pagination.dto.TutorialDto;
import com.bezkoder.spring.thymeleaf.pagination.service.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TutorialController {

    @Autowired
    private TutorialService tutorialService;

    @GetMapping("/tutorials")
    public String getAll(Model model, @RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        List<TutorialDto> tutorials = tutorialService.getAllTutorials(keyword, page, size);
        Page<TutorialDto> pageTuts = new PageImpl<>(tutorials, PageRequest.of(page - 1, size), tutorials.size());

        model.addAttribute("tutorials", pageTuts.getContent());
        model.addAttribute("currentPage", pageTuts.getNumber() + 1);
        model.addAttribute("totalItems", pageTuts.getTotalElements());
        model.addAttribute("totalPages", pageTuts.getTotalPages());
        model.addAttribute("pageSize", size);

        return "tutorials";
    }


    @GetMapping("/tutorials/new")
    public String addTutorial(Model model) {
        TutorialDto tutorialDto = new TutorialDto();
        tutorialDto.setPublished(true);
        model.addAttribute("tutorial", tutorialDto);
        model.addAttribute("pageTitle", "Create new Tutorial");
        return "tutorial_form";
    }

    @PostMapping("/tutorials/save")
    public String saveTutorial(@ModelAttribute("tutorial") TutorialDto tutorialDto, RedirectAttributes redirectAttributes) {
        TutorialDto savedTutorial = tutorialService.saveTutorial(tutorialDto);
        redirectAttributes.addFlashAttribute("message", "The Tutorial has been saved successfully!");
        return "redirect:/tutorials";
    }

    @GetMapping("/tutorials/{id}")
    public String editTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        TutorialDto tutorialDto = tutorialService.getTutorialById(id);
        if (tutorialDto != null) {
            model.addAttribute("tutorial", tutorialDto);
            model.addAttribute("pageTitle", "Edit Tutorial (ID: " + id + ")");
            return "tutorial_form";
        } else {
            redirectAttributes.addFlashAttribute("message", "Tutorial not found");
            return "redirect:/tutorials";
        }
    }

    @GetMapping("/tutorials/delete/{id}")
    public String deleteTutorial(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        tutorialService.deleteTutorial(id);
        redirectAttributes.addFlashAttribute("message", "The Tutorial with id=" + id + " has been deleted successfully!");
        return "redirect:/tutorials";
    }

    @GetMapping("/tutorials/{id}/published/{status}")
    public String updateTutorialPublishedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean published,
                                                RedirectAttributes redirectAttributes) {
        tutorialService.updatePublishedStatus(id, published);
        String status = published ? "published" : "disabled";
        redirectAttributes.addFlashAttribute("message", "The Tutorial id=" + id + " has been " + status);
        return "redirect:/tutorials";
    }

}