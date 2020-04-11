<p align="center">
  <a href="" rel="noopener">
 <img width=150px height=150px src="https://cdn.cleymax.fr/clibrary.png" alt="CLibrary logo"></a>
</p>

<div align="center">

  [![Java CI with Maven](https://github.com/Cleymax/CLibrary/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/Cleymax/CLibrary/actions)
  [![GitHub stars](https://img.shields.io/github/stars/Cleymax/CLibrary.svg)](https://github.com/Cleymax/CLibrary/stargazers)
  [![GitHub Issues](https://img.shields.io/github/issues/Cleymax/CLibrary.svg)](https://github.com/Cleymax/CLibrary/issues)
  [![GitHub Pull Requests](https://img.shields.io/github/issues-pr/Cleymax/CLibrary.svg)](https://github.com/Cleymax/CLibrary/pulls)
  [![GitHub license](https://img.shields.io/github/license/Cleymax/CLibrary)](/LICENSE)

</div>

---

<p align="center"> Api to download and load the necessary dependencies into the classloader.
    <br>
</p>

## 📝 Table of Contents
- [About](#about)
- [Getting Started](#getting_started)
- [Authors](#authors)
- [Build Using](#built_using)
- [License](#license)

## 🧐 About <a name = "about"></a>

This library is used to not have too large jar files. The dependencies are downloaded at startup if they have not yet been downloaded otherwise they are downloaded from the central repository of maven or a selected repository.

## 🏁 Getting Started <a name = "getting_started"></a>
These instructions could be use in any type of java project.

First you need to retrieve CLibrary.

### Prerequisites

You can download the jar file in the releases. [![GitHub release (latest by date)](https://img.shields.io/github/v/release/Cleymax/CLibrary)](https://github.com/Cleymax/CLibrary/releases)

Or get it from github packages: [https://github.com/Cleymax/CLibrary/packages](https://github.com/Cleymax/CLibrary/packages)

Or get it from my personal repository:

- Maven

```xml
<!-- Add to repositories -->
<repository>
    <id>cleymax-releases</id>
    <url>https://repo.cleymax.fr/repository/maven-releases/</url>
</repository>

<!-- Add to dependencies -->
<dependency>
    <groupId>fr.cleymax</groupId>
    <artifactId>CLibrary</artifactId>
    <version>1.0.1</version>
    <scope>compile</scope>
</dependency>
```

- Gradle
```groovy
// Add to repositories
maven {
    name = 'cleymax-releases'
    url = 'https://repo.cleymax.fr/repository/maven-releases/'
}

// Add to dependencies
compile 'fr.cleymax:CLibrary:1.0.1'
```

### Use

Example, you need [Gson](https://github.com/google/gson) in your project.  Add the annotation `@Dependency` on any class to load [Gson](https://github.com/google/gson) .

```java
import fr.cleymax.clibrary.CLibrary;
import fr.cleymax.clibrary.Dependency;

@Dependency(groupId = "com.google.code.gson", artifactId = "gson", version = "2.8.6")
public class Main {

  public static void main(String[] args)
  {
    final CLibrary library = new CLibrary(Main.class);  //Initialize a new instance of `CLibrary`.
    library.loads(); //Load all dependencies of the class that is set as a parameter when initializing the `CLibrary' instance.
  }
}
```

You can choose where the dependencies are stored and downloaded.
```java
final CLibrary library = new CLibrary(Main.class, new File("dependancies/"));
```

You can initialize an instance of `CLibrary` without any parameters. But the method `CLibrary#loads()` can no longer be used!
```java
final CLibrary library = new CLibrary();
```

You can choose the repository or you have to download the dependency.
*Note that if the file cannot be uploaded with the custom repository then it will try to upload to maven's central repository.*
```java
@Repository(url = "https://repo.cleymax.fr/repository/maven-releases/")
@Dependency(groupId = "com.google.code.gson", artifactId = "gson", version = "2.8.6")
```

## ✍️ Authors <a name = "authors"></a>
- [@Cleymax](https://github.com/Cleymax) - Idea & Initial work

See also the list of [contributors](https://github.com/kylelobo/The-Documentation-Compendium/contributors) who participated in this project.

## ⛏️ Built Using <a name = "built_using"></a>
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) - IDE
- [Maven](https://maven.apache.org/) - Project Management

## 🎉 License <a name = "license"></a>
CLibrary is licensed under the [MIT license](/LICENSE).