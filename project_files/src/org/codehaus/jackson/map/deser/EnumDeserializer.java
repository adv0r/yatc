/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.annotate.JsonCachable;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ 
/*     */ @JsonCachable
/*     */ public class EnumDeserializer extends StdScalarDeserializer<Enum<?>>
/*     */ {
/*     */   final EnumResolver<?> _resolver;
/*     */ 
/*     */   public EnumDeserializer(EnumResolver<?> res)
/*     */   {
/*  32 */     super(Enum.class);
/*  33 */     this._resolver = res;
/*     */   }
/*     */ 
/*     */   public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory)
/*     */   {
/*  49 */     if (factory.getParameterType(0) != String.class) {
/*  50 */       throw new IllegalArgumentException("Parameter #0 type for factory method (" + factory + ") not suitable, must be java.lang.String");
/*     */     }
/*  52 */     if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
/*  53 */       ClassUtil.checkAndFixAccess(factory.getMember());
/*     */     }
/*  55 */     return new FactoryBasedDeserializer(enumClass, factory);
/*     */   }
/*     */ 
/*     */   public Enum<?> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  68 */     JsonToken curr = jp.getCurrentToken();
/*     */ 
/*  71 */     if (curr == JsonToken.VALUE_STRING) {
/*  72 */       String name = jp.getText();
/*  73 */       Enum result = this._resolver.findEnum(name);
/*  74 */       if (result == null) {
/*  75 */         throw ctxt.weirdStringException(this._resolver.getEnumClass(), "value not one of declared Enum instance names");
/*     */       }
/*  77 */       return result;
/*     */     }
/*     */ 
/*  80 */     if (curr == JsonToken.VALUE_NUMBER_INT)
/*     */     {
/*  84 */       if (ctxt.isEnabled(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
/*  85 */         throw ctxt.mappingException("Not allowed to deserialize Enum value out of JSON number (disable DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS to allow)");
/*     */       }
/*     */ 
/*  88 */       int index = jp.getIntValue();
/*  89 */       Enum result = this._resolver.getEnum(index);
/*  90 */       if (result == null) {
/*  91 */         throw ctxt.weirdNumberException(this._resolver.getEnumClass(), "index value outside legal index range [0.." + this._resolver.lastValidIndex() + "]");
/*     */       }
/*  93 */       return result;
/*     */     }
/*  95 */     throw ctxt.mappingException(this._resolver.getEnumClass());
/*     */   }
/*     */ 
/*     */   protected static class FactoryBasedDeserializer extends StdScalarDeserializer<Object>
/*     */   {
/*     */     protected final Class<?> _enumClass;
/*     */     protected final Method _factory;
/*     */ 
/*     */     public FactoryBasedDeserializer(Class<?> cls, AnnotatedMethod f)
/*     */     {
/* 116 */       super();
/* 117 */       this._enumClass = cls;
/* 118 */       this._factory = f.getAnnotated();
/*     */     }
/*     */ 
/*     */     public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 125 */       JsonToken curr = jp.getCurrentToken();
/*     */ 
/* 128 */       if (curr != JsonToken.VALUE_STRING) {
/* 129 */         throw ctxt.mappingException(this._enumClass);
/*     */       }
/* 131 */       String value = jp.getText();
/*     */       try {
/* 133 */         return this._factory.invoke(this._enumClass, new Object[] { value });
/*     */       } catch (Exception e) {
/* 135 */         ClassUtil.unwrapAndThrowAsIAE(e);
/*     */       }
/* 137 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.EnumDeserializer
 * JD-Core Version:    0.6.0
 */