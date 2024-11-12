import java.io.IOException;
import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Backend implements BackendInterface {

    private IterableSortedCollection<Song> myTree;

    private int rangeHigh = -1;

    private int rangeLow = -1;

    private int dbThreshold = -100;

    

    public Backend(IterableSortedCollection<Song> tree) {

	this.myTree = tree;
    }
    
    /**
     *
     *
     *@param filename is the name of the csv file to load data from
     *@throws IOException
     */
    @Override
    public void readData(String filename) throws IOException {

	try {
	    File songFile = new File(filename);

	    Scanner scnr = new Scanner(songFile);
	    scnr.nextLine();
	    while(scnr.hasNextLine()) {
		String[] line = scnr.nextLine().split(",");
		myTree.insert(new Song(line[0],line[1],line[2],Integer.parseInt(line[3]),
				       Integer.parseInt(line[4]),Integer.parseInt(line[5]),
				       Integer.parseInt(line[6]),Integer.parseInt(line[7]),
				       Integer.parseInt(line[8])));
	    }
	}
	catch (Exception e) {
	    throw new IOException();
	}
    }

    /**
     *
     *@param low is the minimum Year of song in the returned list
     *@param high is the maximum Year of songs in the returned list
     *@return
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {

	if(high != null) {
	    this.rangeHigh = high;
	}
	if(low != null) {
	    this.rangeLow = low;
	}
	
	List<String> returnList = new ArrayList<>();

	while(myTree.iterator().hasNext()) {

	    if (dbThreshold != -1) {
		Song current = myTree.iterator().next();
		if (current.getYear() >= low && current.getYear() <= high) {
		    returnList.add(current.getTitle());
		}
	    }
	}


        return returnList;
    }

    /**
     *
     *@param threshold filters returned song titles to only include songs that
     *have a loudness that is smaller than this threshold
     *@return
     */
    @Override
    public List<String> setFilter(Integer threshold) {

	this.dbThreshold = threshold;

	List<String> returnList = new ArrayList<>();

	while(myTree.iterator().hasNext()) {
	    Song current = myTree.iterator().next();
	    if (current.getLoudness() <= dbThreshold) {
		returnList.add(current.getTitle());
	    }
	}

	return returnList;
	
    }

    public List<String> fiveMost() {

	List<String> topFive = new ArrayList<>();

	List<Song> eligible = new ArrayList<>();
	List<String> returnList = new ArrayList<>();

	for(int i = 0; i < 5; i++) {

	    try {

		Song pick = new Song(null,null,null,0,0,0,0,0,0);
		Song current = null;
		for (int j = 0; j < eligible.size(); j++) {

		    current = myTree.iterator().next();
		    if (current.getDanceability() > pick.getDanceability()) {
			pick = current;
		    }
		}
		returnList.add(pick.getTitle());
	    }
	    catch (Exception e) {

	    }
	}

	return returnList;
    }


}
