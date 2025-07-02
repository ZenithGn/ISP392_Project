package project.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.model.dao.ServiceDAO;
import project.model.dto.ServiceDTO;

@WebServlet(name = "CreateServiceController", urlPatterns = {"/CreateServiceController"})
public class CreateServiceController extends HttpServlet {

    private static final String ERROR = "manageService.jsp";
    private static final String SUCCESS = "manageService.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String url = ERROR;
        
        try {
            String name = request.getParameter("serviceName");
            String priceStr = request.getParameter("servicePrice");
            String garageIdStr = request.getParameter("garageId");
            String isActiveStr = request.getParameter("isActive");

            // Validate parameters
            if (name == null || name.trim().isEmpty()) {
                throw new Exception("Service name is required");
            }
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new Exception("Service price is required");
            }
            if (garageIdStr == null || garageIdStr.trim().isEmpty()) {
                throw new Exception("Garage ID is required");
            }

            BigDecimal price = new BigDecimal(priceStr);
            int garageId = Integer.parseInt(garageIdStr);
            boolean isActive = isActiveStr != null ? Boolean.parseBoolean(isActiveStr) : true;

            ServiceDTO dto = new ServiceDTO(0, name, price, garageId, isActive);
            boolean success = new ServiceDAO().createService(dto);

            if (success) {
                url = SUCCESS;
            } else {
                throw new Exception("Failed to create service");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid number format for price or garage ID");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error creating service: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Create Service Controller";
    }
}