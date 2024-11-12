//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    Backend - This class implements the BackendInterface, providing
//             functionalities for managing and retrieving song data from a
//             sorted collection of songs.
// Course:   CS 400 Fall 2024
//
// Author:   [Khush Jain]
// Email:    [kjain39@wisc.edu]
// Lecturer: [Gary Dahl]
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// Persons:         NONE
// Online Sources:  NONE
//
///////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class implements the BackendInterface and provides methods for reading song
 * data from a CSV file, filtering songs by year and loudness, and retrieving
 * the top five most danceable songs from a sorted collection.
 */
public class Backend implements BackendInterface {
    private IterableSortedCollection<Song> tree; // Field to store songs
    private Integer loudnessThreshold = null; // To store the loudness filter
    private Integer lowYearBound = null; // To store low year filter
    private Integer highYearBound = null; // To store high year filter

    // Constructor
    public Backend(IterableSortedCollection<Song> tree) {
        this.tree = tree;
    }

    /**
     * Reads song data from a CSV file and inserts it into the tree.
     *
     * @param filename the name of the CSV file to read data from
     * @throws IOException if an error occurs while reading the file
     */
    @Override
    public void readData(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = new String[9]; // Capturing all attributes of the CSV file
                int index = 0;
                boolean inQuotes = false; // Track if we are inside quotes
                StringBuilder currentField = new StringBuilder();

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        inQuotes = !inQuotes; // Entry/exit of string with quotation marks
                    } else if (c == ',' && !inQuotes) {
                        // Outside quotes: comma separates fields
                        parts[index++] = currentField.toString().trim();
                        currentField.setLength(0); // Reset for the next field
                    } else {
                        currentField.append(c); // Add character to the current field
                    }
                }

                // Add the last field after loop
                if (currentField.length() > 0 && index < parts.length) {
                    parts[index++] = currentField.toString().trim();
                }

                // Check if we have the minimum number of required fields
                if (index >= 9) {
                    String title = parts[0];
                    String artist = parts[1];
                    String genres = parts[2];
                    int year = Integer.parseInt(parts[3]);
                    int bpm = Integer.parseInt(parts[4]);
                    int energy = Integer.parseInt(parts[5]);
                    int danceability = Integer.parseInt(parts[6]);
                    int loudness = Integer.parseInt(parts[7]);
                    int liveness = Integer.parseInt(parts[8]);

                    // Create the new Song object and add it to our tree
                    Song song = new Song(title, artist, genres, year, bpm, energy, danceability,
                        loudness, liveness);
                    tree.insert(song); // Safely placed in the catalog
                }
            }
        }
    }

    /**
     * Retrieves a list of song titles within the specified year range
     * and below the loudness threshold.
     *
     * @param low  the lower bound of the year range (inclusive)
     * @param high the upper bound of the year range (inclusive)
     * @return a list of song titles that match the criteria
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        List<String> titles = new ArrayList<>();
        // Retrieve songs from the tree that match the year range
        for (Song song : tree) { // Assuming tree is iterable
            int year = song.getYear(); // Assuming getYear() method exists
            if ((low == null || year >= low) && (high == null || year <= high)) {
                // Apply loudness filter if set
                if (loudnessThreshold == null || song.getLoudness() < loudnessThreshold) {
                    titles.add(song.getTitle()); // Assuming getTitle() exists
                }
            }
        }
        // Store the year bounds for use in other methods
        this.lowYearBound = low;
        this.highYearBound = high;
        return titles;
    }

    /**
     * Sets the loudness filter and returns the list of song titles that
     * match the current year range and the new loudness threshold.
     *
     * @param threshold the loudness threshold to set
     * @return a list of song titles that match the criteria
     */
    @Override
    public List<String> setFilter(Integer threshold) {
        this.loudnessThreshold = threshold; // Set loudness filter
        return getRange(lowYearBound, highYearBound); // Reapply the range with the new filter
    }

    /**
     * Retrieves the top five most danceable songs based on the current
     * year range and loudness threshold.
     *
     * @return a list of titles of the top five most danceable songs
     */
    @Override
    public List<String> fiveMost() {
        List<Song> filteredSongs = new ArrayList<>();
        List<String> mostDanceable = new ArrayList<>();

        // Retrieve songs that match the current range and filter
        for (Song song : tree) {
            int year = song.getYear();
            if ((lowYearBound == null || year >= lowYearBound) && (highYearBound == null || year <= highYearBound)) {
                if (loudnessThreshold == null || song.getLoudness() < loudnessThreshold) {
                    filteredSongs.add(song);
                }
            }
        }

        // Sort by danceability (assuming getDanceability() method exists)
        filteredSongs.sort((s1, s2) -> Integer.compare(s2.getDanceability(), s1.getDanceability()));

        // Return the top five most danceable songs
        for (int i = 0; i < Math.min(5, filteredSongs.size()); i++) {
            mostDanceable.add(filteredSongs.get(i).getTitle());
        }

        return mostDanceable;
    }
}


