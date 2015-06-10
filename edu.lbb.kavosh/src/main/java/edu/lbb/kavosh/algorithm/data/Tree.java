/**
@project Lbb_Kavosh
@author Mitra Ansari
@date May 10, 2011
 **/
package edu.lbb.kavosh.algorithm.data;

import javax.swing.tree.DefaultTreeModel;

public class Tree {
	// ?
	private static final int ULLONG_MAX = 100000;
	private int subGraphSize;
	private int leafNumber = 0;
	private Node currentNode;
	private int head = 0;
	private long[] c;
	private int size = 0;

	public int getSize() {
		return size;
	}

	private DefaultTreeModel treeModel;

	public Tree(int subGraphSize) {
		this.subGraphSize = subGraphSize;
		Node root = new Node();
		size++;
		treeModel = new DefaultTreeModel(root);
	}

	public Node returnRoot() {
		return (Node) treeModel.getRoot();
	}

	public int getLeafNumber() {
		return leafNumber;
	}

	public void init_cur_node() {
		currentNode = returnRoot();
	}

	public void insert_one_main() {
		
		if (currentNode.getRightChild() == null) {
			Node node = new Node(false, size++);
			currentNode.getChilds().add(node);
		}
		currentNode = currentNode.getRightChild();
	}

	public void insert_one_rand() {
		if (currentNode.getRightChild() != null)
			currentNode = currentNode.getRightChild();
		else
			return;
	}

	public void insert_zero_rand() {
		if (currentNode.getLeftChild() != null)
			currentNode = currentNode.getLeftChild();
		else
			return;
	}

	public void insert_zero_main() {
		if (currentNode.getLeftChild() == null) {
			Node node = new Node(true, size++);
			currentNode.getChilds().add(node);
		}
		currentNode = currentNode.getLeftChild();
	}

	public Node update_leaf(Node l, int val) {
		if (l.getCount() == ULLONG_MAX) {
			System.out
					.println("Oops, Fatal error: There is a subgraph instant which occured more than "
							+ ULLONG_MAX
							+ " times, which does not fit in any datatype. We will extend Kavosh to support this case in future. Sorry!");
			System.exit(-1);
		}
		l.setCount(l.getCount() + val);
		return l;
	}

	public void update_one_main(int val) {
		Node leaf;
		if (currentNode.getRightChild() != null) {
			leaf = currentNode.getRightChild();
			update_leaf(leaf, val);
		} else {
			Node node = new Node(false, size++);
			currentNode.getChilds().add(node);
			leaf = currentNode.getRightChild();
			leafNumber++;
		}
	}

	public void update_one_rand(int val) {
		Node leaf;
		if (currentNode.getRightChild() != null) {
			leaf = currentNode.getRightChild();
			update_leaf(leaf, val);
		} else
			return;
	}

	public void update_zero_main(int val) {
		Node leaf;
		if (currentNode.getLeftChild() != null) {
			leaf = currentNode.getLeftChild();
			update_leaf(leaf, val);
		} else {
			Node node = new Node(true, size++);
			currentNode.getChilds().add(node);
			leaf = currentNode.getLeftChild();
			leafNumber++;
		}
	}

	public void update_zero_rand(int val) {
		Node leaf;
		if (currentNode.getLeftChild() != null) {
			leaf = currentNode.getLeftChild();
			update_leaf(leaf, val);
		} else
			return;
	}

	public long[] extract() {
		c = new long[getLeafNumber()];
		head = 0;
		DFS(returnRoot());
		return c;
	}

	public void DFS(Node cur) {
		if (currentNode.getChildCount() == 0) {
			Node cur_leaf = cur;
			if (cur_leaf.getCount() > 0) {
				c[head] = cur_leaf.getCount();
				head++;
				cur_leaf.setCount(0);
			}
			return;
		}
		if (currentNode.getLeftChild() != null) {
			DFS(currentNode.getLeftChild());
		} else
			DFS(currentNode.getRightChild());
	}

	public void DFS_value(Node cur, char[] str, int lev, Value v) {
		if (currentNode.getChildCount() == 0) {
			Node leaf = currentNode;
			v.getC()[head] = leaf.getCount();
			v.getAdj_str()[head] = str;
			head++;
			leaf.setCount(0);
			return;
		}
		if (currentNode.getLeftChild() != null) {
			str[lev] = 0;
			DFS_value(currentNode.getLeftChild(), str, lev + 1, v);
		}
		if (currentNode.getRightChild() != null) {
			str[lev] = 1;
			DFS_value(currentNode.getRightChild(), str, lev + 1, v);
		}
	}

	public Value destroy() {
		Value v = new Value();
		v.setC(new long[getLeafNumber()]);
		v.setAdj_str(new char[getLeafNumber()][]);
		for (int i = 0; i < getLeafNumber(); i++)
			v.getAdj_str()[i] = new char[subGraphSize * (subGraphSize - 1)];
		head = 0;
		char[] str = new char[subGraphSize * (subGraphSize - 1)];
		DFS_value(returnRoot(), str, 0, v);
		return v;
	}
}
