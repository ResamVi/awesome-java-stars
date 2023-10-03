package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import parser.GitHub;
import parser.Category;
import parser.Parser;

import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class Server {
	private final Logger LOGGER = Logger.getLogger(Server.class.getName());

	@Autowired
	private Cron cron;

	@GetMapping("/")
	public String data(Model model) {
		LOGGER.info("incoming request");

		model.addAttribute("data", cron.getCategories());
		return "index";
	}

	@GetMapping("/health")
	public String health() {
		return "OK";
	}

}
