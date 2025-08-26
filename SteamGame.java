import java.util.Date;

/**
 * Represents a game listed on the Steam platform.
 * 
 * <p>This class stores details about a game such as its name, release date, 
 * original price, review summary, and popular tags.
 */
public class SteamGame {

    /** The name of the game. */
    private final String name;

    /** The release date of the game. */
    private final Date releaseDate;

    /** Summary of all reviews for the game (e.g., "Very Positive (10,000)"). */
    private final String allReviews;

    /** The original price of the game in USD. */
    private final double originalPrice;

    /** A comma-separated string of the game's popular tags. */
    private final String popularTags;

    /**
     * Constructs a new {@code SteamGame} instance with the given parameters.
     *
     * @param name the name of the game
     * @param release_date the release date of the game
     * @param all_reviews the all reviews field (e.g., "Mostly Positive (2,345)")
     * @param original_price the original price of the game
     * @param popular_tags comma-separated list of popular tags
     */
    public SteamGame(String name, Date release_date, String all_reviews, double original_price, String popular_tags) {
        this.name = name;
        this.releaseDate = release_date;
        this.allReviews = all_reviews;
        this.originalPrice = original_price;
        this.popularTags = popular_tags;
    }

    /**
     * Returns the name of the game.
     *
     * @return the game name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the release date of the game.
     *
     * @return the release date
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * Returns the summary of all reviews for the game.
     *
     * @return the all reviews string
     */
    public String getAllReviews() {
        return allReviews;
    }

    /**
     * Returns the original price of the game.
     *
     * @return the original price in USD
     */
    public double getOriginalPrice() {
        return originalPrice;
    }

    /**
     * Returns the popular tags associated with the game.
     *
     * @return comma-separated popular tags
     */
    public String getPopularTags() {
        return popularTags;
    }

    /**
     * Returns a string representation of the {@code SteamGame} object.
     *
     * @return a formatted string with game details
     */
    @Override
    public String toString() {
        return String.format(
                "SteamGame [\nName: %s\nRelease Date: %s\nPrice: $%.2f\nPopular Tags: %s\nAll Reviews: %s\n]",
                name, releaseDate.toString(), originalPrice, popularTags, allReviews);
    }
}