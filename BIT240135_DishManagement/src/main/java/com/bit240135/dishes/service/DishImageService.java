package com.bit240135.dishes.service;

import com.bit240135.dishes.exception.NotFoundException;
import com.bit240135.dishes.model.DishImage_BIT240135;
import com.bit240135.dishes.repository.DishImageRepository;
import com.bit240135.dishes.repository.DishRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DishImageService {

    private final DishImageRepository imageRepo;
    private final DishRepository dishRepo;

    public DishImageService(DishImageRepository imageRepo, DishRepository dishRepo) {
        this.imageRepo = imageRepo;
        this.dishRepo = dishRepo;
    }

    private void requireDish(Long dishId) {
        if (!dishRepo.existsById(dishId)) throw new NotFoundException("error.dish.notfound");
    }

    @Transactional
    public void add(Long dishId, String url, boolean asThumbnail) {
        requireDish(dishId);
        List<DishImage_BIT240135> existing = imageRepo.findByDishId(dishId);
        // Món chưa có ảnh đại diện thì ảnh mới tự động trở thành ảnh đại diện
        boolean makeThumb = asThumbnail || existing.stream().noneMatch(i -> Boolean.TRUE.equals(i.getIsThumbnail()));
        if (makeThumb) {
            existing.forEach(i -> i.setIsThumbnail(false));
            imageRepo.saveAll(existing);
        }
        DishImage_BIT240135 img = new DishImage_BIT240135();
        img.setDishId(dishId);
        img.setImageUrl(url.trim());
        img.setIsThumbnail(makeThumb);
        imageRepo.save(img);
    }

    /** Đảm bảo mỗi món tối đa MỘT ảnh có IsThumbnail = true. */
    @Transactional
    public void setThumbnail(Long dishId, Long imageId) {
        requireDish(dishId);
        DishImage_BIT240135 target = imageRepo.findByIdAndDishId(imageId, dishId)
                .orElseThrow(() -> new NotFoundException("error.image.notfound"));
        List<DishImage_BIT240135> all = imageRepo.findByDishId(dishId);
        all.forEach(i -> i.setIsThumbnail(i.getId().equals(target.getId())));
        imageRepo.saveAll(all);
    }

    @Transactional
    public void delete(Long dishId, Long imageId) {
        requireDish(dishId);
        DishImage_BIT240135 img = imageRepo.findByIdAndDishId(imageId, dishId)
                .orElseThrow(() -> new NotFoundException("error.image.notfound"));
        boolean wasThumb = Boolean.TRUE.equals(img.getIsThumbnail());
        imageRepo.delete(img);
        imageRepo.flush();
        if (wasThumb) {
            List<DishImage_BIT240135> rest = imageRepo.findByDishId(dishId);
            if (!rest.isEmpty()) {
                rest.get(0).setIsThumbnail(true);
                imageRepo.save(rest.get(0));
            }
        }
    }
}
