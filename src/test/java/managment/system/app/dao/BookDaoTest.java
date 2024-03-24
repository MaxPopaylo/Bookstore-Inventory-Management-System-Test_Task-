package managment.system.app.dao;

import managment.system.app.dto.BookRequestDto;
import managment.system.app.entity.Book;
import managment.system.app.reporitory.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class BookDaoTest {

    @Mock
    public BookRepository repository;

    @InjectMocks
    public BookDao dao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private final Book defaultBook = new Book();
    private final BookRequestDto defaultDto = new BookRequestDto();
    private final UUID bookUUID = UUID.randomUUID();

    @BeforeEach
    public void initializeBooks() {
        defaultBook.setId(bookUUID);
        defaultBook.setTitle("Correct Title");

        defaultDto.setTitle("Correct Title");
    }

    @Test
    void shouldProperlyGetAllEntity() {

        when(repository.findAll()).thenReturn(singletonList(defaultBook));

        List<Book> books = dao.getAll();

        Assertions.assertEquals(1, books.size());
        Assertions.assertEquals(books.get(0).getId(), defaultBook.getId());
        Assertions.assertEquals(books.get(0).getTitle(), defaultBook.getTitle());

    }

    @Test
    void shouldProperlyGetEntityById() {

        when(repository.findById(bookUUID)).thenReturn(Optional.of(defaultBook));

        Book book = dao.getById(bookUUID);

        Assertions.assertNotNull(book);
        Assertions.assertEquals(book.getId(), defaultBook.getId());
        Assertions.assertEquals(book.getTitle(), defaultBook.getTitle());

    }

    @Test
    void shouldProperlySaveEntity() {

        when(repository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book book = dao.save(defaultDto);

        verify(repository, times(1)).save(any(Book.class));

        Assertions.assertNotNull(book);
        Assertions.assertEquals(book.getTitle(), defaultBook.getTitle());
    }

    @Test
    void shouldProperlyDeleteEntity() {

        when(repository.findById(bookUUID)).thenReturn(Optional.of(defaultBook));
        dao.delete(bookUUID);

        verify(repository, times(1)).delete(any(Book.class));

    }

    @Test
    void shouldProperlyUpdateEntity() {

        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("New Title");
        dto.setAuthor("New Author");

        when(repository.findById(bookUUID)).thenReturn(Optional.of(defaultBook));
        when(repository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book book = dao.update(dto, bookUUID);

        verify(repository, times(1)).save(any(Book.class));

        Assertions.assertNotNull(book);
        Assertions.assertEquals(book.getTitle(), dto.getTitle());
        Assertions.assertEquals(book.getAuthor(), dto.getAuthor());
    }

}