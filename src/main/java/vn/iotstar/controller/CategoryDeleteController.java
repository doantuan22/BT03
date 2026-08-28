package vn.iotstar.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

@WebServlet("/admin/category/delete")
public class CategoryDeleteController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try { categoryService.delete(Integer.parseInt(req.getParameter("id"))); resp.sendRedirect(req.getContextPath() + "/admin/category/list"); }
        catch (NumberFormatException e) { resp.sendError(HttpServletResponse.SC_BAD_REQUEST); }
    }
}
