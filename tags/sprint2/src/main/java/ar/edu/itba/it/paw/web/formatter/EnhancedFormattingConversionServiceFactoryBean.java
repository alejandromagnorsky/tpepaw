package ar.edu.itba.it.paw.web.formatter;

import java.util.Map;

import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

public class EnhancedFormattingConversionServiceFactoryBean extends
		FormattingConversionServiceFactoryBean {

	private Map<String,Formatter<?>> formatters;

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		
		 for (Map.Entry<String, Formatter<?>> entry : formatters.entrySet()) {
	            String className = entry.getKey();
	            try {
	                Class<?> clazz = Class.forName(className);
	                registry.addFormatterForFieldType(clazz, entry.getValue());
	            } catch (ClassNotFoundException e) {
	            	throw new RuntimeException("Error cargando los formatters; Class not found for class name==" + className, e);
	            }
	        }
	}
	
	public void setFormatters(Map<String, Formatter<?>> formatters){
        this.formatters = formatters;
    }
}
