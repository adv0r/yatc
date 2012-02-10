/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.KeyDeserializer;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class NopAnnotationIntrospector extends AnnotationIntrospector
/*     */ {
/*  26 */   public static final NopAnnotationIntrospector instance = new NopAnnotationIntrospector();
/*     */ 
/*     */   public boolean isHandled(Annotation ann)
/*     */   {
/*  36 */     return false;
/*     */   }
/*     */ 
/*     */   public String findEnumValue(Enum<?> value)
/*     */   {
/*  47 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean findCachability(AnnotatedClass ac)
/*     */   {
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   public String findRootName(AnnotatedClass ac)
/*     */   {
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] findPropertiesToIgnore(AnnotatedClass ac)
/*     */   {
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean findIgnoreUnknownProperties(AnnotatedClass ac)
/*     */   {
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker)
/*     */   {
/*  84 */     return checker;
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableConstructor(AnnotatedConstructor c)
/*     */   {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableMethod(AnnotatedMethod m)
/*     */   {
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableField(AnnotatedField f)
/*     */   {
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   public Object findSerializer(Annotated am, BeanProperty property)
/*     */   {
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonSerialize.Inclusion findSerializationInclusion(Annotated a, JsonSerialize.Inclusion defValue)
/*     */   {
/* 127 */     return JsonSerialize.Inclusion.ALWAYS;
/*     */   }
/*     */ 
/*     */   public Class<?> findSerializationType(Annotated a)
/*     */   {
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonSerialize.Typing findSerializationTyping(Annotated a)
/*     */   {
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<?>[] findSerializationViews(Annotated a)
/*     */   {
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] findSerializationPropertyOrder(AnnotatedClass ac)
/*     */   {
/* 153 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean findSerializationSortAlphabetically(AnnotatedClass ac)
/*     */   {
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public String findGettablePropertyName(AnnotatedMethod am)
/*     */   {
/* 169 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasAsValueAnnotation(AnnotatedMethod am)
/*     */   {
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */   public String findDeserializablePropertyName(AnnotatedField af)
/*     */   {
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<?> findDeserializationContentType(Annotated am, JavaType t, String propName)
/*     */   {
/* 184 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<?> findDeserializationKeyType(Annotated am, JavaType t, String propName)
/*     */   {
/* 189 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<?> findDeserializationType(Annotated am, JavaType t, String propName)
/*     */   {
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */   public Object findDeserializer(Annotated am, BeanProperty property) {
/* 198 */     return null;
/*     */   }
/*     */   public Class<KeyDeserializer> findKeyDeserializer(Annotated am) {
/* 201 */     return null;
/*     */   }
/*     */   public Class<JsonDeserializer<?>> findContentDeserializer(Annotated am) {
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   public String findPropertyNameForParam(AnnotatedParameter param)
/*     */   {
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */   public String findSerializablePropertyName(AnnotatedField af)
/*     */   {
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */   public String findSettablePropertyName(AnnotatedMethod am)
/*     */   {
/* 219 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.NopAnnotationIntrospector
 * JD-Core Version:    0.6.0
 */