package accounting;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("loginType");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        Account acc = manager.accountExists(username, password);
        if (reqType.equals("ajax")) {
            if (acc != null) {
                response.getWriter().write("true");
            } else {
                response.getWriter().write("false");
            }
        } else if (reqType.equals("direct")) {
            //security check again
            if (acc != null) {
                request.getSession().setAttribute("Account", acc);
                response.sendRedirect("main.jsp");
            } else {
                response.sendRedirect("login.html");
            }
        }
    }

}
