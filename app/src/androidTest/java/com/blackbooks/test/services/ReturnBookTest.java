package com.blackbooks.test.services;

import java.util.Date;

import junit.framework.Assert;

import com.blackbooks.model.nonpersistent.BookInfo;
import com.blackbooks.services.BookServices;
import com.blackbooks.test.data.Authors;
import com.blackbooks.test.data.Books;

/**
 * Test class of {@link BookServices#returnBook}.
 */
public class ReturnBookTest extends AbstractDatabaseTest {
	
	/**
	 * Test of returnBook.
	 */
	public void returnBookTest() {
		BookInfo bookInfo = new BookInfo();
		bookInfo.title = Books.THE_VOYNICH_MANUSCRIPT;
		bookInfo.loanedTo = Authors.RENE_GOSCINNY;
		bookInfo.loanDate = new Date();
		
		BookServices.saveBookInfo(getDb(), bookInfo);
		
		BookServices.returnBook(getDb(), bookInfo.id);
		
		BookInfo bookInfoDb = BookServices.getBookInfo(getDb(), bookInfo.id);
		
		Assert.assertNull(bookInfoDb.loanedTo);
		Assert.assertNull(bookInfoDb.loanDate);
	}
}