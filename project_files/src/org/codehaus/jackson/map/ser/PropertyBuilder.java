/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
/*     */ import org.codehaus.jackson.map.introspect.Annotated;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedField;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.Annotations;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class PropertyBuilder
/*     */ {
/*     */   protected final SerializationConfig _config;
/*     */   protected final BasicBeanDescription _beanDesc;
/*     */   protected final JsonSerialize.Inclusion _outputProps;
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   protected Object _defaultBean;
/*     */ 
/*     */   public PropertyBuilder(SerializationConfig config, BasicBeanDescription beanDesc)
/*     */   {
/*  44 */     this._config = config;
/*  45 */     this._beanDesc = beanDesc;
/*  46 */     this._outputProps = beanDesc.findSerializationInclusion(config.getSerializationInclusion());
/*  47 */     this._annotationIntrospector = this._config.getAnnotationIntrospector();
/*     */   }
/*     */ 
/*     */   public Annotations getClassAnnotations()
/*     */   {
/*  57 */     return this._beanDesc.getClassAnnotations();
/*     */   }
/*     */ 
/*     */   protected BeanPropertyWriter buildWriter(String name, JavaType declaredType, JsonSerializer<Object> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping)
/*     */   {
/*     */     Field f;
/*     */     Method m;
/*     */     Field f;
/*  72 */     if ((am instanceof AnnotatedField)) {
/*  73 */       Method m = null;
/*  74 */       f = ((AnnotatedField)am).getAnnotated();
/*     */     } else {
/*  76 */       m = ((AnnotatedMethod)am).getAnnotated();
/*  77 */       f = null;
/*     */     }
/*     */ 
/*  81 */     JavaType serializationType = findSerializationType(am, defaultUseStaticTyping);
/*     */ 
/*  84 */     if (contentTypeSer != null)
/*     */     {
/*  89 */       if (serializationType == null)
/*     */       {
/*  91 */         serializationType = declaredType;
/*     */       }
/*  93 */       JavaType ct = serializationType.getContentType();
/*     */ 
/*  98 */       if (ct == null) {
/*  99 */         throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + name + "' (of type " + this._beanDesc.getType() + "); serialization type " + serializationType + " has no content");
/*     */       }
/*     */ 
/* 102 */       serializationType = serializationType.withContentTypeHandler(contentTypeSer);
/* 103 */       ct = serializationType.getContentType();
/*     */     }
/* 105 */     Object suppValue = null;
/* 106 */     boolean suppressNulls = false;
/*     */ 
/* 108 */     JsonSerialize.Inclusion methodProps = this._annotationIntrospector.findSerializationInclusion(am, this._outputProps);
/*     */ 
/* 110 */     if (methodProps != null) {
/* 111 */       switch (1.$SwitchMap$org$codehaus$jackson$map$annotate$JsonSerialize$Inclusion[methodProps.ordinal()]) {
/*     */       case 1:
/* 113 */         suppValue = getDefaultValue(name, m, f);
/* 114 */         if (suppValue != null) break;
/* 115 */         suppressNulls = true; break;
/*     */       case 2:
/* 119 */         suppressNulls = true;
/*     */       }
/*     */     }
/*     */ 
/* 123 */     return new BeanPropertyWriter(am, this._beanDesc.getClassAnnotations(), name, declaredType, ser, typeSer, serializationType, m, f, suppressNulls, suppValue);
/*     */   }
/*     */ 
/*     */   protected JavaType findSerializationType(Annotated a, boolean useStaticTyping)
/*     */   {
/* 142 */     Class serializationType = this._annotationIntrospector.findSerializationType(a);
/* 143 */     if (serializationType != null)
/*     */     {
/* 145 */       Class raw = a.getRawType();
/* 146 */       if (serializationType.isAssignableFrom(raw)) {
/* 147 */         return TypeFactory.type(serializationType);
/*     */       }
/*     */ 
/* 155 */       if (!raw.isAssignableFrom(serializationType)) {
/* 156 */         throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + a.getName() + "': class " + serializationType.getName() + " not a super-type of (declared) class " + raw.getName());
/*     */       }
/*     */ 
/* 162 */       return TypeFactory.type(serializationType);
/*     */     }
/*     */ 
/* 167 */     JsonSerialize.Typing typing = this._annotationIntrospector.findSerializationTyping(a);
/* 168 */     if (typing != null) {
/* 169 */       useStaticTyping = typing == JsonSerialize.Typing.STATIC;
/*     */     }
/* 171 */     if (useStaticTyping) {
/* 172 */       return TypeFactory.type(a.getGenericType(), this._beanDesc.getType());
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   protected Object getDefaultBean()
/*     */   {
/* 179 */     if (this._defaultBean == null)
/*     */     {
/* 183 */       this._defaultBean = this._beanDesc.instantiateBean(this._config.isEnabled(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
/* 184 */       if (this._defaultBean == null) {
/* 185 */         Class cls = this._beanDesc.getClassInfo().getAnnotated();
/* 186 */         throw new IllegalArgumentException("Class " + cls.getName() + " has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
/*     */       }
/*     */     }
/* 189 */     return this._defaultBean;
/*     */   }
/*     */ 
/*     */   protected Object getDefaultValue(String name, Method m, Field f)
/*     */   {
/* 194 */     Object defaultBean = getDefaultBean();
/*     */     try {
/* 196 */       if (m != null) {
/* 197 */         return m.invoke(defaultBean, new Object[0]);
/*     */       }
/* 199 */       return f.get(defaultBean); } catch (Exception e) {
/*     */     }
/* 201 */     return _throwWrapped(e, name, defaultBean);
/*     */   }
/*     */ 
/*     */   protected Object _throwWrapped(Exception e, String propName, Object defaultBean)
/*     */   {
/* 207 */     Throwable t = e;
/* 208 */     while (t.getCause() != null) {
/* 209 */       t = t.getCause();
/*     */     }
/* 211 */     if ((t instanceof Error)) throw ((Error)t);
/* 212 */     if ((t instanceof RuntimeException)) throw ((RuntimeException)t);
/* 213 */     throw new IllegalArgumentException("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance");
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.PropertyBuilder
 * JD-Core Version:    0.6.0
 */