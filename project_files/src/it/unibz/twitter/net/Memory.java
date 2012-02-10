/*    */ package it.unibz.twitter.net;
/*    */ 
/*    */ import it.unibz.twitter.model.Token;
/*    */ import java.util.prefs.BackingStoreException;
/*    */ import java.util.prefs.Preferences;
/*    */ 
/*    */ public class Memory
/*    */ {
/*    */   public static final String TOKEN = "token";
/*    */   public static final String TOKEN_SECRET = "secret";
/*    */   public static final String LAST_TWEET = "lastTweet";
/*    */   private Preferences prefs;
/*    */ 
/*    */   public Memory()
/*    */   {
/* 19 */     this.prefs = Preferences.userNodeForPackage(getClass());
/*    */   }
/*    */ 
/*    */   public void setLastViewedTweet(String id) {
/* 23 */     this.prefs.put("lastTweet", id);
/*    */     try {
/* 25 */       this.prefs.flush();
/*    */     } catch (BackingStoreException e) {
/* 27 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public String getLastViewedTweet() {
/* 32 */     return this.prefs.get("lastTweet", "1");
/*    */   }
/*    */ 
/*    */   public void setUserToken(Token token) {
/* 36 */     this.prefs.put("token", token.getToken());
/* 37 */     this.prefs.put("secret", token.getSecret());
/*    */     try {
/* 39 */       this.prefs.flush();
/*    */     } catch (BackingStoreException e) {
/* 41 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public Token getUserToken()
/*    */   {
/* 47 */     String token = this.prefs.get("token", null);
/* 48 */     String tokenSecret = this.prefs.get("secret", null);
/* 49 */     if ((token == null) || (tokenSecret == null)) return null;
/* 50 */     return new Token(token, tokenSecret);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.net.Memory
 * JD-Core Version:    0.6.0
 */