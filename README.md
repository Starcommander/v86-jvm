# v86-jvm
Run Java-apps on web with v86 emu
see https://java-on-web.org/examples
web https://java-on-web.org/v86/
Possible optional values attached to url:
- profile: Allowed is jre-18 and for internal use jre-18-boot
- ws: A websocket server that runs websockproxy that allows internet access from vm.
- jar: The jar-file-url to run. (currently http:// only)
- log: Shows a logging textbox for debugging purpose

**build_v86.sh**

We clone the v86 emu project and patch it.

**build_image.sh**

Then we download a tiny-core-linux image and build a disk.<br/>
The disk is extended with the java virtual machine (jre) of zulu

**publish.sh**

We publish the project and run the image on
java-on-web.org/examples
- Mounting the disk sda1 to /mnt/sda1
- Executing the script startup.sh

Now we can save the state for faster startup

**Source of v86 emu:**
https://github.com/copy/v86
