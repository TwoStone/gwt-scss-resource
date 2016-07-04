# gwt-scss-resource

The gwt-scss-resource is a GWT resource implementation for using SASS/SCSS files as resources in GWT ClientBundles.

## Usage
For using the scss resource in your gwt project you have to do two things:

* add the gwt-scss-resource jar to your gwt classpath.
When you use a build tools with depenedencies management you can get it from jcenter

Maven:
```xml
<dependency>
  <groupId>com.nwalter.gwt</groupId>
  <artifactId>gwt-scss-resource</artifactId>
  <version>1.2.0</version>
  <type>pom</type>
</dependency>
```

Gradle:
```
dependencies {
    compile 'com.nwalter.gwt:gwt-scss-resource:1.2.0'   
}
```

* add the gwt-scss-resource-module to your module xml 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE module PUBLIC "-//Google Inc.//DTD Google Web Toolkit//EN" "http://gwtproject.org/doctype/2.7.0/gwt-module.dtd">
<module>
  ...
  <inherits name="com.nwalter.gwt.scss.ScssResource" />
  ...
</module>
 ```
 
* use the `ScssResource` interface in your client bundle

```java
public interface MyClientBundle extends ClientBundle {
	
  ScssResource myStyle();
  
}
```
</ol>
 
 
