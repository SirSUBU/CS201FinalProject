
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Unit tests for the {@link SteamGamesImpl} class which implements
 * {@link SteamGamesDatasetQuery}. These tests validate functionality for
 * filtering, sorting, and computing statistics on Steam game data.
 *
 * <p>
 * Author: Abdullah Naim
 */
public class SteamGamesDataQueryTest {

    private SteamGamesDatasetQuery queryEngine;
    private List<SteamGame> testGames;

    private final SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    /**
     * Initializes test data with a variety of Steam games for testing filtering
     * and sorting behavior.
     */
    @Before
    public void setUp() throws Exception {
        testGames = new ArrayList<>();

        testGames.add(new SteamGame("Counter-Strike: Global Offensive",
                format.parse("Aug 21, 2012"),
                "Very Positive,(3,094,223),- 86% of the 3,094,223 user reviews for this game are positive.",
                0.00,
                "FPS,Shooter,Multiplayer,Competitive,Action,Team-Based,e-sports,Tactical,First-Person,PvP,Online Co-Op,Co-op,Strategy,Military,War,Difficult,Trading,Fast-Paced,Realistic,Moddable"));

        testGames.add(new SteamGame("Dota 2",
                format.parse("Jul 09, 2013"),
                "Very Positive,(1,015,621),- 85% of the 1,015,621 user reviews for this game are positive.",
                0.00,
                "Free to Play,MOBA,Multiplayer,Strategy,e-sports,Team-Based,Competitive,Action,Online Co-Op,PvP,Difficult,Co-op,RTS,Tower Defense,Fantasy,RPG,Character Customization,Replay Value,Action RPG,Simulation"));

        testGames.add(new SteamGame("PLAYERUNKNOWN'S BATTLEGROUNDS",
                format.parse("Dec 21, 2017"),
                "Mixed,(836,608),- 49% of the 836,608 user reviews for this game are positive.",
                29.99,
                "Survival,Shooter,Multiplayer,Battle Royale,PvP,FPS,Third-Person Shooter,Action,Online Co-Op,Tactical,Co-op,First-Person,Early Access,Strategy,Competitive,Third Person,Team-Based,Difficult,Simulation,Stealth"));

        testGames.add(new SteamGame("Team Fortress 2",
                format.parse("Oct 10, 2007"),
                "Very Positive,(553,458),- 93% of the 553,458 user reviews for this game are positive.",
                0.00,
                "Free to Play,Multiplayer,FPS,Shooter,Action,Class-Based,Team-Based,Funny,First-Person,Online Co-Op,Competitive,Cartoony,Trading,Co-op,Comedy,Robots,Tactical,Cartoon,Crafting,Moddable"));

        testGames.add(new SteamGame("Grand Theft Auto V",
                format.parse("Apr 14, 2015"),
                "Mostly Positive,(407,706),- 70% of the 407,706 user reviews for this game are positive.",
                29.99,
                "Open World,Action,Multiplayer,Third Person,First-Person,Crime,Shooter,Adventure,Third-Person Shooter,Singleplayer,Mature,Racing,Atmospheric,Co-op,Sandbox,Funny,Great Soundtrack,Comedy,Masterpiece,Moddable"));

        testGames.add(new SteamGame("Unturned",
                format.parse("Jul 07, 2017"),
                "Very Positive,(325,675),- 90% of the 325,675 user reviews for this game are positive.",
                0.00,
                "Free to Play,Survival,Zombies,Multiplayer,Open World,Adventure,Crafting,Action,First-Person,Co-op,Sandbox,Shooter,Post-apocalyptic,FPS,Singleplayer,Massively Multiplayer,Indie,Atmospheric,Casual,Early Access"));

        testGames.add(new SteamGame("Garry's Mod",
                format.parse("Nov 29, 2006"),
                "Overwhelmingly Positive,(310,394),- 95% of the 310,394 user reviews for this game are positive.",
                9.99,
                "Sandbox,Multiplayer,Funny,Moddable,Building,Comedy,Co-op,First-Person,Mod,Simulation,Physics,Online Co-Op,FPS,Singleplayer,Action,Shooter,Animation & Modeling,Indie,Massively Multiplayer,Adventure"));

        testGames.add(new SteamGame("Strata Spaces VR – Professional Edition Upgrade",
                format.parse("Oct 26, 2017"),
                "", 995.00,
                "Design & Illustration,Animation & Modeling"));

        testGames.add(new SteamGame("Crankies Workshop: Bozzbot Assembly",
                format.parse("Sep 13, 2017"),
                "Mixed,(28),- 46% of the 28 user reviews for this game are positive.",
                624.74,
                "Casual,Indie,Simulation,Strategy,Action,Funny,Singleplayer,2D,Atmospheric,Colorful,Cartoony,Puzzle,Cartoon,Sequel,Family Friendly,Replay Value,Comedy"));

        testGames.add(new SteamGame("Crankies Workshop: Grizzbot Assembly",
                format.parse("Sep 12, 2017"),
                "Mixed,(22),- 68% of the 22 user reviews for this game are positive.",
                624.74,
                "Casual,Indie,Strategy,Simulation,Action,Singleplayer,2D,Atmospheric,Cartoony,Colorful,Funny,Puzzle,Family Friendly,Choices Matter,Text-Based,Real-Time,Time Management"));

        queryEngine = new SteamGamesImpl(testGames);
    }

    /**
     * Prints the list of test games to the console for manual inspection.
     */
    @Test
    public void testPrintGames() {
        for (SteamGame game : testGames) {
            System.out.println(game);
        }
    }

    /**
     * Tests the {@code findTopGamesByAttribute} method for "all_reviews".
     */
    @Test
    public void testTopGameByReviews() {
        SteamGame topByReviews = queryEngine.findTopGamesByAttribute("all_reviews", 1).get(0);
        assertEquals("Counter-Strike: Global Offensive", topByReviews.getName());
    }

    /**
     * Tests the {@code findTopGamesByAttribute} method for "original_price".
     */
    @Test
    public void testTopGameByPrice() {
        SteamGame topByPrice = queryEngine.findTopGamesByAttribute("original_price", 1).get(0);
        assertEquals("Strata Spaces VR – Professional Edition Upgrade", topByPrice.getName());
    }

    /**
     * Tests the {@code findTopGamesByAttribute} method for "name".
     */
    @Test
    public void testTopGameByName() {
        SteamGame topByName = queryEngine.findTopGamesByAttribute("name", 1).get(0);
        assertEquals("Counter-Strike: Global Offensive", topByName.getName());
    }

    /**
     * Tests the {@code findTopGamesByAttribute} method for "release_date".
     */
    @Test
    public void testTopGameByReleaseDate() {
        SteamGame topByDate = queryEngine.findTopGamesByAttribute("release_date", 1).get(0);
        assertEquals("PLAYERUNKNOWN'S BATTLEGROUNDS", topByDate.getName());
    }

    /**
     * Tests finding games that exactly match the year and tag.
     */
    @Test
    public void testExactMatch_YearAndTagMatch() throws Exception {
        Date year2017 = format.parse("Jan 01, 2017");

        List<String> results = queryEngine.findGamesByExactQuery(year2017, "Survival");
        assertEquals(2, results.size());
        assertTrue(results.contains("PLAYERUNKNOWN'S BATTLEGROUNDS"));
        assertTrue(results.contains("Unturned"));
    }

    /**
     * Tests that tag matching is case-insensitive.
     */
    @Test
    public void testExactMatch_CaseInsensitiveMatch() throws Exception {
        Date year2013 = format.parse("Jan 01, 2013");

        List<String> results = queryEngine.findGamesByExactQuery(year2013, "mObA");
        assertEquals(1, results.size());
        assertEquals("Dota 2", results.get(0));
    }

    /**
     * Tests that no games are returned when there are no matches.
     */
    @Test
    public void testExactMatch_NoMatches() throws Exception {
        Date year2017 = format.parse("Jan 01, 2017");

        List<String> results = queryEngine.findGamesByExactQuery(year2017, "RPG");
        assertTrue(results.isEmpty());
    }

    /**
     * Tests that an empty tag returns no results.
     */
    @Test
    public void testExactMatch_EmptyTag() throws Exception {
        Date year2017 = format.parse("Jan 01, 2017");

        List<String> results = queryEngine.findGamesByExactQuery(year2017, "");
        assertTrue(results.isEmpty());
    }

    /**
     * Tests that a null tag returns no results.
     */
    @Test
    public void testExactMatch_NullTag() throws Exception {
        Date year2017 = format.parse("Jan 01, 2017");

        List<String> results = queryEngine.findGamesByExactQuery(year2017, null);
        assertTrue(results.isEmpty());
    }

    /**
     * Tests that games with null release dates are ignored during matching.
     */
    @Test
    public void testExactMatch_ReleaseDateNullIgnored() throws Exception {
        Date year2020 = format.parse("Jan 01, 2020");

        testGames.add(new SteamGame("Test Game", null, "Mixed", 0.0, "Action"));
        List<String> results = queryEngine.findGamesByExactQuery(year2020, "Action");
        assertFalse(results.contains("Test Game"));
    }

    /**
     * Tests average price calculation for a valid date range.
     */
    @Test
    public void testAverageGamePrice_withValidDateRange() throws Exception {
        Date start = format.parse("Jan 01, 2013");
        Date end = format.parse("Jan 01, 2018");

        List<String> result = queryEngine.calculateAverageGamePrice(start, end);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        // Optionally print or check actual average values
        System.out.println("Average price result: " + result);
    }

    /**
     * Tests average price when no games are in the specified date range.
     */
    @Test
    public void testAverageGamePrice_withNoGamesInRange() throws Exception {
        Date start = format.parse("Jan 01, 1980");
        Date end = format.parse("Jan 01, 1985");

        List<String> result = queryEngine.calculateAverageGamePrice(start, end);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests average price with tag filtering.
     */
    @Test
    public void testAverageGamePrice_withTagFilter() throws Exception {
        Date start = format.parse("Jan 01, 2013");
        Date end = format.parse("Jan 01, 2020");
        String tag = "Shooter";

        List<String> result = queryEngine.calculateAverageGamePrice(start, end, tag);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        System.out.println("Average price with tag 'Shooter': " + result);
    }

    /**
     * Tests tag filtering when no games match.
     */
    @Test
    public void testAverageGamePrice_withTagButNoMatches() throws Exception {
        Date start = format.parse("Jan 01, 2013");
        Date end = format.parse("Jan 01, 2020");
        String tag = "NonExistentGenre";

        List<String> result = queryEngine.calculateAverageGamePrice(start, end, tag);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests null date parameters.
     */
    @Test(expected = NullPointerException.class)
    public void testAverageGamePrice_withNullDates() {
        queryEngine.calculateAverageGamePrice(null, null);
    }

    /**
     * Tests reversed date range returns empty or is handled gracefully.
     */
    @Test
    public void testAverageGamePrice_withStartDateAfterEndDate() throws Exception {
        Date start = format.parse("Jan 01, 2025");
        Date end = format.parse("Jan 01, 2020");

        List<String> result = queryEngine.calculateAverageGamePrice(start, end);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

        @Test
        public void testRangeQuery_InclusiveBounds() throws Exception {
            Date lower = format.parse("Jan 01, 2017");
            Date upper = format.parse("Dec 31, 2017");

            List<String> results = queryEngine.findGamesByReleaseDate("Survival", lower, upper);
            // Should find exactly PUBG and Unturned
            assertEquals(2, results.size());
            assertTrue(results.containsAll(Arrays.asList(
                    "PLAYERUNKNOWN'S BATTLEGROUNDS",
                    "Unturned"
            )));
        }

        @Test
        public void testRangeQuery_CaseInsensitiveTag() throws Exception {
            Date lower = format.parse("Jan 01, 2017");
            Date upper = format.parse("Dec 31, 2017");

            // Upper/lower same, but tag in different case
            List<String> results = queryEngine.findGamesByReleaseDate("sUrViVaL", lower, upper);
            assertEquals(2, results.size());
        }

        @Test
        public void testRangeQuery_SingleDayMatch() throws Exception {
            // Lower == upper == Dec 21, 2017 → only PUBG
            Date single = format.parse("Dec 21, 2017");
            List<String> results = queryEngine.findGamesByReleaseDate("Survival", single, single);
            assertEquals(1, results.size());
            assertEquals("PLAYERUNKNOWN'S BATTLEGROUNDS", results.get(0));
        }

        @Test
        public void testRangeQuery_LowerAfterUpper_YieldsEmpty() throws Exception {
            Date lower = format.parse("Jan 01, 2018");
            Date upper = format.parse("Dec 31, 2017");
            List<String> results = queryEngine.findGamesByReleaseDate("Survival", lower, upper);
            assertTrue(results.isEmpty());
        }

        @Test
        public void testRangeQuery_UnknownTag_YieldsEmpty() throws Exception {
            Date lower = format.parse("Jan 01, 2012");
            Date upper = format.parse("Dec 31, 2020");
            assertTrue(queryEngine.findGamesByReleaseDate("Nonexistent", lower, upper).isEmpty());
        }

        @Test
        public void testRangeQuery_NullOrBlankInputs_YieldEmpty() throws Exception {
            Date date = format.parse("Dec 31, 2017");
            assertTrue(queryEngine.findGamesByReleaseDate(null, date, date).isEmpty());
            assertTrue(queryEngine.findGamesByReleaseDate("  ", date, date).isEmpty());
            assertTrue(queryEngine.findGamesByReleaseDate("Survival", null, date).isEmpty());
            assertTrue(queryEngine.findGamesByReleaseDate("Survival", date, null).isEmpty());
        }

        @Test
        public void testUpToQuery_InclusiveUpper() throws Exception {
            Date upper = format.parse("Jul 31, 2013");
            List<String> results = queryEngine.findGamesByReleaseDate("Multiplayer", upper);
            assertEquals(4, results.size());
            assertTrue(results.containsAll(Arrays.asList(
                "Garry's Mod", "Team Fortress 2", "Counter-Strike: Global Offensive", "Dota 2"
            )));
        }
        
        @Test
        public void testUpToQuery_CaseInsensitiveTag() throws Exception {
            Date upper = format.parse("Jul 31, 2013");
            List<String> results = queryEngine.findGamesByReleaseDate("MULTIPLAYER", upper);
            assertTrue(results.containsAll(Arrays.asList(
                "Garry's Mod", "Team Fortress 2", "Counter-Strike: Global Offensive", "Dota 2"
        )));
        }
        
        @Test
        public void testUpToQuery_NoMatchesBeforeDate() throws Exception {
            Date upper = format.parse("Dec 31, 2011");
            List<String> results = queryEngine.findGamesByReleaseDate("Multiplayer", upper);
            assertTrue(results.containsAll(Arrays.asList(
                "Garry's Mod",
                "Team Fortress 2"
        )));
        }
        @Test
        public void testUpToQuery_NullOrBlank_YieldEmpty() {
            // null tag or null upperBound
            assertTrue(queryEngine.findGamesByReleaseDate(null, new Date()).isEmpty());
            assertTrue(queryEngine.findGamesByReleaseDate("Action", null).isEmpty());
            assertTrue(queryEngine.findGamesByReleaseDate("   ", new Date()).isEmpty());
        }
    

}
