package Accounting.Controller;

import Accounting.Model.Account;
import Accounting.Model.AccountManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reqType = request.getParameter("loginType");
        switch (reqType) {
            case "ajax":
                processAjaxMessage(request, response);
                break;
            case "direct":
                processDirectMessage(request, response);
                break;
            case "google":
                processGoogleMessage(request, response);
                break;
        }
    }

    private void processGoogleMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        System.out.println(email);
        //TODO: write check in back, not front;
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        Account acc = manager.googleAccountExists(email);
        if(acc==null) {
            acc = manager.register(generateUsername(email), email, null);
        }
        request.getSession().setAttribute("Account", acc);
        //TODO: write google authentification.
    }

    private String generateUsername(String email){
        //TODO: create unique username.
        return email;
    }

    private void processDirectMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        Account acc = manager.accountExists(username, password);
        //security check again
        if (acc != null) {
            request.getSession().setAttribute("Account", acc);
            response.sendRedirect("main.jsp");
        } else {
            response.sendRedirect("login.html");
        }
    }

    private void processAjaxMessage(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        Account acc = manager.accountExists(username, password);
        if (acc != null) {
            response.getWriter().write("true");
        } else {
            response.getWriter().write("false");
        }
    }

}
