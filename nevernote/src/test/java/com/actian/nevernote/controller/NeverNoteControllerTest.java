package com.actian.nevernote.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.actian.nevernote.model.Note;
import com.actian.nevernote.model.Notebook;
import com.actian.nevernote.service.NoteService;
import com.actian.nevernote.service.NotebookService;
import com.actian.nevernote.util.AbstractBaseTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@WebMvcTest(value=NeverNoteController.class, secure = false)
public class NeverNoteControllerTest extends AbstractBaseTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(NeverNoteController.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotebookService notebookService;

    @MockBean
    private NoteService noteService;

    @Test
    public void getAllNotebooksTest() throws Exception{
        List<Notebook> mockNotebooks = Stream.of(getMockNotebook()).collect(Collectors.toCollection(ArrayList::new));
        Mockito.when(notebookService.getAllNotebooks()).thenReturn(mockNotebooks);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notebooks").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        LOGGER.info(result.getResponse().getContentAsString());
        String expected = writeValueAsString(mockNotebooks);
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getNotebookTest() throws Exception{
        Notebook mockNotebook = getMockNotebook();
        Mockito.when(notebookService.getNotebook(Mockito.anyLong())).thenReturn(mockNotebook);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notebooks/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        LOGGER.info(result.getResponse().getContentAsString());
        String expected = writeValueAsString(mockNotebook);
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void addNotebookTest() throws Exception{
        Notebook mockNotebook = getMockNotebook();
        Mockito.when(notebookService.addNotebook(Mockito.any(Notebook.class))).thenReturn(mockNotebook);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/notebooks").accept(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(getMockNewNotebook())).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        LOGGER.info(response.getHeader(HttpHeaders.LOCATION));
        Assert.assertEquals("http://localhost/notebooks/1", response.getHeader(HttpHeaders.LOCATION));
    }

    @Test
    public void updateNotebookTest() throws Exception{
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.TRUE);
        Mockito.doNothing().when(notebookService).updateNotebook(Mockito.any(Notebook.class), Mockito.anyLong());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/notebooks/1").accept(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(getMockNotebook())).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.FALSE);
        result = mockMvc.perform(requestBuilder).andReturn();
        response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void deteletNotebookTest() throws Exception{
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.TRUE);
        Mockito.doNothing().when(notebookService).deleteNotebook(Mockito.anyLong());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/notebooks/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.FALSE);
        result = mockMvc.perform(requestBuilder).andReturn();
        response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void getNotebookNotes() throws Exception{
        List<Note> mockNotes = Stream.of(getMockNote()).collect(Collectors.toCollection(ArrayList::new));
        Mockito.when(notebookService.getNotebookNotes(Mockito.anyLong())).thenReturn(mockNotes);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notebooks/1/notes").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        LOGGER.info(result.getResponse().getContentAsString());
        String expected = writeValueAsString(mockNotes);
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getNotebookNotes_TagFilter() throws Exception{
        List<Note> mockNotes = Stream.of(getMockNote()).collect(Collectors.toCollection(ArrayList::new));
        Mockito.when(notebookService.getNotebookNotesByTag(Mockito.anyLong(), Mockito.anyString())).thenReturn(mockNotes);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notebooks/1/notes").param("tag","TODO").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        LOGGER.info(result.getResponse().getContentAsString());
        String expected = writeValueAsString(mockNotes);
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getAllNotesTest() throws Exception{
        List<Note> mockNotes = Stream.of(getMockNote()).collect(Collectors.toCollection(ArrayList::new));
        Mockito.when(noteService.getAllNotes()).thenReturn(mockNotes);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notes").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        LOGGER.info(result.getResponse().getContentAsString());
        String expected = writeValueAsString(mockNotes);
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getAllNotesTest_TagFilter() throws Exception{
        List<Note> mockNotes = Stream.of(getMockNote()).collect(Collectors.toCollection(ArrayList::new));
        Mockito.when(noteService.getNotesByTag(Mockito.anyString())).thenReturn(mockNotes);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notes").param("tag","TODO").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        LOGGER.info(result.getResponse().getContentAsString());
        String expected = writeValueAsString(mockNotes);
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getNoteTest()throws Exception{
        Note mockNote = getMockNote();
        Mockito.when(noteService.getNote(Mockito.anyLong())).thenReturn(mockNote);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/notebooks/1/notes/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        LOGGER.info(result.getResponse().getContentAsString());
        String expected = writeValueAsString(mockNote);
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void addNoteTest() throws Exception{
        Note mockNote = getMockNote();
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(noteService.addNote(Mockito.any(Note.class), Mockito.anyLong())).thenReturn(mockNote);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/notebooks/1/notes").accept(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(getMockNote())).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        LOGGER.info(response.getHeader(HttpHeaders.LOCATION));
        Assert.assertEquals("http://localhost/notebooks/1/notes/1", response.getHeader(HttpHeaders.LOCATION));
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.FALSE);
        result = mockMvc.perform(requestBuilder).andReturn();
        response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void updateNoteTest() throws Exception{
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(noteService.noteExists(Mockito.anyLong())).thenReturn(Boolean.TRUE);
        Mockito.doNothing().when(noteService).updateNote(Mockito.any(Note.class), Mockito.anyLong());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/notebooks/1/notes/1").accept(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(getMockNote())).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        Mockito.when(noteService.noteExists(Mockito.anyLong())).thenReturn(Boolean.FALSE);
        result = mockMvc.perform(requestBuilder).andReturn();
        response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.FALSE);
        result = mockMvc.perform(requestBuilder).andReturn();
        response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void deleteNoteTest() throws Exception{
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(noteService.noteExists(Mockito.anyLong())).thenReturn(Boolean.TRUE);
        Mockito.doNothing().when(noteService).deleteNote(Mockito.anyLong(), Mockito.anyLong());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/notebooks/1/notes/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        Mockito.when(noteService.noteExists(Mockito.anyLong())).thenReturn(Boolean.FALSE);
        result = mockMvc.perform(requestBuilder).andReturn();
        response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Mockito.when(notebookService.notebookExists(Mockito.anyLong())).thenReturn(Boolean.FALSE);
        result = mockMvc.perform(requestBuilder).andReturn();
        response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    private Note getMockNote(){
        Note note = new Note();
        note.setNoteId(1L);
        note.setTitle("Note 1");
        note.setBody("Body message");
        note.setTags(Stream.of("todo","tag1").collect(Collectors.toCollection(HashSet::new)));
        return note;
    }

    private Notebook getMockNotebook(){
        Notebook notebook = new Notebook();
        Note note = getMockNote();
        notebook.setId(1L);
        notebook.setName("First Notebook");
        notebook.setNotes(Stream.of(note).collect(Collectors.toCollection(ArrayList::new)));
        return notebook;
    }

    private Notebook getMockNewNotebook(){
        Notebook notebook = new Notebook();
        Note note = new Note();
        notebook.setName("First Notebook");
        note.setTitle("Note 1");
        note.setBody("Body message");
        note.setTags(Stream.of("todo","tag1").collect(Collectors.toCollection(HashSet::new)));
        notebook.setNotes(Stream.of(note).collect(Collectors.toCollection(ArrayList::new)));
        return notebook;
    }

}

