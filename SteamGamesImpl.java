import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implementation of the SteamGamesDatasetQuery interface that loads and processes
 * Steam game data from a CSV file. The class supports querying games by release date,
 * tags, price, and other attributes.
 */
public class SteamGamesImpl implements SteamGamesDatasetQuery {

    private Map<String, NavigableMap<Date, List<SteamGame>>> gamesTreeMap = new HashMap<>();
    private Map<String, HashMap<Date, List<SteamGame>>> gamesHashMap = new HashMap<>();
    private List<SteamGame> gamesArrayList = new ArrayList<>();

    /** Default constructor initializing internal data structures. */
    public SteamGamesImpl() {
        gamesTreeMap = new HashMap<>();
        gamesHashMap = new HashMap<>();
        gamesArrayList = new ArrayList<>();
    }

    /**
     * Constructs the implementation using a provided list of SteamGame objects.
     *
     * @param data The list of SteamGame instances to populate the dataset.
     */
    public SteamGamesImpl(List<SteamGame> data) {
        gamesTreeMap = new HashMap<>();
        gamesHashMap = new HashMap<>();
        gamesArrayList = data;

        for (SteamGame game : data) {
            String[] tags = game.getPopularTags().split(",");
            Date releaseDate = game.getReleaseDate();

            for (String tag : tags) {
                tag = tag.trim().toLowerCase();

                // TreeMap population
                gamesTreeMap
                        .computeIfAbsent(tag, k -> new TreeMap<>())
                        .computeIfAbsent(releaseDate, k -> new ArrayList<>())
                        .add(game);

                // HashMap population
                gamesHashMap
                        .computeIfAbsent(tag, k -> new HashMap<>())
                        .computeIfAbsent(releaseDate, k -> new ArrayList<>())
                        .add(game);
            }
        }
    }

    /**
     * Loads and parses a CSV dataset from the provided file path.
     *
     * @param csvFile The path to the CSV file.
     * @return The number of valid SteamGame entries loaded.
     */
    @Override
    public int loadDataset(String csvFile) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header

            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

            while ((line = br.readLine()) != null) {
                List<String> values = parseLine(line);

                if (values.size() < 6) {
                    System.out.println("Skipping malformed row: " + values);
                    continue;
                }

                Date releaseDate = null;
                if (!values.get(3).equalsIgnoreCase("NaN")) {
                    try {
                        releaseDate = format.parse(values.get(3));
                    } catch (ParseException e) {
                        System.out.println("Invalid date format for game '" + values.get(1) + "': " + e.getMessage());
                    }
                }

                double originalPrice = parsePrice(values.get(5));

                if (releaseDate != null) {
                    // Only add valid entries to the main list
                    SteamGame tmp = new SteamGame(values.get(1), releaseDate, values.get(2), originalPrice, values.get(4));
                    gamesArrayList.add(tmp);
                }
            }

        } catch (IOException e) {
            // Silently ignore for now
        }

        // Populate lookup maps after loading all data
        for (SteamGame game : gamesArrayList) {
            String[] tags = game.getPopularTags().split(",");
            Date releaseDate = game.getReleaseDate();

            for (String tag : tags) {
                String normalizedTag = tag.trim().toLowerCase();

                // TreeMap: used for range queries
                gamesTreeMap
                        .computeIfAbsent(normalizedTag, k -> new TreeMap<>())
                        .computeIfAbsent(releaseDate, k -> new ArrayList<>())
                        .add(game);

                // HashMap: used for exact year queries
                gamesHashMap
                        .computeIfAbsent(normalizedTag, k -> new HashMap<>())
                        .computeIfAbsent(releaseDate, k -> new ArrayList<>())
                        .add(game);
            }
        }
        return gamesArrayList.size();
    }

    /**
     * Parses a line from a CSV file, handling quoted fields.
     *
     * @param line The line to parse.
     * @return A list of parsed fields.
     */
    public List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        // Custom CSV parsing that supports quoted fields and escaped quotes
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"'); // Escaped quote
                    i++;
                } else {
                    inQuotes = !inQuotes; // Toggle quote state
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        fields.add(current.toString());
        return fields;
    }

    /**
     * Parses a price string and returns a numeric value.
     *
     * @param priceStr The string representing the price.
     * @return The parsed price as a double.
     */
    private double parsePrice(String priceStr) {
        // Normalize and convert string prices like "$29.99", "Free", or "NaN"
        if (priceStr == null || priceStr.trim().isEmpty() || priceStr.toLowerCase().contains("free")
                || priceStr.equalsIgnoreCase("NaN")) {
            return 0.0;
        }

        try {
            return Double.parseDouble(priceStr.replace("$", "").trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format: " + priceStr);
            return 0.0;
        }
    }

    /**
     * Finds games that exactly match the year and tag provided.
     *
     * @param yearParam The year to match.
     * @param tag The tag to search.
     * @return A list of matching game names.
     */
    @Override
    public List<String> findGamesByExactQuery(Date yearParam, String tag) {
        if (yearParam == null || tag == null || tag.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Extract the year component from the date
        String normalizedTag = tag.toLowerCase().trim();
        Calendar cal = Calendar.getInstance();
        cal.setTime(yearParam);
        int targetYear = cal.get(Calendar.YEAR);

        HashMap<Date, List<SteamGame>> tagMap = gamesHashMap.get(normalizedTag);
        if (tagMap == null) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();

        for (Map.Entry<Date, List<SteamGame>> entry : tagMap.entrySet()) {
            Date releaseDate = entry.getKey();
            if (releaseDate == null) {
                continue;
            }

            cal.setTime(releaseDate);
            int gameYear = cal.get(Calendar.YEAR);
            if (gameYear == targetYear) {
                for (SteamGame game : entry.getValue()) {
                    result.add(game.getName());
                }
            }
        }

        return result;
    }

    /**
     * Finds games with the given tag released between two dates (inclusive).
     *
     * @param tag The tag to search.
     * @param lowerBound The start date (inclusive).
     * @param upperBound The end date (inclusive).
     * @return A list of matching game names.
     */
    @Override
    public List<String> findGamesByReleaseDate(String tag, Date lowerBound, Date upperBound) {
        if (tag == null || tag.isBlank() || lowerBound == null || upperBound == null) {
            return Collections.emptyList();
        }

        if (lowerBound.after(upperBound)) {
            return Collections.emptyList();
        }

        String normalizedTag = tag.trim().toLowerCase();
        NavigableMap<Date, List<SteamGame>> dateMap = gamesTreeMap.get(normalizedTag);
        if (dateMap == null) {
            return Collections.emptyList();
        }

        // Use subMap for efficient date range filtering
        SortedMap<Date, List<SteamGame>> sub = dateMap.subMap(lowerBound, true, upperBound, true);

        List<String> result = new ArrayList<>();
        for (List<SteamGame> bucket : sub.values()) {
            for (SteamGame g : bucket) {
                result.add(g.getName());
            }
        }
        return result;
    }

    /**
     * Finds games with the given tag released on or before the specified date.
     *
     * @param tag The tag to search.
     * @param upperBound The upper date limit (inclusive).
     * @return A list of matching game names.
     */
    @Override
    public List<String> findGamesByReleaseDate(String tag, Date upperBound) {
        if (tag == null || tag.isBlank() || upperBound == null) {
            return Collections.emptyList();
        }

        String normalizedTag = tag.trim().toLowerCase();
        NavigableMap<Date, List<SteamGame>> dateMap = gamesTreeMap.get(normalizedTag);
        if (dateMap == null) {
            return Collections.emptyList();
        }

        // Use headMap to get all games released before or on the upper bound
        SortedMap<Date, List<SteamGame>> sub = dateMap.headMap(upperBound, true);

        List<String> result = new ArrayList<>();
        for (List<SteamGame> bucket : sub.values()) {
            for (SteamGame g : bucket) {
                result.add(g.getName());
            }
        }
        return result;
    }

    /**
     * Finds the top-k games sorted by the specified attribute.
     *
     * @param attribute The attribute to sort by ("original_price", "release_date", "name", "all_reviews").
     * @param k The number of top games to return.
     * @return A list of top SteamGame objects.
     */
    @Override
    public List<SteamGame> findTopGamesByAttribute(String attribute, int k) {
        Comparator<SteamGame> comparator;

        // Dynamically choose a comparator based on attribute
        switch (attribute.toLowerCase()) {
            case "original_price" ->
                comparator = Comparator.comparingDouble(SteamGame::getOriginalPrice).reversed();
            case "release_date" ->
                comparator = Comparator.comparing(SteamGame::getReleaseDate).reversed();
            case "name" ->
                comparator = Comparator.comparing(SteamGame::getName);
            case "all_reviews" ->
                comparator = Comparator.comparingDouble(this::extractReviewScore).reversed();
            default -> {
                System.out.println("Unsupported attribute: " + attribute);
                return List.of();
            }
        }

        // Use stream to get top-k elements based on selected comparator
        return gamesArrayList.stream()
                .sorted(comparator)
                .limit(k)
                .toList();
    }

    /**
     * Parses the 'all_reviews' field to extract a numerical score.
     *
     * @param game The SteamGame object.
     * @return The extracted score as a double.
     */
    private double extractReviewScore(SteamGame game) {
        try {
            String reviewField = game.getAllReviews();
            if (reviewField == null || reviewField.isEmpty()) {
                return 0;
            }
    
            // Return 0 if the % symbol is missing
            int percentIndex = reviewField.indexOf('%');
            if (percentIndex == -1) {
                return 0;
            }
    
            // Extract review percentage
            int percentStart = reviewField.lastIndexOf(' ', percentIndex - 1) + 1;
            String percentStr = reviewField.substring(percentStart, percentIndex).trim();
            int percent = Integer.parseInt(percentStr);
    
            // Extract review count (inside parentheses)    
            int open = reviewField.indexOf('(');
            int close = reviewField.indexOf(')');
            if (open == -1 || close == -1 || close <= open) {
                return percent; // If count missing, return just the percent
            }
    
            String countStr = reviewField.substring(open + 1, close).replace(",", "").trim();
            int count = Integer.parseInt(countStr);
    
            // Combine both metrics into a score
            return percent + count / 1000.0;
    
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Calculates the average price of games within a tag and date range.
     *
     * @param startTime The start date.
     * @param endTime The end date.
     * @param tag The tag to filter by.
     * @return A list containing one string with the formatted average price.
     */
    @Override
    public List<String> calculateAverageGamePrice(Date startTime, Date endTime, String tag) {
        tag = tag.toLowerCase();
        if (startTime == null || endTime == null || tag == null) {
            throw new NullPointerException("Start time, end time, or tag is null.");
        }

        if (startTime.after(endTime)) {
            return Collections.emptyList();
        }

        if (!gamesTreeMap.containsKey(tag)) {
            return Collections.emptyList();
        }

        NavigableMap<Date, List<SteamGame>> dateMap = gamesTreeMap.get(tag);
        SortedMap<Date, List<SteamGame>> gamesInRange = dateMap.subMap(startTime, true, endTime, true);

        double totalPrice = 0.0;
        int count = 0;

        // Aggregate price and count to compute average
        for (List<SteamGame> gameList : gamesInRange.values()) {
            for (SteamGame game : gameList) {
                totalPrice += game.getOriginalPrice();
                count++;
            }
        }

        if (count == 0) {
            return Collections.emptyList();
        }

        double average = totalPrice / count;
        return List.of(String.format("%.2f", average));
    }

    /**
     * Calculates the average price of all games released within a date range.
     *
     * @param startTime The start date.
     * @param endTime The end date.
     * @return A list containing one string with the formatted average price.
     */
    @Override
    public List<String> calculateAverageGamePrice(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            throw new NullPointerException("Start time or end time is null.");
        }

        if (startTime.after(endTime)) {
            return Collections.emptyList();
        }

        if (gamesTreeMap == null || gamesTreeMap.isEmpty()) {
            return Collections.emptyList();
        }

        double totalPrice = 0.0;
        int count = 0;

        // Iterate through all tags to include all games in date range
        for (NavigableMap<Date, List<SteamGame>> dateMap : gamesTreeMap.values()) {
            SortedMap<Date, List<SteamGame>> gamesInRange = dateMap.subMap(startTime, true, endTime, true);

            for (List<SteamGame> gameList : gamesInRange.values()) {
                for (SteamGame game : gameList) {
                    totalPrice += game.getOriginalPrice();
                    count++;
                }
            }
        }

        if (count == 0) {
            return Collections.emptyList();
        }

        double average = totalPrice / count;
        return List.of(String.format("%.2f", average));
    }
}