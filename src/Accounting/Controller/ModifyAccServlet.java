package Accounting.Controller;

import Accounting.Model.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ModifyAccServlet")
public class ModifyAccServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("type");
        switch (reqType) {
            case "essential":
                processEssential(request, response);
                break;
            case "pass_change":
                processPassChange(request, response);
                break;
            case "pass_set":
                processPassSet(request, response);
                break;
        }
    }

    private void processPassChange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String oldPass = request.getParameter("old_pass");
        String newPass = request.getParameter("new_pass");
        Account acc = (Account) request.getSession().getAttribute("Account");
        if(oldPass==null||newPass==null||acc==null){
            response.getWriter().write("failure: one or two parameter(s) is(are) null.");
            return;
        }
        if(acc.changePassword(oldPass,newPass))
            response.getWriter().write("success");
        else
            response.getWriter().write("failure: could not change the password.");
    }

    private void processPassSet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newPass = request.getParameter("new_pass");
        Account acc = (Account) request.getSession().getAttribute("Account");
        if(newPass==null||acc==null){
            response.getWriter().write("failure: one or two parameter(s) is(are) null.");
            return;
        }
        if(acc.changePassword(null,newPass))
            response.getWriter().write("success");
        else
            response.getWriter().write("failure: could not set the password.");
    }

    private void processEssential(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        Account acc = (Account) request.getSession().getAttribute("Account");
        if(email==null||username==null||acc==null){
            response.getWriter().write("failure: one or two parameter(s) is(are) null.");
            return;
        }
        if(acc.change(username,email)){
            response.getWriter().write("success");
        }else{
            response.getWriter().write("failure: could not change the info.");
        }
    }


}
