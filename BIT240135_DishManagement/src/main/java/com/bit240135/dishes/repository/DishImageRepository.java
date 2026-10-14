package com.bit240135.dishes.repository;

import com.bit240135.dishes.model.DishImage_BIT240135;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishImageRepository extends JpaRepository<DishImage_BIT240135, Long> {
    List<DishImage_BIT240135> findByDishId(Long dishId);
    Optional<DishImage_BIT240135> findByIdAndDishId(Long id, Long dishId);
}
