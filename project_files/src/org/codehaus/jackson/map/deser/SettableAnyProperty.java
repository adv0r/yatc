/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class SettableAnyProperty
/*     */ {
/*     */   protected final BeanProperty _property;
/*     */   protected final Method _setter;
/*     */   protected final JavaType _type;
/*     */   protected JsonDeserializer<Object> _valueDeserializer;
/*     */ 
/*     */   public SettableAnyProperty(BeanProperty property, AnnotatedMethod setter, JavaType type)
/*     */   {
/*  49 */     this._property = property;
/*  50 */     this._type = type;
/*  51 */     this._setter = setter.getAnnotated();
/*     */   }
/*     */ 
/*     */   public void setValueDeserializer(JsonDeserializer<Object> deser)
/*     */   {
/*  56 */     if (this._valueDeserializer != null) {
/*  57 */       throw new IllegalStateException("Already had assigned deserializer for SettableAnyProperty");
/*     */     }
/*  59 */     this._valueDeserializer = deser;
/*     */   }
/*     */ 
/*     */   public BeanProperty getProperty()
/*     */   {
/*  68 */     return this._property;
/*     */   }
/*  70 */   public boolean hasValueDeserializer() { return this._valueDeserializer != null; } 
/*     */   public JavaType getType() {
/*  72 */     return this._type;
/*     */   }
/*     */ 
/*     */   public final void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance, String propName)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  88 */     set(instance, propName, deserialize(jp, ctxt));
/*     */   }
/*     */ 
/*     */   public final Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  94 */     JsonToken t = jp.getCurrentToken();
/*  95 */     if (t == JsonToken.VALUE_NULL) {
/*  96 */       return null;
/*     */     }
/*  98 */     return this._valueDeserializer.deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */   public final void set(Object instance, String propName, Object value) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 105 */       this._setter.invoke(instance, new Object[] { propName, value });
/*     */     } catch (Exception e) {
/* 107 */       _throwAsIOE(e, propName, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _throwAsIOE(Exception e, String propName, Object value)
/*     */     throws IOException
/*     */   {
/* 125 */     if ((e instanceof IllegalArgumentException)) {
/* 126 */       String actType = value == null ? "[NULL]" : value.getClass().getName();
/* 127 */       StringBuilder msg = new StringBuilder("Problem deserializing \"any\" property '").append(propName);
/* 128 */       msg.append("' of class " + getClassName() + " (expected type: ").append(this._type);
/* 129 */       msg.append("; actual type: ").append(actType).append(")");
/* 130 */       String origMsg = e.getMessage();
/* 131 */       if (origMsg != null)
/* 132 */         msg.append(", problem: ").append(origMsg);
/*     */       else {
/* 134 */         msg.append(" (no error message provided)");
/*     */       }
/* 136 */       throw new JsonMappingException(msg.toString(), null, e);
/*     */     }
/* 138 */     if ((e instanceof IOException)) {
/* 139 */       throw ((IOException)e);
/*     */     }
/* 141 */     if ((e instanceof RuntimeException)) {
/* 142 */       throw ((RuntimeException)e);
/*     */     }
/*     */ 
/* 145 */     Throwable t = e;
/* 146 */     while (t.getCause() != null) {
/* 147 */       t = t.getCause();
/*     */     }
/* 149 */     throw new JsonMappingException(t.getMessage(), null, t);
/*     */   }
/*     */   private String getClassName() {
/* 152 */     return this._setter.getDeclaringClass().getName();
/*     */   }
/* 154 */   public String toString() { return "[any property on class " + getClassName() + "]";
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.SettableAnyProperty
 * JD-Core Version:    0.6.0
 */