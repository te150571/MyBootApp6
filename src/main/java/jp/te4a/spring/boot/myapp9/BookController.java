package jp.te4a.spring.boot.myapp9;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("books")
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@ModelAttribute
	BookForm setUpForm() {
		return new BookForm();
	}
	
	@GetMapping
	String list(Model model) {
		model.addAttribute("books", bookService.findAll());
		return "books/list";
	}
	@PostMapping(path="create")
	String create(BookForm form, Model model) {
		bookService.create(form);
		return "redirect:/books";
	}
	@PostMapping(path = "edit", params = "form")
	String editForm(@RequestParam Integer id, BookForm form) {
		BookForm bookForm = bookService.findOne(id);
		BeanUtils.copyProperties(bookForm, form);
		return "books/edit";
	}
	@PostMapping(path = "edit")
	String edit(@RequestParam Integer id, BookForm form) {
		bookService.update(form);
		return "redirect:/books";
	}
	@PostMapping(path = "delete")
	String delete(@RequestParam Integer id, BookForm form) {
		bookService.delete(form);
		return "redirect:/books";
	}
	@PostMapping(path = "edit", params = "goToTop")
	String goToTop() {
		return "redirect:/books";
	}
	
//	@RequestMapping("books/list")
//	public String index (Model model) {
//		model.addAttribute("msg", "This is setting message.");
//		return "books/list";
//	}
//	
//	@RequestMapping(value="books/list", method=RequestMethod.POST)
//	public ModelAndView postFrom(@RequestParam("id") String id, @RequestParam("title") String title,
//			@RequestParam("author") String author, @RequestParam("publisher") String publisher, @RequestParam("price") String price) {
//		ModelAndView mv = new ModelAndView("books/list");
//		bookService.save(new BookBean(Integer.valueOf(id), title, author, publisher, Integer.valueOf(price)));
//		StringBuffer buff = new StringBuffer();
//		buff.append("<HR>");
//		for(BookBean bean : bookService.findAll()) {
//			buff.append("ID:" + bean.getId() + "<BR>" + "Title:" + bean.getTitle() + "<BR>" + "Author:" + bean.getAuthor() + "<BR>" + "Publisher:" + bean.getPublisher() + "<BR>" + "Price:" + bean.getPrice() + "<BR><HR>");
//		}
//		mv.addObject("books", bookService.findAll());
//		return mv;
//	}
}
