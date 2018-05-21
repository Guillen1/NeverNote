package com.actian.nevernote.service;


import java.util.List;

import com.actian.nevernote.model.Note;
import com.actian.nevernote.model.Notebook;



public interface NotebookService {
	/**
	 * Get all Notebooks stored.
	 * @return
	 */
	public List<Notebook> getAllNotebooks();

	/**
	 * Get Notebook by Id
	 * @param id The ID of the Notebook to get.
	 * @return
	 */
	public Notebook getNotebook(Long id);

	/**
	 * Add new notebook
	 * @param notebook The Notebook to be added
	 * @return
	 */
	public Notebook addNotebook(Notebook notebook);

	/**
	 * Update Notebook passed by Id
	 * @param notebook The Notebook to be updated
	 * @param id The ID of the Notebook to be updated
	 * @return
	 */
	public void updateNotebook(Notebook notebook, Long id);

	/**
	 * Delete Notebook by Id
	 * @param id The Id of the Notebook to be deleted
	 * @return
	 */
	public void deleteNotebook(Long id);

	/**
	 * Get all Notes in the Notebook passed.
	 * @param id The Notebook Id to select Notes
	 * @return
	 */
	public List<Note> getNotebookNotes(Long id);

	/**
	 * Get all Notes in the Notebook passed by Note Tag
	 * @param id The Notebook Id to select Notes
	 * @param tag The Tag to filter Notes (Optional)
	 * @return
	 */
	public List<Note> getNotebookNotesByTag(Long id, String tag);

	/**
	 * Validate Notebook Id exist
	 * @param id The Notebook Id to validate
	 * @return
	 */
	public Boolean notebookExists(Long id);


}
