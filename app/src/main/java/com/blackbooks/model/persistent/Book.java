package com.blackbooks.model.persistent;

import java.io.Serializable;
import java.util.Date;

import com.blackbooks.model.metadata.Column;
import com.blackbooks.model.metadata.Column.SQLiteDataType;
import com.blackbooks.model.metadata.Table;

@Table(name = Book.NAME, version = 1)
public class Book implements Serializable {

	public static final String NAME = "BOOK";

	private static final long serialVersionUID = 7409388866384981075L;

	@Column(name = Cols.BOO_ID, primaryKey = true, type = SQLiteDataType.INTEGER, version = 1)
	public Long id;

	@Column(name = Cols.BOO_TITLE, type = SQLiteDataType.TEXT, mandatory = true, version = 1)
	public String title;

	@Column(name = Cols.BOO_SUBTITLE, type = SQLiteDataType.TEXT, version = 1)
	public String subtitle;

	@Column(name = Cols.BOO_LANGUAGE_CODE, type = SQLiteDataType.TEXT, version = 1)
	public String languageCode;

	@Column(name = Cols.PUB_ID, type = SQLiteDataType.INTEGER, referencedType = Publisher.class, version = 1)
	public Long publisherId;

	@Column(name = Cols.BOO_PUBLISHED_DATE, type = SQLiteDataType.INTEGER, version = 1)
	public Date publishedDate;

	@Column(name = Cols.BOO_DESCRIPTION, type = SQLiteDataType.TEXT, version = 1)
	public String description;

	@Column(name = Cols.BOO_PAGE_COUNT, type = SQLiteDataType.INTEGER, version = 1)
	public Long pageCount;

	@Column(name = Cols.BOO_SMALL_THUMBNAIL, type = SQLiteDataType.BLOB, version = 1)
	public byte[] smallThumbnail;

	@Column(name = Cols.BOO_THUMBNAIL, type = SQLiteDataType.BLOB, version = 1)
	public byte[] thumbnail;

	@Column(name = Cols.BOO_IS_READ, type = SQLiteDataType.INTEGER, mandatory = true, version = 1)
	public Long isRead;

	@Column(name = Cols.BOO_IS_FAVOURITE, type = SQLiteDataType.INTEGER, mandatory = true, version = 1)
	public Long isFavourite;

	@Column(name = Cols.BOO_ISBN_10, type = SQLiteDataType.TEXT, version = 1)
	public String isbn10;

	@Column(name = Cols.BOO_ISBN_13, type = SQLiteDataType.TEXT, version = 1)
	public String isbn13;

	@Column(name = Cols.BOO_COMMENT, type = SQLiteDataType.TEXT, version = 1)
	public String comment;

	@Column(name = Cols.BKL_ID, type = SQLiteDataType.INTEGER, referencedType = BookLocation.class, version = 1)
	public Long bookLocationId;

	@Column(name = Cols.SER_ID, type = SQLiteDataType.INTEGER, referencedType = Series.class, version = 1)
	public Long seriesId;

	@Column(name = Cols.BOO_NUMBER, type = SQLiteDataType.INTEGER, version = 1)
	public Long number;

	@Column(name = Cols.BOO_LOANED_TO, type = SQLiteDataType.TEXT, version = 1)
	public String loanedTo;

	@Column(name = Cols.BOO_LOAN_DATE, type = SQLiteDataType.INTEGER, version = 1)
	public Date loanDate;

	public Book() {
		this.isRead = 0L;
		this.isFavourite = 0L;
	}

	public static final class Cols {
		public static final String BOO_ID = "BOO_ID";
		public static final String BOO_TITLE = "BOO_TITLE";
		public static final String BOO_SUBTITLE = "BOO_SUBTITLE";
		public static final String BOO_LANGUAGE_CODE = "BOO_LANGUAGE_CODE";
		public static final String PUB_ID = "PUB_ID";
		public static final String BOO_PUBLISHED_DATE = "BOO_PUBLISHED_DATE";
		public static final String BOO_DESCRIPTION = "BOO_DESCRIPTION";
		public static final String BOO_PAGE_COUNT = "BOO_PAGE_COUNT";
		public static final String BOO_SMALL_THUMBNAIL = "BOO_SMALL_THUMBNAIL";
		public static final String BOO_THUMBNAIL = "BOO_THUMBNAIL";
		public static final String BOO_IS_READ = "BOO_IS_READ";
		public static final String BOO_IS_FAVOURITE = "BOO_IS_FAVOURITE";
		public static final String BOO_ISBN_10 = "BOO_ISBN_10";
		public static final String BOO_ISBN_13 = "BOO_ISBN_13";
		public static final String BOO_COMMENT = "BOO_COMMENT";
		public static final String BKL_ID = "BKL_ID";
		public static final String SER_ID = "SER_ID";
		public static final String BOO_NUMBER = "BOO_NUMBER";
		public static final String BOO_LOANED_TO = "BOO_LOANED_TO";
		public static final String BOO_LOAN_DATE = "BOO_LOAN_DATE";
	}
}