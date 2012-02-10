/*    */ package org.codehaus.jackson.util;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public final class InternCache extends LinkedHashMap<String, String>
/*    */ {
/*    */   private static final int MAX_ENTRIES = 192;
/* 25 */   public static final InternCache instance = new InternCache();
/*    */ 
/*    */   private InternCache() {
/* 28 */     super(192, 0.8F, true);
/*    */   }
/*    */ 
/*    */   protected boolean removeEldestEntry(Map.Entry<String, String> eldest)
/*    */   {
/* 34 */     return size() > 192;
/*    */   }
/*    */ 
/*    */   public synchronized String intern(String input)
/*    */   {
/* 39 */     String result = (String)get(input);
/* 40 */     if (result == null) {
/* 41 */       result = input.intern();
/* 42 */       put(result, result);
/*    */     }
/* 44 */     return result;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.InternCache
 * JD-Core Version:    0.6.0
 */