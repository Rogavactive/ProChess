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
		if(reqType.equals("ajax")) {
			AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(manager.accountExists(username, password)) {
				response.getWriter().write("true");
			}else {
				response.getWriter().write("false");
			}
		}else if(reqType.equals("direct")) {
			AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			//security check again
			if(manager.accountExists(username, password)) {
				request.getSession().setAttribute("account", new Account(username));
				request.getRequestDispatcher("welcome.jsp").forward(request, response);
			}else {
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}	
		}
	}

}
