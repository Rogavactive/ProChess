package Accounting.Controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import Accounting.Model.Account;
import Accounting.Model.AccountManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final String CLIENT_ID = "690644503931-dtn1qj0me45ovni28qbsa12g8d6c2ccf.apps.googleusercontent.com";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reqType = request.getParameter("loginType");
        switch (reqType) {
            case "native":
                processNativeMessage(request, response);
                break;
            case "google":
                processGoogleMessage(request, response);
                break;
        }
    }

    private void processGoogleMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = retrieveEmail(request.getParameter("token"));
        if (email == null) return;

        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
        Account acc = manager.googleAccountExists(email);
        if (acc == null) {
            acc = manager.register(generateUsername(email,manager), email, null);
        }

        request.getSession().setAttribute("Account", acc);
        response.sendRedirect("main.jsp");
    }

    private String retrieveEmail(String id_token_string) {
        GoogleIdTokenVerifier verifier = null;
        try {
            verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(id_token_string);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            return email;
        }

        return null;
    }

    private String generateUsername(String email, AccountManager manager) {
        int index = email.indexOf('@');
        String email_prefix = email.substring(0, index);
        String prefix_addition = "";

        while (manager.existsUsername(email_prefix + prefix_addition)) {
            prefix_addition = new Integer((int) (Math.random() * 1000)).toString();
        }

        return email_prefix+prefix_addition;
    }

    private void processNativeMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");

        Account acc = manager.accountExists(username, password);
        //security check again
        if (acc != null) {
            request.getSession().setAttribute("Account", acc);
            response.getWriter().write("true");
        } else {
            response.getWriter().write("false");
        }
    }

}
