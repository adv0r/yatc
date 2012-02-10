/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import org.codehaus.jackson.annotate.JsonAutoDetect;
/*     */ import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/*     */ import org.codehaus.jackson.annotate.JsonMethod;
/*     */ 
/*     */ public abstract interface VisibilityChecker<T extends VisibilityChecker<T>>
/*     */ {
/*     */   public abstract T with(JsonAutoDetect paramJsonAutoDetect);
/*     */ 
/*     */   public abstract T withGetterVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */ 
/*     */   public abstract T withIsGetterVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */ 
/*     */   public abstract T withSetterVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */ 
/*     */   public abstract T withCreatorVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */ 
/*     */   public abstract T withFieldVisibility(JsonAutoDetect.Visibility paramVisibility);
/*     */ 
/*     */   public abstract boolean isGetterVisible(Method paramMethod);
/*     */ 
/*     */   public abstract boolean isGetterVisible(AnnotatedMethod paramAnnotatedMethod);
/*     */ 
/*     */   public abstract boolean isIsGetterVisible(Method paramMethod);
/*     */ 
/*     */   public abstract boolean isIsGetterVisible(AnnotatedMethod paramAnnotatedMethod);
/*     */ 
/*     */   public abstract boolean isSetterVisible(Method paramMethod);
/*     */ 
/*     */   public abstract boolean isSetterVisible(AnnotatedMethod paramAnnotatedMethod);
/*     */ 
/*     */   public abstract boolean isCreatorVisible(Member paramMember);
/*     */ 
/*     */   public abstract boolean isCreatorVisible(AnnotatedMember paramAnnotatedMember);
/*     */ 
/*     */   public abstract boolean isFieldVisible(Field paramField);
/*     */ 
/*     */   public abstract boolean isFieldVisible(AnnotatedField paramAnnotatedField);
/*     */ 
/*     */   @JsonAutoDetect(getterVisibility=JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility=JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility=JsonAutoDetect.Visibility.ANY, creatorVisibility=JsonAutoDetect.Visibility.ANY, fieldVisibility=JsonAutoDetect.Visibility.PUBLIC_ONLY)
/*     */   public static class Std
/*     */     implements VisibilityChecker<Std>
/*     */   {
/* 145 */     protected static final Std DEFAULT = new Std((JsonAutoDetect)Std.class.getAnnotation(JsonAutoDetect.class));
/*     */     protected final JsonAutoDetect.Visibility _getterMinLevel;
/*     */     protected final JsonAutoDetect.Visibility _isGetterMinLevel;
/*     */     protected final JsonAutoDetect.Visibility _setterMinLevel;
/*     */     protected final JsonAutoDetect.Visibility _creatorMinLevel;
/*     */     protected final JsonAutoDetect.Visibility _fieldMinLevel;
/*     */ 
/*     */     public static Std defaultInstance()
/*     */     {
/* 153 */       return DEFAULT;
/*     */     }
/*     */ 
/*     */     public Std(JsonAutoDetect ann)
/*     */     {
/* 163 */       JsonMethod[] incl = ann.value();
/*     */ 
/* 165 */       this._getterMinLevel = (hasMethod(incl, JsonMethod.GETTER) ? ann.getterVisibility() : JsonAutoDetect.Visibility.NONE);
/* 166 */       this._isGetterMinLevel = (hasMethod(incl, JsonMethod.IS_GETTER) ? ann.isGetterVisibility() : JsonAutoDetect.Visibility.NONE);
/* 167 */       this._setterMinLevel = (hasMethod(incl, JsonMethod.SETTER) ? ann.setterVisibility() : JsonAutoDetect.Visibility.NONE);
/* 168 */       this._creatorMinLevel = (hasMethod(incl, JsonMethod.CREATOR) ? ann.creatorVisibility() : JsonAutoDetect.Visibility.NONE);
/* 169 */       this._fieldMinLevel = (hasMethod(incl, JsonMethod.FIELD) ? ann.fieldVisibility() : JsonAutoDetect.Visibility.NONE);
/*     */     }
/*     */ 
/*     */     public Std(JsonAutoDetect.Visibility getter, JsonAutoDetect.Visibility isGetter, JsonAutoDetect.Visibility setter, JsonAutoDetect.Visibility creator, JsonAutoDetect.Visibility field)
/*     */     {
/* 177 */       this._getterMinLevel = getter;
/* 178 */       this._isGetterMinLevel = isGetter;
/* 179 */       this._setterMinLevel = setter;
/* 180 */       this._creatorMinLevel = creator;
/* 181 */       this._fieldMinLevel = field;
/*     */     }
/*     */ 
/*     */     public Std with(JsonAutoDetect ann)
/*     */     {
/* 193 */       if (ann == null) return this;
/* 194 */       Std curr = this;
/*     */ 
/* 196 */       JsonMethod[] incl = ann.value();
/*     */ 
/* 199 */       JsonAutoDetect.Visibility v = hasMethod(incl, JsonMethod.GETTER) ? ann.getterVisibility() : JsonAutoDetect.Visibility.NONE;
/* 200 */       curr = curr.withGetterVisibility(v);
/* 201 */       v = hasMethod(incl, JsonMethod.IS_GETTER) ? ann.isGetterVisibility() : JsonAutoDetect.Visibility.NONE;
/* 202 */       curr = curr.withIsGetterVisibility(v);
/* 203 */       v = hasMethod(incl, JsonMethod.SETTER) ? ann.setterVisibility() : JsonAutoDetect.Visibility.NONE;
/* 204 */       curr = curr.withSetterVisibility(v);
/* 205 */       v = hasMethod(incl, JsonMethod.CREATOR) ? ann.creatorVisibility() : JsonAutoDetect.Visibility.NONE;
/* 206 */       curr = curr.withCreatorVisibility(v);
/* 207 */       v = hasMethod(incl, JsonMethod.FIELD) ? ann.fieldVisibility() : JsonAutoDetect.Visibility.NONE;
/* 208 */       curr = curr.withFieldVisibility(v);
/* 209 */       return curr;
/*     */     }
/*     */ 
/*     */     public Std withGetterVisibility(JsonAutoDetect.Visibility v) {
/* 213 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._getterMinLevel;
/* 214 */       if (this._getterMinLevel == v) return this;
/* 215 */       return new Std(v, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
/*     */     }
/*     */ 
/*     */     public Std withIsGetterVisibility(JsonAutoDetect.Visibility v) {
/* 219 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._isGetterMinLevel;
/* 220 */       if (this._isGetterMinLevel == v) return this;
/* 221 */       return new Std(this._getterMinLevel, v, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
/*     */     }
/*     */ 
/*     */     public Std withSetterVisibility(JsonAutoDetect.Visibility v) {
/* 225 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._setterMinLevel;
/* 226 */       if (this._setterMinLevel == v) return this;
/* 227 */       return new Std(this._getterMinLevel, this._isGetterMinLevel, v, this._creatorMinLevel, this._fieldMinLevel);
/*     */     }
/*     */ 
/*     */     public Std withCreatorVisibility(JsonAutoDetect.Visibility v) {
/* 231 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._creatorMinLevel;
/* 232 */       if (this._creatorMinLevel == v) return this;
/* 233 */       return new Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, v, this._fieldMinLevel);
/*     */     }
/*     */ 
/*     */     public Std withFieldVisibility(JsonAutoDetect.Visibility v) {
/* 237 */       if (v == JsonAutoDetect.Visibility.DEFAULT) v = DEFAULT._fieldMinLevel;
/* 238 */       if (this._fieldMinLevel == v) return this;
/* 239 */       return new Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, v);
/*     */     }
/*     */ 
/*     */     public boolean isCreatorVisible(Member m)
/*     */     {
/* 249 */       return this._creatorMinLevel.isVisible(m);
/*     */     }
/*     */ 
/*     */     public boolean isCreatorVisible(AnnotatedMember m) {
/* 253 */       return isCreatorVisible(m.getMember());
/*     */     }
/*     */ 
/*     */     public boolean isFieldVisible(Field f) {
/* 257 */       return this._fieldMinLevel.isVisible(f);
/*     */     }
/*     */ 
/*     */     public boolean isFieldVisible(AnnotatedField f) {
/* 261 */       return isFieldVisible(f.getAnnotated());
/*     */     }
/*     */ 
/*     */     public boolean isGetterVisible(Method m) {
/* 265 */       return this._getterMinLevel.isVisible(m);
/*     */     }
/*     */     public boolean isGetterVisible(AnnotatedMethod m) {
/* 268 */       return isGetterVisible(m.getAnnotated());
/*     */     }
/*     */ 
/*     */     public boolean isIsGetterVisible(Method m) {
/* 272 */       return this._isGetterMinLevel.isVisible(m);
/*     */     }
/*     */ 
/*     */     public boolean isIsGetterVisible(AnnotatedMethod m) {
/* 276 */       return isIsGetterVisible(m.getAnnotated());
/*     */     }
/*     */ 
/*     */     public boolean isSetterVisible(Method m) {
/* 280 */       return this._setterMinLevel.isVisible(m);
/*     */     }
/*     */ 
/*     */     public boolean isSetterVisible(AnnotatedMethod m) {
/* 284 */       return isSetterVisible(m.getAnnotated());
/*     */     }
/*     */ 
/*     */     private static boolean hasMethod(JsonMethod[] methods, JsonMethod method)
/*     */     {
/* 295 */       for (JsonMethod curr : methods) {
/* 296 */         if ((curr == method) || (curr == JsonMethod.ALL)) return true;
/*     */       }
/* 298 */       return false;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 308 */       return "[Visibility:" + " getter: " + this._getterMinLevel + ", isGetter: " + this._isGetterMinLevel + ", setter: " + this._setterMinLevel + ", creator: " + this._creatorMinLevel + ", field: " + this._fieldMinLevel + "]";
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.VisibilityChecker
 * JD-Core Version:    0.6.0
 */