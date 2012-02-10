/*    */ package org.codehaus.jackson.node;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ 
/*    */ public final class NullNode extends ValueNode
/*    */ {
/* 17 */   public static final NullNode instance = new NullNode();
/*    */ 
/*    */   public static NullNode getInstance()
/*    */   {
/* 21 */     return instance;
/*    */   }
/* 23 */   public JsonToken asToken() { return JsonToken.VALUE_NULL; }
/*    */ 
/*    */   public boolean isNull() {
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   public String getValueAsText() {
/* 30 */     return "null";
/*    */   }
/*    */ 
/*    */   public int getValueAsInt(int defaultValue)
/*    */   {
/* 35 */     return 0;
/*    */   }
/*    */ 
/*    */   public long getValueAsLong(long defaultValue) {
/* 39 */     return 0L;
/*    */   }
/*    */ 
/*    */   public double getValueAsDouble(double defaultValue) {
/* 43 */     return 0.0D;
/*    */   }
/*    */ 
/*    */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 50 */     jg.writeNull();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 56 */     return o == this;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.NullNode
 * JD-Core Version:    0.6.0
 */