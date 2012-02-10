/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*     */ 
/*     */ public class BeanSerializerBuilder
/*     */ {
/*  19 */   private static final BeanPropertyWriter[] NO_PROPERTIES = new BeanPropertyWriter[0];
/*     */   protected final BasicBeanDescription _beanDesc;
/*     */   protected List<BeanPropertyWriter> _properties;
/*     */   protected BeanPropertyWriter[] _filteredProperties;
/*     */   protected AnyGetterWriter _anyGetter;
/*     */   protected Object _filterId;
/*     */ 
/*     */   public BeanSerializerBuilder(BasicBeanDescription beanDesc)
/*     */   {
/*  63 */     this._beanDesc = beanDesc;
/*     */   }
/*     */ 
/*     */   protected BeanSerializerBuilder(BeanSerializerBuilder src)
/*     */   {
/*  70 */     this._beanDesc = src._beanDesc;
/*  71 */     this._properties = src._properties;
/*  72 */     this._filteredProperties = src._filteredProperties;
/*  73 */     this._anyGetter = src._anyGetter;
/*  74 */     this._filterId = src._filterId;
/*     */   }
/*     */   public BasicBeanDescription getBeanDescription() {
/*  77 */     return this._beanDesc; } 
/*  78 */   public List<BeanPropertyWriter> getProperties() { return this._properties; } 
/*  79 */   public BeanPropertyWriter[] getFilteredProperties() { return this._filteredProperties; }
/*     */ 
/*     */   public void setProperties(List<BeanPropertyWriter> properties) {
/*  82 */     this._properties = properties;
/*     */   }
/*     */ 
/*     */   public void setFilteredProperties(BeanPropertyWriter[] properties) {
/*  86 */     this._filteredProperties = properties;
/*     */   }
/*     */ 
/*     */   public void setAnyGetter(AnyGetterWriter anyGetter) {
/*  90 */     this._anyGetter = anyGetter;
/*     */   }
/*     */ 
/*     */   public void setFilterId(Object filterId) {
/*  94 */     this._filterId = filterId;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<?> build()
/*     */   {
/* 109 */     BeanPropertyWriter[] properties = (this._properties == null) || (this._properties.isEmpty()) ? NO_PROPERTIES : (BeanPropertyWriter[])this._properties.toArray(new BeanPropertyWriter[this._properties.size()]);
/*     */ 
/* 111 */     return new BeanSerializer(this._beanDesc.getType(), properties, this._filteredProperties, this._anyGetter, this._filterId);
/*     */   }
/*     */ 
/*     */   public BeanSerializer createDummy()
/*     */   {
/* 121 */     return BeanSerializer.createDummy(this._beanDesc.getBeanClass());
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.BeanSerializerBuilder
 * JD-Core Version:    0.6.0
 */