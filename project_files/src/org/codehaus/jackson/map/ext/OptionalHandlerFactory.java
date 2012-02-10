/*     */ package org.codehaus.jackson.map.ext;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map.Entry;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializerProvider;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.deser.StdDeserializer;
/*     */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*     */ import org.codehaus.jackson.map.util.Provider;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class OptionalHandlerFactory
/*     */ {
/*     */   private static final String PACKAGE_PREFIX_JODA_DATETIME = "org.joda.time.";
/*     */   private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
/*     */   private static final String SERIALIZERS_FOR_JODA_DATETIME = "org.codehaus.jackson.map.ext.JodaSerializers";
/*     */   private static final String SERIALIZERS_FOR_JAVAX_XML = "org.codehaus.jackson.map.ext.CoreXMLSerializers";
/*     */   private static final String DESERIALIZERS_FOR_JODA_DATETIME = "org.codehaus.jackson.map.ext.JodaDeserializers";
/*     */   private static final String DESERIALIZERS_FOR_JAVAX_XML = "org.codehaus.jackson.map.ext.CoreXMLDeserializers";
/*     */   private static final String CLASS_NAME_DOM_NODE = "org.w3c.dom.Node";
/*     */   private static final String CLASS_NAME_DOM_DOCUMENT = "org.w3c.dom.Node";
/*     */   private static final String SERIALIZER_FOR_DOM_NODE = "org.codehaus.jackson.map.ext.DOMSerializer";
/*     */   private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "org.codehaus.jackson.map.ext.DOMDeserializer$DocumentDeserializer";
/*     */   private static final String DESERIALIZER_FOR_DOM_NODE = "org.codehaus.jackson.map.ext.DOMDeserializer$NodeDeserializer";
/*  42 */   public static final OptionalHandlerFactory instance = new OptionalHandlerFactory();
/*     */ 
/*     */   public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanInfo, BeanProperty property)
/*     */   {
/*  55 */     Class rawType = type.getRawClass();
/*  56 */     String className = rawType.getName();
/*     */     String factoryName;
/*  59 */     if (className.startsWith("org.joda.time.")) {
/*  60 */       factoryName = "org.codehaus.jackson.map.ext.JodaSerializers";
/*     */     }
/*     */     else
/*     */     {
/*     */       String factoryName;
/*  61 */       if ((className.startsWith("javax.xml.")) || (hasSupertypeStartingWith(rawType, "javax.xml.")))
/*     */       {
/*  63 */         factoryName = "org.codehaus.jackson.map.ext.CoreXMLSerializers"; } else {
/*  64 */         if (doesImplement(rawType, "org.w3c.dom.Node")) {
/*  65 */           return (JsonSerializer)instantiate("org.codehaus.jackson.map.ext.DOMSerializer");
/*     */         }
/*  67 */         return null;
/*     */       }
/*     */     }
/*     */     String factoryName;
/*  70 */     Object ob = instantiate(factoryName);
/*  71 */     if (ob == null) {
/*  72 */       return null;
/*     */     }
/*     */ 
/*  75 */     Provider prov = (Provider)ob;
/*  76 */     Collection entries = prov.provide();
/*     */ 
/*  79 */     for (Map.Entry entry : entries) {
/*  80 */       if (rawType == entry.getKey()) {
/*  81 */         return (JsonSerializer)entry.getValue();
/*     */       }
/*     */     }
/*     */ 
/*  85 */     for (Map.Entry entry : entries) {
/*  86 */       if (((Class)entry.getKey()).isAssignableFrom(rawType)) {
/*  87 */         return (JsonSerializer)entry.getValue();
/*     */       }
/*     */     }
/*     */ 
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> findDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider p)
/*     */   {
/*  96 */     Class rawType = type.getRawClass();
/*  97 */     String className = rawType.getName();
/*     */     String factoryName;
/* 100 */     if (className.startsWith("org.joda.time.")) {
/* 101 */       factoryName = "org.codehaus.jackson.map.ext.JodaDeserializers";
/*     */     }
/*     */     else
/*     */     {
/*     */       String factoryName;
/* 102 */       if ((className.startsWith("javax.xml.")) || (hasSupertypeStartingWith(rawType, "javax.xml.")))
/*     */       {
/* 104 */         factoryName = "org.codehaus.jackson.map.ext.CoreXMLDeserializers"; } else {
/* 105 */         if (doesImplement(rawType, "org.w3c.dom.Node"))
/* 106 */           return (JsonDeserializer)instantiate("org.codehaus.jackson.map.ext.DOMDeserializer$DocumentDeserializer");
/* 107 */         if (doesImplement(rawType, "org.w3c.dom.Node")) {
/* 108 */           return (JsonDeserializer)instantiate("org.codehaus.jackson.map.ext.DOMDeserializer$NodeDeserializer");
/*     */         }
/* 110 */         return null;
/*     */       }
/*     */     }
/*     */     String factoryName;
/* 112 */     Object ob = instantiate(factoryName);
/* 113 */     if (ob == null) {
/* 114 */       return null;
/*     */     }
/*     */ 
/* 117 */     Provider prov = (Provider)ob;
/* 118 */     Collection entries = prov.provide();
/*     */ 
/* 121 */     for (StdDeserializer deser : entries) {
/* 122 */       if (rawType == deser.getValueClass()) {
/* 123 */         return deser;
/*     */       }
/*     */     }
/*     */ 
/* 127 */     for (StdDeserializer deser : entries) {
/* 128 */       if (deser.getValueClass().isAssignableFrom(rawType)) {
/* 129 */         return deser;
/*     */       }
/*     */     }
/*     */ 
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   private Object instantiate(String className)
/*     */   {
/*     */     try
/*     */     {
/* 145 */       return Class.forName(className).newInstance();
/*     */     } catch (LinkageError e) {
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   private boolean doesImplement(Class<?> actualType, String classNameToImplement)
/*     */   {
/* 155 */     for (Class type = actualType; type != null; type = type.getSuperclass()) {
/* 156 */       if (type.getName().equals(classNameToImplement)) {
/* 157 */         return true;
/*     */       }
/*     */ 
/* 160 */       if (hasInterface(type, classNameToImplement)) {
/* 161 */         return true;
/*     */       }
/*     */     }
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean hasInterface(Class<?> type, String interfaceToImplement)
/*     */   {
/* 169 */     Class[] interfaces = type.getInterfaces();
/* 170 */     for (Class iface : interfaces) {
/* 171 */       if (iface.getName().equals(interfaceToImplement)) {
/* 172 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 176 */     for (Class iface : interfaces) {
/* 177 */       if (hasInterface(iface, interfaceToImplement)) {
/* 178 */         return true;
/*     */       }
/*     */     }
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean hasSupertypeStartingWith(Class<?> rawType, String prefix)
/*     */   {
/* 187 */     for (Class supertype = rawType.getSuperclass(); supertype != null; supertype = supertype.getSuperclass()) {
/* 188 */       if (supertype.getName().startsWith(prefix)) {
/* 189 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 193 */     for (Class cls = rawType; cls != null; cls = cls.getSuperclass()) {
/* 194 */       if (hasInterfaceStartingWith(cls, prefix)) {
/* 195 */         return true;
/*     */       }
/*     */     }
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean hasInterfaceStartingWith(Class<?> type, String prefix)
/*     */   {
/* 203 */     Class[] interfaces = type.getInterfaces();
/* 204 */     for (Class iface : interfaces) {
/* 205 */       if (iface.getName().startsWith(prefix)) {
/* 206 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 210 */     for (Class iface : interfaces) {
/* 211 */       if (hasInterfaceStartingWith(iface, prefix)) {
/* 212 */         return true;
/*     */       }
/*     */     }
/* 215 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ext.OptionalHandlerFactory
 * JD-Core Version:    0.6.0
 */