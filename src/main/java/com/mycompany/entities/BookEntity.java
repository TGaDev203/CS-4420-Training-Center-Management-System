/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.entities;
import static com.mycompany.entities.BaseEntity.close;
import static com.mycompany.entities.BaseEntity.conn;
import static com.mycompany.entities.BaseEntity.open;
import static com.mycompany.entities.BaseEntity.statement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Book;
/**
 *
 * @author Legion
 */
public class BookEntity extends BaseEntity {
    public static void AddBook (Book newBook) {
        open();

        try {
            String sql = "INSERT INTO Book (title, author, genre, publisher, publicationDate ,totalBook, availLeft) VALUES (?, ?, ?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(sql);
            
            statement.setString(1, newBook.getBookTitle());
            statement.setString(2, newBook.getBookAuthor());
            statement.setString(3, newBook.getGenre());
            statement.setString(4, newBook.getPublisher());
            statement.setDate(5, new Date(newBook.getPublicationDate().getTime()));
            statement.setInt(6, newBook.getTotalBook());
            statement.setInt(7, newBook.getAvailBook());
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BookEntity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }
    // Get All Data of Book
    public static ObservableList<Book> GetDataBooks() throws SQLException {
        open();
        List<Book> bookList = new Vector<>();
        try(Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery("SELECT * FROM book")){
          while(rs.next()) {
              Book book = new Book();
              book.setBookTitle(rs.getString("title"));
              book.setBookID(rs.getInt("bookID"));
              book.setBookAuthor(rs.getString("author"));
              book.setGenre(rs.getString("genre"));
              book.setPublisher(rs.getString("publisher"));
              book.setPublicationDate(rs.getDate("publicationDate"));
              book.setTotalBook(rs.getInt("totalBook"));
              book.setAvailBook(rs.getInt("availLeft"));
              bookList.add(book);
          }
        }catch(Exception ex) {
            Logger.getLogger(BookEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<Book> bookDataList = FXCollections.observableList(bookList);
        return bookDataList;
    }
    public static void UpdateBook (Book updateBook, Book currentBook) {
        open();

        try {
            String sql = "UPDATE book SET title = ?, author = ?, genre = ?, publisher = ?, publicationDate = ?, totalBook = ?, availLeft = ? WHERE bookID = ?";
            statement = conn.prepareStatement(sql);
            
            statement.setString(1, updateBook.getBookTitle());
            statement.setString(2, updateBook.getBookAuthor());
            statement.setString(3, updateBook.getGenre());
            statement.setString(4, updateBook.getPublisher());
            statement.setDate(5, new Date (updateBook.getPublicationDate().getTime()));
            statement.setInt(6, updateBook.getTotalBook());
            Integer availLeft = updateBook.getTotalBook() - currentBook.getTotalBook() + currentBook.getAvailBook();
            statement.setInt(7, availLeft);
            statement.setInt(8, currentBook.getBookID());
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BookEntity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }
    public static void DeleteBook(Integer deletedBookID) {
        open();
        
        String sql = "DELETE FROM book WHERE bookID = ?";
        
        try {
            statement = conn.prepareStatement(sql);
        
            statement.setInt(1, deletedBookID);
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BookEntity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }
}
