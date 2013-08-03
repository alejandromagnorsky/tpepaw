package ar.edu.itba.it.paw.web.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class ExceptionHandler extends SimpleMappingExceptionResolver {

	private static Logger logger = Logger.getLogger("logger");

	@Override
	public ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception e) {

		doLog(getStackTrace(e));
		//e.printStackTrace();
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("errormsg", "500 - Error interno del servidor.");
		mav.setViewName("error");

		return mav;
	}

	private void doLog(String msg) {
		URL url = Loader.getResource("log4j.properties");
		PropertyConfigurator.configure(url);

		logger.fatal(msg);
	}

	private String getStackTrace(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
