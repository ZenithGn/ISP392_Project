package project.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Random;

import project.model.dao.CustomerDAO;
import project.model.dto.CustomerDTO;
import project.utils.EmailUtils;

@WebServlet(name = "ForgotPasswordController", urlPatterns = {"/ForgotPasswordController"})
public class ForgotPasswordController extends HttpServlet {
    private static final String ERROR = "forgotPassword.jsp";
    private static final String SUCCESS = "verifyOtp.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR;
        try {
            String email = request.getParameter("email");
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("MESSAGE", "Please enter your email.");
            } else {
                CustomerDAO dao = new CustomerDAO();
                CustomerDTO customer = dao.findByEmail(email.trim());
                if (customer != null) {
                    String otp = String.valueOf(new Random().nextInt(900000) + 100000);
                    long otpCreatedAt = System.currentTimeMillis();
                    HttpSession session = request.getSession();
                    session.setAttribute("otp", otp);
                    session.setAttribute("otpCreatedAt", otpCreatedAt);
                    session.setAttribute("email", email);

                    try {
                        EmailUtils.sendOtpEmail(email, otp);
                        request.setAttribute("MESSAGE", "OTP has been sent to your email.");
                        url = SUCCESS;
                    } catch (Exception e) {
                        request.setAttribute("MESSAGE", "Failed to send OTP. Try again.");
                    }
                } else {
                    request.setAttribute("MESSAGE", "Email does not exist.");
                }
            }
        } catch (Exception e) {
            log("Error at ForgotPasswordController: " + e.toString());
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
        return "Handles Forgot Password - send OTP to email";
    }
}
