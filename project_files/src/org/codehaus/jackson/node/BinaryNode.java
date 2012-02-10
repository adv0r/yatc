/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.Base64Variants;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ 
/*     */ public final class BinaryNode extends ValueNode
/*     */ {
/*  16 */   static final BinaryNode EMPTY_BINARY_NODE = new BinaryNode(new byte[0]);
/*     */   final byte[] _data;
/*     */ 
/*     */   public BinaryNode(byte[] data)
/*     */   {
/*  22 */     this._data = data;
/*     */   }
/*     */ 
/*     */   public BinaryNode(byte[] data, int offset, int length)
/*     */   {
/*  27 */     if ((offset == 0) && (length == data.length)) {
/*  28 */       this._data = data;
/*     */     } else {
/*  30 */       this._data = new byte[length];
/*  31 */       System.arraycopy(data, offset, this._data, 0, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static BinaryNode valueOf(byte[] data)
/*     */   {
/*  37 */     if (data == null) {
/*  38 */       return null;
/*     */     }
/*  40 */     if (data.length == 0) {
/*  41 */       return EMPTY_BINARY_NODE;
/*     */     }
/*  43 */     return new BinaryNode(data);
/*     */   }
/*     */ 
/*     */   public static BinaryNode valueOf(byte[] data, int offset, int length)
/*     */   {
/*  48 */     if (data == null) {
/*  49 */       return null;
/*     */     }
/*  51 */     if (length == 0) {
/*  52 */       return EMPTY_BINARY_NODE;
/*     */     }
/*  54 */     return new BinaryNode(data, offset, length);
/*     */   }
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  63 */     return JsonToken.VALUE_EMBEDDED_OBJECT;
/*     */   }
/*     */ 
/*     */   public boolean isBinary() {
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue()
/*     */   {
/*  75 */     return this._data;
/*     */   }
/*     */ 
/*     */   public String getValueAsText()
/*     */   {
/*  83 */     return Base64Variants.getDefaultVariant().encode(this._data, false);
/*     */   }
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  90 */     jg.writeBinary(this._data);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  96 */     if (o == this) return true;
/*  97 */     if (o == null) return false;
/*  98 */     if (o.getClass() != getClass()) {
/*  99 */       return false;
/*     */     }
/* 101 */     return Arrays.equals(((BinaryNode)o)._data, this._data);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 106 */     return this._data == null ? -1 : this._data.length;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 116 */     return Base64Variants.getDefaultVariant().encode(this._data, true);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.BinaryNode
 * JD-Core Version:    0.6.0
 */