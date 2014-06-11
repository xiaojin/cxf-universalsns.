Environment Setup

1, install maven
a, download maven unzip it, you should download zip file on windows.
b, set M2_HOME system variable and add M2_HOME\bin to PATH
2, unzip the source code zip file to any folder you prefer. For example: /home/cxfuniversalsns/
3, cd to /home/cxfuniversalsns/, open command line window and run this command: 

mvn jetty:run

To Deploy the application on Amazon EC2 server:

mvn jetty:run -Denv=prod

*It will take a while to download various jar files for the first time.
*mvn is an executable which is on your PATH. 

