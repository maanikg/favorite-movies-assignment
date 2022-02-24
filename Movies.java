import java.util.*;

public class Movies implements Comparable <Movies>{
	//Data Encapsulation of variables
	private String title, genre;
	private double rating;
	private int rank;
	private static int totalNumMovies;

	//Initializing objects(depending on what information is known)
	public Movies(String movieTitle, double movieRating, String movieGenre) {
		title = movieTitle;
		rating = movieRating;
		genre = movieGenre;
		totalNumMovies++;
	}

	//This method has movieGenre as a parameter because overloading wouldn't work with just one string param(see below method).
	public Movies(String movieTitle, String movieGenre) {
		title = movieTitle;
		genre = movieGenre;
	}

	public Movies(String movieGenre) {
		genre = movieGenre;
	}

	//Formatting when outputing Movies
	public String toString() {
		String output = String.format("Movie Title: %s"
				+ "%nGenre: %s"
				+ "%nRating: %.2f%%"
				+ "%nDatabase Ranking: %d out of %d%n", title, genre, rating, rank, totalNumMovies);
		return output;
	}

	public int compareTo(Movies currentMovie) {
		return this.title.compareToIgnoreCase(currentMovie.title);
	}

	//Getter and Setter methods
	public double getRating() {
		return this.rating;
	}

	public String getGenre() {
		return this.genre;
	}

	public String getTitle() {
		return this.title;
	}

	public int getRank() {
		return this.rank;
	}
	
	public void setRank(int newRank) {
		rank = newRank;
	}
}

//Class for sorting by rating
class SortRating implements Comparator <Movies>{
	public int compare(Movies movieOne, Movies movieTwo) {
		return (int) (Math.round(movieTwo.getRating()*100)-Math.round(movieOne.getRating()*100));
	}
}

//Class for sorting by genre
class SortGenre implements Comparator <Movies>{
	public int compare(Movies movieOne, Movies movieTwo) {
		/*If both movies' genres are the same, try sorting alphabetically.
		 *If that doesn't work(which can happen when a dummy Movie is created without a title), both movies are sorted by genre alphabetically. 
		*/
		if (movieOne.getGenre().compareToIgnoreCase(movieTwo.getGenre())==0) 
			try {
				return movieOne.getTitle().compareToIgnoreCase(movieTwo.getTitle());
			}catch (NullPointerException e) {
				return movieOne.getGenre().compareToIgnoreCase(movieTwo.getGenre());
			}
		else //If both movies' genres aren't the same, sort by genre alphabetically
			return movieOne.getGenre().compareToIgnoreCase(movieTwo.getGenre());
	}
}