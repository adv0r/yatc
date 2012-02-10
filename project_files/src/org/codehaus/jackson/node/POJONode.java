/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ 
/*     */ public final class POJONode extends ValueNode
/*     */ {
/*     */   protected final Object _value;
/*     */ 
/*     */   public POJONode(Object v)
/*     */   {
/*  18 */     this._value = v;
/*     */   }
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  26 */     return JsonToken.VALUE_EMBEDDED_OBJECT;
/*     */   }
/*     */   public boolean isPojo() {
/*  29 */     return true;
/*     */   }
/*     */ 
/*     */   public String getValueAsText()
/*     */   {
/*  39 */     return this._value == null ? "null" : this._value.toString();
/*     */   }
/*     */ 
/*     */   public boolean getValueAsBoolean(boolean defaultValue)
/*     */   {
/*  45 */     if ((this._value != null) && ((this._value instanceof Boolean))) {
/*  46 */       return ((Boolean)this._value).booleanValue();
/*     */     }
/*  48 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public int getValueAsInt(int defaultValue)
/*     */   {
/*  54 */     if ((this._value instanceof Number)) {
/*  55 */       return ((Number)this._value).intValue();
/*     */     }
/*  57 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public long getValueAsLong(long defaultValue)
/*     */   {
/*  63 */     if ((this._value instanceof Number)) {
/*  64 */       return ((Number)this._value).longValue();
/*     */     }
/*  66 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public double getValueAsDouble(double defaultValue)
/*     */   {
/*  72 */     if ((this._value instanceof Number)) {
/*  73 */       return ((Number)this._value).doubleValue();
/*     */     }
/*  75 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  88 */     if (this._value == null)
/*  89 */       jg.writeNull();
/*     */     else
/*  91 */       jg.writeObject(this._value);
/*     */   }
/*     */ 
/*     */   public Object getPojo()
/*     */   {
/* 104 */     return this._value;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 115 */     if (o == this) return true;
/* 116 */     if (o == null) return false;
/* 117 */     if (o.getClass() != getClass()) {
/* 118 */       return false;
/*     */     }
/* 120 */     POJONode other = (POJONode)o;
/* 121 */     if (this._value == null) {
/* 122 */       return other._value == null;
/*     */     }
/* 124 */     return this._value.equals(other._value);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 128 */     return this._value.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 133 */     return String.valueOf(this._value);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.POJONode
 * JD-Core Version:    0.6.0
 */