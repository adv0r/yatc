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
/*     */ public final class IntNode extends NumericNode
/*     */ {
/*     */   static final int MIN_CANONICAL = -1;
/*     */   static final int MAX_CANONICAL = 10;
/*     */   private static final IntNode[] CANONICALS;
/*     */   final int _value;
/*     */ 
/*     */   public IntNode(int v)
/*     */   {
/*  42 */     this._value = v;
/*     */   }
/*     */   public static IntNode valueOf(int i) {
/*  45 */     if ((i > 10) || (i < -1)) return new IntNode(i);
/*  46 */     return CANONICALS[(i - -1)];
/*     */   }
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  55 */     return JsonToken.VALUE_NUMBER_INT;
/*     */   }
/*     */   public JsonParser.NumberType getNumberType() {
/*  58 */     return JsonParser.NumberType.INT;
/*     */   }
/*     */ 
/*     */   public boolean isIntegralNumber()
/*     */   {
/*  67 */     return true;
/*     */   }
/*     */   public boolean isInt() {
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   public Number getNumberValue() {
/*  74 */     return Integer.valueOf(this._value);
/*     */   }
/*     */ 
/*     */   public int getIntValue() {
/*  78 */     return this._value;
/*     */   }
/*     */   public long getLongValue() {
/*  81 */     return this._value;
/*     */   }
/*     */   public double getDoubleValue() {
/*  84 */     return this._value;
/*     */   }
/*     */   public BigDecimal getDecimalValue() {
/*  87 */     return BigDecimal.valueOf(this._value);
/*     */   }
/*     */   public BigInteger getBigIntegerValue() {
/*  90 */     return BigInteger.valueOf(this._value);
/*     */   }
/*     */ 
/*     */   public String getValueAsText() {
/*  94 */     return NumberOutput.toString(this._value);
/*     */   }
/*     */ 
/*     */   public boolean getValueAsBoolean(boolean defaultValue)
/*     */   {
/*  99 */     return this._value != 0;
/*     */   }
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 106 */     jg.writeNumber(this._value);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 112 */     if (o == this) return true;
/* 113 */     if (o == null) return false;
/* 114 */     if (o.getClass() != getClass()) {
/* 115 */       return false;
/*     */     }
/* 117 */     return ((IntNode)o)._value == this._value;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 121 */     return this._value;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  24 */     int count = 12;
/*  25 */     CANONICALS = new IntNode[count];
/*  26 */     for (int i = 0; i < count; i++)
/*  27 */       CANONICALS[i] = new IntNode(-1 + i);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.IntNode
 * JD-Core Version:    0.6.0
 */