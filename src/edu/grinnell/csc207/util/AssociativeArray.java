package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K and values of type V.
 * Associative Arrays store key/value pairs and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Mitchell Paiva
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  public KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({"unchecked"})
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(), DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> cloned = new AssociativeArray<K, V>();
    cloned.pairs = new KVPair[this.pairs.length];
    for (int i = 0; i < this.size; i++) {
      cloned.pairs[i] = new KVPair(this.pairs[i].key, this.pairs[i].val);
    } // for loop
    cloned.size = this.size;
    return cloned;
  } // clone()


  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String printed = "{";
    for (int i = 0; i < this.size; i++) {
      if (i > 0) {
        printed += ", ";
      } // if statement
      printed += this.pairs[i].key;
      printed += ":";
      printed += this.pairs[i].val;
    } // toString()
    printed += "}";
    return printed;
  } // if statement

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to get(key) will return value.
   *
   * @param key The key whose value we are seeting.
   * @param value The value of that key.
   *
   * @throws NullKeyException If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException();
    } // if statement
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        this.pairs[i].val = value;
        return;
      } // if statement
    } // for loop
    if (this.size == this.pairs.length) {
      expand();
    } // if statement
    this.pairs[this.size] = new KVPair(key, value);
    this.size++;
    return;
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key A key
   *
   * @return The corresponding value
   *
   * @throws KeyNotFoundException when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return this.pairs[i].val;
      } // if statement
    } // for loop
    throw new KeyNotFoundException("Key is null.");
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should return false for the null key, since
   * it cannot appear.
   *
   * @param key The key we're looking for.
   *
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return true;
      } // if statement
    } // for loop
    return false;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls to get(key) will throw an
   * exception. If the key does not appear in the associative array, does nothing.
   *
   * @param key The key to remove.
   */
  public void remove(K key) {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        for (int x = (i + 1); x < this.size; x++) {
          this.pairs[x - 1] = this.pairs[x];
        } // if statement
        this.pairs[this.size - 1] = null;
        this.size--;
        return;
      } // if statement.
    } // for loop.
    return;
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key. If no such entry is found,
   * throws an exception.
   *
   * @param key The key of the entry.
   *
   * @return The index of the key, if found.
   *
   * @throws KeyNotFoundException If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return i;
      } // if statement
    } // for loop
    throw new KeyNotFoundException("Key not found.");
  } // find(K)

} // class AssociativeArray
