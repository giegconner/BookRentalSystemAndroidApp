package edu.csumb.cgieg.mainmenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CsumbLibraryDB {
    private static final String TAG = "CsumbLibraryDB";

    public static String getDateHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = new Date();
        String currDateTime = sdf.format(date);
        return currDateTime;
    }

    // database constants
    public static final String DB_NAME = "csumblibrary.db";
    public static final int DB_VERSION = 1;

    // customer table constants
    public static final String CUSTOMER_TABLE = "Customer";

    public static final String CUSTOMER_ID = "id";
    public static final int CUSTOMER_ID_COL = 0;

    public static final String CUSTOMER_TRANS_TYPE = "transType";
    public static final int CUSTOMER_TRANS_TYPE_COL = 1;

    public static final String CUSTOMER_NAME = "username";
    public static final int CUSTOMER_NAME_COL = 2;

    public static final String CUSTOMER_PWD = "password";
    public static final int CUSTOMER_PWD_COL = 3;

    public static final String CUSTOMER_DATE_HOUR = "dateHour";
    public static final int CUSTOMER_DATE_HOUR_COL = 4;

    // librarian table constants
    public static final String LIBRARIAN_TABLE = "Librarian";

    public static final String LIBRARIAN_ID = "id";
    public static final int LIBRARIAN_ID_COL = 0;

    public static final String LIBRARIAN_TRANS_TYPE = "transType";
    public static final int LIBRARIAN_TRANS_TYPE_COL = 1;

    public static final String LIBRARIAN_NAME = "username";
    public static final int LIBRARIAN_NAME_COL = 2;

    public static final String LIBRARIAN_PWD = "password";
    public static final int LIBRARIAN_PWD_COL = 3;

    public static final String LIBRARIAN_DATE_HOUR = "dateHour";
    public static final int LIBRARIAN_DATE_HOUR_COL = 4;

    // book table constants
    public static final String BOOK_TABLE = "Book";

    public static final String BOOK_ID = "id";
    public static final int BOOK_ID_COL = 0;

    public static final String BOOK_TITLE = "title";
    public static final int BOOK_TITLE_COL = 1;

    public static final String BOOK_AUTHOR = "author";
    public static final int BOOK_AUTHOR_COL = 2;

    public static final String BOOK_FEE_PER_HOUR = "feePerHour";
    public static final int BOOK_FEE_PER_HOUR_COL = 3;

    // book hold table constants
    public static final String BOOK_HOLD_TABLE = "BookHold";

    public static final String BOOK_HOLD_TRANS_TYPE = "transType";
    public static int BOOK_HOLD_TRANS_TYPE_COL = 0;

    public static final String BOOK_HOLD_USERNAME = "username";
    public static int BOOK_HOLD_USERNAME_COL = 1;

    public static final String BOOK_HOLD_PICKUP_DATE_HOUR = "pickupDateHour";
    public static int BOOK_HOLD_PICKUP_DATE_HOUR_COL = 2;

    public static final String BOOK_HOLD_RETURN_DATE_HOUR = "returnDateHour";
    public static int BOOK_HOLD_RETURN_DATE_HOUR_COL = 3;

    public static final String BOOK_HOLD_TITLE = "bookTitle";
    public static int BOOK_HOLD_TITLE_COL = 4;

    public static final String BOOK_HOLD_RESERVE_NUM = "reserveNum";
    public static int BOOK_HOLD_RESERVE_NUM_COL = 5;

    public static final String BOOK_HOLD_TOTAL_AMOUNT = "totalAmount";
    public static int BOOK_HOLD_TOTAL_AMOUNT_COL = 6;

    public static final String BOOK_HOLD_DATE_HOUR = "dateHour";
    public static int BOOK_HOLD_DATE_HOUR_COL = 7;

    // CREATE and DROP TABLE statements

    public static final String CREATE_CUSTOMER_TABLE =
            "CREATE TABLE " + CUSTOMER_TABLE + " (" +
                    CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CUSTOMER_TRANS_TYPE + " TEXT NOT NULL, " +
                    CUSTOMER_NAME + " TEXT NOT NULL, " +
                    CUSTOMER_PWD + " TEXT NOT NULL, " +
                    CUSTOMER_DATE_HOUR + " TEXT);";
    public static final String CREATE_LIBRARIAN_TABLE =
            "CREATE TABLE " + LIBRARIAN_TABLE + " (" +
                    LIBRARIAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LIBRARIAN_TRANS_TYPE + " TEXT NOT NULL, " +
                    LIBRARIAN_NAME + " TEXT NOT NULL, " +
                    LIBRARIAN_PWD + " TEXT NOT NULL, " +
                    LIBRARIAN_DATE_HOUR + " TEXT);";
    public static final String CREATE_BOOK_TABLE =
            "CREATE TABLE " + BOOK_TABLE + " (" +
                    BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BOOK_TITLE + " TEXT NOT NULL, " +
                    BOOK_AUTHOR + " TEXT NOT NULL, " +
                    BOOK_FEE_PER_HOUR + " REAL);";
    public static final String CREATE_BOOK_HOLD_TABLE =
            "CREATE TABLE " + BOOK_HOLD_TABLE + " (" +
                    BOOK_HOLD_TRANS_TYPE + " TEXT NOT NULL, " +
                    BOOK_HOLD_USERNAME + " TEXT NOT NULL, " +
                    BOOK_HOLD_PICKUP_DATE_HOUR + " TEXT NOT NULL, " +
                    BOOK_HOLD_RETURN_DATE_HOUR + " TEXT NOT NULL, " +
                    BOOK_HOLD_TITLE + " TEXT NOT NULL, " +
                    BOOK_HOLD_RESERVE_NUM + " INTEGER PRIMARY KEY NOT NULL, " +
                    BOOK_HOLD_TOTAL_AMOUNT + " REAL NOT NULL, " +
                    BOOK_HOLD_DATE_HOUR + " TEXT NOT NULL);";

    public static final String DROP_CUSTOMER_TABLE =
            "DROP TABLE IF EXISTS " + CUSTOMER_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL(CREATE_LIBRARIAN_TABLE);
            db.execSQL(CREATE_CUSTOMER_TABLE);
            db.execSQL(CREATE_BOOK_TABLE);
            db.execSQL(CREATE_BOOK_HOLD_TABLE);
            // insert into Librarian table
            ContentValues cv = new ContentValues();
            cv.put(LIBRARIAN_TRANS_TYPE, "");
            cv.put(LIBRARIAN_NAME, "Admin2");
            cv.put(LIBRARIAN_PWD, "Admin2");
            cv.put(LIBRARIAN_DATE_HOUR, getDateHour());
            db.insert(LIBRARIAN_TABLE, null, cv);
            // insert into Customer table
            cv = new ContentValues();
            cv.put(CUSTOMER_TRANS_TYPE, "");
            cv.put(CUSTOMER_NAME, "alice5");
            cv.put(CUSTOMER_PWD, "csumb100");
            cv.put(CUSTOMER_DATE_HOUR, getDateHour());
            db.insert(CUSTOMER_TABLE, null, cv);
            cv.put(CUSTOMER_TRANS_TYPE, "");
            cv.put(CUSTOMER_NAME, "Brian7");
            cv.put(CUSTOMER_PWD, "123abc");
            cv.put(CUSTOMER_DATE_HOUR, getDateHour());
            db.insert(CUSTOMER_TABLE, null, cv);
            cv.put(CUSTOMER_TRANS_TYPE, "");
            cv.put(CUSTOMER_NAME, "chris12");
            cv.put(CUSTOMER_PWD, "CHRIS12");
            cv.put(CUSTOMER_DATE_HOUR, getDateHour());
            db.insert(CUSTOMER_TABLE, null, cv);
            // insert into Book table
            cv = new ContentValues();
            cv.put(BOOK_TITLE, "Hot Java");
            cv.put(BOOK_AUTHOR, "S. Narayanan");
            cv.put(BOOK_FEE_PER_HOUR, 0.05);
            db.insert(BOOK_TABLE, null, cv);
            cv.put(BOOK_TITLE, "Fun Java");
            cv.put(BOOK_AUTHOR, "Y. Byun");
            cv.put(BOOK_FEE_PER_HOUR, 1.00);
            db.insert(BOOK_TABLE, null, cv);
            cv.put(BOOK_TITLE, "Algorithm for Java");
            cv.put(BOOK_AUTHOR, "K. Alice");
            cv.put(BOOK_FEE_PER_HOUR, 0.25);
            db.insert(BOOK_TABLE, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("CUSTOMER_ list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(CsumbLibraryDB.DROP_CUSTOMER_TABLE);
            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public CsumbLibraryDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public ArrayList<Customer> getCustomers(String username) {
        String where = CUSTOMER_NAME + " = ?";
        String[] whereArgs = { username };
        if (username.equals("")) {
            where = null;
            whereArgs = null;
        }

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(CUSTOMER_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<Customer> customers = new ArrayList<Customer>();
        while (cursor.moveToNext()) {
            customers.add(getCustomerFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();

        // close db
        if (db != null)
            db.close();

        return customers;
    }

    public ArrayList<Customer> checkCustomers(String username, String password) {
        String where = CUSTOMER_NAME + " = ? AND " + CUSTOMER_PWD + " = ?";
        String[] whereArgs = { username, password };

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(CUSTOMER_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<Customer> customers = new ArrayList<Customer>();
        while (cursor.moveToNext()) {
            customers.add(getCustomerFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();

        // close db
        if (db != null)
            db.close();

        return customers;
    }

    public ArrayList<Librarian> getLibrarians(String username) {
        String where = LIBRARIAN_NAME + " = ?";
        String[] whereArgs = { username };
        if (username.equals("")) {
            where = null;
            whereArgs = null;
        }

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(LIBRARIAN_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<Librarian> librarians = new ArrayList<Librarian>();
        while (cursor.moveToNext()) {
            librarians.add(getLibrarianFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();

        // close db
        if (db != null)
            db.close();

        return librarians;
    }

    public ArrayList<Librarian> checkLibrarians(String username, String password) {
        String where = LIBRARIAN_NAME + " = ? AND " + LIBRARIAN_NAME + " = ?";
        String[] whereArgs = { username, password };

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(LIBRARIAN_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<Librarian> librarians = new ArrayList<Librarian>();
        while (cursor.moveToNext()) {
            librarians.add(getLibrarianFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();

        // close db
        if (db != null)
            db.close();

        return librarians;
    }

    public ArrayList<Book> getBooks(String bookName) {
        String where = BOOK_TITLE + " = ?";
        String[] whereArgs = { bookName };
        if (bookName.equals("")) {
            where = null;
            whereArgs = null;
        }

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(BOOK_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<Book> books = new ArrayList<Book>();
        while (cursor.moveToNext()) {
            books.add(getBookFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();

        // close db
        if (db != null)
            db.close();

        return books;
    }

    public ArrayList<BookHold> getBookHolds(String aUsername) {
        String where = "";
        db = dbHelper.getReadableDatabase();

        ArrayList<BookHold> bookHolds = new ArrayList<BookHold>();
        Cursor cursor;
        if (aUsername.equals("")) { // get all cancel holds and place holds
            where = null;
            String[] whereArgs = null;
            cursor = db.query(BOOK_HOLD_TABLE, null,
                    where, whereArgs,
                    null, null, null);
        }
        else { // get all place holds from username
            where = BOOK_HOLD_USERNAME + " = ? AND " + BOOK_HOLD_TRANS_TYPE + " = ?";
            String[] whereArgs = { aUsername, "Place hold" };
            cursor = db.query(BOOK_HOLD_TABLE, null,
                    where, whereArgs,
                    null, null, null);
        }
        while (cursor.moveToNext()) {
            bookHolds.add(getBookHoldFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();

        // close db
        if (db != null)
            db.close();

        return bookHolds;
    }

    public ArrayList<BookHold> getBookHoldsFromDateHour(String aPickupDateHour, String aReturnDateHour) {
        Log.d(TAG, "pickup date/hour: " + aPickupDateHour);
        Log.d(TAG, "return date/hour: " + aReturnDateHour);
        String where = "((? BETWEEN " + BOOK_HOLD_PICKUP_DATE_HOUR + " AND " + BOOK_HOLD_RETURN_DATE_HOUR + ")" +
                " OR (? BETWEEN " + BOOK_HOLD_PICKUP_DATE_HOUR + " AND " + BOOK_HOLD_RETURN_DATE_HOUR + ")" +
                " OR (" + BOOK_HOLD_PICKUP_DATE_HOUR + " BETWEEN ? AND ?)" +
                " OR (" + BOOK_HOLD_RETURN_DATE_HOUR + " BETWEEN ? AND ?))" +
                " AND (" + BOOK_HOLD_TRANS_TYPE + " = ?);"; // currently works keep this!!

        Log.d(TAG, where);
        String[] whereArgs = { aPickupDateHour, aReturnDateHour, aPickupDateHour, aReturnDateHour,
                aPickupDateHour, aReturnDateHour, "Place hold" };

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(BOOK_HOLD_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<BookHold> bookHolds = new ArrayList<BookHold>();
        while (cursor.moveToNext()) {
            bookHolds.add(getBookHoldFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();

        // close db
        if (db != null)
            db.close();

        return bookHolds;
    }

    private static Customer getCustomerFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Customer customer = new Customer(
                        cursor.getString(CUSTOMER_TRANS_TYPE_COL),
                        cursor.getString(CUSTOMER_NAME_COL),
                        cursor.getString(CUSTOMER_PWD_COL),
                        cursor.getString(CUSTOMER_DATE_HOUR_COL));
                return customer;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    private static Librarian getLibrarianFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Librarian librarian = new Librarian(
                        cursor.getString(LIBRARIAN_TRANS_TYPE_COL),
                        cursor.getString(LIBRARIAN_NAME_COL),
                        cursor.getString(LIBRARIAN_PWD_COL),
                        cursor.getString(LIBRARIAN_DATE_HOUR_COL));
                return librarian;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    private static Book getBookFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Book book = new Book(
                        cursor.getString(BOOK_TITLE_COL),
                        cursor.getString(BOOK_AUTHOR_COL),
                        cursor.getDouble(BOOK_FEE_PER_HOUR_COL));
                return book;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    private static BookHold getBookHoldFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                BookHold bookHold = new BookHold(
                        cursor.getString(BOOK_HOLD_TRANS_TYPE_COL),
                        cursor.getString(BOOK_HOLD_USERNAME_COL),
                        cursor.getString(BOOK_HOLD_PICKUP_DATE_HOUR_COL),
                        cursor.getString(BOOK_HOLD_RETURN_DATE_HOUR_COL),
                        cursor.getString(BOOK_HOLD_TITLE_COL),
                        cursor.getInt(BOOK_HOLD_RESERVE_NUM_COL),
                        cursor.getDouble(BOOK_HOLD_TOTAL_AMOUNT_COL),
                        cursor.getString(BOOK_HOLD_DATE_HOUR_COL));
                return bookHold;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public long insertCustomer(Customer customer) {
        ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_TRANS_TYPE, customer.getTransType());
        cv.put(CUSTOMER_NAME, customer.getUsername());
        cv.put(CUSTOMER_PWD, customer.getPassword());
        cv.put(CUSTOMER_DATE_HOUR, customer.getDateHour());

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(CUSTOMER_TABLE, null, cv);

        // close db
        if (db != null)
            db.close();

        return rowID;
    }

    public long insertBook(Book book) {
        ContentValues cv = new ContentValues();
        cv.put(BOOK_TITLE, book.getTitle());
        cv.put(BOOK_AUTHOR, book.getAuthor());
        cv.put(BOOK_FEE_PER_HOUR, book.getFeePerHour());

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(BOOK_TABLE, null, cv);

        // close db
        if (db != null)
            db.close();

        return rowID;
    }

    public long insertBookHold(BookHold bookHold) {
        ContentValues cv = new ContentValues();
        cv.put(BOOK_HOLD_TRANS_TYPE, bookHold.getTransType());
        cv.put(BOOK_HOLD_USERNAME, bookHold.getUsername());
        cv.put(BOOK_HOLD_PICKUP_DATE_HOUR, bookHold.getPickupDateHour());
        cv.put(BOOK_HOLD_RETURN_DATE_HOUR, bookHold.getReturnDateHour());
        cv.put(BOOK_HOLD_TITLE, bookHold.getBookTitle());
        cv.put(BOOK_HOLD_RESERVE_NUM, bookHold.getReserveNum());
        cv.put(BOOK_HOLD_TOTAL_AMOUNT, bookHold.getTotalAmount());
        cv.put(BOOK_HOLD_DATE_HOUR, bookHold.getDateHour());

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(BOOK_HOLD_TABLE, null, cv);

        // close db
        if (db != null)
            db.close();

        return rowID;
    }

    public int updateBookHold(BookHold bookHold) {
        ContentValues cv = new ContentValues();
        cv.put(BOOK_HOLD_TRANS_TYPE, "Cancel hold");
        cv.put(BOOK_HOLD_TOTAL_AMOUNT, 0.0);
        cv.put(BOOK_HOLD_DATE_HOUR, getDateHour());

        String where = BOOK_HOLD_RESERVE_NUM + " = ?";
        String[] whereArgs = { String.valueOf(bookHold.getReserveNum()) };

        db = dbHelper.getWritableDatabase();
        int rowCount = db.update(BOOK_HOLD_TABLE, cv, where, whereArgs);
        if (db != null)
            db.close();

        return rowCount;
    }
}
