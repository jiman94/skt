package oss.product.controller;

import lombok.extern.slf4j.Slf4j;
import oss.product.infra.read.ProductReadRepository;
import oss.product.model.read.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jaceshim on 2017. 4. 2..
 */
@Controller
@Slf4j
public class ProductFrontController {

	@Autowired
	private ProductReadRepository productReadRepository;

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String getProducts(Model model) {
		final List<Product> products = productReadRepository.findAll();
		model.addAttribute("products", products);

		return "products";
	}

	@RequestMapping(value = "/setproducts", method = RequestMethod.GET)
	public String setProducts(Model model) {
		final List<Product> products = productReadRepository.findAll();
		model.addAttribute("products", products);

		return "setproducts";
	}
	
	@RequestMapping(value = "/fileupload", method = RequestMethod.GET)
	public String fileupload(Model model) {
		final List<Product> products = productReadRepository.findAll();
		model.addAttribute("products", products);

		return "fileupload";
	}
	
	
	@RequestMapping(value = "/products", params = "type=createProduct", method = RequestMethod.GET)
	public String createProduct(Model model) {
		model.addAttribute("images", getImages());
		return "createProduct";
	}

	private List<String> getImages() {
		return Arrays.asList("polo.jpg", "w-denim.jpg", "pant1.jpg", "t-shirt.jpg", "backpack.jpg", "denim.jpg", "flops.jpg", "w-cardigan.jpg");
	}
}
