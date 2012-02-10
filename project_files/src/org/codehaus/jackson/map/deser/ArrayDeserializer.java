/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.type.ArrayType;
/*     */ import org.codehaus.jackson.map.util.ObjectBuffer;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class ArrayDeserializer extends ContainerDeserializer<Object[]>
/*     */ {
/*     */   protected final JavaType _arrayType;
/*     */   protected final boolean _untyped;
/*     */   protected final Class<?> _elementClass;
/*     */   protected final JsonDeserializer<Object> _elementDeserializer;
/*     */   final TypeDeserializer _elementTypeDeserializer;
/*     */ 
/*     */   @Deprecated
/*     */   public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser)
/*     */   {
/*  51 */     this(arrayType, elemDeser, null);
/*     */   }
/*     */ 
/*     */   public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser, TypeDeserializer elemTypeDeser)
/*     */   {
/*  57 */     super([Ljava.lang.Object.class);
/*  58 */     this._arrayType = arrayType;
/*  59 */     this._elementClass = arrayType.getContentType().getRawClass();
/*  60 */     this._untyped = (this._elementClass == Object.class);
/*  61 */     this._elementDeserializer = elemDeser;
/*  62 */     this._elementTypeDeserializer = elemTypeDeser;
/*     */   }
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/*  73 */     return this._arrayType.getContentType();
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/*  78 */     return this._elementDeserializer;
/*     */   }
/*     */ 
/*     */   public Object[] deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  92 */     if (!jp.isExpectedStartArrayToken())
/*     */     {
/*  96 */       if ((jp.getCurrentToken() == JsonToken.VALUE_STRING) && (this._elementClass == Byte.class))
/*     */       {
/*  98 */         return deserializeFromBase64(jp, ctxt);
/*     */       }
/* 100 */       throw ctxt.mappingException(this._arrayType.getRawClass());
/*     */     }
/*     */ 
/* 103 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 104 */     Object[] chunk = buffer.resetAndStart();
/* 105 */     int ix = 0;
/*     */ 
/* 107 */     TypeDeserializer typeDeser = this._elementTypeDeserializer;
/*     */     JsonToken t;
/* 109 */     while ((t = jp.nextToken()) != JsonToken.END_ARRAY)
/*     */     {
/*     */       Object value;
/*     */       Object value;
/* 113 */       if (t == JsonToken.VALUE_NULL) {
/* 114 */         value = null;
/*     */       }
/*     */       else
/*     */       {
/*     */         Object value;
/* 115 */         if (typeDeser == null)
/* 116 */           value = this._elementDeserializer.deserialize(jp, ctxt);
/*     */         else
/* 118 */           value = this._elementDeserializer.deserializeWithType(jp, ctxt, typeDeser);
/*     */       }
/* 120 */       if (ix >= chunk.length) {
/* 121 */         chunk = buffer.appendCompletedChunk(chunk);
/* 122 */         ix = 0;
/*     */       }
/* 124 */       chunk[(ix++)] = value;
/*     */     }
/*     */     Object[] result;
/*     */     Object[] result;
/* 129 */     if (this._untyped)
/* 130 */       result = buffer.completeAndClearBuffer(chunk, ix);
/*     */     else {
/* 132 */       result = buffer.completeAndClearBuffer(chunk, ix, this._elementClass);
/*     */     }
/* 134 */     ctxt.returnObjectBuffer(buffer);
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */   public Object[] deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 146 */     return (Object[])(Object[])typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */ 
/*     */   protected Byte[] deserializeFromBase64(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 159 */     byte[] b = jp.getBinaryValue(ctxt.getBase64Variant());
/*     */ 
/* 161 */     Byte[] result = new Byte[b.length];
/* 162 */     int i = 0; for (int len = b.length; i < len; i++) {
/* 163 */       result[i] = Byte.valueOf(b[i]);
/*     */     }
/* 165 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.ArrayDeserializer
 * JD-Core Version:    0.6.0
 */