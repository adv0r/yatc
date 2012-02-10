/*    */ package it.unibz.twitter.utils;
/*    */ 
/*    */ import it.unibz.twitter.model.Parameter;
/*    */ import it.unibz.twitter.model.Token;
/*    */ import java.io.PrintStream;
/*    */ import java.security.SignatureException;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.logging.Logger;
/*    */ import javax.crypto.Mac;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ 
/*    */ public class Utils
/*    */ {
/* 15 */   private static final Logger log = Logger.getLogger(Utils.class.getName());
/*    */   private boolean showDebugMessages;
/*    */ 
/*    */   public Utils(boolean showDebugMessages)
/*    */   {
/* 19 */     this.showDebugMessages = showDebugMessages;
/*    */   }
/*    */ 
/*    */   public Utils() {
/* 23 */     this.showDebugMessages = true;
/*    */   }
/*    */ 
/*    */   public String getTimeStamp()
/*    */   {
/* 28 */     return System.currentTimeMillis() / 1000L;
/*    */   }
/*    */ 
/*    */   public String getNonce() {
/* 32 */     Random random = new Random();
/* 33 */     return Long.toString(Math.abs(random.nextLong()), 60000);
/*    */   }
/*    */ 
/*    */   public String formatParameters(List<Parameter> httpParameters)
/*    */   {
/* 39 */     if (httpParameters.size() == 0) return "";
/*    */ 
/* 41 */     StringBuilder params = new StringBuilder();
/* 42 */     for (Parameter par : httpParameters) {
/* 43 */       String value = par.value;
/* 44 */       value = value.replaceAll("\\+", "%20");
/* 45 */       params.append(par.key + "=" + value + "&");
/*    */     }
/* 47 */     params.delete(params.length() - 1, params.length());
/* 48 */     return params.toString();
/*    */   }
/*    */ 
/*    */   public String calculateRFC2104HMAC(String data, String key)
/*    */     throws SignatureException
/*    */   {
/*    */     try
/*    */     {
/* 57 */       SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
/*    */ 
/* 60 */       Mac mac = Mac.getInstance("HmacSHA1");
/* 61 */       mac.init(signingKey);
/*    */ 
/* 64 */       byte[] rawHmac = mac.doFinal(data.getBytes());
/*    */ 
/* 67 */       result = Base64.encodeBytes(rawHmac);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/*    */       String result;
/* 70 */       throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
/*    */     }
/*    */     String result;
/* 72 */     return result;
/*    */   }
/*    */ 
/*    */   public Token parseResponse(String response) {
/* 76 */     String[] tokens = response.split("&");
/* 77 */     String token = tokens[0].substring(12);
/* 78 */     String secret = tokens[1].substring(19);
/* 79 */     return new Token(token, secret);
/*    */   }
/*    */ 
/*    */   public void print(String msg)
/*    */   {
/* 85 */     System.out.println(msg);
/*    */   }
/*    */ 
/*    */   public void debug(String msg)
/*    */   {
/* 91 */     if (this.showDebugMessages)
/* 92 */       log.info(msg);
/*    */   }
/*    */ 
/*    */   public void cleanConsole() {
/* 96 */     for (int i = 0; i < 100; i++)
/* 97 */       System.out.println("");
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.utils.Utils
 * JD-Core Version:    0.6.0
 */