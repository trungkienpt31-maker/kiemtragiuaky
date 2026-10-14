package com.bit240135.dishes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BIT240135_DishCategories")
public class DishCategory_BIT240135 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240135_Id")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "BIT240135_Name", nullable = false, length = 100)
    private String name;

    @Size(max = 1000)
    @Column(name = "BIT240135_Description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Dish_BIT240135> dishes = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Dish_BIT240135> getDishes() { return dishes; }
    public void setDishes(List<Dish_BIT240135> dishes) { this.dishes = dishes; }
}
