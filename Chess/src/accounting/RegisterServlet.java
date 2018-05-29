package accounting;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("registerType");
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        if (reqType.equals("ajax")) {
            String response_string = "";
            if (manager.existsUsername(username)) {
                response_string += "false ";
            } else {
                response_string +="true ";
            }
            if(manager.existsEmail(email)) {
                response_string +="false";
            }else {
                response_string +="true";
            }
            response.getWriter().write(response_string);
        } else if (reqType.equals("direct")) {
            String password = request.getParameter("password");
            // security check
            if (validate(password,username,email,manager)) {
                Account acc = manager.register(username, email, password);
                if(acc==null) {
                    request.getRequestDispatcher("register.html").forward(request, response);
                    return;
                }
                request.getSession().setAttribute("Account", acc);
                request.getRequestDispatcher("main.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("register.html").forward(request, response);
            }
        }
    }

    private boolean validate(String password,String username,String email,AccountManager manager) {
        if(manager.existsUsername(username)||manager.existsEmail(email))
            return false;
        if(password.equals(username)||username.length()<8||username.length()>20)
            return false;
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

}
