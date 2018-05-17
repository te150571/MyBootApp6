package jp.te4a.spring.boot.myapp11;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookForm {
	Integer id;
	@NotNull
	@Size(min = 3)
	String title;
	@Size(min = 3, max = 20)
	String author;
	String publisher;
	@Min(0)
	Integer price;
}
