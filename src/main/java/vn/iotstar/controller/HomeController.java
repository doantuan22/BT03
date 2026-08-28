package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.Product;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = { "/home", "/trang-chu" })
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Product> top10Products = productService.findTop10Newest();
        req.setAttribute("top10Products", top10Products);
        req.setAttribute("productList", top10Products);
        req.getRequestDispatcher("/views/web/home.jsp").forward(req, resp);
    }
}
