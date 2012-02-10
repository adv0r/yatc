/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.ClassIntrospector;
/*     */ import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.MapperConfig;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class BasicClassIntrospector extends ClassIntrospector<BasicBeanDescription>
/*     */ {
/* 112 */   public static final BasicClassIntrospector instance = new BasicClassIntrospector();
/*     */ 
/*     */   public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 126 */     AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
/* 127 */     AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
/*     */ 
/* 129 */     ac.resolveMemberMethods(getSerializationMethodFilter(cfg), false);
/*     */ 
/* 137 */     ac.resolveCreators(true);
/*     */ 
/* 139 */     ac.resolveFields(false);
/* 140 */     return new BasicBeanDescription(type, ac, ai);
/*     */   }
/*     */ 
/*     */   public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 148 */     AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
/* 149 */     AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
/*     */ 
/* 151 */     ac.resolveMemberMethods(getDeserializationMethodFilter(cfg), true);
/*     */ 
/* 153 */     ac.resolveCreators(true);
/*     */ 
/* 155 */     ac.resolveFields(true);
/* 156 */     return new BasicBeanDescription(type, ac, ai);
/*     */   }
/*     */ 
/*     */   public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 163 */     AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
/* 164 */     AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
/* 165 */     ac.resolveCreators(true);
/* 166 */     return new BasicBeanDescription(type, ac, ai);
/*     */   }
/*     */ 
/*     */   public BasicBeanDescription forClassAnnotations(MapperConfig<?> cfg, Class<?> c, ClassIntrospector.MixInResolver r)
/*     */   {
/* 173 */     AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
/* 174 */     AnnotatedClass ac = AnnotatedClass.construct(c, ai, r);
/* 175 */     return new BasicBeanDescription(TypeFactory.type(c), ac, ai);
/*     */   }
/*     */ 
/*     */   public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> cfg, Class<?> c, ClassIntrospector.MixInResolver r)
/*     */   {
/* 182 */     AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
/* 183 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(c, ai, r);
/* 184 */     return new BasicBeanDescription(TypeFactory.type(c), ac, ai);
/*     */   }
/*     */ 
/*     */   protected MethodFilter getSerializationMethodFilter(SerializationConfig cfg)
/*     */   {
/* 199 */     return GetterMethodFilter.instance;
/*     */   }
/*     */ 
/*     */   protected MethodFilter getDeserializationMethodFilter(DeserializationConfig cfg)
/*     */   {
/* 211 */     if (cfg.isEnabled(DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS)) {
/* 212 */       return SetterAndGetterMethodFilter.instance;
/*     */     }
/*     */ 
/* 215 */     return SetterMethodFilter.instance;
/*     */   }
/*     */ 
/*     */   public static final class SetterAndGetterMethodFilter extends BasicClassIntrospector.SetterMethodFilter
/*     */   {
/*  85 */     public static final SetterAndGetterMethodFilter instance = new SetterAndGetterMethodFilter();
/*     */ 
/*     */     public boolean includeMethod(Method m)
/*     */     {
/*  90 */       if (super.includeMethod(m)) {
/*  91 */         return true;
/*     */       }
/*  93 */       if (!ClassUtil.hasGetterSignature(m)) {
/*  94 */         return false;
/*     */       }
/*     */ 
/*  97 */       Class rt = m.getReturnType();
/*     */ 
/* 100 */       return (Collection.class.isAssignableFrom(rt)) || (Map.class.isAssignableFrom(rt));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SetterMethodFilter
/*     */     implements MethodFilter
/*     */   {
/*  49 */     public static final SetterMethodFilter instance = new SetterMethodFilter();
/*     */ 
/*     */     public boolean includeMethod(Method m)
/*     */     {
/*  54 */       if (Modifier.isStatic(m.getModifiers())) {
/*  55 */         return false;
/*     */       }
/*  57 */       int pcount = m.getParameterTypes().length;
/*     */ 
/*  59 */       switch (pcount)
/*     */       {
/*     */       case 1:
/*  62 */         return true;
/*     */       case 2:
/*  72 */         return true;
/*     */       }
/*  74 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class GetterMethodFilter
/*     */     implements MethodFilter
/*     */   {
/*  27 */     public static final GetterMethodFilter instance = new GetterMethodFilter();
/*     */ 
/*     */     public boolean includeMethod(Method m)
/*     */     {
/*  33 */       return ClassUtil.hasGetterSignature(m);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.BasicClassIntrospector
 * JD-Core Version:    0.6.0
 */