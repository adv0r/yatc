/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.DeserializerProvider;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.KeyDeserializer;
/*     */ import org.codehaus.jackson.map.ResolvableDeserializer;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class MapDeserializer extends ContainerDeserializer<Map<Object, Object>>
/*     */   implements ResolvableDeserializer
/*     */ {
/*     */   protected final JavaType _mapType;
/*     */   protected final KeyDeserializer _keyDeserializer;
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final Constructor<Map<Object, Object>> _defaultCtor;
/*     */   protected Creator.PropertyBased _propertyBasedCreator;
/*     */   protected HashSet<String> _ignorableProperties;
/*     */ 
/*     */   public MapDeserializer(JavaType mapType, Constructor<Map<Object, Object>> defCtor, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser)
/*     */   {
/*  77 */     super(Map.class);
/*  78 */     this._mapType = mapType;
/*  79 */     this._defaultCtor = defCtor;
/*  80 */     this._keyDeserializer = keyDeser;
/*  81 */     this._valueDeserializer = valueDeser;
/*  82 */     this._valueTypeDeserializer = valueTypeDeser;
/*     */   }
/*     */ 
/*     */   public void setCreators(CreatorContainer creators)
/*     */   {
/*  91 */     this._propertyBasedCreator = creators.propertyBasedCreator();
/*     */   }
/*     */ 
/*     */   public void setIgnorableProperties(String[] ignorable)
/*     */   {
/*  96 */     this._ignorableProperties = ((ignorable == null) || (ignorable.length == 0) ? null : ArrayBuilders.arrayToSet(ignorable));
/*     */   }
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 108 */     return this._mapType.getContentType();
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 113 */     return this._valueDeserializer;
/*     */   }
/*     */ 
/*     */   public void resolve(DeserializationConfig config, DeserializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 131 */     if (this._propertyBasedCreator != null)
/* 132 */       for (SettableBeanProperty prop : this._propertyBasedCreator.properties())
/* 133 */         prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
/*     */   }
/*     */ 
/*     */   public Map<Object, Object> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 149 */     JsonToken t = jp.getCurrentToken();
/* 150 */     if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME) && (t != JsonToken.END_OBJECT)) {
/* 151 */       throw ctxt.mappingException(getMapClass());
/*     */     }
/* 153 */     if (this._propertyBasedCreator != null) {
/* 154 */       return _deserializeUsingCreator(jp, ctxt);
/*     */     }
/*     */ 
/* 157 */     if (this._defaultCtor == null)
/* 158 */       throw ctxt.instantiationException(getMapClass(), "No default constructor found"); Map result;
/*     */     try {
/* 161 */       result = (Map)this._defaultCtor.newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/* 163 */       throw ctxt.instantiationException(getMapClass(), e);
/*     */     }
/* 165 */     _readAndBind(jp, ctxt, result);
/* 166 */     return result;
/*     */   }
/*     */ 
/*     */   public Map<Object, Object> deserialize(JsonParser jp, DeserializationContext ctxt, Map<Object, Object> result)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 175 */     JsonToken t = jp.getCurrentToken();
/* 176 */     if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME)) {
/* 177 */       throw ctxt.mappingException(getMapClass());
/*     */     }
/* 179 */     _readAndBind(jp, ctxt, result);
/* 180 */     return result;
/*     */   }
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 189 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */ 
/*     */   public final Class<?> getMapClass()
/*     */   {
/* 199 */     return this._mapType.getRawClass();
/*     */   }
/* 201 */   public JavaType getValueType() { return this._mapType;
/*     */   }
/*     */ 
/*     */   protected final void _readAndBind(JsonParser jp, DeserializationContext ctxt, Map<Object, Object> result)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 213 */     JsonToken t = jp.getCurrentToken();
/* 214 */     if (t == JsonToken.START_OBJECT) {
/* 215 */       t = jp.nextToken();
/*     */     }
/* 217 */     KeyDeserializer keyDes = this._keyDeserializer;
/* 218 */     JsonDeserializer valueDes = this._valueDeserializer;
/* 219 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 220 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken())
/*     */     {
/* 222 */       String fieldName = jp.getCurrentName();
/* 223 */       Object key = keyDes == null ? fieldName : keyDes.deserializeKey(fieldName, ctxt);
/*     */ 
/* 225 */       t = jp.nextToken();
/* 226 */       if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(fieldName))) {
/* 227 */         jp.skipChildren();
/*     */       }
/*     */       else
/*     */       {
/*     */         Object value;
/*     */         Object value;
/* 232 */         if (t == JsonToken.VALUE_NULL) {
/* 233 */           value = null;
/*     */         }
/*     */         else
/*     */         {
/*     */           Object value;
/* 234 */           if (typeDeser == null)
/* 235 */             value = valueDes.deserialize(jp, ctxt);
/*     */           else {
/* 237 */             value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 243 */         result.put(key, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Map<Object, Object> _deserializeUsingCreator(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 251 */     Creator.PropertyBased creator = this._propertyBasedCreator;
/* 252 */     PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt);
/*     */ 
/* 254 */     JsonToken t = jp.getCurrentToken();
/* 255 */     if (t == JsonToken.START_OBJECT) {
/* 256 */       t = jp.nextToken();
/*     */     }
/* 258 */     JsonDeserializer valueDes = this._valueDeserializer;
/* 259 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 260 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 261 */       String propName = jp.getCurrentName();
/* 262 */       t = jp.nextToken();
/* 263 */       if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(propName))) {
/* 264 */         jp.skipChildren();
/*     */       }
/*     */       else
/*     */       {
/* 268 */         SettableBeanProperty prop = creator.findCreatorProperty(propName);
/* 269 */         if (prop != null)
/*     */         {
/* 271 */           Object value = prop.deserialize(jp, ctxt);
/* 272 */           if (buffer.assignParameter(prop.getCreatorIndex(), value)) { jp.nextToken();
/*     */             Map result;
/*     */             try { result = (Map)creator.build(buffer);
/*     */             } catch (Exception e) {
/* 278 */               wrapAndThrow(e, this._mapType.getRawClass());
/* 279 */               return null;
/*     */             }
/* 281 */             _readAndBind(jp, ctxt, result);
/* 282 */             return result;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 287 */           String fieldName = jp.getCurrentName();
/* 288 */           Object key = this._keyDeserializer == null ? fieldName : this._keyDeserializer.deserializeKey(fieldName, ctxt);
/*     */           Object value;
/*     */           Object value;
/* 290 */           if (t == JsonToken.VALUE_NULL) {
/* 291 */             value = null;
/*     */           }
/*     */           else
/*     */           {
/*     */             Object value;
/* 292 */             if (typeDeser == null)
/* 293 */               value = valueDes.deserialize(jp, ctxt);
/*     */             else
/* 295 */               value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */           }
/* 297 */           buffer.bufferMapProperty(key, value);
/*     */         }
/*     */       }
/*     */     }
/*     */     try {
/* 302 */       return (Map)creator.build(buffer);
/*     */     } catch (Exception e) {
/* 304 */       wrapAndThrow(e, this._mapType.getRawClass());
/* 305 */     }return null;
/*     */   }
/*     */ 
/*     */   protected void wrapAndThrow(Throwable t, Object ref)
/*     */     throws IOException
/*     */   {
/* 314 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 315 */       t = t.getCause();
/*     */     }
/*     */ 
/* 318 */     if ((t instanceof Error)) {
/* 319 */       throw ((Error)t);
/*     */     }
/*     */ 
/* 322 */     if (((t instanceof IOException)) && (!(t instanceof JsonMappingException))) {
/* 323 */       throw ((IOException)t);
/*     */     }
/* 325 */     throw JsonMappingException.wrapWithPath(t, ref, null);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.MapDeserializer
 * JD-Core Version:    0.6.0
 */