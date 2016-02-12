# SunDatePicker
Date picker for Iranian calendar 

## Screenshots

<img src="/Preview.JPG"/>


##Getting started

### Dependency

```
dependencies {
    compile 'com.alirezaafkar:sundatepicker:2.0.0'
}
```

### Usage

```java
 new DatePicker.Builder()
            .id(id)
            .theme(theme)
            .date(initialDate)
            .build(MainActivity.this)
            .show(getSupportFragmentManager(), "");
```

#Licence

    Copyright 2016 Alireza Afkar
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
