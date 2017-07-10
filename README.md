Lilt MT Adapter for WorldServer
===============================

This repository contains a MT Adapter implementation to allow WorldServer
to leverage machine translation from [Lilt](https://lilt.com) memories.

Installing and Configuring the Adapter
======================================
You can find the latest release on the "Releases" page.  Download it locally and
then deploy to WorldServer through the `Management > Administration > Customization` 
interface.

Once deployed, you can go to 
`Management > Linguistic Tool Setup > Machine Translation COnfigurations` and create
a new configuration for the adapter:

* Paste your Lilt API key into the `API Key` field
* Click `Refresh Memories` to load the list of available memories. *Note*: you
  must create memories in Lilt before they can be used from WorldServer. The
  adapter will not create them for you.
* Select the memories you wish to be usable by the adapter configuration.
* If desired, customize the `MT Match Score` value. This is the match score
  that the adapter returns to WorldServer for comparison to available TM fuzzy matches.
* Click `Save`.

You can return to edit the configuration as needed. For subsequent edits,
clicking `Refresh Memories` is not necessary -- the adapter will refresh the
list of available memories every time you open the configuration UI. However,
you will need to select them by hand for them to be used in translation.

Building the Code
=================

Okapi Components for WorldServer
--------------------------------
This project depends on one of the base components in the 
[Okapi Components for WorldServer](https://github.com/spartansw/okapi-worldserver-components)
project.  You will need to build and install this project in your maven repository.

Using the WorldServer SDK with Maven
------------------------------------
The project builds with maven.  However, the code has a compile-time dependency
on the `wssdk-server.jar` which SDL includes as part of its WorldServer
SDK distribution.  This file is not normally accessible to Maven, so you
must install it in a local repository to use it.

The project `pom.xml` references the dependency via invented artifact
and group IDs:

* Artifact ID: `com.idiominc.wssdk`
* Group ID: `wssdk-server`
* Version: We use the WS Build version, which for us is currently `10.4.3.195`. 

To install your copy of wssdk-server.jar to your local maven repository, you
can run this command from within your WSSDK distribution:

    mvn install:install-file -DgroupId=com.idiominc.wssdk \
                 -DartifactId=wssdk-server \
                 -Dversion=10.4.3.195 \
                 -Dpackaging=jar \
                 -Dfile=lib/server/wssdk-server.jar 

You can also use `mvn deploy:deploy-file` to deploy to a shared repository
for convenience.

WSSDK Compatibility and Java Versions
-------------------------------------
This code was developed and tested against the WSSDK 10.4 SDK, however it
should be expected to work on most 10.x versions of WorldServer (and possibly
even 9.x), due to the stability of the WSSDK.  You will need to update the
`ws.version` property in the root `pom.xml` to match the version you are
compiling against.

This code is written and compiled using Java 7.

Building
--------
Run `mvn install` from the root directory to build all components.

If you are using an IDE, you should still build once from the command line
in order to generate the `Version.java` file that is referenced from various
places in the code.

About
=====
This project is a joint collaboration between [Lilt](https://lilt.com), [Tableau Software](https://www.tableau.com/), and [Spartan Software, Inc.](https://spartansoftwareinc.com).

License
-------
All code is licensed under the 
[MIT License](https://opensource.org/licenses/MIT).

Contributing
------------

Questions or comments can also be sent to [opensource@spartansoftwareinc.com](mailto:opensource@spartansoftwareinc.com).
