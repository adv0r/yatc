/*     */ package org.codehaus.jackson.map.jsontype.impl;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*     */ import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.jsontype.NamedType;
/*     */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class StdTypeResolverBuilder
/*     */   implements TypeResolverBuilder<StdTypeResolverBuilder>
/*     */ {
/*     */   protected JsonTypeInfo.Id _idType;
/*     */   protected JsonTypeInfo.As _includeAs;
/*     */   protected String _typeProperty;
/*     */   protected TypeIdResolver _customIdResolver;
/*     */ 
/*     */   public StdTypeResolverBuilder init(JsonTypeInfo.Id idType, TypeIdResolver idRes)
/*     */   {
/*  46 */     if (idType == null) {
/*  47 */       throw new IllegalArgumentException("idType can not be null");
/*     */     }
/*  49 */     this._idType = idType;
/*  50 */     this._customIdResolver = idRes;
/*     */ 
/*  52 */     this._typeProperty = idType.getDefaultPropertyName();
/*  53 */     return this;
/*     */   }
/*     */ 
/*     */   public TypeSerializer buildTypeSerializer(JavaType baseType, Collection<NamedType> subtypes, BeanProperty property)
/*     */   {
/*  60 */     TypeIdResolver idRes = idResolver(baseType, subtypes, true, false);
/*  61 */     switch (1.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[this._includeAs.ordinal()]) {
/*     */     case 1:
/*  63 */       return new AsArrayTypeSerializer(idRes, property);
/*     */     case 2:
/*  65 */       return new AsPropertyTypeSerializer(idRes, property, this._typeProperty);
/*     */     case 3:
/*  67 */       return new AsWrapperTypeSerializer(idRes, property);
/*     */     }
/*  69 */     throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
/*     */   }
/*     */ 
/*     */   public TypeDeserializer buildTypeDeserializer(JavaType baseType, Collection<NamedType> subtypes, BeanProperty property)
/*     */   {
/*  75 */     TypeIdResolver idRes = idResolver(baseType, subtypes, false, true);
/*     */ 
/*  78 */     switch (1.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[this._includeAs.ordinal()]) {
/*     */     case 1:
/*  80 */       return new AsArrayTypeDeserializer(baseType, idRes, property);
/*     */     case 2:
/*  82 */       return new AsPropertyTypeDeserializer(baseType, idRes, property, this._typeProperty);
/*     */     case 3:
/*  84 */       return new AsWrapperTypeDeserializer(baseType, idRes, property);
/*     */     }
/*  86 */     throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
/*     */   }
/*     */ 
/*     */   public StdTypeResolverBuilder inclusion(JsonTypeInfo.As includeAs)
/*     */   {
/*  96 */     if (includeAs == null) {
/*  97 */       throw new IllegalArgumentException("includeAs can not be null");
/*     */     }
/*  99 */     this._includeAs = includeAs;
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   public StdTypeResolverBuilder typeProperty(String typeIdPropName)
/*     */   {
/* 110 */     if ((typeIdPropName == null) || (typeIdPropName.length() == 0)) {
/* 111 */       typeIdPropName = this._idType.getDefaultPropertyName();
/*     */     }
/* 113 */     this._typeProperty = typeIdPropName;
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   public String getTypeProperty()
/*     */   {
/* 123 */     return this._typeProperty;
/*     */   }
/*     */ 
/*     */   protected TypeIdResolver idResolver(JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser)
/*     */   {
/* 140 */     if (this._customIdResolver != null) {
/* 141 */       return this._customIdResolver;
/*     */     }
/* 143 */     if (this._idType == null) {
/* 144 */       throw new IllegalStateException("Can not build, 'init()' not yet called");
/*     */     }
/* 146 */     switch (1.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[this._idType.ordinal()]) {
/*     */     case 1:
/* 148 */       return new ClassNameIdResolver(baseType);
/*     */     case 2:
/* 150 */       return new MinimalClassNameIdResolver(baseType);
/*     */     case 3:
/* 152 */       return TypeNameIdResolver.construct(baseType, subtypes, forSer, forDeser);
/*     */     case 4:
/*     */     case 5:
/*     */     }
/*     */ 
/* 157 */     throw new IllegalStateException("Do not know how to construct standard type id resolver for idType: " + this._idType);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder
 * JD-Core Version:    0.6.0
 */