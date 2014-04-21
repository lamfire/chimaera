package com.lamfire.thalia.serializer;

/**
 * Header byte, is used at start of each record to indicate data type WARNING
 * !!! values bellow must be unique !!!!!
 */
public final class SerializationHeader {

	public final static int NULL = 0;
	public final static int NORMAL = 1;
	public final static int BOOLEAN_TRUE = 2;
	public final static int BOOLEAN_FALSE = 3;
	public final static int INTEGER_MINUS_1 = 4;
	public final static int INTEGER_0 = 5;
	public final static int INTEGER_1 = 6;
	public final static int INTEGER_2 = 7;
	public final static int INTEGER_3 = 8;
	public final static int INTEGER_4 = 9;
	public final static int INTEGER_5 = 10;
	public final static int INTEGER_6 = 11;
	public final static int INTEGER_7 = 12;
	public final static int INTEGER_8 = 13;
	public final static int INTEGER_255 = 14;
	public final static int INTEGER_PACK_NEG = 15;
	public final static int INTEGER_PACK = 16;
	public final static int LONG_MINUS_1 = 17;
	public final static int LONG_0 = 18;
	public final static int LONG_1 = 19;
	public final static int LONG_2 = 20;
	public final static int LONG_3 = 21;
	public final static int LONG_4 = 22;
	public final static int LONG_5 = 23;
	public final static int LONG_6 = 24;
	public final static int LONG_7 = 25;
	public final static int LONG_8 = 26;
	public final static int LONG_PACK_NEG = 27;
	public final static int LONG_PACK = 28;
	public final static int LONG_255 = 29;
	public final static int LONG_MINUS_MAX = 30;
	public final static int SHORT_MINUS_1 = 31;
	public final static int SHORT_0 = 32;
	public final static int SHORT_1 = 33;
	public final static int SHORT_255 = 34;
	public final static int SHORT_FULL = 35;
	public final static int BYTE_MINUS_1 = 36;
	public final static int BYTE_0 = 37;
	public final static int BYTE_1 = 38;
	public final static int BYTE_FULL = 39;
	public final static int CHAR = 40;
	public final static int FLOAT_MINUS_1 = 41;
	public final static int FLOAT_0 = 42;
	public final static int FLOAT_1 = 43;
	public final static int FLOAT_255 = 44;
	public final static int FLOAT_SHORT = 45;
	public final static int FLOAT_FULL = 46;
	public final static int DOUBLE_MINUS_1 = 47;
	public final static int DOUBLE_0 = 48;
	public final static int DOUBLE_1 = 49;
	public final static int DOUBLE_255 = 50;
	public final static int DOUBLE_SHORT = 51;
	public final static int DOUBLE_FULL = 52;
	public final static int DOUBLE_ARRAY = 53;
	public final static int BIGDECIMAL = 54;
	public final static int BIGINTEGER = 55;
	public final static int FLOAT_ARRAY = 56;
	public final static int INTEGER_MINUS_MAX = 57;
	public final static int SHORT_ARRAY = 58;
	public final static int BOOLEAN_ARRAY = 59;

	public final static int ARRAY_INT_B_255 = 60;
	public final static int ARRAY_INT_B_INT = 61;
	public final static int ARRAY_INT_S = 62;
	public final static int ARRAY_INT_I = 63;
	public final static int ARRAY_INT_PACKED = 64;

	public final static int ARRAY_LONG_B = 65;
	public final static int ARRAY_LONG_S = 66;
	public final static int ARRAY_LONG_I = 67;
	public final static int ARRAY_LONG_L = 68;
	public final static int ARRAY_LONG_PACKED = 69;

	public final static int CHAR_ARRAY = 70;
	public final static int ARRAY_BYTE_INT = 71;

	public final static int NOTUSED_ARRAY_OBJECT_255 = 72;
	public final static int ARRAY_OBJECT = 73;
	// special cases for BTree values which stores references
	public final static int ARRAY_OBJECT_PACKED_LONG = 74;
	public final static int ARRAYLIST_PACKED_LONG = 75;

	public final static int STRING_EMPTY = 101;
	public final static int NOTUSED_STRING_255 = 102;
	public final static int STRING = 103;
	public final static int NOTUSED_ARRAYLIST_255 = 104;
	public final static int ARRAYLIST = 105;

	public final static int NOTUSED_TREEMAP_255 = 106;
	public final static int TREEMAP = 107;
	public final static int NOTUSED_HASHMAP_255 = 108;
	public final static int HASHMAP = 109;
	public final static int NOTUSED_LINKEDHASHMAP_255 = 110;
	public final static int LINKEDHASHMAP = 111;

	public final static int NOTUSED_TREESET_255 = 112;
	public final static int TREESET = 113;
	public final static int NOTUSED_HASHSET_255 = 114;
	public final static int HASHSET = 115;
	public final static int NOTUSED_LINKEDHASHSET_255 = 116;
	public final static int LINKEDHASHSET = 117;
	public final static int NOTUSED_LINKEDLIST_255 = 118;
	public final static int LINKEDLIST = 119;

	public final static int NOTUSED_VECTOR_255 = 120;
	public final static int VECTOR = 121;
	public final static int IDENTITYHASHMAP = 122;
	public final static int HASHTABLE = 123;
	public final static int LOCALE = 124;
	public final static int PROPERTIES = 125;

	public final static int CLASS = 126;
	public final static int DATE = 127;
	public final static int UUID = 128;

	public static final int JDBMLINKEDLIST = 159;
	public static final int HTREE = 160;

	public final static int BTREE = 161;

	public static final int BTREE_NODE_LEAF = 162;
	public static final int BTREE_NODE_NONLEAF = 163;
	public static final int HTREE_BUCKET = 164;
	public static final int HTREE_DIRECTORY = 165;
	/**
	 * used for reference to already serialized object in object graph
	 */
	public static final int OBJECT_STACK = 166;
	public static final int JAVA_SERIALIZATION = 172;

}