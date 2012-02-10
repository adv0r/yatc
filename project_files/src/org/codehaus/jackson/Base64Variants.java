/*    */ package org.codehaus.jackson;
/*    */ 
/*    */ public final class Base64Variants
/*    */ {
/*    */   static final String STD_BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
/* 38 */   public static final Base64Variant MIME = new Base64Variant("MIME", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", true, '=', 76);
/*    */ 
/* 49 */   public static final Base64Variant MIME_NO_LINEFEEDS = new Base64Variant(MIME, "MIME-NO-LINEFEEDS", 2147483647);
/*    */ 
/* 56 */   public static final Base64Variant PEM = new Base64Variant(MIME, "PEM", true, '=', 64);
/*    */   public static final Base64Variant MODIFIED_FOR_URL;
/*    */ 
/*    */   public static Base64Variant getDefaultVariant()
/*    */   {
/* 88 */     return MIME_NO_LINEFEEDS;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 71 */     StringBuffer sb = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
/*    */ 
/* 73 */     sb.setCharAt(sb.indexOf("+"), '-');
/* 74 */     sb.setCharAt(sb.indexOf("/"), '_');
/*    */ 
/* 78 */     MODIFIED_FOR_URL = new Base64Variant("MODIFIED-FOR-URL", sb.toString(), false, '\000', 2147483647);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.Base64Variants
 * JD-Core Version:    0.6.0
 */