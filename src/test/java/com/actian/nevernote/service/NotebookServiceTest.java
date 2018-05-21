package com.actian.nevernote.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.actian.nevernote.model.Note;
import com.actian.nevernote.model.Notebook;
import com.actian.nevernote.repository.NotebookRepository;
import com.actian.nevernote.util.AbstractBaseTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class NotebookServiceTest extends AbstractBaseTest {
	@Autowired
	private NotebookService notebookService;
	@Autowired
	private NotebookRepository notebookRepository;

	@Transactional
	@Test
	public void getAllTest(){
		List<Notebook> actual, expected = new ArrayList<>();
		Notebook notebook = notebookRepository.save(newNotebook());
		actual = notebookService.getAllNotebooks();
		expected.add(notebook);
		assertDeepEquals(actual, expected);
	}

	@Transactional
	@Test
	public void getNotebookTest(){
		Notebook actual, expected;
		actual = notebookRepository.save(newNotebook());
		expected = notebookService.getNotebook(actual.getId());
		assertDeepEquals(actual, expected);
	}

	@Transactional
	@Test
	public void addNotebookTest(){
		Notebook actual, expected;
		actual = notebookService.addNotebook(newNotebook());
		Assert.assertNotNull(actual.getId());
		Assert.assertNotNull(actual.getCreated());
		Assert.assertNotNull(actual.getLastModified());
		expected = notebookRepository.findById(actual.getId()).get();
		assertDeepEquals(actual, expected);
	}

	@Transactional
	@Test
	public void updateNotebookTest(){
		Notebook actual, expected;
		expected = notebookRepository.save(newNotebook());
		expected.setName("Notebook 2");
		expected.getNotes().get(0).setBody("Test Update Change");
		notebookService.updateNotebook(expected, expected.getId());
		actual = notebookRepository.findById(expected.getId()).get();
		Assert.assertEquals(actual.getName(), expected.getName());
		Assert.assertEquals(actual.getNotes().get(0).getBody(), expected.getNotes().get(0).getBody( ));
		Assert.assertNotNull(actual.getLastModified());
	}

	@Transactional
	@Test
	public void deleteNotebookTest(){
		Notebook notebook;
		Optional<Notebook> actual;
		notebook = notebookRepository.save(newNotebook());
		notebookService.deleteNotebook(notebook.getId());
		actual = notebookRepository.findById(notebook.getId());
		Assert.assertFalse(actual.isPresent());
	}

	@Transactional
	@Test
	public void getNotebookNotesTest(){
		List<Note> actual, expected;
		Notebook notebook = notebookRepository.save(newNotebook());
		actual = notebookService.getNotebookNotes(notebook.getId());
		expected = notebook.getNotes();
		assertDeepEquals(actual, expected);
	}

	@Transactional
	@Test
	public void getNotebookNotesbyTagTest(){
		List<Note> actual, expected;
		Notebook notebook = notebookRepository.save(newNotebookMultiNotes());
		actual = notebookService.getNotebookNotesByTag(notebook.getId(),"TODO");
		expected = filterByTag("TODO", notebook.getNotes());
		assertDeepEquals(actual, expected);
		actual = notebookService.getNotebookNotesByTag(notebook.getId(), "tag1");
		expected = filterByTag("tag1", notebook.getNotes());
		assertDeepEquals(actual, expected);
	}

	@Transactional
	@Test
	public void getNotebookExistTest(){
		Notebook notebook = notebookRepository.save(newNotebook());
		Assert.assertTrue(notebookService.notebookExists(notebook.getId()));
		Assert.assertFalse(notebookService.notebookExists(1000L));
	}

	private Notebook newNotebook(){
		Notebook notebook = new Notebook(); 
		Note note = new Note(); 
		notebook.setName("First Notebook");
		note.setTitle("Note 1");
		note.setBody("Body message");
		note.setTags(Stream.of("todo","tag1").collect(Collectors.toCollection(HashSet::new)));
		notebook.setNotes(Stream.of(note).collect(Collectors.toCollection(ArrayList::new)));
		return notebook;
	}
	private Notebook newNotebookMultiNotes(){
		Notebook notebook = newNotebook();
		Note note = new Note();
		note.setTitle("Note 2");
		note.setBody("Body message");
		note.setTags(Stream.of("todo","tag2").collect(Collectors.toCollection(HashSet::new)));
		notebook.getNotes().add(note);
		return notebook;
	}
}
