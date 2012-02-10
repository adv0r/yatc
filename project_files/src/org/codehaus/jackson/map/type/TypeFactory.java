/*     */ package org.codehaus.jackson.map.type;
/*     */ 
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ import org.codehaus.jackson.type.TypeReference;
/*     */ 
/*     */ public class TypeFactory
/*     */ {
/*  32 */   public static final TypeFactory instance = new TypeFactory();
/*     */ 
/*  34 */   private static final JavaType[] NO_TYPES = new JavaType[0];
/*     */   protected final TypeParser _parser;
/*     */ 
/*     */   private TypeFactory()
/*     */   {
/*  45 */     this._parser = new TypeParser(this);
/*     */   }
/*     */ 
/*     */   public static JavaType type(Type t)
/*     */   {
/*  63 */     return instance._fromType(t, null);
/*     */   }
/*     */ 
/*     */   public static JavaType type(Type type, Class<?> context)
/*     */   {
/*  76 */     return type(type, new TypeBindings(context));
/*     */   }
/*     */ 
/*     */   public static JavaType type(Type type, JavaType context)
/*     */   {
/*  84 */     return type(type, new TypeBindings(context));
/*     */   }
/*     */ 
/*     */   public static JavaType type(Type type, TypeBindings bindings)
/*     */   {
/*  89 */     return instance._fromType(type, bindings);
/*     */   }
/*     */ 
/*     */   public static JavaType type(TypeReference<?> ref)
/*     */   {
/* 100 */     return type(ref.getType());
/*     */   }
/*     */ 
/*     */   public static JavaType arrayType(Class<?> elementType)
/*     */   {
/* 112 */     return arrayType(type(elementType));
/*     */   }
/*     */ 
/*     */   public static JavaType arrayType(JavaType elementType)
/*     */   {
/* 124 */     return ArrayType.construct(elementType);
/*     */   }
/*     */ 
/*     */   public static JavaType collectionType(Class<? extends Collection> collectionType, Class<?> elementType)
/*     */   {
/* 137 */     return collectionType(collectionType, type(elementType));
/*     */   }
/*     */ 
/*     */   public static JavaType collectionType(Class<? extends Collection> collectionType, JavaType elementType)
/*     */   {
/* 150 */     return CollectionType.construct(collectionType, elementType);
/*     */   }
/*     */ 
/*     */   public static JavaType mapType(Class<? extends Map> mapType, Class<?> keyType, Class<?> valueType)
/*     */   {
/* 163 */     return mapType(mapType, type(keyType), type(valueType));
/*     */   }
/*     */ 
/*     */   public static JavaType mapType(Class<? extends Map> mapType, JavaType keyType, JavaType valueType)
/*     */   {
/* 176 */     return MapType.construct(mapType, keyType, valueType);
/*     */   }
/*     */ 
/*     */   public static JavaType parametricType(Class<?> parametrized, Class<?>[] parameterClasses)
/*     */   {
/* 192 */     int len = parameterClasses.length;
/* 193 */     JavaType[] pt = new JavaType[len];
/* 194 */     for (int i = 0; i < len; i++) {
/* 195 */       pt[i] = instance._fromClass(parameterClasses[i], null);
/*     */     }
/* 197 */     return parametricType(parametrized, pt);
/*     */   }
/*     */ 
/*     */   public static JavaType parametricType(Class<?> parametrized, JavaType[] parameterTypes)
/*     */   {
/* 215 */     if (parametrized.isArray())
/*     */     {
/* 217 */       if (parameterTypes.length != 1) {
/* 218 */         throw new IllegalArgumentException("Need exactly 1 parameter type for arrays (" + parametrized.getName() + ")");
/*     */       }
/* 220 */       return ArrayType.construct(parameterTypes[0]);
/*     */     }
/* 222 */     if (Map.class.isAssignableFrom(parametrized)) {
/* 223 */       if (parameterTypes.length != 2) {
/* 224 */         throw new IllegalArgumentException("Need exactly 2 parameter types for Map types (" + parametrized.getName() + ")");
/*     */       }
/* 226 */       return MapType.construct(parametrized, parameterTypes[0], parameterTypes[1]);
/*     */     }
/* 228 */     if (Collection.class.isAssignableFrom(parametrized)) {
/* 229 */       if (parameterTypes.length != 1) {
/* 230 */         throw new IllegalArgumentException("Need exactly 1 parameter type for Collection types (" + parametrized.getName() + ")");
/*     */       }
/* 232 */       return CollectionType.construct(parametrized, parameterTypes[0]);
/*     */     }
/* 234 */     return _constructSimple(parametrized, parameterTypes);
/*     */   }
/*     */ 
/*     */   public static JavaType fromCanonical(String canonical)
/*     */     throws IllegalArgumentException
/*     */   {
/* 252 */     return instance._parser.parse(canonical);
/*     */   }
/*     */ 
/*     */   public static JavaType specialize(JavaType baseType, Class<?> subclass)
/*     */   {
/* 272 */     if ((baseType instanceof SimpleType))
/*     */     {
/* 274 */       if ((subclass.isArray()) || (Map.class.isAssignableFrom(subclass)) || (Collection.class.isAssignableFrom(subclass)))
/*     */       {
/* 278 */         if (!baseType.getRawClass().isAssignableFrom(subclass)) {
/* 279 */           throw new IllegalArgumentException("Class " + subclass.getClass().getName() + " not subtype of " + baseType);
/*     */         }
/*     */ 
/* 282 */         JavaType subtype = instance._fromClass(subclass, new TypeBindings(baseType.getRawClass()));
/*     */ 
/* 284 */         Object h = baseType.getValueHandler();
/* 285 */         if (h != null) {
/* 286 */           subtype.setValueHandler(h);
/*     */         }
/* 288 */         h = baseType.getTypeHandler();
/* 289 */         if (h != null) {
/* 290 */           subtype = subtype.withTypeHandler(h);
/*     */         }
/* 292 */         return subtype;
/*     */       }
/*     */     }
/*     */ 
/* 296 */     return baseType.narrowBy(subclass);
/*     */   }
/*     */ 
/*     */   public static JavaType fastSimpleType(Class<?> cls)
/*     */   {
/* 310 */     return new SimpleType(cls, null, null);
/*     */   }
/*     */ 
/*     */   public static JavaType[] findParameterTypes(Class<?> clz, Class<?> expType)
/*     */   {
/* 324 */     return findParameterTypes(clz, expType, new TypeBindings(clz));
/*     */   }
/*     */ 
/*     */   public static JavaType[] findParameterTypes(Class<?> clz, Class<?> expType, TypeBindings bindings)
/*     */   {
/* 330 */     HierarchicType subType = _findSuperTypeChain(clz, expType);
/*     */ 
/* 332 */     if (subType == null) {
/* 333 */       throw new IllegalArgumentException("Class " + clz.getName() + " is not a subtype of " + expType.getName());
/*     */     }
/*     */ 
/* 336 */     HierarchicType superType = subType;
/* 337 */     while (superType.getSuperType() != null) {
/* 338 */       superType = superType.getSuperType();
/* 339 */       Class raw = superType.getRawClass();
/* 340 */       TypeBindings newBindings = new TypeBindings(raw);
/* 341 */       if (superType.isGeneric()) {
/* 342 */         ParameterizedType pt = superType.asGeneric();
/* 343 */         Type[] actualTypes = pt.getActualTypeArguments();
/* 344 */         TypeVariable[] vars = raw.getTypeParameters();
/* 345 */         int len = actualTypes.length;
/* 346 */         for (int i = 0; i < len; i++) {
/* 347 */           String name = vars[i].getName();
/* 348 */           JavaType type = instance._fromType(actualTypes[i], bindings);
/* 349 */           newBindings.addBinding(name, type);
/*     */         }
/*     */       }
/* 352 */       bindings = newBindings;
/*     */     }
/*     */ 
/* 356 */     if (!superType.isGeneric()) {
/* 357 */       return null;
/*     */     }
/* 359 */     return bindings.typesAsArray();
/*     */   }
/*     */ 
/*     */   public static JavaType[] findParameterTypes(JavaType type, Class<?> expType)
/*     */   {
/* 383 */     Class raw = type.getRawClass();
/* 384 */     if (raw == expType)
/*     */     {
/* 386 */       int count = type.containedTypeCount();
/* 387 */       if (count == 0) return null;
/* 388 */       JavaType[] result = new JavaType[count];
/* 389 */       for (int i = 0; i < count; i++) {
/* 390 */         result[i] = type.containedType(i);
/*     */       }
/* 392 */       return result;
/*     */     }
/*     */ 
/* 400 */     return findParameterTypes(raw, expType, new TypeBindings(type));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static JavaType fromClass(Class<?> clz)
/*     */   {
/* 423 */     return instance._fromClass(clz, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static JavaType fromTypeReference(TypeReference<?> ref)
/*     */   {
/* 437 */     return type(ref.getType());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static JavaType fromType(Type type)
/*     */   {
/* 450 */     return instance._fromType(type, null);
/*     */   }
/*     */ 
/*     */   protected JavaType _fromClass(Class<?> clz, TypeBindings context)
/*     */   {
/* 466 */     if (clz.isArray()) {
/* 467 */       return ArrayType.construct(_fromType(clz.getComponentType(), null));
/*     */     }
/*     */ 
/* 472 */     if (clz.isEnum()) {
/* 473 */       return new SimpleType(clz);
/*     */     }
/*     */ 
/* 479 */     if (Map.class.isAssignableFrom(clz)) {
/* 480 */       return _mapType(clz);
/*     */     }
/* 482 */     if (Collection.class.isAssignableFrom(clz)) {
/* 483 */       return _collectionType(clz);
/*     */     }
/* 485 */     return new SimpleType(clz);
/*     */   }
/*     */ 
/*     */   protected JavaType _fromParameterizedClass(Class<?> clz, List<JavaType> paramTypes)
/*     */   {
/* 494 */     if (clz.isArray()) {
/* 495 */       return ArrayType.construct(_fromType(clz.getComponentType(), null));
/*     */     }
/* 497 */     if (clz.isEnum()) {
/* 498 */       return new SimpleType(clz);
/*     */     }
/* 500 */     if (Map.class.isAssignableFrom(clz))
/*     */     {
/* 503 */       if (paramTypes.size() > 0) {
/* 504 */         JavaType keyType = (JavaType)paramTypes.get(0);
/* 505 */         JavaType contentType = paramTypes.size() >= 2 ? (JavaType)paramTypes.get(1) : _unknownType();
/*     */ 
/* 507 */         return MapType.construct(clz, keyType, contentType);
/*     */       }
/* 509 */       return _mapType(clz);
/*     */     }
/* 511 */     if (Collection.class.isAssignableFrom(clz)) {
/* 512 */       if (paramTypes.size() >= 1) {
/* 513 */         return CollectionType.construct(clz, (JavaType)paramTypes.get(0));
/*     */       }
/* 515 */       return _collectionType(clz);
/*     */     }
/* 517 */     if (paramTypes.size() == 0) {
/* 518 */       return new SimpleType(clz);
/*     */     }
/* 520 */     JavaType[] pt = (JavaType[])paramTypes.toArray(new JavaType[paramTypes.size()]);
/* 521 */     return _constructSimple(clz, pt);
/*     */   }
/*     */ 
/*     */   public JavaType _fromType(Type type, TypeBindings context)
/*     */   {
/* 532 */     if ((type instanceof Class)) {
/* 533 */       Class cls = (Class)type;
/*     */ 
/* 537 */       if (context == null) {
/* 538 */         context = new TypeBindings(cls);
/*     */       }
/* 540 */       return _fromClass(cls, context);
/*     */     }
/*     */ 
/* 543 */     if ((type instanceof ParameterizedType)) {
/* 544 */       return _fromParamType((ParameterizedType)type, context);
/*     */     }
/* 546 */     if ((type instanceof GenericArrayType)) {
/* 547 */       return _fromArrayType((GenericArrayType)type, context);
/*     */     }
/* 549 */     if ((type instanceof TypeVariable)) {
/* 550 */       return _fromVariable((TypeVariable)type, context);
/*     */     }
/* 552 */     if ((type instanceof WildcardType)) {
/* 553 */       return _fromWildcard((WildcardType)type, context);
/*     */     }
/*     */ 
/* 556 */     throw new IllegalArgumentException("Unrecognized Type: " + type.toString());
/*     */   }
/*     */ 
/*     */   protected JavaType _fromParamType(ParameterizedType type, TypeBindings context)
/*     */   {
/* 574 */     Class rawType = (Class)type.getRawType();
/* 575 */     Type[] args = type.getActualTypeArguments();
/* 576 */     int paramCount = args == null ? 0 : args.length;
/*     */     JavaType[] pt;
/*     */     JavaType[] pt;
/* 580 */     if (paramCount == 0) {
/* 581 */       pt = NO_TYPES;
/*     */     } else {
/* 583 */       pt = new JavaType[paramCount];
/* 584 */       for (int i = 0; i < paramCount; i++) {
/* 585 */         pt[i] = _fromType(args[i], context);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 590 */     if (Map.class.isAssignableFrom(rawType)) {
/* 591 */       JavaType subtype = _constructSimple(rawType, pt);
/* 592 */       JavaType[] mapParams = findParameterTypes(subtype, Map.class);
/* 593 */       if (mapParams.length != 2) {
/* 594 */         throw new IllegalArgumentException("Could not find 2 type parameters for Map class " + rawType.getName() + " (found " + mapParams.length + ")");
/*     */       }
/* 596 */       return MapType.construct(rawType, mapParams[0], mapParams[1]);
/*     */     }
/* 598 */     if (Collection.class.isAssignableFrom(rawType)) {
/* 599 */       JavaType subtype = _constructSimple(rawType, pt);
/* 600 */       JavaType[] collectionParams = findParameterTypes(subtype, Collection.class);
/* 601 */       if (collectionParams.length != 1) {
/* 602 */         throw new IllegalArgumentException("Could not find 1 type parameter for Collection class " + rawType.getName() + " (found " + collectionParams.length + ")");
/*     */       }
/* 604 */       return CollectionType.construct(rawType, collectionParams[0]);
/*     */     }
/* 606 */     if (paramCount == 0) {
/* 607 */       return new SimpleType(rawType);
/*     */     }
/* 609 */     return _constructSimple(rawType, pt);
/*     */   }
/*     */ 
/*     */   protected static SimpleType _constructSimple(Class<?> rawType, JavaType[] parameterTypes)
/*     */   {
/* 615 */     TypeVariable[] typeVars = rawType.getTypeParameters();
/* 616 */     if (typeVars.length != parameterTypes.length) {
/* 617 */       throw new IllegalArgumentException("Parameter type mismatch for " + rawType.getName() + ": expected " + typeVars.length + " parameters, was given " + parameterTypes.length);
/*     */     }
/*     */ 
/* 620 */     String[] names = new String[typeVars.length];
/* 621 */     int i = 0; for (int len = typeVars.length; i < len; i++) {
/* 622 */       names[i] = typeVars[i].getName();
/*     */     }
/* 624 */     return new SimpleType(rawType, names, parameterTypes);
/*     */   }
/*     */ 
/*     */   protected JavaType _fromArrayType(GenericArrayType type, TypeBindings context)
/*     */   {
/* 629 */     JavaType compType = _fromType(type.getGenericComponentType(), context);
/* 630 */     return ArrayType.construct(compType);
/*     */   }
/*     */ 
/*     */   protected JavaType _fromVariable(TypeVariable<?> type, TypeBindings context)
/*     */   {
/* 639 */     if (context == null) {
/* 640 */       return _unknownType();
/*     */     }
/*     */ 
/* 644 */     String name = type.getName();
/* 645 */     JavaType actualType = context.findType(name);
/* 646 */     if (actualType != null) {
/* 647 */       return actualType;
/*     */     }
/*     */ 
/* 655 */     Type[] bounds = type.getBounds();
/*     */ 
/* 670 */     context._addPlaceholder(name);
/* 671 */     return _fromType(bounds[0], context);
/*     */   }
/*     */ 
/*     */   protected JavaType _fromWildcard(WildcardType type, TypeBindings context)
/*     */   {
/* 684 */     return _fromType(type.getUpperBounds()[0], context);
/*     */   }
/*     */ 
/*     */   private JavaType _mapType(Class<?> rawClass)
/*     */   {
/* 689 */     JavaType[] typeParams = findParameterTypes(rawClass, Map.class);
/*     */ 
/* 691 */     if (typeParams == null) {
/* 692 */       return MapType.construct(rawClass, _unknownType(), _unknownType());
/*     */     }
/*     */ 
/* 695 */     if (typeParams.length != 2) {
/* 696 */       throw new IllegalArgumentException("Strange Map type " + rawClass.getName() + ": can not determine type parameters");
/*     */     }
/* 698 */     return MapType.construct(rawClass, typeParams[0], typeParams[1]);
/*     */   }
/*     */ 
/*     */   private JavaType _collectionType(Class<?> rawClass)
/*     */   {
/* 703 */     JavaType[] typeParams = findParameterTypes(rawClass, Collection.class);
/*     */ 
/* 705 */     if (typeParams == null) {
/* 706 */       return CollectionType.construct(rawClass, _unknownType());
/*     */     }
/*     */ 
/* 709 */     if (typeParams.length != 1) {
/* 710 */       throw new IllegalArgumentException("Strange Collection type " + rawClass.getName() + ": can not determine type parameters");
/*     */     }
/* 712 */     return CollectionType.construct(rawClass, typeParams[0]);
/*     */   }
/*     */ 
/*     */   protected static JavaType _resolveVariableViaSubTypes(HierarchicType leafType, String variableName, TypeBindings bindings)
/*     */   {
/* 718 */     if ((leafType != null) && (leafType.isGeneric())) {
/* 719 */       TypeVariable[] typeVariables = leafType.getRawClass().getTypeParameters();
/* 720 */       int i = 0; for (int len = typeVariables.length; i < len; i++) {
/* 721 */         TypeVariable tv = typeVariables[i];
/* 722 */         if (!variableName.equals(tv.getName()))
/*     */           continue;
/* 724 */         Type type = leafType.asGeneric().getActualTypeArguments()[i];
/* 725 */         if ((type instanceof TypeVariable)) {
/* 726 */           return _resolveVariableViaSubTypes(leafType.getSubType(), ((TypeVariable)type).getName(), bindings);
/*     */         }
/*     */ 
/* 729 */         return instance._fromType(type, bindings);
/*     */       }
/*     */     }
/*     */ 
/* 733 */     return instance._unknownType();
/*     */   }
/*     */ 
/*     */   protected JavaType _unknownType() {
/* 737 */     return _fromClass(Object.class, null);
/*     */   }
/*     */ 
/*     */   protected static HierarchicType _findSuperTypeChain(Class<?> subtype, Class<?> supertype)
/*     */   {
/* 749 */     if (supertype.isInterface()) {
/* 750 */       return _findSuperInterfaceChain(subtype, supertype);
/*     */     }
/* 752 */     return _findSuperClassChain(subtype, supertype);
/*     */   }
/*     */ 
/*     */   protected static HierarchicType _findSuperClassChain(Type currentType, Class<?> target)
/*     */   {
/* 757 */     HierarchicType current = new HierarchicType(currentType);
/* 758 */     Class raw = current.getRawClass();
/* 759 */     if (raw == target) {
/* 760 */       return current;
/*     */     }
/*     */ 
/* 763 */     Type parent = raw.getGenericSuperclass();
/* 764 */     if (parent != null) {
/* 765 */       HierarchicType sup = _findSuperClassChain(parent, target);
/* 766 */       if (sup != null) {
/* 767 */         sup.setSubType(current);
/* 768 */         current.setSuperType(sup);
/* 769 */         return current;
/*     */       }
/*     */     }
/* 772 */     return null;
/*     */   }
/*     */ 
/*     */   protected static HierarchicType _findSuperInterfaceChain(Type currentType, Class<?> target)
/*     */   {
/* 777 */     HierarchicType current = new HierarchicType(currentType);
/* 778 */     Class raw = current.getRawClass();
/* 779 */     if (raw == target) {
/* 780 */       return current;
/*     */     }
/*     */ 
/* 783 */     Type[] parents = raw.getGenericInterfaces();
/*     */ 
/* 786 */     if (parents != null) {
/* 787 */       for (Type parent : parents) {
/* 788 */         HierarchicType sup = _findSuperInterfaceChain(parent, target);
/* 789 */         if (sup != null) {
/* 790 */           sup.setSubType(current);
/* 791 */           current.setSuperType(sup);
/* 792 */           return current;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 797 */     Type parent = raw.getGenericSuperclass();
/* 798 */     if (parent != null) {
/* 799 */       HierarchicType sup = _findSuperInterfaceChain(parent, target);
/* 800 */       if (sup != null) {
/* 801 */         sup.setSubType(current);
/* 802 */         current.setSuperType(sup);
/* 803 */         return current;
/*     */       }
/*     */     }
/* 806 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.TypeFactory
 * JD-Core Version:    0.6.0
 */