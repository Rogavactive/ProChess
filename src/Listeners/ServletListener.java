package Listeners;

import Accounting.Model.Account;
import Accounting.Model.AccountManager;
import Game.Model.GameManager;
import GameConnection.Model.GameSearchManager;
import dbConnection.DataBaseMainManager;
import dbConnection.DataBaseManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener()
public class ServletListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public ServletListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        DataBaseMainManager manager = DataBaseMainManager.getInstance();
        sce.getServletContext().setAttribute("AccManager", new AccountManager(manager));
        sce.getServletContext().setAttribute("GameManager", GameManager.getInstance());
        sce.getServletContext().setAttribute("GameSearchManager", GameSearchManager.getInstance());
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
    }

    public void contextDestroyed(ServletContextEvent sce) {
        ((AccountManager) sce.getServletContext().getAttribute("AccManager")).dispose();
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        Account acc = (Account) se.getSession().getAttribute("Account");
        GameSearchManager searchManager = (GameSearchManager) se.getSession().getServletContext().getAttribute("GameSearchManager");
        searchManager.removeFromQueue(acc.getID());
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
    }
}