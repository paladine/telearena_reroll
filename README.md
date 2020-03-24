# telearena_reroll
Tele-Arena Reroll Script

## How to build?
Install [bazel](https://bazel.build/)

To build a deployable jar, run `bazel build //java/com/jeffreys/scripts/tareroll:TAReroll_deploy.jar`

## How to test?
To execute tests, run `bazel test ...`

## How to execute?
`java -jar TAReroll.jar <file with text proto of RerollRequirements>`
  
You can also run out of the repo directory, `bazel run //java/com/jeffreys/scripts/tareroll:TAReroll -- <file with text proto>`

But you're connected to a BBS playing TA, so you need to execute this in the context of my other program - [TelnetScripter](https://github.com/paladine/telnet_scripter)

## How do you run this script?
Use [TelnetScripter](https://github.com/paladine/telnet_scripter), connect to your system, and then execute the script by 
writing a wrapper script

```
#!/bin/sh

java -jar TAReroll.jar myRequirements.txt
```

## How to stop your script?
It should stop when you're done rerolling :)

Or you can kill the script process in your OS. You cannot stop it via special text commands.

## Why Java?
For maximum cross platform support. You can run this on Windows, Linux, Raspberry Pi, etc.

## Sample requirements script
Figure out the [maximum stats](https://tele-arena.tumblr.com/maxstats) you want for your race/class, and add them to a file.
See the [text proto definition](https://github.com/paladine/telearena_reroll/blob/master/java/com/jeffreys/scripts/tareroll/reroll.proto)
for the list of available fields.

Here's an example:
```
knowledge: 20
intellect: 22
physique: 20
stamina: 25
agility: 20
charisma: 5
vitality: 10
logoff_command: "=x\r\n"
```
