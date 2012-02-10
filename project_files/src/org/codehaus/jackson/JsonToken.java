/*     */ package org.codehaus.jackson;
/*     */ 
/*     */ public enum JsonToken
/*     */ {
/*  28 */   NOT_AVAILABLE(null), 
/*     */ 
/*  34 */   START_OBJECT("{"), 
/*     */ 
/*  40 */   END_OBJECT("}"), 
/*     */ 
/*  46 */   START_ARRAY("["), 
/*     */ 
/*  52 */   END_ARRAY("]"), 
/*     */ 
/*  58 */   FIELD_NAME(null), 
/*     */ 
/*  72 */   VALUE_EMBEDDED_OBJECT(null), 
/*     */ 
/*  79 */   VALUE_STRING(null), 
/*     */ 
/*  87 */   VALUE_NUMBER_INT(null), 
/*     */ 
/*  95 */   VALUE_NUMBER_FLOAT(null), 
/*     */ 
/* 101 */   VALUE_TRUE("true"), 
/*     */ 
/* 107 */   VALUE_FALSE("false"), 
/*     */ 
/* 113 */   VALUE_NULL("null");
/*     */ 
/*     */   final String _serialized;
/*     */   final char[] _serializedChars;
/*     */   final byte[] _serializedBytes;
/*     */ 
/*     */   private JsonToken(String token)
/*     */   {
/* 128 */     if (token == null) {
/* 129 */       this._serialized = null;
/* 130 */       this._serializedChars = null;
/* 131 */       this._serializedBytes = null;
/*     */     } else {
/* 133 */       this._serialized = token;
/* 134 */       this._serializedChars = token.toCharArray();
/*     */ 
/* 136 */       int len = this._serializedChars.length;
/* 137 */       this._serializedBytes = new byte[len];
/* 138 */       for (int i = 0; i < len; i++)
/* 139 */         this._serializedBytes[i] = (byte)this._serializedChars[i];
/*     */     }
/*     */   }
/*     */ 
/*     */   public String asString() {
/* 144 */     return this._serialized; } 
/* 145 */   public char[] asCharArray() { return this._serializedChars; } 
/* 146 */   public byte[] asByteArray() { return this._serializedBytes; }
/*     */ 
/*     */   public boolean isNumeric() {
/* 149 */     return (this == VALUE_NUMBER_INT) || (this == VALUE_NUMBER_FLOAT);
/*     */   }
/*     */ 
/*     */   public boolean isScalarValue()
/*     */   {
/* 159 */     return ordinal() >= VALUE_EMBEDDED_OBJECT.ordinal();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonToken
 * JD-Core Version:    0.6.0
 */