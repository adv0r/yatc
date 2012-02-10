/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class ThrowableDeserializer extends BeanDeserializer
/*     */ {
/*     */   protected static final String PROP_NAME_MESSAGE = "message";
/*     */ 
/*     */   public ThrowableDeserializer(BeanDeserializer baseDeserializer)
/*     */   {
/*  28 */     super(baseDeserializer);
/*     */   }
/*     */ 
/*     */   public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  42 */     if (this._propertyBasedCreator != null) {
/*  43 */       return _deserializeUsingPropertyBased(jp, ctxt);
/*     */     }
/*  45 */     if (this._delegatingCreator != null) {
/*  46 */       return this._delegatingCreator.deserialize(jp, ctxt);
/*     */     }
/*  48 */     if (this._beanType.isAbstract()) {
/*  49 */       throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
/*     */     }
/*     */ 
/*  53 */     if (this._stringCreator == null) {
/*  54 */       throw new JsonMappingException("Can not deserialize Throwable of type " + this._beanType + " without having either single-String-arg constructor; or explicit @JsonCreator");
/*     */     }
/*     */ 
/*  58 */     Object throwable = null;
/*  59 */     Object[] pending = null;
/*  60 */     int pendingIx = 0;
/*     */ 
/*  62 */     for (; jp.getCurrentToken() != JsonToken.END_OBJECT; jp.nextToken()) {
/*  63 */       String propName = jp.getCurrentName();
/*  64 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  65 */       jp.nextToken();
/*     */ 
/*  67 */       if (prop != null) {
/*  68 */         if (throwable != null) {
/*  69 */           prop.deserializeAndSet(jp, ctxt, throwable);
/*     */         }
/*     */         else
/*     */         {
/*  73 */           if (pending == null) {
/*  74 */             int len = this._beanProperties.size();
/*  75 */             pending = new Object[len + len];
/*     */           }
/*  77 */           pending[(pendingIx++)] = prop;
/*  78 */           pending[(pendingIx++)] = prop.deserialize(jp, ctxt);
/*     */         }
/*     */ 
/*     */       }
/*  83 */       else if ("message".equals(propName)) {
/*  84 */         throwable = this._stringCreator.construct(jp.getText());
/*     */ 
/*  86 */         if (pending != null) {
/*  87 */           int i = 0; for (int len = pendingIx; i < len; i += 2) {
/*  88 */             prop = (SettableBeanProperty)pending[i];
/*  89 */             prop.set(throwable, pending[(i + 1)]);
/*     */           }
/*  91 */           pending = null;
/*     */         }
/*     */ 
/*     */       }
/*  98 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/*  99 */         jp.skipChildren();
/*     */       }
/* 102 */       else if (this._anySetter != null) {
/* 103 */         this._anySetter.deserializeAndSet(jp, ctxt, throwable, propName);
/*     */       }
/*     */       else
/*     */       {
/* 107 */         handleUnknownProperty(jp, ctxt, throwable, propName);
/*     */       }
/*     */     }
/* 110 */     if (throwable == null)
/*     */     {
/* 117 */       throwable = this._stringCreator.construct(null);
/*     */ 
/* 119 */       if (pending != null) {
/* 120 */         int i = 0; for (int len = pendingIx; i < len; i += 2) {
/* 121 */           SettableBeanProperty prop = (SettableBeanProperty)pending[i];
/* 122 */           prop.set(throwable, pending[(i + 1)]);
/*     */         }
/*     */       }
/*     */     }
/* 126 */     return throwable;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.ThrowableDeserializer
 * JD-Core Version:    0.6.0
 */