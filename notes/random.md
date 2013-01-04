# Random notes for development.

From the TS6 upstream docs:

```
Sends capabilities of the server. This must include QS and ENCAP. It is also
strongly recommended to include EX, CHW, IE and KNOCK, and for charybdis TS6
also SAVE and EUID. For use with services, SERVICES and RSFNC are strongly
recommended.
```

A simple way to gain a services connection to charybdis:

```
PASS foobar TS 6 :00A
CAPAB :QS ENCAP EX CHW IE KNOCK TS6 SAVE EUID
SERVER simpleserv.tenthbit.net 1 :SimpleServ
SVINFO 6 3 0 :1357296902
```

Remember to replace the epoch timestamp.

You can join clients like this:

```
:00A EUID NyanCat 1 1357295900 +ioDS NyanCat simpleserv.tenthbit.net 0 00AAAAAAB * * :Meow
```

Remember to handle PING/PONG.