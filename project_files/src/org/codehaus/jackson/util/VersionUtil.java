/*    */ package org.codehaus.jackson.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.regex.Pattern;
/*    */ import org.codehaus.jackson.Version;
/*    */ 
/*    */ public class VersionUtil
/*    */ {
/*    */   public static final String VERSION_FILE = "VERSION.txt";
/* 17 */   private static final Pattern VERSION_SEPARATOR = Pattern.compile("[-_./;:]");
/*    */ 
/*    */   public static Version versionFor(Class<?> cls)
/*    */   {
/* 30 */     Version version = null;
/*    */     try
/*    */     {
/* 33 */       InputStream in = cls.getResourceAsStream("VERSION.txt");
/* 34 */       if (in != null)
/*    */         try {
/* 36 */           BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
/* 37 */           version = parseVersion(br.readLine());
/*    */         } finally {
/*    */           try {
/* 40 */             in.close();
/*    */           } catch (IOException e) {
/* 42 */             throw new RuntimeException(e);
/*    */           }
/*    */         }
/*    */     } catch (IOException e) {
/*    */     }
/* 47 */     return version == null ? Version.unknownVersion() : version;
/*    */   }
/*    */ 
/*    */   public static Version parseVersion(String versionStr)
/*    */   {
/* 52 */     if (versionStr == null) return null;
/* 53 */     versionStr = versionStr.trim();
/* 54 */     if (versionStr.length() == 0) return null;
/* 55 */     String[] parts = VERSION_SEPARATOR.split(versionStr);
/*    */ 
/* 57 */     if (parts.length < 2) {
/* 58 */       return null;
/*    */     }
/* 60 */     int major = parseVersionPart(parts[0]);
/* 61 */     int minor = parseVersionPart(parts[1]);
/* 62 */     int patch = parts.length > 2 ? parseVersionPart(parts[2]) : 0;
/* 63 */     String snapshot = parts.length > 3 ? parts[3] : null;
/* 64 */     return new Version(major, minor, patch, snapshot);
/*    */   }
/*    */ 
/*    */   protected static int parseVersionPart(String partStr)
/*    */   {
/* 69 */     partStr = partStr.toString();
/* 70 */     int len = partStr.length();
/* 71 */     int number = 0;
/* 72 */     for (int i = 0; i < len; i++) {
/* 73 */       char c = partStr.charAt(i);
/* 74 */       if ((c > '9') || (c < '0')) break;
/* 75 */       number = number * 10 + (c - '0');
/*    */     }
/* 77 */     return number;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.VersionUtil
 * JD-Core Version:    0.6.0
 */