package com.bit240135.dishes.controller;

import com.bit240135.dishes.service.DishImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dishes/{dishId}/images")
public class DishImageController {

    private final DishImageService service;

    public DishImageController(DishImageService service) {
        this.service = service;
    }

    @PostMapping
    public String add(@PathVariable Long dishId,
                      @RequestParam(required = false) String imageUrl,
                      @RequestParam(defaultValue = "false") boolean isThumbnail,
                      RedirectAttributes ra) {
        String url = imageUrl == null ? "" : imageUrl.trim();
        if (url.isEmpty()) {
            ra.addFlashAttribute("error", "image.url.required");
        } else if (url.length() > 1000 || !(url.startsWith("http://") || url.startsWith("https://") || url.startsWith("/"))) {
            ra.addFlashAttribute("error", "image.url.invalid");
        } else {
            service.add(dishId, url, isThumbnail);
            ra.addFlashAttribute("success", "image.added");
        }
        return "redirect:/dishes/" + dishId;
    }

    @PostMapping("/{imageId}/thumbnail")
    public String setThumbnail(@PathVariable Long dishId, @PathVariable Long imageId, RedirectAttributes ra) {
        service.setThumbnail(dishId, imageId);
        ra.addFlashAttribute("success", "image.thumbnail.updated");
        return "redirect:/dishes/" + dishId;
    }

    @PostMapping("/{imageId}/delete")
    public String delete(@PathVariable Long dishId, @PathVariable Long imageId, RedirectAttributes ra) {
        service.delete(dishId, imageId);
        ra.addFlashAttribute("success", "image.deleted");
        return "redirect:/dishes/" + dishId;
    }
}
