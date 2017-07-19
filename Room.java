///////////////////////////////////////////////////////////////////////////////
//
//Main Class File:  TheGame.java
//File:             Room.java
//Semester:         CS 367 Fall 2015
//
//Author:           Andrew Zietlow arzietlow@wisc.edu
//CS Login:         azietlow
//Lecturer's Name:  Jim Skrentny
//Lab Section:      Lecture 1
//
//
//Pair Partner:     N/A
//
////////////////////////////80 columns wide //////////////////////////////////

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Used to describe the player's surroundings in terms of available items, 
 * possible uses for items, and also other potential reachable rooms.
 * Each game has exactly one room that is "special" in that the player can
 * win by reaching that room. 
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

public class Room {

	//name of the room
	private	String name;
	//description of the room
	private	String description;
	//whether the room is lit or dark
	private	boolean	visibility;
	//whether the room is habitable
	private	boolean habitability;
	//reason for room not being habitable (relevant if habitability is false)
	private String habitableMsg;
	//items in the room
	private	Set<Item> items;
	//message handlers
	private	List<MessageHandler> handlers;
	//locked rooms and the reason for their being locked
	private HashMap<Room, String> lockedPassages;	
	//Do not add any more data members

	/**
	 * Constructs a new Room object with the given parameters
	 * @throws IllegalArgumentException for null parameters
	 */
	public Room(String name, String description, boolean visibility, boolean 
			habitability, String habMsg, Set<Item> items, List<MessageHandler> 
	handlers) {

		if (name == null) throw new IllegalArgumentException();
		if (description == null) throw new IllegalArgumentException();
		if (handlers == null) throw new IllegalArgumentException();
		if (items == null) throw new IllegalArgumentException();
		
		this.name = name;
		this.description = description;
		this.visibility = visibility;
		this.habitability = habitability;
		if (!habitability) {
			if (habMsg == null) throw new IllegalArgumentException();
			this.habitableMsg = habMsg;
		}
		else this.habitableMsg = null;
		this.items = items;
		this.handlers = handlers;
		lockedPassages = new HashMap<Room, String>();
		
	}

	/**
	 * Getter method for a Room object's name
	 * @return the room's name
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Getter method for a Room object's visibility
	 * @return whether or not the room is visible
	 */
	public boolean isVisible(){
		return this.visibility;
	}

	/**
	 * Getter method for a Room object's habitability
	 * @return whether or not the room is habitable
	 */
	public boolean isHabitable(){
		return this.habitability;
	}

	/**
	 * Getter method for a Room object's habitableMessage field
	 * @return the message to display when a room is not habitable
	 */
	public String getHabitableMsg(){
		if (!habitability) return this.habitableMsg;
		else return null;
	}

	/**
	 * Getter method for a Room object's locked passages
	 * @return the locked rooms from this room, and why locked for each
	 */
	public HashMap<Room, String> getLockedPassages(){
		return this.lockedPassages;
	}

	/**
	 * Adds a room to this room's set of locked rooms, and why it is locked
	 * @param (passage) the locked room
	 * @param (whyLocked) the reason the room can't be accessed 
	 */
	public void addLockedPassage(Room passage, String whyLocked){
		if ((passage == null) || (whyLocked == null))
			throw new IllegalArgumentException();
		lockedPassages.put(passage, whyLocked);
	}

	/**
	 * Searches the room's set of items for an Item of the specified name
	 * @return the matching Item if found, null if not
	 * @param (item) the name of the item being looked for
	 */
	public Item findItem(String item){
		if (item == null) return null;
		for (Item o: items) {
			if (o.getName().equals(item)) return o;
		}
		return null; //item not found
	}

	/**
	 * Adds an item to this room's set of Items to be picked up
	 * @param (item) the Item object being added
	 * @return whether or not the room was successfully added to the set
	 */
	public boolean addItem(Item item){
		if (item == null) return false;
		return items.add(item);
	}

	/**
	 * Removes an item from this room's set of Items to be picked up
	 * @param (item) the Item object being removed
	 * @return whether or not the room was successfully removed from the set
	 */
	public boolean removeItem(Item item){
		if (item == null) return false;
		return items.remove(item);
	}


	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	/***
	 * Receives messages from items used by the player and executes the 
	 * appropriate action stored in a message handler
	 * @param message is the "message" sent by the item
	 * @return null, this Room or Unlocked Room depending on MessageHandler
	 * DO NOT MODIFY THIS METHOD
	 */
	public Room receiveMessage(String message){
		Iterator<MessageHandler> itr = handlers.iterator();
		MessageHandler msg = null;
		while(itr.hasNext()){
			msg = itr.next();
			if(msg.getExpectedMessage().equalsIgnoreCase(message))
				break;
			msg = null;
		}
		if(msg == null)
			return null;
		switch(msg.getType()){
		case("visibility") :
			this.visibility = true;
		return this;
		case("habitability") :
			this.habitability = true;
		return this;
		case("room") :
			for(Room rm : this.lockedPassages.keySet()){
				if(rm.getName().equalsIgnoreCase(msg.getRoomName())){
					this.lockedPassages.remove(rm);
					return rm;
				}
			}
		default:
			return null;
		}
	}


	@Override
	//Returns a String consisting of the Rooms name, its description,
	//its items and locked rooms.
	// DO NOT MODIFY THIS METHOD
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Welcome to the " + name + "!");
		sb.append(System.getProperty("line.separator"));
		if(!this.visibility){
			sb.append("Its too dark to see a thing!");
			sb.append(System.getProperty("line.separator"));
			sb.append("Places that can be reached from here :");
			sb.append(System.getProperty("line.separator"));
			for(Room rm :this.lockedPassages.keySet()){
				sb.append(rm.getName());
				sb.append(System.getProperty("line.separator"));
			}
			return sb.toString();
		}
		sb.append(description);
		sb.append(System.getProperty("line.separator"));
		if(this.items.size() >0){ 
			sb.append("Some things that stand out from the rest :");
			sb.append(System.getProperty("line.separator"));
		}
		Iterator<Item> itr = this.items.iterator();
		while(itr.hasNext()){
			sb.append(itr.next().getName());
			sb.append(System.getProperty("line.separator"));
		}
		sb.append("Places that can be reached from here :");
		sb.append(System.getProperty("line.separator"));
		for(Room rm :this.lockedPassages.keySet()){
			sb.append(rm.getName());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}

