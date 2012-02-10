/*    */ package org.codehaus.jackson.node;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonParser.NumberType;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ import org.codehaus.jackson.io.NumberOutput;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ 
/*    */ public final class LongNode extends NumericNode
/*    */ {
/*    */   final long _value;
/*    */ 
/*    */   public LongNode(long v)
/*    */   {
/* 25 */     this._value = v;
/*    */   }
/* 27 */   public static LongNode valueOf(long l) { return new LongNode(l);
/*    */   }
/*    */ 
/*    */   public JsonToken asToken()
/*    */   {
/* 35 */     return JsonToken.VALUE_NUMBER_INT;
/*    */   }
/*    */   public JsonParser.NumberType getNumberType() {
/* 38 */     return JsonParser.NumberType.LONG;
/*    */   }
/*    */ 
/*    */   public boolean isIntegralNumber() {
/* 42 */     return true;
/*    */   }
/*    */   public boolean isLong() {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   public Number getNumberValue() {
/* 49 */     return Long.valueOf(this._value);
/*    */   }
/*    */ 
/*    */   public int getIntValue() {
/* 53 */     return (int)this._value;
/*    */   }
/*    */   public long getLongValue() {
/* 56 */     return this._value;
/*    */   }
/*    */   public double getDoubleValue() {
/* 59 */     return this._value;
/*    */   }
/*    */   public BigDecimal getDecimalValue() {
/* 62 */     return BigDecimal.valueOf(this._value);
/*    */   }
/*    */   public BigInteger getBigIntegerValue() {
/* 65 */     return BigInteger.valueOf(this._value);
/*    */   }
/*    */ 
/*    */   public String getValueAsText() {
/* 69 */     return NumberOutput.toString(this._value);
/*    */   }
/*    */ 
/*    */   public boolean getValueAsBoolean(boolean defaultValue)
/*    */   {
/* 74 */     return this._value != 0L;
/*    */   }
/*    */ 
/*    */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 81 */     jg.writeNumber(this._value);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 87 */     if (o == this) return true;
/* 88 */     if (o == null) return false;
/* 89 */     if (o.getClass() != getClass()) {
/* 90 */       return false;
/*    */     }
/* 92 */     return ((LongNode)o)._value == this._value;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 97 */     return (int)this._value ^ (int)(this._value >> 32);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.LongNode
 * JD-Core Version:    0.6.0
 */