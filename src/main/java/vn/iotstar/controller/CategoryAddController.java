package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.Constant;

@MultipartConfig
@WebServlet("/admin/category/add")
public class CategoryAddController extends HttpServlet {
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private final CategoryService categoryService = new CategoryServiceImpl();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { req.getRequestDispatcher("/views/admin/add-category.jsp").forward(req, resp); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null || name.isBlank()) { req.setAttribute("error", "Category name is required."); doGet(req, resp); return; }
        Category category = new Category(); category.setName(name.trim());
        try { category.setIcon(saveImage(req.getPart("icon"))); categoryService.insert(category); resp.sendRedirect(req.getContextPath() + "/admin/category/list"); }
        catch (IllegalArgumentException e) { req.setAttribute("error", e.getMessage()); doGet(req, resp); }
    }
    static String saveImage(Part item) throws IOException {
        if (item == null || item.getSize() == 0) return null;
        String submitted = item.getSubmittedFileName(); int dot = submitted == null ? -1 : submitted.lastIndexOf('.');
        String extension = dot < 0 ? "" : submitted.substring(dot + 1).toLowerCase();
        if (!IMAGE_EXTENSIONS.contains(extension)) throw new IllegalArgumentException("Only image files are allowed.");
        File directory = new File(Constant.DIR, "category"); Files.createDirectories(directory.toPath());
        String fileName = System.currentTimeMillis() + "." + extension;
        item.write(new File(directory, fileName).getAbsolutePath()); return "category/" + fileName;
    }
}
