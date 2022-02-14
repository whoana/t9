/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
 

/**
 * <pre>
 * The Application is a spring boot main application
 * </pre>
 * @author whoana
 * @since Aug 7, 2019
 */
@RestController
@SpringBootApplication 
@EnableAdminServer
public class Admin  {

 
	public static void main(String[] args) { 
		SpringApplicationBuilder sab = new SpringApplicationBuilder(Admin.class);
		{
			String iwannadie = System.getProperty("rose.mary.admin.home", ".") + File.separator + "iwannadie.pid";
			sab.build().addListeners(new ApplicationPidFileWriter(iwannadie));
		}
		sab.run(args);
	}
 
}
