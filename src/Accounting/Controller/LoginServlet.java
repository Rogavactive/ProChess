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
        if (reqType.equals("ajax")) {
            processAjaxMessage(request, response);
        } else if (reqType.equals("direct")) {
            processDirectMessage(request, response);
        }else if(reqType.equals("google")){
            processGoogleMessage(request,response);
        }
    }

    private void processGoogleMessage(HttpServletRequest request, HttpServletResponse response) {
        //TODO: write google authentification.
    }

    private void processDirectMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(password==null)
            password="";//security change
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

    private void processAjaxMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
