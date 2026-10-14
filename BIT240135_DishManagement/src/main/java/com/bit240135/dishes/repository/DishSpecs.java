package com.bit240135.dishes.repository;

import com.bit240135.dishes.model.Dish_BIT240135;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/** Toàn bộ điều kiện lọc được dịch thành WHERE ở tầng database. */
public final class DishSpecs {
    private DishSpecs() {}

    public static Specification<Dish_BIT240135> filter(String keyword, Long categoryId, Boolean available,
                                                        BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                ps.add(cb.like(cb.lower(root.get("name")), "%" + keyword.trim().toLowerCase() + "%"));
            }
            if (categoryId != null) ps.add(cb.equal(root.get("dishCategoryId"), categoryId));
            if (available != null) ps.add(cb.equal(root.get("isAvailable"), available));
            if (minPrice != null) ps.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            if (maxPrice != null) ps.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }
}
