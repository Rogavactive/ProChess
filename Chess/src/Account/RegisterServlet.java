package Account;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String reqType = request.getParameter("registerType");
		if (reqType.equals("ajax")) {
			AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
			String username = request.getParameter("username");
			String email = request.getParameter("email");
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
			AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
			String username = request.getParameter("username");
			String email = request.getParameter("email");
			// security check 
			if (!manager.existsUsername(username)&&!manager.existsEmail(email)) {
				String password = request.getParameter("password");
				if(validatePassword(password,username)) {
					manager.register(username, email, password);
					request.getSession().setAttribute("account", new Account(username));
					request.getRequestDispatcher("welcome.jsp").forward(request, response);
				}
			} else {
				request.getRequestDispatcher("register.html").forward(request, response);
			}
		}
	}

	private boolean validatePassword(String password,String username) {
		if(password.equals(username))
			return false;
		return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
	}

}
