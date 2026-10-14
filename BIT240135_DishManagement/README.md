# BIT240135 – Quản lý món ăn (Spring Boot + Thymeleaf + JPA)

## Chạy (không cần tài khoản/mật khẩu)
`mvn spring-boot:run`  hoặc bấm Run `DishApplication` trong VS Code → http://localhost:8080

- Database `BIT240135_MID` là file H2 tự tạo trong thư mục `data/`, cùng 3 bảng BIT240135_DishCategories / BIT240135_Dishes / BIT240135_DishImages
  (mọi cột có tiền tố BIT240135_) và tự nạp 3 loại + 6 món mẫu.
- Xem bảng/cột/dữ liệu: http://localhost:8080/h2-console  (User `sa`, Password để trống,
  JDBC URL lấy từ application.properties).
- Muốn reset dữ liệu: dừng app, xóa thư mục `data/`, chạy lại.

## Dùng SQL Server (tùy chọn)
Chạy `database/BIT240135_MID.sql` rồi:
`mvn spring-boot:run "-Dspring-boot.run.profiles=sqlserver"` (sửa user/password trong application-sqlserver.properties)

## URL chính
/dishes, /dishes/new, /dishes/{id}, /dishes/{id}/edit, /categories
Đổi ngôn ngữ: nút English / Tiếng Việt trên menu (hoặc ?lang=vi / ?lang=en)
