/*      */ package org.codehaus.jackson.map;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
/*      */ import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
/*      */ import org.codehaus.jackson.map.introspect.Annotated;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedField;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedParameter;
/*      */ import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;
/*      */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*      */ import org.codehaus.jackson.map.jsontype.NamedType;
/*      */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*      */ import org.codehaus.jackson.type.JavaType;
/*      */ 
/*      */ public abstract class AnnotationIntrospector
/*      */ {
/*      */   public static AnnotationIntrospector nopInstance()
/*      */   {
/*   94 */     return NopAnnotationIntrospector.instance;
/*      */   }
/*      */ 
/*      */   public static AnnotationIntrospector pair(AnnotationIntrospector a1, AnnotationIntrospector a2) {
/*   98 */     return new Pair(a1, a2);
/*      */   }
/*      */ 
/*      */   public Collection<AnnotationIntrospector> allIntrospectors()
/*      */   {
/*  119 */     return Collections.singletonList(this);
/*      */   }
/*      */ 
/*      */   public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result)
/*      */   {
/*  133 */     result.add(this);
/*  134 */     return result;
/*      */   }
/*      */ 
/*      */   public abstract boolean isHandled(Annotation paramAnnotation);
/*      */ 
/*      */   public abstract Boolean findCachability(AnnotatedClass paramAnnotatedClass);
/*      */ 
/*      */   public abstract String findRootName(AnnotatedClass paramAnnotatedClass);
/*      */ 
/*      */   public abstract String[] findPropertiesToIgnore(AnnotatedClass paramAnnotatedClass);
/*      */ 
/*      */   public abstract Boolean findIgnoreUnknownProperties(AnnotatedClass paramAnnotatedClass);
/*      */ 
/*      */   public Boolean isIgnorableType(AnnotatedClass ac)
/*      */   {
/*  215 */     return null;
/*      */   }
/*      */ 
/*      */   public Object findFilterId(AnnotatedClass ac)
/*      */   {
/*  226 */     return null;
/*      */   }
/*      */ 
/*      */   public abstract VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass paramAnnotatedClass, VisibilityChecker<?> paramVisibilityChecker);
/*      */ 
/*      */   public TypeResolverBuilder<?> findTypeResolver(AnnotatedClass ac, JavaType baseType)
/*      */   {
/*  269 */     return null;
/*      */   }
/*      */ 
/*      */   public TypeResolverBuilder<?> findPropertyTypeResolver(AnnotatedMember am, JavaType baseType)
/*      */   {
/*  289 */     return null;
/*      */   }
/*      */ 
/*      */   public TypeResolverBuilder<?> findPropertyContentTypeResolver(AnnotatedMember am, JavaType containerType)
/*      */   {
/*  311 */     return null;
/*      */   }
/*      */ 
/*      */   public List<NamedType> findSubtypes(Annotated a)
/*      */   {
/*  326 */     return null;
/*      */   }
/*      */ 
/*      */   public String findTypeName(AnnotatedClass ac)
/*      */   {
/*  337 */     return null;
/*      */   }
/*      */ 
/*      */   public ReferenceProperty findReferenceType(AnnotatedMember member)
/*      */   {
/*  353 */     return null;
/*      */   }
/*      */ 
/*      */   public abstract boolean isIgnorableMethod(AnnotatedMethod paramAnnotatedMethod);
/*      */ 
/*      */   public abstract boolean isIgnorableConstructor(AnnotatedConstructor paramAnnotatedConstructor);
/*      */ 
/*      */   public abstract boolean isIgnorableField(AnnotatedField paramAnnotatedField);
/*      */ 
/*      */   @Deprecated
/*      */   public Object findSerializer(Annotated am)
/*      */   {
/*  414 */     return findSerializer(am, null);
/*      */   }
/*      */ 
/*      */   public abstract Object findSerializer(Annotated paramAnnotated, BeanProperty paramBeanProperty);
/*      */ 
/*      */   public abstract JsonSerialize.Inclusion findSerializationInclusion(Annotated paramAnnotated, JsonSerialize.Inclusion paramInclusion);
/*      */ 
/*      */   public abstract Class<?> findSerializationType(Annotated paramAnnotated);
/*      */ 
/*      */   public abstract JsonSerialize.Typing findSerializationTyping(Annotated paramAnnotated);
/*      */ 
/*      */   public abstract Class<?>[] findSerializationViews(Annotated paramAnnotated);
/*      */ 
/*      */   public abstract String[] findSerializationPropertyOrder(AnnotatedClass paramAnnotatedClass);
/*      */ 
/*      */   public abstract Boolean findSerializationSortAlphabetically(AnnotatedClass paramAnnotatedClass);
/*      */ 
/*      */   public abstract String findGettablePropertyName(AnnotatedMethod paramAnnotatedMethod);
/*      */ 
/*      */   public abstract boolean hasAsValueAnnotation(AnnotatedMethod paramAnnotatedMethod);
/*      */ 
/*      */   public abstract String findEnumValue(Enum<?> paramEnum);
/*      */ 
/*      */   public abstract String findSerializablePropertyName(AnnotatedField paramAnnotatedField);
/*      */ 
/*      */   @Deprecated
/*      */   public final Object findDeserializer(Annotated am)
/*      */   {
/*  565 */     return findDeserializer(am, null);
/*      */   }
/*      */ 
/*      */   public abstract Object findDeserializer(Annotated paramAnnotated, BeanProperty paramBeanProperty);
/*      */ 
/*      */   public abstract Class<? extends KeyDeserializer> findKeyDeserializer(Annotated paramAnnotated);
/*      */ 
/*      */   public abstract Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated paramAnnotated);
/*      */ 
/*      */   public abstract Class<?> findDeserializationType(Annotated paramAnnotated, JavaType paramJavaType, String paramString);
/*      */ 
/*      */   public abstract Class<?> findDeserializationKeyType(Annotated paramAnnotated, JavaType paramJavaType, String paramString);
/*      */ 
/*      */   public abstract Class<?> findDeserializationContentType(Annotated paramAnnotated, JavaType paramJavaType, String paramString);
/*      */ 
/*      */   public abstract String findSettablePropertyName(AnnotatedMethod paramAnnotatedMethod);
/*      */ 
/*      */   public boolean hasAnySetterAnnotation(AnnotatedMethod am)
/*      */   {
/*  685 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean hasAnyGetterAnnotation(AnnotatedMethod am)
/*      */   {
/*  700 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean hasCreatorAnnotation(Annotated a)
/*      */   {
/*  714 */     return false;
/*      */   }
/*      */ 
/*      */   public abstract String findDeserializablePropertyName(AnnotatedField paramAnnotatedField);
/*      */ 
/*      */   public abstract String findPropertyNameForParam(AnnotatedParameter paramAnnotatedParameter);
/*      */ 
/*      */   public static class Pair extends AnnotationIntrospector
/*      */   {
/*      */     protected final AnnotationIntrospector _primary;
/*      */     protected final AnnotationIntrospector _secondary;
/*      */ 
/*      */     public Pair(AnnotationIntrospector p, AnnotationIntrospector s)
/*      */     {
/*  776 */       this._primary = p;
/*  777 */       this._secondary = s;
/*      */     }
/*      */ 
/*      */     public static AnnotationIntrospector create(AnnotationIntrospector primary, AnnotationIntrospector secondary)
/*      */     {
/*  790 */       if (primary == null) {
/*  791 */         return secondary;
/*      */       }
/*  793 */       if (secondary == null) {
/*  794 */         return primary;
/*      */       }
/*  796 */       return new Pair(primary, secondary);
/*      */     }
/*      */ 
/*      */     public Collection<AnnotationIntrospector> allIntrospectors()
/*      */     {
/*  801 */       return allIntrospectors(new ArrayList());
/*      */     }
/*      */ 
/*      */     public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result)
/*      */     {
/*  807 */       this._primary.allIntrospectors(result);
/*  808 */       this._secondary.allIntrospectors(result);
/*  809 */       return result;
/*      */     }
/*      */ 
/*      */     public boolean isHandled(Annotation ann)
/*      */     {
/*  817 */       return (this._primary.isHandled(ann)) || (this._secondary.isHandled(ann));
/*      */     }
/*      */ 
/*      */     public Boolean findCachability(AnnotatedClass ac)
/*      */     {
/*  829 */       Boolean result = this._primary.findCachability(ac);
/*  830 */       if (result == null) {
/*  831 */         result = this._secondary.findCachability(ac);
/*      */       }
/*  833 */       return result;
/*      */     }
/*      */ 
/*      */     public String findRootName(AnnotatedClass ac)
/*      */     {
/*  839 */       String name1 = this._primary.findRootName(ac);
/*  840 */       if (name1 == null)
/*  841 */         return this._secondary.findRootName(ac);
/*  842 */       if (name1.length() > 0) {
/*  843 */         return name1;
/*      */       }
/*      */ 
/*  846 */       String name2 = this._secondary.findRootName(ac);
/*  847 */       return name2 == null ? name1 : name2;
/*      */     }
/*      */ 
/*      */     public String[] findPropertiesToIgnore(AnnotatedClass ac)
/*      */     {
/*  853 */       String[] result = this._primary.findPropertiesToIgnore(ac);
/*  854 */       if (result == null) {
/*  855 */         result = this._secondary.findPropertiesToIgnore(ac);
/*      */       }
/*  857 */       return result;
/*      */     }
/*      */ 
/*      */     public Boolean findIgnoreUnknownProperties(AnnotatedClass ac)
/*      */     {
/*  863 */       Boolean result = this._primary.findIgnoreUnknownProperties(ac);
/*  864 */       if (result == null) {
/*  865 */         result = this._secondary.findIgnoreUnknownProperties(ac);
/*      */       }
/*  867 */       return result;
/*      */     }
/*      */ 
/*      */     public Boolean isIgnorableType(AnnotatedClass ac)
/*      */     {
/*  873 */       Boolean result = this._primary.isIgnorableType(ac);
/*  874 */       if (result == null) {
/*  875 */         result = this._secondary.isIgnorableType(ac);
/*      */       }
/*  877 */       return result;
/*      */     }
/*      */ 
/*      */     public Object findFilterId(AnnotatedClass ac)
/*      */     {
/*  883 */       Object id = this._primary.findFilterId(ac);
/*  884 */       if (id == null) {
/*  885 */         id = this._secondary.findFilterId(ac);
/*      */       }
/*  887 */       return id;
/*      */     }
/*      */ 
/*      */     public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker)
/*      */     {
/*  903 */       checker = this._secondary.findAutoDetectVisibility(ac, checker);
/*  904 */       return this._primary.findAutoDetectVisibility(ac, checker);
/*      */     }
/*      */ 
/*      */     public TypeResolverBuilder<?> findTypeResolver(AnnotatedClass ac, JavaType baseType)
/*      */     {
/*  916 */       TypeResolverBuilder b = this._primary.findTypeResolver(ac, baseType);
/*  917 */       if (b == null) {
/*  918 */         b = this._secondary.findTypeResolver(ac, baseType);
/*      */       }
/*  920 */       return b;
/*      */     }
/*      */ 
/*      */     public TypeResolverBuilder<?> findPropertyTypeResolver(AnnotatedMember am, JavaType baseType)
/*      */     {
/*  926 */       TypeResolverBuilder b = this._primary.findPropertyTypeResolver(am, baseType);
/*  927 */       if (b == null) {
/*  928 */         b = this._secondary.findPropertyTypeResolver(am, baseType);
/*      */       }
/*  930 */       return b;
/*      */     }
/*      */ 
/*      */     public TypeResolverBuilder<?> findPropertyContentTypeResolver(AnnotatedMember am, JavaType baseType)
/*      */     {
/*  936 */       TypeResolverBuilder b = this._primary.findPropertyContentTypeResolver(am, baseType);
/*  937 */       if (b == null) {
/*  938 */         b = this._secondary.findPropertyContentTypeResolver(am, baseType);
/*      */       }
/*  940 */       return b;
/*      */     }
/*      */ 
/*      */     public List<NamedType> findSubtypes(Annotated a)
/*      */     {
/*  946 */       List types1 = this._primary.findSubtypes(a);
/*  947 */       List types2 = this._secondary.findSubtypes(a);
/*  948 */       if ((types1 == null) || (types1.isEmpty())) return types2;
/*  949 */       if ((types2 == null) || (types2.isEmpty())) return types1;
/*  950 */       ArrayList result = new ArrayList(types1.size() + types2.size());
/*  951 */       result.addAll(types1);
/*  952 */       result.addAll(types2);
/*  953 */       return result;
/*      */     }
/*      */ 
/*      */     public String findTypeName(AnnotatedClass ac)
/*      */     {
/*  959 */       String name = this._primary.findTypeName(ac);
/*  960 */       if ((name == null) || (name.length() == 0)) {
/*  961 */         name = this._secondary.findTypeName(ac);
/*      */       }
/*  963 */       return name;
/*      */     }
/*      */ 
/*      */     public AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember member)
/*      */     {
/*  971 */       AnnotationIntrospector.ReferenceProperty ref = this._primary.findReferenceType(member);
/*  972 */       if (ref == null) {
/*  973 */         ref = this._secondary.findReferenceType(member);
/*      */       }
/*  975 */       return ref;
/*      */     }
/*      */ 
/*      */     public boolean isIgnorableMethod(AnnotatedMethod m)
/*      */     {
/*  982 */       return (this._primary.isIgnorableMethod(m)) || (this._secondary.isIgnorableMethod(m));
/*      */     }
/*      */ 
/*      */     public boolean isIgnorableConstructor(AnnotatedConstructor c)
/*      */     {
/*  987 */       return (this._primary.isIgnorableConstructor(c)) || (this._secondary.isIgnorableConstructor(c));
/*      */     }
/*      */ 
/*      */     public boolean isIgnorableField(AnnotatedField f)
/*      */     {
/*  995 */       return (this._primary.isIgnorableField(f)) || (this._secondary.isIgnorableField(f));
/*      */     }
/*      */ 
/*      */     public Object findSerializer(Annotated am, BeanProperty property)
/*      */     {
/* 1003 */       Object result = this._primary.findSerializer(am, property);
/*      */ 
/* 1008 */       if (result == null) {
/* 1009 */         result = this._secondary.findSerializer(am, property);
/*      */       }
/* 1011 */       return result;
/*      */     }
/*      */ 
/*      */     public JsonSerialize.Inclusion findSerializationInclusion(Annotated a, JsonSerialize.Inclusion defValue)
/*      */     {
/* 1029 */       defValue = this._secondary.findSerializationInclusion(a, defValue);
/* 1030 */       defValue = this._primary.findSerializationInclusion(a, defValue);
/* 1031 */       return defValue;
/*      */     }
/*      */ 
/*      */     public Class<?> findSerializationType(Annotated a)
/*      */     {
/* 1037 */       Class result = this._primary.findSerializationType(a);
/* 1038 */       if (result == null) {
/* 1039 */         result = this._secondary.findSerializationType(a);
/*      */       }
/* 1041 */       return result;
/*      */     }
/*      */ 
/*      */     public JsonSerialize.Typing findSerializationTyping(Annotated a)
/*      */     {
/* 1047 */       JsonSerialize.Typing result = this._primary.findSerializationTyping(a);
/* 1048 */       if (result == null) {
/* 1049 */         result = this._secondary.findSerializationTyping(a);
/*      */       }
/* 1051 */       return result;
/*      */     }
/*      */ 
/*      */     public Class<?>[] findSerializationViews(Annotated a)
/*      */     {
/* 1061 */       Class[] result = this._primary.findSerializationViews(a);
/* 1062 */       if (result == null) {
/* 1063 */         result = this._secondary.findSerializationViews(a);
/*      */       }
/* 1065 */       return result;
/*      */     }
/*      */ 
/*      */     public String[] findSerializationPropertyOrder(AnnotatedClass ac)
/*      */     {
/* 1072 */       String[] result = this._primary.findSerializationPropertyOrder(ac);
/* 1073 */       if (result == null) {
/* 1074 */         result = this._secondary.findSerializationPropertyOrder(ac);
/*      */       }
/* 1076 */       return result;
/*      */     }
/*      */ 
/*      */     public Boolean findSerializationSortAlphabetically(AnnotatedClass ac)
/*      */     {
/* 1086 */       Boolean result = this._primary.findSerializationSortAlphabetically(ac);
/* 1087 */       if (result == null) {
/* 1088 */         result = this._secondary.findSerializationSortAlphabetically(ac);
/*      */       }
/* 1090 */       return result;
/*      */     }
/*      */ 
/*      */     public String findGettablePropertyName(AnnotatedMethod am)
/*      */     {
/* 1098 */       String result = this._primary.findGettablePropertyName(am);
/* 1099 */       if (result == null) {
/* 1100 */         result = this._secondary.findGettablePropertyName(am);
/* 1101 */       } else if (result.length() == 0)
/*      */       {
/* 1105 */         String str2 = this._secondary.findGettablePropertyName(am);
/* 1106 */         if (str2 != null) {
/* 1107 */           result = str2;
/*      */         }
/*      */       }
/* 1110 */       return result;
/*      */     }
/*      */ 
/*      */     public boolean hasAsValueAnnotation(AnnotatedMethod am)
/*      */     {
/* 1116 */       return (this._primary.hasAsValueAnnotation(am)) || (this._secondary.hasAsValueAnnotation(am));
/*      */     }
/*      */ 
/*      */     public String findEnumValue(Enum<?> value)
/*      */     {
/* 1122 */       String result = this._primary.findEnumValue(value);
/* 1123 */       if (result == null) {
/* 1124 */         result = this._secondary.findEnumValue(value);
/*      */       }
/* 1126 */       return result;
/*      */     }
/*      */ 
/*      */     public String findSerializablePropertyName(AnnotatedField af)
/*      */     {
/* 1134 */       String result = this._primary.findSerializablePropertyName(af);
/* 1135 */       if (result == null) {
/* 1136 */         result = this._secondary.findSerializablePropertyName(af);
/* 1137 */       } else if (result.length() == 0)
/*      */       {
/* 1141 */         String str2 = this._secondary.findSerializablePropertyName(af);
/* 1142 */         if (str2 != null) {
/* 1143 */           result = str2;
/*      */         }
/*      */       }
/* 1146 */       return result;
/*      */     }
/*      */ 
/*      */     public Object findDeserializer(Annotated am, BeanProperty property)
/*      */     {
/* 1154 */       Object result = this._primary.findDeserializer(am, property);
/* 1155 */       if (result == null) {
/* 1156 */         result = this._secondary.findDeserializer(am, property);
/*      */       }
/* 1158 */       return result;
/*      */     }
/*      */ 
/*      */     public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated am)
/*      */     {
/* 1164 */       Class result = this._primary.findKeyDeserializer(am);
/* 1165 */       if ((result == null) || (result == KeyDeserializer.None.class)) {
/* 1166 */         result = this._secondary.findKeyDeserializer(am);
/*      */       }
/* 1168 */       return result;
/*      */     }
/*      */ 
/*      */     public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated am)
/*      */     {
/* 1174 */       Class result = this._primary.findContentDeserializer(am);
/* 1175 */       if ((result == null) || (result == JsonDeserializer.None.class)) {
/* 1176 */         result = this._secondary.findContentDeserializer(am);
/*      */       }
/* 1178 */       return result;
/*      */     }
/*      */ 
/*      */     public Class<?> findDeserializationType(Annotated am, JavaType baseType, String propName)
/*      */     {
/* 1185 */       Class result = this._primary.findDeserializationType(am, baseType, propName);
/* 1186 */       if (result == null) {
/* 1187 */         result = this._secondary.findDeserializationType(am, baseType, propName);
/*      */       }
/* 1189 */       return result;
/*      */     }
/*      */ 
/*      */     public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName)
/*      */     {
/* 1196 */       Class result = this._primary.findDeserializationKeyType(am, baseKeyType, propName);
/* 1197 */       if (result == null) {
/* 1198 */         result = this._secondary.findDeserializationKeyType(am, baseKeyType, propName);
/*      */       }
/* 1200 */       return result;
/*      */     }
/*      */ 
/*      */     public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType, String propName)
/*      */     {
/* 1207 */       Class result = this._primary.findDeserializationContentType(am, baseContentType, propName);
/* 1208 */       if (result == null) {
/* 1209 */         result = this._secondary.findDeserializationContentType(am, baseContentType, propName);
/*      */       }
/* 1211 */       return result;
/*      */     }
/*      */ 
/*      */     public String findSettablePropertyName(AnnotatedMethod am)
/*      */     {
/* 1220 */       String result = this._primary.findSettablePropertyName(am);
/* 1221 */       if (result == null) {
/* 1222 */         result = this._secondary.findSettablePropertyName(am);
/* 1223 */       } else if (result.length() == 0)
/*      */       {
/* 1227 */         String str2 = this._secondary.findSettablePropertyName(am);
/* 1228 */         if (str2 != null) {
/* 1229 */           result = str2;
/*      */         }
/*      */       }
/* 1232 */       return result;
/*      */     }
/*      */ 
/*      */     public boolean hasAnySetterAnnotation(AnnotatedMethod am)
/*      */     {
/* 1238 */       return (this._primary.hasAnySetterAnnotation(am)) || (this._secondary.hasAnySetterAnnotation(am));
/*      */     }
/*      */ 
/*      */     public boolean hasAnyGetterAnnotation(AnnotatedMethod am)
/*      */     {
/* 1244 */       return (this._primary.hasAnyGetterAnnotation(am)) || (this._secondary.hasAnyGetterAnnotation(am));
/*      */     }
/*      */ 
/*      */     public boolean hasCreatorAnnotation(Annotated a)
/*      */     {
/* 1250 */       return (this._primary.hasCreatorAnnotation(a)) || (this._secondary.hasCreatorAnnotation(a));
/*      */     }
/*      */ 
/*      */     public String findDeserializablePropertyName(AnnotatedField af)
/*      */     {
/* 1258 */       String result = this._primary.findDeserializablePropertyName(af);
/* 1259 */       if (result == null) {
/* 1260 */         result = this._secondary.findDeserializablePropertyName(af);
/* 1261 */       } else if (result.length() == 0)
/*      */       {
/* 1265 */         String str2 = this._secondary.findDeserializablePropertyName(af);
/* 1266 */         if (str2 != null) {
/* 1267 */           result = str2;
/*      */         }
/*      */       }
/* 1270 */       return result;
/*      */     }
/*      */ 
/*      */     public String findPropertyNameForParam(AnnotatedParameter param)
/*      */     {
/* 1278 */       String result = this._primary.findPropertyNameForParam(param);
/* 1279 */       if (result == null) {
/* 1280 */         result = this._secondary.findPropertyNameForParam(param);
/*      */       }
/* 1282 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ReferenceProperty
/*      */   {
/*      */     private final Type _type;
/*      */     private final String _name;
/*      */ 
/*      */     public ReferenceProperty(Type t, String n)
/*      */     {
/*   66 */       this._type = t;
/*   67 */       this._name = n;
/*      */     }
/*      */     public static ReferenceProperty managed(String name) {
/*   70 */       return new ReferenceProperty(Type.MANAGED_REFERENCE, name); } 
/*   71 */     public static ReferenceProperty back(String name) { return new ReferenceProperty(Type.BACK_REFERENCE, name); } 
/*      */     public Type getType() {
/*   73 */       return this._type; } 
/*   74 */     public String getName() { return this._name; } 
/*      */     public boolean isManagedReference() {
/*   76 */       return this._type == Type.MANAGED_REFERENCE; } 
/*   77 */     public boolean isBackReference() { return this._type == Type.BACK_REFERENCE;
/*      */     }
/*      */ 
/*      */     public static enum Type
/*      */     {
/*   50 */       MANAGED_REFERENCE, 
/*      */ 
/*   58 */       BACK_REFERENCE;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.AnnotationIntrospector
 * JD-Core Version:    0.6.0
 */