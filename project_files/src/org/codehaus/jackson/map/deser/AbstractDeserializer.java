/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ import org.codehaus.jackson.map.JsonDeserializer;
/*    */ import org.codehaus.jackson.map.TypeDeserializer;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public class AbstractDeserializer extends JsonDeserializer<Object>
/*    */ {
/*    */   protected final JavaType _baseType;
/*    */ 
/*    */   public AbstractDeserializer(JavaType bt)
/*    */   {
/* 27 */     this._baseType = bt;
/*    */   }
/*    */ 
/*    */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 38 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()])
/*    */     {
/*    */     case 1:
/* 43 */       return jp.getText();
/*    */     case 2:
/* 47 */       if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)) {
/* 48 */         return jp.getBigIntegerValue();
/*    */       }
/* 50 */       return Integer.valueOf(jp.getIntValue());
/*    */     case 3:
/* 54 */       if (ctxt.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 55 */         return jp.getDecimalValue();
/*    */       }
/* 57 */       return Double.valueOf(jp.getDoubleValue());
/*    */     case 4:
/* 60 */       return Boolean.TRUE;
/*    */     case 5:
/* 62 */       return Boolean.FALSE;
/*    */     case 6:
/* 64 */       return jp.getEmbeddedObject();
/*    */     case 7:
/* 67 */       return null;
/*    */     case 8:
/* 77 */       return typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*    */     }
/*    */ 
/* 89 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*    */   }
/*    */ 
/*    */   public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/* 97 */     throw ctxt.instantiationException(this._baseType.getClass(), "abstract types can only be instantiated with additional type information");
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.AbstractDeserializer
 * JD-Core Version:    0.6.0
 */