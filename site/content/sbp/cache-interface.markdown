---
type: post
title:  Cache Interface
categories: SBP
parent: data-access-patterns.html
weight: 100
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 9.6| February 2014|  |     |





# Overview

GigaSpaces XAP provides a powerful IMDG with advanced data access options. Many times a simple key/value interface required to access the IMDG. The [Map API]({{%latestjavaurl%}}/map-api.html) or the [GigaSpace API]({{%latestjavaurl%}}/the-gigaspace-interface-overview.html) can be used in such a case. A simple wrapper around the GigaSpace API described here exposing a `HashMap` interface. 

![cache-service.jpg](/attachment_files/sbp/cache-service.jpg)


# Cache Interface Implementation

The implementation includes two classes. The `Data` class that model the data within the space and the `CacheService` class that wraps the `GigaSpace` API using standard `Map` API (`put`,`get` , `remove`..). 

The `CacheService` support inserting data using a key/value and also via region/key/value. A region allows you to mark entries with a "tag" that groups these for better management. When constructing a `CacheService` you may indicate if you would like to have the data cached also at the client side. In such a case , the client will have a copy of the data maintained for fast data access avoiding any remote calls or serialization activity. Once the client application constructs the `CacheService` all cached data will be pre-loaded into the client side. Make sure the client will have sufficient heap size configured (`Xmx`) to accommodate all entries.

## The Data Space Class

The Data Space Class holds the key , value and region data within a simple POJO properties. The `@SpaceId` annotation indicates the key field specified as the Space class ID field. 


```java
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;

public class Data {
	public Data(){}

	String key;
	Object value;
	String region;

	@SpaceIndex
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region= region;
	}
	@SpaceId(autoGenerate=false)
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}

```

### Optimzing Value Object Serialization and Storage
The `Data` class can be modified to support better serialization for the value object. You may store the value object in binary format or compressed format.

To store the value object in a binary format the `getValue` should have the following:

```java
@SpaceStorageType(storageType=StorageType.BINARY)
public Object getValue() {
	return value;
}
```

To store the value object in a compressed format the `getValue` should have the following:

```java
@SpaceStorageType(storageType=StorageType.COMPRESSED)
public Object getValue() {
	return value;
}
```

## The CacheService
The `CacheService` leveraging the `GigaSpace` interface implementing the standard `put`,`get`,`putAll`,`getAll`,`remove`,etc. methods:

```java
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.openspaces.core.space.cache.LocalViewSpaceConfigurer;

import com.gigaspaces.client.ReadByIdsResult;
import com.gigaspaces.query.IdQuery;
import com.gigaspaces.query.IdsQuery;
import com.j_spaces.core.client.SQLQuery;

public class CacheService {
	private GigaSpace spaceView;
	private GigaSpace space;
	boolean localView;

	public CacheService(String url) throws Exception {
		init(url, false);
	}

	public CacheService(String url, boolean clientCache) throws Exception {
		init(url, clientCache);
	}

	public void init(String url, boolean clientCache) throws Exception {
		this.localView = clientCache;
		UrlSpaceConfigurer urlConfigurer = new UrlSpaceConfigurer(url);
		space = new GigaSpaceConfigurer(urlConfigurer).gigaSpace();
		if (localView) {
			LocalViewSpaceConfigurer localViewConfigurer = new LocalViewSpaceConfigurer(
					urlConfigurer).addViewQuery(new SQLQuery<Data>(Data.class,
					""));
			// Create local view:
			spaceView = new GigaSpaceConfigurer(localViewConfigurer)
					.gigaSpace();
		} else {
			spaceView = space;
		}

	}

	public void put(String key, Object value) throws Exception {
		put(null, key, value);
	}
	
	public void put(String region, String key, Object value) throws Exception {
		Data d = new Data();
		d.setKey(key);
		d.setValue(value);
		d.setRegion(region);
		space.write(d);
	}

	public Object get(String region, String key) throws Exception {
		Data templ = new Data();
		templ.setKey(key);
		templ.setRegion(region);
		Data d = spaceView.read(templ);
		if (d != null)
			return d.getValue();
		else
			return null;
	}

	public void clear() {
		space.clear(new Data());
	}

	public Object get(String key) throws Exception {
		Data d = spaceView.readById(Data.class, key);
		if (d != null)
			return d.getValue();
		else
			return null;
	}

	public void remove(String region, String key) throws Exception {
		Data templ = new Data();
		templ.setKey(key);
		templ.setRegion(region);
		space.clear(templ);
	}

	public void remove(String key) throws Exception {
		IdQuery<Data> idquery = new IdQuery<Data>(Data.class, key);
		space.clear(idquery);
	}

	public boolean containsKey(String key) {
		IdQuery<Data> idquery = new IdQuery<Data>(Data.class, key);
		int count = spaceView.count(idquery);
		return (count > 0);
	}

	public int size() throws Exception {
		return spaceView.count(new Data());
	}

	public int size(String region) throws Exception {
		Data templ = new Data();
		templ.setRegion(region);
		return spaceView.count(templ);
	}

	public Map<String, Object> getAll(List<String> keys) {
		Map<String, Object> map = new HashMap<String, Object>();
		String keysArray[] = new String[keys.size()];
		keys.toArray(keysArray);
		IdsQuery<Data> idsquery = new IdsQuery<Data>(Data.class, keysArray);
		ReadByIdsResult<Data> result= spaceView.readByIds(idsquery);
		Iterator<Data> iter = result.iterator();
		while (iter.hasNext())
		{
			Data data = iter.next();
			map.put(data.getKey(), data.getValue());
		}
		return map;
	}
	
	public void putAll(String region, Map<String, Object> map) {
		Data d[] = new Data[map.size()];
		int count = 0;
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Object val = map.get(key);
			d[count] = new Data();
			d[count].setKey(key);
			d[count].setValue(val);
			d[count].setRegion(region);
			count++;
		}
		space.writeMultiple(d);
	}

	public void putAll(Map<String, Object> map) {
		putAll(null, map);
	}

	 public Collection<Object> values() {
			SQLQuery<Data> query = null;
			Set<Object> values = new HashSet<Object>();
			if (localView)
				query = new SQLQuery<Data>(Data.class, "");
			else
				query = new SQLQuery<Data>(Data.class, "").setProjections("value");

			Data d[] = spaceView.readMultiple(query);

			for (int i = 0; i < d.length; i++) {
				values.add(d[i].getValue());
			}
			return values;

	 }

	public Set<String> keySet() throws Exception {
		SQLQuery<Data> query = null;
		if (localView)
			query = new SQLQuery<Data>(Data.class, "");
		else
			query = new SQLQuery<Data>(Data.class, "").setProjections("key");

		Data d[] = spaceView.readMultiple(query);

		Set<String> keys = new HashSet<String>();

		for (int i = 0; i < d.length; i++) {
			keys.add(d[i].getKey());
		}
		return keys;
	}
}

```

See example how the `CacheService` can be used accessing IMDG called 'mySpace':

```java
CacheService cacheService = new CacheService("jini//*/*/mySpace");

cacheService.put("key1", "value1");
cacheService.put("region1" , "key2", "value2");
		
cacheService.remove("key1");

cacheService.clear();

Map<String, Object> map = new HashMap<String, Object>();
map.put("key1", "value");
map.put("key2", "value");
map.put("key3", "value");
cacheService.putAll(map);

```

{{%refer%}}

- [Modeling and Accessing Your Data]({{%latestjavaurl%}}/modeling-your-data.html)
- [Deploying and Interacting with the Space](./deploying-and-interacting-with-the-space.html)

{{%/refer%}}