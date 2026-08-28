-- Migration: Create products table
-- Fields:
--   id            : INT IDENTITY(1,1) PRIMARY KEY
--   name          : NVARCHAR(255) NOT NULL
--   slug          : NVARCHAR(255) NOT NULL
--   description   : NVARCHAR(MAX) NULL
--   price         : DECIMAL(18, 2) NOT NULL
--   image         : NVARCHAR(500) NULL
--   category_id   : INT NOT NULL (Foreign Key to category.cate_id)
--   created_at    : DATETIME2 NOT NULL DEFAULT SYSDATETIME()
--   updated_at    : DATETIME2 NOT NULL DEFAULT SYSDATETIME()
-- Indexes:
--   - Index on slug
--   - Index on category_id
-- Foreign Key:
--   - FK_products_category referencing category(cate_id)

IF NOT EXISTS (
    SELECT 1 FROM sys.tables WHERE name = 'products' AND schema_id = SCHEMA_ID('dbo')
)
BEGIN
    CREATE TABLE [dbo].[products] (
        [id]            INT IDENTITY(1,1) NOT NULL,
        [name]          NVARCHAR(255) NOT NULL,
        [slug]          NVARCHAR(255) NOT NULL,
        [description]   NVARCHAR(MAX) NULL,
        [price]         DECIMAL(18, 2) NOT NULL DEFAULT 0.00,
        [image]         NVARCHAR(500) NULL,
        [category_id]   INT NOT NULL,
        [created_at]    DATETIME2 NOT NULL CONSTRAINT DF_products_created_at DEFAULT SYSDATETIME(),
        [updated_at]    DATETIME2 NOT NULL CONSTRAINT DF_products_updated_at DEFAULT SYSDATETIME(),
        CONSTRAINT [PK_products] PRIMARY KEY CLUSTERED ([id] ASC),
        CONSTRAINT [FK_products_category] FOREIGN KEY ([category_id]) 
            REFERENCES [dbo].[category] ([cate_id]) 
            ON DELETE CASCADE 
            ON UPDATE CASCADE
    );
END;

-- Create index on slug
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes 
    WHERE name = 'IX_products_slug' AND object_id = OBJECT_ID(N'[dbo].[products]')
)
BEGIN
    CREATE NONCLUSTERED INDEX [IX_products_slug] 
    ON [dbo].[products] ([slug] ASC);
END;

-- Create index on category_id
IF NOT EXISTS (
    SELECT 1 FROM sys.indexes 
    WHERE name = 'IX_products_category_id' AND object_id = OBJECT_ID(N'[dbo].[products]')
)
BEGIN
    CREATE NONCLUSTERED INDEX [IX_products_category_id] 
    ON [dbo].[products] ([category_id] ASC);
END;
