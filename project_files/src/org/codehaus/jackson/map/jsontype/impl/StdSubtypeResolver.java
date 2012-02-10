/*     */ package org.codehaus.jackson.map.jsontype.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.MapperConfig;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*     */ import org.codehaus.jackson.map.jsontype.NamedType;
/*     */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*     */ 
/*     */ public class StdSubtypeResolver extends SubtypeResolver
/*     */ {
/*     */   protected LinkedHashSet<NamedType> _registeredSubtypes;
/*     */ 
/*     */   public void registerSubtypes(NamedType[] types)
/*     */   {
/*  26 */     if (this._registeredSubtypes == null) {
/*  27 */       this._registeredSubtypes = new LinkedHashSet();
/*     */     }
/*  29 */     for (NamedType type : types)
/*  30 */       this._registeredSubtypes.add(type);
/*     */   }
/*     */ 
/*     */   public void registerSubtypes(Class<?>[] classes)
/*     */   {
/*  37 */     NamedType[] types = new NamedType[classes.length];
/*  38 */     int i = 0; for (int len = classes.length; i < len; i++) {
/*  39 */       types[i] = new NamedType(classes[i]);
/*     */     }
/*  41 */     registerSubtypes(types);
/*     */   }
/*     */ 
/*     */   public Collection<NamedType> collectAndResolveSubtypes(AnnotatedMember property, MapperConfig<?> config, AnnotationIntrospector ai)
/*     */   {
/*  54 */     Collection st = ai.findSubtypes(property);
/*     */ 
/*  56 */     if ((st == null) || (st.isEmpty())) {
/*  57 */       return null;
/*     */     }
/*  59 */     return _collectAndResolve(property, config, ai, st);
/*     */   }
/*     */ 
/*     */   public Collection<NamedType> collectAndResolveSubtypes(AnnotatedClass type, MapperConfig<?> config, AnnotationIntrospector ai)
/*     */   {
/*  66 */     HashMap subtypes = new HashMap();
/*     */     Class rawBase;
/*  68 */     if (this._registeredSubtypes != null) {
/*  69 */       rawBase = type.getRawType();
/*  70 */       for (NamedType subtype : this._registeredSubtypes)
/*     */       {
/*  72 */         if (rawBase.isAssignableFrom(subtype.getType())) {
/*  73 */           AnnotatedClass curr = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config);
/*  74 */           _collectAndResolve(curr, subtype, config, ai, subtypes);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  79 */     NamedType rootType = new NamedType(type.getRawType(), null);
/*  80 */     _collectAndResolve(type, rootType, config, ai, subtypes);
/*  81 */     return new ArrayList(subtypes.values());
/*     */   }
/*     */ 
/*     */   protected Collection<NamedType> _collectAndResolve(AnnotatedMember property, MapperConfig<?> config, AnnotationIntrospector ai, Collection<NamedType> subtypeList)
/*     */   {
/* 100 */     HashSet seen = new HashSet(subtypeList);
/* 101 */     ArrayList subtypes = new ArrayList(subtypeList);
/*     */ 
/* 104 */     for (int i = 0; i < subtypes.size(); i++) {
/* 105 */       NamedType type = (NamedType)subtypes.get(i);
/* 106 */       AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(type.getType(), ai, config);
/*     */ 
/* 108 */       if (!type.hasName()) {
/* 109 */         type.setName(ai.findTypeName(ac));
/*     */       }
/*     */ 
/* 112 */       List moreTypes = ai.findSubtypes(ac);
/* 113 */       if (moreTypes != null) {
/* 114 */         for (NamedType t2 : moreTypes)
/*     */         {
/* 116 */           if (seen.add(t2)) {
/* 117 */             subtypes.add(t2);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 124 */     return subtypes;
/*     */   }
/*     */ 
/*     */   protected void _collectAndResolve(AnnotatedClass annotatedType, NamedType namedType, MapperConfig<?> config, AnnotationIntrospector ai, HashMap<NamedType, NamedType> collectedSubtypes)
/*     */   {
/* 133 */     if (!namedType.hasName()) {
/* 134 */       String name = ai.findTypeName(annotatedType);
/* 135 */       if (name != null) {
/* 136 */         namedType = new NamedType(namedType.getType(), name);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     if (collectedSubtypes.containsKey(namedType))
/*     */     {
/* 143 */       if (namedType.hasName()) {
/* 144 */         NamedType prev = (NamedType)collectedSubtypes.get(namedType);
/* 145 */         if (!prev.hasName()) {
/* 146 */           collectedSubtypes.put(namedType, namedType);
/*     */         }
/*     */       }
/* 149 */       return;
/*     */     }
/*     */ 
/* 152 */     collectedSubtypes.put(namedType, namedType);
/* 153 */     Collection st = ai.findSubtypes(annotatedType);
/* 154 */     if ((st != null) && (!st.isEmpty()))
/* 155 */       for (NamedType subtype : st) {
/* 156 */         AnnotatedClass subtypeClass = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config);
/*     */ 
/* 158 */         if (!subtype.hasName()) {
/* 159 */           subtype = new NamedType(subtype.getType(), ai.findTypeName(subtypeClass));
/*     */         }
/* 161 */         _collectAndResolve(subtypeClass, subtype, config, ai, collectedSubtypes);
/*     */       }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver
 * JD-Core Version:    0.6.0
 */