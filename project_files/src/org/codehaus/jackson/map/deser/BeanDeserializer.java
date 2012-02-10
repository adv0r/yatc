/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.BeanProperty.Std;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.DeserializerProvider;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.ResolvableDeserializer;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.annotate.JsonCachable;
/*     */ import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.type.ClassKey;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ import org.codehaus.jackson.util.TokenBuffer;
/*     */ 
/*     */ @JsonCachable
/*     */ public class BeanDeserializer extends StdDeserializer<Object>
/*     */   implements ResolvableDeserializer
/*     */ {
/*     */   protected final AnnotatedClass _forClass;
/*     */   protected final JavaType _beanType;
/*     */   protected final BeanProperty _property;
/*     */   protected final Constructor<?> _defaultConstructor;
/*     */   protected final Creator.StringBased _stringCreator;
/*     */   protected final Creator.NumberBased _numberCreator;
/*     */   protected final Creator.Delegating _delegatingCreator;
/*     */   protected final Creator.PropertyBased _propertyBasedCreator;
/*     */   protected final BeanPropertyMap _beanProperties;
/*     */   protected final SettableAnyProperty _anySetter;
/*     */   protected final HashSet<String> _ignorableProps;
/*     */   protected final boolean _ignoreAllUnknown;
/*     */   protected final Map<String, SettableBeanProperty> _backRefs;
/*     */   protected HashMap<ClassKey, JsonDeserializer<Object>> _subDeserializers;
/*     */ 
/*     */   public BeanDeserializer(AnnotatedClass forClass, JavaType type, BeanProperty property, CreatorContainer creators, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, SettableAnyProperty anySetter)
/*     */   {
/* 172 */     super(type);
/* 173 */     this._forClass = forClass;
/* 174 */     this._beanType = type;
/* 175 */     this._property = property;
/* 176 */     this._beanProperties = properties;
/* 177 */     this._backRefs = backRefs;
/* 178 */     this._ignorableProps = ignorableProps;
/* 179 */     this._ignoreAllUnknown = ignoreAllUnknown;
/* 180 */     this._anySetter = anySetter;
/*     */ 
/* 183 */     this._stringCreator = creators.stringCreator();
/* 184 */     this._numberCreator = creators.numberCreator();
/*     */ 
/* 191 */     this._delegatingCreator = creators.delegatingCreator();
/* 192 */     this._propertyBasedCreator = creators.propertyBasedCreator();
/*     */ 
/* 197 */     if ((this._delegatingCreator != null) || (this._propertyBasedCreator != null))
/* 198 */       this._defaultConstructor = null;
/*     */     else
/* 200 */       this._defaultConstructor = creators.getDefaultConstructor();
/*     */   }
/*     */ 
/*     */   protected BeanDeserializer(BeanDeserializer src)
/*     */   {
/* 213 */     super(src._beanType);
/* 214 */     this._forClass = src._forClass;
/* 215 */     this._beanType = src._beanType;
/* 216 */     this._property = src._property;
/* 217 */     this._beanProperties = src._beanProperties;
/* 218 */     this._backRefs = src._backRefs;
/* 219 */     this._ignorableProps = src._ignorableProps;
/* 220 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/* 221 */     this._anySetter = src._anySetter;
/*     */ 
/* 224 */     this._defaultConstructor = src._defaultConstructor;
/* 225 */     this._stringCreator = src._stringCreator;
/* 226 */     this._numberCreator = src._numberCreator;
/* 227 */     this._delegatingCreator = src._delegatingCreator;
/* 228 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*     */   }
/*     */ 
/*     */   public boolean hasProperty(String propertyName)
/*     */   {
/* 238 */     return this._beanProperties.find(propertyName) != null;
/*     */   }
/*     */ 
/*     */   public int getPropertyCount()
/*     */   {
/* 247 */     return this._beanProperties.size();
/*     */   }
/*     */ 
/*     */   public void resolve(DeserializationConfig config, DeserializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 264 */     Iterator it = this._beanProperties.allProperties();
/* 265 */     while (it.hasNext()) {
/* 266 */       SettableBeanProperty prop = (SettableBeanProperty)it.next();
/*     */ 
/* 268 */       if (!prop.hasValueDeserializer()) {
/* 269 */         prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
/*     */       }
/*     */ 
/* 272 */       String refName = prop.getManagedReferenceName();
/* 273 */       if (refName != null) {
/* 274 */         JsonDeserializer valueDeser = prop._valueDeserializer;
/* 275 */         SettableBeanProperty backProp = null;
/* 276 */         boolean isContainer = false;
/* 277 */         if ((valueDeser instanceof BeanDeserializer)) {
/* 278 */           backProp = ((BeanDeserializer)valueDeser).findBackReference(refName);
/* 279 */         } else if ((valueDeser instanceof ContainerDeserializer)) {
/* 280 */           JsonDeserializer contentDeser = ((ContainerDeserializer)valueDeser).getContentDeserializer();
/* 281 */           if (!(contentDeser instanceof BeanDeserializer)) {
/* 282 */             throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': value deserializer is of type ContainerDeserializer, but content type is not handled by a BeanDeserializer " + " (instead it's of type " + contentDeser.getClass().getName() + ")");
/*     */           }
/*     */ 
/* 286 */           backProp = ((BeanDeserializer)contentDeser).findBackReference(refName);
/* 287 */           isContainer = true; } else {
/* 288 */           if ((valueDeser instanceof AbstractDeserializer)) {
/* 289 */             throw new IllegalArgumentException("Can not handle managed/back reference for abstract types (property " + this._beanType.getRawClass().getName() + "." + prop.getName() + ")");
/*     */           }
/* 291 */           throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': type for value deserializer is not BeanDeserializer or ContainerDeserializer, but " + valueDeser.getClass().getName());
/*     */         }
/*     */ 
/* 295 */         if (backProp == null) {
/* 296 */           throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': no back reference property found from type " + prop.getType());
/*     */         }
/*     */ 
/* 300 */         JavaType referredType = this._beanType;
/* 301 */         JavaType backRefType = backProp.getType();
/* 302 */         if (!backRefType.getRawClass().isAssignableFrom(referredType.getRawClass())) {
/* 303 */           throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': back reference type (" + backRefType.getRawClass().getName() + ") not compatible with managed type (" + referredType.getRawClass().getName() + ")");
/*     */         }
/*     */ 
/* 307 */         this._beanProperties.replace(new SettableBeanProperty.ManagedReferenceProperty(refName, prop, backProp, this._forClass.getAnnotations(), isContainer));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 313 */     if ((this._anySetter != null) && (!this._anySetter.hasValueDeserializer())) {
/* 314 */       this._anySetter.setValueDeserializer(findDeserializer(config, provider, this._anySetter.getType(), this._anySetter.getProperty()));
/*     */     }
/*     */ 
/* 318 */     if (this._delegatingCreator != null)
/*     */     {
/* 320 */       BeanProperty.Std property = new BeanProperty.Std(null, this._delegatingCreator.getValueType(), this._forClass.getAnnotations(), this._delegatingCreator.getCreator());
/*     */ 
/* 322 */       JsonDeserializer deser = findDeserializer(config, provider, this._delegatingCreator.getValueType(), property);
/* 323 */       this._delegatingCreator.setDeserializer(deser);
/*     */     }
/*     */ 
/* 326 */     if (this._propertyBasedCreator != null)
/* 327 */       for (SettableBeanProperty prop : this._propertyBasedCreator.properties())
/* 328 */         if (!prop.hasValueDeserializer())
/* 329 */           prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
/*     */   }
/*     */ 
/*     */   public final Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 347 */     JsonToken t = jp.getCurrentToken();
/*     */ 
/* 349 */     if (t == JsonToken.START_OBJECT) {
/* 350 */       jp.nextToken();
/* 351 */       return deserializeFromObject(jp, ctxt);
/*     */     }
/*     */ 
/* 354 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
/*     */     case 1:
/* 356 */       return deserializeFromString(jp, ctxt);
/*     */     case 2:
/*     */     case 3:
/* 359 */       return deserializeFromNumber(jp, ctxt);
/*     */     case 4:
/* 361 */       return jp.getEmbeddedObject();
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/* 366 */       return deserializeUsingCreator(jp, ctxt);
/*     */     case 8:
/*     */     case 9:
/* 369 */       return deserializeFromObject(jp, ctxt);
/*     */     }
/* 371 */     throw ctxt.mappingException(getBeanClass());
/*     */   }
/*     */ 
/*     */   public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object bean)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 383 */     JsonToken t = jp.getCurrentToken();
/*     */ 
/* 385 */     if (t == JsonToken.START_OBJECT) {
/* 386 */       t = jp.nextToken();
/*     */     }
/* 388 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 389 */       String propName = jp.getCurrentName();
/* 390 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 391 */       jp.nextToken();
/*     */ 
/* 393 */       if (prop != null) {
/*     */         try {
/* 395 */           prop.deserializeAndSet(jp, ctxt, bean);
/*     */         } catch (Exception e) {
/* 397 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         }
/*     */ 
/*     */       }
/* 404 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 405 */         jp.skipChildren();
/*     */       }
/* 408 */       else if (this._anySetter != null) {
/* 409 */         this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
/*     */       }
/*     */       else
/*     */       {
/* 413 */         handleUnknownProperty(jp, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 415 */     return bean;
/*     */   }
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 424 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */ 
/*     */   public final Class<?> getBeanClass()
/*     */   {
/* 433 */     return this._beanType.getRawClass();
/*     */   }
/* 435 */   public JavaType getValueType() { return this._beanType;
/*     */   }
/*     */ 
/*     */   public Iterator<SettableBeanProperty> properties()
/*     */   {
/* 443 */     if (this._beanProperties == null) {
/* 444 */       throw new IllegalStateException("Can only call before BeanDeserializer has been resolved");
/*     */     }
/* 446 */     return this._beanProperties.allProperties();
/*     */   }
/*     */ 
/*     */   public SettableBeanProperty findBackReference(String logicalName)
/*     */   {
/* 455 */     if (this._backRefs == null) {
/* 456 */       return null;
/*     */     }
/* 458 */     return (SettableBeanProperty)this._backRefs.get(logicalName);
/*     */   }
/*     */ 
/*     */   public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 470 */     if (this._defaultConstructor == null)
/*     */     {
/* 472 */       if (this._propertyBasedCreator != null) {
/* 473 */         return _deserializeUsingPropertyBased(jp, ctxt);
/*     */       }
/*     */ 
/* 476 */       if (this._delegatingCreator != null) {
/* 477 */         return this._delegatingCreator.deserialize(jp, ctxt);
/*     */       }
/*     */ 
/* 480 */       if (this._beanType.isAbstract()) {
/* 481 */         throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
/*     */       }
/*     */ 
/* 484 */       throw JsonMappingException.from(jp, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (need to add/enable type information?)");
/*     */     }
/*     */ 
/* 487 */     Object bean = constructDefaultInstance();
/* 488 */     for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
/* 489 */       String propName = jp.getCurrentName();
/*     */ 
/* 491 */       jp.nextToken();
/* 492 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 493 */       if (prop != null) {
/*     */         try {
/* 495 */           prop.deserializeAndSet(jp, ctxt, bean);
/*     */         } catch (Exception e) {
/* 497 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         }
/*     */ 
/*     */       }
/* 504 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName)))
/* 505 */         jp.skipChildren();
/* 506 */       else if (this._anySetter != null) {
/*     */         try {
/* 508 */           this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
/*     */         } catch (Exception e) {
/* 510 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 515 */         handleUnknownProperty(jp, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 518 */     return bean;
/*     */   }
/*     */ 
/*     */   public Object deserializeFromString(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 524 */     if (this._stringCreator != null) {
/* 525 */       return this._stringCreator.construct(jp.getText());
/*     */     }
/* 527 */     if (this._delegatingCreator != null) {
/* 528 */       return this._delegatingCreator.deserialize(jp, ctxt);
/*     */     }
/* 530 */     throw ctxt.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON String");
/*     */   }
/*     */ 
/*     */   public Object deserializeFromNumber(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 536 */     if (this._numberCreator != null) {
/* 537 */       switch (jp.getNumberType()) {
/*     */       case INT:
/* 539 */         return this._numberCreator.construct(jp.getIntValue());
/*     */       case LONG:
/* 541 */         return this._numberCreator.construct(jp.getLongValue());
/*     */       }
/*     */     }
/* 544 */     if (this._delegatingCreator != null) {
/* 545 */       return this._delegatingCreator.deserialize(jp, ctxt);
/*     */     }
/* 547 */     throw ctxt.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON Number");
/*     */   }
/*     */ 
/*     */   public Object deserializeUsingCreator(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 553 */     if (this._delegatingCreator != null) {
/*     */       try {
/* 555 */         return this._delegatingCreator.deserialize(jp, ctxt);
/*     */       } catch (Exception e) {
/* 557 */         wrapAndThrow(e, this._beanType.getRawClass(), null, ctxt);
/*     */       }
/*     */     }
/* 560 */     throw ctxt.mappingException(getBeanClass());
/*     */   }
/*     */ 
/*     */   protected final Object _deserializeUsingPropertyBased(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 576 */     Creator.PropertyBased creator = this._propertyBasedCreator;
/* 577 */     PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt);
/*     */ 
/* 580 */     TokenBuffer unknown = null;
/*     */ 
/* 582 */     JsonToken t = jp.getCurrentToken();
/* 583 */     for (; t == JsonToken.FIELD_NAME; t = jp.nextToken()) {
/* 584 */       String propName = jp.getCurrentName();
/* 585 */       jp.nextToken();
/*     */ 
/* 587 */       SettableBeanProperty prop = creator.findCreatorProperty(propName);
/* 588 */       if (prop != null)
/*     */       {
/* 590 */         Object value = prop.deserialize(jp, ctxt);
/* 591 */         if (buffer.assignParameter(prop.getCreatorIndex(), value)) { jp.nextToken();
/*     */           Object bean;
/*     */           try { bean = creator.build(buffer);
/*     */           } catch (Exception e) {
/* 597 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 598 */             continue;
/*     */           }
/*     */ 
/* 601 */           if (bean.getClass() != this._beanType.getRawClass()) {
/* 602 */             return handlePolymorphic(jp, ctxt, bean, unknown);
/*     */           }
/* 604 */           if (unknown != null) {
/* 605 */             bean = handleUnknownProperties(ctxt, bean, unknown);
/*     */           }
/*     */ 
/* 608 */           return deserialize(jp, ctxt, bean);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 613 */         prop = this._beanProperties.find(propName);
/* 614 */         if (prop != null) {
/* 615 */           buffer.bufferProperty(prop, prop.deserialize(jp, ctxt));
/*     */         }
/* 621 */         else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 622 */           jp.skipChildren();
/*     */         }
/* 626 */         else if (this._anySetter != null) {
/* 627 */           buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(jp, ctxt));
/*     */         }
/*     */         else
/*     */         {
/* 631 */           if (unknown == null) {
/* 632 */             unknown = new TokenBuffer(jp.getCodec());
/*     */           }
/* 634 */           unknown.writeFieldName(propName);
/* 635 */           unknown.copyCurrentStructure(jp);
/*     */         }
/*     */       }
/*     */     }
/*     */     Object bean;
/*     */     try {
/* 641 */       bean = creator.build(buffer);
/*     */     } catch (Exception e) {
/* 643 */       wrapAndThrow(e, this._beanType.getRawClass(), null, ctxt);
/* 644 */       return null;
/*     */     }
/* 646 */     if (unknown != null)
/*     */     {
/* 648 */       if (bean.getClass() != this._beanType.getRawClass()) {
/* 649 */         return handlePolymorphic(null, ctxt, bean, unknown);
/*     */       }
/*     */ 
/* 652 */       return handleUnknownProperties(ctxt, bean, unknown);
/*     */     }
/* 654 */     return bean;
/*     */   }
/*     */ 
/*     */   protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object beanOrClass, String propName)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 676 */     if ((this._ignoreAllUnknown) || ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))))
/*     */     {
/* 678 */       jp.skipChildren();
/* 679 */       return;
/*     */     }
/*     */ 
/* 684 */     super.handleUnknownProperty(jp, ctxt, beanOrClass, propName);
/*     */   }
/*     */ 
/*     */   protected Object handleUnknownProperties(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 696 */     unknownTokens.writeEndObject();
/*     */ 
/* 699 */     JsonParser bufferParser = unknownTokens.asParser();
/* 700 */     while (bufferParser.nextToken() != JsonToken.END_OBJECT) {
/* 701 */       String propName = bufferParser.getCurrentName();
/*     */ 
/* 703 */       bufferParser.nextToken();
/* 704 */       handleUnknownProperty(bufferParser, ctxt, bean, propName);
/*     */     }
/* 706 */     return bean;
/*     */   }
/*     */ 
/*     */   protected Object handlePolymorphic(JsonParser jp, DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 725 */     JsonDeserializer subDeser = _findSubclassDeserializer(ctxt, bean, unknownTokens);
/* 726 */     if (subDeser != null) {
/* 727 */       if (unknownTokens != null)
/*     */       {
/* 729 */         unknownTokens.writeEndObject();
/* 730 */         JsonParser p2 = unknownTokens.asParser();
/* 731 */         p2.nextToken();
/* 732 */         bean = subDeser.deserialize(p2, ctxt, bean);
/*     */       }
/*     */ 
/* 735 */       if (jp != null) {
/* 736 */         bean = subDeser.deserialize(jp, ctxt, bean);
/*     */       }
/* 738 */       return bean;
/*     */     }
/*     */ 
/* 741 */     if (unknownTokens != null) {
/* 742 */       bean = handleUnknownProperties(ctxt, bean, unknownTokens);
/*     */     }
/*     */ 
/* 745 */     if (jp != null) {
/* 746 */       bean = deserialize(jp, ctxt, bean);
/*     */     }
/* 748 */     return bean;
/*     */   }
/*     */ 
/*     */   protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*     */     JsonDeserializer subDeser;
/* 761 */     synchronized (this) {
/* 762 */       subDeser = this._subDeserializers == null ? null : (JsonDeserializer)this._subDeserializers.get(new ClassKey(bean.getClass()));
/*     */     }
/* 764 */     if (subDeser != null) {
/* 765 */       return subDeser;
/*     */     }
/*     */ 
/* 768 */     DeserializerProvider deserProv = ctxt.getDeserializerProvider();
/* 769 */     if (deserProv != null) {
/* 770 */       JavaType type = TypeFactory.type(bean.getClass());
/*     */ 
/* 774 */       subDeser = deserProv.findValueDeserializer(ctxt.getConfig(), type, this._property);
/*     */ 
/* 776 */       if (subDeser != null) {
/* 777 */         synchronized (this) {
/* 778 */           if (this._subDeserializers == null) {
/* 779 */             this._subDeserializers = new HashMap();
/*     */           }
/* 781 */           this._subDeserializers.put(new ClassKey(bean.getClass()), subDeser);
/*     */         }
/*     */       }
/*     */     }
/* 785 */     return subDeser;
/*     */   }
/*     */ 
/*     */   protected Object constructDefaultInstance()
/*     */   {
/*     */     try
/*     */     {
/* 799 */       return this._defaultConstructor.newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/* 801 */       ClassUtil.unwrapAndThrowAsIAE(e);
/* 802 */     }return null;
/*     */   }
/*     */ 
/*     */   public void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 832 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 833 */       t = t.getCause();
/*     */     }
/*     */ 
/* 836 */     if ((t instanceof Error)) {
/* 837 */       throw ((Error)t);
/*     */     }
/* 839 */     boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationConfig.Feature.WRAP_EXCEPTIONS));
/*     */ 
/* 841 */     if ((t instanceof IOException)) {
/* 842 */       if ((!wrap) || (!(t instanceof JsonMappingException)))
/* 843 */         throw ((IOException)t);
/*     */     }
/* 845 */     else if ((!wrap) && 
/* 846 */       ((t instanceof RuntimeException))) {
/* 847 */       throw ((RuntimeException)t);
/*     */     }
/*     */ 
/* 851 */     throw JsonMappingException.wrapWithPath(t, bean, fieldName);
/*     */   }
/*     */ 
/*     */   public void wrapAndThrow(Throwable t, Object bean, int index, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 857 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 858 */       t = t.getCause();
/*     */     }
/*     */ 
/* 861 */     if ((t instanceof Error)) {
/* 862 */       throw ((Error)t);
/*     */     }
/* 864 */     boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationConfig.Feature.WRAP_EXCEPTIONS));
/*     */ 
/* 866 */     if ((t instanceof IOException)) {
/* 867 */       if ((!wrap) || (!(t instanceof JsonMappingException)))
/* 868 */         throw ((IOException)t);
/*     */     }
/* 870 */     else if ((!wrap) && 
/* 871 */       ((t instanceof RuntimeException))) {
/* 872 */       throw ((RuntimeException)t);
/*     */     }
/*     */ 
/* 876 */     throw JsonMappingException.wrapWithPath(t, bean, index);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void wrapAndThrow(Throwable t, Object bean, String fieldName)
/*     */     throws IOException
/*     */   {
/* 886 */     wrapAndThrow(t, bean, fieldName, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void wrapAndThrow(Throwable t, Object bean, int index)
/*     */     throws IOException
/*     */   {
/* 896 */     wrapAndThrow(t, bean, index, null);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BeanDeserializer
 * JD-Core Version:    0.6.0
 */