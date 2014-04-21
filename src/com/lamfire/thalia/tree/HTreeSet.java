package com.lamfire.thalia.tree;

import java.util.AbstractSet;
import java.util.Iterator;

import com.lamfire.thalia.Utils;

/**
 * Wrapper for HTree to implement java.util.Map interface
 */
public class HTreeSet<E> extends AbstractSet<E> {

	final HTree<E, Object> tree;

	public HTreeSet(HTree<E, Object> map) {
		this.tree = map;
	}

	public Iterator<E> iterator() {
		return tree.keySet().iterator();
	}

	public int size() {
		return tree.size();
	}

	public boolean isEmpty() {
		return tree.isEmpty();
	}

	public boolean contains(Object o) {
		return tree.containsKey(o);
	}

	public boolean add(E e) {
		return tree.put(e, Utils.EMPTY_STRING) == null;
	}

	public boolean remove(Object o) {
		return tree.remove(o) == Utils.EMPTY_STRING;
	}

	public void clear() {
		tree.clear();
	}

	public HTree<E, Object> getTree() {
		return tree;
	}

}
