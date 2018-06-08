# IconicsFontGenerator

Code generator for [Android-Iconics Library](https://github.com/mikepenz/Android-Iconics)

## Usage

This plugin is available from [Bintray's JCenter repository](http://jcenter.bintray.com). You can
add it to your top-level build script using the following configuration:

### `plugins` block:

```groovy
plugins {
  id "fr.ligol.iconics_generator" version "$version"
}
```
or via the

### `buildscript` block:
```groovy
apply plugin: "fr.ligol.iconics_generator"

buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }

  }

  dependencies {
    classpath "gradle.plugin.fr.ligol:gradle-iconics-generator:$version"
  }
}
```

The current version (1.0-alpha1) is known to work with Gradle versions up to 4.5.

## Configuration

```
iconics {
    fonstaticApiKey = "MY FONTASTIC API KEY"
    name = "name e.g. 'Google Material'" default: "iconics"
    code = "code e.g. 'gmd'" default: "ico"
    versionName = "versionname e.g. '2.1.0.1'" default: "1.0.0"
    versionCode = "versioncode e.g. '21001'" default: "1"
    author = "author e.g. 'Google'" default: ""
    url = "url e.g. '...'" default: ""
    description = "description e.g. '...'" default: ""
    license = "license e.g. '...'" default: ""
    licenseUrl = "licenseurl e.g. '...'" default: ""
}
```

## Tasks
### `generateIconicsProject`
Generate the module named `config.name-font` in the root folder of your project.

When running for the first time you need to add the module name to your settings.gradle like
`include ':app', 'config.name-font'`

Re-run the task to regenerate the module with the last fontastic update.

# Credits
- [Mike Penz](https://github.com/mikepenz) He is the creator of [Android-Iconics](https://github.com/mikepenz/Android-Iconics)

# Developed By

* Baptiste Lagache

# License

    Copyright 2018 Baptiste Lagache

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
