# EditTextDecimal
## version 1.2

![](https://img.shields.io/badge/Java-7-orange.svg)
![](https://img.shields.io/badge/Android-aar-green.svg)

EditText Decimal is custom EditText for show decimal with comma(,) and validation of precision & scale

![Screenshot](https://github.com/lordgift/EditTextDecimal/blob/master/Screenshot.jpg)

##Component
1. `th.in.lordgift.widget.EditTextDecimal` - Decimal with floating point validation.
2. `th.in.lordgift.widget.EditTextIntegerComma` - Integer format with comma(,) and validate numberLength because ~~maxLenght~~ count comma(,).
3. `th.in.lordgift.widget.EditTextDecimalComma` - mix of above.
4. `th.in.lordgift.widget.EditTextCard` - validate idCard format with hyphen(-)

##Installation

Add dependency to your `build.gradle`

```
    compile 'th.in.lordgift:edittextdecimal:1.2'
```

##Usage

Please see the demo module

1. Add namespace to your top-level layout XML
 ```xml
 xmlns:app="http://schemas.android.com/apk/res-auto"
 ```

2. Add components to layout somewhere
 ```xml
    <th.in.lordgift.widget.EditTextDecimalComma
        android:id="@+id/editText2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="12,345.67"
        app:precision="10"
        app:scale="2" />
 ```

 * `precision` - limit all digit included scale `EditTextDecimalComma` & `EditTextDecimalComma`
 * `scale` - limit digit after point(.) `EditTextDecimalComma` & `EditTextDecimalComma`
 * `numberLength` - limit all digit without comma for `EditTextIntegerComma`
 * `cardType` - can only use idCard that is thai id-card format & validation

 *DO NOT FORGET SET ATTRIBUTES FOR COMPONENT, please `CTRL+SPACE`

3. getValue() to get real value without comma(,) and hyphen(-)

