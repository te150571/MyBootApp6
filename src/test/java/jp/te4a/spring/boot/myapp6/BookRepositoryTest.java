package jp.te4a.spring.boot.myapp6;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class BookRepositoryTest {

	// 初期データ無しでのテスト(またはデータ数に依存しないテスト)
	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public static class NoDataTest {

		@Autowired
		BookRepository bookRepository;

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
		private BookRepository bookRepository;

		@Before
		public void init() {
			bookRepository.save(new BookBean(1, "Test Title", "Test Author", "Test Publisher", 100));
		}

		// findAllメソッドの返すデータ(1件)
		@Test
		public void RepositoryfindAllReturn()throws Exception {
			assertThat(1, is(bookRepository.findAll().size()));
		}

		// findAllメソッドの返すデータが事前登録データと一致するか
		@Test
		public void RepositoryfindAllReturnData()throws Exception {
			assertThat(new BookBean(1, "Test Title", "Test Author", "Test Publisher", 100), is(bookRepository.findAll().get(0)));
		}

		// saveメソッドですでに登録してあるIDと同じIDをもつBookBeanを登録
		@Test
		public void RepositorySaveSomeId()throws Exception {
			BookBean bookBean = new BookBean(1, "Test Title 1", "Test Author 1", "Test Publisher 1", 1000);
			bookRepository.save(bookBean);
			assertThat(bookRepository.findAll().get(0), is(bookBean));
		}

		// saveメソッドですでに登録してあるIDと異なるIDをもつBookBeanを登録
		@Test
		public void RepositorySaveDifferentId()throws Exception {
			BookBean bookBean = new BookBean(2, "Test Title 2", "Test Author 2", "Test Publisher 2", 1000);
			bookRepository.save(bookBean);
			assertThat(bookRepository.findAll().get(1), is(bookBean));
		}

		// saveメソッドですでに登録してあるIDと同じIDをもつBookBeanを登録したとき、登録数は1件
		@Test
		public void RepositorySaveSomeIdResultNum()throws Exception {
			BookBean bookBean = new BookBean(1, "Test Title 1", "Test Author 1", "Test Publisher 1", 1000);
			bookRepository.save(bookBean);
			assertThat(1, is(bookRepository.findAll().size()));
		}

		// saveメソッドですでに登録してあるIDと異なるをもつBookBeanを登録したとき、登録数は2件
		@Test
		public void RepositorySaveDifferentIdResultNum()throws Exception {
			BookBean bookBean = new BookBean(2, "Test Title 2", "Test Author 2", "Test Publisher 2", 1000);
			bookRepository.save(bookBean);
			assertThat(2, is(bookRepository.findAll().size()));
		}

		// deleteメソッドで1件だけ登録してあるBookBeanを削除
		@Test
		public void RepositoryDeleteOne()throws Exception {
			bookRepository.delete(1);
			assertThat(0, is(bookRepository.findAll().size()));
		}

		// deleteメソッドで存在しないIDのBookBeanを削除
		@Test
		public void RepositoryDeleteNone()throws Exception {
			bookRepository.delete(2);
			assertThat(1, is(bookRepository.findAll().size()));
		}

		// findAllメソッドが返すリストのサイズ
		@Test
		public void RepositoryFindAllSize()throws Exception {
			assertThat(1, is(bookRepository.findAll().size()));
		}
	}

	// 初期データが複数(3件)でのテスト
	@RunWith(SpringRunner.class)
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public static class TwoDataTest {

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
			beanList.forEach(bean -> bookRepository.save(bean));
		}

		// deleteメソッドで複数登録してあるBookBeanを削除
		@Test
		public void RepositoryDeleteTwo()throws Exception {
			bookRepository.delete(1);
			bookRepository.delete(2);
			assertThat(1, is(bookRepository.findAll().size()));
		}

		// findAllメソッドが返すリストのサイズ
		@Test
		public void RepositoryFindAllSize()throws Exception {
			assertThat(3, is(bookRepository.findAll().size()));
		}

		// findAllメソッドが返すリストが事前登録されたBookBeanのリストと一致するか
		@Test
		public void RepositoryFindAllList()throws Exception {
			assertThat(beanList, is(bookRepository.findAll()));
		}
	}
}
