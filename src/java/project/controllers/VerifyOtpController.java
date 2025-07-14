package project.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "VerifyOtpController", urlPatterns = {"/VerifyOtpController"})
public class VerifyOtpController extends HttpServlet {
    private static final String ERROR = "verifyOtp.jsp";
    private static final String SUCCESS = "resetPassword.jsp";
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR;
        try {
            String inputOtp = request.getParameter("otp");
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("otp") == null) {
                request.setAttribute("MESSAGE", "Session expired or invalid.");
            } else {
                String sessionOtp = (String) session.getAttribute("otp");
                Long otpCreatedAt = (Long) session.getAttribute("otpCreatedAt");

                if (otpCreatedAt == null || System.currentTimeMillis() - otpCreatedAt > OTP_VALID_DURATION) {
                    session.removeAttribute("otp");
                    session.removeAttribute("otpCreatedAt");
                    request.setAttribute("MESSAGE", "OTP expired. Please request a new one.");
                } else if (sessionOtp.equals(inputOtp)) {
                    session.removeAttribute("otp");
                    session.removeAttribute("otpCreatedAt");
                    url = SUCCESS;
                } else {
                    request.setAttribute("MESSAGE", "Incorrect OTP. Try again.");
                }
            }
        } catch (Exception e) {
            log("Error at VerifyOtpController: " + e.toString());
            request.setAttribute("MESSAGE", "Error verifying OTP.");
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
        return "Handles OTP verification from email.";
    }
}
