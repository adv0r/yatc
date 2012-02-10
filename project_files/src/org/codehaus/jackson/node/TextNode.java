/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.Base64Variants;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonLocation;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.io.NumberInput;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.util.ByteArrayBuilder;
/*     */ import org.codehaus.jackson.util.CharTypes;
/*     */ 
/*     */ public final class TextNode extends ValueNode
/*     */ {
/*     */   static final int INT_SPACE = 32;
/*  19 */   static final TextNode EMPTY_STRING_NODE = new TextNode("");
/*     */   final String _value;
/*     */ 
/*     */   public TextNode(String v)
/*     */   {
/*  23 */     this._value = v;
/*     */   }
/*     */ 
/*     */   public static TextNode valueOf(String v)
/*     */   {
/*  36 */     if (v == null) {
/*  37 */       return null;
/*     */     }
/*  39 */     if (v.length() == 0) {
/*  40 */       return EMPTY_STRING_NODE;
/*     */     }
/*  42 */     return new TextNode(v);
/*     */   }
/*     */   public JsonToken asToken() {
/*  45 */     return JsonToken.VALUE_STRING;
/*     */   }
/*     */ 
/*     */   public boolean isTextual()
/*     */   {
/*  51 */     return true;
/*     */   }
/*     */ 
/*     */   public String getTextValue() {
/*  55 */     return this._value;
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue(Base64Variant b64variant)
/*     */     throws IOException
/*     */   {
/*  66 */     ByteArrayBuilder builder = new ByteArrayBuilder(100);
/*  67 */     String str = this._value;
/*  68 */     int ptr = 0;
/*  69 */     int len = str.length();
/*     */ 
/*  72 */     while (ptr < len)
/*     */     {
/*     */       do
/*     */       {
/*  76 */         ch = str.charAt(ptr++);
/*  77 */         if (ptr >= len)
/*     */           break;
/*     */       }
/*  80 */       while (ch <= ' ');
/*  81 */       int bits = b64variant.decodeBase64Char(ch);
/*  82 */       if (bits < 0) {
/*  83 */         _reportInvalidBase64(b64variant, ch, 0);
/*     */       }
/*  85 */       int decodedData = bits;
/*     */ 
/*  87 */       if (ptr >= len) {
/*  88 */         _reportBase64EOF();
/*     */       }
/*  90 */       char ch = str.charAt(ptr++);
/*  91 */       bits = b64variant.decodeBase64Char(ch);
/*  92 */       if (bits < 0) {
/*  93 */         _reportInvalidBase64(b64variant, ch, 1);
/*     */       }
/*  95 */       decodedData = decodedData << 6 | bits;
/*     */ 
/*  97 */       if (ptr >= len) {
/*  98 */         _reportBase64EOF();
/*     */       }
/* 100 */       ch = str.charAt(ptr++);
/* 101 */       bits = b64variant.decodeBase64Char(ch);
/*     */ 
/* 104 */       if (bits < 0) {
/* 105 */         if (bits != -2) {
/* 106 */           _reportInvalidBase64(b64variant, ch, 2);
/*     */         }
/*     */ 
/* 109 */         if (ptr >= len) {
/* 110 */           _reportBase64EOF();
/*     */         }
/* 112 */         ch = str.charAt(ptr++);
/* 113 */         if (!b64variant.usesPaddingChar(ch)) {
/* 114 */           _reportInvalidBase64(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*     */         }
/*     */ 
/* 117 */         decodedData >>= 4;
/* 118 */         builder.append(decodedData);
/* 119 */         continue;
/*     */       }
/*     */ 
/* 122 */       decodedData = decodedData << 6 | bits;
/*     */ 
/* 124 */       if (ptr >= len) {
/* 125 */         _reportBase64EOF();
/*     */       }
/* 127 */       ch = str.charAt(ptr++);
/* 128 */       bits = b64variant.decodeBase64Char(ch);
/* 129 */       if (bits < 0) {
/* 130 */         if (bits != -2) {
/* 131 */           _reportInvalidBase64(b64variant, ch, 3);
/*     */         }
/* 133 */         decodedData >>= 2;
/* 134 */         builder.appendTwoBytes(decodedData);
/*     */       }
/*     */       else {
/* 137 */         decodedData = decodedData << 6 | bits;
/* 138 */         builder.appendThreeBytes(decodedData);
/*     */       }
/*     */     }
/* 141 */     return builder.toByteArray();
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue()
/*     */     throws IOException
/*     */   {
/* 147 */     return getBinaryValue(Base64Variants.getDefaultVariant());
/*     */   }
/*     */ 
/*     */   public String getValueAsText()
/*     */   {
/* 158 */     return this._value;
/*     */   }
/*     */ 
/*     */   public boolean getValueAsBoolean(boolean defaultValue)
/*     */   {
/* 165 */     if ((this._value != null) && 
/* 166 */       ("true".equals(this._value.trim()))) {
/* 167 */       return true;
/*     */     }
/*     */ 
/* 170 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public int getValueAsInt(int defaultValue)
/*     */   {
/* 175 */     return NumberInput.parseAsInt(this._value, defaultValue);
/*     */   }
/*     */ 
/*     */   public long getValueAsLong(long defaultValue)
/*     */   {
/* 180 */     return NumberInput.parseAsLong(this._value, defaultValue);
/*     */   }
/*     */ 
/*     */   public double getValueAsDouble(double defaultValue)
/*     */   {
/* 185 */     return NumberInput.parseAsDouble(this._value, defaultValue);
/*     */   }
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 198 */     if (this._value == null)
/* 199 */       jg.writeNull();
/*     */     else
/* 201 */       jg.writeString(this._value);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 214 */     if (o == this) return true;
/* 215 */     if (o == null) return false;
/* 216 */     if (o.getClass() != getClass()) {
/* 217 */       return false;
/*     */     }
/* 219 */     return ((TextNode)o)._value.equals(this._value);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 223 */     return this._value.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 231 */     int len = this._value.length();
/* 232 */     len = len + 2 + (len >> 4);
/* 233 */     StringBuilder sb = new StringBuilder(len);
/* 234 */     appendQuoted(sb, this._value);
/* 235 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   protected static void appendQuoted(StringBuilder sb, String content)
/*     */   {
/* 240 */     sb.append('"');
/* 241 */     CharTypes.appendQuoted(sb, content);
/* 242 */     sb.append('"');
/*     */   }
/*     */ 
/*     */   protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex)
/*     */     throws JsonParseException
/*     */   {
/* 254 */     _reportInvalidBase64(b64variant, ch, bindex, null);
/*     */   }
/*     */ 
/*     */   protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex, String msg)
/*     */     throws JsonParseException
/*     */   {
/*     */     String base;
/*     */     String base;
/* 265 */     if (ch <= ' ') {
/* 266 */       base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
/*     */     }
/*     */     else
/*     */     {
/*     */       String base;
/* 267 */       if (b64variant.usesPaddingChar(ch)) {
/* 268 */         base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
/*     */       }
/*     */       else
/*     */       {
/*     */         String base;
/* 269 */         if ((!Character.isDefined(ch)) || (Character.isISOControl(ch)))
/*     */         {
/* 271 */           base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*     */         }
/* 273 */         else base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content"; 
/*     */       }
/*     */     }
/* 275 */     if (msg != null) {
/* 276 */       base = base + ": " + msg;
/*     */     }
/* 278 */     throw new JsonParseException(base, JsonLocation.NA);
/*     */   }
/*     */ 
/*     */   protected void _reportBase64EOF()
/*     */     throws JsonParseException
/*     */   {
/* 284 */     throw new JsonParseException("Unexpected end-of-String when base64 content", JsonLocation.NA);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.TextNode
 * JD-Core Version:    0.6.0
 */