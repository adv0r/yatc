/*    */ package org.codehaus.jackson.map;
/*    */ 
/*    */ public class RuntimeJsonMappingException extends RuntimeException
/*    */ {
/*    */   public RuntimeJsonMappingException(JsonMappingException cause)
/*    */   {
/* 11 */     super(cause);
/*    */   }
/*    */ 
/*    */   public RuntimeJsonMappingException(String message) {
/* 15 */     super(message);
/*    */   }
/*    */ 
/*    */   public RuntimeJsonMappingException(String message, JsonMappingException cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.RuntimeJsonMappingException
 * JD-Core Version:    0.6.0
 */