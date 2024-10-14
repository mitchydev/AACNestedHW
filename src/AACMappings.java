import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.NullKeyException;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


/**
 * Creates a set of mappings of an AAC that has two levels, one for categories and then within each
 * category, it has images that have associated text to be spoken. This class provides the methods
 * for interacting with the categories and updating the set of images that would be shown and
 * handling an interactions.
 * 
 * @author Catie Baker & Mitchell Paiva
 *
 */
public class AACMappings implements AACPage {
	private AssociativeArray<String, AACCategory> categories;
	private AACCategory currentCategory;
	private String currCatName;

	/**
	 * Creates a set of mappings for the AAC based on the provided file. The file is read in to create
	 * categories and fill each of the categories with initial items. The file is formatted as the
	 * text location of the category followed by the text name of the category and then one line per
	 * item in the category that starts with > and then has the file name and text of that image
	 * 
	 * for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing and food has french fries and
	 * watermelon and clothing has a collared shirt
	 * 
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
		categories = new AssociativeArray<>();
		currCatName = "";
		currentCategory = null;

		loadMap(filename);
	} // AACMappings

	/**
	 * This function reads a file, then proceeds to load the mappings from the file
	 * and converts it to the data structure categories.
	 * 
	 * @param filename the name of the file to be read.
	 */
	private void loadMap(String filename) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			AACCategory category = null;
			String imageLoc = null;

			while ((line = reader.readLine()) != null) {
				String[] args = line.split(" ");
				if (!line.startsWith(">")) {
					imageLoc = args[0];
					String categoryName = args[1];
					if (args.length > 2) {
						for (int i = 2; i < args.length; i++) {
							categoryName += " " + args[i];
						} // if statement
					} // if statmenet
					category = new AACCategory(categoryName);
					this.categories.set(imageLoc, category);
				} else {
					String itemLoc = args[0].substring(1);
					String text = args[1];
					
					if (args.length > 2) {
						for (int i = 2; i < args.length; i++) {
							text += " " + args[i];
						} // for loop
					} // if statement
					if (category != null) {
						category.addItem(itemLoc, text);
					} // if state,ent
				} // else
			} // while
		} catch (Exception e) {
			System.out.println("error.");
		} // catch
	} // loadmap

	/**
	 * Given the image location selected, it determines the action to be taken. This can be updating
	 * the information that should be displayed or returning text to be spoken. If the image provided
	 * is a category, it updates the AAC's current category to be the category associated with that
	 * image and returns the empty string. If the AAC is currently in a category and the image
	 * provided is in that category, it returns the text to be spoken.
	 * 
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise it returns the
	 *         empty string
	 * @throws NoSuchElementException if the image provided is not in the current category
	 */
	public String select(String imageLoc) {
		if (this.currentCategory == null) {
			if (this.categories.hasKey(imageLoc)) {
				try {
					this.currentCategory = this.categories.get(imageLoc);
					this.currCatName = this.currentCategory.getCategory();
					return "";
				} catch (Exception e) {
					throw new NoSuchElementException("Error: No such category.");
				} // catch
			} // if
			throw new NoSuchElementException("Image not found.");
		} else {
			if (currentCategory.hasImage(imageLoc)) {
				return currentCategory.select(imageLoc);
			} else {
				throw new NoSuchElementException("Image not found.");
			} // else
		} // else
	} // select



	/**
	 * Provides an array of all the images in the current category
	 * 
	 * @return the array of images in the current category; if there are no images, it should return
	 *         an empty array
	 */
	public String[] getImageLocs() {
		if (this.currentCategory == null) {
			String[] imageLocations = new String[categories.size()];
			for (int i = 0; i < this.categories.size(); i++) {
				imageLocations[i] = this.categories.pairs[i].key;
			} // for loop
			return imageLocations;
		} else
			return currentCategory.getImageLocs();
	} // getImageLocs


	/**
	 * Resets the current category of the AAC back to the default category
	 */
	public void reset() {
		currentCategory = null;
		currCatName = "";
	} // reset


	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as the text location of the
	 * category followed by the text name of the category and then one line per item in the category
	 * that starts with > and then has the file name and text of that image
	 * 
	 * for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing and food has french fries and
	 * watermelon and clothing has a collared shirt
	 * 
	 * @param filename the name of the file to write the AAC mapping to
	 */
	public void writeToFile(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			for (int i = 0; i < categories.size(); i++) {
				String categoryK = this.categories.pairs[i].key;
				AACCategory category = this.categories.pairs[i].val;


				writer.write(categoryK + " " + category.getCategory());
				writer.newLine();

				String[] imageLocs = category.getImageLocs();
				for (int x = 0; x < imageLocs.length; x++) {
					String imageLocation = imageLocs[x];
					writer.write(">" + imageLocation + " " + category.select(imageLocation));
					writer.newLine();
				} // for loop
			} // for loop
			writer.close();
		} catch (Exception e) {
			System.out.println("error.");
		} // catch
	} // writreToFile

	/**
	 * Adds the mapping to the current category (or the default category if that is the current
	 * category)
	 * 
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		if (this.currentCategory == null) {
			try {
				this.categories.set(imageLoc, new AACCategory(text));
			} catch (NullKeyException e) {
				System.out.println("error.");
			} // catch
		} else {
			currentCategory.addItem(imageLoc, text);
		} // else
	} // addItem



	/**
	 * Gets the name of the current category
	 * 
	 * @return returns the current category or the empty string if on the default category
	 */
	public String getCategory() {
		return this.currCatName;
	} // getCategory


	/**
	 * Determines if the provided image is in the set of images that can be displayed and false
	 * otherwise
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		if (this.categories == null) {
			return false;
		} // if statement
		return this.categories.hasKey(imageLoc);
	} // hasImage
} // AACMappings
