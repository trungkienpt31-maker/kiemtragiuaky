package com.bit240135.dishes.repository;

import com.bit240135.dishes.model.Dish_BIT240135;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DishRepository extends JpaRepository<Dish_BIT240135, Long>, JpaSpecificationExecutor<Dish_BIT240135> {
    boolean existsByDishCategoryId(Long dishCategoryId);
    boolean existsByNameIgnoreCaseAndDishCategoryId(String name, Long dishCategoryId);
    boolean existsByNameIgnoreCaseAndDishCategoryIdAndIdNot(String name, Long dishCategoryId, Long id);
}
