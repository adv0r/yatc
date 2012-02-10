/*    */ package it.unibz.twitter.model;
/*    */ 
/*    */ public class Token
/*    */ {
/*    */   private final String token;
/*    */   private final String secret;
/*    */ 
/*    */   public Token(String token, String secret)
/*    */   {
/*  9 */     this.token = token;
/* 10 */     this.secret = secret;
/*    */   }
/*    */ 
/*    */   public String getToken() {
/* 14 */     return this.token;
/*    */   }
/*    */ 
/*    */   public String getSecret() {
/* 18 */     return this.secret;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 23 */     Token to = (Token)obj;
/* 24 */     return (to.getToken().equals(this.token)) && (to.getSecret().equals(this.secret));
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.model.Token
 * JD-Core Version:    0.6.0
 */