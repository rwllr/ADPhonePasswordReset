# Self Service Password Reset By Phone
A web application that allows self service of AD accounts over the phone.
The user enters their account details, and they receive a call to their number listed in the directory with a new password.

## What It Does
The user puts in the details of their account, including email address and phone number.
The application searches Active Directory if these details are correct. 
If they are, the user receives a phone call with a newly generated password.

## How To Use
Modify the config.properties to suit your environment, ensuring the username and password provided has permissions to reset passwords.

## Disclaimer
This is 100% not production ready. 

The fact there is no validation or normalisation on the input fields is probably just the start. 
This was intended purely as a proof of concept for others to see and implement/modify/reuse.