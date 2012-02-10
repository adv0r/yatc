/*    */ package org.codehaus.jackson.map.util;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class LRUMap<K, V> extends LinkedHashMap<K, V>
/*    */ {
/*    */   protected final int _maxEntries;
/*    */ 
/*    */   public LRUMap(int initialEntries, int maxEntries)
/*    */   {
/* 18 */     super(initialEntries, 0.8F, true);
/* 19 */     this._maxEntries = maxEntries;
/*    */   }
/*    */ 
/*    */   protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
/*    */   {
/* 25 */     return size() > this._maxEntries;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.LRUMap
 * JD-Core Version:    0.6.0
 */