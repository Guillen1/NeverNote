package com.actian.nevernote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.actian.nevernote.model.Note;
import com.actian.nevernote.model.Notebook;
import com.actian.nevernote.repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {
	@Autowired
	NotebookService notebookService;
	
	@Autowired
	NoteRepository noteRepository;

	/**
	 * Get all Notes stored
	 * @return
	 */
	public List<Note> getAllNotes(){ 
		List<Note> notes = new ArrayList<>();
		noteRepository.findAll().forEach(notes::add);
		return notes;
	}

	/**
	 * Get Note by Note Id
	 * @param noteId The Note Id to get the Note
	 * @return
	 */
	public Note getNote(Long noteId){
		return noteRepository.findById(noteId).get();
	}

	/**
	 * Add new Note by Notebook Id
	 * @param note The Note to be added
	 * @param notebookId The Notebook Id to add Note
	 * @return
	 */
	public Note addNote(Note note, Long notebookId){
		//Add new Note to create Note Id
		note = noteRepository.save(note);
		//Get Notebook notes
		Notebook notebook = notebookService.getNotebook(notebookId);
		notebook.getNotes().add(note);
		//Update Notebook Notes with new Note
		notebookService.updateNotebook(notebook, notebook.getId());
		return note;
	}

	/**
	 * Update Note by Note Id
	 * @param note The Note to be updated
	 * @param noteId The Note Id to add Note
	 * @return
	 */
	public void updateNote(Note note, Long noteId){
		note.setNoteId(noteId);
		noteRepository.save(note);
	}

	/**
	 * Delete Note by Notebook Id and Note Id
	 * @param notebookId The Notebook Id to delete Note
	 * @param noteId The Note Id to delete Note
	 * @return
	 */
	public void deleteNote(Long noteId, Long notebookId){
		//Get Notebook notes
		Notebook notebook = notebookService.getNotebook(notebookId);
		//Remove note to be deleted from Notebook notes
		notebook.getNotes().removeIf(note -> note.getNoteId() == noteId);
		//Update Notebook Notes without the removed Note to remove association of Note
		notebookService.updateNotebook(notebook, notebook.getId());
		//Delete Note
		noteRepository.deleteById(noteId);
	}

	/**
	 * Get all Notes stored by Note Tag
	 * @param tag The Tag to filter Noted
	 * @return
	 */
	public List<Note> getNotesByTag(String tag){
		//Ignore case for Tags
		return noteRepository.findByTag(tag.toUpperCase());
	}

	/**
	 * Validate Note Id exists
	 * @param noteId The Note Id to validate
	 * @return
     */
	public Boolean noteExists(Long noteId){
		return noteRepository.existsById(noteId);
	}

}
