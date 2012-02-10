/*    */ package org.codehaus.jackson.map.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.map.JsonSerializableWithType;
/*    */ import org.codehaus.jackson.map.JsonSerializer;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.TypeSerializer;
/*    */ import org.codehaus.jackson.map.type.TypeFactory;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public class JSONPObject
/*    */   implements JsonSerializableWithType
/*    */ {
/*    */   protected final String _function;
/*    */   protected final Object _value;
/*    */   protected final JavaType _serializationType;
/*    */ 
/*    */   public JSONPObject(String function, Object value)
/*    */   {
/* 42 */     this(function, value, (JavaType)null);
/*    */   }
/*    */ 
/*    */   public JSONPObject(String function, Object value, JavaType asType)
/*    */   {
/* 47 */     this._function = function;
/* 48 */     this._value = value;
/* 49 */     this._serializationType = asType;
/*    */   }
/*    */ 
/*    */   public JSONPObject(String function, Object value, Class<?> rawType) {
/* 53 */     this._function = function;
/* 54 */     this._value = value;
/* 55 */     this._serializationType = (rawType == null ? null : TypeFactory.type(rawType));
/*    */   }
/*    */ 
/*    */   public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 68 */     serialize(jgen, provider);
/*    */   }
/*    */ 
/*    */   public void serialize(JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 75 */     jgen.writeRaw(this._function);
/* 76 */     jgen.writeRaw('(');
/* 77 */     if (this._value == null) {
/* 78 */       provider.defaultSerializeNull(jgen);
/* 79 */     } else if (this._serializationType != null) {
/* 80 */       provider.findTypedValueSerializer(this._serializationType, true, null).serialize(this._value, jgen, provider);
/*    */     } else {
/* 82 */       Class cls = this._value.getClass();
/* 83 */       provider.findTypedValueSerializer(cls, true, null).serialize(this._value, jgen, provider);
/*    */     }
/* 85 */     jgen.writeRaw(')');
/*    */   }
/*    */ 
/*    */   public String getFunction()
/*    */   {
/* 94 */     return this._function; } 
/* 95 */   public Object getValue() { return this._value; } 
/* 96 */   public JavaType getSerializationType() { return this._serializationType;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.JSONPObject
 * JD-Core Version:    0.6.0
 */