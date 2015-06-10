/**
@project Lbb_Kavosh
@author Mitra Ansari
@date May 10, 2011
**/
package edu.lbb.kavosh.algorithm.data;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

public class Node implements TreeNode {
	private boolean visited = false;
	private List<Node> childs = new ArrayList<Node>(2);
	private int index = 0;

	private Node parent;
	boolean leftChild = false;
	private int count = 1;
	private int level;
	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isLeftChild() {
		return leftChild;
	}

	public void setLeftChild(boolean leftChild) {
		this.leftChild = leftChild;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node() {
		
	}
	
	public Node(boolean isLeftChild, int index) {
		leftChild = isLeftChild;
		this.index = index;
	}

	public Node getRightChild() {
		for (Node child : childs) {
			if (!child.isLeftChild())
				return child;
		}
		return null;
	}
	
	public Node getLeftChild() {
		for (Node child : childs) {
			if (child.isLeftChild())
				return child;
		}
		return null;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	@Override
	public Enumeration<Node> children() {
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Node getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return childs.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return index;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		if (childs.size() == 0)
			return true;
		return false;
	}
	
	public List<Node> getChilds() {
		return childs;
	}
	
	@Override
	public String toString() {
		return index + "(" + level + "-" + index + ")";
	}

	public void setIndex(int i) {
		index = i;
	}

	public Integer getIndex() {
		return index;
	}
}
