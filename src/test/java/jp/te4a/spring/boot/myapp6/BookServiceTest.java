package jp.te4a.spring.boot.myapp6;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(Enclosed.class)
public class BookServiceTest {

	// 初期データ無しでのテスト(またはデータ数に依存しないテスト)
	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public static class NoDataTest {

		@Autowired
		BookService bookService;

		// saveメソッドでnullを登録
		@Test(expected = NullPointerException.class)
		public void ServiceSaveNull()throws Exception {
			bookService.save(null);
		}

		// saveメソッドで各フィールドが0かnull
		@Test
		public void ServiceSaveBeanNull()throws Exception {
			BookBean bookBean = new BookBean(0, null, null, null, 0);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(0), is(bookBean));
		}

		// saveメソッドで各フィールドが0か空文字
		@Test
		public void ServiceSaveBeanEmpty()throws Exception {
			BookBean bookBean = new BookBean(0, "", "", "", 0);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(0), is(bookBean));
		}

		// saveメソッドでIDが負の値
		@Test
		public void ServiceSaveBeanNegId()throws Exception {
			BookBean bookBean = new BookBean(-1, "", "", "", 0);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(0), is(bookBean));
		}
	}

	// 初期データ1件でのテスト
	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public static class OneDataTest {

		@Autowired
		private BookService bookService;

		@Before
		public void init() {
			bookService.save(new BookBean(1, "Test Title", "Test Author", "Test Publisher", 100));
		}

		// saveメソッドですでに登録してあるIDと同じIDをもつBookBeanを登録
		@Test
		public void ServiceSaveSomeId()throws Exception {
			BookBean bookBean = new BookBean(1, "Test Title 1", "Test Author 1", "Test Publisher 1", 1000);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(0), is(bookBean));
		}

		// saveメソッドですでに登録してあるIDと異なるIDをもつBookBeanを登録
		@Test
		public void ServiceSaveDifferentId()throws Exception {
			BookBean bookBean = new BookBean(2, "Test Title 2", "Test Author 2", "Test Publisher 2", 1000);
			bookService.save(bookBean);
			assertThat(bookService.findAll().get(1), is(bookBean));
		}

		// saveメソッドですでに登録してあるIDと同じIDをもつBookBeanを登録したとき、登録数は1件
		@Test
		public void ServiceSaveSomeIdResultNum()throws Exception {
			BookBean bookBean = new BookBean(1, "Test Title 1", "Test Author 1", "Test Publisher 1", 1000);
			bookService.save(bookBean);
			assertThat(1, is(bookService.findAll().size()));
		}

		// saveメソッドですでに登録してあるIDと異なるをもつBookBeanを登録したとき、登録数は2件
		@Test
		public void ServiceSaveDifferentIdResultNum()throws Exception {
			BookBean bookBean = new BookBean(2, "Test Title 2", "Test Author 2", "Test Publisher 2", 1000);
			bookService.save(bookBean);
			assertThat(2, is(bookService.findAll().size()));
		}
	}
}
