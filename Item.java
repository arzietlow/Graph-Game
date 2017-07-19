///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  TheGame.java
// File:             Item.java
// Semester:         CS 367 Fall 2015
//
// Author:           Andrew Zietlow arzietlow@wisc.edu
// CS Login:         azietlow
// Lecturer's Name:  Jim Skrentny
// Lab Section:      Lecture 1
//
//
// Pair Partner:     N/A
//
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 * Creates Item objects, used to access new rooms, reach new objects, or
 * alter the properties of a room.
 * Items each have messages that are sent to a Room object for display at the 
 * proper times. 
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

public class Item {
	//name of item
	private String name;
	//description of item
	private String description;
	//whether item is activated or not
	private boolean activated;
	//whether item can be activated multiple times or not
	private boolean oneUse;
	//activation message
	private String activationMsg;
	//used message
	private String usedMsg;

	/**
	 * Constructs a new Item object with the specified parameters. 
	 * @throws IllegalArgumentException String parameters cannot be null
	 */
	public Item(String name, String description, boolean activated, 
			String message,boolean oneTimeUse, String usedString){

		if (name == null) throw new IllegalArgumentException();
		if (description == null) throw new IllegalArgumentException();
		if (message == null) throw new IllegalArgumentException();
		if (usedString == null) throw new IllegalArgumentException();
		
		this.name = name;
		this.description = description;
		this.activated = activated;
		this.oneUse = oneTimeUse;
		this.activationMsg = message;
		this.usedMsg = usedString;

	}

	/**
	 * Getter method for the item's name
	 * @return the item's name
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Getter method for item's description
	 * @return the item's description
	 */
	public String getDescription(){
		return this.description;
	}

	/**
	 * Getter method for item's "activated" field
	 * @return whether or not the item is activated
	 */
	public boolean activated(){
		return this.activated;
	}

	/**
	 * Getter method for item's activation message
	 * @return the message displayed upon item activation
	 */
	public String on_use(){
		return this.activationMsg;
	}

	/**
	 * Sets the item's "activated" field to true (for when item is/was used)
	 */
	public void activate(){
		this.activated = true;
	}

	/**
	 * Getter method for item's used message
	 * @return the message displayed when the item has been used up
	 */
	public String on_useString(){
		return this.usedMsg;
	}

	/**
	 * Getter method for whether or not the item can only be used once
	 * @return true if item disappears after one use, false otherwise
	 */
	public boolean isOneTimeUse(){
		return this.oneUse;
	}

	@Override
	//This returns a String consisting of the name and description of the Item
	//This has been done for you.
	//DO NOT MODIFY
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Item Name: " + this.name);
		sb.append(System.getProperty("line.separator"));
		sb.append("Description: " + this.description);
		return sb.toString();
	}
}
