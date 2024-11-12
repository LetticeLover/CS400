import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Backend implements BackendInterface{
  
  private IterableSortedCollection<Song> tree;
  private Integer filter;
  private Integer low;
  private Integer high;
  //public Backend(IterableSortedCollection<Song> tree)
  // Your constructor must have the signature above. All methods below must
  // use the provided tree to store, sort, and iterate through songs. This
  // will enable you to create some tests that use the placeholder tree, and
  // others that make use of a working tree.
  public Backend(IterableSortedCollection<Song> tree) {
    this.tree = tree;
  }

  /**
   * Loads data from the .csv file referenced by filename.  You can rely
   * on the exact headers found in the provided songs.csv, but you should
   * not rely on them always being presented in this order or on there
   * not being additional columns describing other song qualities.
   * After reading songs from the file, the songs are inserted into
   * the tree passed to the constructor.
   * @param filename is the name of the csv file to load data from
   * @throws IOException when there is trouble finding/reading file
   */
  @Override
  public void readData(String filename) throws IOException {
    // TODO Auto-generated method stub
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      boolean isFirstLine = true;
      
      while ((line = br.readLine()) != null) {
        // skipping the header
        if (isFirstLine) {
          isFirstLine = false;
          continue;
        }
        
        // split the line by commas
        String[] attributes = line.split(",");
        
        // create a Song and enter its attributes
        Song song = new Song(
            attributes[0], // title
            attributes[1], // artist
            attributes[2], // genre
            Integer.parseInt(attributes[3]), // year
            Integer.parseInt(attributes[4]), // bpm
            Integer.parseInt(attributes[5]), // energy
            Integer.parseInt(attributes[6]), // danceability
            Integer.parseInt(attributes[7]), // loudness
            Integer.parseInt(attributes[8]) // liveness
            );
        
        // put o=song into tree
        this.tree.insert(song);
      }
    } catch (IOException e) {
      throw new IOException("Could not read file: " + filename);
    }
  }

  /**
   * Retrieves a list of song titles from the tree passed to the contructor.
   * The songs should be ordered by the songs' Year, and that fall within
   * the specified range of Year values.  This Year range will
   * also be used by future calls to the setFilter and getmost Danceable methods.
   *
   * If a Loudness filter has been set using the setFilter method
   * below, then only songs that pass that filter should be included in the
   * list of titles returned by this method.
   *
   * When null is passed as either the low or high argument to this method,
   * that end of the range is understood to be unbounded.  For example, a null
   * high argument means that there is no maximum Year to include in
   * the returned list.
   *
   * @param low is the minimum Year of songs in the returned list
   * @param high is the maximum Year of songs in the returned list
   * @return List of titles for all songs from low to high, or an empty
   *     list when no such songs have been loaded
   */
  @Override
  public List<String> getRange(Integer low, Integer high) {
    // TODO Auto-generated method stub
    this.low = low;
    this.high = high;
    List<Song> inRange = new ArrayList<Song>();
    
    // iterating over the song tree
    for (Song song : tree) {
      boolean withinRange = true;
      
      // check if within range
      if (low != null && song.getYear() < low) {
        withinRange = false;
      }
      
      if (high != null && song.getYear() > high) {
        withinRange = false;
      }
      
      // check loudness filter
      if (this.filter != null && song.getLoudness() >= this.filter) {
        withinRange = false;
      }
      
      // all checks passed
      if (withinRange) {
        inRange.add(song);
      }
    }
    
    // sorting songs by year
    Collections.sort(inRange, Comparator.comparingInt(Song::getYear));
    
    // get titles
    List<String> titles = new ArrayList<>();
    for (Song song : inRange) {
        titles.add(song.getTitle());
    }
    
    return titles;
  }

  /**
   * Retrieves a list of song titles that have a Loudness that is
   * smaller than the specified threshold.  Similar to the getRange
   * method: this list of song titles should be ordered by the songs'
   * Year, and should only include songs that fall within the specified
   * range of Year values that was established by the most recent call
   * to getRange.  If getRange has not previously been called, then no low
   * or high Year bound should be used.  The filter set by this method
   * will be used by future calls to the getRange and getmost Danceable methods.
   *
   * When null is passed as the threshold to this method, then no Loudness
   * threshold should be used.  This effectively clears the filter.
   *
   * @param threshold filters returned song titles to only include songs that
   *     have a Loudness that is smaller than this threshold.
   * @return List of titles for songs that meet this filter requirement, or
   *     an empty list when no such songs have been loaded
   */
  @Override
  public List<String> setFilter(Integer threshold) {
    // TODO Auto-generated method stub
    this.filter = threshold;
    
    List<Song> filteredSongs = new ArrayList<Song>();
    
    for (Song song : tree) {
      boolean withinRange = true;
      
      // check if within range
      if (low != null && song.getYear() < low) {
        withinRange = false;
      }
      
      if (high != null && song.getYear() > high) {
        withinRange = false;
      }
      
      if (this.filter != null && song.getLoudness() >= this.filter) {
        withinRange = false;
      }
      
      if (withinRange) {
        filteredSongs.add(song);
      }
    }
    
    // sorting songs by year
    Collections.sort(filteredSongs, Comparator.comparingInt(Song::getYear));
    
    // get titles
    List<String> filteredTitles = new ArrayList<>();
    for (Song song : filteredSongs) {
      filteredTitles.add(song.getTitle());
    }
    
    return filteredTitles;
    
  }

  /**
   * This method returns a list of song titles representing the five
   * most Danceable songs that both fall within any attribute range specified
   * by the most recent call to getRange, and conform to any filter set by
   * the most recent call to setFilter.  The order of the song titles in this
   * returned list is up to you.
   *
   * If fewer than five such songs exist, return all of them.  And return an
   * empty list when there are no such songs.
   *
   * @return List of five most Danceable song titles
   */
  @Override
  public List<String> fiveMost() {
    // TODO Auto-generated method stub
    List<Song> filteredSongs = new ArrayList<Song>();
    
    for (Song song : tree) {
      boolean withinRange = true;
      
      // check if within range
      if (low != null && song.getYear() < low) {
        withinRange = false;
      }
      
      if (high != null && song.getYear() > high) {
        withinRange = false;
      }
      
      if (this.filter != null && song.getLoudness() >= this.filter) {
        withinRange = false;
      }
      
      if (withinRange) {
        filteredSongs.add(song);
      }
    }
    
    // sort the songs by danceability in descending order
    filteredSongs.sort((s1, s2) -> Integer.compare(s2.getDanceability(), s1.getDanceability()));
    
    // get titles of the top five most danceable songs
    List<String> mostDanceable = new ArrayList<>();
    for (int i = 0; i < Math.min(5, filteredSongs.size()); ++i) {
      mostDanceable.add(filteredSongs.get(i).getTitle());
    }
    
    return mostDanceable;
  }

}
