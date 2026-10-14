package com.bit240135.dishes.repository;

import com.bit240135.dishes.model.DishCategory_BIT240135;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishCategoryRepository extends JpaRepository<DishCategory_BIT240135, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
