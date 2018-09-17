---
type: post
title:  Distributed Bitmap
categories: SBP
parent: data-access-patterns.html
weight: 1900
---



|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| DeWayne Filppi| 9.6 | October 2013|    |    |



# Overview

Any application that has a need for a very large bitmap can benefit from this pattern. The idea is to represent the entire range of values for java native integral types (i.e. byte, short, int, long). In practice, the ranges of byte(256), and short(65536) are too small to be distributed. Long (1.8e19) is too large to represent in any realistic cluster. That leaves int (about 4.3 billion bits), and sub ranges of long (maybe up to 40 bits or so in a large cluster). This example implements the integer (32 bit) bitmap. Practically, this means a bit exists for every possible integer (or sub range of bits).

# Design

As a file system breaks files into blocks suitable for storage on block oriented storage, so can we break the 32-bit range of the integer into arbitrary sized "bit pages", each bit page consisting of a "block" of bits from the integer range.


![intrange.png](/attachment_files/sbp/intrange.png)


The bit page is a space object containing a page of bits. The "page" of bits is simply a byte array that, for the integer example, is a multiple of 4 bytes in length. The example sets the size of each page at 1024 bits (or 128 bytes). This is arbitrary and is something that can be varied based on system needs: larger arrays result in lower storage cost per bit, but make retrieving pages more expensive. As usual, a tradeoff between storage and performance must be made. The BitPage class is simple:



```java
@SpaceClass
public class BitPage {
private static final int PAGE_SIZE_BYTES=128;
private static final int PAGE_SIZE_BITS=PAGE_SIZE_BYTES*8;
private Integer id;
private byte[] bits=new byte[PAGE_SIZE_BYTES];

public BitPage(){}

/**
* Sets a value in the bitmap, and sets the
* id based on the supplied value.
*
* @param value
*/
public BitPage(int value){
id=getPageId(value);
setBit(value);
}

public void setBit(int val) {
int offset=val-id;
bits[offset/8] |= offset%8;
}

public void clearBit(int val){
int offset=val-id;
bits[offset/8] &= 0;
}

@SpaceId(autoGenerate=false)
@SpaceRouting
public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public byte[] getBits() {
return bits;
}

public void setBits(byte[] bits) {
this.bits = bits;
}

public boolean isBitSet(int val){
int offset=val-id;
return (bits[offset/8] & offset%8)!=0;
}

/**
* Calculates the page id for the given
* integer. The space of signed integer
* values is divided into pages of PAGE_SIZE
* bytes. Each page represent 1024 bits
* of the bitmap representing all 4.3+B
* ints. The id of a page is the first
* value represented by the bits it
* contains.
*
* @param val
* @return
*/
public int getPageId(final int val){
long pageno=((long)val-(long)Integer.MIN_VALUE)/PAGE_SIZE_BITS;
return Integer.MIN_VALUE+((int)pageno*PAGE_SIZE_BITS);
}


}
```


Each pages space id consists in the lowest value integer that it represents. For example, the page with id '0' represents bits from 0 to 1023 (in the 1024 bit page size case). So if I want to inspect (or set) the value of bit 5, I need to first fetch page id 0, and then apply a mask. Note the final utility method in the class. It's purpose is to return the page id for any supplied integer.

This is really all you need, but it's not user friendly or efficient. Ideally we don't want to fetch pages across the network. Using XAPs [executor remoting]({{%latestjavaurl%}}/executor-based-remoting.html) we can create a distributed RPC that executes the bit page operations colocated with the page itself. The service implementation looks like this:



```java
@RemotingService
public class BitPageServiceImpl implements BitPageService {
@GigaSpaceContext
private GigaSpace space;

public Boolean exists(Integer val) {
if(val==null)throw new IllegalArgumentException("null val supplied");
int routing=new BitPage().getPageId(val);
BitPage page=space.readById(new IdQuery<BitPage>(BitPage.class,routing,routing));
if(page==null)return false;
return page.isBitSet(val);
}

public void clear(Integer val) {
if(val==null)throw new IllegalArgumentException("null val supplied");

int routing=new BitPage().getPageId(val);
BitPage page=space.readById(new IdQuery<BitPage>(BitPage.class,routing,routing));
if(page==null)return;
page.clearBit(val);
space.write(page);
}

public Boolean set(Integer val) {
if(val==null)throw new IllegalArgumentException("null val supplied");

int routing=new BitPage().getPageId(val);
BitPage page=space.readById(new IdQuery<BitPage>(BitPage.class,routing,routing));
boolean ret=false;
if(page==null){ //new one
page=new BitPage(val);
}
else{
ret=page.isBitSet(val);
}
page.setBit(val);
space.write(page);
return ret;
}

}
```


The service implementation permits the operations on the bitmap to be conducted in terms of integers. If I wish to set or check the bit corresponding to integer 123456, I can simply call the proper method on the service and all is good. Note that the `readById` calls are using the space id as the routing id. This is merely safe programming, but we wouldn't want these calls to actually reference pages in other partitions. In order to ensure that pages are not fetched from other partitions (potentially resulting in a performance diminishing network hop), the service proxy should be constructed carefully:



```java
BitPageService svc=new ExecutorRemotingProxyConfigurer<BitPageService>(space, BitPageService.class)
.remoteRoutingHandler(new RemoteRoutingHandler<Integer>(){
public Integer computeRouting(
SpaceRemotingInvocation remotingEntry) {
return new BitPage().getPageId((Integer)remotingEntry.getArguments()[0]);
}

})
.proxy();

boolean res=svc.set(1);
```


Note that when the service proxy is constructed on the client side, the `getPageId` utility method mentioned previously is called to fetch the target page id (and therefore routing id) of the page requested.

# Conclusion

This solution has shown how to construct a very large distributed bit map using XAP. The original motivation for the pattern was fast duplicate checking over data that potentially could be any integer value. Using the conventional approach of constructing a XAP domain object for each integer proved far too slow and memory intensive. The maximum number of space objects required for a given bit page size S and bit range R is R/S, which in the case of 32 bit integers and page size of 1024 is 4.3 million. Actual storage required maxed out a around 4.3 gigabytes which is certainly manageable. Conceivably, a subset of the range of long (maybe 40 bits) could be used if an extremely large 1TB+ grid were available. The pattern itself is applicable to any problem that requires the storage of a large number of booleans (duplicate checking, existence checking), or even potentially multi-state bit ranges (you could address 2^10 4 bit nibbles in a similar manner with some modifications). The source is available for download from the [patterns](https://github.com/Gigaspaces/bestpractices) github repo.
