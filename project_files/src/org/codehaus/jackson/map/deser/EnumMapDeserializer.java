/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.EnumMap;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ import org.codehaus.jackson.map.JsonDeserializer;
/*    */ import org.codehaus.jackson.map.TypeDeserializer;
/*    */ 
/*    */ public final class EnumMapDeserializer extends StdDeserializer<EnumMap<?, ?>>
/*    */ {
/*    */   final EnumResolver<?> _enumResolver;
/*    */   final JsonDeserializer<Object> _valueDeserializer;
/*    */ 
/*    */   public EnumMapDeserializer(EnumResolver<?> enumRes, JsonDeserializer<Object> valueDes)
/*    */   {
/* 30 */     super(EnumMap.class);
/* 31 */     this._enumResolver = enumRes;
/* 32 */     this._valueDeserializer = valueDes;
/*    */   }
/*    */ 
/*    */   public EnumMap<?, ?> deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 41 */     if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
/* 42 */       throw ctxt.mappingException(EnumMap.class);
/*    */     }
/* 44 */     EnumMap result = constructMap();
/*    */ 
/* 46 */     while (jp.nextToken() != JsonToken.END_OBJECT) {
/* 47 */       String fieldName = jp.getCurrentName();
/* 48 */       Enum key = this._enumResolver.findEnum(fieldName);
/* 49 */       if (key == null) {
/* 50 */         throw ctxt.weirdStringException(this._enumResolver.getEnumClass(), "value not one of declared Enum instance names");
/*    */       }
/*    */ 
/* 53 */       JsonToken t = jp.nextToken();
/*    */ 
/* 57 */       Object value = t == JsonToken.VALUE_NULL ? null : this._valueDeserializer.deserialize(jp, ctxt);
/*    */ 
/* 59 */       result.put(key, value);
/*    */     }
/* 61 */     return result;
/*    */   }
/*    */ 
/*    */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 70 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*    */   }
/*    */ 
/*    */   private EnumMap<?, ?> constructMap()
/*    */   {
/* 76 */     Class enumCls = this._enumResolver.getEnumClass();
/* 77 */     return new EnumMap(enumCls);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.EnumMapDeserializer
 * JD-Core Version:    0.6.0
 */