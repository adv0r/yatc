/*    */ package org.codehaus.jackson.map.ser.impl;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.codehaus.jackson.map.JsonSerializer;
/*    */ import org.codehaus.jackson.map.ser.SerializerCache.TypeKey;
/*    */ 
/*    */ public class JsonSerializerMap
/*    */ {
/*    */   private final Bucket[] _buckets;
/*    */   private final int _size;
/*    */ 
/*    */   public JsonSerializerMap(Map<SerializerCache.TypeKey, JsonSerializer<Object>> serializers)
/*    */   {
/* 21 */     int size = findSize(serializers.size());
/* 22 */     this._size = size;
/* 23 */     int hashMask = size - 1;
/* 24 */     Bucket[] buckets = new Bucket[size];
/* 25 */     for (Map.Entry entry : serializers.entrySet()) {
/* 26 */       SerializerCache.TypeKey key = (SerializerCache.TypeKey)entry.getKey();
/* 27 */       int index = key.hashCode() & hashMask;
/* 28 */       buckets[index] = new Bucket(buckets[index], key, (JsonSerializer)entry.getValue());
/*    */     }
/* 30 */     this._buckets = buckets;
/*    */   }
/*    */ 
/*    */   private static final int findSize(int size)
/*    */   {
/* 36 */     int needed = size <= 64 ? size + size : size + (size >> 2);
/* 37 */     int result = 8;
/* 38 */     while (result < needed) {
/* 39 */       result += result;
/*    */     }
/* 41 */     return result;
/*    */   }
/*    */ 
/*    */   public int size()
/*    */   {
/* 50 */     return this._size;
/*    */   }
/*    */ 
/*    */   public JsonSerializer<Object> find(SerializerCache.TypeKey key) {
/* 54 */     int index = key.hashCode() & this._buckets.length - 1;
/* 55 */     Bucket bucket = this._buckets[index];
/*    */ 
/* 60 */     if (bucket == null) {
/* 61 */       return null;
/*    */     }
/* 63 */     if (key.equals(bucket.key)) {
/* 64 */       return bucket.value;
/*    */     }
/* 66 */     while ((bucket = bucket.next) != null) {
/* 67 */       if (key.equals(bucket.key)) {
/* 68 */         return bucket.value;
/*    */       }
/*    */     }
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   private static final class Bucket
/*    */   {
/*    */     public final SerializerCache.TypeKey key;
/*    */     public final JsonSerializer<Object> value;
/*    */     public final Bucket next;
/*    */ 
/*    */     public Bucket(Bucket next, SerializerCache.TypeKey key, JsonSerializer<Object> value)
/*    */     {
/* 88 */       this.next = next;
/* 89 */       this.key = key;
/* 90 */       this.value = value;
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.JsonSerializerMap
 * JD-Core Version:    0.6.0
 */