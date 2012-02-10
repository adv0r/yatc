/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ abstract class Creator
/*     */ {
/*     */   static final class PropertyBased
/*     */   {
/*     */     protected final Constructor<?> _ctor;
/*     */     protected final Method _factoryMethod;
/*     */     protected final HashMap<String, SettableBeanProperty> _properties;
/*     */     protected final Object[] _defaultValues;
/*     */ 
/*     */     public PropertyBased(AnnotatedConstructor ctor, SettableBeanProperty[] ctorProps, AnnotatedMethod factory, SettableBeanProperty[] factoryProps)
/*     */     {
/*     */       SettableBeanProperty[] props;
/* 222 */       if (ctor != null) {
/* 223 */         this._ctor = ctor.getAnnotated();
/* 224 */         this._factoryMethod = null;
/* 225 */         props = ctorProps;
/*     */       }
/*     */       else
/*     */       {
/*     */         SettableBeanProperty[] props;
/* 226 */         if (factory != null) {
/* 227 */           this._ctor = null;
/* 228 */           this._factoryMethod = factory.getAnnotated();
/* 229 */           props = factoryProps;
/*     */         } else {
/* 231 */           throw new IllegalArgumentException("Internal error: neither delegating constructor nor factory method passed");
/*     */         }
/*     */       }
/*     */       SettableBeanProperty[] props;
/* 233 */       this._properties = new HashMap();
/*     */ 
/* 235 */       Object[] defValues = null;
/* 236 */       int i = 0; for (int len = props.length; i < len; i++) {
/* 237 */         SettableBeanProperty prop = props[i];
/* 238 */         this._properties.put(prop.getName(), prop);
/* 239 */         if (prop.getType().isPrimitive()) {
/* 240 */           if (defValues == null) {
/* 241 */             defValues = new Object[len];
/*     */           }
/* 243 */           defValues[i] = ClassUtil.defaultValue(prop.getType().getRawClass());
/*     */         }
/*     */       }
/* 246 */       this._defaultValues = defValues;
/*     */     }
/*     */ 
/*     */     public Collection<SettableBeanProperty> properties() {
/* 250 */       return this._properties.values();
/*     */     }
/*     */ 
/*     */     public SettableBeanProperty findCreatorProperty(String name) {
/* 254 */       return (SettableBeanProperty)this._properties.get(name);
/*     */     }
/*     */ 
/*     */     public PropertyValueBuffer startBuilding(JsonParser jp, DeserializationContext ctxt)
/*     */     {
/* 262 */       return new PropertyValueBuffer(jp, ctxt, this._properties.size());
/*     */     }
/*     */ 
/*     */     public Object build(PropertyValueBuffer buffer)
/*     */       throws Exception
/*     */     {
/*     */       Object bean;
/*     */       try
/*     */       {
/*     */         Object bean;
/* 270 */         if (this._ctor != null)
/* 271 */           bean = this._ctor.newInstance(buffer.getParameters(this._defaultValues));
/*     */         else
/* 273 */           bean = this._factoryMethod.invoke(null, buffer.getParameters(this._defaultValues));
/*     */       }
/*     */       catch (Exception e) {
/* 276 */         ClassUtil.throwRootCause(e);
/* 277 */         return null;
/*     */       }
/*     */ 
/* 280 */       for (PropertyValue pv = buffer.buffered(); pv != null; pv = pv.next) {
/* 281 */         pv.assign(bean);
/*     */       }
/* 283 */       return bean;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Delegating
/*     */   {
/*     */     protected final AnnotatedMember _creator;
/*     */     protected final JavaType _valueType;
/*     */     protected final Constructor<?> _ctor;
/*     */     protected final Method _factoryMethod;
/*     */     protected JsonDeserializer<Object> _deserializer;
/*     */ 
/*     */     public Delegating(AnnotatedConstructor ctor, AnnotatedMethod factory)
/*     */     {
/* 153 */       if (ctor != null) {
/* 154 */         this._creator = ctor;
/* 155 */         this._ctor = ctor.getAnnotated();
/* 156 */         this._factoryMethod = null;
/* 157 */         this._valueType = TypeFactory.type(ctor.getParameterType(0));
/* 158 */       } else if (factory != null) {
/* 159 */         this._creator = factory;
/* 160 */         this._ctor = null;
/* 161 */         this._factoryMethod = factory.getAnnotated();
/* 162 */         this._valueType = TypeFactory.type(factory.getParameterType(0));
/*     */       } else {
/* 164 */         throw new IllegalArgumentException("Internal error: neither delegating constructor nor factory method passed");
/*     */       }
/*     */     }
/*     */ 
/*     */     public JavaType getValueType() {
/* 168 */       return this._valueType;
/*     */     }
/* 170 */     public AnnotatedMember getCreator() { return this._creator; }
/*     */ 
/*     */     public void setDeserializer(JsonDeserializer<Object> deser)
/*     */     {
/* 174 */       this._deserializer = deser;
/*     */     }
/*     */ 
/*     */     public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 180 */       Object value = this._deserializer.deserialize(jp, ctxt);
/*     */       try {
/* 182 */         if (this._ctor != null) {
/* 183 */           return this._ctor.newInstance(new Object[] { value });
/*     */         }
/*     */ 
/* 186 */         return this._factoryMethod.invoke(null, new Object[] { value });
/*     */       } catch (Exception e) {
/* 188 */         ClassUtil.unwrapAndThrowAsIAE(e);
/* 189 */       }return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class NumberBased
/*     */   {
/*     */     protected final Class<?> _valueClass;
/*     */     protected final Constructor<?> _intCtor;
/*     */     protected final Constructor<?> _longCtor;
/*     */     protected final Method _intFactoryMethod;
/*     */     protected final Method _longFactoryMethod;
/*     */ 
/*     */     public NumberBased(Class<?> valueClass, AnnotatedConstructor intCtor, AnnotatedMethod ifm, AnnotatedConstructor longCtor, AnnotatedMethod lfm)
/*     */     {
/*  77 */       this._valueClass = valueClass;
/*  78 */       this._intCtor = (intCtor == null ? null : intCtor.getAnnotated());
/*  79 */       this._longCtor = (longCtor == null ? null : longCtor.getAnnotated());
/*  80 */       this._intFactoryMethod = (ifm == null ? null : ifm.getAnnotated());
/*  81 */       this._longFactoryMethod = (lfm == null ? null : lfm.getAnnotated());
/*     */     }
/*     */ 
/*     */     public Object construct(int value)
/*     */     {
/*     */       try
/*     */       {
/*  88 */         if (this._intCtor != null) {
/*  89 */           return this._intCtor.newInstance(new Object[] { Integer.valueOf(value) });
/*     */         }
/*  91 */         if (this._intFactoryMethod != null)
/*  92 */           return this._intFactoryMethod.invoke(this._valueClass, new Object[] { Integer.valueOf(value) });
/*     */       }
/*     */       catch (Exception e) {
/*  95 */         ClassUtil.unwrapAndThrowAsIAE(e);
/*     */       }
/*     */ 
/*  98 */       return construct(value);
/*     */     }
/*     */ 
/*     */     public Object construct(long value)
/*     */     {
/*     */       try
/*     */       {
/* 108 */         if (this._longCtor != null) {
/* 109 */           return this._longCtor.newInstance(new Object[] { Long.valueOf(value) });
/*     */         }
/* 111 */         if (this._longFactoryMethod != null)
/* 112 */           return this._longFactoryMethod.invoke(this._valueClass, new Object[] { Long.valueOf(value) });
/*     */       }
/*     */       catch (Exception e) {
/* 115 */         ClassUtil.unwrapAndThrowAsIAE(e);
/*     */       }
/* 117 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class StringBased
/*     */   {
/*     */     protected final Class<?> _valueClass;
/*     */     protected final Method _factoryMethod;
/*     */     protected final Constructor<?> _ctor;
/*     */ 
/*     */     public StringBased(Class<?> valueClass, AnnotatedConstructor ctor, AnnotatedMethod factoryMethod)
/*     */     {
/*  38 */       this._valueClass = valueClass;
/*  39 */       this._ctor = (ctor == null ? null : ctor.getAnnotated());
/*  40 */       this._factoryMethod = (factoryMethod == null ? null : factoryMethod.getAnnotated());
/*     */     }
/*     */ 
/*     */     public Object construct(String value)
/*     */     {
/*     */       try {
/*  46 */         if (this._ctor != null) {
/*  47 */           return this._ctor.newInstance(new Object[] { value });
/*     */         }
/*  49 */         if (this._factoryMethod != null)
/*  50 */           return this._factoryMethod.invoke(this._valueClass, new Object[] { value });
/*     */       }
/*     */       catch (Exception e) {
/*  53 */         ClassUtil.unwrapAndThrowAsIAE(e);
/*     */       }
/*  55 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.Creator
 * JD-Core Version:    0.6.0
 */