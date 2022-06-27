import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateTask : DefaultTask() {
    @Internal
    val contexts: MutableList<Context> = mutableListOf()

    @Input
    var templateDirectory: String? = null

    @Input
    var generateSourceDirectory: String? = null

    @get:InputFiles
    val inputFiles: Set<File>
        get() = contexts.flatMap { context ->
            val packagePath = context.packageName.replace('.', '/')

            context.actions.map { File(templateDirectory!!).resolve("$packagePath/${it.third}.java.ftl") }
        }.toSet()

    @get:OutputFiles
    val outputFiles: Set<File>
        get() = contexts.flatMap { context ->
            val packagePath = context.packageName.replace('.', '/')
            context.actions.map { File(generateSourceDirectory!!).resolve("$packagePath/${it.first}.java") }
        }.toSet()

    inline fun withPackage(
        packageName: String,
        action: Context.() -> Unit
    ) {
        val context = Context(packageName)
        context.action()
        contexts.add(context)
    }

    @TaskAction
    fun run() {
        val templateDirectory = File(this.templateDirectory!!)
        val generateSourceDirectory = File(this.generateSourceDirectory!!)

        templateDirectory.mkdirs()
        generateSourceDirectory.mkdirs()

        val conf = freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31)
        conf.setDirectoryForTemplateLoading(templateDirectory)
        conf.defaultEncoding = "UTF-8"

        for (context in contexts) {
            val packagePath = context.packageName.replace('.', '/')
            for (action in context.actions) {
                generateSourceDirectory.resolve("$packagePath/${action.first}.java").bufferedWriter().use {
                    conf.getTemplate("$packagePath/${action.third}.java.ftl").process(action.second, it)
                }
            }

        }
    }

    class Context(val packageName: String) {
        val actions: MutableList<Triple<String, Any, String>> = mutableListOf()

        fun generate(fileName: String, model: Any, templateName: String) {
            actions.add(Triple(fileName, model, templateName))
        }
    }
}