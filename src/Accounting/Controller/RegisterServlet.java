package Accounting.Controller;

import Accounting.Model.Account;
import Accounting.Model.AccountManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reqType = request.getParameter("registerType");
        if (reqType.equals("ajax")) {
            processAjaxMessage(request, response);
        } else if (reqType.equals("direct")) {
            processDirectMessage(request, response);
        }else if(reqType.equals("google")){
            processGoogle(request,response);
        }else if(reqType.equals("validate_ajax")){
            //TODO: validate for ajax. just return false or true.
        }else if(reqType.equals("validate_direct")){
            //TODO: validate code direct from the form. security check here.
        }
    }

    private void processGoogle(HttpServletRequest request, HttpServletResponse response) {
        //TODO: google registration.
    }

    private void processAjaxMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String response_string = "";
        if (manager.existsUsername(username)) {
            response_string += "false ";
        } else {
            response_string += "true ";
        }
        if (manager.existsEmail(email)!=null) {
            response_string += "false";
        } else {
            response_string += "true";
        }
        response.getWriter().write(response_string);
    }

    private void processDirectMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if(password==null)
            password="";//security change
        // security check
        if (validate(password, username, email, manager)) {
            //TODO: when we start implementing google, change manager.register with sendValidate
            Account acc = manager.register(username, email, password);
            if (acc == null) {
                response.sendRedirect("register.html");
                return;
            }
            request.getSession().setAttribute("Account", acc);
            response.sendRedirect("main.jsp");
        } else {
            response.sendRedirect("register.html");
        }
    }

    private boolean validate(String password, String username, String email, AccountManager manager) {
        if (manager.existsUsername(username) || manager.existsEmail(email)!=null)
            return false;
        if (password.equals(username) || username.length() < 8 || username.length() > 20)
            return false;
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

}
