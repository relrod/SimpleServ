# SimpleServ

A very minimalistic set of IRC services written for Charybdis IRCD in Scala.

# NOTE: DO NOT USE THESE RIGHT NOW!

**Really** -- they don't even exist or compile yet. Everything that is committed
is a rough approximation of how it will look when actually implemented.

# The How

You can very easily rip out and replace any part of SimpleServ.
In the configuration file, you'll find blocks which you can add and/or remove,
which control the services that are active, and which classes they point to.

For example:

```
services.enabled = [
  'chanserv',
  'nickserv'
]
chanserv.class = me.elrod.SimpleServ.ChanServ
chanserv.nick = ChanServ
...
nickserv.class = my.special.NickServ.Implmementation
nickserv.nick = MyNickServ
nickserv.my_custom_setting = true
...
uplink.server = myuplink.mynetwork.net
uplink.port = 6667
uplink.password = changeme123123please
uplink.ssl = false
```

Once you implement what you want, and configure it how you want, simply run
`sbt run` and watch SBT do its magic.

More documentation will be coming once the services actually exist, but the idea
is that you will extend the `Service` trait, which will use the current protocol
driver (which is, itself, an extension of `LinkProtocol`).

# License

Apache 2.
