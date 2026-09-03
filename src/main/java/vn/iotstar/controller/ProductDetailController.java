package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.Product;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = {
        "/product/*",
        "/products/*"
})
public class ProductDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IProductService productService = new ProductServiceImpl();
    private final ProductListController listController = new ProductListController();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equalsIgnoreCase("/list")) {
            listController.doGet(req, resp);
            return;
        }

        if (pathInfo.equalsIgnoreCase("/detail")) {
            handleDetailByParam(req, resp);
            return;
        }

        String slugOrId = pathInfo.substring(1).trim();
        if (slugOrId.isEmpty()) {
            listController.doGet(req, resp);
            return;
        }

        Product product = productService.findBySlug(slugOrId);

        if (product == null) {
            try {
                int id = Integer.parseInt(slugOrId);
                product = productService.findById(id);
            } catch (NumberFormatException ignored) {
            }
        }

        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm yêu cầu.");
            return;
        }

        req.setAttribute("product", product);
        req.getRequestDispatcher("/views/web/product-detail.jsp").forward(req, resp);
    }

    private void handleDetailByParam(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String slug = req.getParameter("slug");

        Product product = null;
        if (slug != null && !slug.isBlank()) {
            product = productService.findBySlug(slug.trim());
        }
        if (product == null && idStr != null && !idStr.isBlank()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                product = productService.findById(id);
            } catch (NumberFormatException ignored) {
            }
        }

        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm yêu cầu.");
            return;
        }

        req.setAttribute("product", product);
        req.getRequestDispatcher("/views/web/product-detail.jsp").forward(req, resp);
    }
}
