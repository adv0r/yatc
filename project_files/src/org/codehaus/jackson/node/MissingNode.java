/*    */ package org.codehaus.jackson.node;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ 
/*    */ public final class MissingNode extends BaseJsonNode
/*    */ {
/* 23 */   private static final MissingNode instance = new MissingNode();
/*    */ 
/*    */   public static MissingNode getInstance()
/*    */   {
/* 27 */     return instance;
/*    */   }
/* 29 */   public JsonToken asToken() { return JsonToken.NOT_AVAILABLE; }
/*    */ 
/*    */   public boolean isMissingNode() {
/* 32 */     return true;
/*    */   }
/*    */   public String getValueAsText() {
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */   public int getValueAsInt(int defaultValue) {
/* 39 */     return 0;
/*    */   }
/*    */ 
/*    */   public long getValueAsLong(long defaultValue) {
/* 43 */     return 0L;
/*    */   }
/*    */ 
/*    */   public double getValueAsDouble(double defaultValue) {
/* 47 */     return 0.0D;
/*    */   }
/*    */ 
/*    */   public JsonNode path(String fieldName) {
/* 51 */     return this;
/*    */   }
/*    */   public JsonNode path(int index) {
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 77 */     return o == this;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 84 */     return "";
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.MissingNode
 * JD-Core Version:    0.6.0
 */