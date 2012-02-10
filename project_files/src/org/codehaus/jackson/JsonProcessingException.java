/*    */ package org.codehaus.jackson;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class JsonProcessingException extends IOException
/*    */ {
/*    */   static final long serialVersionUID = 123L;
/*    */   protected JsonLocation mLocation;
/*    */ 
/*    */   protected JsonProcessingException(String msg, JsonLocation loc, Throwable rootCause)
/*    */   {
/* 22 */     super(msg);
/* 23 */     if (rootCause != null) {
/* 24 */       initCause(rootCause);
/*    */     }
/* 26 */     this.mLocation = loc;
/*    */   }
/*    */ 
/*    */   protected JsonProcessingException(String msg)
/*    */   {
/* 31 */     super(msg);
/*    */   }
/*    */ 
/*    */   protected JsonProcessingException(String msg, JsonLocation loc)
/*    */   {
/* 36 */     this(msg, loc, null);
/*    */   }
/*    */ 
/*    */   protected JsonProcessingException(String msg, Throwable rootCause)
/*    */   {
/* 41 */     this(msg, null, rootCause);
/*    */   }
/*    */ 
/*    */   protected JsonProcessingException(Throwable rootCause)
/*    */   {
/* 46 */     this(null, null, rootCause);
/*    */   }
/*    */ 
/*    */   public JsonLocation getLocation()
/*    */   {
/* 51 */     return this.mLocation;
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 60 */     String msg = super.getMessage();
/* 61 */     if (msg == null) {
/* 62 */       msg = "N/A";
/*    */     }
/* 64 */     JsonLocation loc = getLocation();
/* 65 */     if (loc != null) {
/* 66 */       StringBuilder sb = new StringBuilder();
/* 67 */       sb.append(msg);
/* 68 */       sb.append('\n');
/* 69 */       sb.append(" at ");
/* 70 */       sb.append(loc.toString());
/* 71 */       return sb.toString();
/*    */     }
/* 73 */     return msg;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 78 */     return getClass().getName() + ": " + getMessage();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonProcessingException
 * JD-Core Version:    0.6.0
 */