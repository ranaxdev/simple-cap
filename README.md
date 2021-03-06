
[![LinkedIn][linkedin-shield]][linkedin-url]
[![Apache License][license-shield]][license-url]
[![Twitter][twitter-shield]][twitter-url]


<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/ranaxdev/simple-cap">
    <img src="res/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">simple-cap</h3>

  <p align="center">
    A super lightweight screen capturing/cropping tool for your image sharing needs.
    <br />
    <br />
    <a href="https://youtu.be/zfDVTm_fjp0">Video Demo</a>
    ·
    <a href="https://github.com/ranaxdev/simple-cap/releases/tag/1.0">Download</a>
    ·
    <a href="https://github.com/ranaxdev/simple-cap/issues">Report Bug</a>
  </p>
</p>


<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]]()

Despite all the fancy screenshotting applications out there that have tons of features and customization options, I haven't really found one that matches my daily needs, so I decided to build my own.

Here's what simple-cap does:
* There are two modes to select from : Copy & Save
* Copy mode will basically let you crop the area you wish and then copy it to your clipboard. The application exits automatically after that.
* Save mode is like copy, except it will prompt you to save to a location after a crop (in PNG/JPG format) and exit the program right after as well.

That is all. I usually use screenshotting/cropping applications to copy code snippets and share them in my slack or discord servers. When I was using other screenshotting applications, I found the variety of features (such as built in image editors and just icon after icon) to be distracting from the true purpose, so I specifically made it to be super simple and close itself right after you're done (to be minimally invasive).



### Built With

This was made in raw Java using the [Swing API](https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html)


<!-- GETTING STARTED -->
## Download

As of `4/2/2021` there is a single release for the Windows x64 platform which can be downloaded from the [releases page](https://github.com/ranaxdev/simple-cap/releases/tag/1.0).

### Prerequisites

You are required to have JDK installed as this was built in Java. It needs to use the Java Runtime Environment (JRE) to launch. By default it will try to access the JRE directory from `%JAVA_HOME%/jre` (assuming `JAVA_HOME` points to the jdk directory). You can change it using the `simplecap.xml` config file (found in the installation directory). Find the `<path>` tag within the `<jre>` tag and replace it with the JRE directory path. Example:

```xml
    <jre>
    <path>%JAVA_HOME%/jre</path>
    .
    .
    .
    </jre>
  ```

### Installation

1. Launch `simplecapInstaller.exe`

2. Browse to install directory of your choice

3. Optionally create Desktop shortcut

4. Click `Next` to continue

5. Click `Install` and wait

6. Optionally launch directly from setup or browse to your install directory and launch with `simplecap.exe`



<!-- LICENSE -->
## License

Distributed under the Apache 2.0 License. See `LICENSE` for more information.


<!-- CONTACT -->
## Contact

S.R. Qaiser - [@pitu_dev](https://twitter.com/pitu_dev) - sc21srq@leeds.ac.uk

Project Link: [https://github.com/ranaxdev/simple-cap](https://github.com/RanaSharjeel/simple-cap)



<!-- MARKDOWN LINKS & IMAGES -->
[linkedin-shield]: https://img.shields.io/badge/-LINKEDIN-blue?logo=linkedin
[linkedin-url]: https://www.linkedin.com/in/sharqais/
[license-shield]: https://img.shields.io/badge/-LICENSE-red?logo=apache
[license-url]: https://github.com/RanaSharjeel/simple-cap/blob/main/LICENSE
[twitter-shield]: https://img.shields.io/badge/-TWITTER-lightgrey?logo=twitter
[twitter-url]: https://twitter.com/pitu_dev
[product-screenshot]: https://github.com/RanaSharjeel/simple-cap/blob/main/res/Capture.PNG
