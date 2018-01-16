//CSCI 232 Lab 1
//Zach Domke
//9-26-16

import java.io.*;
import java.util.*;

class Node
{
	public char iData; // data item (key)
	public int dData; // data item
	public Node leftChild; // this node's left child
	public Node rightChild; // this node's right child
	public void displayNode() // display ourself
	{
		System.out.print('{');
		System.out.print(iData);
		System.out.print(", ");
		System.out.print(dData);
		System.out.print("} ");
	}
}

class Tree
{
	private Node root; // first node of tree
	// -------------------------------------------------------------
	public Tree() // constructor
	{ root = null; } // no nodes in tree yet
	//-------------------------------------------------------------
	public Node find(int key) // find node with given key
	{ // (assumes non-empty tree)
		Node current = root; // start at root
		while(current.iData != key) // while no match,
		{
			if(key < Character.getNumericValue(current.iData)) // go left?
				current = current.leftChild;
			else // or go right?
				current = current.rightChild;
			if(current == null) // if no child,
				return null; // didn't find it
		}
		return current; // found it
	} // end find()
	// -------------------------------------------------------------
	public void insert(char id, int dd)
	{
		Node newNode = new Node(); // make new node
		newNode.iData = id; // insert data
		newNode.dData = dd;
		if(root==null) // no node in root
			root = newNode;
		else // root occupied
		{
			Node current = root; // start at root
			Node parent;
			while(true) // (exits internally)
			{
				parent = current;
				if(dd < current.dData) // go left?
				{
					current = current.leftChild;
					if(current == null) // if end of the line,
					{ // insert on left
						parent.leftChild = newNode;
						return;
					}
				} // end if go left
				else // or go right?
				{
					current = current.rightChild;
					if(current == null) // if end of the line
					{ // insert on right
						parent.rightChild = newNode;
						return;
					}
				} // end else go right
			} // end while
		} // end else not root
	} // end insert()
	// -------------------------------------------------------------
	public boolean delete(int key) // delete node with given key
	{ // (assumes non-empty list)
		Node current = root;
		Node parent = root;
		boolean isLeftChild = true;
		while(current.iData != key) // search for node
		{
			parent = current;
			if(key < current.dData) // go left?
			{
				isLeftChild = true;
				current = current.leftChild;
			}
			else // or go right?
			{
				isLeftChild = false;
				current = current.rightChild;
			}
			if(current == null) // end of the line,
				return false; // didn't find it
		} // end while
		// found node to delete
		// if no children, simply delete it
		if(current.leftChild==null &&
				current.rightChild==null)
		{
			if(current == root) // if root,
				root = null; // tree is empty
			else if(isLeftChild)
				parent.leftChild = null; // disconnect
			else // from parent
				parent.rightChild = null;
		}
		// if no right child, replace with left subtree
		else if(current.rightChild==null)
			if(current == root)
				root = current.leftChild;
			else if(isLeftChild)
				parent.leftChild = current.leftChild;
			else
				parent.rightChild = current.leftChild;
		// if no left child, replace with right subtree
		else if(current.leftChild==null)
			if(current == root)
				root = current.rightChild;
			else if(isLeftChild)
				parent.leftChild = current.rightChild;
			else
				parent.rightChild = current.rightChild;
		else // two children, so replace with inorder successor
		{
			// get successor of node to delete (current)
			Node successor = getSuccessor(current);
			// connect parent of current to successor instead
			if(current == root)
				root = successor;
			else if(isLeftChild)
				parent.leftChild = successor;
			else
				parent.rightChild = successor;
			// connect successor to current's left child
			successor.leftChild = current.leftChild;
		} // end else two children
		// (successor cannot have a left child)
		return true; // success
	} // end delete()
	// -------------------------------------------------------------
	// returns node with next-highest value after delNode
	// goes to right child, then right child's left descendents
	private Node getSuccessor(Node delNode)
	{
		Node successorParent = delNode;
		Node successor = delNode;
		Node current = delNode.rightChild; // go to right child
		while(current != null) // until no more
		{ // left children,
			successorParent = successor;
			successor = current;
			current = current.leftChild; // go to left child
		}
		// if successor not
		if(successor != delNode.rightChild) // right child,
		{ // make connections
			successorParent.leftChild = successor.rightChild;
			successor.rightChild = delNode.rightChild;
		}
		return successor;
	}
	// -------------------------------------------------------------
	public void traverse(int traverseType)
	{
		switch(traverseType)
		{
		case 1: System.out.print("\nPreorder traversal: ");
		preOrder(root);
		break;
		case 2: System.out.print("\nInorder traversal: ");
		inOrder(root);
		break;
		case 3: System.out.print("\nPostorder traversal: ");
		postOrder(root);
		break;
		}
		System.out.println();
	}
	// -------------------------------------------------------------
	private void preOrder(Node localRoot)
	{
		if(localRoot != null)
		{
			System.out.print(localRoot.iData + " ");
			preOrder(localRoot.leftChild);
			preOrder(localRoot.rightChild);
		}
	}
	// -------------------------------------------------------------
	private void inOrder(Node localRoot)
	{
		if(localRoot != null)
		{
			inOrder(localRoot.leftChild);
			System.out.print(localRoot.iData + " ");
			inOrder(localRoot.rightChild);
		}
	}
	// -------------------------------------------------------------
	private void postOrder(Node localRoot)
	{
		if(localRoot != null)
		{
			postOrder(localRoot.leftChild);
			postOrder(localRoot.rightChild);
			System.out.print(localRoot.iData + " ");
		}
	}
	// -------------------------------------------------------------
	public void displayTree()
	{
		Stack globalStack = new Stack();
		globalStack.push(root);
		int nBlanks = 32;
		boolean isRowEmpty = false;
		//root.displayNode();
		System.out.println(
				"......................................................");
		while(isRowEmpty==false)
		{
			Stack localStack = new Stack();
			isRowEmpty = true;
			for(int j=0; j<nBlanks; j++)
				System.out.print(' ');
			while(globalStack.isEmpty()==false)
			{
				Node temp = (Node)globalStack.pop();
				if(temp != null)
				{
					System.out.print(temp.iData);
					localStack.push(temp.leftChild);
					localStack.push(temp.rightChild);
					if(temp.leftChild != null ||
							temp.rightChild != null)
						isRowEmpty = false;
				}
				else
				{
					System.out.print("--");
					localStack.push(null);
					localStack.push(null);
				}
				for(int j=0; j<nBlanks*2-2; j++)
					System.out.print(' ');
			} // end while globalStack not empty
			System.out.println();
			nBlanks /= 2;
			while(localStack.isEmpty()==false)
				globalStack.push( localStack.pop() );
		} // end while isRowEmpty is false
		System.out.println(
				"......................................................");
	} // end displayTree()
	// -------------------------------------------------------------
}

public class Driver {
	
	public static void main(String[] args) throws IOException{
		String text;
		Tree theTree = new Tree();
		while(true){
			System.out.print("Enter first letter of enter, show, code, or decode");
			int choice = getChar();
			switch(choice){
				case 'e': System.out.println("Enter text lines, terminate with $");
					//PriorityQApp.test();
					text = getString();
					PriorityQ thePQ = new PriorityQ(text.length());
					int var = 0;
					while(!thePQ.isFull()){
						if(text.charAt(var) == ' '){
							thePQ.insert('[');
							theTree.insert('[', Character.getNumericValue('['));
							System.out.print('[');
						} else if(text.charAt(var) == '$'){
							thePQ.insert('\\');
							theTree.insert('\\', Character.getNumericValue('\\'));
							System.out.print('\\');
						} else {
							char upperVar = Character.toUpperCase(text.charAt(var));
							thePQ.insert(upperVar);
							theTree.insert(upperVar, Character.getNumericValue(upperVar));
							System.out.print(Character.toUpperCase(text.charAt(var)));
						}
						var++;
					}
					System.out.println();
					while(!thePQ.isEmpty()){
						char item = thePQ.remove();
						//theTree.insert(item, Character.getNumericValue(item));
					} 
					break;
				case 's': theTree.displayTree();
					break;
				case 'c': //theTree.encode();
					break;
				case 'd': //theTree.decode();
					break;
				default: System.out.println("Invalid entry");
			}
		}
	}
	
	public static String getString() throws IOException
	{
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}
	
	public static char getChar() throws IOException
	{
		String s = getString();
		return s.charAt(0);
	}
}

class PriorityQ
{
	// array in sorted order, from max at 0 to min at size-1
	private int maxSize;
	private char[] queArray;
	private int nItems;
	//-------------------------------------------------------------
	public PriorityQ(int s) // constructor
	{
		maxSize = s;
		queArray = new char[maxSize];
		nItems = 0;
	}
	//-------------------------------------------------------------
	public void insert(char item) // insert item
	{
		int j;
		if(nItems==0) // if no items,
			queArray[nItems++] = item; // insert at 0
		else // if items,
		{
			for(j=nItems-1; j>=0; j--) // start at end,
			{
				if( item > queArray[j] ) // if new item larger,
					queArray[j+1] = queArray[j]; // shift upward
				else // if smaller,
					break; // done shifting
			} // end for
			queArray[j+1] = item; // insert it
			nItems++;
		} // end else (nItems > 0)
	} // end insert()
	//-------------------------------------------------------------
	public char remove() // remove minimum item
	{ return queArray[--nItems]; }
	//-------------------------------------------------------------
	public char peekMin() // peek at minimum item
	{ return queArray[nItems-1]; }
	//-------------------------------------------------------------
	public boolean isEmpty() // true if queue is empty
	{ return (nItems==0); }
	//-------------------------------------------------------------
	public boolean isFull() // true if queue is full
	{ return (nItems == maxSize); }
	//-------------------------------------------------------------
}

/*class PriorityQApp
{
	public static void test() throws IOException
	{
		PriorityQ thePQ = new PriorityQ(5);
		thePQ.insert('a');
		thePQ.insert('l');
		thePQ.insert('3');
		thePQ.insert('h');
		thePQ.insert('z');
		while( !thePQ.isEmpty() )
		{
			char item = thePQ.remove();
			System.out.print(item + " "); // 10, 20, 30, 40, 50
		} // end while
		System.out.println();
	} // end main()
	//-------------------------------------------------------------
} // end class PriorityQApp */

