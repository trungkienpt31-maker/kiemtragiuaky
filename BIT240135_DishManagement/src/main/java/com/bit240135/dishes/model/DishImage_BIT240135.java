package com.bit240135.dishes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "BIT240135_DishImages")
public class DishImage_BIT240135 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240135_Id")
    private Long id;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "BIT240135_ImageUrl", nullable = false, length = 1000)
    private String imageUrl;

    @Column(name = "BIT240135_IsThumbnail", nullable = false)
    private Boolean isThumbnail = false;

    @Column(name = "BIT240135_DishId", nullable = false)
    private Long dishId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BIT240135_DishId", insertable = false, updatable = false)
    private Dish_BIT240135 dish;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsThumbnail() { return isThumbnail; }
    public void setIsThumbnail(Boolean isThumbnail) { this.isThumbnail = isThumbnail; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public Dish_BIT240135 getDish() { return dish; }
    public void setDish(Dish_BIT240135 dish) { this.dish = dish; }
}
