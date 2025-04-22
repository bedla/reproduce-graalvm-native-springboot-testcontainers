
Steps to reproduce:

1. Build with native profile:

```bash
mvn -DskipTests=true clean install -P native
```

2. Build Docker image:

```bash
mvn -DskipTests=true build-image-no-fork -P native
```

3. Run Docker image:

```bash
docker run \
-e LOGGING_LEVEL_ORG_TESTCONTAINERS=DEBUG \
-e DOCKER_HOST=tcp://host.docker.internal:2375 \
demo:0.0.1-SNAPSHOT
```

Exception will be thrown:

```bash
2025-04-22T13:50:55.790Z DEBUG 1 --- [demo] [           main] org.testcontainers.DockerClientFactory   : Failure while checking for mountable file support

com.github.dockerjava.api.exception.InternalServerErrorException: Status 500: mount denied:
the source path "resource:/org/testcontainers/utility/ResourceReaper.class:/dummy:ro"
too many colons

        at org.testcontainers.shaded.com.github.dockerjava.core.DefaultInvocationBuilder.execute(DefaultInvocationBuilder.java:247) ~[na:na]
        at org.testcontainers.shaded.com.github.dockerjava.core.DefaultInvocationBuilder.post(DefaultInvocationBuilder.java:124) ~[na:na]
        at org.testcontainers.shaded.com.github.dockerjava.core.exec.CreateContainerCmdExec.execute(CreateContainerCmdExec.java:37) ~[na:na]
        at org.testcontainers.shaded.com.github.dockerjava.core.exec.CreateContainerCmdExec.execute(CreateContainerCmdExec.java:13) ~[na:na]
        at org.testcontainers.shaded.com.github.dockerjava.core.exec.AbstrSyncDockerCmdExec.exec(AbstrSyncDockerCmdExec.java:21) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at org.testcontainers.shaded.com.github.dockerjava.core.command.AbstrDockerCmd.exec(AbstrDockerCmd.java:33) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at org.testcontainers.shaded.com.github.dockerjava.core.command.CreateContainerCmdImpl.exec(CreateContainerCmdImpl.java:608) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at org.testcontainers.DockerClientFactory.runInsideDocker(DockerClientFactory.java:364) ~[na:na]
        at org.testcontainers.DockerClientFactory.runInsideDocker(DockerClientFactory.java:351) ~[na:na]
        at org.testcontainers.DockerClientFactory.checkMountableFile(DockerClientFactory.java:293) ~[na:na]
        at org.testcontainers.DockerClientFactory.isFileMountingSupported(DockerClientFactory.java:99) ~[na:na]
        at org.testcontainers.containers.ContainerDef.applyTo(ContainerDef.java:117) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at org.testcontainers.containers.GenericContainer.applyConfiguration(GenericContainer.java:783) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at org.testcontainers.containers.GenericContainer.tryStart(GenericContainer.java:381) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at org.testcontainers.containers.GenericContainer.lambda$doStart$0(GenericContainer.java:346) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at org.rnorth.ducttape.unreliables.Unreliables.retryUntilSuccess(Unreliables.java:81) ~[na:na]
        at org.testcontainers.containers.GenericContainer.doStart(GenericContainer.java:336) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at org.testcontainers.containers.GenericContainer.start(GenericContainer.java:322) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:1.20.6]
        at cz.publicstaticvoidmain.reproduce.demo.MyCommandLineRunner.run(DemoApplication.kt:54) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:na]
        at org.springframework.boot.SpringApplication.lambda$callRunner$5(SpringApplication.java:788) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:3.4.4]
        at org.springframework.util.function.ThrowingConsumer$1.acceptWithException(ThrowingConsumer.java:82) ~[na:na]
        at org.springframework.util.function.ThrowingConsumer.accept(ThrowingConsumer.java:60) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:6.2.5]
        at org.springframework.util.function.ThrowingConsumer$1.accept(ThrowingConsumer.java:86) ~[na:na]
        at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:796) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:3.4.4]
        at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:787) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:3.4.4]
        at org.springframework.boot.SpringApplication.lambda$callRunners$3(SpringApplication.java:772) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:3.4.4]
        at java.base@21.0.6/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184) ~[na:na]
        at java.base@21.0.6/java.util.stream.SortedOps$SizedRefSortingSink.end(SortedOps.java:357) ~[na:na]
        at java.base@21.0.6/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:510) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:na]
        at java.base@21.0.6/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:na]
        at java.base@21.0.6/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:na]
        at java.base@21.0.6/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174) ~[na:na]
        at java.base@21.0.6/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:na]
        at java.base@21.0.6/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:596) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:na]
        at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:772) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:3.4.4]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:325) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:3.4.4]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1361) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:3.4.4]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1350) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:3.4.4]
        at cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt.main(DemoApplication.kt:109) ~[cz.publicstaticvoidmain.reproduce.demo.DemoApplicationKt:na]
        at java.base@21.0.6/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH) ~[na:na]
```