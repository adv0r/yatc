/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.EnumSet;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ import org.codehaus.jackson.map.TypeDeserializer;
/*    */ 
/*    */ public final class EnumSetDeserializer extends StdDeserializer<EnumSet<?>>
/*    */ {
/*    */   final Class<Enum> _enumClass;
/*    */   final EnumDeserializer _enumDeserializer;
/*    */ 
/*    */   public EnumSetDeserializer(EnumResolver enumRes)
/*    */   {
/* 31 */     super(EnumSet.class);
/* 32 */     this._enumDeserializer = new EnumDeserializer(enumRes);
/*    */ 
/* 34 */     this._enumClass = enumRes.getEnumClass();
/*    */   }
/*    */ 
/*    */   public EnumSet<?> deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 43 */     if (!jp.isExpectedStartArrayToken()) {
/* 44 */       throw ctxt.mappingException(EnumSet.class);
/*    */     }
/* 46 */     EnumSet result = constructSet();
/*    */     JsonToken t;
/* 49 */     while ((t = jp.nextToken()) != JsonToken.END_ARRAY)
/*    */     {
/* 55 */       if (t == JsonToken.VALUE_NULL) {
/* 56 */         throw ctxt.mappingException(this._enumClass);
/*    */       }
/* 58 */       Enum value = this._enumDeserializer.deserialize(jp, ctxt);
/* 59 */       result.add(value);
/*    */     }
/* 61 */     return result;
/*    */   }
/*    */ 
/*    */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 69 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*    */   }
/*    */ 
/*    */   private EnumSet constructSet()
/*    */   {
/* 76 */     return EnumSet.noneOf(this._enumClass);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.EnumSetDeserializer
 * JD-Core Version:    0.6.0
 */