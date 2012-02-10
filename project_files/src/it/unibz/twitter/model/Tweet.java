/*     */ package it.unibz.twitter.model;
/*     */ 
/*     */ public class Tweet
/*     */ {
/*     */   private User user;
/*     */   private Tweet retweeted_status;
/*     */   private String coordinates;
/*     */   private String in_reply_to_user_id;
/*     */   private String source;
/*     */   private String in_reply_to_screen_name;
/*     */   private String created_at;
/*     */   private String contributors;
/*     */   private String favorited;
/*     */   private String truncated;
/*     */   private String id;
/*     */   private String in_reply_to_status_id;
/*     */   private String text;
/*     */   private String retweeted;
/*     */   private String in_reply_to_status_id_str;
/*     */   private String in_reply_to_user_id_str;
/*     */ 
/*     */   public User getUser()
/*     */   {
/*  25 */     return this.user;
/*     */   }
/*     */   public void setUser(User user) {
/*  28 */     this.user = user;
/*     */   }
/*     */ 
/*     */   public Tweet getRetweeted_status() {
/*  32 */     return this.retweeted_status;
/*     */   }
/*     */   public void setRetweeted_status(Tweet retweetedStatus) {
/*  35 */     this.retweeted_status = retweetedStatus;
/*     */   }
/*     */   public String getCoordinates() {
/*  38 */     return this.coordinates;
/*     */   }
/*     */   public void setCoordinates(String coordinates) {
/*  41 */     this.coordinates = coordinates;
/*     */   }
/*     */ 
/*     */   public String getIn_reply_to_user_id() {
/*  45 */     return this.in_reply_to_user_id;
/*     */   }
/*     */   public void setIn_reply_to_user_id(String inReplyToUserId) {
/*  48 */     this.in_reply_to_user_id = inReplyToUserId;
/*     */   }
/*     */   public String getSource() {
/*  51 */     return this.source;
/*     */   }
/*     */   public void setSource(String source) {
/*  54 */     this.source = source;
/*     */   }
/*     */   public String getIn_reply_to_screen_name() {
/*  57 */     return this.in_reply_to_screen_name;
/*     */   }
/*     */   public void setIn_reply_to_screen_name(String inReplyToScreenName) {
/*  60 */     this.in_reply_to_screen_name = inReplyToScreenName;
/*     */   }
/*     */   public String getCreated_at() {
/*  63 */     return this.created_at;
/*     */   }
/*     */   public void setCreated_at(String createdAt) {
/*  66 */     this.created_at = createdAt;
/*     */   }
/*     */   public String getContributors() {
/*  69 */     return this.contributors;
/*     */   }
/*     */   public void setContributors(String contributors) {
/*  72 */     this.contributors = contributors;
/*     */   }
/*     */   public String getFavorited() {
/*  75 */     return this.favorited;
/*     */   }
/*     */   public void setFavorited(String favorited) {
/*  78 */     this.favorited = favorited;
/*     */   }
/*     */   public String getTruncated() {
/*  81 */     return this.truncated;
/*     */   }
/*     */   public void setTruncated(String truncated) {
/*  84 */     this.truncated = truncated;
/*     */   }
/*     */   public String getId() {
/*  87 */     return this.id;
/*     */   }
/*     */   public void setId(String id) {
/*  90 */     this.id = id;
/*     */   }
/*     */ 
/*     */   public String getIn_reply_to_status_id() {
/*  94 */     return this.in_reply_to_status_id;
/*     */   }
/*     */   public void setIn_reply_to_status_id(String inReplyToStatusId) {
/*  97 */     this.in_reply_to_status_id = inReplyToStatusId;
/*     */   }
/*     */   public String getText() {
/* 100 */     return this.text;
/*     */   }
/*     */   public void setText(String text) {
/* 103 */     this.text = text;
/*     */   }
/*     */   public String getRetweeted() {
/* 106 */     return this.retweeted;
/*     */   }
/*     */   public void setRetweeted(String retweeted) {
/* 109 */     this.retweeted = retweeted;
/*     */   }
/*     */   public String getIn_reply_to_status_id_str() {
/* 112 */     return this.in_reply_to_status_id_str;
/*     */   }
/*     */   public void setIn_reply_to_status_id_str(String inReplyToStatusIdStr) {
/* 115 */     this.in_reply_to_status_id_str = inReplyToStatusIdStr;
/*     */   }
/*     */   public String getIn_reply_to_user_id_str() {
/* 118 */     return this.in_reply_to_user_id_str;
/*     */   }
/*     */   public void setIn_reply_to_user_id_str(String inReplyToUserIdStr) {
/* 121 */     this.in_reply_to_user_id_str = inReplyToUserIdStr;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.model.Tweet
 * JD-Core Version:    0.6.0
 */