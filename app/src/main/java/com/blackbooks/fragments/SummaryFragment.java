package com.blackbooks.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackbooks.R;
import com.blackbooks.activities.BookGroupListActivity;
import com.blackbooks.activities.BookListActivity2;
import com.blackbooks.database.SQLiteHelper;
import com.blackbooks.model.nonpersistent.BookGroup;
import com.blackbooks.model.nonpersistent.Summary;
import com.blackbooks.services.SummaryServices;

/**
 * A fragment displaying statistics of the library.
 */
public class SummaryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = getView();
        LinearLayout mLayoutBooks = (LinearLayout) view.findViewById(R.id.summary_layoutBooks);
        LinearLayout mLayoutAuthors = (LinearLayout) view.findViewById(R.id.summary_layoutAuthors);
        LinearLayout mLayoutCategories = (LinearLayout) view.findViewById(R.id.summary_layoutCategories);
        LinearLayout mLayoutLanguages = (LinearLayout) view.findViewById(R.id.summary_layoutLanguages);
        LinearLayout mLayoutSeries = (LinearLayout) view.findViewById(R.id.summary_layoutSeries);
        LinearLayout mLayoutLocations = (LinearLayout) view.findViewById(R.id.summary_layoutLocations);
        LinearLayout mLayoutToRead = (LinearLayout) view.findViewById(R.id.summary_layoutToRead);
        LinearLayout mLayoutLoaned = (LinearLayout) view.findViewById(R.id.summary_layoutLoaned);
        LinearLayout mLayoutFavourite = (LinearLayout) view.findViewById(R.id.summary_layoutFavourite);

        mLayoutBooks.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBookGroupListActivity(BookGroup.BookGroupType.FIRST_LETTER);
            }
        });
        mLayoutAuthors.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBookGroupListActivity(BookGroup.BookGroupType.AUTHOR);
            }
        });
        mLayoutCategories.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBookGroupListActivity(BookGroup.BookGroupType.CATEGORY);
            }
        });
        mLayoutLanguages.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBookGroupListActivity(BookGroup.BookGroupType.LANGUAGE);
            }
        });
        mLayoutSeries.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBookGroupListActivity(BookGroup.BookGroupType.SERIES);
            }
        });
        mLayoutLocations.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBookGroupListActivity(BookGroup.BookGroupType.BOOK_LOCATION);
            }
        });
        mLayoutToRead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BookListActivity2.class);
                i.putExtra(BookListActivity2.EXTRA_BOOK_GROUP_TYPE, BookGroup.BookGroupType.TO_READ);
                startActivity(i);
            }
        });
        mLayoutLoaned.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBookGroupListActivity(BookGroup.BookGroupType.LOANED);
            }
        });
        mLayoutFavourite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BookListActivity2.class);
                i.putExtra(BookListActivity2.EXTRA_BOOK_GROUP_TYPE, BookGroup.BookGroupType.FAVOURITE);
                startActivity(i);
            }
        });

        TextView mTextBooksCount = (TextView) view.findViewById(R.id.summary_booksCount);
        TextView mTextAuthorsCount = (TextView) view.findViewById(R.id.summary_authorsCount);
        TextView mTextCategoriesCount = (TextView) view.findViewById(R.id.summary_categoriesCount);
        TextView mTextLanguagesCount = (TextView) view.findViewById(R.id.summary_languagesCount);
        TextView mTextSeriesCount = (TextView) view.findViewById(R.id.summary_seriesCount);
        TextView mTextLocationsCount = (TextView) view.findViewById(R.id.summary_locationsCount);
        TextView mTextToReadCount = (TextView) view.findViewById(R.id.summary_toReadCount);
        TextView mTextLoanedCount = (TextView) view.findViewById(R.id.summary_loanedCount);
        TextView mTextFavouriteCount = (TextView) view.findViewById(R.id.summary_favouriteCount);

        TextView mTextLabelBookCount = (TextView) view.findViewById(R.id.summary_textLabelBookCount);
        TextView mTextLabelAuthorCount = (TextView) view.findViewById(R.id.summary_textLabelAuthorCount);
        TextView mTextLabelCategoryCount = (TextView) view.findViewById(R.id.summary_textLabelCategoryCount);
        TextView mTextLabelLanguageCount = (TextView) view.findViewById(R.id.summary_textLabelLanguageCount);
        TextView mTextLabelSeriesCount = (TextView) view.findViewById(R.id.summary_textLabelSeriesCount);
        TextView mTextLabelLocationCount = (TextView) view.findViewById(R.id.summary_textLabelLocationCount);
        TextView mTextLabelToReadCount = (TextView) view.findViewById(R.id.summary_textLabelToReadCount);
        TextView mTextLabelLoanedCount = (TextView) view.findViewById(R.id.summary_textLabelLoanedCount);
        TextView mTextLabelFavouriteCount = (TextView) view.findViewById(R.id.summary_textLabelFavouriteCount);

        SQLiteDatabase db = SQLiteHelper.getInstance().getReadableDatabase();
        Summary summary = SummaryServices.getSummary(db);

        mTextBooksCount.setText(String.valueOf(summary.books));
        mTextAuthorsCount.setText(String.valueOf(summary.authors));
        mTextCategoriesCount.setText(String.valueOf(summary.categories));
        mTextLanguagesCount.setText(String.valueOf(summary.languages));
        mTextSeriesCount.setText(String.valueOf(summary.series));
        mTextLocationsCount.setText(String.valueOf(summary.bookLocations));
        mTextToReadCount.setText(String.valueOf(summary.toRead));
        mTextLoanedCount.setText(String.valueOf(summary.loaned));
        mTextFavouriteCount.setText(String.valueOf(summary.favourites));

        Resources res = getResources();

        mTextLabelBookCount.setText(res.getQuantityText(R.plurals.label_summary_books, summary.books));
        mTextLabelAuthorCount.setText(res.getQuantityText(R.plurals.label_summary_authors, summary.authors));
        mTextLabelCategoryCount.setText(res.getQuantityText(R.plurals.label_summary_categories, summary.categories));
        mTextLabelLanguageCount.setText(res.getQuantityText(R.plurals.label_summary_languages, summary.languages));
        mTextLabelSeriesCount.setText(res.getQuantityText(R.plurals.label_summary_series, summary.series));
        mTextLabelLocationCount.setText(res.getQuantityText(R.plurals.label_summary_locations, summary.bookLocations));
        mTextLabelToReadCount.setText(res.getQuantityText(R.plurals.label_summary_to_read, summary.toRead));
        mTextLabelLoanedCount.setText(res.getQuantityText(R.plurals.label_summary_loaned, summary.loaned));
        mTextLabelFavouriteCount.setText(res.getQuantityText(R.plurals.label_summary_favourites, summary.favourites));
    }

    /**
     * Start the {@link com.blackbooks.activities.BookGroupListActivity}.
     *
     * @param bookGroupType BookGroupType.
     */
    private void startBookGroupListActivity(BookGroup.BookGroupType bookGroupType) {
        Intent i = new Intent(getActivity(), BookGroupListActivity.class);
        i.putExtra(BookGroupListActivity.EXTRA_GROUP_BOOK_TYPE, bookGroupType);
        startActivity(i);
    }
}
