/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedField;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedParameter;
/*     */ import org.codehaus.jackson.map.util.Annotations;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ import org.codehaus.jackson.util.InternCache;
/*     */ 
/*     */ public abstract class SettableBeanProperty
/*     */   implements BeanProperty
/*     */ {
/*     */   protected final String _propName;
/*     */   protected final JavaType _type;
/*     */   protected final Annotations _contextAnnotations;
/*     */   protected JsonDeserializer<Object> _valueDeserializer;
/*     */   protected TypeDeserializer _valueTypeDeserializer;
/*     */   protected NullProvider _nullProvider;
/*     */   protected String _managedReferenceName;
/*  81 */   protected int _propertyIndex = -1;
/*     */ 
/*     */   protected SettableBeanProperty(String propName, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations)
/*     */   {
/*  96 */     if ((propName == null) || (propName.length() == 0))
/*  97 */       this._propName = "";
/*     */     else {
/*  99 */       this._propName = InternCache.instance.intern(propName);
/*     */     }
/* 101 */     this._type = type;
/* 102 */     this._contextAnnotations = contextAnnotations;
/* 103 */     this._valueTypeDeserializer = typeDeser;
/*     */   }
/*     */ 
/*     */   public void setValueDeserializer(JsonDeserializer<Object> deser)
/*     */   {
/* 108 */     if (this._valueDeserializer != null) {
/* 109 */       throw new IllegalStateException("Already had assigned deserializer for property '" + getName() + "' (class " + getDeclaringClass().getName() + ")");
/*     */     }
/* 111 */     this._valueDeserializer = deser;
/* 112 */     Object nvl = this._valueDeserializer.getNullValue();
/* 113 */     this._nullProvider = (nvl == null ? null : new NullProvider(this._type, nvl));
/*     */   }
/*     */ 
/*     */   public void setManagedReferenceName(String n) {
/* 117 */     this._managedReferenceName = n;
/*     */   }
/*     */ 
/*     */   public void assignIndex(int index)
/*     */   {
/* 126 */     if (this._propertyIndex != -1) {
/* 127 */       throw new IllegalStateException("Property '" + getName() + "' already had index (" + this._propertyIndex + "), trying to assign " + index);
/*     */     }
/* 129 */     this._propertyIndex = index;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 138 */     return this._propName;
/*     */   }
/* 140 */   public JavaType getType() { return this._type; } 
/*     */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*     */ 
/*     */   public abstract AnnotatedMember getMember();
/*     */ 
/*     */   public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
/* 148 */     return this._contextAnnotations.get(acls);
/*     */   }
/*     */ 
/*     */   protected final Class<?> getDeclaringClass()
/*     */   {
/* 158 */     return getMember().getDeclaringClass();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public String getPropertyName()
/*     */   {
/* 165 */     return this._propName;
/*     */   }
/* 167 */   public String getManagedReferenceName() { return this._managedReferenceName; } 
/*     */   public boolean hasValueDeserializer() {
/* 169 */     return this._valueDeserializer != null;
/*     */   }
/*     */ 
/*     */   public int getCreatorIndex()
/*     */   {
/* 179 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getProperytIndex()
/*     */   {
/* 190 */     return this._propertyIndex;
/*     */   }
/*     */ 
/*     */   public abstract void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public abstract void set(Object paramObject1, Object paramObject2)
/*     */     throws IOException;
/*     */ 
/*     */   public final Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 223 */     JsonToken t = jp.getCurrentToken();
/* 224 */     if (t == JsonToken.VALUE_NULL) {
/* 225 */       return this._nullProvider == null ? null : this._nullProvider.nullValue(ctxt);
/*     */     }
/* 227 */     if (this._valueTypeDeserializer != null) {
/* 228 */       return this._valueDeserializer.deserializeWithType(jp, ctxt, this._valueTypeDeserializer);
/*     */     }
/* 230 */     return this._valueDeserializer.deserialize(jp, ctxt);
/*     */   }
/*     */ 
/*     */   protected void _throwAsIOE(Exception e, Object value)
/*     */     throws IOException
/*     */   {
/* 246 */     if ((e instanceof IllegalArgumentException)) {
/* 247 */       String actType = value == null ? "[NULL]" : value.getClass().getName();
/* 248 */       StringBuilder msg = new StringBuilder("Problem deserializing property '").append(getPropertyName());
/* 249 */       msg.append("' (expected type: ").append(getType());
/* 250 */       msg.append("; actual type: ").append(actType).append(")");
/* 251 */       String origMsg = e.getMessage();
/* 252 */       if (origMsg != null)
/* 253 */         msg.append(", problem: ").append(origMsg);
/*     */       else {
/* 255 */         msg.append(" (no error message provided)");
/*     */       }
/* 257 */       throw new JsonMappingException(msg.toString(), null, e);
/*     */     }
/* 259 */     _throwAsIOE(e);
/*     */   }
/*     */ 
/*     */   protected IOException _throwAsIOE(Exception e)
/*     */     throws IOException
/*     */   {
/* 265 */     if ((e instanceof IOException)) {
/* 266 */       throw ((IOException)e);
/*     */     }
/* 268 */     if ((e instanceof RuntimeException)) {
/* 269 */       throw ((RuntimeException)e);
/*     */     }
/*     */ 
/* 272 */     Throwable th = e;
/* 273 */     while (th.getCause() != null) {
/* 274 */       th = th.getCause();
/*     */     }
/* 276 */     throw new JsonMappingException(th.getMessage(), null, th);
/*     */   }
/*     */   public String toString() {
/* 279 */     return "[property '" + getName() + "']";
/*     */   }
/*     */ 
/*     */   protected static final class NullProvider
/*     */   {
/*     */     private final Object _nullValue;
/*     */     private final boolean _isPrimitive;
/*     */     private final Class<?> _rawType;
/*     */ 
/*     */     protected NullProvider(JavaType type, Object nullValue)
/*     */     {
/* 679 */       this._nullValue = nullValue;
/*     */ 
/* 681 */       this._isPrimitive = type.isPrimitive();
/* 682 */       this._rawType = type.getRawClass();
/*     */     }
/*     */ 
/*     */     public Object nullValue(DeserializationContext ctxt) throws JsonProcessingException
/*     */     {
/* 687 */       if ((this._isPrimitive) && (ctxt.isEnabled(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES))) {
/* 688 */         throw ctxt.mappingException("Can not map JSON null into type " + this._rawType.getName() + " (set DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)");
/*     */       }
/*     */ 
/* 691 */       return this._nullValue;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class ManagedReferenceProperty extends SettableBeanProperty
/*     */   {
/*     */     protected final String _referenceName;
/*     */     protected final boolean _isContainer;
/*     */     protected final SettableBeanProperty _managedProperty;
/*     */     protected final SettableBeanProperty _backProperty;
/*     */ 
/*     */     public ManagedReferenceProperty(String refName, SettableBeanProperty forward, SettableBeanProperty backward, Annotations contextAnnotations, boolean isContainer)
/*     */     {
/* 591 */       super(forward.getType(), forward._valueTypeDeserializer, contextAnnotations);
/*     */ 
/* 593 */       this._referenceName = refName;
/* 594 */       this._managedProperty = forward;
/* 595 */       this._backProperty = backward;
/* 596 */       this._isContainer = isContainer;
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */     {
/* 607 */       return this._managedProperty.getAnnotation(acls);
/*     */     }
/*     */     public AnnotatedMember getMember() {
/* 610 */       return this._managedProperty.getMember();
/*     */     }
/*     */ 
/*     */     public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 623 */       set(instance, this._managedProperty.deserialize(jp, ctxt));
/*     */     }
/*     */ 
/*     */     public final void set(Object instance, Object value)
/*     */       throws IOException
/*     */     {
/* 630 */       this._managedProperty.set(instance, value);
/*     */ 
/* 634 */       if (value != null)
/* 635 */         if (this._isContainer) {
/* 636 */           if ((value instanceof Object[])) {
/* 637 */             for (Object ob : (Object[])(Object[])value)
/* 638 */               if (ob != null)
/* 639 */                 this._backProperty.set(ob, instance);
/*     */           }
/*     */           else
/*     */           {
/*     */             Iterator i$;
/* 642 */             if ((value instanceof Collection)) {
/* 643 */               for (i$ = ((Collection)value).iterator(); i$.hasNext(); ) { Object ob = i$.next();
/* 644 */                 if (ob != null)
/* 645 */                   this._backProperty.set(ob, instance);
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/*     */               Iterator i$;
/* 648 */               if ((value instanceof Map)) {
/* 649 */                 for (i$ = ((Map)value).values().iterator(); i$.hasNext(); ) { Object ob = i$.next();
/* 650 */                   if (ob != null)
/* 651 */                     this._backProperty.set(ob, instance);
/*     */                 }
/*     */               }
/*     */               else
/* 655 */                 throw new IllegalStateException("Unsupported container type (" + value.getClass().getName() + ") when resolving reference '" + this._referenceName + "'");
/*     */             }
/*     */           }
/*     */         }
/* 659 */         else this._backProperty.set(value, instance);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class CreatorProperty extends SettableBeanProperty
/*     */   {
/*     */     protected final AnnotatedParameter _annotated;
/*     */     protected final int _index;
/*     */ 
/*     */     public CreatorProperty(String name, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param, int index)
/*     */     {
/* 510 */       super(type, typeDeser, contextAnnotations);
/* 511 */       this._annotated = param;
/* 512 */       this._index = index;
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */     {
/* 523 */       return this._annotated.getAnnotation(acls);
/*     */     }
/*     */     public AnnotatedMember getMember() {
/* 526 */       return this._annotated;
/*     */     }
/*     */ 
/*     */     public int getCreatorIndex()
/*     */     {
/* 543 */       return this._index;
/*     */     }
/*     */ 
/*     */     public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 550 */       set(instance, deserialize(jp, ctxt));
/*     */     }
/*     */ 
/*     */     public void set(Object instance, Object value)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class FieldProperty extends SettableBeanProperty
/*     */   {
/*     */     protected final AnnotatedField _annotated;
/*     */     protected final Field _field;
/*     */ 
/*     */     public FieldProperty(String name, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedField field)
/*     */     {
/* 448 */       super(type, typeDeser, contextAnnotations);
/* 449 */       this._annotated = field;
/* 450 */       this._field = field.getAnnotated();
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */     {
/* 461 */       return this._annotated.getAnnotation(acls);
/*     */     }
/*     */     public AnnotatedMember getMember() {
/* 464 */       return this._annotated;
/*     */     }
/*     */ 
/*     */     public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 477 */       set(instance, deserialize(jp, ctxt));
/*     */     }
/*     */ 
/*     */     public final void set(Object instance, Object value)
/*     */       throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 485 */         this._field.set(instance, value);
/*     */       } catch (Exception e) {
/* 487 */         _throwAsIOE(e, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SetterlessProperty extends SettableBeanProperty
/*     */   {
/*     */     protected final AnnotatedMethod _annotated;
/*     */     protected final Method _getter;
/*     */ 
/*     */     public SetterlessProperty(String name, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedMethod method)
/*     */     {
/* 367 */       super(type, typeDeser, contextAnnotations);
/* 368 */       this._annotated = method;
/* 369 */       this._getter = method.getAnnotated();
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */     {
/* 380 */       return this._annotated.getAnnotation(acls);
/*     */     }
/*     */     public AnnotatedMember getMember() {
/* 383 */       return this._annotated;
/*     */     }
/*     */ 
/*     */     public final void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 396 */       JsonToken t = jp.getCurrentToken();
/* 397 */       if (t == JsonToken.VALUE_NULL)
/*     */       {
/* 401 */         return;
/*     */       }
/*     */       Object toModify;
/*     */       try
/*     */       {
/* 407 */         toModify = this._getter.invoke(instance, new Object[0]);
/*     */       } catch (Exception e) {
/* 409 */         _throwAsIOE(e);
/* 410 */         return;
/*     */       }
/*     */ 
/* 417 */       if (toModify == null) {
/* 418 */         throw new JsonMappingException("Problem deserializing 'setterless' property '" + getName() + "': get method returned null");
/*     */       }
/* 420 */       this._valueDeserializer.deserialize(jp, ctxt, toModify);
/*     */     }
/*     */ 
/*     */     public final void set(Object instance, Object value)
/*     */       throws IOException
/*     */     {
/* 427 */       throw new UnsupportedOperationException("Should never call 'set' on setterless property");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class MethodProperty extends SettableBeanProperty
/*     */   {
/*     */     protected final AnnotatedMethod _annotated;
/*     */     protected final Method _setter;
/*     */ 
/*     */     public MethodProperty(String name, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedMethod method)
/*     */     {
/* 305 */       super(type, typeDeser, contextAnnotations);
/* 306 */       this._annotated = method;
/* 307 */       this._setter = method.getAnnotated();
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */     {
/* 318 */       return this._annotated.getAnnotation(acls);
/*     */     }
/*     */     public AnnotatedMember getMember() {
/* 321 */       return this._annotated;
/*     */     }
/*     */ 
/*     */     public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 334 */       set(instance, deserialize(jp, ctxt));
/*     */     }
/*     */ 
/*     */     public final void set(Object instance, Object value)
/*     */       throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 342 */         this._setter.invoke(instance, new Object[] { value });
/*     */       } catch (Exception e) {
/* 344 */         _throwAsIOE(e, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.SettableBeanProperty
 * JD-Core Version:    0.6.0
 */