/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.io.SerializedString;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*     */ import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
/*     */ import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import org.codehaus.jackson.map.util.Annotations;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class BeanPropertyWriter
/*     */   implements BeanProperty
/*     */ {
/*     */   protected final AnnotatedMember _member;
/*     */   protected final Annotations _contextAnnotations;
/*     */   protected final JavaType _declaredType;
/*     */   protected final Method _accessorMethod;
/*     */   protected final Field _field;
/*     */   protected HashMap<Object, Object> _internalSettings;
/*     */   protected final SerializedString _name;
/*     */   protected final JavaType _cfgSerializationType;
/*     */   protected final JsonSerializer<Object> _serializer;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */   protected final boolean _suppressNulls;
/*     */   protected final Object _suppressableValue;
/*     */   protected Class<?>[] _includeInViews;
/*     */   protected TypeSerializer _typeSerializer;
/*     */   protected JavaType _nonTrivialBaseType;
/*     */ 
/*     */   public BeanPropertyWriter(AnnotatedMember member, Annotations contextAnnotations, String name, JavaType declaredType, JsonSerializer<Object> ser, TypeSerializer typeSer, JavaType serType, Method m, Field f, boolean suppressNulls, Object suppressableValue)
/*     */   {
/* 166 */     this(member, contextAnnotations, new SerializedString(name), declaredType, ser, typeSer, serType, m, f, suppressNulls, suppressableValue);
/*     */   }
/*     */ 
/*     */   public BeanPropertyWriter(AnnotatedMember member, Annotations contextAnnotations, SerializedString name, JavaType declaredType, JsonSerializer<Object> ser, TypeSerializer typeSer, JavaType serType, Method m, Field f, boolean suppressNulls, Object suppressableValue)
/*     */   {
/* 176 */     this._member = member;
/* 177 */     this._contextAnnotations = contextAnnotations;
/* 178 */     this._name = name;
/* 179 */     this._declaredType = declaredType;
/* 180 */     this._serializer = ser;
/* 181 */     this._dynamicSerializers = (ser == null ? PropertySerializerMap.emptyMap() : null);
/* 182 */     this._typeSerializer = typeSer;
/* 183 */     this._cfgSerializationType = serType;
/* 184 */     this._accessorMethod = m;
/* 185 */     this._field = f;
/* 186 */     this._suppressNulls = suppressNulls;
/* 187 */     this._suppressableValue = suppressableValue;
/*     */   }
/*     */ 
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base)
/*     */   {
/* 195 */     this(base, base._serializer);
/*     */   }
/*     */ 
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base, JsonSerializer<Object> ser)
/*     */   {
/* 203 */     this._serializer = ser;
/*     */ 
/* 205 */     this._member = base._member;
/* 206 */     this._contextAnnotations = base._contextAnnotations;
/* 207 */     this._name = base._name;
/* 208 */     this._declaredType = base._declaredType;
/* 209 */     this._dynamicSerializers = base._dynamicSerializers;
/* 210 */     this._typeSerializer = base._typeSerializer;
/* 211 */     this._cfgSerializationType = base._cfgSerializationType;
/* 212 */     this._accessorMethod = base._accessorMethod;
/* 213 */     this._field = base._field;
/* 214 */     this._suppressNulls = base._suppressNulls;
/* 215 */     this._suppressableValue = base._suppressableValue;
/*     */ 
/* 217 */     if (base._internalSettings != null)
/* 218 */       this._internalSettings = new HashMap(base._internalSettings);
/*     */   }
/*     */ 
/*     */   public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser)
/*     */   {
/* 230 */     if (getClass() != BeanPropertyWriter.class) {
/* 231 */       throw new IllegalStateException("BeanPropertyWriter sub-class does not override 'withSerializer()'; needs to!");
/*     */     }
/* 233 */     return new BeanPropertyWriter(this, ser);
/*     */   }
/*     */ 
/*     */   public void setViews(Class<?>[] views)
/*     */   {
/* 244 */     this._includeInViews = views;
/*     */   }
/*     */ 
/*     */   public void setNonTrivialBaseType(JavaType t)
/*     */   {
/* 254 */     this._nonTrivialBaseType = t;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 264 */     return this._name.getValue();
/*     */   }
/*     */ 
/*     */   public JavaType getType() {
/* 268 */     return this._declaredType;
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls) {
/* 272 */     return this._member.getAnnotation(acls);
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
/* 276 */     return this._contextAnnotations.get(acls);
/*     */   }
/*     */ 
/*     */   public AnnotatedMember getMember() {
/* 280 */     return this._member;
/*     */   }
/*     */ 
/*     */   public Object getInternalSetting(Object key)
/*     */   {
/* 299 */     if (this._internalSettings == null) {
/* 300 */       return null;
/*     */     }
/* 302 */     return this._internalSettings.get(key);
/*     */   }
/*     */ 
/*     */   public Object setInternalSetting(Object key, Object value)
/*     */   {
/* 314 */     if (this._internalSettings == null) {
/* 315 */       this._internalSettings = new HashMap();
/*     */     }
/* 317 */     return this._internalSettings.put(key, value);
/*     */   }
/*     */ 
/*     */   public Object removeInternalSetting(Object key)
/*     */   {
/* 329 */     Object removed = null;
/* 330 */     if (this._internalSettings != null) {
/* 331 */       removed = this._internalSettings.remove(key);
/*     */ 
/* 333 */       if (this._internalSettings.size() == 0) {
/* 334 */         this._internalSettings = null;
/*     */       }
/*     */     }
/* 337 */     return removed;
/*     */   }
/*     */ 
/*     */   public SerializedString getSerializedName()
/*     */   {
/* 346 */     return this._name;
/*     */   }
/* 348 */   public boolean hasSerializer() { return this._serializer != null; }
/*     */ 
/*     */   protected JsonSerializer<Object> getSerializer()
/*     */   {
/* 352 */     return this._serializer;
/*     */   }
/*     */ 
/*     */   public JavaType getSerializationType() {
/* 356 */     return this._cfgSerializationType;
/*     */   }
/*     */ 
/*     */   public Class<?> getRawSerializationType() {
/* 360 */     return this._cfgSerializationType == null ? null : this._cfgSerializationType.getRawClass();
/*     */   }
/*     */ 
/*     */   public Class<?> getPropertyType()
/*     */   {
/* 365 */     if (this._accessorMethod != null) {
/* 366 */       return this._accessorMethod.getReturnType();
/*     */     }
/* 368 */     return this._field.getType();
/*     */   }
/*     */ 
/*     */   public Type getGenericPropertyType()
/*     */   {
/* 378 */     if (this._accessorMethod != null) {
/* 379 */       return this._accessorMethod.getGenericReturnType();
/*     */     }
/* 381 */     return this._field.getGenericType();
/*     */   }
/*     */   public Class<?>[] getViews() {
/* 384 */     return this._includeInViews;
/*     */   }
/*     */ 
/*     */   public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 400 */     Object value = get(bean);
/*     */ 
/* 402 */     if (value == null) {
/* 403 */       if (!this._suppressNulls) {
/* 404 */         jgen.writeFieldName(this._name);
/* 405 */         prov.defaultSerializeNull(jgen);
/*     */       }
/* 407 */       return;
/*     */     }
/*     */ 
/* 410 */     if (value == bean) {
/* 411 */       _reportSelfReference(bean);
/*     */     }
/* 413 */     if ((this._suppressableValue != null) && (this._suppressableValue.equals(value))) {
/* 414 */       return;
/*     */     }
/*     */ 
/* 417 */     JsonSerializer ser = this._serializer;
/* 418 */     if (ser == null) {
/* 419 */       Class cls = value.getClass();
/* 420 */       PropertySerializerMap map = this._dynamicSerializers;
/* 421 */       ser = map.serializerFor(cls);
/* 422 */       if (ser == null) {
/* 423 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     }
/* 426 */     jgen.writeFieldName(this._name);
/* 427 */     if (this._typeSerializer == null)
/* 428 */       ser.serialize(value, jgen, prov);
/*     */     else
/* 430 */       ser.serializeWithType(value, jgen, prov, this._typeSerializer);
/*     */   }
/*     */ 
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/* 441 */     if (this._nonTrivialBaseType != null) {
/* 442 */       JavaType t = this._nonTrivialBaseType.forcedNarrowBy(type);
/* 443 */       result = map.findAndAddSerializer(t, provider, this);
/*     */     } else {
/* 445 */       result = map.findAndAddSerializer(type, provider, this);
/*     */     }
/*     */ 
/* 448 */     if (map != result.map) {
/* 449 */       this._dynamicSerializers = result.map;
/*     */     }
/* 451 */     return result.serializer;
/*     */   }
/*     */ 
/*     */   public final Object get(Object bean)
/*     */     throws Exception
/*     */   {
/* 464 */     if (this._accessorMethod != null) {
/* 465 */       return this._accessorMethod.invoke(bean, new Object[0]);
/*     */     }
/* 467 */     return this._field.get(bean);
/*     */   }
/*     */ 
/*     */   protected void _reportSelfReference(Object bean)
/*     */     throws JsonMappingException
/*     */   {
/* 473 */     throw new JsonMappingException("Direct self-reference leading to cycle");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 479 */     StringBuilder sb = new StringBuilder(40);
/* 480 */     sb.append("property '").append(getName()).append("' (");
/* 481 */     if (this._accessorMethod != null)
/* 482 */       sb.append("via method ").append(this._accessorMethod.getDeclaringClass().getName()).append("#").append(this._accessorMethod.getName());
/*     */     else {
/* 484 */       sb.append("field \"").append(this._field.getDeclaringClass().getName()).append("#").append(this._field.getName());
/*     */     }
/* 486 */     sb.append(')');
/* 487 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.BeanPropertyWriter
 * JD-Core Version:    0.6.0
 */