import java.util.*;
import java.io.*;

public class MyFavoriteMoviesMain {
	//Method for performing the action desired by the user and outputting the relevant information.
	public static void performAction(ArrayList <Movies> list, Scanner scanner, int userAction, String keyword) {
		/* Performs the action the user desires(analyze a movie or list all movies of a genre).
		 * Parameters are the arraylist of movies, the scanner that allows the user to input information, the integer that represents the user's choice, 
		 * and the keyword that will represent the movie title or the genre to be searched depending on the user's choice..
		 * Returns void.
		 */

		//If user wants to analyze a specific movie
		if (userAction==1) {
			System.out.println();
			Collections.sort(list);
			try{
				System.out.println(list.get(Collections.binarySearch(list, new Movies(keyword, ""))));
			}catch (IndexOutOfBoundsException e) {
				System.out.printf("Sorry, the movie '%s' was not found in the database.%n", keyword);
			};
			//If user wants to look at all the movies of a genre
		}else if (userAction == 2) {
			System.out.println();
			Collections.sort(list, new SortGenre());
			try {
				int counter = Collections.binarySearch(list, new Movies(keyword), new SortGenre());
				if (counter>=-1) {
					while (list.get(counter).getGenre().equalsIgnoreCase(keyword) && counter >= 0) {
						counter--;
						if (counter < 0)
							break;
					}
					counter++;
					for (int count = counter; list.get(count).getGenre().equalsIgnoreCase(keyword) && count < list.size(); count++) {
						System.out.println(list.get(count));
						if (count+1>=list.size())
							break;
					}
				}else
					System.out.printf("Sorry, none of the movies in the database are of the genre '%s'.%n", keyword);
			}catch (IndexOutOfBoundsException e) {
				System.out.printf("Sorry, none of the movies in the database are of the genre '%s'.%n", keyword);
			}
		}
		//		else
		//			System.out.println("ERROR HERE");
	}

	public static void main(String[] args) {
		ArrayList <Movies> moviesList = new ArrayList <Movies>();
		Scanner userInput = new Scanner(System.in);
		boolean fileDoesntExist = false;
		//Filling arraylist of Movies
		try{
			Scanner fileInput = new Scanner(new File("movies.txt"));
			String currentLine;
			while (fileInput.hasNextLine()) {
				currentLine = fileInput.nextLine().trim();
				try {
					double rating = Math.round((Double.parseDouble(currentLine.substring(0, currentLine.indexOf(' ')-1)))*100)/100.0;
					if (currentLine.indexOf(' ')!=currentLine.lastIndexOf(' ') && 
							rating > 0 && rating <=100 &&
							currentLine.charAt(currentLine.indexOf(' ')-1)=='%') {
						String title = currentLine.substring(currentLine.indexOf(' ')+1, currentLine.lastIndexOf(' ')).trim();
						String genre = currentLine.substring(currentLine.lastIndexOf(' ')+1).toUpperCase().toLowerCase().trim();
						genre = Character.toUpperCase(genre.charAt(0))+genre.substring(1);
						moviesList.add(new Movies(title, rating, genre));
					}
				}catch (NumberFormatException e) {
				}catch (StringIndexOutOfBoundsException e) {}
			}
			fileInput.close();
		}catch (FileNotFoundException e) {
			System.out.println("This file doesn't exist.");
			fileDoesntExist = true;
		}
		boolean goAgain = true;
		int menuOption = 0;
		/* Menu Options:
		 O = main menu(only at startup)
		 1 = choose movie
		 2 = choose genre
		 */
		Collections.sort(moviesList, new SortRating());
		//Assigning rank of each movie
		int tiedRankCounter = 0;
		for (int count = 1; count <= moviesList.size(); count++) {
			if (count > 1 && moviesList.get(count-2).getRating() == moviesList.get(count-1).getRating()) {
				tiedRankCounter++;
				moviesList.get(count-1).setRank(moviesList.get(count-2).getRank());
			}else if (count > 1 && tiedRankCounter>0) {
				moviesList.get(count-1).setRank(moviesList.get(count-2).getRank()+1+tiedRankCounter);
				tiedRankCounter = 0;
			}else if (tiedRankCounter == 0)
				if (count == 1)
					moviesList.get(count-1).setRank(count);
				else if (count > 1)
					moviesList.get(count-1).setRank(moviesList.get(count-2).getRank()+1);
		}
		if (moviesList.size()!=0) 
			System.out.println("You can analyze the statistics of any individual movie, or you can list all the movies of a specific genre.");
		else if (moviesList.size()==0 && !fileDoesntExist){
			System.out.println("This is an empty file - there is nothing to analyze.");
			goAgain = false;
		}else if (fileDoesntExist)
			goAgain = false;

		//Continue asking as long as user doesn't enter 'exit'
		while (goAgain) {
			String input = "";
			//Main menu option
			if (menuOption == 0) {
				System.out.println("Enter 'movie', or 'm' to analyze a movie, or 'genre', or 'g' to list all the movies of a specific genre. Enter 'exit' or 'x' to leave at any time.");
				input= userInput.nextLine().trim();
				while (input.length()==0) {
					System.out.println("Please try again.");
					System.out.println("Enter 'movie', or 'm' to analyze a movie, or 'genre', or 'g' to list all the movies of a specific genre. Enter 'exit' or 'x' to leave at any time.");
					input = userInput.nextLine().trim();
				}
				if (input.equalsIgnoreCase("Exit") || (input.trim().equalsIgnoreCase("x")))
					goAgain = false;
				else if (input.equalsIgnoreCase("movie")||(input.equalsIgnoreCase("m"))) 
					menuOption = 1;
				else if (input.equalsIgnoreCase("genre")|| (input.equalsIgnoreCase("g"))) 
					menuOption = 2;
				else 
					System.out.println("Please try again.");
				//Choose movie option
			}else if (menuOption == 1) {
				System.out.println("Enter a movie title to analyze. You can also enter 'exit' or 'x' to leave, or 'genre' or 'g' to switch to analyzing by genre.");
				input = userInput.nextLine().trim();
				while (input.length()==0) {
					System.out.println("Please try again.");
					System.out.println("Enter a movie title to analyze. You can also enter 'exit' or 'x' to leave, or 'genre' or 'g' to switch to analyzing by genre.");
					input = userInput.nextLine().trim();
				}
				if (input.equalsIgnoreCase("exit")||input.equalsIgnoreCase("x")) {
					goAgain = false;
				}else if (input.equalsIgnoreCase("genre")||input.equalsIgnoreCase("g")) 
					menuOption = 2;
				else 
					performAction(moviesList, userInput, menuOption, input);
				//Choose genre option
			}else if (menuOption == 2) {
				System.out.println("Enter a genre to list all the movies of. You can also enter 'exit' or 'x' to leave, or 'movie' or 'm' to switch to analyzing a specific movie.");
				input = userInput.nextLine().trim();
				while (input.length()==0) {
					System.out.println("Please try again.");
					System.out.println("Enter a genre to list all the movies of. You can also enter 'exit' or 'x' to leave, or 'movie' or 'm' to switch to analyzing a specific movie.");
					input = userInput.nextLine().trim();
				}
				if (input.equalsIgnoreCase("exit")||input.equalsIgnoreCase("x"))
					goAgain = false;
				else if (input.equalsIgnoreCase("movie")||input.equalsIgnoreCase("m")) 
					menuOption = 1;
				else 
					performAction(moviesList, userInput, menuOption, input);
			}
		}
		userInput.close();
		System.out.println("Have a nice day!");
	}
}