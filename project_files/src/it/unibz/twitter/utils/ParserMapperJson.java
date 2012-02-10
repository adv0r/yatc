/*    */ package it.unibz.twitter.utils;
/*    */ 
/*    */ import it.unibz.twitter.model.Tweet;
/*    */ import it.unibz.twitter.model.User;
/*    */ import java.io.IOException;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.JsonParseException;
/*    */ import org.codehaus.jackson.map.ObjectMapper;
/*    */ 
/*    */ public class ParserMapperJson
/*    */   implements Parser
/*    */ {
/*    */   private ObjectMapper mapper;
/*    */ 
/*    */   public ParserMapperJson()
/*    */   {
/* 19 */     this.mapper = new ObjectMapper();
/*    */   }
/*    */ 
/*    */   public List<Tweet> parseTimeLine(String jsonResponse)
/*    */     throws Exception
/*    */   {
/* 26 */     JsonNode node = (JsonNode)this.mapper.readValue(jsonResponse, JsonNode.class);
/* 27 */     List tweets = new LinkedList();
/* 28 */     JsonNode errorNode = node.path("error");
/* 29 */     String error = errorNode.getTextValue();
/* 30 */     if ((error != null) && (error.length() > 0)) {
/* 31 */       throw new Exception();
/*    */     }
/* 33 */     for (int i = 0; i < node.size(); i++) {
/* 34 */       JsonNode nodeTweet = node.path(i);
/* 35 */       Tweet tweet = new Tweet();
/* 36 */       User user = new User();
/* 37 */       tweet.setId(nodeTweet.path("id_str").getTextValue());
/* 38 */       tweet.setText(nodeTweet.path("text").getTextValue());
/*    */ 
/* 40 */       JsonNode nodeUser = nodeTweet.path("user");
/* 41 */       user.setId(nodeUser.path("id_str").getTextValue());
/* 42 */       user.setScreen_name(nodeUser.path("screen_name").getTextValue());
/* 43 */       user.setName(nodeUser.path("name").getTextValue());
/* 44 */       tweet.setUser(user);
/*    */ 
/* 46 */       tweets.add(tweet);
/*    */     }
/*    */ 
/* 49 */     return tweets;
/*    */   }
/*    */ 
/*    */   public List<Tweet> parseDirects(String jsonResponse) throws JsonParseException, IOException {
/* 53 */     JsonNode node = (JsonNode)this.mapper.readValue(jsonResponse, JsonNode.class);
/* 54 */     List tweets = new LinkedList();
/* 55 */     for (int i = 0; i < node.size(); i++) {
/* 56 */       JsonNode nodeTweet = node.path(i);
/* 57 */       Tweet tweet = new Tweet();
/* 58 */       User user = new User();
/* 59 */       tweet.setId(nodeTweet.path("id_str").getTextValue());
/* 60 */       tweet.setText(nodeTweet.path("text").getTextValue());
/*    */ 
/* 62 */       user.setId(nodeTweet.path("id_str").getTextValue());
/* 63 */       user.setScreen_name(nodeTweet.path("sender_screen_name").getTextValue());
/* 64 */       user.setName(nodeTweet.path("name").getTextValue());
/* 65 */       tweet.setUser(user);
/* 66 */       tweets.add(tweet);
/*    */     }
/*    */ 
/* 69 */     return tweets;
/*    */   }
/*    */ 
/*    */   public Tweet parseTweetResponse(String jsonResponse)
/*    */     throws JsonParseException, SecurityException, IOException, NoSuchMethodException
/*    */   {
/* 75 */     Tweet tweet = (Tweet)this.mapper.readValue(jsonResponse, Tweet.class);
/* 76 */     return tweet;
/*    */   }
/*    */ 
/*    */   public String parseUserInfo(String jsonResponse)
/*    */     throws Exception
/*    */   {
/* 82 */     JsonNode node = (JsonNode)this.mapper.readValue(jsonResponse, JsonNode.class);
/* 83 */     return node.findPath("screen_name").getTextValue();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.utils.ParserMapperJson
 * JD-Core Version:    0.6.0
 */