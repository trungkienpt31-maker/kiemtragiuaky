/* =====================================================================
   Database : BIT240135_MID   (SQL Server)
   Bảng     : BIT240135_DishCategories, BIT240135_Dishes, BIT240135_DishImages
   Tất cả tên database / bảng / cột đều có tiền tố MSSV BIT240135
   Chạy toàn bộ file này 1 lần trong SSMS / Azure Data Studio.
   ===================================================================== */
IF DB_ID(N'BIT240135_MID') IS NULL
    CREATE DATABASE BIT240135_MID;
GO
USE BIT240135_MID;
GO

IF OBJECT_ID(N'dbo.BIT240135_DishImages', N'U')     IS NOT NULL DROP TABLE dbo.BIT240135_DishImages;
IF OBJECT_ID(N'dbo.BIT240135_Dishes', N'U')         IS NOT NULL DROP TABLE dbo.BIT240135_Dishes;
IF OBJECT_ID(N'dbo.BIT240135_DishCategories', N'U') IS NOT NULL DROP TABLE dbo.BIT240135_DishCategories;
GO

CREATE TABLE dbo.BIT240135_DishCategories (
    BIT240135_Id          BIGINT IDENTITY(1,1) NOT NULL,
    BIT240135_Name        NVARCHAR(100)  NOT NULL,
    BIT240135_Description NVARCHAR(1000) NULL,
    CONSTRAINT PK_BIT240135_DishCategories PRIMARY KEY (BIT240135_Id),
    CONSTRAINT UQ_BIT240135_DishCategories_Name UNIQUE (BIT240135_Name)
);

CREATE TABLE dbo.BIT240135_Dishes (
    BIT240135_Id              BIGINT IDENTITY(1,1) NOT NULL,
    BIT240135_Name            NVARCHAR(200)  NOT NULL,
    BIT240135_Price           DECIMAL(18,2)  NOT NULL,
    BIT240135_PreparationTime INT            NOT NULL,
    BIT240135_IsAvailable     BIT            NOT NULL CONSTRAINT DF_BIT240135_Dishes_IsAvailable DEFAULT 1,
    BIT240135_Description     NVARCHAR(1000) NULL,
    BIT240135_DishCategoryId  BIGINT         NOT NULL,
    CONSTRAINT PK_BIT240135_Dishes PRIMARY KEY (BIT240135_Id),
    CONSTRAINT FK_BIT240135_Dishes_DishCategories FOREIGN KEY (BIT240135_DishCategoryId)
        REFERENCES dbo.BIT240135_DishCategories (BIT240135_Id),
    CONSTRAINT CK_BIT240135_Dishes_Price CHECK (BIT240135_Price > 0),
    CONSTRAINT CK_BIT240135_Dishes_PreparationTime CHECK (BIT240135_PreparationTime > 0),
    -- tên món không trùng trong cùng một loại
    CONSTRAINT UQ_BIT240135_Dishes_Name_Category UNIQUE (BIT240135_DishCategoryId, BIT240135_Name)
);

CREATE TABLE dbo.BIT240135_DishImages (
    BIT240135_Id          BIGINT IDENTITY(1,1) NOT NULL,
    BIT240135_ImageUrl    NVARCHAR(1000) NOT NULL,
    BIT240135_IsThumbnail BIT            NOT NULL CONSTRAINT DF_BIT240135_DishImages_IsThumbnail DEFAULT 0,
    BIT240135_DishId      BIGINT         NOT NULL,
    CONSTRAINT PK_BIT240135_DishImages PRIMARY KEY (BIT240135_Id),
    CONSTRAINT FK_BIT240135_DishImages_Dishes FOREIGN KEY (BIT240135_DishId)
        REFERENCES dbo.BIT240135_Dishes (BIT240135_Id)
);
GO

/* ---------------- Dữ liệu mẫu: 3 loại món, 6 món, mỗi món đúng 1 loại ---------------- */
INSERT INTO dbo.BIT240135_DishCategories (BIT240135_Name, BIT240135_Description) VALUES
 (N'Main Dishes', N'Món chính - rice, noodle and soup dishes'),
 (N'Drinks',      N'Đồ uống - coffee, tea and juice'),
 (N'Desserts',    N'Tráng miệng - sweet dishes');

DECLARE @Main BIGINT    = (SELECT BIT240135_Id FROM dbo.BIT240135_DishCategories WHERE BIT240135_Name = N'Main Dishes');
DECLARE @Drink BIGINT   = (SELECT BIT240135_Id FROM dbo.BIT240135_DishCategories WHERE BIT240135_Name = N'Drinks');
DECLARE @Dessert BIGINT = (SELECT BIT240135_Id FROM dbo.BIT240135_DishCategories WHERE BIT240135_Name = N'Desserts');

INSERT INTO dbo.BIT240135_Dishes
 (BIT240135_Name, BIT240135_Price, BIT240135_PreparationTime, BIT240135_IsAvailable, BIT240135_Description, BIT240135_DishCategoryId) VALUES
 (N'Phở bò',      55000, 20, 1, N'Phở bò Hà Nội',                    @Main),
 (N'Bún chả',     60000, 25, 1, N'Bún chả nướng than hoa',           @Main),
 (N'Gỏi cuốn',    35000, 10, 1, N'Gỏi cuốn tôm thịt',                @Main),
 (N'Trà đào',     30000,  5, 1, N'Trà đào cam sả',                   @Drink),
 (N'Cà phê sữa',  25000,  5, 0, N'Cà phê sữa đá (tạm ngừng bán)',    @Drink),
 (N'Chè ba màu',  28000, 15, 1, N'Chè ba màu truyền thống',          @Dessert);

INSERT INTO dbo.BIT240135_DishImages (BIT240135_ImageUrl, BIT240135_IsThumbnail, BIT240135_DishId)
SELECT v.Url, v.IsThumb, d.BIT240135_Id
FROM (VALUES
  (N'Phở bò',   N'https://picsum.photos/seed/pho1/600/400',     1),
  (N'Phở bò',   N'https://picsum.photos/seed/pho2/600/400',     0),
  (N'Bún chả',  N'https://picsum.photos/seed/buncha1/600/400',  1),
  (N'Gỏi cuốn', N'https://picsum.photos/seed/goicuon1/600/400', 1),
  (N'Gỏi cuốn', N'https://picsum.photos/seed/goicuon2/600/400', 0),
  (N'Trà đào',  N'https://picsum.photos/seed/tradao1/600/400',  1)
) AS v(DishName, Url, IsThumb)
JOIN dbo.BIT240135_Dishes d ON d.BIT240135_Name = v.DishName;
-- "Cà phê sữa" và "Chè ba màu" cố ý chưa có ảnh để demo trường hợp món chưa có ảnh.
GO

SELECT * FROM dbo.BIT240135_DishCategories;
SELECT * FROM dbo.BIT240135_Dishes;
SELECT * FROM dbo.BIT240135_DishImages;
GO
