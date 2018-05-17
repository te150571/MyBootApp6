package jp.te4a.spring.boot.myapp10;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	
	@Autowired
	BookRepository bookRepository;
	
	public BookForm create(BookForm bookForm) {
		//bookForm.setId(bookRepository.getBookId());
		BookBean bookBean = new BookBean();
		BeanUtils.copyProperties(bookForm, bookBean);
		bookRepository.save(bookBean);
		return bookForm;
	}
	
	public BookForm update(BookForm bookForm) {
		BookBean bookBean = new BookBean();
		BeanUtils.copyProperties(bookForm, bookBean);
		bookRepository.save(bookBean);
		return bookForm;
	}
	
	public void delete(BookForm bookForm) {
		BookBean bookBean = new BookBean();
		BeanUtils.copyProperties(bookForm, bookBean);
		bookRepository.delete(bookBean);
	}
	
	public List<BookBean> findAll(){
		return bookRepository.findAll();
	}
	
	public BookForm findOne(Integer id) {
		Optional<BookBean> opt = bookRepository.findById(id);
		BookForm bookForm = new BookForm();
		opt.ifPresent(book -> {
			BeanUtils.copyProperties(opt, bookForm);
		});
		return bookForm;
	}
}
