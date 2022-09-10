# v86-jvm
Run Java-apps on web with v86 emu

*build_v86.sh*
We clone the v86 emu project and patch it.
*build_image.sh*
Then we download a tiny-core-linux image and build a disk.
The disk is extended with the java virtual machine (jre) of zulu
*publish.sh*
We publish the project and run the image on
java-on-web.org/examples
- Mounting the disk sda1 to /mnt/sda1
- Executing the script startup.sh
Now we can save the state for faster startup
