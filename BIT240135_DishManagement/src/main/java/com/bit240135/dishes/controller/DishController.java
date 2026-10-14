package com.bit240135.dishes.controller;

import com.bit240135.dishes.exception.NotFoundException;
import com.bit240135.dishes.model.Dish_BIT240135;
import com.bit240135.dishes.repository.DishCategoryRepository;
import com.bit240135.dishes.repository.DishRepository;
import com.bit240135.dishes.repository.DishSpecs;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dishes")
public class DishController {

    private final DishRepository dishRepo;
    private final DishCategoryRepository categoryRepo;

    public DishController(DishRepository dishRepo, DishCategoryRepository categoryRepo) {
        this.dishRepo = dishRepo;
        this.categoryRepo = categoryRepo;
    }

    @InitBinder("dish")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "category*", "images*");
    }

    // ================= LIST =================
    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) String available,
                       @RequestParam(required = false) String minPrice,
                       @RequestParam(required = false) String maxPrice,
                       @RequestParam(required = false) String sort,
                       Model model) {

        Boolean avail = "true".equals(available) ? Boolean.TRUE : "false".equals(available) ? Boolean.FALSE : null;

        BigDecimal min = null, max = null;
        String priceError = null;
        try {
            min = parsePrice(minPrice);
            max = parsePrice(maxPrice);
            if (min != null && max != null && min.compareTo(max) > 0) priceError = "error.price.range";
        } catch (NumberFormatException e) {
            priceError = "error.price.invalid";
        }
        if (priceError != null) { min = null; max = null; } // khoảng giá sai -> bỏ lọc giá và báo lỗi

        model.addAttribute("dishes", dishRepo.findAll(DishSpecs.filter(q, categoryId, avail, min, max), toSort(sort)));
        model.addAttribute("categories", categoryRepo.findAll(Sort.by("name")));
        model.addAttribute("priceError", priceError);
        // trả lại điều kiện để form giữ nguyên
        model.addAttribute("q", q);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("available", available);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);
        return "dishes/list";
    }

    private static BigDecimal parsePrice(String s) {
        if (s == null || s.isBlank()) return null;
        BigDecimal v = new BigDecimal(s.trim());
        if (v.signum() < 0) throw new NumberFormatException("negative");
        return v;
    }

    private static Sort toSort(String sort) {
        if (sort == null) sort = "";
        return switch (sort) {
            case "priceAsc" -> Sort.by("price").ascending().and(Sort.by("id"));
            case "priceDesc" -> Sort.by("price").descending().and(Sort.by("id"));
            case "timeAsc" -> Sort.by("preparationTime").ascending().and(Sort.by("id"));
            default -> Sort.by("id");
        };
    }

    // ================= DETAIL =================
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("dish", find(id));
        return "dishes/detail";
    }

    // ================= CREATE =================
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("dish", new Dish_BIT240135());
        return form(model, "/dishes", "dish.create.title");
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("dish") Dish_BIT240135 dish, BindingResult br,
                         Model model, RedirectAttributes ra) {
        validateBusiness(dish, null, br);
        if (br.hasErrors()) return form(model, "/dishes", "dish.create.title");
        dish.setId(null);
        dish.setName(dish.getName().trim());
        if (dish.getIsAvailable() == null) dish.setIsAvailable(false);
        dishRepo.save(dish);
        ra.addFlashAttribute("success", "dish.created");
        return "redirect:/dishes/" + dish.getId();
    }

    // ================= EDIT =================
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("dish", find(id));
        return form(model, "/dishes/" + id, "dish.edit.title");
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("dish") Dish_BIT240135 dish,
                         BindingResult br, Model model, RedirectAttributes ra) {
        Dish_BIT240135 existing = find(id);
        validateBusiness(dish, id, br);
        if (br.hasErrors()) {
            dish.setId(id);
            return form(model, "/dishes/" + id, "dish.edit.title");
        }
        existing.setName(dish.getName().trim());
        existing.setPrice(dish.getPrice());
        existing.setPreparationTime(dish.getPreparationTime());
        existing.setIsAvailable(Boolean.TRUE.equals(dish.getIsAvailable()));
        existing.setDescription(dish.getDescription());
        existing.setDishCategoryId(dish.getDishCategoryId());
        dishRepo.save(existing);
        ra.addFlashAttribute("success", "dish.updated");
        return "redirect:/dishes/" + id;
    }

    // ================= DELETE =================
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        dishRepo.delete(find(id));
        ra.addFlashAttribute("success", "dish.deleted");
        return "redirect:/dishes";
    }

    // ================= helpers =================
    private Dish_BIT240135 find(Long id) {
        return dishRepo.findById(id).orElseThrow(() -> new NotFoundException("error.dish.notfound"));
    }

    private String form(Model model, String action, String titleKey) {
        model.addAttribute("categories", categoryRepo.findAll(Sort.by("name")));
        model.addAttribute("action", action);
        model.addAttribute("titleKey", titleKey);
        return "dishes/form";
    }

    /** DishCategoryId phải tồn tại; tên món không trùng trong cùng một loại. */
    private void validateBusiness(Dish_BIT240135 d, Long selfId, BindingResult br) {
        Long catId = d.getDishCategoryId();
        if (catId == null || br.hasFieldErrors("dishCategoryId")) return;
        if (!categoryRepo.existsById(catId)) {
            br.rejectValue("dishCategoryId", "dish.category.notfound");
            return;
        }
        if (d.getName() != null && !d.getName().isBlank() && !br.hasFieldErrors("name")) {
            String name = d.getName().trim();
            boolean dup = selfId == null
                    ? dishRepo.existsByNameIgnoreCaseAndDishCategoryId(name, catId)
                    : dishRepo.existsByNameIgnoreCaseAndDishCategoryIdAndIdNot(name, catId, selfId);
            if (dup) br.rejectValue("name", "dish.name.duplicate");
        }
    }
}
