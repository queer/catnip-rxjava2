# catnip-rxjava2

Adds support for RxJava 2 Observables to catnip.

## Usage

[Get it on Jitpack](https://jitpack.io/#queer/catnip-rxjava2).

Example usage:
```Java
catnip.extensionManager()
        .extension(RxJava2Extension.class)
        .createObservable(DiscordEvent.MESSAGE_CREATE)
        // Your ID goes here
        .filter(msg -> msg.author().id().equalsIgnoreCase("128316294742147072"))
        .forEach(msg -> catnip.logAdapter().info("{}#{} in #{}: {}", msg.author().username(),
                msg.author().discriminator(), msg.channel().asTextChannel().name(),
                msg.content()));
```