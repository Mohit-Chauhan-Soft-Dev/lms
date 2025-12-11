package com.lms.booktrack.controller;

import com.lms.booktrack.model.Book;
import com.lms.booktrack.model.Issue;
import com.lms.booktrack.service.BookService;
import com.lms.booktrack.service.IssueService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private IssueService issueService;

    @GetMapping
    public String listBooks(Model model, @RequestParam(value = "search", required = false) String search) {
        List<Book> books;
        if (search != null && !search.trim().isEmpty()) {
            books = bookService.searchBooks(search.trim());
        } else {
            books = bookService.getAllBooks();
        }

        long availableCount = books.stream().filter(b -> b.getAvailableCopies() > 0).count();
        long issuedCount = books.size() - availableCount;

        model.addAttribute("books", books);
        model.addAttribute("search", search);
        model.addAttribute("availableCount", availableCount);
        model.addAttribute("issuedCount", issuedCount);

        return "books/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        System.out.println("In showAddForm method ...");
        return "books/add";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttrs) {

        System.out.println("In addBook method ...");
                            
        if (result.hasErrors()) {
            return "books/add";
        }
        if (bookService.isIsbnExists(book.getIsbn())) {
            model.addAttribute("errorMessage", "ISBN already exists. Please enter a unique ISBN.");
            return "books/add";
        }

        book.setAvailableCopies(book.getTotalCopies());
        bookService.saveBook(book);
        redirectAttrs.addFlashAttribute("successMessage", "Book added successfully!");
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs) {
        Book book = bookService.getBookById(id).orElse(null);
        if (book == null) {
            redirectAttrs.addFlashAttribute("errorMessage", "Book not found.");
            return "redirect:/books";
        }
        model.addAttribute("book", book);

        List<Issue> bookIssues = issueService.getIssuesByBookId(id);
        model.addAttribute("bookIssues", bookIssues);
        return "books/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "books/edit";
        }
        if (bookService.isIsbnExistsForOtherBook(book.getIsbn(), id)) {
            model.addAttribute("errorMessage", "ISBN already exists for another book. Please enter a unique ISBN.");
            return "books/edit";
        }
        try {
            bookService.updateBook(id, book);
            redirectAttrs.addFlashAttribute("successMessage", "Book updated successfully!");
        } catch (RuntimeException ex) {
            redirectAttrs.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttrs) {
        try {
            bookService.deleteBook(id);
            redirectAttrs.addFlashAttribute("successMessage", "Book deleted successfully!");
        } catch (RuntimeException ex) {
            redirectAttrs.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/books";
    }
}
