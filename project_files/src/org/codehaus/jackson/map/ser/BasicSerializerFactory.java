/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.Vector;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonSerializable;
/*     */ import org.codehaus.jackson.map.JsonSerializableWithType;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.SerializerFactory;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
/*     */ import org.codehaus.jackson.map.ext.OptionalHandlerFactory;
/*     */ import org.codehaus.jackson.map.introspect.Annotated;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*     */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.ser.impl.IndexedStringListSerializer;
/*     */ import org.codehaus.jackson.map.ser.impl.ObjectArraySerializer;
/*     */ import org.codehaus.jackson.map.ser.impl.StringCollectionSerializer;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.map.util.EnumValues;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ import org.codehaus.jackson.util.TokenBuffer;
/*     */ 
/*     */ public abstract class BasicSerializerFactory extends SerializerFactory
/*     */ {
/*  65 */   protected static final HashMap<String, JsonSerializer<?>> _concrete = new HashMap();
/*     */ 
/*  75 */   protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy = new HashMap();
/*     */ 
/*  79 */   static final JsonSerializer<?> MARKER_INDEXED_LIST = new SerializerMarker(null);
/*  80 */   static final JsonSerializer<?> MARKER_COLLECTION = new SerializerMarker(null);
/*  81 */   static final JsonSerializer<?> MARKER_OBJECT_ARRAY = new SerializerMarker(null);
/*  82 */   static final JsonSerializer<?> MARKER_STRING_ARRAY = new SerializerMarker(null);
/*  83 */   static final JsonSerializer<?> MARKER_OBJECT_MAP = new SerializerMarker(null);
/*     */ 
/* 182 */   protected OptionalHandlerFactory optionalHandlers = OptionalHandlerFactory.instance;
/*     */ 
/*     */   public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property)
/*     */   {
/* 217 */     BasicBeanDescription beanDesc = (BasicBeanDescription)config.introspect(type);
/* 218 */     JsonSerializer ser = findSerializerFromAnnotation(config, beanDesc.getClassInfo(), property);
/* 219 */     if (ser == null)
/*     */     {
/* 221 */       ser = findSerializerByLookup(type, config, beanDesc, property);
/* 222 */       if (ser == null)
/*     */       {
/* 226 */         ser = findSerializerByPrimaryType(type, config, beanDesc, property);
/* 227 */         if (ser == null)
/*     */         {
/* 229 */           ser = findSerializerByAddonType(config, type, beanDesc, property);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 234 */     JsonSerializer s2 = ser;
/* 235 */     return s2;
/*     */   }
/*     */ 
/*     */   public TypeSerializer createTypeSerializer(SerializationConfig config, JavaType baseType, BeanProperty property)
/*     */   {
/* 247 */     BasicBeanDescription bean = (BasicBeanDescription)config.introspectClassAnnotations(baseType.getRawClass());
/* 248 */     AnnotatedClass ac = bean.getClassInfo();
/* 249 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 250 */     TypeResolverBuilder b = ai.findTypeResolver(ac, baseType);
/*     */ 
/* 254 */     Collection subtypes = null;
/* 255 */     if (b == null)
/* 256 */       b = config.getDefaultTyper(baseType);
/*     */     else {
/* 258 */       subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, config, ai);
/*     */     }
/* 260 */     return b == null ? null : b.buildTypeSerializer(baseType, subtypes, property);
/*     */   }
/*     */ 
/*     */   public final JsonSerializer<?> getNullSerializer()
/*     */   {
/* 271 */     return NullSerializer.instance;
/*     */   }
/*     */ 
/*     */   public final JsonSerializer<?> findSerializerByLookup(JavaType type, SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 288 */     String clsName = type.getRawClass().getName();
/* 289 */     JsonSerializer ser = (JsonSerializer)_concrete.get(clsName);
/* 290 */     if (ser == null) {
/* 291 */       Class serClass = (Class)_concreteLazy.get(clsName);
/* 292 */       if (serClass != null) {
/*     */         try {
/* 294 */           ser = (JsonSerializer)serClass.newInstance();
/*     */         } catch (Exception e) {
/* 296 */           throw new IllegalStateException("Failed to instantiate standard serializer (of type " + serClass.getName() + "): " + e.getMessage(), e);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 307 */     if (ser != null) {
/* 308 */       if (ser == MARKER_OBJECT_MAP) {
/* 309 */         return buildMapSerializer(config, type, beanDesc, property);
/*     */       }
/* 311 */       if (ser == MARKER_OBJECT_ARRAY) {
/* 312 */         return buildObjectArraySerializer(config, type, beanDesc, property);
/*     */       }
/* 314 */       if (ser == MARKER_STRING_ARRAY) {
/* 315 */         return new ArraySerializers.StringArraySerializer(property);
/*     */       }
/* 317 */       if (ser == MARKER_INDEXED_LIST)
/*     */       {
/* 319 */         JavaType elemType = type.getContentType();
/* 320 */         if (elemType.getRawClass() == String.class) {
/* 321 */           return new IndexedStringListSerializer(property);
/*     */         }
/* 323 */         return buildIndexedListSerializer(config, type, beanDesc, property);
/*     */       }
/* 325 */       if (ser == MARKER_COLLECTION)
/*     */       {
/* 327 */         JavaType elemType = type.getContentType();
/* 328 */         if (elemType.getRawClass() == String.class) {
/* 329 */           return new StringCollectionSerializer(property);
/*     */         }
/* 331 */         return buildCollectionSerializer(config, type, beanDesc, property);
/*     */       }
/*     */     }
/*     */     else {
/* 335 */       ser = this.optionalHandlers.findSerializer(config, type, beanDesc, property);
/*     */     }
/* 337 */     return ser;
/*     */   }
/*     */ 
/*     */   public final JsonSerializer<?> findSerializerByPrimaryType(JavaType type, SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 350 */     Class cls = type.getRawClass();
/*     */ 
/* 365 */     if (JsonSerializable.class.isAssignableFrom(cls)) {
/* 366 */       if (JsonSerializableWithType.class.isAssignableFrom(cls)) {
/* 367 */         return StdSerializers.SerializableWithTypeSerializer.instance;
/*     */       }
/* 369 */       return StdSerializers.SerializableSerializer.instance;
/*     */     }
/* 371 */     if (Map.class.isAssignableFrom(cls)) {
/* 372 */       if (EnumMap.class.isAssignableFrom(cls)) {
/* 373 */         return buildEnumMapSerializer(config, type, beanDesc, property);
/*     */       }
/* 375 */       return buildMapSerializer(config, type, beanDesc, property);
/*     */     }
/* 377 */     if ([Ljava.lang.Object.class.isAssignableFrom(cls)) {
/* 378 */       return buildObjectArraySerializer(config, type, beanDesc, property);
/*     */     }
/* 380 */     if (List.class.isAssignableFrom(cls)) {
/* 381 */       if ((cls == List.class) || (cls == AbstractList.class) || (RandomAccess.class.isAssignableFrom(cls))) {
/* 382 */         return buildIndexedListSerializer(config, type, beanDesc, property);
/*     */       }
/* 384 */       return buildCollectionSerializer(config, type, beanDesc, property);
/*     */     }
/*     */ 
/* 387 */     AnnotatedMethod valueMethod = beanDesc.findJsonValueMethod();
/* 388 */     if (valueMethod != null) {
/* 389 */       JsonSerializer ser = findSerializerFromAnnotation(config, valueMethod, property);
/* 390 */       return new JsonValueSerializer(valueMethod.getAnnotated(), ser, property);
/*     */     }
/*     */ 
/* 393 */     if (Number.class.isAssignableFrom(cls)) {
/* 394 */       return StdSerializers.NumberSerializer.instance;
/*     */     }
/* 396 */     if (Enum.class.isAssignableFrom(cls))
/*     */     {
/* 398 */       Class enumClass = cls;
/* 399 */       return EnumSerializer.construct(enumClass, config, beanDesc);
/*     */     }
/* 401 */     if (Calendar.class.isAssignableFrom(cls)) {
/* 402 */       return StdSerializers.CalendarSerializer.instance;
/*     */     }
/* 404 */     if (java.util.Date.class.isAssignableFrom(cls)) {
/* 405 */       return StdSerializers.UtilDateSerializer.instance;
/*     */     }
/* 407 */     if (Collection.class.isAssignableFrom(cls)) {
/* 408 */       if (EnumSet.class.isAssignableFrom(cls)) {
/* 409 */         return buildEnumSetSerializer(config, type, beanDesc, property);
/*     */       }
/* 411 */       return buildCollectionSerializer(config, type, beanDesc, property);
/*     */     }
/* 413 */     return null;
/*     */   }
/*     */ 
/*     */   public final JsonSerializer<?> findSerializerByAddonType(SerializationConfig config, JavaType javaType, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 427 */     Class type = javaType.getRawClass();
/*     */ 
/* 430 */     if (Iterator.class.isAssignableFrom(type)) {
/* 431 */       return buildIteratorSerializer(config, javaType, beanDesc, property);
/*     */     }
/* 433 */     if (Iterable.class.isAssignableFrom(type)) {
/* 434 */       return buildIterableSerializer(config, javaType, beanDesc, property);
/*     */     }
/* 436 */     if (CharSequence.class.isAssignableFrom(type)) {
/* 437 */       return ToStringSerializer.instance;
/*     */     }
/* 439 */     return null;
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<Object> findSerializerFromAnnotation(SerializationConfig config, Annotated a, BeanProperty property)
/*     */   {
/* 452 */     Object serDef = config.getAnnotationIntrospector().findSerializer(a, property);
/* 453 */     if (serDef != null) {
/* 454 */       if ((serDef instanceof JsonSerializer)) {
/* 455 */         return (JsonSerializer)serDef;
/*     */       }
/*     */ 
/* 460 */       if (!(serDef instanceof Class)) {
/* 461 */         throw new IllegalStateException("AnnotationIntrospector returned value of type " + serDef.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
/*     */       }
/* 463 */       Class cls = (Class)serDef;
/* 464 */       if (!JsonSerializer.class.isAssignableFrom(cls)) {
/* 465 */         throw new IllegalStateException("AnnotationIntrospector returned Class " + cls.getName() + "; expected Class<JsonSerializer>");
/*     */       }
/* 467 */       return (JsonSerializer)ClassUtil.createInstance(cls, config.isEnabled(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
/*     */     }
/* 469 */     return null;
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> buildMapSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 479 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 480 */     JavaType valueType = type.getContentType();
/* 481 */     TypeSerializer vts = createTypeSerializer(config, valueType, property);
/* 482 */     boolean staticTyping = usesStaticTyping(config, beanDesc, vts);
/* 483 */     return MapSerializer.construct(intr.findPropertiesToIgnore(beanDesc.getClassInfo()), type, staticTyping, vts, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> buildEnumMapSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 490 */     JavaType keyType = type.getKeyType();
/* 491 */     JavaType valueType = type.getContentType();
/*     */ 
/* 493 */     EnumValues enums = null;
/* 494 */     if (keyType.isEnumType())
/*     */     {
/* 496 */       Class enumClass = keyType.getRawClass();
/* 497 */       enums = EnumValues.construct(enumClass, config.getAnnotationIntrospector());
/*     */     }
/* 499 */     TypeSerializer vts = createTypeSerializer(config, valueType, property);
/* 500 */     return new EnumMapSerializer(valueType, usesStaticTyping(config, beanDesc, vts), enums, vts, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> buildObjectArraySerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 511 */     JavaType valueType = type.getContentType();
/* 512 */     TypeSerializer vts = createTypeSerializer(config, valueType, property);
/* 513 */     return new ObjectArraySerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> buildIndexedListSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 520 */     JavaType valueType = type.getContentType();
/* 521 */     TypeSerializer vts = createTypeSerializer(config, valueType, property);
/* 522 */     return ContainerSerializers.indexedListSerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> buildCollectionSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 529 */     JavaType valueType = type.getContentType();
/* 530 */     TypeSerializer vts = createTypeSerializer(config, valueType, property);
/* 531 */     return ContainerSerializers.collectionSerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 539 */     JavaType valueType = type.containedType(0);
/* 540 */     if (valueType == null) {
/* 541 */       valueType = TypeFactory.type(Object.class);
/*     */     }
/* 543 */     TypeSerializer vts = createTypeSerializer(config, valueType, property);
/* 544 */     return ContainerSerializers.iteratorSerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> buildIterableSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 552 */     JavaType valueType = type.containedType(0);
/* 553 */     if (valueType == null) {
/* 554 */       valueType = TypeFactory.type(Object.class);
/*     */     }
/* 556 */     TypeSerializer vts = createTypeSerializer(config, valueType, property);
/* 557 */     return ContainerSerializers.iterableSerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<?> buildEnumSetSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 565 */     JavaType enumType = type.getContentType();
/*     */ 
/* 567 */     if (!enumType.isEnumType()) {
/* 568 */       enumType = null;
/*     */     }
/* 570 */     return ContainerSerializers.enumSetSerializer(enumType, property);
/*     */   }
/*     */ 
/*     */   protected boolean usesStaticTyping(SerializationConfig config, BasicBeanDescription beanDesc, TypeSerializer typeSer)
/*     */   {
/* 586 */     if (typeSer != null) {
/* 587 */       return false;
/*     */     }
/* 589 */     JsonSerialize.Typing t = config.getAnnotationIntrospector().findSerializationTyping(beanDesc.getClassInfo());
/* 590 */     if (t != null) {
/* 591 */       return t == JsonSerialize.Typing.STATIC;
/*     */     }
/* 593 */     return config.isEnabled(SerializationConfig.Feature.USE_STATIC_TYPING);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  89 */     _concrete.put(String.class.getName(), new StdSerializers.StringSerializer());
/*  90 */     ToStringSerializer sls = ToStringSerializer.instance;
/*  91 */     _concrete.put(StringBuffer.class.getName(), sls);
/*  92 */     _concrete.put(StringBuilder.class.getName(), sls);
/*  93 */     _concrete.put(Character.class.getName(), sls);
/*  94 */     _concrete.put(Character.TYPE.getName(), sls);
/*     */ 
/*  97 */     _concrete.put(Boolean.TYPE.getName(), new StdSerializers.BooleanSerializer(true));
/*  98 */     _concrete.put(Boolean.class.getName(), new StdSerializers.BooleanSerializer(false));
/*  99 */     JsonSerializer intS = new StdSerializers.IntegerSerializer();
/* 100 */     _concrete.put(Integer.class.getName(), intS);
/* 101 */     _concrete.put(Integer.TYPE.getName(), intS);
/* 102 */     _concrete.put(Long.class.getName(), StdSerializers.LongSerializer.instance);
/* 103 */     _concrete.put(Long.TYPE.getName(), StdSerializers.LongSerializer.instance);
/* 104 */     _concrete.put(Byte.class.getName(), StdSerializers.IntLikeSerializer.instance);
/* 105 */     _concrete.put(Byte.TYPE.getName(), StdSerializers.IntLikeSerializer.instance);
/* 106 */     _concrete.put(Short.class.getName(), StdSerializers.IntLikeSerializer.instance);
/* 107 */     _concrete.put(Short.TYPE.getName(), StdSerializers.IntLikeSerializer.instance);
/*     */ 
/* 110 */     _concrete.put(Float.class.getName(), StdSerializers.FloatSerializer.instance);
/* 111 */     _concrete.put(Float.TYPE.getName(), StdSerializers.FloatSerializer.instance);
/* 112 */     _concrete.put(Double.class.getName(), StdSerializers.DoubleSerializer.instance);
/* 113 */     _concrete.put(Double.TYPE.getName(), StdSerializers.DoubleSerializer.instance);
/*     */ 
/* 116 */     JsonSerializer ns = new StdSerializers.NumberSerializer();
/* 117 */     _concrete.put(BigInteger.class.getName(), ns);
/* 118 */     _concrete.put(BigDecimal.class.getName(), ns);
/*     */ 
/* 123 */     _concrete.put(Calendar.class.getName(), StdSerializers.CalendarSerializer.instance);
/* 124 */     _concrete.put(java.util.Date.class.getName(), StdSerializers.UtilDateSerializer.instance);
/* 125 */     _concrete.put(java.sql.Date.class.getName(), new StdSerializers.SqlDateSerializer());
/* 126 */     _concrete.put(Time.class.getName(), new StdSerializers.SqlTimeSerializer());
/*     */ 
/* 128 */     _concrete.put(Timestamp.class.getName(), StdSerializers.UtilDateSerializer.instance);
/*     */ 
/* 131 */     _concrete.put([Z.class.getName(), new ArraySerializers.BooleanArraySerializer());
/* 132 */     _concrete.put([B.class.getName(), new ArraySerializers.ByteArraySerializer());
/* 133 */     _concrete.put([C.class.getName(), new ArraySerializers.CharArraySerializer());
/* 134 */     _concrete.put([S.class.getName(), new ArraySerializers.ShortArraySerializer());
/* 135 */     _concrete.put([I.class.getName(), new ArraySerializers.IntArraySerializer());
/* 136 */     _concrete.put([J.class.getName(), new ArraySerializers.LongArraySerializer());
/* 137 */     _concrete.put([F.class.getName(), new ArraySerializers.FloatArraySerializer());
/* 138 */     _concrete.put([D.class.getName(), new ArraySerializers.DoubleArraySerializer());
/*     */ 
/* 140 */     _concrete.put([Ljava.lang.Object.class.getName(), MARKER_OBJECT_ARRAY);
/* 141 */     _concrete.put([Ljava.lang.String.class.getName(), MARKER_STRING_ARRAY);
/*     */ 
/* 143 */     _concrete.put(ArrayList.class.getName(), MARKER_INDEXED_LIST);
/* 144 */     _concrete.put(Vector.class.getName(), MARKER_INDEXED_LIST);
/* 145 */     _concrete.put(LinkedList.class.getName(), MARKER_COLLECTION);
/*     */ 
/* 149 */     _concrete.put(HashMap.class.getName(), MARKER_OBJECT_MAP);
/* 150 */     _concrete.put(Hashtable.class.getName(), MARKER_OBJECT_MAP);
/* 151 */     _concrete.put(LinkedHashMap.class.getName(), MARKER_OBJECT_MAP);
/* 152 */     _concrete.put(TreeMap.class.getName(), MARKER_OBJECT_MAP);
/* 153 */     _concrete.put(Properties.class.getName(), MARKER_OBJECT_MAP);
/*     */ 
/* 155 */     _concrete.put(HashSet.class.getName(), MARKER_COLLECTION);
/* 156 */     _concrete.put(LinkedHashSet.class.getName(), MARKER_COLLECTION);
/* 157 */     _concrete.put(TreeSet.class.getName(), MARKER_COLLECTION);
/*     */ 
/* 160 */     for (Map.Entry en : new JdkSerializers().provide()) {
/* 161 */       Object value = en.getValue();
/* 162 */       if ((value instanceof JsonSerializer)) {
/* 163 */         _concrete.put(((Class)en.getKey()).getName(), (JsonSerializer)value);
/* 164 */       } else if ((value instanceof Class))
/*     */       {
/* 166 */         Class cls = (Class)value;
/* 167 */         _concreteLazy.put(((Class)en.getKey()).getName(), cls);
/*     */       } else {
/* 169 */         throw new IllegalStateException("Internal error: unrecognized value of type " + en.getClass().getName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 175 */     _concreteLazy.put(TokenBuffer.class.getName(), StdSerializers.TokenBufferSerializer.class);
/*     */   }
/*     */ 
/*     */   private static final class SerializerMarker extends JsonSerializer<Object>
/*     */   {
/*     */     public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.BasicSerializerFactory
 * JD-Core Version:    0.6.0
 */