/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.io.NumberOutput;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ 
/*     */ public final class DoubleNode extends NumericNode
/*     */ {
/*     */   protected final double _value;
/*     */ 
/*     */   public DoubleNode(double v)
/*     */   {
/*  26 */     this._value = v;
/*     */   }
/*  28 */   public static DoubleNode valueOf(double v) { return new DoubleNode(v);
/*     */   }
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  36 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*     */   }
/*     */   public JsonParser.NumberType getNumberType() {
/*  39 */     return JsonParser.NumberType.DOUBLE;
/*     */   }
/*     */ 
/*     */   public boolean isFloatingPointNumber()
/*     */   {
/*  48 */     return true;
/*     */   }
/*     */   public boolean isDouble() {
/*  51 */     return true;
/*     */   }
/*     */ 
/*     */   public Number getNumberValue() {
/*  55 */     return Double.valueOf(this._value);
/*     */   }
/*     */ 
/*     */   public int getIntValue() {
/*  59 */     return (int)this._value;
/*     */   }
/*     */   public long getLongValue() {
/*  62 */     return ()this._value;
/*     */   }
/*     */   public double getDoubleValue() {
/*  65 */     return this._value;
/*     */   }
/*     */   public BigDecimal getDecimalValue() {
/*  68 */     return BigDecimal.valueOf(this._value);
/*     */   }
/*     */ 
/*     */   public BigInteger getBigIntegerValue() {
/*  72 */     return getDecimalValue().toBigInteger();
/*     */   }
/*     */ 
/*     */   public String getValueAsText()
/*     */   {
/*  77 */     return NumberOutput.toString(this._value);
/*     */   }
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  84 */     jg.writeNumber(this._value);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  90 */     if (o == this) return true;
/*  91 */     if (o == null) return false;
/*  92 */     if (o.getClass() != getClass()) {
/*  93 */       return false;
/*     */     }
/*  95 */     return ((DoubleNode)o)._value == this._value;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 102 */     long l = Double.doubleToLongBits(this._value);
/* 103 */     return (int)l ^ (int)(l >> 32);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.DoubleNode
 * JD-Core Version:    0.6.0
 */