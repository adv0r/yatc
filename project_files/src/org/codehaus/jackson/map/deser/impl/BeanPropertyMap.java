/*     */ package org.codehaus.jackson.map.deser.impl;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.codehaus.jackson.map.deser.SettableBeanProperty;
/*     */ 
/*     */ public final class BeanPropertyMap
/*     */ {
/*     */   private final Bucket[] _buckets;
/*     */   private final int _hashMask;
/*     */   private final int _size;
/*     */ 
/*     */   public BeanPropertyMap(Collection<SettableBeanProperty> properties)
/*     */   {
/*  28 */     this._size = properties.size();
/*  29 */     int bucketCount = findSize(this._size);
/*  30 */     this._hashMask = (bucketCount - 1);
/*  31 */     Bucket[] buckets = new Bucket[bucketCount];
/*  32 */     for (SettableBeanProperty property : properties) {
/*  33 */       String key = property.getName();
/*  34 */       int index = key.hashCode() & this._hashMask;
/*  35 */       buckets[index] = new Bucket(buckets[index], key, property);
/*     */     }
/*  37 */     this._buckets = buckets;
/*     */   }
/*     */ 
/*     */   public void assignIndexes()
/*     */   {
/*  43 */     int index = 0;
/*  44 */     for (Bucket bucket : this._buckets)
/*  45 */       while (bucket != null) {
/*  46 */         bucket.value.assignIndex(index++);
/*  47 */         bucket = bucket.next;
/*     */       }
/*     */   }
/*     */ 
/*     */   private static final int findSize(int size)
/*     */   {
/*  55 */     int needed = size <= 32 ? size + size : size + (size >> 2);
/*  56 */     int result = 2;
/*  57 */     while (result < needed) {
/*  58 */       result += result;
/*     */     }
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  69 */     return this._size;
/*     */   }
/*     */ 
/*     */   public Iterator<SettableBeanProperty> allProperties()
/*     */   {
/*  75 */     return new IteratorImpl(this._buckets);
/*     */   }
/*     */ 
/*     */   public SettableBeanProperty find(String key)
/*     */   {
/*  80 */     int index = key.hashCode() & this._hashMask;
/*  81 */     Bucket bucket = this._buckets[index];
/*     */ 
/*  83 */     if (bucket == null) {
/*  84 */       return null;
/*     */     }
/*     */ 
/*  87 */     if (bucket.key == key) {
/*  88 */       return bucket.value;
/*     */     }
/*  90 */     while ((bucket = bucket.next) != null) {
/*  91 */       if (bucket.key == key) {
/*  92 */         return bucket.value;
/*     */       }
/*     */     }
/*     */ 
/*  96 */     return _findWithEquals(key, index);
/*     */   }
/*     */ 
/*     */   public void replace(SettableBeanProperty property)
/*     */   {
/* 106 */     String name = property.getName();
/* 107 */     int index = name.hashCode() & this._buckets.length - 1;
/*     */ 
/* 112 */     Bucket tail = null;
/* 113 */     boolean found = false;
/*     */ 
/* 116 */     for (Bucket bucket = this._buckets[index]; bucket != null; bucket = bucket.next)
/*     */     {
/* 118 */       if ((!found) && (bucket.key.equals(name)))
/* 119 */         found = true;
/*     */       else {
/* 121 */         tail = new Bucket(tail, bucket.key, bucket.value);
/*     */       }
/*     */     }
/*     */ 
/* 125 */     if (!found) {
/* 126 */       throw new NoSuchElementException("No entry '" + property + "' found, can't replace");
/*     */     }
/*     */ 
/* 129 */     this._buckets[index] = new Bucket(tail, name, property);
/*     */   }
/*     */ 
/*     */   private SettableBeanProperty _findWithEquals(String key, int index)
/*     */   {
/* 140 */     Bucket bucket = this._buckets[index];
/* 141 */     while (bucket != null) {
/* 142 */       if (key.equals(bucket.key)) {
/* 143 */         return bucket.value;
/*     */       }
/* 145 */       bucket = bucket.next;
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   private static final class IteratorImpl
/*     */     implements Iterator<SettableBeanProperty>
/*     */   {
/*     */     private final BeanPropertyMap.Bucket[] _buckets;
/*     */     private BeanPropertyMap.Bucket _currentBucket;
/*     */     private int _nextBucketIndex;
/*     */ 
/*     */     public IteratorImpl(BeanPropertyMap.Bucket[] buckets)
/*     */     {
/* 188 */       this._buckets = buckets;
/*     */ 
/* 190 */       int i = 0;
/* 191 */       for (int len = this._buckets.length; i < len; ) {
/* 192 */         BeanPropertyMap.Bucket b = this._buckets[(i++)];
/* 193 */         if (b != null) {
/* 194 */           this._currentBucket = b;
/* 195 */           break;
/*     */         }
/*     */       }
/* 198 */       this._nextBucketIndex = i;
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 203 */       return this._currentBucket != null;
/*     */     }
/*     */ 
/*     */     public SettableBeanProperty next()
/*     */     {
/* 209 */       BeanPropertyMap.Bucket curr = this._currentBucket;
/* 210 */       if (curr == null) {
/* 211 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/* 214 */       BeanPropertyMap.Bucket b = curr.next;
/* 215 */       while ((b == null) && (this._nextBucketIndex < this._buckets.length)) {
/* 216 */         b = this._buckets[(this._nextBucketIndex++)];
/*     */       }
/* 218 */       this._currentBucket = b;
/* 219 */       return curr.value;
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 224 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Bucket
/*     */   {
/*     */     public final Bucket next;
/*     */     public final String key;
/*     */     public final SettableBeanProperty value;
/*     */ 
/*     */     public Bucket(Bucket next, String key, SettableBeanProperty value)
/*     */     {
/* 164 */       this.next = next;
/* 165 */       this.key = key;
/* 166 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.impl.BeanPropertyMap
 * JD-Core Version:    0.6.0
 */