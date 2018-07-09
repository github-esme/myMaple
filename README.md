# myMaple 
Game source files for myMaple v83, a MMORPG MapleStory based game. Uses JDK 1.7.

## How to set up the server in Linux (Centos 6.x)

Since not many people know how to set up a MapleStory private server under Linux and there's not an updated and official tutorial on how to do it, I'll teach you how. You can save about $15/m on your server cost by running the server under Linux since a license for a Windows server is not free. You will also be saving system resources by using Linux. This tutorial will work for any MapleStory version but may need some tweaking depending on your MapleStory source and the version that you're running. This tutorial will work 100% on v83 myMaple. We will be using Centos because it is the most popular Linux distribution. 

### Step One

Buy a VPS or a dedicated server from a hosting provideBuy a VPS or a dedicated server from a hosting provider. Unless you have hundreds of players, I recommend a VPS since you won't need the power of a dedicated server. I recommend [NFOServers](https://www.nfoservers.com/) for a VPS as they have been the most stable in my personal experience. People say [OVH](https://www.ovh.com/us/) is good but unless you get their dedicated server, it's a bad option. From my experiences, you will experience a slow disk I/O speed and random occasional network timeouts on their VPS services because their nodes are so overloaded. You are much better off with a different hosting provider. The main place of where you can find VPS and dedicated servers offers is at [WebHostingTalk](http://www.webhostingtalk.com/). ( Click [here](http://www.webhostingtalk.com/forumdisplay.php?f=104 to see VPS offers. Click [here](http://www.webhostingtalk.com/forumdisplay.php?f=36) to see dedicated server offers. ) VPS providers will offer different virtualization platform: XEN, OpenVZ, and KVM. It won't matter which one you choose although I recommend OpenVZ because OpenVZ is scalable, faster, and consumes less resources than other platforms. XEN and KVM is more "stable" because each VPS is contained. You also want to look for providers that offer DDoS protection because haters will DDoS your server. You can also look for providers that offer SSD drives because SSD drives will immensely increase your MySQL performance and overall speed of your server. It's a win win. Once you buy your server, every hosting provider will have the option for you to choose what OS you want on your VPS. Pick Centos **6.x**. If you have any questions about any hosting provider, feel free to post below and I'll give you my opinion on them.

### Step Two

Download [KiTTY](http://kitty.9bis.net/). You'll need this to connect to your server. In KiTTY, type in your server IP address in the *Host Name* field and your default server port should be 22. It should look like this below.

![](https://i.imgur.com/Mk191jB.png)

Then click on the *Open* button on the left. Then it should show this window below.

![](https://i.imgur.com/WBjUkHu.png)

Type in *root* and then press enter. Then type in your server password. When you type in your password, you won't be able to see what you type in so don't be confused why it seems like nothing is being typed in when you are actually typing. If you don't know what your server password is, you should have received it in the email you should have gotten when you have ordered your server.

Then you should see something like this below.

![](https://i.imgur.com/vDk7wXE.png)

If you don't see *vps104206* like in the screenshot, don't freak out. It's different for everyone. Now, enter these commands below. To paste into the command prompt, you just right click once in the command prompt. You can't use CTRL+V to paste something in the command prompt. 

```
yum update -y
yum install nano -y
yum install wget -y
reboot
```

It should take about a minute or two to finish all those and it should restart your VPS automatically. Once your VPS comes back up, proceed to Step Three.

### Step Three

Okay, let's get to actually setting up the MapleStory server. Download and install [FileZilla Client](https://filezilla-project.org/download.php?type=client). With this, you'll be able to upload files to your server. When you open the program, look at the top. You'll see something like the screenshot below.

![](https://i.imgur.com/1LL9GV9.png)

Type in *sftp://yourserverip* in the *Host* field, type *root* in the *Username* field, type your server password in the *Password* field, and type *22* in the *Port* field.

You will use FileZilla to upload all your files to the server. You just drag and drop the files from the left side to the right side to upload files. Pretty self explanatory. There's also different folder directories in the server that you can navigate to. I can't spoon feed you on every single thing. Just experiment with it. FileZilla is self explanatory. In your source code folder, create a new file called *launch.sh* (the file extension must be .sh) and save the file with the code below. You may need to change some things depending on which source you're using. This will work for the myMaple source.

```
#!/bin/sh
export CLASSPATH=".:dist/*" 
java -Dwzpath=wz/ -Xmx1024m net.server.Server
```

Then using your FileZilla client, upload your whole source code folder to the */root* directory in the server. If you don't know where that is, it should say */root* like the screenshot below. You can just type in */root* and press enter and it'll take you there. And yes, you need to include your wz files too.

![](https://i.imgur.com/NicFzQu.png)

### Step Four

You can't run your server yet because you haven't installed Java. Download [jre-7u17-linux-x64.tar.gz](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html#jre-7u17-oth-JPR) and [jdk-7u17-linux-x64.rpm](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html#jdk-7u17-oth-JPR) onto your Desktop. Then using FileZilla, upload those two files to the server in the */root* directory.

Then in the command prompt, enter the following below:

```
rpm -Uvh jdk-7u17-linux-x64.rpm
tar zxvf jre-7u17-linux-x64.tar.gz
```

### Step Five

Download and extract the JCE files to your Desktop by clicking [here](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html). Using the FileZilla client, upload (and replace if existing) both the *local_policy* and *US_export_policy* files in the following directories below.

```
/root/jre1.7.0_17/lib
/root/jre1.7.0_17/lib/ext
/root/jre1.7.0_17/lib/security
/usr/java/jdk1.7.0_17/jre/lib
/usr/java/jdk1.7.0_17/jre/lib/ext
/usr/java/jdk1.7.0_17/jre/lib/security
```

### Step Six

Let's install LAMP stack now. Enter the following commands below.

```
yum install httpd mysql-server php php-mysql epel-release phpmyadmin screen -y
service httpd start
service mysqld start
chkconfig httpd on
chkconfig mysqld on
```

### Step Seven

Then enter the command below and just read and pick the options you like. If you're confused on this, just ask.

```
/usr/bin/mysql_secure_installation
```

### Step Eight
Now, make a new file called **phpMyAdmin.conf** in your Desktop and it should contain the following code below:

```
# phpMyAdmin - Web based MySQL browser written in php# 
# Allows only localhost by default
#
# But allowing phpMyAdmin to anyone other than localhost should be considered
# dangerous unless properly secured by SSL


Alias /phpMyAdmin /usr/share/phpMyAdmin
Alias /phpmyadmin /usr/share/phpMyAdmin


<Directory /usr/share/phpMyAdmin/>
   AddDefaultCharset UTF-8


   <IfModule mod_authz_core.c>
     # Apache 2.4
     <RequireAny>
       Require ip 127.0.0.1
       Require ip ::1
     </RequireAny>
   </IfModule>
   <IfModule !mod_authz_core.c>
     # Apache 2.2
     Order Deny,Allow
     Allow from All
     Allow from 127.0.0.1
     Allow from ::1
   </IfModule>
</Directory>


<Directory /usr/share/phpMyAdmin/setup/>
   <IfModule mod_authz_core.c>
     # Apache 2.4
     <RequireAny>
       Require ip 127.0.0.1
       Require ip ::1
     </RequireAny>
   </IfModule>
   <IfModule !mod_authz_core.c>
     # Apache 2.2
     Order Deny,Allow
     Deny from All
     Allow from 127.0.0.1
     Allow from ::1
   </IfModule>
</Directory>


# These directories do not require access over HTTP - taken from the original
# phpMyAdmin upstream tarball
#
<Directory /usr/share/phpMyAdmin/libraries/>
    Order Deny,Allow
    Deny from All
    Allow from None
</Directory>


<Directory /usr/share/phpMyAdmin/setup/lib/>
    Order Deny,Allow
    Deny from All
    Allow from None
</Directory>


<Directory /usr/share/phpMyAdmin/setup/frames/>
    Order Deny,Allow
    Deny from All
    Allow from None
</Directory>


# This configuration prevents mod_security at phpMyAdmin directories from
# filtering SQL etc.  This may break your mod_security implementation.
#
#<IfModule mod_security.c>
#    <Directory /usr/share/phpMyAdmin/>
#        SecRuleInheritance Off
#    </Directory>
#</IfModule>
```

Upload and replace that *phpMyAdmin.conf* file you created to the server in the */etc/httpd/conf.d* directory.

Then enter the following command below in the command prompt:

```
service httpd restart
```

Now you should be able to access and manage your MySQL database through the web. To do that, go to your web browser and type in: http://yourserverip/phpMyAdmin

You should see something like this below:

![](https://i.imgur.com/g1sLto8.png)

For your Username, type in *root* and for your *Password*, type in your MySQL password if you created one in Step Seven. 

Then once you're logged in, click on the *Database* tab and type in your database name in the field.

![](https://i.imgur.com/XdeuRna.png)

Then click on the *Create* button. Then click you'll see the database name you created on the left sidebar. Click on the database you created and click on the *Import* tab. You should see something like the screenshot below. My database that I created was called *maple_maplelife*

![](https://i.imgur.com/GEIV4VW.png)

Click on the *Choose File* button and choose your MySQL script that you're supposed to upload for your MapleStory source. Then click on the Go button on the bottom of the page. That will upload and execute your MySQL script.

### Step Nine

Now, you're pretty much done. You have now set up your MySQL and Java and uploaded your source to the server. Now, let's start the server. Type the following in the command prompt:

```
screen
cd ENTER_YOUR_SOURCE_FOLDER_NAME_HERE
sh launch.sh
```

Hooray, you started your MapleStory server! You can use the phpMyAdmin to manage your database.

If you exit out of command prompt, your MapleStory server will not crash because we 'screened' it which is why we used the screen command when we started the MapleStory server. If you didn't use the screen command, your MapleStory server will crash if you closed the command prompt. If you want to restore the screen window, just type in the following command below:

```
screen -r
```

There's some other stuff you can do to secure your server, optimize the system, and fine-tune the MySQL, you most likely won't need it.

This tutorial was very much spoonfed so if you had a hard time following this tutorial, it's best for you to just use Windows.
