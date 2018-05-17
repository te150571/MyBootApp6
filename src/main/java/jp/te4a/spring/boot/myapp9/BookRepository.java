package jp.te4a.spring.boot.myapp9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<BookBean, Integer> {
//	@Query("SELECT X FROM BookBean X ORDER BY X.Title")
//	List<BookBean>findAllOrderByTitle();
//	private final ConcurrentMap<Integer, BookBean>bookMap = new ConcurrentHashMap<>();
//	private int BOOK_ID = 1;
//	public int getBookId() {
//		return BOOK_ID++;
//	}
//	
//	public BookBean create(BookBean bookBean) {
//		return bookMap.put(bookBean.getId(), bookBean);
//	}
//	
//	public BookBean update(BookBean updateBookBean) {
//		BookBean bookBean = bookMap.get(updateBookBean.getId());
//		BeanUtils.copyProperties(updateBookBean, bookBean);
//		return bookBean;
//	}
//	
//	public void delete(Integer bookId) {
//		bookMap.remove(bookId);
//	}
//	
//	public List<BookBean> findAll(){
//		return new ArrayList<>(bookMap.values());
//	}
//	
//	public BookBean findOne(Integer id){
//		return bookMap.get(id);
//	}
}
