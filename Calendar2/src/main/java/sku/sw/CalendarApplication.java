package sku.sw;

import javax.swing.SwingUtilities;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;
import sku.sw.views.CalendarView;

@Slf4j // 로깅 프레임워크
@SpringBootApplication
public class CalendarApplication implements CommandLineRunner , ApplicationContextAware{
	private static ApplicationContext applicationContext;
	
    public static void main(String[] args) {
    	
    	ConfigurableApplicationContext ctx = new SpringApplicationBuilder(CalendarApplication.class)
                .headless(false).run(args);
    	
//        log.info("STARTING THE APPLICATION");
//        SpringApplication.run(CalendarApplication.class, args);
//        log.info("APPLICATION FINISHED");
    }
 
    @Override
    public void run(String... args) {

    	
        System.setProperty("java.awt.headless", "false");
        SwingUtilities.invokeLater(() -> {
        	CalendarView calendar = applicationContext.getBean(CalendarView.class);
        	calendar.updateCalendar();
        	calendar.setVisible(true);
        });
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
}