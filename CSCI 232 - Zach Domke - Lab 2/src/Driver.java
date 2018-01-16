//CSCI 232 Lab 2
//Zach Domke
//10-15-16

import java.io.*;
import java.util.*;

class Driver{
	static int hashSize;
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter size of hash table: ");
		hashSize = in.nextInt();
		Item[][] hashArray = new Item[hashSize][15];
		for(int i = 0; i < hashArray.length; i++){
			for(int j = 0; j < hashArray[i].length; j++){
				Item empty = new Item(314);
				hashArray[i][j] = empty;
			}
		}
		
		System.out.print("Enter inital number of items: ");
		int numItems = in.nextInt();
		for(int i = 0; i < numItems; i++){
			randInsert(hashArray);
		}
		
		char choice = 'a';
		while(choice != 'e'){
			System.out.print("Enter first letter of show, insert, delete, find, or end: ");
			choice = in.next().charAt(0);
			switch(choice){
				case 's': show(hashArray);
					break;
				case 'i': insert(hashArray);
					break;
				case 'd': delete(hashArray);
					break;
				case 'f': System.out.print("Enter key value to find: ");
					int temp = in.nextInt();
					if(search(temp, hashArray) == null){
						System.out.println("Could not find " + temp);
					} else {
						System.out.println("Found " + temp);
					}
					break;
				case 'e': System.out.println("Good bye");
					in.close();
					break;
				default: System.out.println("That is not an option, try again");
					break;
			}
		}
	}
	
	public static int hashCode(int key){
		return key % hashSize;
	}
	
	public static Item search(int key, Item[][] hashArray){
		int hashIndex = hashCode(key);
		for(int i=0; i < hashArray[hashIndex].length - 1; i++){
			if(hashArray[hashIndex][i].key == key){
				return hashArray[hashIndex][i];
			}
		}
		return null;
	}
	
	public static void randInsert(Item[][] hashArray){
		Random rand = new Random();
		int key = rand.nextInt(20) + 1;
		int hashIndex = hashCode(key);
		if(search(key, hashArray) == null){
			Item thing = new Item(key);
			hashArray[hashIndex][hashArray[hashIndex].length - 1] = thing;
			sortArr(hashArray[hashIndex]);
		} else {
			randInsert(hashArray);
		}
	}
	
	public static void insert(Item[][] hashArray){
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a key value to insert: ");
		int key = in.nextInt();
		int hashIndex = hashCode(key);
		if(search(key, hashArray) == null){
			Item thing = new Item(key);
			hashArray[hashIndex][hashArray[hashIndex].length - 1] = thing;
			sortArr(hashArray[hashIndex]);
		} else {
			System.out.println(key + " already exists, enter a new key value");
			insert(hashArray);
		}
	}
	
	public static void delete(Item[][] hashArray){
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a key value to delete: ");
		int key = in.nextInt();
		int hashIndex = hashCode(key);
		Item temp = null;
		for(int i = 0; i < hashArray[hashIndex].length - 1; i++){
			if(hashArray[hashIndex][i].key == key){
				temp = hashArray[hashIndex][i];
				hashArray[hashIndex][i].key = 314;
				sortArr(hashArray[hashIndex]);
			}
		}
		if(temp == null){
			System.out.println("Could not delete " + key);
		}
	}
	
	public static void show(Item[][] hashArray){
		for(int i = 0; i < hashArray.length; i++){
			System.out.print(hashCode(i) + ".  ");
			for(int j = 0; j < hashArray[i].length; j++){
				if(hashArray[i][j].key != 314){
					System.out.print(hashArray[i][j].key + " ");
				}
			}
			System.out.println();
		}
	}
	
	public static void sortArr(Item[] singArr){
		Item temp;
		for(int i = (singArr.length - 1); i >= 0; i--){
			for(int j = 1; j <= i; j++){
				if(singArr[j - 1].key > singArr[j].key){
					temp = singArr[j - 1];
					singArr[j - 1] = singArr[j];
					singArr[j] = temp;
				}
			}
		}
	}
}

class Item{
	int key;
	
	public Item(int inKey){
		key = inKey;
	}
}