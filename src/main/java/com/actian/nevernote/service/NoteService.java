package com.actian.nevernote.service;


import java.util.List;

import com.actian.nevernote.model.Note;

public interface NoteService {
	/**
	 * Get all Notes stored
	 * @return
	 */
	public List<Note> getAllNotes();

	/**
	 * Get Note by Note Id
	 * @param noteId The Note Id to get the Note
	 * @return
	 */
	public Note getNote(Long noteId);

	/**
	 * Add new Note by Notebook Id
	 * @param note The Note to be added
	 * @param notebookId The Notebook Id to add Note
	 * @return
	 */
	public Note addNote(Note note, Long notebookId);

	/**
	 * Update Note by Note Id
	 * @param note The Note to be updated
	 * @param noteId The Note Id to add Note
	 * @return
	 */
	public void updateNote(Note note, Long noteId);

	/**
	 * Delete Note by Notebook Id and Note Id
	 * @param notebookId The Notebook Id to delete Note
	 * @param noteId The Note Id to delete Note
	 * @return
	 */
	public void deleteNote(Long noteId, Long notebookId);

	/**
	 * Get all Notes stored by Note Tag
	 * @param tag The Tag to filter Noted
	 * @return
	 */
	public List<Note> getNotesByTag(String tag);

	/**
	 * Validate Note Id exists
	 * @param noteId The Note Id to validate
	 * @return
	 */
	public Boolean noteExists(Long noteId);

}
