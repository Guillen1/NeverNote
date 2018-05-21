package com.actian.nevernote.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.actian.nevernote.model.Note;
import com.actian.nevernote.model.Notebook;
import com.actian.nevernote.repository.NoteRepository;
import com.actian.nevernote.util.AbstractBaseTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by CZF1F3 on 5/20/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteServiceTest extends AbstractBaseTest {
    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NotebookService notebookService;

    @Transactional
    @Test
    public void getAllNotesTest(){
        List<Note> actual, expected = new ArrayList<>();
        noteRepository.findAll().forEach(expected::add);
        actual = noteService.getAllNotes();
        assertDeepEquals(actual, expected);
        Note note = noteRepository.save(newNote());
        actual = noteService.getAllNotes();
        Assert.assertTrue(actual.stream().anyMatch(n -> n.getNoteId().equals(note.getNoteId())));
    }

    @Transactional
    @Test
    public void getNoteTest(){
        Note actual, expected;
        expected = noteRepository.save(newNote());
        actual = noteService.getNote(expected.getNoteId());
        assertDeepEquals(actual, expected);
    }

    @Transactional
    @Test
    public void addNoteTest(){
        Note actual, expected;
        Notebook notebook = notebookService.addNotebook(new Notebook());
        actual = noteService.addNote(newNote(), notebook.getId());
        Assert.assertNotNull(actual.getNoteId());
        Assert.assertNotNull(actual.getCreated());
        Assert.assertNotNull(actual.getLastModified());
        expected = noteRepository.findById(notebook.getNotes().get(0).getNoteId()).get();
        assertDeepEquals(actual, expected);
        notebook = notebookService.getNotebook(notebook.getId());
        Assert.assertTrue(notebook.getNotes().stream().anyMatch(note -> note.getNoteId().equals(actual.getNoteId())));
    }

    @Transactional
    @Test
    public void updateNoteTest(){
        Note actual, expected;
        expected = noteRepository.save(new Note());
        expected.setTitle("Note 2");
        expected.setBody("Update note test.");
        noteService.updateNote(expected, expected.getNoteId());
        actual = noteRepository.findById(expected.getNoteId()).get();
        Assert.assertEquals(actual.getTitle(), expected.getTitle());
        Assert.assertEquals(actual.getBody(), expected.getBody());
        Assert.assertNotNull(actual.getLastModified());
    }

    @Transactional
    @Test
    public void deleteNoteTest(){
        Optional<Note> actual;
        Notebook  notebook = new Notebook();
        List<Note> notes = new ArrayList<>();
        notes.add(newNote());
        notebook.setNotes(notes);
        notebook = notebookService.addNotebook(notebook);
        Long noteId = notebook.getNotes().get(0).getNoteId();
        noteService.deleteNote(noteId, notebook.getId());
        actual = noteRepository.findById(noteId);
        Assert.assertFalse(actual.isPresent());
    }

    @Transactional
    @Test
    public void getNotesByTagTest(){
        List<Note> actual, expected = new ArrayList<>();
        Note note = newNote();
        note.setTags(Stream.of("todo","tag3").collect(Collectors.toCollection(HashSet::new)));
        noteRepository.save(note);
        actual = noteService.getNotesByTag("TAG3");
        noteRepository.findAll().forEach(expected::add);
        expected = filterByTag("TAG3", expected);
        assertDeepEquals(actual, expected);
    }

    @Transactional
    @Test
    public void noteExistsTest(){
        Note note = noteRepository.save(newNote());
        Assert.assertTrue(noteService.noteExists(note.getNoteId()));
        Assert.assertFalse(noteService.noteExists(new Long(1000)));
    }

    private Note newNote(){
        Note note = new Note();
        note.setTitle("Note 1");
        note.setBody("Body message");
        note.setTags(Stream.of("todo","tag1").collect(Collectors.toCollection(HashSet::new)));
        return note;
    }
}
