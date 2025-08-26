# CS201 Final Project

**Team Members:**  
Abdullah Naim · Uzziel Avalos · Saubdiel Avalos · Renzo Larrea

---

## Chosen Dataset

**Steam Games Complete Dataset**  
https://www.kaggle.com/datasets/trolukovich/steam-games-complete-dataset

- **Size:** 40,833 rows  
- **Total Attributes:** 20  
- **Selected Attributes (5):**  
  - `name`  
  - `release_date`  
  - `original_price`    
  - `popular_tags`  
  - `all_reviews`  

---

## Rationale for Selection

This Steam Games dataset provides a diverse mix of data types—text, numeric, categorical, lists, and dates—that let us illustrate a wide range of querying and indexing strategies:

- **Exact‑match queries** on computed fields (release year + tag)  
- **Range filters** on dates (`release_date`)  
- **Range filters** on numeric values (`original_price`)  
- **Full‑text search** in `all_reviews`  
- **Aggregate computations** over date ranges  
- **Top‑K retrieval** by price, review sentiment, or recency  

Its moderate size (~40 K records) ensures responsive development and clear performance comparisons.

---

## Planned Query Types

1. **loadDataset(String filePath)**  
   Load the CSV from `filePath` into memory and return the number of records loaded (throws `IOException` on read errors).

2. **findGamesByExactQuery(Date year, String tag)**  
   Return all games released in the specified **year** that include the given **tag**.

3. **findGamesByReleaseDate(String tag, Date lowerBound, Comparable<Date> upperBound)**  
   Return all games with the specified **tag** released between `lowerBound` and `upperBound` (inclusive).

4. **findGamesByReleaseDate(String tag, Date upperBound)**  
   Return all games with the specified **tag** released on or before `upperBound`.

5. **calculateAverageGamePrice(String price, Date startTime, Date endTime)**  
   Compute the average `original_price` of games released between `startTime` and `endTime`.

6. **findTopGamesByAttribute(String attribute, int k)**  
   Return the top *k* games sorted descending by:
   - `"original_price"` → highest prices  
   - `"all_reviews"` → most positive reviews  
   - `"release_date"` → most recent releases  
