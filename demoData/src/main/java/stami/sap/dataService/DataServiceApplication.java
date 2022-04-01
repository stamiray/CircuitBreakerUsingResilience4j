package stami.sap.dataService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class DataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataServiceApplication.class, args);
	}

	@RequestMapping("/data")
	public ResponseEntity<String> data() {
		return new ResponseEntity<String>( "Hello SAP Ariba, here is your data", HttpStatus.OK);
	}

}
