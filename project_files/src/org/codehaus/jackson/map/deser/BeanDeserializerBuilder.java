/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
/*     */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*     */ 
/*     */ public class BeanDeserializerBuilder
/*     */ {
/*     */   protected final BasicBeanDescription _beanDesc;
/*  36 */   protected final HashMap<String, SettableBeanProperty> _properties = new HashMap();
/*     */   protected HashMap<String, SettableBeanProperty> _backRefProperties;
/*     */   protected HashSet<String> _ignorableProps;
/*     */   protected CreatorContainer _creators;
/*     */   protected SettableAnyProperty _anySetter;
/*     */   protected boolean _ignoreAllUnknown;
/*     */ 
/*     */   public BeanDeserializerBuilder(BasicBeanDescription beanDesc)
/*     */   {
/*  76 */     this._beanDesc = beanDesc;
/*     */   }
/*     */ 
/*     */   public void setCreators(CreatorContainer creators) {
/*  80 */     this._creators = creators;
/*     */   }
/*     */ 
/*     */   public void addOrReplaceProperty(SettableBeanProperty prop, boolean allowOverride)
/*     */   {
/*  88 */     this._properties.put(prop.getName(), prop);
/*     */   }
/*     */ 
/*     */   public void addProperty(SettableBeanProperty prop)
/*     */   {
/*  98 */     SettableBeanProperty old = (SettableBeanProperty)this._properties.put(prop.getName(), prop);
/*  99 */     if ((old != null) && (old != prop))
/* 100 */       throw new IllegalArgumentException("Duplicate property '" + prop.getName() + "' for " + this._beanDesc.getType());
/*     */   }
/*     */ 
/*     */   public void addBackReferenceProperty(String referenceName, SettableBeanProperty prop)
/*     */   {
/* 106 */     if (this._backRefProperties == null) {
/* 107 */       this._backRefProperties = new HashMap(4);
/*     */     }
/* 109 */     this._backRefProperties.put(referenceName, prop);
/*     */   }
/*     */ 
/*     */   public void addIgnorable(String propName)
/*     */   {
/* 118 */     if (this._ignorableProps == null) {
/* 119 */       this._ignorableProps = new HashSet();
/*     */     }
/* 121 */     this._ignorableProps.add(propName);
/*     */   }
/*     */ 
/*     */   public boolean hasProperty(String propertyName) {
/* 125 */     return this._properties.containsKey(propertyName);
/*     */   }
/*     */ 
/*     */   public SettableBeanProperty removeProperty(String name)
/*     */   {
/* 130 */     return (SettableBeanProperty)this._properties.remove(name);
/*     */   }
/*     */ 
/*     */   public void setAnySetter(SettableAnyProperty s)
/*     */   {
/* 135 */     if ((this._anySetter != null) && (s != null)) {
/* 136 */       throw new IllegalStateException("_anySetter already set to non-null");
/*     */     }
/* 138 */     this._anySetter = s;
/*     */   }
/*     */ 
/*     */   public void setIgnoreUnknownProperties(boolean ignore) {
/* 142 */     this._ignoreAllUnknown = ignore;
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> build(BeanProperty forProperty)
/*     */   {
/* 153 */     BeanPropertyMap propertyMap = new BeanPropertyMap(this._properties.values());
/* 154 */     propertyMap.assignIndexes();
/*     */ 
/* 156 */     return new BeanDeserializer(this._beanDesc.getClassInfo(), this._beanDesc.getType(), forProperty, this._creators, propertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, this._anySetter);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BeanDeserializerBuilder
 * JD-Core Version:    0.6.0
 */