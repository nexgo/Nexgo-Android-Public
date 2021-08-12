# Nexgo-Android-Public

![Nexgo, Inc](/res/img/nexgo_logo_blue.png)

Nexgo is focused on providing hardware and software solutions to the Payments industry. Our current line of point-of-sale terminals runs a customized and hardended version Android operating system that has been certified with PCI. Due to the prevelence of Android in the "wild", outside developers will find it exceedingly easy to create/port software solutions on the Nexgo devices. 

This is the root directory containing the various resources for developing applications and integration solutions on Nexgo's Android based payment terminals.


This repository  will focus on [2] main scenarios:
1. Semi-Integration ("SmartConnect")
   1. Use Semi-Integration to allow 3rd parties to easily take payments using our pre-certified payment applications. Our Semi-Integration solution is called *SmartConnect* on the Android devices, and it allows initiating payment transactions by sending a message to our payment application over standard Android 'Intents' or standard TCP. 
1. Native Application Development (i.e. requiring device SDK)
   1. Use our device SDK to allow 3rd parties to create their own solutions from the ground up using the integrated  MSR/EMV/NFC readers, printer, and other hardware on the device.

Contents:
  * **[Semi-Integration API](Semi-Integration/) (SmartConnect API) -** *The Nexgo API used for performing Semi-Integrated transactions using the Nexgo pre-certified payment applications.* 
    *  Sample Applications
	*  Documentation
  * **[Nexgo Device SDK](Nexgo-Smart-SDK/) -** *The Nexgo Device SDK used for accessing the terminals hardware (i.e. printer, MSR, EMV) and other integrated solutions.* 
    *  Sample Applications
	*  Documentation
	*  SDK Library
  * **[Development Process](Development%20Process/) -** *Information for developers on the flow for application development, including how distribution works on Nexgo and how to sign applications for running on release devices.*
    *  Application Signing
	*  Debug / Release Mode Information
	*  Development Considerations
	*  Application Update / Distribution Informaion
  * FAQ

This repository will provide the information necessary to get you off the ground and to begin integrating into the Nexgo family of devices. 

You agree to abide by the license contained within. 

For questions, please contact sdk@nexgo.us.