/*     */ package it.unibz.twitter.net;
/*     */ 
/*     */ import it.unibz.twitter.model.Parameter;
/*     */ import it.unibz.twitter.model.Token;
/*     */ import it.unibz.twitter.model.Tweet;
/*     */ import it.unibz.twitter.oauth.OAuth;
/*     */ import it.unibz.twitter.utils.Parser;
/*     */ import it.unibz.twitter.utils.ParserMapperJson;
/*     */ import it.unibz.twitter.utils.Utils;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URLEncoder;
/*     */ import java.security.SignatureException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ 
/*     */ public class TweetService
/*     */ {
/*     */   private Memory memory;
/*     */   private OAuth oa;
/*     */   private Parser parser;
/*     */   private Utils utils;
/*     */   public static final String TIMELINE = "http://api.twitter.com/1/statuses/home_timeline.json";
/*     */   public static final String UPDATE = "http://api.twitter.com/1/statuses/update.json";
/*     */   public static final String USER_INFO = "https://api.twitter.com/1/account/verify_credentials.json";
/*     */ 
/*     */   public TweetService()
/*     */   {
/*  38 */     this.memory = new Memory();
/*  39 */     this.oa = new OAuth();
/*  40 */     this.utils = new Utils();
/*  41 */     setParser(new ParserMapperJson());
/*     */   }
/*     */ 
/*     */   public TweetService(Memory memory, OAuth oa) {
/*  45 */     this.memory = memory;
/*  46 */     this.oa = oa;
/*     */   }
/*     */ 
/*     */   public List<Tweet> getTimeline()
/*     */   {
/*  51 */     return getTimeline(20);
/*     */   }
/*     */ 
/*     */   public String getUserScreenName()
/*     */   {
/*  57 */     String toReturn = "";
/*  58 */     List params = new LinkedList();
/*     */     try {
/*  60 */       String response = this.oa.doPetition("https://api.twitter.com/1/account/verify_credentials.json", params, this.memory.getUserToken(), false);
/*  61 */       toReturn = this.parser.parseUserInfo(response);
/*     */     }
/*     */     catch (Exception e) {
/*  64 */       this.utils.debug(e.getStackTrace());
/*     */     }
/*  66 */     return toReturn;
/*     */   }
/*     */ 
/*     */   public void unfollow(String screen_name)
/*     */   {
/*  73 */     tweet("unfollow " + screen_name);
/*     */   }
/*     */ 
/*     */   public void follow(String screen_name)
/*     */   {
/*  82 */     tweet("follow " + screen_name);
/*     */   }
/*     */ 
/*     */   public List<Tweet> getTimeline(int how_many)
/*     */   {
/*  89 */     if (how_many > 800) {
/*  90 */       throw new IllegalArgumentException("No More than 800 tweets at the time,plee");
/*     */     }
/*  92 */     List params2 = new LinkedList();
/*  93 */     params2.add(new Parameter("count", how_many));
/*  94 */     return getTweetList("http://api.twitter.com/1/statuses/home_timeline.json", params2);
/*     */   }
/*     */ 
/*     */   public void tweet(String text)
/*     */   {
/* 103 */     if (text.length() > 140)
/* 104 */       throw new IllegalArgumentException("Max tweet length = 140");
/*     */     try
/*     */     {
/* 107 */       List params = new LinkedList();
/* 108 */       text = URLEncoder.encode(text, "UTF-8").replaceAll("\\+", "%20");
/* 109 */       params.add(new Parameter("status", text));
/* 110 */       this.oa.doPetition("http://api.twitter.com/1/statuses/update.json", params, this.memory.getUserToken(), true);
/*     */     } catch (Exception e) {
/* 112 */       e.printStackTrace();
/* 113 */       throw new RuntimeException("Not able to retreive the timeline", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<Tweet> getTweetsSinceX(String since)
/*     */   {
/* 121 */     List params2 = new LinkedList();
/* 122 */     params2.add(new Parameter("since_id", since));
/*     */ 
/* 124 */     if (!since.matches("[0-9]+")) {
/* 125 */       throw new IllegalArgumentException("It should be int");
/*     */     }
/*     */ 
/* 128 */     return getTweetList("http://api.twitter.com/1/statuses/home_timeline.json", params2);
/*     */   }
/*     */ 
/*     */   private List<Tweet> getTweetList(String url, List<Parameter> params2)
/*     */   {
/*     */     try
/*     */     {
/* 135 */       String response = this.oa.doPetition(url, params2, this.memory.getUserToken(), false);
/* 136 */       List lista = this.parser.parseTimeLine(response);
/* 137 */       return lista;
/*     */     } catch (JsonParseException e) {
/* 139 */       e.printStackTrace();
/* 140 */       throw new RuntimeException("i cant parse twitter response", e);
/*     */     }
/*     */     catch (IOException e) {
/* 143 */       e.printStackTrace();
/* 144 */       throw new RuntimeException("i cant access twitter ", e);
/*     */     } catch (SignatureException e) {
/* 146 */       e.printStackTrace();
/* 147 */       throw new RuntimeException("i cant sign twitter request", e);
/*     */     }
/*     */     catch (Exception e) {
/* 150 */       e.printStackTrace();
/* 151 */     }throw new RuntimeException("Exception while getting the list", e);
/*     */   }
/*     */ 
/*     */   public Parser getParser()
/*     */   {
/* 156 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/* 160 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public Token getRequestToken() throws SignatureException, IOException {
/* 164 */     return this.oa.getRequestToken();
/*     */   }
/*     */ 
/*     */   public String getAuthorizationURL(Token token) {
/* 168 */     String url = "https://api.twitter.com/oauth/authorize";
/* 169 */     return "https://api.twitter.com/oauth/authorize?oauth_token=" + token.getToken();
/*     */   }
/*     */ 
/*     */   public Token getAccessToken(Token token, String pin) throws UnsupportedEncodingException, SignatureException, MalformedURLException, ProtocolException, IOException
/*     */   {
/* 174 */     return this.oa.getAccessToken(token, pin);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.net.TweetService
 * JD-Core Version:    0.6.0
 */