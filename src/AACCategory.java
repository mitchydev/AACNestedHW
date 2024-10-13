import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

/**
 * Represents the mappings for a single category of items that should be displayed
 * 
 * @author Catie Baker & Mitchell Paiva
 *
 */
public class AACCategory implements AACPage {

	private final String categoryName;

	private final AssociativeArray<String, String> thing;


	/**
	 * Creates a new empty category with the given name
	 * 
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.categoryName = name;
		this.thing = new AssociativeArray<>();
	} // AACCategory

	/**
	 * Adds the image location, text pairing to the category
	 * 
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
			this.thing.set(imageLoc, text);
		} catch (NullKeyException e) {
			System.out.println("Error!");
		} // Catch
	} // addItem

	/**
	 * Returns an array of all the images in the category
	 * 
	 * @return the array of image locations; if there are no images, it should return an empty array
	 */
	public String[] getImageLocs() {
		String[] imageLocations = new String[this.thing.size()];
		for (int i = 0; i < this.thing.size(); i++) {
			imageLocations[i] = this.thing.pairs[i].key;
		} // for loop
		return imageLocations;
	} // getImageLocs

	/**
	 * Returns the name of the category
	 * 
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.categoryName;
	}

	/**
	 * Returns the text associated with the given image in this category
	 * 
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current category
	 */
	public String select(String imageLoc) {
		try {
			return this.thing.get(imageLoc);
		} catch (KeyNotFoundException e) {
			throw new NoSuchElementException("error");
		} // catch
	}

	/**
	 * Determines if the provided images is stored in the category
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return this.thing.hasKey(imageLoc);
	} // hasImage
} // AACCategory
