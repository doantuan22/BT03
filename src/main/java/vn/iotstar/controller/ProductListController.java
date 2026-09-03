package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.Category;
import vn.iotstar.model.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = {
        "/product",
        "/product/list",
        "/products"
})
public class ProductListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final int PAGE_SIZE = 6;

    private final IProductService productService = new ProductServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pageStr = req.getParameter("page");
        String categoryIdStr = req.getParameter("category_id");
        if (categoryIdStr == null || categoryIdStr.isBlank()) {
            categoryIdStr = req.getParameter("cid");
        }
        String sort = req.getParameter("sort");
        if (sort == null || sort.isBlank()) {
            sort = "newest";
        }
        String keyword = req.getParameter("keyword");
        if (keyword == null || keyword.isBlank()) {
            keyword = req.getParameter("q");
        }
        if (keyword != null) {
            keyword = keyword.trim();
        }

        int page = 1;
        if (pageStr != null && !pageStr.isBlank()) {
            try {
                page = Integer.parseInt(pageStr.trim());
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.isBlank()) {
            try {
                int cid = Integer.parseInt(categoryIdStr.trim());
                if (cid > 0) {
                    categoryId = cid;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        int totalProducts = productService.countFiltered(categoryId, keyword);
        int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);
        if (totalPages < 1) {
            totalPages = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        List<Product> productList = productService.findPaginated(page, PAGE_SIZE, categoryId, keyword, sort);
        List<Category> categories = categoryService.getAll();

        req.setAttribute("productList", productList);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", page);
        req.setAttribute("pageSize", PAGE_SIZE);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalProducts);
        req.setAttribute("selectedCategoryId", categoryId);
        req.setAttribute("selectedSort", sort);
        req.setAttribute("keyword", keyword != null ? keyword : "");

        req.getRequestDispatcher("/views/web/product-list.jsp").forward(req, resp);
    }
}
