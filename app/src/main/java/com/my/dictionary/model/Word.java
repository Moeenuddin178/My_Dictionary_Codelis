package com.my.dictionary.model;

import java.io.Serializable;

public class Word implements Serializable{
	String id, word, result, favorites, edited;
	boolean userDb;

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public Word() {
	}

	public String getFavorites() {
		return favorites;
	}


	public void setFavorites(String favorites) {
		this.favorites = favorites;
	}


	public String getEdited() {
		return edited;
	}


	public void setEdited(String edited) {
		this.edited = edited;
	}

	public boolean isUserDb() {
		return userDb;
	}

	public void setUserDb(boolean userDb) {
		this.userDb = userDb;
	}
}
