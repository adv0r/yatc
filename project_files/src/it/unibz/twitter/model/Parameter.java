/*    */ package it.unibz.twitter.model;
/*    */ 
/*    */ public class Parameter
/*    */   implements Comparable<Parameter>
/*    */ {
/*    */   public String value;
/*    */   public String key;
/*    */ 
/*    */   public Parameter(String key, String value)
/*    */   {
/*  9 */     this.key = key;
/* 10 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public int compareTo(Parameter object)
/*    */   {
/* 15 */     return this.key.compareTo(object.key);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.model.Parameter
 * JD-Core Version:    0.6.0
 */