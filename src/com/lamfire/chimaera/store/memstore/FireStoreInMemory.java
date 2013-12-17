package com.lamfire.chimaera.store.memstore;

import com.lamfire.chimaera.ChimaeraException;
import com.lamfire.chimaera.store.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FireStoreInMemory implements FireStore {

    private final Map<String, Object> store = new ConcurrentHashMap<String, Object>();
	
	boolean isInstance(String key,Class<?> type){
		Object obj = store.get(key);
		return type.isInstance(obj);
	}

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public void clear() {
        store.clear();
    }
	
	@SuppressWarnings("unchecked")
	private <T> T assertInstance(String key,Class<T> type){
		Object obj = store.get(key);
		if(obj == null){
			return null;
		}
		if( type.isInstance(obj)){
			return (T)obj;
		}
		throw new ChimaeraException(obj.getClass().getName() + " not instance of " + type.getName());
	}

	@Override
	public FireIncrement getFireIncrement(String key){
		FireIncrement result =   assertInstance(key,FireIncrement.class);
		if(result == null){
			result = FireFactory.newFireIncrement();
			store.put(key, result);
		}
		return result;
	}

	@Override
	public FireList getFireList(String key){
		FireList result =   assertInstance(key,FireList.class);
		if(result == null){
			result = FireFactory.newFireList();
			store.put(key, result);
		}
		return result;
	}

	@Override
	public FireMap getFireMap(String key){
		FireMap result =   assertInstance(key,FireMap.class);
		if(result == null){
			result = FireFactory.newFireMap();
			store.put(key, result);
		}
		return result;
	}

	@Override
	public FireQueue getFireQueue(String key){
		FireQueue result =   assertInstance(key,FireQueue.class);
		if(result == null){
			result = FireFactory.newFireQueue();
			store.put(key, result);
		}
		return result;
	}

	@Override
	public FireSet getFireSet(String key){
		FireSet result =   assertInstance(key,FireSet.class);
		if(result == null){
			result = FireFactory.newFireSet();
			store.put(key, result);
		}
		return result;
	}

    @Override
    public FireRank getFireRank(String key) {
        FireRank result =   assertInstance(key,FireRank.class);
        if(result == null){
            result = FireFactory.newFireCounter();
            store.put(key, result);
        }
        return result;
    }

	@Override
	public void remove(String key) {
		store.remove(key);
	}

	@Override
	public int size(String key){
        try{
             FireCollection c = assertInstance(key,FireCollection.class);
             return c.size();
        }catch (Exception e){
            throw new ChimaeraException("The key["+key+"] cannot read size,must be instanceof 'FireCollection'");
        }
	}

	@Override
	public void clear(String key) {
        try{
            FireCollection c = assertInstance(key,FireCollection.class);
            c.clear();
        }catch (Exception e){
            throw new ChimaeraException("The key["+key+"] cannot read size,must be instanceof 'FireCollection'");
        }
	}

	@Override
	public boolean exists(String key) {
		return store.containsKey(key);
	}

}
