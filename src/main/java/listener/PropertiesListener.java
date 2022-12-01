package listener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PropertiesListener
 *
 */
@WebListener
public class PropertiesListener implements ServletContextListener {

	public PropertiesListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	//	Webアプリケーションのシャットダウン時に実行する処理
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	//	Webアプリケーションの起動時に実行する処理
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext context = arg0.getServletContext();

		//		プロパティファイルを読み込み、アプリケーションスコープに設定する
		try {
			InputStream is = PropertiesListener.class.getClassLoader().getResourceAsStream("application.properties");
			Properties properties = new Properties();
			properties.load(is);
			is.close();

			Iterator<String> pit = properties.stringPropertyNames().iterator();
			while (pit.hasNext()) {
				String pname = pit.next();
				context.setAttribute(pname, properties.getProperty(pname));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}