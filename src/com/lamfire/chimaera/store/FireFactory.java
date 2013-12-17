package com.lamfire.chimaera.store;

import com.lamfire.chimaera.store.memstore.*;

public class FireFactory {
	
	public static FireIncrement newFireIncrement(){
		return new FireIncrementInMemory();
	}

	public static FireList newFireList(){
		return new FireListInMemory();
	}
	
	public static FireMap newFireMap(){
		return new FireMapInMemory();
	}
	
	public static FireQueue newFireQueue(){
		return new FireQueueInMemory();
	}
	
	public static FireSet newFireSet(){
		return new FireSetInMemory();
	}

    public static FireRank newFireCounter(){
        return new FireRankInMemory();
    }
}
