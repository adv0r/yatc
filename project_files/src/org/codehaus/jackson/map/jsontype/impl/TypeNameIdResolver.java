/*     */ package org.codehaus.jackson.map.jsontype.impl;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
/*     */ import org.codehaus.jackson.map.jsontype.NamedType;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class TypeNameIdResolver extends TypeIdResolverBase
/*     */ {
/*     */   protected final HashMap<String, String> _typeToId;
/*     */   protected final HashMap<String, JavaType> _idToType;
/*     */ 
/*     */   protected TypeNameIdResolver(JavaType baseType, HashMap<String, String> typeToId, HashMap<String, JavaType> idToType)
/*     */   {
/*  27 */     super(baseType);
/*  28 */     this._typeToId = typeToId;
/*  29 */     this._idToType = idToType;
/*     */   }
/*     */ 
/*     */   public static TypeNameIdResolver construct(JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser)
/*     */   {
/*  36 */     if (forSer == forDeser) throw new IllegalArgumentException();
/*     */ 
/*  38 */     HashMap typeToId = null;
/*  39 */     HashMap idToType = null;
/*     */ 
/*  41 */     if (forSer) {
/*  42 */       typeToId = new HashMap();
/*     */     }
/*  44 */     if (forDeser) {
/*  45 */       idToType = new HashMap();
/*     */     }
/*  47 */     if (subtypes != null) {
/*  48 */       for (NamedType t : subtypes)
/*     */       {
/*  52 */         Class cls = t.getType();
/*  53 */         String id = t.hasName() ? t.getName() : _defaultTypeId(cls);
/*  54 */         if (forSer) {
/*  55 */           typeToId.put(cls.getName(), id);
/*     */         }
/*  57 */         if (forDeser)
/*     */         {
/*  59 */           if (!idToType.containsKey(id)) {
/*  60 */             idToType.put(id, TypeFactory.type(cls));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  65 */     return new TypeNameIdResolver(baseType, typeToId, idToType);
/*     */   }
/*     */   public JsonTypeInfo.Id getMechanism() {
/*  68 */     return JsonTypeInfo.Id.NAME;
/*     */   }
/*     */ 
/*     */   public String idFromValue(Object value) {
/*  72 */     Class cls = value.getClass();
/*  73 */     String name = (String)this._typeToId.get(cls.getName());
/*     */ 
/*  75 */     if (name == null)
/*     */     {
/*  77 */       name = _defaultTypeId(cls);
/*     */     }
/*  79 */     return name;
/*     */   }
/*     */ 
/*     */   public JavaType typeFromId(String id)
/*     */     throws IllegalArgumentException
/*     */   {
/*  85 */     JavaType t = (JavaType)this._idToType.get(id);
/*     */ 
/*  91 */     return t;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  97 */     StringBuilder sb = new StringBuilder();
/*  98 */     sb.append('[').append(getClass().getName());
/*  99 */     sb.append("; id-to-type=").append(this._idToType);
/* 100 */     sb.append(']');
/* 101 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   protected static String _defaultTypeId(Class<?> cls)
/*     */   {
/* 116 */     String n = cls.getName();
/* 117 */     int ix = n.lastIndexOf('.');
/* 118 */     return ix < 0 ? n : n.substring(ix + 1);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.TypeNameIdResolver
 * JD-Core Version:    0.6.0
 */