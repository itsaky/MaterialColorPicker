# MaterialColorPicker
[![license](https://img.shields.io/github/license/itsaky/MaterialColorPicker.svg?style=flat-square)](https://github.com/itsaky/MaterialColorPicker/blob/master/LICENSE) [![](https://jitpack.io/v/itsaky/MaterialColorPicker.svg)](https://jitpack.io/#itsaky/MaterialColorPicker)

MaterialColorPicker made for Android. Written in Java with [AIDE](https://play.google.com/store/apps/details?id=com.aide.ui)

## Download

Add it in your root build.gradle at the end of repositories:
```
allprojects { 		
    repositories { 			
        ... 			
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency
```
dependencies {
    implementation 'com.github.itsaky:MaterialColorPicker:<lastest-version>'
}
```

## Usage
- **1. Create a ColorPickerDialog instance using one of the following methods :**

  - **a.**
  ```java
  // Alpha slider will be invisible
  ColorPickerDialog view = new ColorPickerDialog(this);
  ```

  - **b.**
  ```java
  // Alpha slider will be visible
  ColorPickerDialog view = new ColorPickerDialog(this, "#f44336");
  ```

  - **c.** 
  ```java
  //Alpha slider will be visible
  ColorPickerDialog view = new ColorPickerDialog(this, /*alpha*/255, /*red*/ 255, /*green*/ 255, /*blue*/ 255);
  ```

  - **d.** 
  ```java
  // Alpha slider will be invisible
  ColorPickerDialog view = new ColorPickerDialog(this, /*red*/ 255, /*green*/ 255, /*blue*/ 255);
  ```

- **2. Modify values**
  ```java
  // Setters
  view.setAlpha(255);
  view.setRed(255);
  view.setGreen(255);
  view.setBlue(255);
  view.withAlpha(true); // enables alpha slider
  view.setCloseOnPicked(false); // Prevents closing of dialog when 'pick' is clicked
  ```

- **3. Getting values**
  ```java
  view.getAlpha();
  view.getRed();
  view.getGreen();
  view.getBlue();
  view.getColor(); // returns int value of current color
  view.getHexColorCode(); // if withAlpha then with alpha hex value else without it
  view.getHexWithAlpha();
  view.getHexWithoutAlpha();
  ```

- **4. Callback**
Get callback when 'pick' is clicked
  ```java
  view.setColorPickerCallback(new ColorPickerDialog.ColorPickerCallback(){
    @Override
    public void onColorPicked(int color, String hexColorCode)
    {
      Toast.makeText(getApplicationContext(), "int[" + color + "] hex[" + hexColorCode + "]", Toast.LENGTH_LONG).show();
    }
  });
  ```

## License
- [The MIT License](https://github.com/itsaky/MaterialColorPicker/blob/master/LICENSE)
