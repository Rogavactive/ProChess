package Account;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ServletContextLifecycleListener
 *
 */
@WebListener
public class ServletContextLifecycleListener implements ServletContextListener {
	AccountManager manager = new AccountManager();

    /**
     * Default constructor. 
     */
    public ServletContextLifecycleListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	manager.dispose();
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
		sce.getServletContext().setAttribute("AccManager", manager);
         // TODO Auto-generated method stub
    }
	
}
