package vn.iotstar.controller.admin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.model.Category;
import vn.iotstar.model.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1MB
        maxFileSize = 1024 * 1024 * 10,        // 10MB
        maxRequestSize = 1024 * 1024 * 25      // 25MB
)
@WebServlet(urlPatterns = {
        "/admin/products",
        "/admin/products/*",
        "/admin/product/*"
})
public class ProductController extends HttpServlet {

    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    private final IProductService productService = new ProductServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = getActionPath(req);

        switch (path) {
            case "create", "add" -> create(req, resp);
            case "show", "detail" -> show(req, resp);
            case "edit" -> edit(req, resp);
            case "destroy", "delete" -> destroy(req, resp);
            default -> index(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String path = getActionPath(req);

        switch (path) {
            case "create", "store", "add" -> store(req, resp);
            case "edit", "update" -> update(req, resp);
            case "destroy", "delete" -> destroy(req, resp);
            default -> index(req, resp);
        }
    }

    /**
     * Action: index - List all products.
     */
    public void index(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Product> products = productService.findAll();
        req.setAttribute("productList", products);
        req.getRequestDispatcher("/views/admin/list-product.jsp").forward(req, resp);
    }

    /**
     * Action: create - Render new product form with categories.
     */
    public void create(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Category> categories = categoryService.getAll();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/views/admin/add-product.jsp").forward(req, resp);
    }

    /**
     * Action: store - Validate, upload image, and persist new product.
     */
    public void store(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String slug = req.getParameter("slug");
        String categoryIdStr = req.getParameter("category_id");
        String priceStr = req.getParameter("price");
        String description = req.getParameter("description");

        Product product = new Product();
        product.setName(name != null ? name.trim() : "");
        product.setDescription(description != null ? description.trim() : "");

        // Validation: Name
        if (name == null || name.isBlank()) {
            req.setAttribute("error", "Tên sản phẩm không được để trống.");
            req.setAttribute("product", product);
            create(req, resp);
            return;
        }

        // Slug computation / validation
        if (slug == null || slug.isBlank()) {
            slug = slugify(name.trim());
        } else {
            slug = slugify(slug.trim());
        }
        product.setSlug(slug);

        // Validation: Category
        int categoryId = 0;
        try {
            categoryId = Integer.parseInt(categoryIdStr);
            Category category = categoryService.get(categoryId);
            if (category == null) {
                req.setAttribute("error", "Danh mục đã chọn không hợp lệ.");
                req.setAttribute("product", product);
                create(req, resp);
                return;
            }
            product.setCategoryId(categoryId);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Vui lòng chọn danh mục cho sản phẩm.");
            req.setAttribute("product", product);
            create(req, resp);
            return;
        }

        // Validation: Price
        BigDecimal price;
        try {
            price = new BigDecimal(priceStr.trim());
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                req.setAttribute("error", "Giá sản phẩm phải lớn hơn hoặc bằng 0.");
                req.setAttribute("product", product);
                create(req, resp);
                return;
            }
            product.setPrice(price);
        } catch (Exception e) {
            req.setAttribute("error", "Giá sản phẩm không hợp lệ.");
            req.setAttribute("product", product);
            create(req, resp);
            return;
        }

        // Image upload
        try {
            Part filePart = req.getPart("image");
            String imagePath = saveImage(filePart);
            product.setImage(imagePath);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("product", product);
            create(req, resp);
            return;
        }

        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

        productService.insert(product);
        setSessionFlash(req, "Thêm sản phẩm mới thành công!");
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    /**
     * Action: show - Display product details.
     */
    public void show(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Product product = loadProduct(req);
        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm.");
            return;
        }
        req.setAttribute("product", product);
        req.getRequestDispatcher("/views/admin/show-product.jsp").forward(req, resp);
    }

    /**
     * Action: edit - Render edit form.
     */
    public void edit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Product product = loadProduct(req);
        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm.");
            return;
        }
        List<Category> categories = categoryService.getAll();
        req.setAttribute("product", product);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
    }

    /**
     * Action: update - Validate and update product.
     */
    public void update(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Product existing = loadProduct(req);
        if (existing == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm để cập nhật.");
            return;
        }

        String name = req.getParameter("name");
        String slug = req.getParameter("slug");
        String categoryIdStr = req.getParameter("category_id");
        String priceStr = req.getParameter("price");
        String description = req.getParameter("description");

        if (name == null || name.isBlank()) {
            req.setAttribute("error", "Tên sản phẩm không được để trống.");
            req.setAttribute("product", existing);
            req.setAttribute("categories", categoryService.getAll());
            req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
            return;
        }

        if (slug == null || slug.isBlank()) {
            slug = slugify(name.trim());
        } else {
            slug = slugify(slug.trim());
        }

        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdStr);
            Category cat = categoryService.get(categoryId);
            if (cat == null) {
                req.setAttribute("error", "Danh mục không hợp lệ.");
                req.setAttribute("product", existing);
                req.setAttribute("categories", categoryService.getAll());
                req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
                return;
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Vui lòng chọn danh mục.");
            req.setAttribute("product", existing);
            req.setAttribute("categories", categoryService.getAll());
            req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceStr.trim());
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                req.setAttribute("error", "Giá sản phẩm phải lớn hơn hoặc bằng 0.");
                req.setAttribute("product", existing);
                req.setAttribute("categories", categoryService.getAll());
                req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
                return;
            }
        } catch (Exception e) {
            req.setAttribute("error", "Giá sản phẩm không hợp lệ.");
            req.setAttribute("product", existing);
            req.setAttribute("categories", categoryService.getAll());
            req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
            return;
        }

        existing.setName(name.trim());
        existing.setSlug(slug);
        existing.setCategoryId(categoryId);
        existing.setPrice(price);
        existing.setDescription(description != null ? description.trim() : "");
        existing.setUpdatedAt(new Date());

        try {
            Part filePart = req.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String imagePath = saveImage(filePart);
                existing.setImage(imagePath);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("product", existing);
            req.setAttribute("categories", categoryService.getAll());
            req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req, resp);
            return;
        }

        productService.update(existing);
        setSessionFlash(req, "Cập nhật sản phẩm thành công!");
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    /**
     * Action: destroy - Delete product.
     */
    public void destroy(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            idStr = getPathId(req);
        }

        try {
            int id = Integer.parseInt(idStr);
            productService.delete(id);
            setSessionFlash(req, "Đã xóa sản phẩm thành công!");
        } catch (Exception e) {
            setSessionFlash(req, "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private Product loadProduct(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            idStr = getPathId(req);
        }
        try {
            return productService.findById(Integer.parseInt(idStr));
        } catch (Exception e) {
            return null;
        }
    }

    private String getActionPath(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        if (path.startsWith("/admin/products/")) {
            return path.substring("/admin/products/".length()).split("/")[0].toLowerCase();
        } else if (path.startsWith("/admin/product/")) {
            return path.substring("/admin/product/".length()).split("/")[0].toLowerCase();
        } else if (path.equals("/admin/products") || path.equals("/admin/product")) {
            return "index";
        }
        return "index";
    }

    private String getPathId(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());
        String[] parts = path.split("/");
        if (parts.length > 3) {
            return parts[3];
        }
        return null;
    }

    public static String saveImage(Part item) throws IOException {
        if (item == null || item.getSize() == 0) {
            return null;
        }
        String submitted = item.getSubmittedFileName();
        int dot = submitted == null ? -1 : submitted.lastIndexOf('.');
        String extension = dot < 0 ? "" : submitted.substring(dot + 1).toLowerCase();
        if (!IMAGE_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Định dạng ảnh không hợp lệ. Chỉ chấp nhận jpg, jpeg, png, gif, webp.");
        }

        File directory = new File(Constant.DIR, "product");
        Files.createDirectories(directory.toPath());
        String fileName = System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + "." + extension;
        File destFile = new File(directory, fileName);
        item.write(destFile.getAbsolutePath());
        return "product/" + fileName;
    }

    public static String slugify(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        String nowhitespace = WHITESPACE.matcher(input.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}", "-").replaceAll("^-|-$", "");
    }

    private void setSessionFlash(HttpServletRequest req, String message) {
        HttpSession session = req.getSession(true);
        session.setAttribute("msg", message);
    }
}
