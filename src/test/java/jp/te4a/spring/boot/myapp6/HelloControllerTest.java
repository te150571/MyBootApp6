package jp.te4a.spring.boot.myapp6;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(Enclosed.class)
public class HelloControllerTest {
	
	// 初期データ無しでのテスト(またはデータ数に依存しないテスト)
	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@AutoConfigureMockMvc
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public static class NoDataTest {
		@Autowired
		private MockMvc mockMvc;
		
		@Autowired
		BookService bookService;
		
		// indexメソッドのテスト
		@Test
		public void HelloTestIndex()throws Exception {
			mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists())
				.andExpect(model().attribute("msg", is("This is setting message.")))
				.andExpect(view().name("index"));
		}
		
		// 書籍情報をすべて正常に指定
		@Test
		public void PostForm()throws Exception {
			mockMvc.perform(post("/post").param("id", "0")
					.param("title", "Test Title")
					.param("author", "Test Author")
					.param("publisher", "Test Publisher")
					.param("price", "100"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("msg"))
				.andExpect(model().attribute("msg", is("<HR>ID:0<BR>Title:Test Title<BR>Author:Test Author<BR>Publisher:Test Publisher<BR>Price:100<BR><HR>")))
				.andExpect(view().name("index"));
			
			BookBean expectedBean = new BookBean(0, "Test Title", "Test Author", "Test Publisher", 100);
			assertThat(1, is(bookService.findAll().size()));
			assertThat(bookService.findAll().get(0), is(expectedBean));
		}
		
		// 書籍情報を空文字（数値は0）で指定
		@Test
		public void PostFormEmpty()throws Exception {
			mockMvc.perform(post("/post").param("id", "0")
					.param("title", "")
					.param("author", "")
					.param("publisher", "")
					.param("price", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("msg"))
				.andExpect(model().attribute("msg", is("<HR>ID:0<BR>Title:<BR>Author:<BR>Publisher:<BR>Price:0<BR><HR>")))
				.andExpect(view().name("index"));
			
			BookBean expectedBean = new BookBean(0, "", "", "", 0);
			assertThat(1, is(bookService.findAll().size()));
			assertThat(bookService.findAll().get(0), is(expectedBean));
		}
		
		// 書籍情報を一部指定しない
		@Test
		public void PostFormNullPart()throws Exception {
			mockMvc.perform(post("/post").param("id", "0")
					.param("author", "")
					.param("publisher", "")
					.param("price", "0"))
				.andExpect(status().isBadRequest());
		}
		
		// 書籍情報をすべて指定しない
		@Test
		public void PostFormNullAll()throws Exception {
			mockMvc.perform(post("/post"))
				.andExpect(status().isBadRequest());
		}
	}
}
