/*    */ package org.codehaus.jackson;
/*    */ 
/*    */ public enum JsonEncoding
/*    */ {
/* 15 */   UTF8("UTF-8", false), 
/* 16 */   UTF16_BE("UTF-16BE", true), 
/* 17 */   UTF16_LE("UTF-16LE", false), 
/* 18 */   UTF32_BE("UTF-32BE", true), 
/* 19 */   UTF32_LE("UTF-32LE", false);
/*    */ 
/*    */   final String mJavaName;
/*    */   final boolean mBigEndian;
/*    */ 
/*    */   private JsonEncoding(String javaName, boolean bigEndian)
/*    */   {
/* 28 */     this.mJavaName = javaName;
/* 29 */     this.mBigEndian = bigEndian;
/*    */   }
/*    */ 
/*    */   public String getJavaName()
/*    */   {
/* 37 */     return this.mJavaName;
/*    */   }
/*    */ 
/*    */   public boolean isBigEndian()
/*    */   {
/* 47 */     return this.mBigEndian;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonEncoding
 * JD-Core Version:    0.6.0
 */