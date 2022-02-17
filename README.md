# Phantom Loader
A mod loader with mixin support for Minecraft 1.12.2

You can use this loader to load your mod remotely(Including mixin json)

## Usage
1.Put the xx-client.jar to the server path and launch server

2.Put the xx-loader.jar to the mods folder

3.Add HWIDs to the server

4.launch game


## Development
There are 2 ways to run dev client

1.Use the raw jar in mods folder(without -client or - loader)

2.Task runClient in IDE(UserDevConfiguration is ready.Class hotswap supported)


## To Client Devs
You can't obfuscate MixinPlugin FMLCoreMod InitializationManager

But If your obfuscator support mixin obf and meta-inf replace then it will be ok.

BTW ResourceCache is not safe at all.You can use ur own class loader and encrypt the download stream, protocol, confusing the loader logic

## Credits
Some ideas are from falcon loader
