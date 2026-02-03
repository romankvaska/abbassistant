package com.chubb.abbassistant.controller;

import com.chubb.abbassistant.dto.AbbreviationRequest;
import com.chubb.abbassistant.dto.AbbreviationResponse;
import com.chubb.abbassistant.dto.TranslationDto;
import com.chubb.abbassistant.service.AbbreviationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AbbreviationService abbreviationService;

    @GetMapping
    public String list(Model model) {
        List<AbbreviationResponse> abbreviations = abbreviationService.findAll();
        model.addAttribute("abbreviations", abbreviations);
        return "abbreviations/list";
    }

    @GetMapping("/abbreviations/new")
    public String newForm(Model model) {
        AbbreviationRequest request = new AbbreviationRequest();
        request.setTranslations(new ArrayList<>());
        request.getTranslations().add(new TranslationDto()); // Add one empty translation form
        model.addAttribute("abbreviation", request);
        model.addAttribute("isNew", true);
        return "abbreviations/form";
    }

    @PostMapping("/abbreviations")
    public String create(@Valid @ModelAttribute("abbreviation") AbbreviationRequest request,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isNew", true);
            return "abbreviations/form";
        }

        // Remove empty translations
        request.getTranslations().removeIf(t ->
                t.getLanguageCode() == null || t.getLanguageCode().isBlank());

        try {
            abbreviationService.create(request);
            redirectAttributes.addFlashAttribute("success", "Abbreviation created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin";
    }

    @GetMapping("/abbreviations/{id}")
    public String view(@PathVariable Long id, Model model) {
        AbbreviationResponse abbreviation = abbreviationService.findById(id);
        model.addAttribute("abbreviation", abbreviation);
        return "abbreviations/view";
    }

    @GetMapping("/abbreviations/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        AbbreviationResponse response = abbreviationService.findById(id);

        AbbreviationRequest request = new AbbreviationRequest();
        request.setCode(response.getCode());
        request.setTranslations(new ArrayList<>(response.getTranslations()));

        model.addAttribute("abbreviation", request);
        model.addAttribute("abbreviationId", id);
        model.addAttribute("isNew", false);
        return "abbreviations/form";
    }

    @PostMapping("/abbreviations/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("abbreviation") AbbreviationRequest request,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("abbreviationId", id);
            model.addAttribute("isNew", false);
            return "abbreviations/form";
        }

        // Remove empty translations
        request.getTranslations().removeIf(t ->
                t.getLanguageCode() == null || t.getLanguageCode().isBlank());

        try {
            abbreviationService.update(id, request);
            redirectAttributes.addFlashAttribute("success", "Abbreviation updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin";
    }

    @PostMapping("/abbreviations/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            abbreviationService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Abbreviation deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    @GetMapping("/abbreviations/{id}/translations/new")
    public String newTranslationForm(@PathVariable Long id, Model model) {
        AbbreviationResponse abbreviation = abbreviationService.findById(id);
        model.addAttribute("abbreviation", abbreviation);
        model.addAttribute("translation", new TranslationDto());
        return "abbreviations/translation-form";
    }

    @PostMapping("/abbreviations/{id}/translations")
    public String addTranslation(@PathVariable Long id,
                                 @Valid @ModelAttribute("translation") TranslationDto translation,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            AbbreviationResponse abbreviation = abbreviationService.findById(id);
            model.addAttribute("abbreviation", abbreviation);
            return "abbreviations/translation-form";
        }

        try {
            abbreviationService.addTranslation(id, translation);
            redirectAttributes.addFlashAttribute("success", "Translation added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/abbreviations/" + id;
    }

    @PostMapping("/abbreviations/{id}/translations/{lang}/delete")
    public String deleteTranslation(@PathVariable Long id,
                                    @PathVariable String lang,
                                    RedirectAttributes redirectAttributes) {
        try {
            abbreviationService.deleteTranslation(id, lang);
            redirectAttributes.addFlashAttribute("success", "Translation deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/abbreviations/" + id;
    }
}
