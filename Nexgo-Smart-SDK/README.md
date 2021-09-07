# Nexgo-Smart-SDK

The NEXGO SmartSDK allows developers to interact with various hardware and system components on the N5/N6 devices for tasks such as reading payment cards, printing receipts on the integrated printer, updating the system time, and much more. 

> The GitHub documentation for the SmartSDK is being constantly updated. The entire SDK is documented in the 3.0.2 API document contained in this repository. Additional information and sample code will be added to this page on an ongoing page. 

Nexgo provides a .aar file that allows the library to be easily imported into Android Studio projects.

## Description of Resources
This repository contains the following items:
*  **SmartSDK (Device SDK)**
   *  This contains the library for developing applications that interact with the hardware and other system items on the device (i.e. EMV reader, setting system time, getting S/N of device, etc.)
*  **[AAR Libraries and Release Documentation](Library%20and%20Release%20Documentation/)**
    * This contains the importable AAR library that allows the developer to use the Nexgo SDK. 
* **[Sample-Applications](Sample-Applications/)** that can be imported/built as-is such as:
     *  **EMVSample** - Sample EMV application for reading data from an EMV card and performing a transaction natively. 
	 *  **MSRSample** - Sample MSR application for reading data from an MSR card (payment or RAW)
	 *  **PrinterSample** - Sample application for using the integrated printer to print receipts from an application. 
	 *  **CameraScannerSample** - Sample application for scanning barcodes using the built-in cameras on the device. 
	 *  **LEDSample** - Sample application for controlling the 4 status LEDs on the front/top right of the payment terminals. 
	 *  **LocationSample** - Sample application for retrieving the Nexgo device's location using standard Android methods.
	 *  **XgdLocationSample** - Sample application for retrieving the Nexgo device's location using Baidu location from the system.

	 
## Table of Contents
* [SmartSDK Supported Features](#smartsdk-supported-features)
* [Requirements](#requirements)
* [Importing the SDK into Android Studio Project](#importing-the-sdk-into-android-studio-project)
* [Using the SDK](#using-the-sdk)
* [LED Control](#led-control)
* [Printer](#printer)
  * [appendBarcode](#appendbarcode)
  * [appendPrnStr (String)](#appendprnstr)
  * [appendQRCode](#appendqrcode)
  * [feedPaper](#feedpaper)
  * [setGray (Darkness)](#setgray)  
  * [setLetterSpacing](#setletterspacing)
  * [setTypeface](#settypeface)
    * [Using a Custom Typeface/Font](#using-a-custom-typeface)
  * [startPrint](#startprint) 
* [PIN Entry + Secure Keyboard](#)
* [Barcode Scanning](#barcode-scanning)
  * [decode (from image)](#decode)
  * [initScanner](#initscanner)
  * [startScan](#startscan)
  * [stopScan](#stopscan)
* [Read MSR Magstripe Cards](#)
* [Read EMV Payment Cards](#)
* [Setting Datetime on Device](#)
* [Install APK in Background](#)
* [Uninstall Package in Background](#)
* [Get Device Information](#)
* [Using the Serial Port](#)
* [Beeper/Buzzer Control](#)
* [Managing HSM on Device](#)
  

## SmartSDK Supported Features

Most android features are available on the devices, however the system SDK provides additional functionality to the developer. 

The following features are included in the SmartSDK:
* Controlling the LEDs on the front of the device
* Printing to the integrated printer in the device
* Secure PINPad and Password Entry input using injected encryption keys
* Scanning 1D/2D/QR Barcodes using the integrated camera on the device
* Reading MSR cards from the magnetic swipe reader on the device
* Reading ICC cards using the ICC reader on the device
* Reading and processing EMV payment cards using the ICC reader on the device
* Setting the system clock on the device
* Installing signed applications in the background on the device
* Uninstalling APK packages from the device by package name
* Getting device information such as serial number, and other system information from the device
* Connecting and using a serial port on the device
* Controlling and using the buzzer/beeper on the device
* Reading and writing M1/Desfire cards 
* Managing the HSM on the device

## Requirements
The library is only supported on Nexgo N5/N6 devices. There is no emulator for the devices, and projects must be tested on a supported terminal if you wish to test functionality that requires using the Nexgo SmartSDK. 

## Importing the SDK into Android Studio Project
In order to make use of the Nexgo SmartSDK in your application, you first need to import the .aar library into your project.

You need to do the following:
1. Copy the aar file into your 'libs' folder
2. Add the dependency to your app build.gradle file
3. Add the 'libs' dir to your project build.grade file's repositories section

### Copy aar to 'libs' folder
Copy the .aar library file into your project directoy, into the 'libs' folder. 

This folder should be inside the 'app' directory (ROOT_PROJ_FOLDER/app/libs). 

### Add the SmartSDK to your build.gradle (app) file
Inside the build.gradle (app) file, under the dependencies{...} section, we need to add the aar as a dependency. 

Inside the `dependencies` section of the build.gradle, add the aar library file. Make sure the 'name' matches the filename of the version of the SDK you are trying to import. 
```gradle
  implementation files('libs/nexgo-smartpos-sdk-v3.03.001_20210412.aar')
```

### Add the 'libs' directory to your build.gradle (project) file's repositories section
Inside the build.gradle (project) file, add the 'libs' folder to the repositories{...} section under 'allprojects'. 

```gradle
   allprojects {
    repositories {
        google()
        jcenter()
        flatDir{
            dirs 'libs'
        }
    }
   }
```

## Using the SDK 

### DeviceEngine
Nearly all of the SDK components use an object known as the `DeviceEngine` to get an instance of other objects used to interact with the system. 

For instance, to use the printer on the terminal, we need to get an instance of the `Printer` from the DeviceEngine. 

You should create a single DeviceEngine object, and use it to get the resources from the system that you use. We will use the `APIProxy` function `getDeviceEnging(..)` to get an instance of the DeviceEnging. 

```java
DeviceEngine deviceEngine = APIProxy.getDeviceEngine(MainActivity.this);
```

Then, you can use the `deviceEngine` object, to get the other SDK components as needed. 
```java
Printer printer = deviceEngine.getPrinter();
```

### LED Control
The N series terminals have 4 color LEDs (blue, green, yellow, and red) located on the front of each terminal. These LEDs can be used to help denote information to the user of an application. For example, when an error has occurred, you can illuminate the red LED. 

To control the LEDs, you first need to get an instance of the `LEDDriver` object from the deviceEngine:
```java
   DeviceEngine deviceEngine = APIProxy.getDeviceEngine(MainActivity.this);
   LEDDriver ledDriver = deviceEngine.getLEDDriver();
```

After you get the `LEDDriver` object, you can use the `setLed(LightModeEnum enum, boolean illuminated)` to control each of the LEDs:
```java
   //Turn the red LED off
   ledDriver.setLed(LightModeEnum.RED, false);
   
   //Turn the red LED on
   ledDriver.setLed(LightModeEnum.RED, true);
```

The valid `LightModeEnum` values are as follows:
| LightModeEnum | Color |
| :--------------- | :--------------- |
| LightModeEnum.RED | RED |
| LightModeEnum.BLUE | BLUE |
| LightModeEnum.GREEN| GREEN |
| LightModeEnum.YELLOW| YELLOW |

The boolean values for `setLed(..., boolean illuminated)` are as follows:
| boolean | Action |
| :--------------- | :--------------- |
| true | LED on |
| false | LED off |

> Note: The system will turn on/off certain LEDs itself, such as green/red when charging. Once your application utilizes the LED driver to change the status of an LED - the system will not change it back.

## Printer 
The N series terminal may or may not have an integrated printer built-in to the device. For instance, the N5 has an integrated printer, and the N6 does not. For devices that contain an integrated printer, you can use the `Printer` class from the SmartSDK to print receipts directly to the integrated printer.

The flow of the printer is as follows:
Get Printer -> `initPrinter()` -> append content to printer queue -> start printing with `startPrint(..)` -> Check result in `onPrintResult(..)`

To control the Printer, you first need to get an instance of the `Printer` object from the deviceEngine:
```java
   DeviceEngine deviceEngine = APIProxy.getDeviceEngine(MainActivity.this);
   Printer printer = deviceEngine.getPrinter();
```

After getting the `printer` object, you *must* initialize the printer using the `initPrinter()` function:
```java
   //Initialize the printer
   printer.initPrinter();
```

After calling the `initPrinter()` function, you *can* (not required) call the `getStatus()` function to check if it was successful, or not. For example, if the printer is out of paper, you may want to show a message to the user to insert a new paper roll before starting the print job.
```java
   int initResult = printer.getStatus();
   switch (initResult){
       case SdkResult.Success:
           Log.d(TAG, "Printer init success");
           break;
       case SdkResult.Printer_PaperLack:
           Log.w(TAG, "Printer is out of paper");
           Toast.makeText(MainActivity.this, "Out of Paper!", Toast.LENGTH_LONG).show();
           break;
       default:
           Log.w(TAG, "Printer Init Misc Error: " + initResult);
           break;
   }
```

The possible result codes returned in `getStatus()` are as follows:
| Return Value | Meaning |
| :--------------- | :--------------- |
| SdkResult.Success | Init Successful |
| SdkResult.Printer_UnFinished | Unfinished Print Job |
| SdkResult.Printer_PaperLack | Out of Paper |
| SdkResult.Printer_Too_Hot | Printer is Too Hot |
| SdkResult.Printer_Fail | Printer Failed |
| SdkResult.Fail | General Error |


Once the `printer` has been initialized using the `initPrinter()` function, you can begin adding content to the printer queue. Nothing will be printed until the `startPrint()` method is called. 

#### appendPrnStr
Append a String of text to the printer queue. 
```java
int appendPrnStr(String text, int fontsize, AlignEnum align, boolean isBolded)
```

| Parameter | Description |
| :--------------- | :--------------- |
| text | String to be appended to the printer queue |
| fontsize | Font Size small: 16; normal: 20; large: 24; x-large: 32 |
| align | Enumerated type of [AlignEnum](#alignenum) |
| isBolded | Whether to bold the string when printed |

```java
int appendPrnStr(String text, int fontsize, AlignEnum align, boolean isBolded, LineOptionEntity ops)
```

| Parameter | Description |
| :--------------- | :--------------- |
| text | String to be appended to the printer queue |
| fontsize | Font Size small: 16; normal: 20; large: 24; x-large: 32 |
| align | Enumerated type of [AlignEnum](#alignenum) |
| isBolded | Whether to bold the string when printed |
| ops | `LineOptionEntity`:addt'l [options](#lineoptionentity) |


```java
int appendPrnStr(String leftText, String rightText, int fontsize, AlignEnum align, boolean isBolded, LineOptionEntity ops)
```

| Parameter | Description |
| :--------------- | :--------------- |
| leftText | String to print on left side |
| rightText | String to print on right side |
| fontsize | Font Size small: 16; normal: 20; large: 24; x-large: 32 |
| align | Enumerated type of [AlignEnum](#alignenum) |
| isBolded | Whether to bold the string when printed |
| ops | `LineOptionEntity` addit'l [options](#lineoptionentity)|


Sample Usage:
```java
   printer.appendPrnStr("Laguna Hills, CA", 30, AlignEnum.CENTER, false);
```


For each of the `appendPrnStr(..)`, an int is returned denoting the result of appending it to the printer queue:
| Return Value | Description |
| :--------------- | :--------------- |
| SdkResult.Success | Success |
| SdkResult.Printer_Wrong_Package | Print Packet Format Error |
| SdkResult.Printer_AddPrnStr_Fail | Failed to Set String Buffer |

#### appendBarcode
Append a traditional barcode to the printer queue. 
```java
int appendBarcode(String content, int height, int spacing, int textheight, BarcodeFormatEnum format, AlignEnum align)
```

| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| content | `String` | String value of barcode |
| height | `int` | Font Size small: 16; normal: 20; large: 24; x-large: 32 |
| spacing | `int` | Enumerated type of alignment |
| textheight | `int` | Whether to bold the string when printed |
| format | `BarcodeFormatEnum` | Enumerated type of One-dimensional code format [BarcodeFormatEnum](#barcodeformatenum) |
| align | `AlignEnum` | Enumerated type of [AlignEnum](#alignenum) |

Sample Usage
```java
   printer.appendBarcode("1234567890",
                100,
                0,
                10,
                BarcodeFormatEnum.CODE_128,
                AlignEnum.CENTER);
```

#### appendQRCode
Append two-dimensional code. 
```java
int appendQRCode(String content, int width, int height, AlignEnum align)
```

| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| content | `String` | String value of barcode |
| width | `int` | Two-dimensional code printing width; range 1-384 |
| height | `int` | Two-dimensional code height for printing; ranges greater than 0 |
| align | `AlignEnum` | Enumerated type of [AlignEnum](#alignenum) |

Append two-dimensional code. 
```java
int appendQRCode(String content, int width, int height, int version, int level, AlignEnum align)
```

| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| content | `String` | String value of barcode |
| width | `int` | Two-dimensional code printing width; range 1-384 |
| height | `int` | Two-dimensional code height for printing; ranges greater than 0 |
| version | `int` | QR code version is 1-40 |
| level | `int` | Error correction level, from low to high, 0-3 |
| align | `AlignEnum` | Enumerated type of [AlignEnum](#alignenum) |

Sample Usage:
```java
   printer.appendQRcode("Sample QR Code for the PrintSample Application!",
           384,
           7,
           3,
           AlignEnum.CENTER);
```

#### feedPaper
Feed a defined length of paper. This will always occur at the beginning of the print job. 
```java
void feedPaper(int pixels)
```
| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| pixels | `int` | Paper length is in pixels; range of greater than or equal to 0; if the user has not set, the default value equals 0 |


#### startPrint
Initiates the print job; printer will print what is in the printer queue. 
```java
int startPrint(boolean rollPaperEnd, OnPrintListener listener)
```
| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| rollPaperEnd | `boolean` | Advance to the end of the paper automatically; true: yes, false: no |
| listener | `OnPrintListener` | The callback interface after printing is complete |

Return Value:
| Return Value | Meaning |
| :--------------- | :--------------- |
| SdkResult.Success | Operation successful; listener can successfully callback |
| SdkResult.Printer_Busy | Printer is busy |
| SdkResult.Printer_Print_Fail | Print data is emtpy |
| SdkResult.Param_In_Invalid | Parameter is invalid |

#### setLetterSpacing
Set the spacing between the print lines.
```java
int setLetterSpacing(int value)
```
| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| value | `int` | Line spacing is in pixels; the default value equals 4 |

#### setGray
Sets the darkness of the print job. 
```java
void setGray(GrayLevelEnum level)
```
| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| level | `GrayLevelEnum` | Establish gray value; if the user has not set, the default value is LEVEL_0. The higher the grayscale, the darker the print font, the slower the print speed. See [GrayLevelEnum](#graylevelenum) |

#### setTypeface
Set the font type.
```java
void setTypeface(Typeface typeface)
```
| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| typeface | `Typeface` | Android SDK Typeface. Can use external fonts. |

#### Using a custom Typeface
You can use an external font using the steps below:
1. Download the font you want to use
2. Package the font .ttf file into the Android Project's "assets" folder
3. Create a `Typeface` object representing the external font from the font file
4. Set the printer to use the new `Typeface` object
```java
  //Let's use an external font from the file in our 'assets' directory
  AssetManager am = getApplicationContext().getAssets();
  Typeface xmas = Typeface.createFromAsset(am, "Christmas.ttf");
  printer.setTypeface(xmas);
```


## Barcode Scanning
The N series terminals have integrated cameras located on the front and back of the devices. The SDK can utilize these cameras to act as 'scanners' to read barcodes. You can use the `Scanner` class from the SmartSDK to handle scanning these barcodes.

The flow of the scanner is as follows:
Get Scanner -> `initScanner()` -> call `startScan(..)` -> Check result in `onPrintResult(..)` OnScannerListener -> call stopScan(..)

To control the Scanner, you first need to get an instance of the `Scanner` object from the deviceEngine:
```java
   DeviceEngine deviceEngine = APIProxy.getDeviceEngine(MainActivity.this);
   Scanner scanner = deviceEngine.getScanner();
```

Configure the `ScannerCfgEntity` object that is passed to the Scanner object when calling the initScanner(..) function:
```java
  //Configure the ScannerCfgEntity to pass to initScanner(..) function
  final ScannerCfgEntity scannerConfigEntity = new ScannerCfgEntity();
  scannerConfigEntity.setAutoFocus(true);
  scannerConfigEntity.setUsedFrontCcd(false);
  scannerConfigEntity.setBulkMode(false);
  scannerConfigEntity.setInterval(1000);
```

Call the `scanner.initScanner(ScannerCfgEntity scannerConfigEntity, onScannerListener listener)` function to initialize the Scanner object.
```java
  scanner.initScanner(scannerConfigEntity, MainActivity.this);
```

After calling the `initScanner(..)` function, the callback function `onInitResult(int resultCode)` will be called. From here, we can determine whether or not the scanner was initialized properly or not. If so, we can call the `startScan(..)` function to begin scanning for barcodes.
```java
  @Override
  public void onInitResult(int resultCode) {
      switch (resultCode) {
          case SdkResult.Success:
              Log.d(TAG, "Scanner Init Success");

              Log.d(TAG, "Calling startScan(..)");
              scanner.startScan(60, MainActivity.this);
              break;
          default:
              Log.e(TAG, "Scanner Failed to Init: " + resultCode);
              break;
        }
    }
```

After you call the `startScanner(..)` function, the camera will appear and scan for barcodes. After the camera scanner exits (from getting barcode, error, or cancel) - the callback functiononScannerResult(int resultCode, String s)` will be called. From here, we can determine whether or not the scanner completed successfully. If so, we can get the barcode as a String - otherwise can handle the error.
```java
  @Override
  public void onScannerResult(int resultCode, String s) {
      switch (resultCode) {
          case SdkResult.Success:
              Log.d(TAG, "Got Code: " + s);
              break;
          case SdkResult.Scanner_Customer_Exit:
              Log.d(TAG, "Requested Exit Scanner OK.");
              break;
          case SdkResult.Scanner_Other_Error:
              Log.e(TAG, "Got Scanner Error OtherError");
              break;
          default:
              Log.e(TAG, "Other general error.");
      }
      scanner.stopScan();
  }
```

* Each time you use the scanner object, you should call the `stopScan()` function after getting a result in `onScannerResult(..)` - even if the result is an error.
* Each time you begin a 'new' scan, you must go through the `initScanner(..)` function/flow again.

#### decode
Decode a scanned barcode image.
```java
String decode(byte[] imageData, int imageWidth, int imageHeight)
```

| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| imageData | `byte[]` | Image, date type is YUV420SP |
| imageWidth | `int` | Image width |
| imageHeight | `int` | Image height |

Return Value (`String`):
| Result | Value |
| :--------------- | :--------------- |
| Success | `String` representation of decoded image |
| Failed | Empty `String` |

> Note: This method is used to decode existing barcode images. If, for example, you are scanning a traditional barcode, the barcodes content will be returned directly inside the `onScannerResult(..)` callback function.

#### initScanner
Initialize the scanner, which includes passing the scanner configuration and scanner listener. This must be done before the scanner can be used. 
```java
int initScanner (ScannerCfgEntity cfgEntity, OnScannerListener listener)
```

| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| cfgEntity | `ScannerCfgEntity` | The scanner configuration object. See [ScannerCfgEntity](#scannercfgentity) |
| listener | `OnScannerListener` | The callback interface for the scanner |


#### startScan
Start scanning for a barcode. When a barcode is detected, it will be available from the `OnScannerListener` 
```java
int startScan (int timeout, OnScannerListener listener)
```

| Parameter | Type | Description |
| :--------------- | :--------------- | :--------------- |
| timeout | `int` | Scan code timeout in seconds; recommended value 60 |
| listener | `OnScannerListener` | The callback interface for the scanner |

#### stopScan();
Stop scanning for a barcode. 
```java
void stopScan()
```


## Reference

#### AlignEnum
`AlignEnum` contains the following possible options:
| Enumeration Name | Description |
| :--------------- | :--------------- |
| LEFT | Align Left |
| RIGHT | Align Right |
| CENTER | Align Center |

```java
  AlignEnum.LEFT
```

#### BarcodeFormatEnum
`BarcodeFormatEnum` contains the following possible options:

| Format Name |
| :--------------- |
| AZTEC |
| CODABAR |
| CODE_39 |
| CODE_93 |
| CODE_128 |
| DATA_MATRIX |
| EAN_8 |
| EAN_13 |
| ITF |
| MAXICODE |
| PDF_417 |
| QR_CODE |
| RSS_14 |
| RSS_EXPANDED |
| UPC_A |
| UPC_E |
| UPC_EAN_EXTENSION |

```java
  BarcodeFormatEnum.UPC_A
```

#### GrayLevelEnum
The higher the grayscale, the darker the print font, the slower the print speed.

`GrayLevelEnum` contains the following possible options:
| Enumeration Name | Description |
| :--------------- | :--------------- |
| LEVEL_0 | Primary grayscale |
| LEVEL_1 | Secondary grayscale |
| LEVEL_2 | Tertiary grayscale |

```java
  GrayLevelEnum.LEVEL_0
```

#### LineOptionEntity
The `LineOptionEntity` object is used to configure additional printing settings. 
`LineOptionEntity` takes the following attributes:
| Attribute | Type | Description |
| :--------------- | :--------------- | :--------------- |
|  isUnderline | `boolean` | Whether text should be underlined |
|  marginLeft | `int` | Left margin size |


#### ScannerCfgEntity
The `ScannerCfgEntity` object is used to configure the settings for the Scanner object, such as whether or not to use auto-focus, etc.
`ScannerCfgEntity` takes the following attributes:
| Attribute | Type | Description |
| :--------------- | :--------------- | :--------------- |
|  isUsedFrontCcd | `boolean` | Whether to use the front camera; if only back camera, then open the back camera by default |
|  isBulkMode | `boolean` | Whether to use continuous scan mode, (opens the scan after the success scan; does not exit the interface |
|  i nterval | `int` | Continuous scan code interval, in milliseconds; default 1000 |
|  isAutoFocus | `boolean` | Whether to enable auto-focus |
