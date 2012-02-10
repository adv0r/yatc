/*    */ package org.codehaus.jackson;
/*    */ 
/*    */ public class JsonParseException extends JsonProcessingException
/*    */ {
/*    */   static final long serialVersionUID = 123L;
/*    */ 
/*    */   public JsonParseException(String msg, JsonLocation loc)
/*    */   {
/* 15 */     super(msg, loc);
/*    */   }
/*    */ 
/*    */   public JsonParseException(String msg, JsonLocation loc, Throwable root)
/*    */   {
/* 20 */     super(msg, loc, root);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonParseException
 * JD-Core Version:    0.6.0
 */