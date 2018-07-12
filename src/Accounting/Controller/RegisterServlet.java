package Accounting.Controller;

import Accounting.Model.AccountManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processMessage(request, response);
    }

    private void processMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        boolean registerIsValid = true;
        String response_string = "";

        if (manager.existsUsername(username)) {
            registerIsValid = false;
            response_string += "false ";
        } else {
            response_string += "true ";
        }

        if (manager.existsEmail(email)) {
            registerIsValid = false;
            response_string += "false ";
        } else {
            response_string += "true ";
        }

        if (!registerIsValid) {
            response.getWriter().write(response_string + "false");
            return;
        }

        String password = request.getParameter("password");
        if (password == null) password = "";

        // security check
        if (validate(password, username)) {
            if (!manager.sendValidate(username, email, password)) {
                response.getWriter().write(response_string + "false");
            } else {
                response.getWriter().write(response_string + "true");
            }
        } else {
            response.getWriter().write(response_string + "false");
        }
    }

    private boolean validate(String password, String username) {
        if (password.equals(username) || username.length() < 8 || username.length() > 20)
            return false;
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

}
