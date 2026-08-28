package vn.iotstar.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

@MultipartConfig
@WebServlet("/admin/category/edit")
public class CategoryEditController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category = loadCategory(req); if (category == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        req.setAttribute("category", category); req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category current = loadCategory(req); String name = req.getParameter("name");
        if (current == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        if (name == null || name.isBlank()) { req.setAttribute("category", current); req.setAttribute("error", "Category name is required."); req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp); return; }
        try { Category category = new Category(); category.setId(current.getId()); category.setName(name.trim()); category.setIcon(CategoryAddController.saveImage(req.getPart("icon"))); categoryService.edit(category); resp.sendRedirect(req.getContextPath() + "/admin/category/list"); }
        catch (IllegalArgumentException e) { req.setAttribute("category", current); req.setAttribute("error", e.getMessage()); req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp); }
    }
    private Category loadCategory(HttpServletRequest req) { try { return categoryService.get(Integer.parseInt(req.getParameter("id"))); } catch (NumberFormatException e) { return null; } }
}
