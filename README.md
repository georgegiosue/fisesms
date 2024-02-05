# Fise SMS 🔥

![Kotlin](https://img.shields.io/badge/Kotlin-646464?&style=flat&logo=kotlin&logoColor=red-771e9d)
[![MinSDK](https://img.shields.io/badge/minSDK-23-blue?style=flat&logo=android)](https://developer.android.com/studio/)
[![Material You](https://img.shields.io/badge/Material_You-3-brightgreen?style=flat&logo=material-design)](https://material.io/)


## Available in 

Coming soon...

<a href='#'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height='80px'/></a>


## Table of Contents 📚

1. [Overview](#overview-📖)
2. [Installation](#installation-💻)
3. [Usage](#usage-📱)
4. [Contribution](#contribution-🤝)
5. [Troubleshooting](#troubleshooting-🔧)
6. [License](#license-📄)

## Overview 📖

### What is FISE?

<div style="display: flex; justify-content: start; align-items: center">
<img src="./.github/images/fise-logo.png" alt="FISE" width="80"/>
<img src="./.github/images/minem-banner.png" alt="Ministerio de Energia y Minas del Peru" width="150" height="34"/>
</div>
<br>

FISE is a social inclusion policy mechanism of the Republic of Peru, aimed at expanding the energy frontier in vulnerable segments of the population, through:

The massification of the use of natural gas (residential and vehicular) in vulnerable sectors.
The development of new supplies in the energy frontier focused on the most vulnerable populations.
Promoting access to LPG for vulnerable urban and rural sectors.
The compensation mechanism for the residential electricity tariff.

#### How does it work?

Text messages (SMS) are sent to the number `55555` with the discount code (`VALE`) and the `DNI` number of the beneficiary.

```html
fise ah01 <DNI> <VALE>
```

To check the balance, proceed as follows:

```
saldo ah01
```

The system processes the message and returns a text message with the result of the operation.

> Read more about FISE [here](https://www.fise.gob.pe/).

### What is Fise SMS?

|||
|-|-|
|<img src="./.github/images/ss_process_page.png" alt="Process page" width="950"/>|<p style="font-size: medium">Fise SMS is an android application that allows LPG (Liquefied Petroleum Gas) suppliers to <b>process discount coupons</b>, check their <b>account balance</b>, and check the latest coupons processed through transaction history. Make SMS requests quickly and easily with a user friendly interface.</p>|

## Installation 💻

To run this application, you will need to have Android Studio installed on your machine. Here are the steps to install it:

1. Download and install Android Studio from [here](https://developer.android.com/studio).
2. Clone this repository on your local machine using `git clone https://github.com/georgegiosue/fisesms.git`.

## Usage 📱

To run the application, follow these steps:

1. Open Android Studio.
2. Click on "File" -> "Open".
3. Navigate to the directory of the cloned repository and click on "Open".
4. Once the project is open, click on "Run" -> "Run 'app'".

## Contribution 🤝

Contributions are always welcome. To contribute:

1. Fork the project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## Troubleshooting 🔧

If you encounter any problems while setting up or running the application, please check the [Issues](https://github.com/georgegiosue/fisesms/issues) section of this repository to see if your issue has already been addressed. If not, feel free to open a new issue with a description of the problem you're experiencing.

## License 📄

[AGPL v3](./LICENSE)