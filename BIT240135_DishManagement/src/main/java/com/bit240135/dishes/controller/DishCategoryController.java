package com.bit240135.dishes.controller;

import com.bit240135.dishes.exception.NotFoundException;
import com.bit240135.dishes.model.DishCategory_BIT240135;
import com.bit240135.dishes.repository.DishCategoryRepository;
import com.bit240135.dishes.repository.DishRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class DishCategoryController {

    private final DishCategoryRepository categoryRepo;
    private final DishRepository dishRepo;

    public DishCategoryController(DishCategoryRepository categoryRepo, DishRepository dishRepo) {
        this.categoryRepo = categoryRepo;
        this.dishRepo = dishRepo;
    }

    @InitBinder("category")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "dishes*");
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryRepo.findAll(Sort.by("id")));
        return "categories/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("category", new DishCategory_BIT240135());
        return form(model, "/categories", "category.create.title");
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("category") DishCategory_BIT240135 category, BindingResult br,
                         Model model, RedirectAttributes ra) {
        if (!br.hasFieldErrors("name") && categoryRepo.existsByNameIgnoreCase(category.getName().trim())) {
            br.rejectValue("name", "category.name.duplicate");
        }
        if (br.hasErrors()) return form(model, "/categories", "category.create.title");
        category.setId(null);
        category.setName(category.getName().trim());
        categoryRepo.save(category);
        ra.addFlashAttribute("success", "category.saved");
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", find(id));
        return form(model, "/categories/" + id, "category.edit.title");
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("category") DishCategory_BIT240135 category,
                         BindingResult br, Model model, RedirectAttributes ra) {
        DishCategory_BIT240135 existing = find(id);
        if (!br.hasFieldErrors("name") && categoryRepo.existsByNameIgnoreCaseAndIdNot(category.getName().trim(), id)) {
            br.rejectValue("name", "category.name.duplicate");
        }
        if (br.hasErrors()) {
            category.setId(id);
            return form(model, "/categories/" + id, "category.edit.title");
        }
        existing.setName(category.getName().trim());
        existing.setDescription(category.getDescription());
        categoryRepo.save(existing);
        ra.addFlashAttribute("success", "category.saved");
        return "redirect:/categories";
    }

    /** Không cho xóa loại món đang có món sử dụng. */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        DishCategory_BIT240135 existing = find(id);
        if (dishRepo.existsByDishCategoryId(id)) {
            ra.addFlashAttribute("error", "category.delete.inUse");
            return "redirect:/categories";
        }
        categoryRepo.delete(existing);
        ra.addFlashAttribute("success", "category.deleted");
        return "redirect:/categories";
    }

    private DishCategory_BIT240135 find(Long id) {
        return categoryRepo.findById(id).orElseThrow(() -> new NotFoundException("error.category.notfound"));
    }

    private String form(Model model, String action, String titleKey) {
        model.addAttribute("action", action);
        model.addAttribute("titleKey", titleKey);
        return "categories/form";
    }
}
