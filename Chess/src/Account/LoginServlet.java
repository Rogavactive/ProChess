package Account;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.bcel.internal.generic.LNEG;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reqType = request.getParameter("loginType");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
		Account acc = manager.accountExists(username, password);
		if(reqType.equals("ajax")) {
			if(acc!=null) {
				response.getWriter().write("true");
			}else {
				response.getWriter().write("false");
			}
		}else if(reqType.equals("direct")) {
			//security check again
			if(acc!=null) {
				request.getSession().setAttribute("Account", acc);
				request.getRequestDispatcher("main.jsp").forward(request, response);
			}else {
				request.getRequestDispatcher("login.html").forward(request, response);
			}	
		}
	}

}
