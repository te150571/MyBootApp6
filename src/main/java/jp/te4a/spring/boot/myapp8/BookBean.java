package jp.te4a.spring.boot.myapp8;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookBean {
	Integer id;
	String title;
	String author;
	String publisher;
	Integer price;
}
