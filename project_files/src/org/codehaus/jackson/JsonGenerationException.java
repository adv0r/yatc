/*    */ package org.codehaus.jackson;
/*    */ 
/*    */ public class JsonGenerationException extends JsonProcessingException
/*    */ {
/*    */   static final long serialVersionUID = 123L;
/*    */ 
/*    */   public JsonGenerationException(Throwable rootCause)
/*    */   {
/* 15 */     super(rootCause);
/*    */   }
/*    */ 
/*    */   public JsonGenerationException(String msg)
/*    */   {
/* 20 */     super(msg, (JsonLocation)null);
/*    */   }
/*    */ 
/*    */   public JsonGenerationException(String msg, Throwable rootCause)
/*    */   {
/* 25 */     super(msg, (JsonLocation)null, rootCause);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonGenerationException
 * JD-Core Version:    0.6.0
 */