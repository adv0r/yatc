/*    */ package org.codehaus.jackson.node;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ 
/*    */ public final class BooleanNode extends ValueNode
/*    */ {
/* 18 */   public static final BooleanNode TRUE = new BooleanNode();
/* 19 */   public static final BooleanNode FALSE = new BooleanNode();
/*    */ 
/*    */   public static BooleanNode getTrue()
/*    */   {
/* 23 */     return TRUE; } 
/* 24 */   public static BooleanNode getFalse() { return FALSE; } 
/*    */   public static BooleanNode valueOf(boolean b) {
/* 26 */     return b ? TRUE : FALSE;
/*    */   }
/*    */ 
/*    */   public JsonToken asToken() {
/* 30 */     return this == TRUE ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
/*    */   }
/*    */ 
/*    */   public boolean isBoolean() {
/* 34 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean getBooleanValue() {
/* 38 */     return this == TRUE;
/*    */   }
/*    */ 
/*    */   public String getValueAsText()
/*    */   {
/* 43 */     return this == TRUE ? "true" : "false";
/*    */   }
/*    */ 
/*    */   public boolean getValueAsBoolean()
/*    */   {
/* 48 */     return this == TRUE;
/*    */   }
/*    */ 
/*    */   public boolean getValueAsBoolean(boolean defaultValue)
/*    */   {
/* 53 */     return this == TRUE;
/*    */   }
/*    */ 
/*    */   public int getValueAsInt(int defaultValue)
/*    */   {
/* 58 */     return this == TRUE ? 1 : 0;
/*    */   }
/*    */ 
/*    */   public long getValueAsLong(long defaultValue) {
/* 62 */     return this == TRUE ? 1L : 0L;
/*    */   }
/*    */ 
/*    */   public double getValueAsDouble(double defaultValue) {
/* 66 */     return this == TRUE ? 1.0D : 0.0D;
/*    */   }
/*    */ 
/*    */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 73 */     jg.writeBoolean(this == TRUE);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 82 */     return o == this;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.BooleanNode
 * JD-Core Version:    0.6.0
 */