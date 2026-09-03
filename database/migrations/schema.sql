USE [master];
GO

IF NOT EXISTS (SELECT 1 FROM sys.databases WHERE name = N'ShoppingServiceMVC')
BEGIN
    CREATE DATABASE [ShoppingServiceMVC];
END;
GO

USE [ShoppingServiceMVC];
GO

IF OBJECT_ID(N'[dbo].[products]', 'U') IS NOT NULL DROP TABLE [dbo].[products];
IF OBJECT_ID(N'[dbo].[categories]', 'U') IS NOT NULL DROP TABLE [dbo].[categories];
IF OBJECT_ID(N'[dbo].[users]', 'U') IS NOT NULL DROP TABLE [dbo].[users];
GO

CREATE TABLE [dbo].[users] (
    [id]                INT IDENTITY(1,1) NOT NULL,
    [email]             NVARCHAR(255)     NOT NULL,
    [username]          NVARCHAR(100)     NOT NULL,
    [fullname]          NVARCHAR(255)     NOT NULL,
    [password]          NVARCHAR(255)     NOT NULL,
    [image_url]         NVARCHAR(500)     NULL,
    [image_public_id]   NVARCHAR(255)     NULL,
    [role_id]           INT               NOT NULL CONSTRAINT [DF_users_role_id] DEFAULT 5,
    [phone]             NVARCHAR(30)      NOT NULL,
    [otp_code]          NVARCHAR(10)      NULL,
    [otp_expires_at]    DATETIME2         NULL,
    [is_activated]      BIT               NOT NULL CONSTRAINT [DF_users_is_activated] DEFAULT 0,
    [created_at]        DATETIME2         NOT NULL CONSTRAINT [DF_users_created_at] DEFAULT SYSDATETIME(),
    [updated_at]        DATETIME2         NOT NULL CONSTRAINT [DF_users_updated_at] DEFAULT SYSDATETIME(),

    CONSTRAINT [PK_users] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [UQ_users_email] UNIQUE NONCLUSTERED ([email] ASC),
    CONSTRAINT [UQ_users_username] UNIQUE NONCLUSTERED ([username] ASC)
);
GO

CREATE NONCLUSTERED INDEX [IX_users_email] ON [dbo].[users] ([email] ASC);
CREATE NONCLUSTERED INDEX [IX_users_username] ON [dbo].[users] ([username] ASC);
CREATE NONCLUSTERED INDEX [IX_users_phone] ON [dbo].[users] ([phone] ASC);
GO

CREATE TABLE [dbo].[categories] (
    [id]                INT IDENTITY(1,1) NOT NULL,
    [name]              NVARCHAR(255)     NOT NULL,
    [image_url]         NVARCHAR(500)     NULL,
    [image_public_id]   NVARCHAR(255)     NULL,
    [created_at]        DATETIME2         NOT NULL CONSTRAINT [DF_categories_created_at] DEFAULT SYSDATETIME(),
    [updated_at]        DATETIME2         NOT NULL CONSTRAINT [DF_categories_updated_at] DEFAULT SYSDATETIME(),

    CONSTRAINT [PK_categories] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [UQ_categories_name] UNIQUE NONCLUSTERED ([name] ASC)
);
GO

CREATE NONCLUSTERED INDEX [IX_categories_name] ON [dbo].[categories] ([name] ASC);
GO

CREATE TABLE [dbo].[products] (
    [id]                INT IDENTITY(1,1) NOT NULL,
    [name]              NVARCHAR(255)     NOT NULL,
    [slug]              NVARCHAR(255)     NOT NULL,
    [description]       NVARCHAR(MAX)     NULL,
    [price]             DECIMAL(18, 2)    NOT NULL CONSTRAINT [DF_products_price] DEFAULT 0.00,
    [image_url]         NVARCHAR(500)     NULL,
    [image_public_id]   NVARCHAR(255)     NULL,
    [category_id]       INT               NOT NULL,
    [created_at]        DATETIME2         NOT NULL CONSTRAINT [DF_products_created_at] DEFAULT SYSDATETIME(),
    [updated_at]        DATETIME2         NOT NULL CONSTRAINT [DF_products_updated_at] DEFAULT SYSDATETIME(),

    CONSTRAINT [PK_products] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [UQ_products_slug] UNIQUE NONCLUSTERED ([slug] ASC),
    CONSTRAINT [FK_products_categories] FOREIGN KEY ([category_id]) 
        REFERENCES [dbo].[categories] ([id]) 
        ON DELETE NO ACTION 
        ON UPDATE CASCADE
);
GO

CREATE NONCLUSTERED INDEX [IX_products_slug] ON [dbo].[products] ([slug] ASC);
CREATE NONCLUSTERED INDEX [IX_products_category_id] ON [dbo].[products] ([category_id] ASC);
CREATE NONCLUSTERED INDEX [IX_products_created_at] ON [dbo].[products] ([created_at] DESC);
GO

INSERT INTO [dbo].[users] (
    [email], [username], [fullname], [password], 
    [image_url], [image_public_id], [role_id], [phone], 
    [otp_code], [otp_expires_at], [is_activated], [created_at], [updated_at]
) VALUES 
(
    N'tuan0947881956@gmail.com', N'admin', N'Quản Trị Viên Hệ Thống', N'admin123',
    NULL, NULL, 1, N'0901234567',
    NULL, NULL, 1, SYSDATETIME(), SYSDATETIME()
),
(
    N'user@gmail.com', N'user', N'Nguyễn Văn Khách', N'user123',
    NULL, NULL, 5, N'0987654321',
    NULL, NULL, 1, SYSDATETIME(), SYSDATETIME()
);
GO

SET IDENTITY_INSERT [dbo].[categories] ON;

INSERT INTO [dbo].[categories] ([id], [name], [image_url], [image_public_id], [created_at], [updated_at])
VALUES 
(1, N'Điện thoại & Tablet', N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451422/seed_cat_1.jpg', N'seed_cat_1', SYSDATETIME(), SYSDATETIME()),
(2, N'Laptop & Máy tính', N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451464/seed_cat_2.jpg', N'seed_cat_2', SYSDATETIME(), SYSDATETIME()),
(3, N'Phụ kiện công nghệ', N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451528/seed_cat_3.jpg', N'seed_cat_3', SYSDATETIME(), SYSDATETIME()),
(4, N'Thiết bị SmartHome', N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451573/seed_cat_4.jpg', N'seed_cat_4', SYSDATETIME(), SYSDATETIME());

SET IDENTITY_INSERT [dbo].[categories] OFF;
GO

SET IDENTITY_INSERT [dbo].[products] ON;

INSERT INTO [dbo].[products] (
    [id], [name], [slug], [description], [price], 
    [image_url], [image_public_id], [category_id], [created_at], [updated_at]
) VALUES 
(
    1, 
    N'iPhone 15 Pro Max 256GB', 
    N'iphone-15-pro-max-256gb', 
    N'Thiết kế khung titan chuẩn hàng không vũ trụ, chip A17 Pro mạnh mẽ, camera tiềm vọng zoom quang học 5x sắc nét đỉnh cao.', 
    29990000.00, 
    N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451588/seed_prod_1.jpg', N'seed_prod_1', 1, SYSDATETIME(), SYSDATETIME()
),
(
    2, 
    N'Samsung Galaxy S24 Ultra', 
    N'samsung-galaxy-s24-ultra', 
    N'Tích hợp công nghệ Galaxy AI tiên tiến, màn hình phẳng Dynamic AMOLED 2X rực rỡ, bút S-Pen tích hợp và camera 200MP.', 
    27490000.00, 
    N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451614/seed_prod_2.jpg', N'seed_prod_2', 1, SYSDATETIME(), SYSDATETIME()
),
(
    3, 
    N'MacBook Pro 14 inch M3 Pro', 
    N'macbook-pro-14-inch-m3-pro', 
    N'Hiệu năng đột phá với vi xử lý Apple Silicon M3 Pro, màn hình Liquid Retina XDR 120Hz mượt mà, thời lượng pin lên đến 18 giờ.', 
    49990000.00, 
    N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451619/seed_prod_3.jpg', N'seed_prod_3', 2, SYSDATETIME(), SYSDATETIME()
),
(
    4, 
    N'Laptop Dell XPS 15 9530', 
    N'laptop-dell-xps-15-9530', 
    N'Màn hình OLED 3.5K viền siêu mỏng InfinityEdge, chip Intel Core i7-13700H kết hợp card đồ họa rời NVIDIA RTX 4060.', 
    44500000.00, 
    N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451636/seed_prod_4.jpg', N'seed_prod_4', 2, SYSDATETIME(), SYSDATETIME()
),
(
    5, 
    N'Chuột không dây Logitech MX Master 3S', 
    N'chuot-logitech-mx-master-3s', 
    N'Cảm biến Quiet Clicks giảm 90% tiếng ồn, độ phân giải 8000 DPI lướt trên mọi bề mặt, con lăn điện từ MagSpeed siêu nhanh.', 
    2250000.00, 
    N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451640/seed_prod_5.jpg', N'seed_prod_5', 3, SYSDATETIME(), SYSDATETIME()
),
(
    6, 
    N'Bàn phím cơ Logitech MX Mechanical', 
    N'ban-phim-co-logitech-mx-mechanical', 
    N'Switch xúc giác Tactile Quiet êm ái, đèn nền thông minh tự động sáng khi đưa tay tới, kết nối đa thiết bị linh hoạt.', 
    3450000.00, 
    N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451645/seed_prod_6.jpg', N'seed_prod_6', 3, SYSDATETIME(), SYSDATETIME()
),
(
    7, 
    N'Màn hình thông minh Google Nest Hub Gen 2', 
    N'google-nest-hub-gen-2', 
    N'Trung tâm điều khiển ngôi nhà thông minh với trợ lý Google Assistant, cảm biến theo dõi giấc ngủ Sleep Sensing hiện đại.', 
    1850000.00, 
    N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451657/seed_prod_7.jpg', N'seed_prod_7', 4, SYSDATETIME(), SYSDATETIME()
),
(
    8, 
    N'Camera an ninh Ezviz C6N 2K', 
    N'camera-an-ninh-ezviz-c6n-2k', 
    N'Độ phân giải 2K sắc nét, quan sát toàn cảnh 360 độ không điểm mù, tự động theo dõi chuyển động thông minh Smart Tracking.', 
    650000.00, 
    N'https://res.cloudinary.com/dfyfpuguj/image/upload/v1788451668/seed_prod_8.jpg', N'seed_prod_8', 4, SYSDATETIME(), SYSDATETIME()
);

SET IDENTITY_INSERT [dbo].[products] OFF;
GO
