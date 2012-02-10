/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*    */ import org.codehaus.jackson.map.BeanProperty;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ import org.codehaus.jackson.map.JsonDeserializer;
/*    */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public class AsWrapperTypeDeserializer extends TypeDeserializerBase
/*    */ {
/*    */   public AsWrapperTypeDeserializer(JavaType bt, TypeIdResolver idRes, BeanProperty property)
/*    */   {
/* 24 */     super(bt, idRes, property);
/*    */   }
/*    */ 
/*    */   public JsonTypeInfo.As getTypeInclusion()
/*    */   {
/* 29 */     return JsonTypeInfo.As.WRAPPER_OBJECT;
/*    */   }
/*    */ 
/*    */   public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 39 */     return _deserialize(jp, ctxt);
/*    */   }
/*    */ 
/*    */   public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 46 */     return _deserialize(jp, ctxt);
/*    */   }
/*    */ 
/*    */   public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 53 */     return _deserialize(jp, ctxt);
/*    */   }
/*    */ 
/*    */   public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 60 */     return _deserialize(jp, ctxt);
/*    */   }
/*    */ 
/*    */   private final Object _deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 78 */     if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
/* 79 */       throw ctxt.wrongTokenException(jp, JsonToken.START_OBJECT, "need JSON Object to contain As.WRAPPER_OBJECT type information for class " + baseTypeName());
/*    */     }
/*    */ 
/* 83 */     if (jp.nextToken() != JsonToken.FIELD_NAME) {
/* 84 */       throw ctxt.wrongTokenException(jp, JsonToken.FIELD_NAME, "need JSON String that contains type id (for subtype of " + baseTypeName() + ")");
/*    */     }
/*    */ 
/* 87 */     JsonDeserializer deser = _findDeserializer(ctxt, jp.getText());
/* 88 */     jp.nextToken();
/* 89 */     Object value = deser.deserialize(jp, ctxt);
/*    */ 
/* 91 */     if (jp.nextToken() != JsonToken.END_OBJECT) {
/* 92 */       throw ctxt.wrongTokenException(jp, JsonToken.END_OBJECT, "expected closing END_OBJECT after type information and deserialized value");
/*    */     }
/*    */ 
/* 95 */     return value;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.AsWrapperTypeDeserializer
 * JD-Core Version:    0.6.0
 */