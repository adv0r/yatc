/*     */ package org.codehaus.jackson.map.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.JsonSerializableWithType;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class JSONWrappedObject
/*     */   implements JsonSerializableWithType
/*     */ {
/*     */   protected final String _prefix;
/*     */   protected final String _suffix;
/*     */   protected final Object _value;
/*     */   protected final JavaType _serializationType;
/*     */ 
/*     */   public JSONWrappedObject(String prefix, String suffix, Object value)
/*     */   {
/*  51 */     this(prefix, suffix, value, (JavaType)null);
/*     */   }
/*     */ 
/*     */   public JSONWrappedObject(String prefix, String suffix, Object value, JavaType asType)
/*     */   {
/*  56 */     this._prefix = prefix;
/*  57 */     this._suffix = suffix;
/*  58 */     this._value = value;
/*  59 */     this._serializationType = asType;
/*     */   }
/*     */ 
/*     */   public JSONWrappedObject(String prefix, String suffix, Object value, Class<?> rawType) {
/*  63 */     this._prefix = prefix;
/*  64 */     this._suffix = suffix;
/*  65 */     this._value = value;
/*  66 */     this._serializationType = (rawType == null ? null : TypeFactory.type(rawType));
/*     */   }
/*     */ 
/*     */   public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  79 */     serialize(jgen, provider);
/*     */   }
/*     */ 
/*     */   public void serialize(JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  86 */     if (this._prefix != null) jgen.writeRaw(this._prefix);
/*  87 */     if (this._value == null) {
/*  88 */       provider.defaultSerializeNull(jgen);
/*  89 */     } else if (this._serializationType != null) {
/*  90 */       provider.findTypedValueSerializer(this._serializationType, true, null).serialize(this._value, jgen, provider);
/*     */     } else {
/*  92 */       Class cls = this._value.getClass();
/*  93 */       provider.findTypedValueSerializer(cls, true, null).serialize(this._value, jgen, provider);
/*     */     }
/*  95 */     if (this._suffix != null) jgen.writeRaw(this._suffix);
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 104 */     return this._prefix; } 
/* 105 */   public String getSuffix() { return this._suffix; } 
/* 106 */   public Object getValue() { return this._value; } 
/* 107 */   public JavaType getSerializationType() { return this._serializationType;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.JSONWrappedObject
 * JD-Core Version:    0.6.0
 */