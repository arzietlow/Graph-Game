///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            The Game (TheGame.java)
// Files:            DirectedGraph.java, GraphADT.java, Item.java, Player.java,
//					 MessageHandler.java, Room.java, TheGame.java 
//
// Semester:         CS 367 - Fall 2015
//
// Author:           Andrew Zietlow
// Email:            arzietlow@wisc.edu
// CS Login:         azietlow
// Lecturer's Name:  Jim Skrentny
// Lab Section:      Lecture 1
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * The main class of the game. Handles initialization of all data by
 * reading the input file. Also processes and executes user commands. 
 *
 * <p>Bugs: none known
 *
 * @author azietlow
 */

public class TheGame {
	private static String gameIntro; // initial introduction to the game
	private static String winningMessage; //winning message of game
	private static String gameInfo; //additional game info
	private static boolean gameWon = false; //state of the game
	private static Scanner scanner = null; //for reading files
	private static Scanner ioscanner = null; //for reading standard input
	private static Player player; //object for player of the game
	private static Room location; //current room in which player is located
	private static Room winningRoom; //Room which player must reach to win
	private static Item winningItem; //Item which player must find
	private static DirectedGraph<Room> layout; //Graph structure of the Rooms

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Bad invocation! Correct usage: "
					+ "java TheGame <gameFile>");
			System.exit(1);
		}

		boolean didInitialize = initializeGame(args[0]);

		if (!didInitialize) {
			System.err.println("Failed to initialize the application!");
			System.exit(1);
		}

		System.out.println(gameIntro); // game intro

		processUserCommands();
	}

	/**
	 * Reads the file named "gameFile" and initializes all variables for game 
	 * to run smoothly. This includes layour, player, rooms, items etc.
	 * 
	 * @throws NullPointerException if scanner runs off the file
	 * @throws FileNotFoundException if the initialization file is not found
	 * @param (gameFile) the name of the .txt file to read input from
	 * @return whether or not the file was read without error
	 */
	private static boolean initializeGame(String gameFile) {

		try {
			//reads player name
			System.out.println("Welcome worthy squire! What might be your "
					+ "name?");
			ioscanner = new Scanner(System.in);
			String playerName = ioscanner.nextLine();
			player = new Player(playerName, new HashSet<Item>());
			layout = new DirectedGraph<Room>();

			File inputFile = new File(gameFile);//file being read from
			scanner = new Scanner(inputFile);//printstream of game file

			//THESE NEXT 3 LINES CAN BE ANYTHING
			gameIntro      = scanner.nextLine();
			winningMessage = scanner.nextLine();
			gameInfo       = scanner.nextLine();

			String keyLine = scanner.nextLine();
			//Keyline is used for knowing the section of the file being read
			if (keyLine.contains("#player items:")) {
				keyLine = scanner.nextLine();
				while (keyLine.contains("#item:")) {

					String itemName = scanner.nextLine().trim();
					String itemDescrip = scanner.nextLine().trim();
					String checkActivated = scanner.nextLine().trim();
					String activationMsg = scanner.nextLine().trim();
					Boolean activated = null;
					String checkOneUse = scanner.nextLine().trim();
					Boolean oneUse = null;
					String usedMsg = scanner.nextLine().trim();

					//parsing activated boolean from String
					if (checkActivated.equalsIgnoreCase("true")) 
						activated = true;
					else if (checkActivated.equalsIgnoreCase("false")) 
						activated = false;

					//parsing oneTimeUse boolean from String
					if (checkOneUse.equalsIgnoreCase("true")) 
						oneUse = true;
					else if (checkOneUse.equalsIgnoreCase("false")) 
						oneUse = false;

					//Error checking
					if ((oneUse == null) || (activated == null)) {
						return false;
					}

					Item toAdd = new Item(itemName, itemDescrip, activated, 
							activationMsg, oneUse, usedMsg);
					if (keyLine.contains("#win")) {
						if (winningItem == null) {
							winningItem = toAdd;
						}
						else return false;
					}
					player.addItem(toAdd);
					keyLine = scanner.nextLine();
				}
			}
			
			//parsing all listed rooms
			while (keyLine.contains("#room:")) {
				boolean isWinningRoom = false;
				if (keyLine.contains("#win")) isWinningRoom = true;
				Set<Item> roomItems = new HashSet<Item>();
				List<MessageHandler> handlers = new 
						ArrayList<MessageHandler>();
				Boolean visibility = null;
				Boolean habitable = null;
				String habMsg = null;

				String roomName = scanner.nextLine().trim();
				String roomDescrip = scanner.nextLine().trim();
				String checkVisibility = scanner.nextLine().trim();
				String checkHabitability = scanner.nextLine().trim();

				//Parsing habitability boolean from String
				if (checkHabitability.equalsIgnoreCase("false")) {
					habitable = false;
				}
				else if (checkHabitability.equalsIgnoreCase("true")) {
					habitable = true;
				}
				if (!habitable) habMsg = scanner.nextLine().trim();

				//Parsing visibility boolean from String
				if (checkVisibility.equalsIgnoreCase("false")) {
					visibility = false;
				}
				else if (checkVisibility.equalsIgnoreCase("true")) {
					visibility = true;
				}

				//Error checking
				if ((visibility == null) || (habitable == null)) 
					return false;

				keyLine = scanner.nextLine();
				//In later revisions, I would try and find a good way to recycle
				//this code rather than using it in 2 places (it is used above)
				while (keyLine.contains("#item:")) {

					String itemName = scanner.nextLine().trim();
					String itemDescrip = scanner.nextLine().trim();
					String checkActivated = scanner.nextLine().trim();
					String activationMsg = scanner.nextLine().trim();
					Boolean activated = null;
					String checkOneUse = scanner.nextLine().trim();
					Boolean oneUse = null;
					String usedMsg = scanner.nextLine().trim();

					//Parsing activated boolean from String
					if (checkActivated.equalsIgnoreCase("true")) 
						activated = true;
					else if (checkActivated.equalsIgnoreCase("false")) 
						activated = false;

					//Parsing oneTimeUse boolean from String
					if (checkOneUse.equalsIgnoreCase("true")) 
						oneUse = true;
					else if (checkOneUse.equalsIgnoreCase("false")) 
						oneUse = false;

					//Error checking
					if ((activated == null) || (oneUse == null)) return false;

					Item itemToAdd = new Item(itemName, itemDescrip, activated, 
							activationMsg, oneUse, usedMsg);
					if (keyLine.contains("#win")) {
						if (winningItem == null) winningItem = itemToAdd;
						else return false; //Can't have 2 winning Items
					}
					roomItems.add(itemToAdd);
					keyLine = scanner.nextLine();
				}
				while (keyLine.contains("messageHandler:")) {
					String msg = scanner.nextLine().trim();
					String type = scanner.nextLine().trim();
					String room = null;
					if (type.equals("room")) {
						room = scanner.nextLine().trim();
					}
					handlers.add(new MessageHandler(msg, type, room));
					keyLine = scanner.nextLine();
				}
				Room roomToAdd = new Room(roomName, roomDescrip, visibility,
						habitable, habMsg, roomItems, handlers);
				layout.addVertex(roomToAdd);
				if (isWinningRoom) {
					if (winningRoom == null) winningRoom = roomToAdd;
					else return false; //can't have 2+ winning rooms
				}
				//sets first room as start location
				if (location == null) location = roomToAdd;
			}
			if (keyLine.contains("#locked passages:")) {
				Set<Room> rooms = layout.getAllVertices();
				keyLine = scanner.nextLine().trim();
				
				//continues reading locked passages until key reads adj. header
				while (!keyLine.contains("#Adjacency List:")) {
					//divides the adjacency string into room names (should be 2)
					String[] roomStrings = keyLine.split(" ");
					//the adjacency string should only contain 2 room names
					if (roomStrings.length > 2) return false;
					//no room can be unrechable from itself
					if (roomStrings[0].equals(roomStrings[1])) return false;
					String whyLocked = scanner.nextLine().trim();
					Room addPassage = null;
					Room lockedRoom = null;
					for (Room r: rooms) {
						if (r.getName().equals(roomStrings[0])) {
							addPassage = r;
						}
						else if (r.getName().equals(roomStrings[1]))
							lockedRoom = r;
					}
					//init fails if a room name doesn't match any room objects
					if ((addPassage == null) || (lockedRoom == null))
						return false;
					addPassage.addLockedPassage(lockedRoom, whyLocked);
					keyLine = scanner.nextLine();
				}
			}
			if (keyLine.contains("#Adjacency List:")) {
				boolean oneEdgeRead = false;//Makes sure graph has >= 1 edge
				Set<Room> rooms = layout.getAllVertices();
				while (scanner.hasNextLine()) {

					boolean roomFound = false;//whether the source room exists
					Room startRoom = null;
					Room endRoom = null;
					String[] roomNames = 
							scanner.nextLine().trim().split(" ");
					for (Room r: rooms) {
						if (r.getName().equals(roomNames[0])) {
							startRoom = r;
							roomFound = true;
							break;
						}
					}
					if (roomFound == false) return false;//no matching room name

					roomNames[0] = null;
					//removes 1st string on roomName list, represents source
					
					//Loop starts at 1 so null string isn't error checked
					for (int i = 1; i < roomNames.length; i++) {
						boolean edgeRoomFound = false;
						for (Room r: rooms) {
							if (r.getName().equals(roomNames[i])) {
								edgeRoomFound = true;
								oneEdgeRead = true;
								endRoom = r;
								layout.addEdge(startRoom, endRoom);
								startRoom.getLockedPassages();
								break;
							}
						}
						//Returns false if not every string specified by the
						//file could be matched to a corrresponding room object
						if (edgeRoomFound == false) return false;
					}
				}
				//returns false if there is not at least 1 edge in the graph
				//or if there isn't a winning room
				if (oneEdgeRead == false) return false;
				if (winningRoom == null) return false;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e){
			return false;
		}
		return true;
	}

	private static void processUserCommands() {
		String command = null;
		do {
			System.out.print("\nPlease Enter a command ([H]elp):");
			command = ioscanner.next();
			switch (command.toLowerCase()) {
			case "p": // pick up
				processPickUp(ioscanner.nextLine().trim());
				goalStateReached();
				break;
			case "d": // put down item
				processPutDown(ioscanner.nextLine().trim());
				break;
			case "u": // use item
				processUse(ioscanner.nextLine().trim());
				break;
			case "lr":// look around
				processLookAround();
				break;
			case "lt":// look at
				processLookAt(ioscanner.nextLine().trim());
				break;
			case "ls":// look at sack
				System.out.println(player.printSack());
				break;
			case "g":// goto room
				processGoTo(ioscanner.nextLine().trim());
				goalStateReached();
				break;
			case "q":
				System.out.println("You Quit! You, " + player.getName() + ", "
						+ "are a loser!!");
				break;
			case "i":
				System.out.println(gameInfo);
				break;
			case "h":
				System.out
				.println("\nCommands are indicated in [], and may be followed "
						+ "by \n"+
						"any additional information which may be needed, "
						+ "indicated within <>.\n"
						+ "\t[p]  pick up item: <item name>\n"
						+ "\t[d]  put down item: <item name>\n"
						+ "\t[u]  use item: <item name>\n"
						+ "\t[lr] look around\n"
						+ "\t[lt] look at item: <item name>\n"
						+ "\t[ls] look in your magic sack\n"
						+ "\t[g]  go to: <destination name>\n"
						+ "\t[q]  quit\n"
						+ "\t[i]  game info\n");
				break;
			default:
				System.out.println("Unrecognized Command!");
				break;
			}
		} while (!command.equalsIgnoreCase("q") && !gameWon);
		ioscanner.close();
	}

	private static void processLookAround() {
		System.out.print(location.toString());
		for(Room rm : layout.getNeighbors(location)){
			System.out.println(rm.getName());
		}
	}

	private static void processLookAt(String item) {
		Item itm = player.findItem(item);
		if(itm == null){
			itm = location.findItem(item);
		}
		if(itm == null){
			System.out.println(item + " not found");
		}
		else
			System.out.println(itm.toString());
	}

	private static void processPickUp(String item) {
		if(player.findItem(item) != null){
			System.out.println(item + " already in sack");
			return;
		}
		Item newItem = location.findItem(item);
		if(newItem == null){
			System.out.println("Could not find " + item);
			return;
		}
		player.addItem(newItem);
		location.removeItem(newItem);
		System.out.println("You picked up ");
		System.out.println(newItem.toString());
	}

	private static void processPutDown(String item) {
		if(player.findItem(item) == null){
			System.out.println(item + " not in sack");
			return;
		}
		Item newItem = player.findItem(item);
		location.addItem(newItem);
		player.removeItem(newItem);
		System.out.println("You put down " + item);
	}

	private static void processUse(String item) {
		Item newItem = player.findItem(item);
		if(newItem == null){
			System.out.println("Your magic sack doesn't have a " + item);
			return;
		}
		if (newItem.activated()) {
			System.out.println(item + " already in use");
			return;
		}
		if(notifyRoom(newItem)){
			if (newItem.isOneTimeUse()) {
				player.removeItem(newItem);
			}
		}
	}

	private static void processGoTo(String destination) {
		Room dest = findRoomInNeighbours(destination);
		if(dest == null) {
			for(Room rm : location.getLockedPassages().keySet()){
				if(rm.getName().equalsIgnoreCase(destination)){
					System.out.println(location.getLockedPassages().get(rm));
					return;
				}
			}
			System.out.println("Cannot go to " + destination + " from here");
			return;
		}
		Room prevLoc = location;
		location = dest;
		if(!player.getActiveItems().isEmpty())
			System.out.println("The following items are active:");
		for(Item itm:player.getActiveItems()){
			notifyRoom(itm);
		}
		if(!dest.isHabitable()){
			System.out.println("Thou shall not pass because");
			System.out.println(dest.getHabitableMsg());
			location = prevLoc;
			return;
		}

		System.out.println();
		processLookAround();
	}

	private static boolean notifyRoom(Item item) {
		Room toUnlock = location.receiveMessage(item.on_use());
		if (toUnlock == null) {
			if(!item.activated())
				System.out.println("The " + item.getName() + " cannot be used "
						+ "here");
			return false;
		} else if (toUnlock == location) {
			System.out.println(item.getName() + ": " + item.on_useString());
			item.activate();
		} else {
			// add edge from location to to Unlock
			layout.addEdge(location, toUnlock);
			if(!item.activated())
				System.out.println(item.on_useString());
			item.activate();
		}
		return true;
	}

	private static Room findRoomInNeighbours(String room) {
		Set<Room> neighbours = layout.getNeighbors(location);
		for(Room rm : neighbours){
			if(rm.getName().equalsIgnoreCase(room)){
				return rm;
			}
		}
		return null;
	}

	private static void goalStateReached() {
		if ((location == winningRoom && player.hasItem(winningItem)) 
				|| (location == winningRoom && winningItem == null)){
			System.out.println("Congratulations, " + player.getName() + "!");
			System.out.println(winningMessage);
			System.out.println(gameInfo);
			gameWon = true;
		}
	}

}
