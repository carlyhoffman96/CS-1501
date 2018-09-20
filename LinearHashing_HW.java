import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/*
Name: Carly Hoffman
User: CAH231
Reciation: 11:00 AM Th
*/


//assumes linear hashing
public class LinearHashing_HW<K,V>
{
	//fields
	private int numBucketsInLevel;
	private int numLevels;
	private int splitBucketPointer;
	private List<List<K>> keys;
	private List<List<V>> table;

	//static fields
	private static final int BUCKET_LIMIT = 4;

	//constructor
	public LinearHashing_HW()
	{
		this.numBucketsInLevel = 4;				//start with 4 buckets - each will have linked list
		this.numLevels = 0;								//
		this.splitBucketPointer = 0;			//update everytime there is overflow
		this.keys = new ArrayList<List<K>>();
		this.table = new ArrayList<List<V>>();
		for(int i = 0; i < this.numBucketsInLevel; i++)
		{
			this.keys.add(new LinkedList<K>());
			this.table.add(new LinkedList<V>());
		}
	}

	//checks if the key is already in use
	public boolean containsKey(final K key)
	{
		final List<K> keyBucket = this.keys.get(this.hashValue(key)); //finds the hash value for the key we are looking for, gets the bucket associated with that key, and sets the list keyBucket equal to that linkedlist
		for(int j = 0; j < keyBucket.size(); j++)//for every element in the current bucket
		{
			if(keyBucket.get(j).equals(key)) //if the element at current index = key
			{
				return true;//found
			}
		}//else
		return false; //not found
	}

	//checks if the value is already in use (no guarantee about the specific key, given there may be duplicates)
	public boolean containsValue(final V value)
	{
		for(int i = 0; i < this.table.size(); i++)//for every element in the table (values)
		{
			final List<V> valueBucket = this.table.get(i);//finds current bucket/table slot and sets valueBucket equal to the linekd list of values there
			for(int j = 0; j < valueBucket.size(); j++)//for every element in this table slot
			{
				if(valueBucket.get(j).equals(value))//if the current element = value
				{
					return true;//found
				}
			}
		}
		return false;//not found
	}

	//insert function
	public boolean insert(final K key, final V value)
	{
		int i = this.hashValue(key);
		final List<K> keyBucket = this.keys.get(i);	//get linked list at appropriate bucket (key)
		final List<V> valueBucket = this.table.get(i); //get linked list at appropriate bucket (val)
		if(keyBucket.size()< BUCKET_LIMIT) // if bucket has not reached limit
		{
			keyBucket.add(key); //add key to bucket
			valueBucket.add(value); //add value to bucket
			return true; //if added
		}
		else
		{
			splitBucketPointer++;
			split(keyBucket);
			return true;
		}
	}

	//retrieves the value from the hash table based on the key
	public V retrieve(final K key)
	{
		return find(key,false);
	}

	//removes and returns the value from the hash table based on the key
	public V remove(final K key)
	{
		return find(key,true);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		//provide keys
		sb.append("Keys:\n");
		for(int i = 0; i < this.keys.size(); i++)
		{
			sb.append(i + "|");
			final List<K> keyBucket = this.keys.get(i);
			for(int j = 0; j < keyBucket.size(); j++)
			{
				sb.append(keyBucket.get(j).toString() + ",");
			}
			sb.append("\n");
		}

		//provide values
		sb.append("\nValues:\n");
		for(int i = 0; i < this.table.size(); i++)
		{
			sb.append(i + "|");
			final List<V> valueBucket = this.table.get(i);
			for(int j = 0; j < valueBucket.size(); j++)
			{
				sb.append(valueBucket.get(j).toString() + ",");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	//locates a value based on its key, and may remove the value from the table
	private V find(final K key, boolean remove)
	{
		//retrieve correct bucket
		final int hashValue = this.hashValue(key);
		final List<K> keyBucket = this.keys.get(hashValue);
		final List<V> valueBucket = this.table.get(hashValue);

		//find item
		for(int i = 0; i < keyBucket.size(); i++)
		{
			if(keyBucket.get(i).equals(key))
			{
				if(remove)
				{
					keyBucket.remove(i);
					return valueBucket.remove(i);
				}
				else
				{
					return valueBucket.get(i);
				}
			}
		}
		return null;
	}

	//performs the linear split, where applicable
	private void split(final List<K> keyBucket) // what does this do?
	{
		numLevels++;
		for(int i = 0; i <keyBucket.size(); i++)
		{
			V tempVal= this.find(keyBucket.get(i),true);
			insert(keyBucket.get(i),tempVal);
		}
	}
	/*
	Couldn't figure out how to get this to work appropriately. I wanted to call split when there
	was an overflow and have it update the levels, temporarily store the value, remove it and then
	re insert the key/val with the new hash function
	*/

	//hash value from keys
	private int hashValue(K key)
	{
		return (int)(key.hashCode() % (numBucketsInLevel * (Math.pow(2,numLevels))));
	}

	public static void main(String[] args)
	{
		LinearHashing_HW<String,String> lh = new LinearHashing_HW<>();
		lh.insert("12","A");
		lh.insert("16","B");
		lh.insert("52","C");
		lh.insert("56","D");
		System.out.println(lh.toString());

		System.out.println("First split");
		lh.insert("5","E");
		System.out.println(lh.toString());

		System.out.println("Second split");
		lh.insert("9","F");
		System.out.println(lh.toString());

		System.out.println("Remove");
		System.out.println(lh.remove("12"));
		System.out.println(lh.toString());

		System.out.println(lh.containsKey("12"));
		System.out.println(lh.containsKey("16"));
		System.out.println(lh.containsValue("D"));
	}
}
