/*     */ package org.codehaus.jackson.io;
/*     */ 
/*     */ import org.codehaus.jackson.SerializableString;
/*     */ 
/*     */ public class SerializedString
/*     */   implements SerializableString
/*     */ {
/*     */   protected final String _value;
/*     */   protected byte[] _quotedUTF8Ref;
/*     */   protected byte[] _unquotedUTF8Ref;
/*     */   protected char[] _quotedChars;
/*     */ 
/*     */   public SerializedString(String v)
/*     */   {
/*  36 */     this._value = v;
/*     */   }
/*     */ 
/*     */   public final String getValue()
/*     */   {
/*  44 */     return this._value;
/*     */   }
/*     */ 
/*     */   public final int charLength()
/*     */   {
/*  49 */     return this._value.length();
/*     */   }
/*     */ 
/*     */   public final char[] asQuotedChars() {
/*  53 */     char[] result = this._quotedChars;
/*  54 */     if (result == null) {
/*  55 */       result = JsonStringEncoder.getInstance().quoteAsString(this._value);
/*  56 */       this._quotedChars = result;
/*     */     }
/*  58 */     return result;
/*     */   }
/*     */ 
/*     */   public final byte[] asUnquotedUTF8()
/*     */   {
/*  67 */     byte[] result = this._unquotedUTF8Ref;
/*  68 */     if (result == null) {
/*  69 */       result = JsonStringEncoder.getInstance().encodeAsUTF8(this._value);
/*  70 */       this._unquotedUTF8Ref = result;
/*     */     }
/*  72 */     return result;
/*     */   }
/*     */ 
/*     */   public final byte[] asQuotedUTF8()
/*     */   {
/*  81 */     byte[] result = this._quotedUTF8Ref;
/*  82 */     if (result == null) {
/*  83 */       result = JsonStringEncoder.getInstance().quoteAsUTF8(this._value);
/*  84 */       this._quotedUTF8Ref = result;
/*     */     }
/*  86 */     return result;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/*  96 */     return this._value;
/*     */   }
/*     */   public final int hashCode() {
/*  99 */     return this._value.hashCode();
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object o)
/*     */   {
/* 104 */     if (o == this) return true;
/* 105 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 106 */     SerializedString other = (SerializedString)o;
/* 107 */     return this._value.equals(other._value);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.io.SerializedString
 * JD-Core Version:    0.6.0
 */