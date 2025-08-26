# CS201 Spring 2025 – Final Project Documentation

**Team Members:**  
Abdullah Naim · Uzziel Avalos · Saubdiel Avalos · Renzo Larrea

---

## 🔍 Overview

This document provides a technical analysis of our group project built around the **Steam Games Complete Dataset**. Our focus was on designing and implementing a high-performance query engine that supports exact match lookups, range queries, top-K ranking, and statistical calculations. Below we detail our data structure decisions, complexity considerations, and how each method performs under expected use cases.

---

## Data Structure Justification

To support fast querying across different access patterns, we adopted the following structures:

- **`HashMap<String, HashMap<Date, List<SteamGame>>` (`gamesHashMap`)**  
  → Used for exact-match queries by tag and release year. Tags are normalized (lowercased and trimmed) to ensure consistency.  
  -Lookup is `O(1)` per tag, and `O(m)` for filtering by year from the inner map.

- **`HashMap<String, NavigableMap<Date, List<SteamGame>>` (`gamesTreeMap`)**  
  → Used for range queries over time and tag-based filtering. It declares a HashMap with String keys representing tags to a Navigable map (implemented as TreeMaps) whose keys are Dates and whose values are the SteamGame objects.
  -Lookup based on tags has `O(1)` runtime as it is a hashmap structure. From there, finding entries from a given range of Dates is has logarithmic runtime `O(log n)` as it finds the range from a NavigableMap (TreeMap), using efficient slicing with `subMap` or `headMap`. 

- **`List<SteamGame>` (`gamesArrayList`)**  
  → Retained for compatibility with sorting tasks (e.g., top-k by attribute) and general iteration.  
  -Sorting is performed via Java Streams and comparators on demand.

These structures allow us to tailor our implementation per method, balancing insertion cost at load time for optimal runtime querying.

---

## ⏱ Time & Space Complexity Analysis

| Method | Time Complexity | Space Complexity | Notes |
|--------|------------------|------------------|-------|
| `loadDataset()` | O(n·t) | O(n·t) | `t` = average number of tags per game |
| `findGamesByExactQuery()` | O(1) + O(m) | — | Fast lookup, linear scan over inner map |
| `findGamesByReleaseDate(tag, lower, upper)` | O(log n + m) | — | TreeMap gives fast range scan |
| `calculateAverageGamePrice(start, end)` | O(k·log n + k·m) | — | Aggregate over all tag-specific maps |
| `findTopGamesByAttribute(attr, k)` | O(n·log n) | — | Sort entire array once per call |

- `n`: total number of games (~40K)
- `t`: average number of tags per game (≈ 8–12)
- `m`: number of games under a specific tag
- `k`: number of top items requested

---

## Performance Comparison Summary

| Use Case | Data Structure | Rationale |
|----------|----------------|-----------|
| Year + Tag Exact Match | `HashMap → HashMap` | Quick direct lookup, avoids full list scan |
| Release Date Range Query | `HashMap → TreeMap` | Efficient time slicing using `subMap()` |
| Top-K Games by Attribute | `ArrayList` + Sort | One-time sorting with Java’s efficient comparator system |
| Average Price by Time/Tag | `TreeMap` traversal | Supports fast windowed aggregation |

Compared to an ArrayList-only implementation, our solution reduced:
- **Query time for exact matches** from O(n) → O(1) + O(m)
- **Range filters** from O(n) → O(log n + m)
- **Repeated sorting** by decoupling storage from search logic

---

## Limitations & Future Improvements

- **Memory Usage:**  
  Because each game may appear under multiple tag keys, our maps duplicate references, increasing space usage. However, since they point to the same `SteamGame` objects, duplication is limited to references.

- **Tag Normalization Only at Load Time:**  
  Tags must be correctly normalized at dataset load time. Errors or inconsistencies in tag cleaning can cause mismatches. Input sanitation is critical.

- **Static Dataset Assumption:**  
  Current implementation assumes a static dataset. For dynamic systems (e.g., live updates or deletions), we’d need to implement cache invalidation or more robust update logic.

- **Review Score Extraction Heuristic:**  
  The review metric parsing from `all_reviews` is ad hoc and fragile (dependent on string formatting). A more structured metric (e.g., separate `score` and `count` fields) would be preferred in production.

- **Parallelism:**  
  All operations are currently single-threaded. Future versions could explore parallel sorting or aggregation using Java’s `parallelStream()` or fork/join frameworks.

---

## Testing Summary

We created **comprehensive JUnit tests** covering:
- Valid and invalid exact matches
- Case-insensitive and whitespace-tag normalization
- Date boundary conditions
- Average calculations with and without tag filters
- Top-k sorting correctness

This ensured that edge cases, correctness, and robustness were validated throughout development.

---

## Conclusion: Closing Thoughts

By choosing specialized data structures (`HashMap`, `TreeMap`, `ArrayList`), we achieved highly efficient querying across multiple dimensions. Our design separates responsibilities for filtering, sorting, and aggregation while maintaining clean interface boundaries.

This project demonstrated how foundational CS structures translate directly into real-world performance gains — and how clean design choices scale with data size and complexity.

