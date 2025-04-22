package cz.publicstaticvoidmain.reproduce.demo

import com.github.dockerjava.api.command.InspectContainerResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.utility.MountableFile
import org.testcontainers.utility.ResourceReaper
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Component
class MyCommandLineRunner : CommandLineRunner {
    override fun run(vararg args: String?) {
        println("Hello, World!")

        val mountableFile = MountableFile.forClasspathResource(
            ResourceReaper::class.java.name.replace(".", "/") + ".class"
        )
        println(mountableFile)
        println(mountableFile.filesystemPath)
        println(mountableFile.resolvedPath)

        val tempDir = createTempDirectory("license-analyzer").toFile().also {
            println("Temporary files & results in temp directory: $it")
        }

        ExecutorContainer(
            image = "alpine:latest",
            command = listOf("list -l /"),
            resultFilename = "result.txt",
            hostDirectory = tempDir,
            containerDirectory = "/my-data",
            logger = LoggerFactory.getLogger(DemoApplication::class.java)!!,
            postInit = {}
        ).initialize().use {
            it.start()
        }

        println("DA End")
    }
}


open class ExecutorContainer(
    private val image: String,
    private val command: List<String>,
    private val resultFilename: String,
    private val hostDirectory: File,
    private val containerDirectory: String,
    private val logger: Logger,
    private val postInit: (ExecutorContainer) -> Unit
) : GenericContainer<ExecutorContainer>(
    ImageFromDockerfile()
        .withDockerfileFromBuilder { builder ->
            builder
                .from(image)
                .copy("/to-copy/entrypoint.sh", "/usr/local/bin/entrypoint.sh")
                .run("chmod", "+x", "/usr/local/bin/entrypoint.sh")
                // info: here we are doing some dark-magic to make correctly work combination of Docker EXEC and SHELL forms
                .entryPoint(*arrayOf("/usr/local/bin/entrypoint.sh"))
                .build()
        }
        .withFileFromClasspath("/to-copy/entrypoint.sh", "entrypoint.sh")
) {
    fun initialize(): ExecutorContainer {
        val commandStr = buildList {
            addAll(command)
            add("> $containerDirectory/$resultFilename")
        }.joinToString(" ")

        withLogConsumer(Slf4jLogConsumer(logger))
            .waitingFor(
                LogMessageWaitStrategy().withRegEx(".*CMD FINISH.*")
                    .withStartupTimeout(30.seconds.toJavaDuration())
            )
            .withFileSystemBind(hostDirectory.canonicalPath, containerDirectory, BindMode.READ_WRITE)
            // info: here to make it consistent with ENTRYPOINT
            .withCommand("sh", "-c", commandStr)

        postInit(this)
        return this
    }

    override fun containerIsStarted(containerInfo: InspectContainerResponse?) {
        dockerClient.inspectContainerCmd(containerInfo?.id!!).exec()!!.run {
            val exitCode = this.state?.exitCodeLong
            require(exitCode == 0L) { "Container exited with error $exitCode" }
        }
    }
}