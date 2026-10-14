package com.bit240135.dishes.config;

import com.bit240135.dishes.model.*;
import com.bit240135.dishes.repository.*;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Dữ liệu mẫu: 3 loại món, 6 món, mỗi món thuộc đúng 1 loại. Chỉ chạy khi DB còn trống. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final DishCategoryRepository categoryRepo;
    private final DishRepository dishRepo;
    private final DishImageRepository imageRepo;

    public DataSeeder(DishCategoryRepository categoryRepo, DishRepository dishRepo, DishImageRepository imageRepo) {
        this.categoryRepo = categoryRepo;
        this.dishRepo = dishRepo;
        this.imageRepo = imageRepo;
    }

    @Override
    public void run(String... args) {
        if (categoryRepo.count() > 0) return;

        DishCategory_BIT240135 main = cat("Main Dishes", "Món chính - rice, noodle and soup dishes");
        DishCategory_BIT240135 drink = cat("Drinks", "Đồ uống - coffee, tea and juice");
        DishCategory_BIT240135 dessert = cat("Desserts", "Tráng miệng - sweet dishes");

        Dish_BIT240135 pho = dish("Phở bò", "55000", 20, true, "Phở bò Hà Nội", main);
        Dish_BIT240135 buncha = dish("Bún chả", "60000", 25, true, "Bún chả nướng than hoa", main);
        Dish_BIT240135 goicuon = dish("Gỏi cuốn", "35000", 10, true, "Gỏi cuốn tôm thịt", main);
        Dish_BIT240135 tradao = dish("Trà đào", "30000", 5, true, "Trà đào cam sả", drink);
        dish("Cà phê sữa", "25000", 5, false, "Cà phê sữa đá (tạm ngừng bán)", drink); // chưa có ảnh + ngừng bán
        dish("Chè ba màu", "28000", 15, true, "Chè ba màu truyền thống", dessert);   // chưa có ảnh

        img(pho, "pho1", true);
        img(pho, "pho2", false);
        img(buncha, "buncha1", true);
        img(goicuon, "goicuon1", true);
        img(goicuon, "goicuon2", false);
        img(tradao, "tradao1", true);
    }

    private DishCategory_BIT240135 cat(String name, String desc) {
        DishCategory_BIT240135 c = new DishCategory_BIT240135();
        c.setName(name);
        c.setDescription(desc);
        return categoryRepo.save(c);
    }

    private Dish_BIT240135 dish(String name, String price, int time, boolean available, String desc, DishCategory_BIT240135 cat) {
        Dish_BIT240135 d = new Dish_BIT240135();
        d.setName(name);
        d.setPrice(new BigDecimal(price));
        d.setPreparationTime(time);
        d.setIsAvailable(available);
        d.setDescription(desc);
        d.setDishCategoryId(cat.getId());
        return dishRepo.save(d);
    }

    private void img(Dish_BIT240135 d, String seed, boolean thumb) {
        DishImage_BIT240135 i = new DishImage_BIT240135();
        i.setDishId(d.getId());
        i.setImageUrl("https://picsum.photos/seed/" + seed + "/600/400");
        i.setIsThumbnail(thumb);
        imageRepo.save(i);
    }
}
