import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Interface for querying a dataset of Steam games.
 *
 * <p>Provides methods to load the dataset and query it by various attributes such as release date,
 * price, and tags.
 */
public interface SteamGamesDatasetQuery {

    /**
     * Loads the Steam games dataset from the specified CSV file.
     *
     * @param filePath the path to the CSV dataset file
     * @return the number of records loaded
     * @throws IOException if an error occurs while reading the file
     */
    int loadDataset(String filePath) throws IOException;

    /**
     * Returns all games that exactly match the specified release year and tag.
     *
     * @param year the release year to match
     * @param tag the tag to match (case-insensitive)
     * @return a list of game names that match both the year and tag
     */
    List<String> findGamesByExactQuery(Date year, String tag);

    /**
     * Returns all games that match the specified tag and have a release date
     * between the given lower and upper bounds (inclusive).
     *
     * @param tag the tag to match
     * @param lowerBound the start of the release date range (inclusive)
     * @param upperBound the end of the release date range (inclusive)
     * @return a list of game names that match the tag and fall within the date range
     */
    List<String> findGamesByReleaseDate(String tag, Date lowerBound, Date upperBound);

    /**
     * Returns all games that match the specified tag and have a release date
     * before or equal to the given upper bound.
     *
     * @param tag the tag to match
     * @param upperBound the upper bound of the release date range (inclusive)
     * @return a list of game names that match the tag and were released on or before the upper bound
     */
    List<String> findGamesByReleaseDate(String tag, Date upperBound);

    /**
     * Calculates the average price of all games released between the specified start and end dates.
     *
     * @param startTime the start date of the range (inclusive)
     * @param endTime the end date of the range (inclusive)
     * @return a list of strings representing the average price(s) or summary statistics
     */
    List<String> calculateAverageGamePrice(Date startTime, Date endTime);

    /**
     * Calculates the average price of games that match the specified tag and were released
     * between the given start and end dates.
     *
     * @param startTime the start date of the range (inclusive)
     * @param endTime the end date of the range (inclusive)
     * @param tag the tag to filter games by
     * @return a list of strings representing the average price(s) or summary statistics
     */
    List<String> calculateAverageGamePrice(Date startTime, Date endTime, String tag);

    /**
     * Returns the top {@code k} games sorted by the specified attribute.
     *
     * <p>Supported attributes:
     * <ul>
     *   <li>{@code "original_price"} – returns games with the highest prices</li>
     *   <li>{@code "all_reviews"} – returns games with the most positive reviews</li>
     *   <li>{@code "release_date"} – returns the most recently released games</li>
     *   <li>{@code "name"} – returns alphabetically lowest game names</li>
     * </ul>
     *
     * @param attribute the attribute to sort by
     * @param k the number of top games to return
     * @return a list of {@link SteamGame} objects representing the top {@code k} games
     */
    List<SteamGame> findTopGamesByAttribute(String attribute, int k);
}