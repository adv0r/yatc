/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.Base64Variants;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders.BooleanBuilder;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders.ByteBuilder;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders.DoubleBuilder;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders.FloatBuilder;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders.IntBuilder;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders.LongBuilder;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders.ShortBuilder;
/*     */ import org.codehaus.jackson.map.util.ObjectBuffer;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class ArrayDeserializers
/*     */ {
/*     */   HashMap<JavaType, JsonDeserializer<Object>> _allDeserializers;
/*  22 */   static final ArrayDeserializers instance = new ArrayDeserializers();
/*     */ 
/*     */   private ArrayDeserializers()
/*     */   {
/*  26 */     this._allDeserializers = new HashMap();
/*     */ 
/*  28 */     add(Boolean.TYPE, new BooleanDeser());
/*     */ 
/*  34 */     add(Byte.TYPE, new ByteDeser());
/*  35 */     add(Short.TYPE, new ShortDeser());
/*  36 */     add(Integer.TYPE, new IntDeser());
/*  37 */     add(Long.TYPE, new LongDeser());
/*     */ 
/*  39 */     add(Float.TYPE, new FloatDeser());
/*  40 */     add(Double.TYPE, new DoubleDeser());
/*     */ 
/*  42 */     add(String.class, new StringDeser());
/*     */ 
/*  46 */     add(Character.TYPE, new CharDeser());
/*     */   }
/*     */ 
/*     */   public static HashMap<JavaType, JsonDeserializer<Object>> getAll()
/*     */   {
/*  51 */     return instance._allDeserializers;
/*     */   }
/*     */ 
/*     */   private void add(Class<?> cls, JsonDeserializer<?> deser)
/*     */   {
/*  57 */     this._allDeserializers.put(TypeFactory.type(cls), deser);
/*     */   }
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  68 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class DoubleDeser extends ArrayDeserializers.ArrayDeser<double[]>
/*     */   {
/*     */     public DoubleDeser()
/*     */     {
/* 409 */       super();
/*     */     }
/*     */ 
/*     */     public double[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 415 */       if (!jp.isExpectedStartArrayToken()) {
/* 416 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 418 */       ArrayBuilders.DoubleBuilder builder = ctxt.getArrayBuilders().getDoubleBuilder();
/* 419 */       double[] chunk = (double[])builder.resetAndStart();
/* 420 */       int ix = 0;
/*     */ 
/* 422 */       while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 423 */         double value = _parseDoublePrimitive(jp, ctxt);
/* 424 */         if (ix >= chunk.length) {
/* 425 */           chunk = (double[])builder.appendCompletedChunk(chunk, ix);
/* 426 */           ix = 0;
/*     */         }
/* 428 */         chunk[(ix++)] = value;
/*     */       }
/* 430 */       return (double[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class FloatDeser extends ArrayDeserializers.ArrayDeser<float[]>
/*     */   {
/*     */     public FloatDeser()
/*     */     {
/* 379 */       super();
/*     */     }
/*     */ 
/*     */     public float[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 385 */       if (!jp.isExpectedStartArrayToken()) {
/* 386 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 388 */       ArrayBuilders.FloatBuilder builder = ctxt.getArrayBuilders().getFloatBuilder();
/* 389 */       float[] chunk = (float[])builder.resetAndStart();
/* 390 */       int ix = 0;
/*     */ 
/* 392 */       while (jp.nextToken() != JsonToken.END_ARRAY)
/*     */       {
/* 394 */         float value = _parseFloatPrimitive(jp, ctxt);
/* 395 */         if (ix >= chunk.length) {
/* 396 */           chunk = (float[])builder.appendCompletedChunk(chunk, ix);
/* 397 */           ix = 0;
/*     */         }
/* 399 */         chunk[(ix++)] = value;
/*     */       }
/* 401 */       return (float[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class LongDeser extends ArrayDeserializers.ArrayDeser<long[]>
/*     */   {
/*     */     public LongDeser()
/*     */     {
/* 350 */       super();
/*     */     }
/*     */ 
/*     */     public long[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 356 */       if (!jp.isExpectedStartArrayToken()) {
/* 357 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 359 */       ArrayBuilders.LongBuilder builder = ctxt.getArrayBuilders().getLongBuilder();
/* 360 */       long[] chunk = (long[])builder.resetAndStart();
/* 361 */       int ix = 0;
/*     */ 
/* 363 */       while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 364 */         long value = _parseLongPrimitive(jp, ctxt);
/* 365 */         if (ix >= chunk.length) {
/* 366 */           chunk = (long[])builder.appendCompletedChunk(chunk, ix);
/* 367 */           ix = 0;
/*     */         }
/* 369 */         chunk[(ix++)] = value;
/*     */       }
/* 371 */       return (long[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class IntDeser extends ArrayDeserializers.ArrayDeser<int[]>
/*     */   {
/*     */     public IntDeser()
/*     */     {
/* 320 */       super();
/*     */     }
/*     */ 
/*     */     public int[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 326 */       if (!jp.isExpectedStartArrayToken()) {
/* 327 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 329 */       ArrayBuilders.IntBuilder builder = ctxt.getArrayBuilders().getIntBuilder();
/* 330 */       int[] chunk = (int[])builder.resetAndStart();
/* 331 */       int ix = 0;
/*     */ 
/* 333 */       while (jp.nextToken() != JsonToken.END_ARRAY)
/*     */       {
/* 335 */         int value = _parseIntPrimitive(jp, ctxt);
/* 336 */         if (ix >= chunk.length) {
/* 337 */           chunk = (int[])builder.appendCompletedChunk(chunk, ix);
/* 338 */           ix = 0;
/*     */         }
/* 340 */         chunk[(ix++)] = value;
/*     */       }
/* 342 */       return (int[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class ShortDeser extends ArrayDeserializers.ArrayDeser<short[]>
/*     */   {
/*     */     public ShortDeser()
/*     */     {
/* 291 */       super();
/*     */     }
/*     */ 
/*     */     public short[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 297 */       if (!jp.isExpectedStartArrayToken()) {
/* 298 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 300 */       ArrayBuilders.ShortBuilder builder = ctxt.getArrayBuilders().getShortBuilder();
/* 301 */       short[] chunk = (short[])builder.resetAndStart();
/* 302 */       int ix = 0;
/*     */ 
/* 304 */       while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 305 */         short value = _parseShortPrimitive(jp, ctxt);
/* 306 */         if (ix >= chunk.length) {
/* 307 */           chunk = (short[])builder.appendCompletedChunk(chunk, ix);
/* 308 */           ix = 0;
/*     */         }
/* 310 */         chunk[(ix++)] = value;
/*     */       }
/* 312 */       return (short[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class ByteDeser extends ArrayDeserializers.ArrayDeser<byte[]>
/*     */   {
/*     */     public ByteDeser()
/*     */     {
/* 237 */       super();
/*     */     }
/*     */ 
/*     */     public byte[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 243 */       JsonToken t = jp.getCurrentToken();
/*     */ 
/* 246 */       if (t == JsonToken.VALUE_STRING) {
/* 247 */         return jp.getBinaryValue(ctxt.getBase64Variant());
/*     */       }
/*     */ 
/* 250 */       if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 251 */         Object ob = jp.getEmbeddedObject();
/* 252 */         if (ob == null) return null;
/* 253 */         if ((ob instanceof byte[])) {
/* 254 */           return (byte[])(byte[])ob;
/*     */         }
/*     */       }
/* 257 */       if (!jp.isExpectedStartArrayToken()) {
/* 258 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 260 */       ArrayBuilders.ByteBuilder builder = ctxt.getArrayBuilders().getByteBuilder();
/* 261 */       byte[] chunk = (byte[])builder.resetAndStart();
/* 262 */       int ix = 0;
/*     */ 
/* 264 */       while ((t = jp.nextToken()) != JsonToken.END_ARRAY)
/*     */       {
/*     */         byte value;
/*     */         byte value;
/* 267 */         if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT))
/*     */         {
/* 269 */           value = jp.getByteValue();
/*     */         }
/*     */         else {
/* 272 */           if (t != JsonToken.VALUE_NULL) {
/* 273 */             throw ctxt.mappingException(this._valueClass.getComponentType());
/*     */           }
/* 275 */           value = 0;
/*     */         }
/* 277 */         if (ix >= chunk.length) {
/* 278 */           chunk = (byte[])builder.appendCompletedChunk(chunk, ix);
/* 279 */           ix = 0;
/*     */         }
/* 281 */         chunk[(ix++)] = value;
/*     */       }
/* 283 */       return (byte[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class BooleanDeser extends ArrayDeserializers.ArrayDeser<boolean[]>
/*     */   {
/*     */     public BooleanDeser()
/*     */     {
/* 203 */       super();
/*     */     }
/*     */ 
/*     */     public boolean[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 209 */       if (!jp.isExpectedStartArrayToken()) {
/* 210 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 212 */       ArrayBuilders.BooleanBuilder builder = ctxt.getArrayBuilders().getBooleanBuilder();
/* 213 */       boolean[] chunk = (boolean[])builder.resetAndStart();
/* 214 */       int ix = 0;
/*     */ 
/* 216 */       while (jp.nextToken() != JsonToken.END_ARRAY)
/*     */       {
/* 218 */         boolean value = _parseBooleanPrimitive(jp, ctxt);
/* 219 */         if (ix >= chunk.length) {
/* 220 */           chunk = (boolean[])builder.appendCompletedChunk(chunk, ix);
/* 221 */           ix = 0;
/*     */         }
/* 223 */         chunk[(ix++)] = value;
/*     */       }
/* 225 */       return (boolean[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class CharDeser extends ArrayDeserializers.ArrayDeser<char[]>
/*     */   {
/*     */     public CharDeser()
/*     */     {
/* 137 */       super();
/*     */     }
/*     */ 
/*     */     public char[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 147 */       JsonToken t = jp.getCurrentToken();
/* 148 */       if (t == JsonToken.VALUE_STRING)
/*     */       {
/* 150 */         char[] buffer = jp.getTextCharacters();
/* 151 */         int offset = jp.getTextOffset();
/* 152 */         int len = jp.getTextLength();
/*     */ 
/* 154 */         char[] result = new char[len];
/* 155 */         System.arraycopy(buffer, offset, result, 0, len);
/* 156 */         return result;
/*     */       }
/* 158 */       if (jp.isExpectedStartArrayToken())
/*     */       {
/* 160 */         StringBuilder sb = new StringBuilder(64);
/* 161 */         while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
/* 162 */           if (t != JsonToken.VALUE_STRING) {
/* 163 */             throw ctxt.mappingException(Character.TYPE);
/*     */           }
/* 165 */           String str = jp.getText();
/* 166 */           if (str.length() != 1) {
/* 167 */             throw JsonMappingException.from(jp, "Can not convert a JSON String of length " + str.length() + " into a char element of char array");
/*     */           }
/* 169 */           sb.append(str.charAt(0));
/*     */         }
/* 171 */         return sb.toString().toCharArray();
/*     */       }
/*     */ 
/* 174 */       if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 175 */         Object ob = jp.getEmbeddedObject();
/* 176 */         if (ob == null) return null;
/* 177 */         if ((ob instanceof char[])) {
/* 178 */           return (char[])(char[])ob;
/*     */         }
/* 180 */         if ((ob instanceof String)) {
/* 181 */           return ((String)ob).toCharArray();
/*     */         }
/*     */ 
/* 184 */         if ((ob instanceof byte[])) {
/* 185 */           return Base64Variants.getDefaultVariant().encode((byte[])(byte[])ob, false).toCharArray();
/*     */         }
/*     */       }
/*     */ 
/* 189 */       throw ctxt.mappingException(this._valueClass);
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class StringDeser extends ArrayDeserializers.ArrayDeser<String[]>
/*     */   {
/*     */     public StringDeser()
/*     */     {
/* 103 */       super();
/*     */     }
/*     */ 
/*     */     public String[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 110 */       if (!jp.isExpectedStartArrayToken()) {
/* 111 */         throw ctxt.mappingException(this._valueClass);
/*     */       }
/* 113 */       ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 114 */       Object[] chunk = buffer.resetAndStart();
/* 115 */       int ix = 0;
/*     */       JsonToken t;
/* 118 */       while ((t = jp.nextToken()) != JsonToken.END_ARRAY)
/*     */       {
/* 120 */         String value = t == JsonToken.VALUE_NULL ? null : jp.getText();
/* 121 */         if (ix >= chunk.length) {
/* 122 */           chunk = buffer.appendCompletedChunk(chunk);
/* 123 */           ix = 0;
/*     */         }
/* 125 */         chunk[(ix++)] = value;
/*     */       }
/* 127 */       String[] result = (String[])buffer.completeAndClearBuffer(chunk, ix, String.class);
/* 128 */       ctxt.returnObjectBuffer(buffer);
/* 129 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class ArrayDeser<T> extends StdDeserializer<T>
/*     */   {
/*     */     protected ArrayDeser(Class<T> cls)
/*     */     {
/*  81 */       super();
/*     */     }
/*     */ 
/*     */     public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  89 */       return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.ArrayDeserializers
 * JD-Core Version:    0.6.0
 */