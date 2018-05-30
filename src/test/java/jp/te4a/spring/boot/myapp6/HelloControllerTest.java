package jp.te4a.spring.boot.myapp6;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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

//@RunWith(SpringRunner.class)
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
		
		@Autowired
		BookRepository bookRepository;
		
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
		
		// BookServiceのsaveメソッドでnullを登録
		@Test(expected = NullPointerException.class)
		public void ServiceSaveNull()throws Exception {
			bookService.save(null);
		}
		
		// BookServiceのsaveメソッドで各フィールドが0かnull
		@Test
		public void ServiceSaveBeanNull()throws Exception {
			BookBean bookBean = new BookBean(0, null, null, null, 0);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(0), is(bookBean));
		}
		
		// BookServiceのsaveメソッドで各フィールドが0か空文字
		@Test
		public void ServiceSaveBeanEmpty()throws Exception {
			BookBean bookBean = new BookBean(0, "", "", "", 0);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(0), is(bookBean));
		}
		
		// BookServiceのsaveメソッドでIDが負の値
		@Test
		public void ServiceSaveBeanNegId()throws Exception {
			BookBean bookBean = new BookBean(-1, "", "", "", 0);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(0), is(bookBean));
		}
		
		// BookRepositoryのfindAllメソッドの返すデータ(0件)
		@Test
		public void RepositoryfindAllReturn()throws Exception {
			assertThat(0, is(bookRepository.findAll().size()));
		}
		
		// BookRepositoryのsaveメソッドで初回登録時null
		@Test(expected = NullPointerException.class)
		public void RepositorySaveFirstNull()throws Exception {
			bookRepository.save(null);
		}
		
		// BookRepositoryのsaveメソッドで初回登録時各フィールドがnull(数値は0)
		@Test
		public void RepositorySaveBeanNull()throws Exception {
			BookBean bookBean = new BookBean(0, null, null, null, 0);
			bookRepository.save(bookBean);
			assertThat(bookRepository.findAll().get(0), is(bookBean));
		}
		
		// BookRepositoryのsaveメソッドで初回登録時各フィールドが空文字(数値は0)
		@Test
		public void RepositorySaveBeanEmpty()throws Exception {
			BookBean bookBean = new BookBean(0, "", "", "", 0);
			bookRepository.save(bookBean);
			assertThat(bookRepository.findAll().get(0), is(bookBean));
		}
		
		// BookRepositoryのsaveメソッドで初回登録時IDが負の値
		@Test
		public void RepositorySaveBeanNegId()throws Exception {
			BookBean bookBean = new BookBean(-1, "", "", "", 0);
			bookRepository.save(bookBean);
			assertThat(bookRepository.findAll().get(0), is(bookBean));
		}
		
		// BookRepositoryのfindAllメソッドが返すリストのサイズ
		@Test
		public void RepositoryFindAllSize()throws Exception {
			assertThat(0, is(bookRepository.findAll().size()));
		}
	}
	
	// 初期データ1件でのテスト
	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public static class OneDataTest {
		
		@Autowired
		private BookService bookService;
		
		@Autowired
		private BookRepository bookRepository;
		
		@Before
		public void init() {
			bookService.save(new BookBean(1, "Test Title", "Test Author", "Test Publisher", 100));
		}
		
		//BookServiceのsaveメソッドですでに登録してあるIDと同じIDをもつBookBeanを登録
		@Test
		public void ServiceSaveSomeId()throws Exception {
			BookBean bookBean = new BookBean(1, "Test Title 1", "Test Author 1", "Test Publisher 1", 1000);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(0), is(bookBean));
		}
		
		//BookServiceのsaveメソッドですでに登録してあるIDと異なるIDをもつBookBeanを登録
		@Test
		public void ServiceSaveDifferentId()throws Exception {
			BookBean bookBean = new BookBean(2, "Test Title 2", "Test Author 2", "Test Publisher 2", 1000);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(1), is(bookBean));
		}
		
		//BookServiceのsaveメソッドですでに登録してあるIDと同じIDをもつBookBeanを登録したとき、登録数は1件
		@Test
		public void ServiceSaveSomeIdResultNum()throws Exception {
			BookBean bookBean = new BookBean(1, "Test Title 1", "Test Author 1", "Test Publisher 1", 1000);
			bookService.save(bookBean);
			assertThat(1, is(bookService.findAll().size()));
		}
		
		//BookServiceのsaveメソッドですでに登録してあるIDと異なるをもつBookBeanを登録したとき、登録数は2件
		@Test
		public void ServiceSaveDifferentIdResultNum()throws Exception {
			BookBean bookBean = new BookBean(2, "Test Title 2", "Test Author 2", "Test Publisher 2", 1000);
			bookService.save(bookBean);
			assertThat(2, is(bookService.findAll().size()));
		}
		
		// BookRepositoryのfindAllメソッドの返すデータ(1件)
		@Test
		public void RepositoryfindAllReturn()throws Exception {
			assertThat(1, is(bookRepository.findAll().size()));
		}
		
		// BookRepositoryのfindAllメソッドの返すデータが事前登録データと一致するか
		@Test
		public void RepositoryfindAllReturnData()throws Exception {
			assertThat(new BookBean(1, "Test Title", "Test Author", "Test Publisher", 100), is(bookRepository.findAll().get(0)));
		}
		
		//BookRepositoryのsaveメソッドですでに登録してあるIDと同じIDをもつBookBeanを登録
		@Test
		public void RepositorySaveSomeId()throws Exception {
			BookBean bookBean = new BookBean(1, "Test Title 1", "Test Author 1", "Test Publisher 1", 1000);
			bookRepository.save(bookBean);
			assertThat(bookRepository.findAll().get(0), is(bookBean));
		}

		//BookRepositoryのsaveメソッドですでに登録してあるIDと異なるIDをもつBookBeanを登録
		@Test
		public void RepositorySaveDifferentId()throws Exception {
			BookBean bookBean = new BookBean(2, "Test Title 2", "Test Author 2", "Test Publisher 2", 1000);
			bookRepository.save(bookBean);
			assertThat(bookRepository.findAll().get(1), is(bookBean));
		}

		//BookRepositoryのsaveメソッドですでに登録してあるIDと同じIDをもつBookBeanを登録したとき、登録数は1件
		@Test
		public void RepositorySaveSomeIdResultNum()throws Exception {
			BookBean bookBean = new BookBean(1, "Test Title 1", "Test Author 1", "Test Publisher 1", 1000);
			bookRepository.save(bookBean);
			assertThat(1, is(bookRepository.findAll().size()));
		}

		//BookRepositoryのsaveメソッドですでに登録してあるIDと異なるをもつBookBeanを登録したとき、登録数は2件
		@Test
		public void RepositorySaveDifferentIdResultNum()throws Exception {
			BookBean bookBean = new BookBean(2, "Test Title 2", "Test Author 2", "Test Publisher 2", 1000);
			bookRepository.save(bookBean);
			assertThat(2, is(bookRepository.findAll().size()));
		}
		
		// BookRepositoryのdeleteメソッドで1件だけ登録してあるBookBeanを削除
		@Test
		public void RepositoryDeleteOne()throws Exception {
			bookRepository.delete(1);
			assertThat(0, is(bookRepository.findAll().size()));
		}
		
		// BookRepositoryのdeleteメソッドで存在しないIDのBookBeanを削除
		@Test
		public void RepositoryDeleteNone()throws Exception {
			bookRepository.delete(2);
			assertThat(1, is(bookRepository.findAll().size()));
		}
		
		// BookRepositoryのfindAllメソッドが返すリストのサイズ
		@Test
		public void RepositoryFindAllSize()throws Exception {
			assertThat(1, is(bookRepository.findAll().size()));
		}
	}
	
	// 初期データ3件でのテスト
	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public static class TwoDataTest {

		@Autowired
		private BookService bookService;

		@Autowired
		private BookRepository bookRepository;
		
		private List<BookBean> beanList = new ArrayList<>(
				Arrays.asList(
						new BookBean(1, "Test Title1", "Test Author1", "Test Publisher1", 100),
						new BookBean(2, "Test Title2", "Test Author2", "Test Publisher2", 200),
						new BookBean(3, "Test Title3", "Test Author3", "Test Publisher3", 300)
				));

		@Before
		public void init() {
			beanList.forEach(bean -> bookService.save(bean));
		}
		
		// BookRepositoryのdeleteメソッドで複数登録してあるBookBeanを削除
		@Test
		public void RepositoryDeleteTwo()throws Exception {
			bookRepository.delete(1);
			bookRepository.delete(2);
			assertThat(1, is(bookRepository.findAll().size()));
		}
		
		// BookRepositoryのfindAllメソッドが返すリストのサイズ
		@Test
		public void RepositoryFindAllSize()throws Exception {
			assertThat(3, is(bookRepository.findAll().size()));
		}
		
		// BookRepositoryのfindAllメソッドが返すリストが事前登録されたBookBeanのリストと一致するか
		@Test
		public void RepositoryFindAllList()throws Exception {
			assertThat(beanList, is(bookRepository.findAll()));
		}
	}
}
