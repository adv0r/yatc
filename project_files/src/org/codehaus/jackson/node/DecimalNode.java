/*    */ package org.codehaus.jackson.node;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonParser.NumberType;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ 
/*    */ public final class DecimalNode extends NumericNode
/*    */ {
/*    */   protected final BigDecimal _value;
/*    */ 
/*    */   public DecimalNode(BigDecimal v)
/*    */   {
/* 25 */     this._value = v;
/*    */   }
/* 27 */   public static DecimalNode valueOf(BigDecimal d) { return new DecimalNode(d);
/*    */   }
/*    */ 
/*    */   public JsonToken asToken()
/*    */   {
/* 35 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*    */   }
/*    */   public JsonParser.NumberType getNumberType() {
/* 38 */     return JsonParser.NumberType.BIG_DECIMAL;
/*    */   }
/*    */ 
/*    */   public boolean isFloatingPointNumber()
/*    */   {
/* 47 */     return true;
/*    */   }
/*    */   public boolean isBigDecimal() {
/* 50 */     return true;
/*    */   }
/*    */   public Number getNumberValue() {
/* 53 */     return this._value;
/*    */   }
/*    */   public int getIntValue() {
/* 56 */     return this._value.intValue();
/*    */   }
/*    */   public long getLongValue() {
/* 59 */     return this._value.longValue();
/*    */   }
/*    */ 
/*    */   public BigInteger getBigIntegerValue() {
/* 63 */     return this._value.toBigInteger();
/*    */   }
/*    */   public double getDoubleValue() {
/* 66 */     return this._value.doubleValue();
/*    */   }
/*    */   public BigDecimal getDecimalValue() {
/* 69 */     return this._value;
/*    */   }
/*    */ 
/*    */   public String getValueAsText() {
/* 73 */     return this._value.toString();
/*    */   }
/*    */ 
/*    */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 80 */     jg.writeNumber(this._value);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 86 */     if (o == this) return true;
/* 87 */     if (o == null) return false;
/* 88 */     if (o.getClass() != getClass()) {
/* 89 */       return false;
/*    */     }
/* 91 */     return ((DecimalNode)o)._value.equals(this._value);
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 95 */     return this._value.hashCode();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.DecimalNode
 * JD-Core Version:    0.6.0
 */