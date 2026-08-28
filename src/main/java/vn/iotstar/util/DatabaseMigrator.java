package vn.iotstar.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMigrator {

    private static final String DEFAULT_URL =
            "jdbc:sqlserver://localhost:1433;databaseName=ShoppingServiceMVC;encrypt=true;trustServerCertificate=true";
    private static final String DEFAULT_USER = "sa";
    private static final String DEFAULT_PASS = "doantuan";

    public static void main(String[] args) {
        runMigrations();
    }

    public static boolean runMigrations() {
        System.out.println("Starting Database Migration...");
        try (Connection conn = getConnection()) {
            System.out.println("Connected to SQL Server: " + conn.getCatalog());

            // 1. Add otp_code if not exists
            executeSql(conn, """
                IF NOT EXISTS (
                    SELECT 1 FROM sys.columns 
                    WHERE object_id = OBJECT_ID(N'[dbo].[User]') AND name = 'otp_code'
                )
                BEGIN
                    ALTER TABLE [dbo].[User] ADD [otp_code] NVARCHAR(50) NULL;
                    PRINT 'Added column otp_code to [User] table.';
                END;
            """);

            // 2. Add otp_expires_at if not exists
            executeSql(conn, """
                IF NOT EXISTS (
                    SELECT 1 FROM sys.columns 
                    WHERE object_id = OBJECT_ID(N'[dbo].[User]') AND name = 'otp_expires_at'
                )
                BEGIN
                    ALTER TABLE [dbo].[User] ADD [otp_expires_at] DATETIME2 NULL;
                    PRINT 'Added column otp_expires_at to [User] table.';
                END;
            """);

            // 3. Add is_activated if not exists
            executeSql(conn, """
                IF NOT EXISTS (
                    SELECT 1 FROM sys.columns 
                    WHERE object_id = OBJECT_ID(N'[dbo].[User]') AND name = 'is_activated'
                )
                BEGIN
                    ALTER TABLE [dbo].[User] ADD [is_activated] BIT NOT NULL CONSTRAINT DF_User_is_activated DEFAULT 0;
                    PRINT 'Added column is_activated to [User] table.';
                END;
            """);

            // 4. Create products table if not exists
            executeSql(conn, """
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
                    PRINT 'Created table [dbo].[products].';
                END;
            """);

            // 5. Create index on slug
            executeSql(conn, """
                IF NOT EXISTS (
                    SELECT 1 FROM sys.indexes 
                    WHERE name = 'IX_products_slug' AND object_id = OBJECT_ID(N'[dbo].[products]')
                )
                BEGIN
                    CREATE NONCLUSTERED INDEX [IX_products_slug] 
                    ON [dbo].[products] ([slug] ASC);
                    PRINT 'Created index IX_products_slug on [products].';
                END;
            """);

            // 6. Create index on category_id
            executeSql(conn, """
                IF NOT EXISTS (
                    SELECT 1 FROM sys.indexes 
                    WHERE name = 'IX_products_category_id' AND object_id = OBJECT_ID(N'[dbo].[products]')
                )
                BEGIN
                    CREATE NONCLUSTERED INDEX [IX_products_category_id] 
                    ON [dbo].[products] ([category_id] ASC);
                    PRINT 'Created index IX_products_category_id on [products].';
                END;
            """);

            System.out.println("Migration executed successfully!");
            printUserTableColumns(conn);
            printProductsTableDetails(conn);
            return true;
        } catch (Exception e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void executeSql(Connection conn, String sql) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DEFAULT_URL, DEFAULT_USER, DEFAULT_PASS);
    }

    public static void printUserTableColumns(Connection conn) throws Exception {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getColumns("ShoppingServiceMVC", null, "User", "%")) {
            System.out.println("\n--- Current Columns in [User] Table ---");
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String typeName = rs.getString("TYPE_NAME");
                String isNullable = rs.getString("IS_NULLABLE");
                String colDef = rs.getString("COLUMN_DEF");
                System.out.printf(" - %-20s %-15s (Nullable: %-3s, Default: %s)%n", colName, typeName, isNullable, colDef);
            }
            System.out.println("----------------------------------------\n");
        }
    }

    public static void printProductsTableDetails(Connection conn) throws Exception {
        DatabaseMetaData meta = conn.getMetaData();
        System.out.println("--- Columns in [products] Table ---");
        try (ResultSet rs = meta.getColumns("ShoppingServiceMVC", null, "products", "%")) {
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String typeName = rs.getString("TYPE_NAME");
                String isNullable = rs.getString("IS_NULLABLE");
                String colDef = rs.getString("COLUMN_DEF");
                System.out.printf(" - %-20s %-15s (Nullable: %-3s, Default: %s)%n", colName, typeName, isNullable, colDef);
            }
        }

        System.out.println("\n--- Foreign Keys for [products] ---");
        try (ResultSet rs = meta.getImportedKeys("ShoppingServiceMVC", null, "products")) {
            while (rs.next()) {
                String pkTable = rs.getString("PKTABLE_NAME");
                String pkCol = rs.getString("PKCOLUMN_NAME");
                String fkCol = rs.getString("FKCOLUMN_NAME");
                String fkName = rs.getString("FK_NAME");
                System.out.printf(" - FK: %s (%s) -> %s(%s)%n", fkName, fkCol, pkTable, pkCol);
            }
        }

        System.out.println("\n--- Indexes on [products] ---");
        try (ResultSet rs = meta.getIndexInfo("ShoppingServiceMVC", null, "products", false, false)) {
            while (rs.next()) {
                String indexName = rs.getString("INDEX_NAME");
                String colName = rs.getString("COLUMN_NAME");
                boolean nonUnique = rs.getBoolean("NON_UNIQUE");
                if (indexName != null) {
                    System.out.printf(" - Index: %-25s on Column: %-20s (Non-Unique: %b)%n", indexName, colName, nonUnique);
                }
            }
        }
        System.out.println("-------------------------------------\n");
    }
}
