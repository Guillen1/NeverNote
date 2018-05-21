package com.actian.nevernote.util;

import org.junit.Assert;

import com.actian.nevernote.model.Note;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractBaseTest {
	
	public void assertDeepEquals(Object expected, Object actual) {
		assertDeepEquals(null, expected, actual);
	}
	
	public void assertDeepEquals(String message, Object expected, Object actual) {
		ObjectMapper jsonMapper = new ObjectMapper();
		String jsonExpected = "";
		String jsonActual = "";
		try {
			jsonExpected = jsonMapper.writeValueAsString(expected);
			jsonActual = jsonMapper.writeValueAsString(actual);
		} catch (JsonProcessingException e) {
			// ignore
		}
		if (message == null) {
			Assert.assertEquals(jsonExpected, jsonActual);
		} else {
			Assert.assertEquals(message, jsonExpected, jsonActual);
		}
	}

	public List<Note> filterByTag(String tag, List<Note> notes){
		List<Note> filterNotes = new ArrayList<>();
		for(Note note : notes){
			if(!note.getTags().stream().filter(t -> t.equalsIgnoreCase(tag)).collect(Collectors.toSet()).isEmpty())
				filterNotes.add(note);
		}
		return filterNotes;
	}

	public String writeValueAsString(Object object){
		ObjectMapper jsonMapper = new ObjectMapper();
		try{
			return jsonMapper.writeValueAsString(object);
		}catch (JsonProcessingException e) {
			return "";
		}
	}
}
