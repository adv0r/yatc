/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ 
/*     */ public final class BigIntegerNode extends NumericNode
/*     */ {
/*     */   protected final BigInteger _value;
/*     */ 
/*     */   public BigIntegerNode(BigInteger v)
/*     */   {
/*  24 */     this._value = v;
/*     */   }
/*  26 */   public static BigIntegerNode valueOf(BigInteger v) { return new BigIntegerNode(v);
/*     */   }
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  35 */     return JsonToken.VALUE_NUMBER_INT;
/*     */   }
/*     */   public JsonParser.NumberType getNumberType() {
/*  38 */     return JsonParser.NumberType.BIG_INTEGER;
/*     */   }
/*     */   public boolean isIntegralNumber() {
/*  41 */     return true;
/*     */   }
/*     */   public boolean isBigInteger() {
/*  44 */     return true;
/*     */   }
/*     */ 
/*     */   public Number getNumberValue() {
/*  48 */     return this._value;
/*     */   }
/*     */ 
/*     */   public int getIntValue() {
/*  52 */     return this._value.intValue();
/*     */   }
/*     */   public long getLongValue() {
/*  55 */     return this._value.longValue();
/*     */   }
/*     */   public BigInteger getBigIntegerValue() {
/*  58 */     return this._value;
/*     */   }
/*     */   public double getDoubleValue() {
/*  61 */     return this._value.doubleValue();
/*     */   }
/*     */   public BigDecimal getDecimalValue() {
/*  64 */     return new BigDecimal(this._value);
/*     */   }
/*     */ 
/*     */   public String getValueAsText()
/*     */   {
/*  74 */     return this._value.toString();
/*     */   }
/*     */ 
/*     */   public boolean getValueAsBoolean(boolean defaultValue)
/*     */   {
/*  79 */     return !BigInteger.ZERO.equals(this._value);
/*     */   }
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  86 */     jg.writeNumber(this._value);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  92 */     if (o == this) return true;
/*  93 */     if (o == null) return false;
/*  94 */     if (o.getClass() != getClass()) {
/*  95 */       return false;
/*     */     }
/*  97 */     return ((BigIntegerNode)o)._value == this._value;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 102 */     return this._value.hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.BigIntegerNode
 * JD-Core Version:    0.6.0
 */