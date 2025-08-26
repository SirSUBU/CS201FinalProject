import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// Example calls to SteamGamesImpl()
public class Main {
    public static void main(String[] args) {
        SteamGamesImpl steam = new SteamGamesImpl();
        steam.loadDataset("data/steam_games.csv");

        // Define the date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date startTime = sdf.parse("2018-12-01");
            Date endTime = sdf.parse("2018-12-31");
            Date queryYear = sdf.parse("2018-01-01");
            Date upperBound = sdf.parse("2019-01-01");

            // calculateAverageGamePrice(Date, Date)
            List<String> avgPrice = steam.calculateAverageGamePrice(startTime, endTime);
            System.out.println("Average price (all tags): " + avgPrice);

            // calculateAverageGamePrice(Date, Date, String)
            List<String> avgPriceByTag = steam.calculateAverageGamePrice(startTime, endTime, "indie");
            System.out.println("Average price (tag: indie): " + avgPriceByTag);

            // findTopGamesByAttribute(String, int)
            System.out.println("Top 5 games by review score:");
            steam.findTopGamesByAttribute("all_reviews", 5).forEach(System.out::println);

            System.out.println("Top 3 games by original price:");
            steam.findTopGamesByAttribute("original_price", 3).forEach(System.out::println);

            System.out.println("Top 10 games by name:");
            steam.findTopGamesByAttribute("name", 10).forEach(System.out::println);

            System.out.println("Top 10 games by release date:");
            steam.findTopGamesByAttribute("release_date", 10).forEach(System.out::println);

            // findGamesByExactQuery(Date, String)
            System.out.println("Games with tag 'action' released in 2018:");
            steam.findGamesByExactQuery(queryYear, "action").forEach(System.out::println);

            // findGamesByReleaseDate(String, Date, Date)
            System.out.println("Games with tag 'strategy' released in Dec 2018:");
            steam.findGamesByReleaseDate("strategy", startTime, endTime).forEach(System.out::println);

            // findGamesByReleaseDate(String, Date)
            System.out.println("Games with tag 'indie' released before Jan 2019:");
            steam.findGamesByReleaseDate("indie", upperBound).forEach(System.out::println);

        } catch (ParseException e) {
            e.printStackTrace();  // Handle parsing errors
        }
    }
}