# detekt-extensions

This detekt plugin includes custom rules we use at [Medly](https://medly.com)

## Custom Rules

### mutation

**MutationRule**:
Reports usage of mutable variables, collections usages and parameters as code smells
- Mutable variables declared using `var`
- Mutable collections(mutableListOf, mutableSetOf, mutableMapOf)
- Mutable parameters

*Noncompliant Code*:
```
var listOfSquares = mutableListOf()
for(i in 1..10) listOfSquares.add(i * i)
```

*Compliant Code*:
```
val listOfSquares = (1..10).map { it * it }
```

## Usage  
- Import detekt as a gradle, as mentioned [here](https://detekt.github.io/detekt/gradle.html).
- Add *jitpack* in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    detektPlugins "com.medly:detekt-extensions:v0.0.1"
}
```
