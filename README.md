# Fise SMS 🔥

![Kotlin](https://img.shields.io/badge/Kotlin-646464?&style=flat&logo=kotlin&logoColor=red-771e9d)
[![MinSDK](https://img.shields.io/badge/minSDK-23-blue?style=flat&logo=android)](https://developer.android.com/studio/)
[![Material You](https://img.shields.io/badge/Material_You-3-brightgreen?style=flat&logo=material-design)](https://material.io/)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/georgegiosue/fisesms/total)
[![Android CI / Publish Snapshot](https://github.com/georgegiosue/fisesms/actions/workflows/publish-snapshot.yml/badge.svg?branch=master)](https://github.com/georgegiosue/fisesms/actions/workflows/publish-snapshot.yml)
[![Android CI / Publish Release](https://github.com/georgegiosue/fisesms/actions/workflows/publish-release.yml/badge.svg?branch=master)](https://github.com/georgegiosue/fisesms/actions/workflows/publish-release.yml)

#### FiseVision API

![Docker Pulls](https://img.shields.io/docker/pulls/5george/fisesms-api)
![Docker Image Size](https://img.shields.io/docker/image-size/5george/fisesms-api)

Container Image: https://hub.docker.com/r/5george/fisesms-api

## Available in

Coming soon...

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" alt="Google Play" height="80">](https://github.com/georgegiosue/fisesms)
&nbsp;
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://github.com/georgegiosue/fisesms)
&nbsp;

## Table of Contents

1. [Overview](#overview)
2. [Installation](#installation)
3. [Contribution](#contribution)
4. [Troubleshooting](#troubleshooting)
5. [Privacy](#privacy)
6. [License](#license)

## Overview

### What is FISE?

<div style="display: flex; justify-content: start; align-items: center">
<img src="./.github/images/fise-logo.png" alt="FISE" width="80"/>
<img src="./.github/images/minem-banner.png" alt="Ministerio de Energia y Minas del Peru" width="150" height="34"/>
</div>
<br>

FISE is a social inclusion policy mechanism of the Republic of Peru, aimed at expanding the energy
frontier in vulnerable segments of the population, through:

The massification of the use of natural gas (residential and vehicular) in vulnerable sectors.
The development of new supplies in the energy frontier focused on the most vulnerable populations.
Promoting access to LPG for vulnerable urban and rural sectors.
The compensation mechanism for the residential electricity tariff.

#### How does it work?

Text messages (SMS) are sent to the number `+55555` with the discount code (`VALE`) and the `DNI`
number of the beneficiary.

```html
fise ah01
<DNI>
    <VALE>
```

To check the balance, proceed as follows:

```
saldo ah01
```

The system processes the message and returns a text message with the result of the operation.

> Read more about FISE [here](https://www.fise.gob.pe/).

### What is Fise SMS?

|                                                                            |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
|----------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img src="./.github/images/fise-demo.jpg" alt="Process page" width="950"/> | <p style="font-size: medium">Fise SMS is a cutting-edge Android application designed to optimize the management of FISE discount coupons for LPG (Liquefied Petroleum Gas) suppliers. By harnessing advanced computer vision and AI-powered inference through a PyTorch model, the app seamlessly extracts critical information, such as coupon numbers and DNI, from images captured via the device's camera. With an intuitive interface, Fise SMS streamlines coupon processing, provides real-time account balance updates, and maintains a comprehensive transaction history, ensuring efficiency and precision in operations. |

For more information, visit https://fisesms.georgegiosue.dev

## Installation

⬇️ You can download the latest version of the application in **.apk** by clicking the following
link:

[Fise SMS latest release](https://github.com/georgegiosue/fisesms/releases/latest)

## Contribution

Contributions are always welcome. To contribute:

1. Fork the project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## Troubleshooting

If you encounter any problems while setting up or running the application, please check
the [Issues](https://github.com/georgegiosue/fisesms/issues) section of this repository to see if
your issue has already been addressed. If not, feel free to open a new issue with a description of
the problem you're experiencing.

For further assistance or if you have specific questions, you can also contact me by email
at [peraldonamoc@gmail.com](mailto:peraldonamoc@gmail.com).

## Privacy

Available in https://fisesms.georgegiosue.dev/privacy

## License

[AGPL v3](./LICENSE)
