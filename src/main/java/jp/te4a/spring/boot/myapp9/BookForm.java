package jp.te4a.spring.boot.myapp9;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookForm {
	Integer id;
	String title;
	String author;
	String publisher;
	Integer price;
}
