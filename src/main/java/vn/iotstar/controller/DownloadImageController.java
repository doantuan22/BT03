package vn.iotstar.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import vn.iotstar.util.Constant;

@WebServlet("/image")
public class DownloadImageController extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fileName = req.getParameter("fname");
        if (fileName == null || !fileName.matches("category/[A-Za-z0-9._-]+")) { resp.sendError(HttpServletResponse.SC_BAD_REQUEST); return; }
        File file = new File(Constant.DIR, fileName); if (!file.isFile()) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        resp.setContentType(getServletContext().getMimeType(file.getName()));
        try (FileInputStream input = new FileInputStream(file)) { IOUtils.copy(input, resp.getOutputStream()); }
    }
}
