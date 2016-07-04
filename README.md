# gwt-scss-resource

[ ![Download](https://api.bintray.com/packages/walter-niklas/maven/gwt-scss-resource/images/download.svg) ](https://bintray.com/walter-niklas/maven/gwt-scss-resource/_latestVersion)
[![Build Status](https://travis-ci.org/TwoStone/gwt-scss-resource.svg?branch=master)](https://travis-ci.org/TwoStone/gwt-scss-resource)

The gwt-scss-resource is a GWT resource implementation for using SASS/SCSS files as resources in GWT ClientBundles.

## Basic usage
For using the scss resource in your gwt project you have to do the following steps:

* add the gwt-scss-resource jar to your gwt classpath.
When you use a build tools with depenedencies management you can get it from [jcenter](https://bintray.com/walter-niklas/maven/gwt-scss-resource)

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
 
## Advanced

### Minify
You can enable the minification of the resulting css by setting the configuration property scss.minify to true

Example:
```xml
<module>
  ...
  <inherits name="com.nwalter.gwt.scss.ScssResource" />

  <set-configuration-property name="scss.minify" value="true"/>
  ...
</module>
 ```

### Include paths
For resolving `@import` statements in your sass files the compiler will look in the directory of the sass file and on the classpath of the gwt-compiler.
If you want to add additional paths to lookup imports you can add this by annotating the resource method in your client bundle.

Example:
```java
public interface MyClientBundle extends ClientBundle {
	
  @IncludePaths({
    "resources/vendor/bootstrap",
    "resources/vendor/ui"
  })
  @Source("style.scss")
  ScssResource myStyle();
  
}
```
