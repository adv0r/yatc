/*     */ package org.codehaus.jackson.map.type;
/*     */ 
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class TypeBindings
/*     */ {
/*  15 */   private static final JavaType[] NO_TYPES = new JavaType[0];
/*     */ 
/*  20 */   public static final JavaType UNBOUND = new SimpleType(Object.class);
/*     */   protected final JavaType _contextType;
/*     */   protected final Class<?> _contextClass;
/*     */   protected Map<String, JavaType> _bindings;
/*     */   protected HashSet<String> _placeholders;
/*     */   private final TypeBindings _parentBindings;
/*     */ 
/*     */   public TypeBindings(Class<?> cc)
/*     */   {
/*  63 */     this(null, cc, null);
/*     */   }
/*     */ 
/*     */   public TypeBindings(JavaType type)
/*     */   {
/*  68 */     this(null, type.getRawClass(), type);
/*     */   }
/*     */ 
/*     */   public TypeBindings childInstance()
/*     */   {
/*  80 */     return new TypeBindings(this, this._contextClass, this._contextType);
/*     */   }
/*     */ 
/*     */   private TypeBindings(TypeBindings parent, Class<?> cc, JavaType type)
/*     */   {
/*  88 */     this._parentBindings = parent;
/*  89 */     this._contextClass = cc;
/*  90 */     this._contextType = type;
/*     */   }
/*     */ 
/*     */   public int getBindingCount()
/*     */   {
/* 100 */     if (this._bindings == null) {
/* 101 */       _resolve();
/*     */     }
/* 103 */     return this._bindings.size();
/*     */   }
/*     */ 
/*     */   public JavaType findType(String name)
/*     */   {
/* 108 */     if (this._bindings == null) {
/* 109 */       _resolve();
/*     */     }
/* 111 */     JavaType t = (JavaType)this._bindings.get(name);
/* 112 */     if (t != null) {
/* 113 */       return t;
/*     */     }
/* 115 */     if ((this._placeholders != null) && (this._placeholders.contains(name))) {
/* 116 */       return UNBOUND;
/*     */     }
/*     */ 
/* 119 */     if (this._parentBindings != null)
/* 120 */       return this._parentBindings.findType(name);
/*     */     String className;
/*     */     String className;
/* 126 */     if (this._contextClass != null) {
/* 127 */       className = this._contextClass.getName();
/*     */     }
/*     */     else
/*     */     {
/*     */       String className;
/* 128 */       if (this._contextType != null)
/* 129 */         className = this._contextType.toString();
/*     */       else
/* 131 */         className = "UNKNOWN";
/*     */     }
/* 133 */     throw new IllegalArgumentException("Type variable '" + name + "' can not be resolved (with context of class " + className + ")");
/*     */   }
/*     */ 
/*     */   public void addBinding(String name, JavaType type)
/*     */   {
/* 140 */     if (this._bindings == null) {
/* 141 */       this._bindings = new LinkedHashMap();
/*     */     }
/* 143 */     this._bindings.put(name, type);
/*     */   }
/*     */ 
/*     */   public JavaType[] typesAsArray()
/*     */   {
/* 148 */     if (this._bindings == null) {
/* 149 */       _resolve();
/*     */     }
/* 151 */     if (this._bindings.size() == 0) {
/* 152 */       return NO_TYPES;
/*     */     }
/* 154 */     return (JavaType[])this._bindings.values().toArray(new JavaType[this._bindings.size()]);
/*     */   }
/*     */ 
/*     */   protected void _resolve()
/*     */   {
/* 165 */     _resolveBindings(this._contextClass);
/*     */ 
/* 168 */     if (this._contextType != null) {
/* 169 */       int count = this._contextType.containedTypeCount();
/* 170 */       if (count > 0) {
/* 171 */         if (this._bindings == null) {
/* 172 */           this._bindings = new LinkedHashMap();
/*     */         }
/* 174 */         for (int i = 0; i < count; i++) {
/* 175 */           String name = this._contextType.containedTypeName(i);
/* 176 */           JavaType type = this._contextType.containedType(i);
/* 177 */           this._bindings.put(name, type);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 183 */     if (this._bindings == null)
/* 184 */       this._bindings = Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   public void _addPlaceholder(String name)
/*     */   {
/* 189 */     if (this._placeholders == null) {
/* 190 */       this._placeholders = new HashSet();
/*     */     }
/* 192 */     this._placeholders.add(name);
/*     */   }
/*     */ 
/*     */   protected void _resolveBindings(Type t)
/*     */   {
/* 197 */     if (t == null) return;
/*     */     Class raw;
/* 200 */     if ((t instanceof ParameterizedType)) {
/* 201 */       ParameterizedType pt = (ParameterizedType)t;
/* 202 */       Type[] args = pt.getActualTypeArguments();
/* 203 */       if ((args != null) && (args.length > 0)) {
/* 204 */         Class rawType = (Class)pt.getRawType();
/* 205 */         TypeVariable[] vars = rawType.getTypeParameters();
/* 206 */         if (vars.length != args.length) {
/* 207 */           throw new IllegalArgumentException("Strange parametrized type (in class " + rawType.getName() + "): number of type arguments != number of type parameters (" + args.length + " vs " + vars.length + ")");
/*     */         }
/* 209 */         int i = 0; for (int len = args.length; i < len; i++) {
/* 210 */           TypeVariable var = vars[i];
/* 211 */           String name = var.getName();
/* 212 */           if (this._bindings == null) {
/* 213 */             this._bindings = new LinkedHashMap();
/*     */           }
/*     */           else
/*     */           {
/* 218 */             if (this._bindings.containsKey(name))
/*     */               continue;
/*     */           }
/* 221 */           _addPlaceholder(name);
/*     */ 
/* 223 */           this._bindings.put(name, TypeFactory.instance._fromType(args[i], this));
/*     */         }
/*     */       }
/* 226 */       raw = (Class)pt.getRawType();
/* 227 */     } else if ((t instanceof Class)) {
/* 228 */       Class raw = (Class)t;
/*     */ 
/* 232 */       TypeVariable[] vars = raw.getTypeParameters();
/* 233 */       if ((vars != null) && (vars.length > 0)) {
/* 234 */         for (TypeVariable var : vars) {
/* 235 */           String name = var.getName();
/* 236 */           Type varType = var.getBounds()[0];
/* 237 */           if (varType != null) {
/* 238 */             if (this._bindings == null)
/* 239 */               this._bindings = new LinkedHashMap();
/*     */             else
/* 241 */               if (this._bindings.containsKey(name))
/*     */                 continue;
/* 243 */             _addPlaceholder(name);
/* 244 */             this._bindings.put(name, TypeFactory.instance._fromType(varType, this));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 252 */       return;
/*     */     }
/*     */     Class raw;
/* 255 */     _resolveBindings(raw.getGenericSuperclass());
/* 256 */     for (Type intType : raw.getGenericInterfaces())
/* 257 */       _resolveBindings(intType);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 264 */     if (this._bindings == null) {
/* 265 */       _resolve();
/*     */     }
/* 267 */     StringBuilder sb = new StringBuilder("[TypeBindings for ");
/* 268 */     if (this._contextType != null)
/* 269 */       sb.append(this._contextType.toString());
/*     */     else {
/* 271 */       sb.append(this._contextClass.getName());
/*     */     }
/* 273 */     sb.append(": ").append(this._bindings).append("]");
/* 274 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.TypeBindings
 * JD-Core Version:    0.6.0
 */