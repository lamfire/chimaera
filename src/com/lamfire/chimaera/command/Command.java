package com.lamfire.chimaera.command;



public class Command {
	public static final String PUT = "PUT";
	public static final String GET = "GET";
    public static final String REMOVE = "REMOVE";
    public static final String SIZE = "SIZE";
    public static final String CLEAR = "CLEAR";
    public static final String EXISTS = "EXISTS";

    public static final String INCREMENT_INCR = "INCREMENT_INCR";
    public static final String INCREMENT_INCR_GET = "INCREMENT_INCR_GET";
    public static final String INCREMENT_DECR = "INCREMENT_DECR";
    public static final String INCREMENT_DECR_GET = "INCREMENT_DECR_GET";
    public static final String INCREMENT_GET = "INCREMENT_GET";
    public static final String INCREMENT_SET = "INCREMENT_SET";

    public static final String LIST_ADD = "LIST_ADD";
    public static final String LIST_SET = "LIST_SET";
    public static final String LIST_GET = "LIST_GET";
    public static final String LIST_GETS = "LIST_GETS";
    public static final String LIST_REMOVE = "LIST_REMOVE";
    public static final String LIST_SIZE = "LIST_SIZE";
    public static final String LIST_CLEAR = "LIST_CLEAR";

    public static final String SET_ADD = "SET_ADD";
    public static final String SET_GET = "SET_GET";
    public static final String SET_GETS = "SET_GETS";
    public static final String SET_REMOVE = "SET_REMOVE";
    public static final String SET_SIZE = "SET_SIZE";
    public static final String SET_CLEAR = "SET_CLEAR";
    public static final String SET_EXISTS = "SET_EXISTS";

    public static final String QUEUE_PUSHLEFT = "QUEUE_PUSHLEFT";
    public static final String QUEUE_PUSHRIGHT = "QUEUE_PUSHRIGHT";
    public static final String QUEUE_POPLEFT = "QUEUE_POPLEFT";
    public static final String QUEUE_POPRIGHT = "QUEUE_POPRIGHT";
    public static final String QUEUE_SIZE = "QUEUE_SIZE";
    public static final String QUEUE_CLEAR = "QUEUE_CLEAR";

    public static final String MAP_PUT = "MAP_PUT";
    public static final String MAP_GET = "MAP_GET";
    public static final String MAP_KEYS = "MAP_KEYS";
    public static final String MAP_REMOVE = "MAP_REMOVE";
    public static final String MAP_SIZE = "MAP_SIZE";
    public static final String MAP_CLEAR = "MAP_CLEAR";
    public static final String MAP_EXISTS = "MAP_EXISTS";


    public static final String RANK_PUT = "RANK_PUT";
    public static final String RANK_INCR = "RANK_INCR";
    public static final String RANK_SET = "RANK_SET";
    public static final String RANK_SCORE = "RANK_SCORE";
    public static final String RANK_REMOVE = "RANK_REMOVE";
    public static final String RANK_SIZE = "RANK_SIZE";
    public static final String RANK_CLEAR = "RANK_CLEAR";
    public static final String RANK_MAX = "RANK_MAX";
    public static final String RANK_MIN = "RANK_MIN";
    public static final String RANK_MAX_RANGE = "RANK_MAX_RANGE";
    public static final String RANK_MIN_RANGE = "RANK_MIN_RANGE";


    public static final String SUBSCRIBE_BIND = "SUBSCRIBE_BIND";
    public static final String SUBSCRIBE_UNBIND = "SUBSCRIBE_UNBIND";
    public static final String SUBSCRIBE_PUBLISH = "SUBSCRIBE_PUBLISH";
	
	private String store;
    private String command;
	private String key;
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "Command{" +
                "store='" + store + '\'' +
                ", command='" + command + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
