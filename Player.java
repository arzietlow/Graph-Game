///////////////////////////////////////////////////////////////////////////////
//
//Main Class File:  TheGame.java
//File:             Player.java
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

import java.util.HashSet;
import java.util.Set;

/**
 * Creates the one and only Player object used during play. Keeps track of 
 * items gathered and used by the user over the course of the game, 
 * as well as their name. 
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

public class Player {

	//player name
	private String name;
	//the magic sack held by the player that contains all his/her items
	private Set<Item> magicSack;

	/**
	 * Constructs a Player Object.
	 * @throws an IllegalArgumentException for null arguments
	 */
	public Player(String name, Set<Item> startingItems){
		
		if ((name == null) || (startingItems == null)) 
			throw new IllegalArgumentException();
		this.name = name;
		this.magicSack = new HashSet<Item>();
		
	}

	/**Getter method for the Name of the player
	 *@return the player's name 
	 */
	public String getName(){
		return this.name;
	}

	/**Returns a String consisting of the items in the sack
	 *@return the item list in string format
	 *@author CS367
	 */
	//DO NOT MODIFY THIS METHOD
	public String printSack(){
		//neatly printed items in sack
		StringBuilder sb = new StringBuilder();
		sb.append("Scanning contents of your magic sack");
		sb.append(System.getProperty("line.separator"));
		for(Item itm : magicSack){
			sb.append(itm.getName());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	/**Iterates through the sack, finding the items whose status is "activated" 
	 * Used in TheGame class when a user enters a new room, so that all 
	 * active items work in the new room.
	 * @return the set of active items
	 */
	public Set<Item> getActiveItems(){
		Set<Item> activeItems = new HashSet<Item>();
		for (Item i: magicSack) {
			if (i.activated()) activeItems.add(i);
		}
		return activeItems;
	}

	/**Find the Item in the sack whose name is "item"
	 *@param (item) the name of the item object being looked for
	 *@return the item if you find it, otherwise null.
	 */
	public Item findItem(String item){
		if (item == null) return null;
		for (Item i: magicSack) {
			if (i.getName().equals(item)) return i;
		}
		return null;
	}

	/**Checks if the player has a specified item in their sack.
	 * @param item the item object being searched for
	 * @return true if they do, otherwise false
	 */
	public boolean hasItem(Item item){
		if (item == null) return false;
		for (Item i: magicSack) {
			if (i.equals(item)) return true;
		}
		return false;
	}

	/**Adds the specified item to the sack. Duplicates not allowed.
	 * @param item the item object being added
	 * @return true if add was successful, else false.
	 */
	public boolean addItem(Item item){
		if (item == null) return false;
		return magicSack.add(item);
	}

	/**Removes the specified item from the sack, if possible.
	 * @param item the item object being searched for
	 * @return true if removal was successful, else false.
	 */
	public boolean removeItem(Item item){
		if (item == null) return false;
		return magicSack.remove(item);
	}
}
