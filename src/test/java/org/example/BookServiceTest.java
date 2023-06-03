package org.example;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)


public class BookServiceTest {
    @Mock
    private List<Book> bookDatabase;

    @InjectMocks
    private BookService bookService;

    //setup
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void setClass() {
    }

    @Test
    public void testSearchKeywordBookTitleMatch() {
        Book book1 = new Book("Charlottes Web", "E.B. White", "Fiction", 21.00);
        Book book2 = new Book("The Giving Tree", "Shel Silverstein", "Fiction", 19.99);
        Book book3 = new Book("The Cat and the Hat", "Dr.Suess", "Fiction", 20.95);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);

        //set up mock book database
        when(bookDatabase.stream()).thenReturn(books.stream());

        //search
        List<Book> results = bookService.searchBook("Charlottes");

        //verify
        assertEquals(1, results.size());
        assertTrue(results.contains(book1));

    }

    @Test
    public void testSearchKeywordBookAuthorMatch() {
        Book book1 = new Book("Charlottes Web", "E.B. White", "Fiction", 21.00);
        Book book2 = new Book("The Giving Tree", "Shel Silverstein", "Fiction", 19.99);
        Book book3 = new Book("The Cat and the Hat", "Dr.Seuss", "Fiction", 20.95);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);

        //set up mock book database
        when(bookDatabase.stream()).thenReturn(books.stream());

        //search
        List<Book> results = bookService.searchBook("Dr.Seuss");

        assertEquals(1, results.size());
        assertTrue(results.contains(book3));
    }

    @Test
    public void testSearchKeywordBookGenreMatch() {
        Book book1 = new Book("Charlottes Web", "E.B. White", "Fiction", 21.00);
        Book book2 = new Book("The Giving Tree", "Shel Silverstein", "History", 19.99);
        Book book3 = new Book("The Cat and the Hat", "Dr.Seuss", "Fiction", 20.95);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);

        //set up mock book database
        when(bookDatabase.stream()).thenReturn(books.stream());

        //search
        List<Book> results = bookService.searchBook("History");

        assertEquals(1, results.size());
        assertTrue(results.contains(book2));
    }

    @Test
    public void testSearchBookNotInDatabase() {
        Book book1 = new Book("Charlottes Web", "E.B. White", "Fiction", 21.00);
        Book book2 = new Book("The Giving Tree", "Shel Silverstein", "History", 19.99);
        Book book3 = new Book("The Cat and the Hat", "Dr.Seuss", "Fiction", 20.95);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);

        //set up mock book database
        when(bookDatabase.stream()).thenReturn(books.stream());

        //search-no match
        List<Book> searchResult = bookService.searchBook("Harry Potter");

        assertTrue(searchResult.isEmpty());
    }


    @Test
    public void testPurchaseBookExistsInDatabase() {
        Book book3 = new Book("The Cat and the Hat", "Dr.Seuss", "Fiction", 20.95);
        User user = new User("darranda", " Password123", "d@email.com.com");

        //set up mock book database
        when(bookDatabase.contains(book3)).thenReturn(true);

        //purchase
        boolean result = bookService.purchaseBook(user, book3);

        assertTrue(result);
    }

    @Test
    public void testPurchaseBookDoesNotExistInDatabase() {
        Book book3 = new Book("The Cat and the Hat", "Dr.Seuss", "Fiction", 20.95);
        User user = new User("darranda", " Password123", "d@email.com.com");

        //set up mock book database
        when(bookDatabase.contains(book3)).thenReturn(false);

        //purchase
        boolean result = bookService.purchaseBook(user, book3);

        //verify - not successful
        assertFalse(result);
    }

    @Test
    public void testPurchaseBookInsufficientBalance() {
        Book book3 = new Book("The Cat and the Hat", "Dr.Seuss", "Fiction", 20.95);
        User user = new User("darranda", " Password123", "d@email.com.com");

        //set up mock book database
        when(bookDatabase.contains(book3)).thenReturn(false);

        // purchase book
        boolean result = bookService.purchaseBook(user, book3);

        assertFalse(result);
    }

    @Test
    public void testAddBookPositive() {
        Book book2 = new Book("The Giving Tree", "Shel Silverstein", "Fiction", 19.99);

        //set up mock book database
        when(bookDatabase.contains(book2)).thenReturn(false);

        //add
        boolean result = bookService.addBook(book2);

        //verify
        assertTrue(result);
        verify(bookDatabase).add(book2);
    }

    @Test
    public void testAddBookNegativeBookInDatabase() {
        Book book2 = new Book("The Giving Tree", "Shel Silverstein", "Fiction", 19.99);

        //set up mock book database
        when(bookDatabase.contains(book2)).thenReturn(true);

        //add
        boolean result = bookService.addBook(book2);

        //verify - not successful
        assertFalse(result);
        verify(bookDatabase, never()).add(book2);
    }

    @Test
    public void testAddBookEdge() {
        Book book2 = new Book("The Giving Tree", "Shel Silverstein", "Fiction", 19.99);

        //set up mock book database
        when(bookDatabase.contains(book2)).thenReturn(true);

        //add
        boolean result = bookService.addBook(book2);

        //verify - not successful
        assertFalse(result);
        verify(bookDatabase, never()).add(book2);


        Book book1 = new Book("Charlottes Web", "E.B. White", "Fiction", 21.00);

        //set up mock
        when(bookDatabase.contains(book1)).thenReturn(false);

        //add
        boolean result2 = bookService.addBook(book1);

        assertTrue(result2);
        verify(bookDatabase).add(book1);
    }

    @Test
    public void testRemoveBookPositive() {
        Book book1 = new Book("Charlottes Web", "E.B. White", "Fiction", 21.00);

        //set up mock
        when(bookDatabase.remove(book1)).thenReturn(true);

        //remove
        boolean result = bookService.removeBook(book1);

        assertTrue(result);
        verify(bookDatabase).remove(book1);
    }

    @Test
    public void testRemoveBookNegativeBookNotInDatabase() {
        Book book1 = new Book("Charlottes Web", "E.B. White", "Fiction", 21.00);

        //set up mock
        when(bookDatabase.remove(book1)).thenReturn(false);

        //remove
        boolean result = bookService.removeBook(book1);

        //verify not successful
        assertFalse(result);
        verify(bookDatabase).remove(book1);
    }

    @Test
    public void testRemoveBookEdge() {
        Book book1 = new Book("Charlottes Web", "E.B. White", "Fiction", 21.00);

        //set up mock
        when(bookDatabase.remove(book1)).thenReturn(false);

        //remove
        boolean result = bookService.removeBook(book1);

        assertFalse(result);
        verify(bookDatabase).remove(book1);


        Book book3 = new Book("The Cat and the Hat", "Dr.Seuss", "Fiction", 20.95);

        //set up mock
        when(bookDatabase.remove(book3)).thenReturn(true);

        //remove
        boolean result2 = bookService.removeBook(book3);

        //verify
        assertTrue(result2);
        verify(bookDatabase).remove(book1);
    }

    //clean up
    @AfterClass
    public static void tearDownClass() {
    }

    @After
    public void tearDown() {
    }
}