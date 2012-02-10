/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import org.codehaus.jackson.JsonProcessingException;
/*    */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*    */ import org.codehaus.jackson.map.BeanProperty;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ import org.codehaus.jackson.map.DeserializerProvider;
/*    */ import org.codehaus.jackson.map.JsonDeserializer;
/*    */ import org.codehaus.jackson.map.TypeDeserializer;
/*    */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public abstract class TypeDeserializerBase extends TypeDeserializer
/*    */ {
/*    */   protected final TypeIdResolver _idResolver;
/*    */   protected final JavaType _baseType;
/*    */   protected final BeanProperty _property;
/*    */   protected final HashMap<String, JsonDeserializer<Object>> _deserializers;
/*    */ 
/*    */   protected TypeDeserializerBase(JavaType baseType, TypeIdResolver idRes, BeanProperty property)
/*    */   {
/* 35 */     this._baseType = baseType;
/* 36 */     this._idResolver = idRes;
/* 37 */     this._property = property;
/* 38 */     this._deserializers = new HashMap();
/*    */   }
/*    */   public abstract JsonTypeInfo.As getTypeInclusion();
/*    */ 
/*    */   public String baseTypeName() {
/* 44 */     return this._baseType.getRawClass().getName();
/*    */   }
/*    */   public String getPropertyName() {
/* 47 */     return null;
/*    */   }
/*    */   public TypeIdResolver getTypeIdResolver() {
/* 50 */     return this._idResolver;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 55 */     StringBuilder sb = new StringBuilder();
/* 56 */     sb.append('[').append(getClass().getName());
/* 57 */     sb.append("; base-type:").append(this._baseType);
/* 58 */     sb.append("; id-resolver: ").append(this._idResolver);
/* 59 */     sb.append(']');
/* 60 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   protected final JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
/*    */     throws IOException, JsonProcessingException
/*    */   {
/*    */     JsonDeserializer deser;
/* 74 */     synchronized (this._deserializers) {
/* 75 */       deser = (JsonDeserializer)this._deserializers.get(typeId);
/* 76 */       if (deser == null) {
/* 77 */         JavaType type = this._idResolver.typeFromId(typeId);
/* 78 */         if (type == null) {
/* 79 */           throw ctxt.unknownTypeException(this._baseType, typeId);
/*    */         }
/*    */ 
/* 89 */         if ((this._baseType != null) && (this._baseType.getClass() == type.getClass())) {
/* 90 */           type = this._baseType.narrowBy(type.getRawClass());
/*    */         }
/* 92 */         deser = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), type, this._property);
/* 93 */         this._deserializers.put(typeId, deser);
/*    */       }
/*    */     }
/* 96 */     return deser;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.TypeDeserializerBase
 * JD-Core Version:    0.6.0
 */