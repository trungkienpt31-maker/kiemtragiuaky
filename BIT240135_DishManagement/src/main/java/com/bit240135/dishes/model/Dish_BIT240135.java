package com.bit240135.dishes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BIT240135_Dishes")
public class Dish_BIT240135 {

    public static final int QUICK_MAX_MINUTES = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240135_Id")
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(name = "BIT240135_Name", nullable = false, length = 200)
    private String name;

    @NotNull
    @Positive
    @Column(name = "BIT240135_Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @NotNull
    @Positive
    @Column(name = "BIT240135_PreparationTime", nullable = false)
    private Integer preparationTime;

    @Column(name = "BIT240135_IsAvailable", nullable = false)
    private Boolean isAvailable = true;

    @Size(max = 1000)
    @Column(name = "BIT240135_Description", length = 1000)
    private String description;

    /** Khóa ngoại thật sự được ghi xuống DB. */
    @NotNull
    @Column(name = "BIT240135_DishCategoryId", nullable = false)
    private Long dishCategoryId;

    /** Navigation property (chỉ đọc) dùng chung cột khóa ngoại. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BIT240135_DishCategoryId", insertable = false, updatable = false)
    private DishCategory_BIT240135 category;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE)
    @OrderBy("isThumbnail DESC, id ASC")
    private List<DishImage_BIT240135> images = new ArrayList<>();

    // ----- Phân loại thời gian chế biến: tính khi hiển thị, KHÔNG có cột trong DB -----
    @Transient
    public boolean isQuick() {
        return preparationTime != null && preparationTime <= QUICK_MAX_MINUTES;
    }

    @Transient
    public DishImage_BIT240135 getThumbnail() {
        if (images == null || images.isEmpty()) return null;
        return images.stream()
                .filter(i -> Boolean.TRUE.equals(i.getIsThumbnail()))
                .findFirst()
                .orElse(images.get(0));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getPreparationTime() { return preparationTime; }
    public void setPreparationTime(Integer preparationTime) { this.preparationTime = preparationTime; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getDishCategoryId() { return dishCategoryId; }
    public void setDishCategoryId(Long dishCategoryId) { this.dishCategoryId = dishCategoryId; }
    public DishCategory_BIT240135 getCategory() { return category; }
    public void setCategory(DishCategory_BIT240135 category) { this.category = category; }
    public List<DishImage_BIT240135> getImages() { return images; }
    public void setImages(List<DishImage_BIT240135> images) { this.images = images; }
}
