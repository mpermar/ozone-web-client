# Ozone Web Client - Run Ozone Commands from your Browser

The Ozone Web Client is a web application that lets you run Ozone commands from your browser. It is built upon the [Ozone Java Client](https://github.com/tropo/tropo2/tree/master/ozone-java-client) library so you can run any of the API interfaces exposed by this library but additionally as it is based on Groovy you will be able to create your own Groovy scripts to create more complex applications and test cases. The following sections show some examples.

![Ozone Web Client](https://github.com/mpermar/ozone-web-client/blob/master/images/dashboard.png?raw=true)

## Building from the source

To build the Ozone Web Client you first need to download the source code from Github:

	git clone git@github.com:mpermar/ozone-web-client.git
	
And then go into the ozone-web-client folder and use [Apache Maven](http://maven.apache.org) to install:

	cd ozone-web-client
	mvn clean install
	
## Installing and running the Ozone Web Client application

Once the Ozone Web Client has been built you can find the WAR application at ozone-web-client/target/ozone-web-client.war

That WAR file can be deployed on any JEE compatible container. Best of all, you can deploy it at Prism. To deploy it on your Prism server, you only need to copy the WAR file to the apps folder:

	cd YOUR_PRISM_INSTALLATION
	cp OZONE-WEB-CLIENT-GIT-REPOSITORY/target/ozone-web-client.war apps

Finally, start your Prism instance. You should be able to see the Ozone Web Client dashboard by browsing to the following URL:

	http://localhost:8080/ozone-web-client

## Screencast

I have published a Screencast to Vimeo that shows how to build, install and use the Ozone Web Client. [Check it our here](http://www.vimeo.com/23868385) (pwd. thisisozone)

## Basic commands

The Ozone Web Client offers a set of basic commands that you can run and that will let you test your Ozone server. The most important command is **answer** that will let you answer calls. Do the following to answer a call:

- Go to the Ozone Web Client's dashboard by browsing to http://localhost:8080/ozone-web-client
- Open a sip phone and call Ozone. It depends on your port number but a normal SIP destination could be userc@localhost:5060
- You should see the incoming Offer on the sidebar at the right side of the dashboard
- Accept the incoming call by running the following command in the Ozone commands area: answer()
- The call will be answered

Once the call is answered you will be able to run the following commands:

- Say. Says a text. Ex. say('hello')
- SayAudio. Plays some song. Ex. sayAudio('http://myserver.com/song.mp3')
- Ask. Asks a question. Ex. ask('Which color do you prefer?','red,green,blue')
- Dial. Dials a number. Ex. dial('1234567899')
- Transfer. Transfers the call. Ex. transfer('123456789')
- Conference. Joins a conference. Ex. conference('abcd')
- Hangup. Hangs up the call. Ex. hangup()
	   	
** Audio Playback **

A special case is audio playback. Once you have run the sayAudio command you will be able to run some commands that let you pause, resume and stop the song that you are playing. These are the available commands:

- Stop. Stops the current song. Ex. stop()
- Resume. Resumes the current song. Ex. resume()
- Pause. Pauses the current song. Ex. pause()

## Creating more complex applications and learning the Ozone Web Client internals

The first thing you need to know if you plan to create more complex applications and tests with the Ozone Web Client is that the commands shown above are actually shorcuts to the [OzoneClient](https://github.com/tropo/tropo2/blob/master/ozone-java-client/src/main/java/com/voxeo/ozone/client/OzoneClient.java) java class methods. 

The scripts console available in the dashboard is in fact a Groovy engine that is available to parse your source code and run it in the Server. Internally, we are exposing an instance of OzoneClient and when you run one of the shorcuts from above then we fetch the OzoneClient instance and we run that command on it. That's why you can easily run commands like 'say', 'ask' and so on. That OzoneClient instance is also accessible if you use the variable name 'client'. Like for example, the following commands are compatible:

	say('hello')
	client.say('hello')

	dial('123456789')
	client.dial('123456789')

Obviously, you can use the OzoneClient instance to access to all the methods available in the [OzoneClient](https://github.com/tropo/tropo2/blob/master/ozone-java-client/src/main/java/com/voxeo/ozone/client/OzoneClient.java) class. 

Like for example you could use the callId parameter of the different OzoneClient's methods to invoke say commands on different calls:

	client.say('hello call 1','abcd-dfgh-jdkd-uied')       
	client.say('hello call 2','mknj-klji-bvcx-zxcv')       

** Running arbitrary code **

As I mentioned earlier, the Ozone Web Client's scripts console is actually a Groovy console that expoxes an OzoneClient instance and that you can take advantage of to create more complex scripts. So, for example the following would be a perfectly valid script that uses only shorcuts:

	answer()
	say('We are going to transfer your call. Bear with us')
	sayAudio('http://somewebserver/somemusic.mp3')
	transfer('tel:123456789')
	hangup()	

Or you can create "groovier" scripts:

	answer()
	def sayRef = client.say('Hello from Ozone')
	client.say("We have received a ref command from the JID ${sayRef.jid}")
		